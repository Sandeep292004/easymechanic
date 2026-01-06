<?php
/**
 * User Registration Endpoint (Vehicle Owners Only)
 * POST /api/auth/user_register.php
 * 
 * Body: {
 *   "name": "John Doe",
 *   "email": "john@example.com",
 *   "phone": "1234567890",
 *   "password": "password123",
 *   "vehicle_type": "Car" (optional),
 *   "vehicle_number": "ABC123" (optional)
 * }
 */

require_once __DIR__ . '/../config/headers.php';
require_once __DIR__ . '/../config/db.php';
require_once __DIR__ . '/../config/jwt.php';

// Only allow POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Method not allowed']);
    exit();
}

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

// Validate required fields
$required = ['name', 'email', 'phone', 'password'];
foreach ($required as $field) {
    if (empty($input[$field])) {
        http_response_code(400);
        echo json_encode(['success' => false, 'message' => "Field '$field' is required"]);
        exit();
    }
}

$name = trim($input['name']);
$email = trim($input['email']);
$phone = trim($input['phone']);
$password = $input['password'];
$vehicleType = isset($input['vehicle_type']) ? trim($input['vehicle_type']) : null;
$vehicleNumber = isset($input['vehicle_number']) ? trim($input['vehicle_number']) : null;

// Validate email format
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid email format']);
    exit();
}

// Validate password length
if (strlen($password) < 6) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Password must be at least 6 characters']);
    exit();
}

// Validate phone number (basic validation)
if (strlen($phone) < 10) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid phone number']);
    exit();
}

// Hash password
$hashedPassword = password_hash($password, PASSWORD_DEFAULT);

try {
    // Register as User
    $stmt = $db->prepare("INSERT INTO users (name, email, phone, password, vehicle_type, vehicle_number) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("ssssss", $name, $email, $phone, $hashedPassword, $vehicleType, $vehicleNumber);
    
    if ($stmt->execute()) {
        $userId = $db->getLastInsertId();
        $token = JWT::generateToken($userId, 'user');
        
        echo json_encode([
            'success' => true,
            'message' => 'User registered successfully',
            'data' => [
                'user_id' => intval($userId),
                'name' => $name,
                'email' => $email,
                'phone' => $phone,
                'vehicle_type' => $vehicleType,
                'vehicle_number' => $vehicleNumber,
                'user_type' => 'user',
                'token' => $token
            ]
        ]);
    } else {
        $conn = $db->getConnection();
        if ($conn->errno === 1062) {
            http_response_code(409);
            echo json_encode(['success' => false, 'message' => 'Email already registered']);
        } else {
            throw new Exception($conn->error);
        }
    }
    $stmt->close();
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Registration failed',
        'error' => $e->getMessage()
    ]);
}


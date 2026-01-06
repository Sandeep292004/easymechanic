<?php
/**
 * Mechanic Registration Endpoint (Mechanics Only)
 * POST /api/auth/mechanic_register.php
 * 
 * Body: {
 *   "name": "Mechanic Name",
 *   "email": "mechanic@example.com",
 *   "phone": "1234567890",
 *   "password": "password123",
 *   "specialization": "Engine Repair" (optional),
 *   "experience_years": 5 (optional)
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
$specialization = isset($input['specialization']) ? trim($input['specialization']) : null;
$experienceYears = isset($input['experience_years']) ? intval($input['experience_years']) : 0;

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

// Validate experience years (if provided)
if ($experienceYears < 0) {
    $experienceYears = 0;
}

// Hash password
$hashedPassword = password_hash($password, PASSWORD_DEFAULT);

try {
    // Register as Mechanic
    $stmt = $db->prepare("INSERT INTO mechanics (name, email, phone, password, specialization, experience_years) VALUES (?, ?, ?, ?, ?, ?)");
    $stmt->bind_param("sssssi", $name, $email, $phone, $hashedPassword, $specialization, $experienceYears);
    
    if ($stmt->execute()) {
        $mechanicId = $db->getLastInsertId();
        $token = JWT::generateToken($mechanicId, 'mechanic');
        
        echo json_encode([
            'success' => true,
            'message' => 'Mechanic registered successfully',
            'data' => [
                'mechanic_id' => intval($mechanicId),
                'name' => $name,
                'email' => $email,
                'phone' => $phone,
                'specialization' => $specialization,
                'experience_years' => $experienceYears,
                'rating' => 0.00,
                'is_available' => true,
                'user_type' => 'mechanic',
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


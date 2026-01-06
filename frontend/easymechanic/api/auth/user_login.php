<?php
/**
 * User Login Endpoint (Vehicle Owners Only)
 * POST /api/auth/user_login.php
 * 
 * Body: {
 *   "email": "john@example.com",
 *   "password": "password123"
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
if (empty($input['email']) || empty($input['password'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Email and password are required']);
    exit();
}

$email = trim($input['email']);
$password = $input['password'];

// Validate email format
if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid email format']);
    exit();
}

try {
    // Login as User
    $stmt = $db->prepare("SELECT id, name, email, phone, password, vehicle_type, vehicle_number FROM users WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows === 0) {
        http_response_code(401);
        echo json_encode(['success' => false, 'message' => 'Invalid email or password']);
        $stmt->close();
        exit();
    }
    
    $user = $result->fetch_assoc();
    
    if (!password_verify($password, $user['password'])) {
        http_response_code(401);
        echo json_encode(['success' => false, 'message' => 'Invalid email or password']);
        $stmt->close();
        exit();
    }
    
    $token = JWT::generateToken($user['id'], 'user');
    
    echo json_encode([
        'success' => true,
        'message' => 'Login successful',
        'data' => [
            'user_id' => intval($user['id']),
            'name' => $user['name'],
            'email' => $user['email'],
            'phone' => $user['phone'],
            'vehicle_type' => $user['vehicle_type'],
            'vehicle_number' => $user['vehicle_number'],
            'user_type' => 'user',
            'token' => $token
        ]
    ]);
    $stmt->close();
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Login failed',
        'error' => $e->getMessage()
    ]);
}


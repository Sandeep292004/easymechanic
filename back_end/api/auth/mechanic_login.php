<?php
/**
 * Mechanic Login Endpoint (Mechanics Only)
 * POST /api/auth/mechanic_login.php
 * 
 * Body: {
 *   "email": "mechanic@example.com",
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
    // Login as Mechanic
    $stmt = $db->prepare("SELECT id, name, email, phone, password, specialization, experience_years, rating, is_available FROM mechanics WHERE email = ?");
    $stmt->bind_param("s", $email);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows === 0) {
        http_response_code(401);
        echo json_encode(['success' => false, 'message' => 'Invalid email or password']);
        $stmt->close();
        exit();
    }
    
    $mechanic = $result->fetch_assoc();
    
    if (!password_verify($password, $mechanic['password'])) {
        http_response_code(401);
        echo json_encode(['success' => false, 'message' => 'Invalid email or password']);
        $stmt->close();
        exit();
    }
    
    $token = JWT::generateToken($mechanic['id'], 'mechanic');
    
    echo json_encode([
        'success' => true,
        'message' => 'Login successful',
        'data' => [
            'mechanic_id' => intval($mechanic['id']),
            'name' => $mechanic['name'],
            'email' => $mechanic['email'],
            'phone' => $mechanic['phone'],
            'specialization' => $mechanic['specialization'],
            'experience_years' => intval($mechanic['experience_years']),
            'rating' => floatval($mechanic['rating']),
            'is_available' => (bool)$mechanic['is_available'],
            'user_type' => 'mechanic',
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


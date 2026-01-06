<?php
/**
 * DEPRECATED: Use user_login.php or mechanic_login.php instead
 * This endpoint is kept for backward compatibility but redirects to appropriate endpoint
 * POST /api/auth/login.php
 */

require_once __DIR__ . '/../config/headers.php';

// Only allow POST requests
if ($_SERVER['REQUEST_METHOD'] !== 'POST') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Method not allowed']);
    exit();
}

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

// Check if user_type is provided
if (empty($input['user_type'])) {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'This endpoint is deprecated. Please use /auth/user_login.php for users or /auth/mechanic_login.php for mechanics',
        'endpoints' => [
            'user_login' => '/auth/user_login.php',
            'mechanic_login' => '/auth/mechanic_login.php'
        ]
    ]);
    exit();
}

// Redirect to appropriate endpoint based on user_type
$userType = strtolower($input['user_type']);

if ($userType === 'user') {
    // Include and execute user login
    require_once __DIR__ . '/user_login.php';
} elseif ($userType === 'mechanic') {
    // Include and execute mechanic login
    require_once __DIR__ . '/mechanic_login.php';
} else {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Invalid user_type. Please use /auth/user_login.php for users or /auth/mechanic_login.php for mechanics'
    ]);
}


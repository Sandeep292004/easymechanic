<?php
/**
 * User Logout Endpoint
 * POST /api/auth/logout.php
 * 
 * Headers: Authorization: Bearer {token}
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

$token = JWT::getTokenFromHeader();

if ($token) {
    JWT::logout($token);
    echo json_encode([
        'success' => true,
        'message' => 'Logged out successfully'
    ]);
} else {
    http_response_code(400);
    echo json_encode([
        'success' => false,
        'message' => 'Token not provided'
    ]);
}


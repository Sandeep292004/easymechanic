<?php
/**
 * EASY MECHANIC API - Welcome/Health Check Endpoint
 * GET /api/index.php
 */

require_once __DIR__ . '/config/headers.php';
require_once __DIR__ . '/config/db.php';

// Health check endpoint
if ($_SERVER['REQUEST_METHOD'] === 'GET') {
    try {
        // Test database connection
        $testQuery = $db->query("SELECT 1");
        
        echo json_encode([
            'success' => true,
            'message' => 'EASY MECHANIC API is running',
            'version' => '1.0.0',
            'status' => 'healthy',
            'database' => 'connected',
            'timestamp' => date('Y-m-d H:i:s'),
            'endpoints' => [
                'authentication' => [
                    'POST /auth/user_register.php',
                    'POST /auth/mechanic_register.php',
                    'POST /auth/user_login.php',
                    'POST /auth/mechanic_login.php',
                    'POST /auth/logout.php'
                ],
                'mechanic' => [
                    'POST /mechanic/update_location.php',
                    'GET /mechanic/find_mechanics.php',
                    'POST /mechanic/toggle_availability.php'
                ],
                'requests' => [
                    'POST /requests/create_request.php',
                    'POST /requests/accept_request.php',
                    'POST /requests/complete_request.php',
                    'GET /requests/get_requests.php'
                ],
                'payment' => [
                    'POST /payment/process_payment.php',
                    'GET /payment/get_payments.php'
                ],
                'ai' => [
                    'POST /ai/troubleshoot.php'
                ]
            ]
        ]);
    } catch (Exception $e) {
        http_response_code(503);
        echo json_encode([
            'success' => false,
            'message' => 'API is running but database connection failed',
            'error' => $e->getMessage()
        ]);
    }
} else {
    http_response_code(405);
    echo json_encode([
        'success' => false,
        'message' => 'Method not allowed'
    ]);
}


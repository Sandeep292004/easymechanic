<?php
/**
 * Get Payment History
 * GET /api/payment/get_payments.php?service_request_id=1
 * 
 * Headers: Authorization: Bearer {token}
 * 
 * Query Parameters:
 * - service_request_id: Filter by service request (optional)
 */

require_once __DIR__ . '/../config/headers.php';
require_once __DIR__ . '/../config/db.php';
require_once __DIR__ . '/../config/jwt.php';

// Only allow GET requests
if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Method not allowed']);
    exit();
}

// Validate token and get user info
$tokenData = JWT::validateToken();
$userId = $tokenData['user_id'];
$userType = $tokenData['user_type'];

// Get query parameters
$requestId = isset($_GET['service_request_id']) ? intval($_GET['service_request_id']) : null;

try {
    if ($userType === 'user') {
        // Get payments for user's service requests
        if ($requestId) {
            // Verify request belongs to user
            $stmt = $db->prepare("SELECT id FROM service_requests WHERE id = ? AND user_id = ?");
            $stmt->bind_param("ii", $requestId, $userId);
            $stmt->execute();
            $result = $stmt->get_result();
            
            if ($result->num_rows === 0) {
                http_response_code(404);
                echo json_encode(['success' => false, 'message' => 'Service request not found']);
                $stmt->close();
                exit();
            }
            $stmt->close();
            
            $stmt = $db->prepare("
                SELECT p.*, sr.issue_description, sr.status as request_status
                FROM payments p
                INNER JOIN service_requests sr ON p.service_request_id = sr.id
                WHERE p.service_request_id = ? AND sr.user_id = ?
                ORDER BY p.created_at DESC
            ");
            $stmt->bind_param("ii", $requestId, $userId);
        } else {
            $stmt = $db->prepare("
                SELECT p.*, sr.issue_description, sr.status as request_status
                FROM payments p
                INNER JOIN service_requests sr ON p.service_request_id = sr.id
                WHERE sr.user_id = ?
                ORDER BY p.created_at DESC
            ");
            $stmt->bind_param("i", $userId);
        }
    } else {
        // Get payments for mechanic's completed requests
        if ($requestId) {
            $stmt = $db->prepare("
                SELECT p.*, sr.issue_description, sr.status as request_status
                FROM payments p
                INNER JOIN service_requests sr ON p.service_request_id = sr.id
                WHERE p.service_request_id = ? AND sr.mechanic_id = ?
                ORDER BY p.created_at DESC
            ");
            $stmt->bind_param("ii", $requestId, $userId);
        } else {
            $stmt = $db->prepare("
                SELECT p.*, sr.issue_description, sr.status as request_status
                FROM payments p
                INNER JOIN service_requests sr ON p.service_request_id = sr.id
                WHERE sr.mechanic_id = ?
                ORDER BY p.created_at DESC
            ");
            $stmt->bind_param("i", $userId);
        }
    }
    
    $stmt->execute();
    $result = $stmt->get_result();
    
    $payments = [];
    while ($row = $result->fetch_assoc()) {
        $payments[] = [
            'payment_id' => intval($row['id']),
            'service_request_id' => intval($row['service_request_id']),
            'issue_description' => $row['issue_description'],
            'amount' => floatval($row['amount']),
            'payment_method' => $row['payment_method'],
            'transaction_id' => $row['transaction_id'],
            'razorpay_order_id' => $row['razorpay_order_id'],
            'razorpay_payment_id' => $row['razorpay_payment_id'],
            'status' => $row['status'],
            'request_status' => $row['request_status'],
            'created_at' => $row['created_at'],
            'updated_at' => $row['updated_at']
        ];
    }
    $stmt->close();
    
    echo json_encode([
        'success' => true,
        'message' => 'Payments retrieved successfully',
        'data' => [
            'count' => count($payments),
            'payments' => $payments
        ]
    ]);
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Failed to retrieve payments',
        'error' => $e->getMessage()
    ]);
}


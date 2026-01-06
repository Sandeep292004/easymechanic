<?php
/**
 * Complete Service Request (Mechanic)
 * POST /api/requests/complete_request.php
 * 
 * Headers: Authorization: Bearer {token}
 * Body: {
 *   "request_id": 1
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

// Validate token and get user info
$tokenData = JWT::validateToken();

if ($tokenData['user_type'] !== 'mechanic') {
    http_response_code(403);
    echo json_encode(['success' => false, 'message' => 'Access denied. Only mechanics can complete requests']);
    exit();
}

$mechanicId = $tokenData['user_id'];

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

// Validate required fields
if (empty($input['request_id'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'request_id is required']);
    exit();
}

$requestId = intval($input['request_id']);

try {
    // Check if request exists and is accepted by this mechanic
    $stmt = $db->prepare("SELECT id, mechanic_id, status FROM service_requests WHERE id = ?");
    $stmt->bind_param("i", $requestId);
    $stmt->execute();
    $result = $stmt->get_result();
    
    if ($result->num_rows === 0) {
        http_response_code(404);
        echo json_encode(['success' => false, 'message' => 'Service request not found']);
        $stmt->close();
        exit();
    }
    
    $request = $result->fetch_assoc();
    $stmt->close();
    
    if ($request['mechanic_id'] != $mechanicId) {
        http_response_code(403);
        echo json_encode(['success' => false, 'message' => 'You can only complete requests assigned to you']);
        exit();
    }
    
    if (!in_array($request['status'], ['accepted', 'in_progress'])) {
        http_response_code(400);
        echo json_encode(['success' => false, 'message' => 'Service request cannot be completed in current status']);
        exit();
    }
    
    // Update request status
    $stmt = $db->prepare("UPDATE service_requests SET status = 'completed', completed_at = NOW() WHERE id = ?");
    $stmt->bind_param("i", $requestId);
    
    if ($stmt->execute()) {
        // Get updated request details
        $stmt2 = $db->prepare("
            SELECT sr.*, u.name as user_name, u.phone as user_phone, m.name as mechanic_name 
            FROM service_requests sr
            LEFT JOIN users u ON sr.user_id = u.id
            LEFT JOIN mechanics m ON sr.mechanic_id = m.id
            WHERE sr.id = ?
        ");
        $stmt2->bind_param("i", $requestId);
        $stmt2->execute();
        $result2 = $stmt2->get_result();
        $requestData = $result2->fetch_assoc();
        $stmt2->close();
        
        echo json_encode([
            'success' => true,
            'message' => 'Service request completed successfully',
            'data' => [
                'request_id' => intval($requestData['id']),
                'user_id' => intval($requestData['user_id']),
                'user_name' => $requestData['user_name'],
                'mechanic_id' => intval($requestData['mechanic_id']),
                'mechanic_name' => $requestData['mechanic_name'],
                'issue_description' => $requestData['issue_description'],
                'status' => $requestData['status'],
                'completed_at' => $requestData['completed_at'],
                'created_at' => $requestData['created_at']
            ]
        ]);
    } else {
        $conn = $db->getConnection();
        throw new Exception($conn->error);
    }
    $stmt->close();
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Failed to complete service request',
        'error' => $e->getMessage()
    ]);
}


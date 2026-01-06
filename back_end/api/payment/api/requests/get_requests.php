<?php
/**
 * Get Service Requests
 * GET /api/requests/get_requests.php?type=user&status=pending
 * 
 * Headers: Authorization: Bearer {token}
 * 
 * Query Parameters:
 * - type: "user" or "mechanic" (default: based on token)
 * - status: Filter by status (optional)
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
$requestType = isset($_GET['type']) ? strtolower($_GET['type']) : $userType;
$status = isset($_GET['status']) ? $_GET['status'] : null;

// Validate request type
if (!in_array($requestType, ['user', 'mechanic'])) {
    $requestType = $userType;
}

try {
    if ($requestType === 'user' || $userType === 'user') {
        // Get requests for user
        if ($status) {
            $stmt = $db->prepare("
                SELECT sr.*, m.name as mechanic_name, m.phone as mechanic_phone, m.specialization
                FROM service_requests sr
                LEFT JOIN mechanics m ON sr.mechanic_id = m.id
                WHERE sr.user_id = ? AND sr.status = ?
                ORDER BY sr.created_at DESC
            ");
            $stmt->bind_param("is", $userId, $status);
        } else {
            $stmt = $db->prepare("
                SELECT sr.*, m.name as mechanic_name, m.phone as mechanic_phone, m.specialization
                FROM service_requests sr
                LEFT JOIN mechanics m ON sr.mechanic_id = m.id
                WHERE sr.user_id = ?
                ORDER BY sr.created_at DESC
            ");
            $stmt->bind_param("i", $userId);
        }
    } else {
        // Get requests for mechanic
        if ($status) {
            $stmt = $db->prepare("
                SELECT sr.*, u.name as user_name, u.phone as user_phone, u.vehicle_type, u.vehicle_number
                FROM service_requests sr
                LEFT JOIN users u ON sr.user_id = u.id
                WHERE (sr.mechanic_id = ? OR sr.status = 'pending') AND sr.status = ?
                ORDER BY sr.created_at DESC
            ");
            $stmt->bind_param("is", $userId, $status);
        } else {
            $stmt = $db->prepare("
                SELECT sr.*, u.name as user_name, u.phone as user_phone, u.vehicle_type, u.vehicle_number
                FROM service_requests sr
                LEFT JOIN users u ON sr.user_id = u.id
                WHERE sr.mechanic_id = ? OR sr.status = 'pending'
                ORDER BY sr.created_at DESC
            ");
            $stmt->bind_param("i", $userId);
        }
    }
    
    $stmt->execute();
    $result = $stmt->get_result();
    
    $requests = [];
    while ($row = $result->fetch_assoc()) {
        $requestData = [
            'request_id' => intval($row['id']),
            'user_id' => intval($row['user_id']),
            'mechanic_id' => $row['mechanic_id'] ? intval($row['mechanic_id']) : null,
            'issue_description' => $row['issue_description'],
            'latitude' => floatval($row['latitude']),
            'longitude' => floatval($row['longitude']),
            'address' => $row['address'],
            'status' => $row['status'],
            'created_at' => $row['created_at'],
            'accepted_at' => $row['accepted_at'],
            'completed_at' => $row['completed_at']
        ];
        
        if ($requestType === 'user' || $userType === 'user') {
            $requestData['mechanic_name'] = $row['mechanic_name'];
            $requestData['mechanic_phone'] = $row['mechanic_phone'];
            $requestData['mechanic_specialization'] = $row['specialization'];
        } else {
            $requestData['user_name'] = $row['user_name'];
            $requestData['user_phone'] = $row['user_phone'];
            $requestData['vehicle_type'] = $row['vehicle_type'];
            $requestData['vehicle_number'] = $row['vehicle_number'];
        }
        
        $requests[] = $requestData;
    }
    $stmt->close();
    
    echo json_encode([
        'success' => true,
        'message' => 'Requests retrieved successfully',
        'data' => [
            'count' => count($requests),
            'requests' => $requests
        ]
    ]);
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Failed to retrieve requests',
        'error' => $e->getMessage()
    ]);
}


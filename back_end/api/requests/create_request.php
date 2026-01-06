<?php
/**
 * Create Service Request
 * POST /api/requests/create_request.php
 * 
 * Headers: Authorization: Bearer {token}
 * Body: {
 *   "issue_description": "Engine not starting",
 *   "latitude": 28.6139,
 *   "longitude": 77.2090,
 *   "address": "123 Main Street, City"
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

if ($tokenData['user_type'] !== 'user') {
    http_response_code(403);
    echo json_encode(['success' => false, 'message' => 'Access denied. Only users can create service requests']);
    exit();
}

$userId = $tokenData['user_id'];

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

// Validate required fields
if (empty($input['issue_description']) || empty($input['latitude']) || empty($input['longitude'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'issue_description, latitude, and longitude are required']);
    exit();
}

$issueDescription = trim($input['issue_description']);
$latitude = floatval($input['latitude']);
$longitude = floatval($input['longitude']);
$address = isset($input['address']) ? trim($input['address']) : null;

// Validate coordinates
if ($latitude < -90 || $latitude > 90 || $longitude < -180 || $longitude > 180) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid coordinates']);
    exit();
}

try {
    $stmt = $db->prepare("INSERT INTO service_requests (user_id, issue_description, latitude, longitude, address, status) VALUES (?, ?, ?, ?, ?, 'pending')");
    $stmt->bind_param("isddss", $userId, $issueDescription, $latitude, $longitude, $address);
    
    if ($stmt->execute()) {
        $requestId = $db->getLastInsertId();
        
        echo json_encode([
            'success' => true,
            'message' => 'Service request created successfully',
            'data' => [
                'request_id' => $requestId,
                'user_id' => $userId,
                'issue_description' => $issueDescription,
                'latitude' => $latitude,
                'longitude' => $longitude,
                'address' => $address,
                'status' => 'pending',
                'created_at' => date('Y-m-d H:i:s')
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
        'message' => 'Failed to create service request',
        'error' => $e->getMessage()
    ]);
}


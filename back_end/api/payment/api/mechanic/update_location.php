<?php
/**
 * Update Mechanic GPS Location
 * POST /api/mechanic/update_location.php
 * 
 * Headers: Authorization: Bearer {token}
 * Body: {
 *   "latitude": 28.6139,
 *   "longitude": 77.2090
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
    echo json_encode(['success' => false, 'message' => 'Access denied. Only mechanics can update location']);
    exit();
}

$mechanicId = $tokenData['user_id'];

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

// Validate required fields
if (empty($input['latitude']) || empty($input['longitude'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Latitude and longitude are required']);
    exit();
}

$latitude = floatval($input['latitude']);
$longitude = floatval($input['longitude']);

// Validate coordinates
if ($latitude < -90 || $latitude > 90 || $longitude < -180 || $longitude > 180) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid coordinates']);
    exit();
}

try {
    // Check if location exists for this mechanic
    $stmt = $db->prepare("SELECT id FROM mechanic_locations WHERE mechanic_id = ?");
    $stmt->bind_param("i", $mechanicId);
    $stmt->execute();
    $result = $stmt->get_result();
    $stmt->close();
    
    if ($result->num_rows > 0) {
        // Update existing location
        $stmt = $db->prepare("UPDATE mechanic_locations SET latitude = ?, longitude = ?, updated_at = NOW() WHERE mechanic_id = ?");
        $stmt->bind_param("ddi", $latitude, $longitude, $mechanicId);
    } else {
        // Insert new location
        $stmt = $db->prepare("INSERT INTO mechanic_locations (mechanic_id, latitude, longitude) VALUES (?, ?, ?)");
        $stmt->bind_param("idd", $mechanicId, $latitude, $longitude);
    }
    
    if ($stmt->execute()) {
        echo json_encode([
            'success' => true,
            'message' => 'Location updated successfully',
            'data' => [
                'mechanic_id' => $mechanicId,
                'latitude' => $latitude,
                'longitude' => $longitude,
                'updated_at' => date('Y-m-d H:i:s')
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
        'message' => 'Failed to update location',
        'error' => $e->getMessage()
    ]);
}


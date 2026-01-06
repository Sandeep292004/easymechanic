<?php
/**
 * Toggle Mechanic Availability
 * POST /api/mechanic/toggle_availability.php
 * 
 * Headers: Authorization: Bearer {token}
 * Body: {
 *   "is_available": true or false
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
    echo json_encode(['success' => false, 'message' => 'Access denied. Only mechanics can update availability']);
    exit();
}

$mechanicId = $tokenData['user_id'];

// Get JSON input
$input = json_decode(file_get_contents('php://input'), true);

// Validate required fields
if (!isset($input['is_available'])) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'is_available field is required']);
    exit();
}

$isAvailable = filter_var($input['is_available'], FILTER_VALIDATE_BOOLEAN) ? 1 : 0;

try {
    $stmt = $db->prepare("UPDATE mechanics SET is_available = ? WHERE id = ?");
    $stmt->bind_param("ii", $isAvailable, $mechanicId);
    
    if ($stmt->execute()) {
        echo json_encode([
            'success' => true,
            'message' => 'Availability updated successfully',
            'data' => [
                'mechanic_id' => $mechanicId,
                'is_available' => (bool)$isAvailable
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
        'message' => 'Failed to update availability',
        'error' => $e->getMessage()
    ]);
}


<?php
/**
 * Find Nearby Mechanics
 * GET /api/mechanic/find_mechanics.php?latitude=28.6139&longitude=77.2090&radius=10
 * 
 * Headers: Authorization: Bearer {token} (optional for users)
 * 
 * Query Parameters:
 * - latitude: User's latitude (required)
 * - longitude: User's longitude (required)
 * - radius: Search radius in kilometers (optional, default: 10)
 */

require_once __DIR__ . '/../config/headers.php';
require_once __DIR__ . '/../config/db.php';
require_once __DIR__ . '/../config/config.php';

// Only allow GET requests
if ($_SERVER['REQUEST_METHOD'] !== 'GET') {
    http_response_code(405);
    echo json_encode(['success' => false, 'message' => 'Method not allowed']);
    exit();
}

// Get query parameters
$latitude = isset($_GET['latitude']) ? floatval($_GET['latitude']) : null;
$longitude = isset($_GET['longitude']) ? floatval($_GET['longitude']) : null;
$radius = isset($_GET['radius']) ? floatval($_GET['radius']) : SEARCH_RADIUS_KM;

// Validate required parameters
if ($latitude === null || $longitude === null) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Latitude and longitude are required']);
    exit();
}

// Validate coordinates
if ($latitude < -90 || $latitude > 90 || $longitude < -180 || $longitude > 180) {
    http_response_code(400);
    echo json_encode(['success' => false, 'message' => 'Invalid coordinates']);
    exit();
}

try {
    // Haversine formula SQL query to find mechanics within radius
    // Distance calculation: 6371 is Earth's radius in kilometers
    $sql = "
        SELECT 
            m.id,
            m.name,
            m.email,
            m.phone,
            m.specialization,
            m.experience_years,
            m.rating,
            m.is_available,
            ml.latitude,
            ml.longitude,
            ml.updated_at as location_updated_at,
            (
                6371 * acos(
                    cos(radians(?)) * 
                    cos(radians(ml.latitude)) * 
                    cos(radians(ml.longitude) - radians(?)) + 
                    sin(radians(?)) * 
                    sin(radians(ml.latitude))
                )
            ) AS distance_km
        FROM mechanics m
        INNER JOIN mechanic_locations ml ON m.id = ml.mechanic_id
        WHERE m.is_available = 1
        HAVING distance_km <= ?
        ORDER BY distance_km ASC
        LIMIT 50
    ";
    
    $stmt = $db->prepare($sql);
    $stmt->bind_param("dddd", $latitude, $longitude, $latitude, $radius);
    $stmt->execute();
    $result = $stmt->get_result();
    
    $mechanics = [];
    while ($row = $result->fetch_assoc()) {
        $mechanics[] = [
            'mechanic_id' => intval($row['id']),
            'name' => $row['name'],
            'email' => $row['email'],
            'phone' => $row['phone'],
            'specialization' => $row['specialization'],
            'experience_years' => intval($row['experience_years']),
            'rating' => floatval($row['rating']),
            'is_available' => (bool)$row['is_available'],
            'latitude' => floatval($row['latitude']),
            'longitude' => floatval($row['longitude']),
            'distance_km' => round(floatval($row['distance_km']), 2),
            'location_updated_at' => $row['location_updated_at']
        ];
    }
    $stmt->close();
    
    echo json_encode([
        'success' => true,
        'message' => 'Mechanics found',
        'data' => [
            'count' => count($mechanics),
            'search_radius_km' => $radius,
            'user_location' => [
                'latitude' => $latitude,
                'longitude' => $longitude
            ],
            'mechanics' => $mechanics
        ]
    ]);
    
} catch (Exception $e) {
    http_response_code(500);
    echo json_encode([
        'success' => false,
        'message' => 'Failed to find mechanics',
        'error' => $e->getMessage()
    ]);
}


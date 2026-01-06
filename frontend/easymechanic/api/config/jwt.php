<?php
/**
 * JWT Token Management
 */

require_once __DIR__ . '/config.php';
require_once __DIR__ . '/db.php';

class JWT {
    
    /**
     * Generate JWT Token
     */
    public static function generateToken($userId, $userType) {
        $header = json_encode(['typ' => 'JWT', 'alg' => JWT_ALGORITHM]);
        $payload = json_encode([
            'user_id' => $userId,
            'user_type' => $userType,
            'iat' => time(),
            'exp' => time() + JWT_EXPIRY
        ]);
        
        $base64Header = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($header));
        $base64Payload = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($payload));
        
        $signature = hash_hmac('sha256', $base64Header . "." . $base64Payload, JWT_SECRET, true);
        $base64Signature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));
        
        $token = $base64Header . "." . $base64Payload . "." . $base64Signature;
        
        // Store token in database
        global $db;
        $expiresAt = date('Y-m-d H:i:s', time() + JWT_EXPIRY);
        $stmt = $db->prepare("INSERT INTO user_tokens (user_id, user_type, token, expires_at) VALUES (?, ?, ?, ?)");
        $stmt->bind_param("isss", $userId, $userType, $token, $expiresAt);
        $stmt->execute();
        $stmt->close();
        
        return $token;
    }
    
    /**
     * Verify JWT Token
     */
    public static function verifyToken($token) {
        if (empty($token)) {
            return false;
        }
        
        $parts = explode('.', $token);
        if (count($parts) !== 3) {
            return false;
        }
        
        list($base64Header, $base64Payload, $base64Signature) = $parts;
        
        // Verify signature
        $signature = hash_hmac('sha256', $base64Header . "." . $base64Payload, JWT_SECRET, true);
        $expectedSignature = str_replace(['+', '/', '='], ['-', '_', ''], base64_encode($signature));
        
        if ($base64Signature !== $expectedSignature) {
            return false;
        }
        
        // Decode payload
        $payload = json_decode(base64_decode(str_replace(['-', '_'], ['+', '/'], $base64Payload)), true);
        
        if (!$payload) {
            return false;
        }
        
        // Check expiration
        if (isset($payload['exp']) && $payload['exp'] < time()) {
            return false;
        }
        
        // Verify token exists in database
        global $db;
        $stmt = $db->prepare("SELECT * FROM user_tokens WHERE token = ? AND expires_at > NOW()");
        $stmt->bind_param("s", $token);
        $stmt->execute();
        $result = $stmt->get_result();
        $stmt->close();
        
        if ($result->num_rows === 0) {
            return false;
        }
        
        return $payload;
    }
    
    /**
     * Get token from request headers
     */
    public static function getTokenFromHeader() {
        $headers = getallheaders();
        
        if (isset($headers['Authorization'])) {
            $authHeader = $headers['Authorization'];
            if (preg_match('/Bearer\s+(.*)$/i', $authHeader, $matches)) {
                return $matches[1];
            }
        }
        
        return null;
    }
    
    /**
     * Validate token and return user data
     */
    public static function validateToken() {
        $token = self::getTokenFromHeader();
        
        if (!$token) {
            http_response_code(401);
            echo json_encode([
                'success' => false,
                'message' => 'Authorization token required'
            ]);
            exit();
        }
        
        $payload = self::verifyToken($token);
        
        if (!$payload) {
            http_response_code(401);
            echo json_encode([
                'success' => false,
                'message' => 'Invalid or expired token'
            ]);
            exit();
        }
        
        return $payload;
    }
    
    /**
     * Logout - invalidate token
     */
    public static function logout($token) {
        global $db;
        $stmt = $db->prepare("DELETE FROM user_tokens WHERE token = ?");
        $stmt->bind_param("s", $token);
        $stmt->execute();
        $stmt->close();
    }
}


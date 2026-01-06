<?php
/**
 * EASY MECHANIC API Configuration
 */

// Database Configuration
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');
define('DB_NAME', 'easymechanic');

// JWT Configuration
define('JWT_SECRET', 'easymechanic_secret_key_2024_change_in_production');
define('JWT_ALGORITHM', 'HS256');
define('JWT_EXPIRY', 86400); // 24 hours in seconds

// API Configuration
define('API_VERSION', 'v1');
define('BASE_URL', 'http://localhost/easymechanic/api/');

// Search Radius (in kilometers)
define('SEARCH_RADIUS_KM', 10);

// Payment Configuration (for future Razorpay integration)
define('RAZORPAY_KEY_ID', '');
define('RAZORPAY_KEY_SECRET', '');

// Timezone
date_default_timezone_set('Asia/Kolkata');

// Error Reporting (set to 0 in production)
error_reporting(E_ALL);
ini_set('display_errors', 1);


-- =====================================================
-- EASY MECHANIC Database Alterations (MySQL Compatible)
-- Version: 2.0
-- Compatible with MySQL 5.7+
-- =====================================================
-- Use this file if you already have the database and want to upgrade
-- Run this SQL in phpMyAdmin
-- =====================================================
-- NOTE: Run each section separately and check for errors
-- If a column already exists, skip that ALTER statement
-- =====================================================

USE easymechanic;

-- =====================================================
-- Section 1: Alter Users table
-- =====================================================
-- Run these one by one if you get errors

ALTER TABLE users ADD COLUMN address TEXT NULL AFTER vehicle_number;
ALTER TABLE users ADD COLUMN city VARCHAR(100) NULL AFTER address;
ALTER TABLE users ADD COLUMN state VARCHAR(100) NULL AFTER city;
ALTER TABLE users ADD COLUMN pincode VARCHAR(10) NULL AFTER state;
ALTER TABLE users ADD COLUMN profile_image VARCHAR(255) NULL AFTER pincode;
ALTER TABLE users ADD COLUMN is_active TINYINT(1) DEFAULT 1 AFTER profile_image;
ALTER TABLE users ADD COLUMN email_verified TINYINT(1) DEFAULT 0 AFTER is_active;
ALTER TABLE users ADD COLUMN phone_verified TINYINT(1) DEFAULT 0 AFTER email_verified;
ALTER TABLE users ADD COLUMN last_login TIMESTAMP NULL AFTER phone_verified;

-- Add indexes
ALTER TABLE users ADD INDEX idx_active (is_active);
ALTER TABLE users ADD INDEX idx_created (created_at);

-- =====================================================
-- Section 2: Alter Mechanics table
-- =====================================================

ALTER TABLE mechanics ADD COLUMN shop_name VARCHAR(200) NULL AFTER phone;
ALTER TABLE mechanics ADD COLUMN address TEXT NULL AFTER experience_years;
ALTER TABLE mechanics ADD COLUMN city VARCHAR(100) NULL AFTER address;
ALTER TABLE mechanics ADD COLUMN state VARCHAR(100) NULL AFTER city;
ALTER TABLE mechanics ADD COLUMN pincode VARCHAR(10) NULL AFTER state;
ALTER TABLE mechanics ADD COLUMN latitude DECIMAL(10, 8) NULL AFTER pincode;
ALTER TABLE mechanics ADD COLUMN longitude DECIMAL(11, 8) NULL AFTER latitude;
ALTER TABLE mechanics ADD COLUMN profile_image VARCHAR(255) NULL AFTER longitude;
ALTER TABLE mechanics ADD COLUMN shop_image VARCHAR(255) NULL AFTER profile_image;
ALTER TABLE mechanics ADD COLUMN is_active TINYINT(1) DEFAULT 1 AFTER is_available;
ALTER TABLE mechanics ADD COLUMN email_verified TINYINT(1) DEFAULT 0 AFTER is_active;
ALTER TABLE mechanics ADD COLUMN phone_verified TINYINT(1) DEFAULT 0 AFTER email_verified;
ALTER TABLE mechanics ADD COLUMN last_login TIMESTAMP NULL AFTER phone_verified;
ALTER TABLE mechanics ADD COLUMN total_ratings INT DEFAULT 0 AFTER rating;

-- Add indexes
ALTER TABLE mechanics ADD INDEX idx_active (is_active);
ALTER TABLE mechanics ADD INDEX idx_rating (rating);
ALTER TABLE mechanics ADD INDEX idx_location (latitude, longitude);
ALTER TABLE mechanics ADD INDEX idx_created (created_at);

-- =====================================================
-- Section 3: Alter Mechanic locations table
-- =====================================================

ALTER TABLE mechanic_locations ADD COLUMN accuracy DECIMAL(10, 2) NULL AFTER longitude;
ALTER TABLE mechanic_locations ADD INDEX idx_updated (updated_at);

-- =====================================================
-- Section 4: Alter Service requests table
-- =====================================================

ALTER TABLE service_requests ADD COLUMN city VARCHAR(100) NULL AFTER address;
ALTER TABLE service_requests ADD COLUMN state VARCHAR(100) NULL AFTER city;
ALTER TABLE service_requests ADD COLUMN pincode VARCHAR(10) NULL AFTER state;
ALTER TABLE service_requests ADD COLUMN vehicle_type VARCHAR(50) NULL AFTER pincode;
ALTER TABLE service_requests ADD COLUMN vehicle_number VARCHAR(20) NULL AFTER vehicle_type;
ALTER TABLE service_requests ADD COLUMN priority ENUM('low', 'medium', 'high', 'urgent') DEFAULT 'medium' AFTER status;
ALTER TABLE service_requests ADD COLUMN estimated_cost DECIMAL(10, 2) NULL AFTER priority;
ALTER TABLE service_requests ADD COLUMN actual_cost DECIMAL(10, 2) NULL AFTER estimated_cost;
ALTER TABLE service_requests ADD COLUMN notes TEXT NULL AFTER actual_cost;
ALTER TABLE service_requests ADD COLUMN user_rating INT NULL AFTER notes;
ALTER TABLE service_requests ADD COLUMN user_feedback TEXT NULL AFTER user_rating;
ALTER TABLE service_requests ADD COLUMN started_at TIMESTAMP NULL AFTER accepted_at;
ALTER TABLE service_requests ADD COLUMN cancelled_at TIMESTAMP NULL AFTER completed_at;
ALTER TABLE service_requests ADD COLUMN cancelled_by ENUM('user', 'mechanic') NULL AFTER cancelled_at;
ALTER TABLE service_requests ADD COLUMN cancellation_reason TEXT NULL AFTER cancelled_by;

-- Add indexes
ALTER TABLE service_requests ADD INDEX idx_priority (priority);
ALTER TABLE service_requests ADD INDEX idx_completed (completed_at);

-- =====================================================
-- Section 5: Alter Payments table
-- =====================================================

ALTER TABLE payments ADD COLUMN user_id INT NULL AFTER service_request_id;
ALTER TABLE payments ADD COLUMN mechanic_id INT NULL AFTER user_id;
ALTER TABLE payments ADD COLUMN razorpay_signature VARCHAR(255) NULL AFTER razorpay_payment_id;
ALTER TABLE payments ADD COLUMN failure_reason TEXT NULL AFTER status;
ALTER TABLE payments ADD COLUMN refund_amount DECIMAL(10, 2) NULL AFTER failure_reason;
ALTER TABLE payments ADD COLUMN refund_reason TEXT NULL AFTER refund_amount;

-- Add foreign keys (run only if foreign keys don't exist)
-- ALTER TABLE payments ADD FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
-- ALTER TABLE payments ADD FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE;

-- Add indexes
ALTER TABLE payments ADD INDEX idx_user (user_id);
ALTER TABLE payments ADD INDEX idx_mechanic (mechanic_id);
ALTER TABLE payments ADD INDEX idx_razorpay_order (razorpay_order_id);

-- =====================================================
-- Section 6: Alter User tokens table
-- =====================================================

ALTER TABLE user_tokens ADD COLUMN device_info VARCHAR(255) NULL AFTER token;
ALTER TABLE user_tokens ADD COLUMN ip_address VARCHAR(45) NULL AFTER device_info;
ALTER TABLE user_tokens ADD INDEX idx_created (created_at);

-- =====================================================
-- Section 7: Create new tables
-- =====================================================

-- Reviews table
CREATE TABLE IF NOT EXISTS mechanic_reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_request_id INT NOT NULL,
    mechanic_id INT NOT NULL,
    user_id INT NOT NULL,
    rating INT NOT NULL COMMENT 'Rating from 1 to 5',
    review_text TEXT NULL,
    is_visible TINYINT(1) DEFAULT 1 COMMENT 'Show/hide review',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (service_request_id) REFERENCES service_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_request_review (service_request_id) COMMENT 'One review per service request',
    INDEX idx_mechanic (mechanic_id),
    INDEX idx_user (user_id),
    INDEX idx_rating (rating),
    INDEX idx_visible (is_visible),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Service request images table
CREATE TABLE IF NOT EXISTS service_request_images (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_request_id INT NOT NULL,
    image_url VARCHAR(255) NOT NULL COMMENT 'Image URL/path',
    image_type ENUM('issue', 'before', 'after', 'other') DEFAULT 'issue',
    uploaded_by ENUM('user', 'mechanic') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (service_request_id) REFERENCES service_requests(id) ON DELETE CASCADE,
    INDEX idx_request (service_request_id),
    INDEX idx_type (image_type),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    user_type ENUM('user', 'mechanic') NOT NULL,
    title VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    type VARCHAR(50) NULL COMMENT 'request_accepted, payment_received, etc.',
    related_id INT NULL COMMENT 'Related service_request_id or payment_id',
    is_read TINYINT(1) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_type (user_id, user_type),
    INDEX idx_read (is_read),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Section 8: Update existing data
-- =====================================================

UPDATE users SET is_active = 1 WHERE is_active IS NULL;
UPDATE mechanics SET is_active = 1 WHERE is_active IS NULL;
UPDATE mechanics SET total_ratings = 0 WHERE total_ratings IS NULL;

-- =====================================================
-- Database Alterations Complete
-- =====================================================


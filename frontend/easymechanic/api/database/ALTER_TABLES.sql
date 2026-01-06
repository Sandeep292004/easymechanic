-- =====================================================
-- EASY MECHANIC Database Alterations
-- Version: 2.0
-- =====================================================
-- Use this file if you already have the database and want to upgrade
-- Run this SQL in phpMyAdmin
-- =====================================================

USE easymechanic;

-- =====================================================
-- Alter Users table - Add new fields
-- =====================================================
ALTER TABLE users 
ADD COLUMN IF NOT EXISTS address TEXT NULL AFTER vehicle_number,
ADD COLUMN IF NOT EXISTS city VARCHAR(100) NULL AFTER address,
ADD COLUMN IF NOT EXISTS state VARCHAR(100) NULL AFTER city,
ADD COLUMN IF NOT EXISTS pincode VARCHAR(10) NULL AFTER state,
ADD COLUMN IF NOT EXISTS profile_image VARCHAR(255) NULL AFTER pincode,
ADD COLUMN IF NOT EXISTS is_active TINYINT(1) DEFAULT 1 AFTER profile_image,
ADD COLUMN IF NOT EXISTS email_verified TINYINT(1) DEFAULT 0 AFTER is_active,
ADD COLUMN IF NOT EXISTS phone_verified TINYINT(1) DEFAULT 0 AFTER email_verified,
ADD COLUMN IF NOT EXISTS last_login TIMESTAMP NULL AFTER phone_verified;

-- Add new indexes for users
ALTER TABLE users 
ADD INDEX IF NOT EXISTS idx_active (is_active),
ADD INDEX IF NOT EXISTS idx_created (created_at);

-- =====================================================
-- Alter Mechanics table - Add new fields
-- =====================================================
ALTER TABLE mechanics 
ADD COLUMN IF NOT EXISTS shop_name VARCHAR(200) NULL AFTER phone,
ADD COLUMN IF NOT EXISTS address TEXT NULL AFTER experience_years,
ADD COLUMN IF NOT EXISTS city VARCHAR(100) NULL AFTER address,
ADD COLUMN IF NOT EXISTS state VARCHAR(100) NULL AFTER city,
ADD COLUMN IF NOT EXISTS pincode VARCHAR(10) NULL AFTER state,
ADD COLUMN IF NOT EXISTS latitude DECIMAL(10, 8) NULL AFTER pincode,
ADD COLUMN IF NOT EXISTS longitude DECIMAL(11, 8) NULL AFTER latitude,
ADD COLUMN IF NOT EXISTS profile_image VARCHAR(255) NULL AFTER longitude,
ADD COLUMN IF NOT EXISTS shop_image VARCHAR(255) NULL AFTER profile_image,
ADD COLUMN IF NOT EXISTS is_active TINYINT(1) DEFAULT 1 AFTER is_available,
ADD COLUMN IF NOT EXISTS email_verified TINYINT(1) DEFAULT 0 AFTER is_active,
ADD COLUMN IF NOT EXISTS phone_verified TINYINT(1) DEFAULT 0 AFTER email_verified,
ADD COLUMN IF NOT EXISTS last_login TIMESTAMP NULL AFTER phone_verified,
ADD COLUMN IF NOT EXISTS total_ratings INT DEFAULT 0 AFTER rating;

-- Add new indexes for mechanics
ALTER TABLE mechanics 
ADD INDEX IF NOT EXISTS idx_active (is_active),
ADD INDEX IF NOT EXISTS idx_rating (rating),
ADD INDEX IF NOT EXISTS idx_location (latitude, longitude),
ADD INDEX IF NOT EXISTS idx_created (created_at);

-- =====================================================
-- Alter Mechanic locations table
-- =====================================================
ALTER TABLE mechanic_locations 
ADD COLUMN IF NOT EXISTS accuracy DECIMAL(10, 2) NULL AFTER longitude,
ADD INDEX IF NOT EXISTS idx_updated (updated_at);

-- =====================================================
-- Alter Service requests table - Add new fields
-- =====================================================
ALTER TABLE service_requests 
ADD COLUMN IF NOT EXISTS city VARCHAR(100) NULL AFTER address,
ADD COLUMN IF NOT EXISTS state VARCHAR(100) NULL AFTER city,
ADD COLUMN IF NOT EXISTS pincode VARCHAR(10) NULL AFTER state,
ADD COLUMN IF NOT EXISTS vehicle_type VARCHAR(50) NULL AFTER pincode,
ADD COLUMN IF NOT EXISTS vehicle_number VARCHAR(20) NULL AFTER vehicle_type,
ADD COLUMN IF NOT EXISTS priority ENUM('low', 'medium', 'high', 'urgent') DEFAULT 'medium' AFTER status,
ADD COLUMN IF NOT EXISTS estimated_cost DECIMAL(10, 2) NULL AFTER priority,
ADD COLUMN IF NOT EXISTS actual_cost DECIMAL(10, 2) NULL AFTER estimated_cost,
ADD COLUMN IF NOT EXISTS notes TEXT NULL AFTER actual_cost,
ADD COLUMN IF NOT EXISTS user_rating INT NULL AFTER notes,
ADD COLUMN IF NOT EXISTS user_feedback TEXT NULL AFTER user_rating,
ADD COLUMN IF NOT EXISTS started_at TIMESTAMP NULL AFTER accepted_at,
ADD COLUMN IF NOT EXISTS cancelled_at TIMESTAMP NULL AFTER completed_at,
ADD COLUMN IF NOT EXISTS cancelled_by ENUM('user', 'mechanic') NULL AFTER cancelled_at,
ADD COLUMN IF NOT EXISTS cancellation_reason TEXT NULL AFTER cancelled_by;

-- Rename accepted_at if needed (already exists, so skip if error)
-- ALTER TABLE service_requests CHANGE accepted_at accepted_at TIMESTAMP NULL;

-- Add new indexes for service_requests
ALTER TABLE service_requests 
ADD INDEX IF NOT EXISTS idx_priority (priority),
ADD INDEX IF NOT EXISTS idx_completed (completed_at);

-- =====================================================
-- Alter Payments table - Add new fields
-- =====================================================
ALTER TABLE payments 
ADD COLUMN IF NOT EXISTS user_id INT NULL AFTER service_request_id,
ADD COLUMN IF NOT EXISTS mechanic_id INT NULL AFTER user_id,
ADD COLUMN IF NOT EXISTS razorpay_signature VARCHAR(255) NULL AFTER razorpay_payment_id,
ADD COLUMN IF NOT EXISTS failure_reason TEXT NULL AFTER status,
ADD COLUMN IF NOT EXISTS refund_amount DECIMAL(10, 2) NULL AFTER failure_reason,
ADD COLUMN IF NOT EXISTS refund_reason TEXT NULL AFTER refund_amount;

-- Add foreign keys for payments (if not exists)
-- Note: MySQL doesn't support IF NOT EXISTS for foreign keys, so run these carefully
-- ALTER TABLE payments 
-- ADD FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
-- ADD FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE;

-- Add new indexes for payments
ALTER TABLE payments 
ADD INDEX IF NOT EXISTS idx_user (user_id),
ADD INDEX IF NOT EXISTS idx_mechanic (mechanic_id),
ADD INDEX IF NOT EXISTS idx_razorpay_order (razorpay_order_id);

-- =====================================================
-- Alter User tokens table - Add new fields
-- =====================================================
ALTER TABLE user_tokens 
ADD COLUMN IF NOT EXISTS device_info VARCHAR(255) NULL AFTER token,
ADD COLUMN IF NOT EXISTS ip_address VARCHAR(45) NULL AFTER device_info,
ADD INDEX IF NOT EXISTS idx_created (created_at);

-- =====================================================
-- Create new tables (if they don't exist)
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
-- Update existing data (if needed)
-- =====================================================

-- Set default values for new fields
UPDATE users SET is_active = 1 WHERE is_active IS NULL;
UPDATE mechanics SET is_active = 1 WHERE is_active IS NULL;
UPDATE mechanics SET total_ratings = 0 WHERE total_ratings IS NULL;

-- =====================================================
-- Database Alterations Complete
-- =====================================================


-- =====================================================
-- EASY MECHANIC Database Schema
-- Version: 2.0
-- Last Updated: 2024
-- =====================================================
-- Run this SQL in phpMyAdmin to create the database and tables
-- Location: http://localhost/phpmyadmin/
-- =====================================================

CREATE DATABASE IF NOT EXISTS easymechanic CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE easymechanic;

-- =====================================================
-- Users table (Vehicle Owners)
-- =====================================================
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    vehicle_type VARCHAR(50) NULL COMMENT 'Car, Bike, Truck, etc.',
    vehicle_number VARCHAR(20) NULL,
    address TEXT NULL,
    city VARCHAR(100) NULL,
    state VARCHAR(100) NULL,
    pincode VARCHAR(10) NULL,
    profile_image VARCHAR(255) NULL COMMENT 'Profile picture URL/path',
    is_active TINYINT(1) DEFAULT 1 COMMENT 'Account status',
    email_verified TINYINT(1) DEFAULT 0 COMMENT 'Email verification status',
    phone_verified TINYINT(1) DEFAULT 0 COMMENT 'Phone verification status',
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_active (is_active),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Mechanics table (Service Providers)
-- =====================================================
CREATE TABLE IF NOT EXISTS mechanics (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20) NOT NULL,
    password VARCHAR(255) NOT NULL,
    shop_name VARCHAR(200) NULL COMMENT 'Workshop/Shop name',
    specialization VARCHAR(100) NULL COMMENT 'Engine Repair, AC Service, etc.',
    experience_years INT DEFAULT 0,
    rating DECIMAL(3,2) DEFAULT 0.00 COMMENT 'Average rating (0.00 to 5.00)',
    total_ratings INT DEFAULT 0 COMMENT 'Total number of ratings received',
    address TEXT NULL,
    city VARCHAR(100) NULL,
    state VARCHAR(100) NULL,
    pincode VARCHAR(10) NULL,
    latitude DECIMAL(10, 8) NULL COMMENT 'Shop location latitude',
    longitude DECIMAL(11, 8) NULL COMMENT 'Shop location longitude',
    profile_image VARCHAR(255) NULL COMMENT 'Profile picture URL/path',
    shop_image VARCHAR(255) NULL COMMENT 'Shop/workshop image URL/path',
    is_available TINYINT(1) DEFAULT 1 COMMENT 'Available for new requests',
    is_active TINYINT(1) DEFAULT 1 COMMENT 'Account status',
    email_verified TINYINT(1) DEFAULT 0 COMMENT 'Email verification status',
    phone_verified TINYINT(1) DEFAULT 0 COMMENT 'Phone verification status',
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_available (is_available),
    INDEX idx_active (is_active),
    INDEX idx_rating (rating),
    INDEX idx_location (latitude, longitude),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Mechanic locations table (for real-time GPS tracking)
-- =====================================================
CREATE TABLE IF NOT EXISTS mechanic_locations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    mechanic_id INT NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    accuracy DECIMAL(10, 2) NULL COMMENT 'GPS accuracy in meters',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE,
    INDEX idx_mechanic (mechanic_id),
    INDEX idx_location (latitude, longitude),
    INDEX idx_updated (updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Service requests table
-- =====================================================
CREATE TABLE IF NOT EXISTS service_requests (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    mechanic_id INT NULL,
    issue_description TEXT NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    address TEXT NULL,
    city VARCHAR(100) NULL,
    state VARCHAR(100) NULL,
    pincode VARCHAR(10) NULL,
    vehicle_type VARCHAR(50) NULL COMMENT 'Vehicle type for this request',
    vehicle_number VARCHAR(20) NULL COMMENT 'Vehicle number for this request',
    status ENUM('pending', 'accepted', 'in_progress', 'completed', 'cancelled') DEFAULT 'pending',
    priority ENUM('low', 'medium', 'high', 'urgent') DEFAULT 'medium',
    estimated_cost DECIMAL(10, 2) NULL,
    actual_cost DECIMAL(10, 2) NULL,
    notes TEXT NULL COMMENT 'Additional notes from mechanic',
    user_rating INT NULL COMMENT 'User rating (1-5) after service',
    user_feedback TEXT NULL COMMENT 'User feedback after service',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    accepted_at TIMESTAMP NULL,
    started_at TIMESTAMP NULL COMMENT 'When mechanic started work',
    completed_at TIMESTAMP NULL,
    cancelled_at TIMESTAMP NULL,
    cancelled_by ENUM('user', 'mechanic') NULL COMMENT 'Who cancelled',
    cancellation_reason TEXT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE SET NULL,
    INDEX idx_user (user_id),
    INDEX idx_mechanic (mechanic_id),
    INDEX idx_status (status),
    INDEX idx_priority (priority),
    INDEX idx_location (latitude, longitude),
    INDEX idx_created (created_at),
    INDEX idx_completed (completed_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Payments table
-- =====================================================
CREATE TABLE IF NOT EXISTS payments (
    id INT AUTO_INCREMENT PRIMARY KEY,
    service_request_id INT NOT NULL,
    user_id INT NOT NULL COMMENT 'For quick access',
    mechanic_id INT NOT NULL COMMENT 'For quick access',
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) DEFAULT 'cash' COMMENT 'cash, razorpay, card, upi',
    transaction_id VARCHAR(100) UNIQUE NULL,
    razorpay_order_id VARCHAR(100) NULL,
    razorpay_payment_id VARCHAR(100) NULL,
    razorpay_signature VARCHAR(255) NULL COMMENT 'Razorpay payment signature',
    status ENUM('pending', 'success', 'failed', 'refunded') DEFAULT 'pending',
    failure_reason TEXT NULL COMMENT 'Reason if payment failed',
    refund_amount DECIMAL(10, 2) NULL COMMENT 'Amount refunded if any',
    refund_reason TEXT NULL COMMENT 'Reason for refund',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (service_request_id) REFERENCES service_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (mechanic_id) REFERENCES mechanics(id) ON DELETE CASCADE,
    INDEX idx_request (service_request_id),
    INDEX idx_user (user_id),
    INDEX idx_mechanic (mechanic_id),
    INDEX idx_status (status),
    INDEX idx_transaction (transaction_id),
    INDEX idx_razorpay_order (razorpay_order_id),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- JWT tokens table (for token management)
-- =====================================================
CREATE TABLE IF NOT EXISTS user_tokens (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    user_type ENUM('user', 'mechanic') NOT NULL,
    token VARCHAR(500) NOT NULL,
    device_info VARCHAR(255) NULL COMMENT 'Device information',
    ip_address VARCHAR(45) NULL COMMENT 'IPv4 or IPv6 address',
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_type (user_id, user_type),
    INDEX idx_token (token),
    INDEX idx_expires (expires_at),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Reviews/Ratings table (for mechanic reviews)
-- =====================================================
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

-- =====================================================
-- Service request images table (for issue photos)
-- =====================================================
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

-- =====================================================
-- Notifications table (for push notifications)
-- =====================================================
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
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_type (user_id, user_type),
    INDEX idx_read (is_read),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- Database Setup Complete
-- =====================================================
-- All tables created successfully!
-- 
-- Next Steps:
-- 1. Test the API endpoints
-- 2. Register test users and mechanics
-- 3. Create service requests
-- 4. Test payment flow
-- 
-- =====================================================

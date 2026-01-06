-- Add UPI ID and QR Code fields to mechanics table
-- Run this SQL in phpMyAdmin to update existing database

USE easymechanic;

ALTER TABLE mechanics 
ADD COLUMN IF NOT EXISTS upi_id VARCHAR(100) NULL COMMENT 'Mechanic UPI ID for payments',
ADD COLUMN IF NOT EXISTS upi_qr_code VARCHAR(255) NULL COMMENT 'QR code image URL/path for UPI payments';

-- Add index for faster lookups
ALTER TABLE mechanics 
ADD INDEX IF NOT EXISTS idx_upi (upi_id);


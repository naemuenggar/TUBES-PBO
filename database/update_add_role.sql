-- Migration Script: Add Role Column to User Table
-- Date: 2025-12-20
-- Purpose: Add role-based access control (admin vs user)

USE MoneyMate;

-- Add role column to user table
ALTER TABLE user ADD COLUMN role ENUM('admin', 'user') NOT NULL DEFAULT 'user';

-- Create default admin user for testing
-- Password is plain text for demo (in production, use hashed passwords)
INSERT INTO user (id, nama, email, password, role) 
VALUES ('admin001', 'Administrator', 'admin@moneymate.com', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE role='admin', password='admin123';

-- Create demo regular user for testing
INSERT INTO user (id, nama, email, password, role) 
VALUES ('user001', 'Demo User', 'user@moneymate.com', 'user123', 'user')
ON DUPLICATE KEY UPDATE role='user', password='user123';

-- Verify the changes
SELECT id, nama, email, role FROM user;

COMMIT;

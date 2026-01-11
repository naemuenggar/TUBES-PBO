-- =========================================
-- Migration: Add role column & seed users (Safe/Idempotent)
-- DB      : MoneyMate
-- Purpose : Role-based access (admin/user)
-- =========================================

USE moneymate;

-- 1. Safe Column Addition Procedure
-- This procedure checks if 'role' exists. If not, adds it. If yes, modifies it to be sure.
DROP PROCEDURE IF EXISTS EnsureRoleColumn;

DELIMITER //

CREATE PROCEDURE EnsureRoleColumn()
BEGIN
    -- Check if column exists in the current database for table 'user'
    IF NOT EXISTS (
        SELECT * FROM information_schema.COLUMNS 
        WHERE TABLE_SCHEMA = DATABASE() 
        AND TABLE_NAME = 'user' 
        AND COLUMN_NAME = 'role'
    ) THEN
        -- Scenario A: Column does NOT exist. Add it.
        ALTER TABLE `user`
        ADD COLUMN `role` ENUM('admin', 'user') NOT NULL DEFAULT 'user' AFTER `password`;
        SELECT 'Column role added successfully' AS result;
    ELSE
        -- Scenario B: Column ALREADY exists. Ensure definition is correct.
        ALTER TABLE `user`
        MODIFY COLUMN `role` ENUM('admin', 'user') NOT NULL DEFAULT 'user';
        SELECT 'Column role already exists. Definition verified.' AS result;
    END IF;
END//

DELIMITER ;

-- 2. Execute the procedure
CALL EnsureRoleColumn();

-- 3. Cleanup procedure
DROP PROCEDURE IF EXISTS EnsureRoleColumn;

-- =========================================
-- 4. Seed/Repair Users
-- =========================================

-- Ensure Admin
INSERT INTO `user` (id, nama, email, password, role)
VALUES ('admin001', 'Administrator', 'admin@moneymate.com', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE
    role='admin',
    password='admin123';

-- Ensure User
INSERT INTO `user` (id, nama, email, password, role)
VALUES ('user001', 'Demo User', 'user@moneymate.com', 'user123', 'user')
ON DUPLICATE KEY UPDATE
    role='user',
    password='user123';

-- =========================================
-- 5. Verification
-- =========================================
SELECT id, nama, email, role FROM `user`;

COMMIT;

-- =========================================
-- Migration: Ensure role column & seed users
-- DB      : MoneyMate
-- Purpose : Role-based access (admin/user)
-- =========================================

USE MoneyMate;

-- Pastikan kolom role ada & definisinya sesuai
-- (gunakan MODIFY karena kolom sudah ada)
ALTER TABLE `user`
MODIFY COLUMN `role` ENUM('admin','user') NOT NULL DEFAULT 'user';

-- =========================================
-- Seed Admin User
-- =========================================
INSERT INTO `user` (id, nama, email, password, role)
VALUES ('admin001', 'Administrator', 'admin@moneymate.com', 'admin123', 'admin')
ON DUPLICATE KEY UPDATE
    nama='Administrator',
    email='admin@moneymate.com',
    password='admin123',
    role='admin';

-- =========================================
-- Seed Regular User
-- =========================================
INSERT INTO `user` (id, nama, email, password, role)
VALUES ('user001', 'Demo User', 'user@moneymate.com', 'user123', 'user')
ON DUPLICATE KEY UPDATE
    nama='Demo User',
    email='user@moneymate.com',
    password='user123',
    role='user';

-- =========================================
-- Verifikasi
-- =========================================
SELECT id, nama, email, role FROM `user`;

COMMIT;

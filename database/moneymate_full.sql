-- =============================================
-- MoneyMate Full Database Export
-- Generated on: 2025-01-02
-- Description: Complete schema and seed data for MoneyMate application.
-- Usage: Import this file in PhpMyAdmin or via MySQL CLI.
-- =============================================

CREATE DATABASE IF NOT EXISTS MoneyMate
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE MoneyMate;

-- =============================================
-- 1. Table Definitions
-- =============================================

-- Table: user
CREATE TABLE IF NOT EXISTS user (
  id        VARCHAR(50)  NOT NULL,
  nama      VARCHAR(100) NOT NULL,
  email     VARCHAR(150) NOT NULL,
  password  VARCHAR(255) NOT NULL,
  role      ENUM('admin', 'user') NOT NULL DEFAULT 'user',
  PRIMARY KEY (id),
  UNIQUE KEY uq_user_email (email)
) ENGINE=InnoDB;

-- Table: kategori
CREATE TABLE IF NOT EXISTS kategori (
  id    VARCHAR(50)  NOT NULL,
  nama  VARCHAR(100) NOT NULL,
  tipe  ENUM('pemasukan','pengeluaran') NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Table: transaksi
CREATE TABLE IF NOT EXISTS transaksi (
  id          VARCHAR(50)   NOT NULL,
  user_id     VARCHAR(50)   NOT NULL,
  jumlah      DECIMAL(20,2) NOT NULL,
  deskripsi   VARCHAR(255)  NULL,
  tanggal     DATE          NOT NULL,
  kategori_id VARCHAR(50)   NOT NULL,
  jenis       ENUM('pemasukan','pengeluaran') NOT NULL,
  PRIMARY KEY (id),
  KEY idx_transaksi_user (user_id),
  KEY idx_transaksi_kategori (kategori_id),
  KEY idx_transaksi_tanggal (tanggal),
  CONSTRAINT fk_transaksi_user
    FOREIGN KEY (user_id) REFERENCES user(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_transaksi_kategori
    FOREIGN KEY (kategori_id) REFERENCES kategori(id)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB;

-- Table: anggaran
CREATE TABLE IF NOT EXISTS anggaran (
  id      VARCHAR(50)   NOT NULL,
  user_id VARCHAR(50)   NOT NULL,
  nama    VARCHAR(150)  NOT NULL,
  jumlah  DECIMAL(20,2) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_anggaran_user (user_id),
  CONSTRAINT fk_anggaran_user
    FOREIGN KEY (user_id) REFERENCES user(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table: target_tabungan
CREATE TABLE IF NOT EXISTS target_tabungan (
  id            VARCHAR(50)   NOT NULL,
  user_id       VARCHAR(50)   NOT NULL,
  nama          VARCHAR(150)  NOT NULL,
  jumlah_target DECIMAL(20,2) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_target_user (user_id),
  CONSTRAINT fk_target_user
    FOREIGN KEY (user_id) REFERENCES user(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table: fingoal (progress untuk target tabungan)
CREATE TABLE IF NOT EXISTS fingoal (
  id        VARCHAR(50)   NOT NULL,
  target_id VARCHAR(50)   NOT NULL,
  progress  DECIMAL(20,2) NOT NULL DEFAULT 0,
  status    VARCHAR(50)   NOT NULL,
  PRIMARY KEY (id),
  KEY idx_fingoal_target (target_id),
  CONSTRAINT fk_fingoal_target
    FOREIGN KEY (target_id) REFERENCES target_tabungan(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table: pengingat
CREATE TABLE IF NOT EXISTS pengingat (
  id      VARCHAR(50)  NOT NULL,
  user_id VARCHAR(50)  NOT NULL,
  pesan   VARCHAR(255) NOT NULL,
  tanggal DATE         NOT NULL,
  PRIMARY KEY (id),
  KEY idx_pengingat_user (user_id),
  KEY idx_pengingat_tanggal (tanggal),
  CONSTRAINT fk_pengingat_user
    FOREIGN KEY (user_id) REFERENCES user(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- Table: tagihan
CREATE TABLE IF NOT EXISTS tagihan (
  id                 VARCHAR(50)   NOT NULL,
  user_id            VARCHAR(50)   NOT NULL,
  nama               VARCHAR(150)  NOT NULL,
  jumlah             DECIMAL(20,2) NOT NULL,
  tanggal_jatuh_tempo DATE         NOT NULL,
  PRIMARY KEY (id),
  KEY idx_tagihan_user (user_id),
  KEY idx_tagihan_jt (tanggal_jatuh_tempo),
  CONSTRAINT fk_tagihan_user
    FOREIGN KEY (user_id) REFERENCES user(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- =============================================
-- 2. Data Seeding
-- =============================================

-- Seed Users (Admin & Demo User)
INSERT INTO user (id, nama, email, password, role) VALUES 
('admin001', 'Administrator', 'admin@moneymate.com', 'admin123', 'admin'),
('user001', 'Demo User', 'user@moneymate.com', 'user123', 'user')
ON DUPLICATE KEY UPDATE
    nama=VALUES(nama),
    email=VALUES(email),
    password=VALUES(password),
    role=VALUES(role);

-- Seed Categories (Pemasukan)
INSERT IGNORE INTO kategori (id, nama, tipe) VALUES 
('k1', 'Gaji', 'pemasukan'),
('k2', 'Hadiah', 'pemasukan'),
('k3', 'Investasi', 'pemasukan'),
('k4', 'Penjualan', 'pemasukan'),
('k5', 'Lainnya', 'pemasukan');

-- Seed Categories (Pengeluaran)
INSERT IGNORE INTO kategori (id, nama, tipe) VALUES 
('k6', 'Makanan & Minuman', 'pengeluaran'),
('k7', 'Transportasi', 'pengeluaran'),
('k8', 'Belanja', 'pengeluaran'),
('k9', 'Tagihan & Utilitas', 'pengeluaran'),
('k10', 'Hiburan', 'pengeluaran'),
('k11', 'Kesehatan', 'pengeluaran'),
('k12', 'Pendidikan', 'pengeluaran'),
('k13', 'Asuransi', 'pengeluaran');

COMMIT;

-- End of File

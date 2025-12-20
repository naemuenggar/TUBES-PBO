-- MoneyMate database schema (MySQL)
-- Generated from the servlet queries in this repo.

CREATE DATABASE IF NOT EXISTS MoneyMate
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_unicode_ci;

USE MoneyMate;

-- Users
CREATE TABLE IF NOT EXISTS user (
  id        VARCHAR(50)  NOT NULL,
  nama      VARCHAR(100) NOT NULL,
  email     VARCHAR(150) NOT NULL,
  password  VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_user_email (email)
) ENGINE=InnoDB;

-- Kategori transaksi (pemasukan / pengeluaran)
CREATE TABLE IF NOT EXISTS kategori (
  id    VARCHAR(50)  NOT NULL,
  nama  VARCHAR(100) NOT NULL,
  tipe  ENUM('pemasukan','pengeluaran') NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

-- Transaksi (pemasukan/pengeluaran)
CREATE TABLE IF NOT EXISTS transaksi (
  id          VARCHAR(50)   NOT NULL,
  user_id     VARCHAR(50)   NOT NULL,
  jumlah      [
  {
    "TargetContent": "DECIMAL(18,2)",
    "ReplacementContent": "DECIMAL(20,2)",
    "StartLine": 1,
    "EndLine": 116,
    "AllowMultiple": true
  }
] NOT NULL,
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

-- Anggaran
CREATE TABLE IF NOT EXISTS anggaran (
  id      VARCHAR(50)   NOT NULL,
  user_id VARCHAR(50)   NOT NULL,
  nama    VARCHAR(150)  NOT NULL,
  jumlah  [
  {
    "TargetContent": "DECIMAL(18,2)",
    "ReplacementContent": "DECIMAL(20,2)",
    "StartLine": 1,
    "EndLine": 116,
    "AllowMultiple": true
  }
] NOT NULL,
  PRIMARY KEY (id),
  KEY idx_anggaran_user (user_id),
  CONSTRAINT fk_anggaran_user
    FOREIGN KEY (user_id) REFERENCES user(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- Target tabungan
CREATE TABLE IF NOT EXISTS target_tabungan (
  id            VARCHAR(50)   NOT NULL,
  user_id       VARCHAR(50)   NOT NULL,
  nama          VARCHAR(150)  NOT NULL,
  jumlah_target [
  {
    "TargetContent": "DECIMAL(18,2)",
    "ReplacementContent": "DECIMAL(20,2)",
    "StartLine": 1,
    "EndLine": 116,
    "AllowMultiple": true
  }
] NOT NULL,
  PRIMARY KEY (id),
  KEY idx_target_user (user_id),
  CONSTRAINT fk_target_user
    FOREIGN KEY (user_id) REFERENCES user(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- FinGoal (progress untuk target tabungan)
CREATE TABLE IF NOT EXISTS fingoal (
  id        VARCHAR(50)   NOT NULL,
  target_id VARCHAR(50)   NOT NULL,
  progress  [
  {
    "TargetContent": "DECIMAL(18,2)",
    "ReplacementContent": "DECIMAL(20,2)",
    "StartLine": 1,
    "EndLine": 116,
    "AllowMultiple": true
  }
] NOT NULL DEFAULT 0,
  status    VARCHAR(50)   NOT NULL,
  PRIMARY KEY (id),
  KEY idx_fingoal_target (target_id),
  CONSTRAINT fk_fingoal_target
    FOREIGN KEY (target_id) REFERENCES target_tabungan(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

-- Pengingat
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

-- Tagihan
CREATE TABLE IF NOT EXISTS tagihan (
  id                 VARCHAR(50)   NOT NULL,
  user_id            VARCHAR(50)   NOT NULL,
  nama               VARCHAR(150)  NOT NULL,
  jumlah             [
  {
    "TargetContent": "DECIMAL(18,2)",
    "ReplacementContent": "DECIMAL(20,2)",
    "StartLine": 1,
    "EndLine": 116,
    "AllowMultiple": true
  }
] NOT NULL,
  tanggal_jatuh_tempo DATE         NOT NULL,
  PRIMARY KEY (id),
  KEY idx_tagihan_user (user_id),
  KEY idx_tagihan_jt (tanggal_jatuh_tempo),
  CONSTRAINT fk_tagihan_user
    FOREIGN KEY (user_id) REFERENCES user(id)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB;

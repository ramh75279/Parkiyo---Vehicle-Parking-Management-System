-- Parkiyo MySQL 8 reference schema.
-- Normal application startup uses Hibernate update mode and does not run this file.
-- Run manually for a fresh database when you want explicit SQL control.

CREATE DATABASE IF NOT EXISTS parkiyo
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE parkiyo;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    password_reset_token VARCHAR(255),
    password_reset_token_expiry DATETIME(6),
    email_notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    sms_notifications_enabled BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_users_role (role),
    INDEX idx_users_status (status)
);

CREATE TABLE IF NOT EXISTS vehicles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    license_plate VARCHAR(255) NOT NULL UNIQUE,
    category VARCHAR(30) NOT NULL,
    make VARCHAR(255),
    model VARCHAR(255),
    color VARCHAR(255),
    manufacture_year INT,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_vehicle_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS parking_slots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    slot_number VARCHAR(255) NOT NULL UNIQUE,
    zone VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    hourly_rate DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    INDEX idx_slots_status (status),
    INDEX idx_slots_zone (zone)
);

CREATE TABLE IF NOT EXISTS parking_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    user_id BIGINT,
    entry_time DATETIME(6) NOT NULL,
    exit_time DATETIME(6),
    duration_minutes INT,
    amount_charged DECIMAL(10,2),
    entry_operator VARCHAR(255),
    exit_operator VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_record_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_record_slot FOREIGN KEY (slot_id) REFERENCES parking_slots(id),
    CONSTRAINT fk_record_user FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_records_active (active),
    INDEX idx_records_entry_time (entry_time)
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_code VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    slot_id BIGINT NOT NULL,
    vehicle_id BIGINT NOT NULL,
    parking_record_id BIGINT,
    start_time DATETIME(6) NOT NULL,
    end_time DATETIME(6) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_reservation_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_reservation_slot FOREIGN KEY (slot_id) REFERENCES parking_slots(id),
    CONSTRAINT fk_reservation_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    CONSTRAINT fk_reservation_record FOREIGN KEY (parking_record_id) REFERENCES parking_records(id),
    INDEX idx_reservations_status (status),
    INDEX idx_reservations_window (start_time, end_time)
);

CREATE TABLE IF NOT EXISTS wallets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    transaction_code VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    parking_record_id BIGINT,
    reservation_id BIGINT,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    paid_at DATETIME(6),
    refunded_at DATETIME(6),
    refund_reason VARCHAR(255),
    created_at DATETIME(6),
    updated_at DATETIME(6),
    CONSTRAINT fk_payment_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_payment_record FOREIGN KEY (parking_record_id) REFERENCES parking_records(id),
    CONSTRAINT fk_payment_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    INDEX idx_payments_status (status),
    INDEX idx_payments_paid_at (paid_at)
);

CREATE TABLE IF NOT EXISTS wallet_transactions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id BIGINT NOT NULL,
    payment_id BIGINT,
    type VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    balance_after DECIMAL(10,2) NOT NULL,
    description VARCHAR(255),
    created_at DATETIME(6),
    CONSTRAINT fk_wtxn_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id),
    CONSTRAINT fk_wtxn_payment FOREIGN KEY (payment_id) REFERENCES payments(id),
    INDEX idx_wtxn_created_at (created_at)
);

CREATE TABLE IF NOT EXISTS receipts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    payment_id BIGINT NOT NULL UNIQUE,
    receipt_number VARCHAR(255),
    transaction_id VARCHAR(255),
    payment_date DATETIME(6),
    customer_name VARCHAR(255),
    customer_email VARCHAR(255),
    plate VARCHAR(255),
    vehicle_model VARCHAR(255),
    vehicle_license_plate VARCHAR(255),
    slot_code VARCHAR(255),
    slot_number VARCHAR(255),
    zone VARCHAR(255),
    session_type VARCHAR(255),
    date DATE,
    arrival DATETIME(6),
    departure DATETIME(6),
    entry_time DATETIME(6),
    exit_time DATETIME(6),
    duration VARCHAR(255),
    parking_duration INT,
    billing_breakdown VARCHAR(255),
    discount VARCHAR(255),
    subtotal DECIMAL(10,2),
    tax DECIMAL(10,2),
    total DECIMAL(10,2),
    amount_paid DECIMAL(10,2),
    payment_method VARCHAR(255),
    created_at DATETIME(6),
    CONSTRAINT fk_receipt_payment FOREIGN KEY (payment_id) REFERENCES payments(id)
);

CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    type VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    action_url VARCHAR(255),
    is_read BOOLEAN NOT NULL DEFAULT FALSE,
    read_at DATETIME(6),
    created_at DATETIME(6),
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id),
    INDEX idx_notifications_user_read (user_id, is_read)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    performed_by_id BIGINT,
    action VARCHAR(255) NOT NULL,
    entity_type VARCHAR(255),
    entity_id BIGINT,
    description TEXT NOT NULL,
    change_details TEXT,
    ip_address VARCHAR(255),
    user_agent VARCHAR(255),
    created_at DATETIME(6),
    CONSTRAINT fk_audit_user FOREIGN KEY (performed_by_id) REFERENCES users(id),
    INDEX idx_audit_created_at (created_at),
    INDEX idx_audit_entity_type (entity_type)
);

CREATE TABLE IF NOT EXISTS saved_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    report_type VARCHAR(32) NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    INDEX idx_saved_reports_type (report_type),
    INDEX idx_saved_reports_status (status),
    INDEX idx_saved_reports_updated (updated_at)
);

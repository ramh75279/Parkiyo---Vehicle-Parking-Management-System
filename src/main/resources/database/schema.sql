-- ─────────────────────────────────────────────────────────────
-- Parkiyo Database Schema
-- Matches current JPA entity classes exactly
-- Run this once on a fresh database, OR let Hibernate auto-create
-- via spring.jpa.hibernate.ddl-auto=update (recommended)
-- ─────────────────────────────────────────────────────────────

-- Drop in reverse dependency order (safe re-run)
DROP TABLE IF EXISTS audit_logs;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS wallet_transactions;
DROP TABLE IF EXISTS wallets;
DROP TABLE IF EXISTS receipts;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS parking_records;
DROP TABLE IF EXISTS parking_slots;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS users;

-- ─────────────────────────────────────────────────────────────
-- USERS
-- FIXED: removed wallet_balance (now in separate wallets table)
-- FIXED: removed gender, profile_image_path (not in entity)
-- ADDED: updated_at, password_reset_token, email/sms notification flags
-- ─────────────────────────────────────────────────────────────
CREATE TABLE users (
                       id                          BIGINT AUTO_INCREMENT PRIMARY KEY,
                       first_name                  VARCHAR(50)  NOT NULL,
                       last_name                   VARCHAR(50)  NOT NULL,
                       email                       VARCHAR(100) NOT NULL UNIQUE,
                       phone                       VARCHAR(20),
                       password                    VARCHAR(255) NOT NULL,
                       role                        VARCHAR(20)  NOT NULL DEFAULT 'USER',
                       status                      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
                       password_reset_token        VARCHAR(255),
                       password_reset_token_expiry DATETIME,
                       email_notifications_enabled BOOLEAN      NOT NULL DEFAULT TRUE,
                       sms_notifications_enabled   BOOLEAN      NOT NULL DEFAULT TRUE,
                       created_at                  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                       updated_at                  TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ─────────────────────────────────────────────────────────────
-- VEHICLES
-- FIXED: renamed plate_number → license_plate (matches entity)
-- FIXED: removed owner_name, owner_phone, driver_* (not in entity)
-- ADDED: make, model, color, year, active, updated_at
-- ─────────────────────────────────────────────────────────────
CREATE TABLE vehicles (
                          id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id       BIGINT,
                          license_plate VARCHAR(20)  NOT NULL UNIQUE,
                          category      VARCHAR(30)  NOT NULL,
                          make          VARCHAR(50),
                          model         VARCHAR(50),
                          color         VARCHAR(30),
                          year          INT,
                          active        BOOLEAN      NOT NULL DEFAULT TRUE,
                          created_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                          updated_at    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          CONSTRAINT fk_vehicle_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ─────────────────────────────────────────────────────────────
-- PARKING SLOTS
-- FIXED: renamed slot_size/slot_type → zone, hourly_rate (matches entity)
-- ADDED: hourly_rate, updated_at
-- ─────────────────────────────────────────────────────────────
CREATE TABLE parking_slots (
                               id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                               slot_number VARCHAR(20)    NOT NULL UNIQUE,
                               zone        VARCHAR(30),
                               status      VARCHAR(20)    NOT NULL DEFAULT 'AVAILABLE',
                               hourly_rate DECIMAL(10,2)  NOT NULL DEFAULT 0.00,
                               created_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
                               updated_at  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- ─────────────────────────────────────────────────────────────
-- PARKING RECORDS
-- FIXED: renamed parking_sessions → parking_records (matches entity)
-- FIXED: renamed fee → amount_charged (matches entity)
-- FIXED: removed status column (entity uses boolean active flag)
-- ADDED: user_id, entry_operator, exit_operator, active, updated_at
-- ─────────────────────────────────────────────────────────────
CREATE TABLE parking_records (
                                 id               BIGINT AUTO_INCREMENT PRIMARY KEY,
                                 vehicle_id       BIGINT        NOT NULL,
                                 slot_id          BIGINT        NOT NULL,
                                 user_id          BIGINT,
                                 entry_time       DATETIME      NOT NULL,
                                 exit_time        DATETIME,
                                 duration_minutes INT,
                                 amount_charged   DECIMAL(10,2),
                                 entry_operator   VARCHAR(100),
                                 exit_operator    VARCHAR(100),
                                 active           BOOLEAN       NOT NULL DEFAULT TRUE,
                                 created_at       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                                 updated_at       TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                 CONSTRAINT fk_record_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
                                 CONSTRAINT fk_record_slot    FOREIGN KEY (slot_id)    REFERENCES parking_slots(id),
                                 CONSTRAINT fk_record_user    FOREIGN KEY (user_id)    REFERENCES users(id)
);

-- ─────────────────────────────────────────────────────────────
-- RESERVATIONS
-- FIXED: renamed reservation_start/end → start_time/end_time
-- ADDED: reservation_code, user_id, parking_record_id, updated_at
-- ─────────────────────────────────────────────────────────────
CREATE TABLE reservations (
                              id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                              reservation_code  VARCHAR(50)  NOT NULL UNIQUE,
                              user_id           BIGINT       NOT NULL,
                              slot_id           BIGINT       NOT NULL,
                              vehicle_id        BIGINT       NOT NULL,
                              start_time        DATETIME     NOT NULL,
                              end_time          DATETIME     NOT NULL,
                              status            VARCHAR(20)  NOT NULL DEFAULT 'PENDING',
                              parking_record_id BIGINT,
                              created_at        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                              updated_at        TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                              CONSTRAINT fk_reservation_user    FOREIGN KEY (user_id)           REFERENCES users(id),
                              CONSTRAINT fk_reservation_slot    FOREIGN KEY (slot_id)           REFERENCES parking_slots(id),
                              CONSTRAINT fk_reservation_vehicle FOREIGN KEY (vehicle_id)        REFERENCES vehicles(id),
                              CONSTRAINT fk_reservation_record  FOREIGN KEY (parking_record_id) REFERENCES parking_records(id)
);

-- ─────────────────────────────────────────────────────────────
-- WALLETS  (NEW — was wallet_balance column inside users before)
-- ─────────────────────────────────────────────────────────────
CREATE TABLE wallets (
                         id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id    BIGINT        NOT NULL UNIQUE,
                         balance    DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                         created_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         CONSTRAINT fk_wallet_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ─────────────────────────────────────────────────────────────
-- WALLET TRANSACTIONS  (NEW)
-- ─────────────────────────────────────────────────────────────
CREATE TABLE wallet_transactions (
                                     id            BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     wallet_id     BIGINT        NOT NULL,
                                     type          VARCHAR(10)   NOT NULL,   -- CREDIT or DEBIT
                                     amount        DECIMAL(10,2) NOT NULL,
                                     balance_after DECIMAL(10,2) NOT NULL,
                                     description   VARCHAR(255),
                                     payment_id    BIGINT,
                                     created_at    TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_wtxn_wallet  FOREIGN KEY (wallet_id)  REFERENCES wallets(id)
);

-- ─────────────────────────────────────────────────────────────
-- PAYMENTS
-- FIXED: removed session_id (now parking_record_id)
-- FIXED: renamed status default UNPAID → PENDING (matches enum)
-- ADDED: transaction_code, user_id, reservation_id, payment_method,
--        refunded_at, refund_reason, updated_at
-- ─────────────────────────────────────────────────────────────
CREATE TABLE payments (
                          id                BIGINT AUTO_INCREMENT PRIMARY KEY,
                          transaction_code  VARCHAR(50)   NOT NULL UNIQUE,
                          user_id           BIGINT        NOT NULL,
                          parking_record_id BIGINT,
                          reservation_id    BIGINT,
                          amount            DECIMAL(10,2) NOT NULL,
                          payment_method    VARCHAR(30)   NOT NULL,
                          status            VARCHAR(20)   NOT NULL DEFAULT 'PENDING',
                          paid_at           DATETIME,
                          refunded_at       DATETIME,
                          refund_reason     VARCHAR(255),
                          created_at        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP,
                          updated_at        TIMESTAMP     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          CONSTRAINT fk_payment_user   FOREIGN KEY (user_id)           REFERENCES users(id),
                          CONSTRAINT fk_payment_record FOREIGN KEY (parking_record_id) REFERENCES parking_records(id),
                          CONSTRAINT fk_payment_resv   FOREIGN KEY (reservation_id)    REFERENCES reservations(id)
);

-- Add FK from wallet_transactions → payments (after payments table exists)
ALTER TABLE wallet_transactions
    ADD CONSTRAINT fk_wtxn_payment FOREIGN KEY (payment_id) REFERENCES payments(id);

-- ─────────────────────────────────────────────────────────────
-- RECEIPTS  (NEW — was missing entirely)
-- ─────────────────────────────────────────────────────────────
CREATE TABLE receipts (
                          id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                          payment_id BIGINT NOT NULL UNIQUE,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_receipt_payment FOREIGN KEY (payment_id) REFERENCES payments(id)
);

-- ─────────────────────────────────────────────────────────────
-- NOTIFICATIONS
-- FIXED: constraint name had illegal hyphen fk_notification-user
-- ADDED: title, action_url, read_at, updated_at
-- ─────────────────────────────────────────────────────────────
CREATE TABLE notifications (
                               id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id    BIGINT       NOT NULL,
                               type       VARCHAR(50)  NOT NULL,
                               title      VARCHAR(100) NOT NULL,
                               message    TEXT         NOT NULL,
                               action_url VARCHAR(255),
                               is_read    BOOLEAN      NOT NULL DEFAULT FALSE,
                               read_at    DATETIME,
                               created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id)
);

-- ─────────────────────────────────────────────────────────────
-- AUDIT LOGS  (unchanged — was correct)
-- ─────────────────────────────────────────────────────────────
CREATE TABLE audit_logs (
                            id         BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id    BIGINT,
                            action     VARCHAR(100) NOT NULL,
                            module     VARCHAR(50)  NOT NULL,
                            details    VARCHAR(255),
                            ip_address VARCHAR(50),
                            created_at TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id)
);
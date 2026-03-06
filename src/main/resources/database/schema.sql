CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       phone VARCHAR(20) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'ATTENDANT',
                       status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                       wallet_balance DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                       gender VARCHAR(20),
                       profile_image_path VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE vehicles (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          user_id BIGINT NOT NULL,
                          plate_number VARCHAR(20) NOT NULL UNIQUE,
                          vehicle_type VARCHAR(30) NOT NULL,
                          vehicle_category VARCHAR(30),
                          vehicle_color VARCHAR(30),
                          owner_name VARCHAR(100) NOT NULL,
                          owner_phone VARCHAR(20) NOT NULL,
                          owner_email VARCHAR(100),
                          driver_name VARCHAR(100),
                          driver_phone VARCHAR(20),
                          driver_license_number VARCHAR(50),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT fk_vehicle_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE parking_slots (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               slot_number VARCHAR(20) NOT NULL UNIQUE,
                               slot_size VARCHAR(20) NOT NULL,
                               slot_type VARCHAR(20) NOT NULL,
                               status VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE parking_sessions (
                                  id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                  vehicle_id BIGINT NOT NULL,
                                  slot_id BIGINT NOT NULL,
                                  entry_time DATETIME NOT NULL,
                                  exit_time DATETIME,
                                  duration_minutes INT,
                                  fee DECIMAL(10,2),
                                  status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
                                  CONSTRAINT fk_session_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
                                  CONSTRAINT fk_session_slot FOREIGN KEY (slot_id) REFERENCES parking_slots(id)
);

CREATE TABLE reservations (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              vehicle_id BIGINT NOT NULL,
                              slot_id BIGINT NOT NULL,
                              reservation_start DATETIME NOT NULL,
                              reservation_end DATETIME NOT NULL,
                              status VARCHAR(20) NOT NULL DEFAULT 'RESERVED',
                              CONSTRAINT fk_reservation_vehicle FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
                              CONSTRAINT fk_reservation_slot FOREIGN KEY (slot_id) REFERENCES parking_slots(id)
);

CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          session_id BIGINT NOT NULL,
                          amount DECIMAL(10,2) NOT NULL,
                          payment_method VARCHAR(30) NOT NULL,
                          status VARCHAR(20) NOT NULL DEFAULT 'UNPAID',
                          paid_at DATETIME,
                          CONSTRAINT fk_payment_session FOREIGN KEY (session_id) REFERENCES parking_sessions(id)
);

CREATE TABLE notifications (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               user_id BIGINT NOT NULL,
                               message VARCHAR(255) NOT NULL,
                               type VARCHAR(50),
                               is_read BOOLEAN NOT NULL DEFAULT FALSE,
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE audit_logs (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT,
                            action VARCHAR(100) NOT NULL,
                            module VARCHAR(50) NOT NULL,
                            details VARCHAR(255),
                            ip_address VARCHAR(50),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id)
);
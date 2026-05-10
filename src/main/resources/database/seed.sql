-- Optional development seed data.
-- Password for both users is: Admin@12345

USE parkiyo;

INSERT INTO users (
    first_name, last_name, email, phone, password, role, status,
    email_notifications_enabled, sms_notifications_enabled, created_at, updated_at
) VALUES
('Parkiyo', 'Admin', 'admin@parkiyo.com', '+94770000000', '$2a$10$NuRcfVy4MtLqmcBwzzZIE.LTJfy8/ww0Crpd0CbBnGeJ9TMCgYvtK', 'ADMIN', 'ACTIVE', TRUE, TRUE, NOW(6), NOW(6)),
('Demo', 'User', 'user@parkiyo.com', '+94771111111', '$2a$10$NuRcfVy4MtLqmcBwzzZIE.LTJfy8/ww0Crpd0CbBnGeJ9TMCgYvtK', 'USER', 'ACTIVE', TRUE, TRUE, NOW(6), NOW(6))
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

INSERT INTO wallets (user_id, balance, created_at, updated_at)
SELECT id, 5000.00, NOW(6), NOW(6) FROM users WHERE email IN ('admin@parkiyo.com', 'user@parkiyo.com')
ON DUPLICATE KEY UPDATE balance = wallets.balance;

INSERT INTO parking_slots (slot_number, zone, status, hourly_rate, created_at, updated_at) VALUES
('A-001', 'Ground Floor', 'AVAILABLE', 250.00, NOW(6), NOW(6)),
('A-002', 'Ground Floor', 'AVAILABLE', 250.00, NOW(6), NOW(6)),
('A-003', 'Ground Floor', 'AVAILABLE', 250.00, NOW(6), NOW(6)),
('B-001', 'Covered', 'AVAILABLE', 350.00, NOW(6), NOW(6)),
('B-002', 'Covered', 'AVAILABLE', 350.00, NOW(6), NOW(6)),
('EV-001', 'EV Charging', 'AVAILABLE', 500.00, NOW(6), NOW(6))
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at);

INSERT INTO vehicles (
    user_id, license_plate, category, make, model, color, manufacture_year, active, created_at, updated_at
)
SELECT u.id, v.license_plate, v.category, v.make, v.model, v.color, v.manufacture_year, v.active, NOW(6), NOW(6)
FROM users u
JOIN (
    SELECT 'CAB-2401' AS license_plate, 'CAR' AS category, 'Toyota' AS make, 'Yaris' AS model, 'White' AS color, 2021 AS manufacture_year, TRUE AS active
    UNION ALL
    SELECT 'KLL-9981', 'MOTORCYCLE', 'Yamaha', 'FZ', 'Blue', 2022, TRUE
    UNION ALL
    SELECT 'NCP-7720', 'VAN', 'Nissan', 'Caravan', 'Silver', 2020, FALSE
) v
WHERE u.email = 'user@parkiyo.com'
ON DUPLICATE KEY UPDATE
    make = VALUES(make),
    model = VALUES(model),
    color = VALUES(color),
    manufacture_year = VALUES(manufacture_year),
    active = VALUES(active),
    updated_at = VALUES(updated_at);

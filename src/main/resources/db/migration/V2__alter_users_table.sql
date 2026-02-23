ALTER TABLE users
DROP COLUMN created_at,
DROP COLUMN updated_at;

ALTER TABLE users
ADD created_at TIMESTAMP NOT NULL,
ADD updated_at TIMESTAMP NULL;
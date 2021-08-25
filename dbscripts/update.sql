ALTER TABLE users ADD COLUMN password TEXT;

CREATE TABLE sessions (
    token       TEXT PRIMARY KEY,
    user_id     INTEGER NOT NULL REFERENCES users ON DELETE CASCADE,
    created     TIMESTAMPTZ DEFAULT NOW()
);

ALTER TABLE users RENAME COLUMN login to username;

ALTER TABLE users DROP COLUMN email;

UPDATE users SET password = '1000:a55d2a919684268d8d14138f177119ec50a707cd42436edc:7e7a159e2e57b31f2523e750cb9ac98d85e50d332dd1ac0a';

ALTER TABLE clients RENAME COLUMN list_name_display TO display_name;

ALTER TABLE clients RENAME COLUMN list_phone_display TO display_phone;
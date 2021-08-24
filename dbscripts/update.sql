UPDATE users SET email = CONCAT(LOWER(first_name), '@buchsbaumtax.com') WHERE first_name IS NOT NULL;

ALTER TABLE users ADD COLUMN password TEXT;

CREATE TABLE sessions (
    token       TEXT PRIMARY KEY,
    user_id     INTEGER NOT NULL REFERENCES users ON DELETE CASCADE,
    created     TIMESTAMPTZ DEFAULT NOW()
);
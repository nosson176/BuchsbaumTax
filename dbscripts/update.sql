ALTER TABLE users ADD COLUMN password TEXT;

CREATE TABLE sessions (
    token       TEXT PRIMARY KEY,
    user_id     INTEGER NOT NULL REFERENCES users ON DELETE CASCADE,
    created     TIMESTAMPTZ DEFAULT NOW()
);

ALTER TABLE users RENAME COLUMN login to username;

ALTER TABLE users DROP COLUMN email;
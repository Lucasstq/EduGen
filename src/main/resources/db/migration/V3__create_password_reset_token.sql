-- Create password_reset_token table for password recovery flow
CREATE TABLE IF NOT EXISTS password_reset_token (
    id          BIGSERIAL    PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    user_id     UUID         NOT NULL REFERENCES tb_user(id) ON DELETE CASCADE,
    expiry_date TIMESTAMP    NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_password_reset_token_user_id ON password_reset_token(user_id);
CREATE INDEX IF NOT EXISTS idx_password_reset_token_token   ON password_reset_token(token);


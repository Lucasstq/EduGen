-- Add email verification token columns to tb_user (added after initial schema)
ALTER TABLE tb_user
    ADD COLUMN IF NOT EXISTS email_verification_token VARCHAR(255),
    ADD COLUMN IF NOT EXISTS email_verification_token_expires_at TIMESTAMP;

-- Index to speed up lookups by token (nullable)
CREATE INDEX IF NOT EXISTS idx_user_email_verification_token ON tb_user(email_verification_token);


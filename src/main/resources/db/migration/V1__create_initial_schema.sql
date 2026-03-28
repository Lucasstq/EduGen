-- 1. tb_user
CREATE TABLE IF NOT EXISTS tb_user (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    username      VARCHAR(255) NOT NULL UNIQUE,
    email         VARCHAR(255) NOT NULL UNIQUE,
    hash_password VARCHAR(255) NOT NULL,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now()
);

-- 2. tb_worksheet
CREATE TABLE IF NOT EXISTS tb_worksheet (
    id              BIGSERIAL    PRIMARY KEY,
    owner_id        UUID         NOT NULL REFERENCES tb_user(id) ON DELETE CASCADE,
    teacher_name    VARCHAR(255),
    subject         VARCHAR(50)  NOT NULL,
    description     TEXT,
    grade           VARCHAR(50)  NOT NULL,
    topic           VARCHAR(255) NOT NULL,
    difficulty      VARCHAR(50)  NOT NULL,
    question_count  INT          NOT NULL,
    question_type   VARCHAR(50),
    created_at      TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at      TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_worksheet_owner_id ON tb_worksheet(owner_id);

-- 3. tb_worksheet_version
CREATE TABLE IF NOT EXISTS tb_worksheet_version (
    id                    BIGSERIAL    PRIMARY KEY,
    worksheet_id          BIGINT       NOT NULL REFERENCES tb_worksheet(id) ON DELETE CASCADE,
    version_type          VARCHAR(50)  NOT NULL,
    seed                  INT          NOT NULL DEFAULT 0,
    include_answers       BOOLEAN      NOT NULL DEFAULT FALSE,
    include_explanations  BOOLEAN      NOT NULL DEFAULT FALSE,
    status                VARCHAR(50)  NOT NULL,
    spec_json             JSONB,
    ai_description        TEXT,
    created_at            TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_worksheet_version_worksheet_id ON tb_worksheet_version(worksheet_id);

-- 4. tb_question
CREATE TABLE IF NOT EXISTS tb_question (
    id              BIGSERIAL    PRIMARY KEY,
    version_id      BIGINT       NOT NULL REFERENCES tb_worksheet_version(id) ON DELETE CASCADE,
    order_number    INT          NOT NULL DEFAULT 0,
    type            VARCHAR(50),
    statement       TEXT,
    correct_answer  TEXT,
    explanation     TEXT
);

CREATE INDEX IF NOT EXISTS idx_question_version_id ON tb_question(version_id);

-- 5. tb_question_option
CREATE TABLE IF NOT EXISTS tb_question_option (
    id           BIGSERIAL    PRIMARY KEY,
    question_id  BIGINT       NOT NULL REFERENCES tb_question(id) ON DELETE CASCADE,
    label        VARCHAR(10),
    text         VARCHAR(255)
);

CREATE INDEX IF NOT EXISTS idx_question_option_question_id ON tb_question_option(question_id);

-- 6. tb_worksheet_file
CREATE TABLE IF NOT EXISTS tb_worksheet_file (
    id           BIGSERIAL    PRIMARY KEY,
    version_id   BIGINT       NOT NULL REFERENCES tb_worksheet_version(id) ON DELETE CASCADE,
    audience     VARCHAR(50),
    storage_key  VARCHAR(255),
    sha256       VARCHAR(64),
    size_bytes   BIGINT       NOT NULL DEFAULT 0,
    created_at   TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_worksheet_file_version_id ON tb_worksheet_file(version_id);

-- 7. tb_refresh_token
CREATE TABLE IF NOT EXISTS tb_refresh_token (
    id          BIGSERIAL    PRIMARY KEY,
    token       VARCHAR(500) NOT NULL UNIQUE,
    user_id     UUID         NOT NULL REFERENCES tb_user(id) ON DELETE CASCADE,
    expiry_date TIMESTAMP WITH TIME ZONE NOT NULL,
    revoked     BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_refresh_token_user_id ON tb_refresh_token(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_token_token   ON tb_refresh_token(token);

-- 8. tb_teaching_example
CREATE TABLE IF NOT EXISTS tb_teaching_example (
    id           BIGSERIAL    PRIMARY KEY,
    subject      VARCHAR(50),
    grade        VARCHAR(50),
    topic        VARCHAR(255),
    content_text TEXT,
    source       VARCHAR(255),
    embedding    VARCHAR(255),
    created_at   TIMESTAMP    NOT NULL DEFAULT now()
);


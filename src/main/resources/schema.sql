-- 1. 테이블 삭제
DROP TABLE IF EXISTS tbl_payment_order CASCADE;
DROP TABLE IF EXISTS tbl_payment CASCADE;
DROP TABLE IF EXISTS tbl_order_menu CASCADE;
DROP TABLE IF EXISTS tbl_order CASCADE;
DROP TABLE IF EXISTS tbl_menu CASCADE;
DROP TABLE IF EXISTS tbl_category CASCADE;

-- 2. 테이블 생성
CREATE TABLE IF NOT EXISTS users
(
    -- column level constraints
    id         UUID PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL,
    password   VARCHAR(60)  NOT NULL,
    email      VARCHAR(100) NOT NULL,
    profile_id UUID,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS user_statues
(
    id             UUID PRIMARY KEY,
    user_id        UUID      NOT NULL,
    last_active_at TIMESTAMP NOT NULL,
    created_at     TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP NOT NULL,
    CONSTRAINT fk_user_status_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS channels
(
    id          UUID PRIMARY KEY,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) NOT NULL,
    create_at   TIMESTAMP   NOT NULL,
    update_at   TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS read_statues
(
    id           UUID PRIMARY KEY,
    user_id      UUID      NOT NULL,
    channel_id   UUID      NOT NULL,
    last_read_at TIMESTAMP NOT NULL,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP NOT NULL,
    CONSTRAINT fk_read_status_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_read_status_channel FOREIGN KEY (channel_id) REFERENCES channels (id)
);

CREATE TABLE IF NOT EXISTS binary_contents
(
    id           UUID PRIMARY KEY,
    file_name    VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes        BYTEA        NOT NULL,
    size         BIGINT       NOT NULL,
    created_at   TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS messages
(
    id         UUID PRIMARY KEY,
    content    TEXT      NOT NULL,
    channel_id UUID      NOT NULL,
    author_id  UUID      NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_message_channel FOREIGN KEY (channel_id) REFERENCES channels (id),
    CONSTRAINT fk_message_author FOREIGN KEY (author_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL,
    PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_attachment_message FOREIGN KEY (message_id) REFERENCES messages (id),
    CONSTRAINT fk_attachment_binary FOREIGN KEY (attachment_id) REFERENCES binary_contents (id)
);

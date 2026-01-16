DROP TABLE IF EXISTS message_attachments CASCADE;
DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS read_statuses CASCADE;
DROP TABLE IF EXISTS user_statuses CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS channels CASCADE;
DROP TABLE IF EXISTS binary_contents CASCADE;


CREATE TABLE IF NOT EXISTS binary_contents
(
    id           uuid,
    created_at   timestamptz  NOT NULL,
    file_name    varchar(255) NOT NULL,
    size         bigint       NOT NULL,
    content_type varchar(100) NOT NULL,
    bytes        bytea        NOT NULL,

    CONSTRAINT pk_binary_content_id PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS channels
(
    id          uuid,
    created_at  timestamptz NOT NULL,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) NOT NULL,

    CONSTRAINT pk_channels_id PRIMARY KEY (id),
    CONSTRAINT check_channel_type CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE IF NOT EXISTS users
(
    id         uuid,
    created_at timestamptz  NOT NULL,
    updated_at timestamptz,
    username   varchar(50)  NOT NULL,
    email      varchar(100) NOT NULL,
    password   varchar(60)  NOT NULL,
    profile_id uuid,

    CONSTRAINT pk_users_id PRIMARY KEY (id),
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT fk_users_profile_id FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL,
    CONSTRAINT uk_users_profile_id UNIQUE (profile_id)
);

CREATE TABLE IF NOT EXISTS user_statuses
(
    id             uuid,
    created_at     timestamptz NOT NULL,
    updated_at     timestamptz,
    user_id        uuid        NOT NULL,
    last_active_at timestamptz NOT NULL,

    CONSTRAINT pk_user_statuses_id PRIMARY KEY (id),
    CONSTRAINT fk_user_statuses_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uk_user_statuses_user_id UNIQUE (user_id)
);

CREATE TABLE IF NOT EXISTS read_statuses
(
    id           uuid,
    created_at   timestamptz NOT NULL,
    updated_at   timestamptz,
    user_id      uuid        NOT NULL,
    channel_id   uuid        NOT NULL,
    last_read_at timestamptz NOT NULL,

    CONSTRAINT pk_read_statuses_id PRIMARY KEY (id),
    CONSTRAINT fk_read_statuses_user_id FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT uk_read_statuses_user_id UNIQUE (user_id, channel_id),
    CONSTRAINT fk_read_statuses_channel_id FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS messages
(
    id         uuid,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    content    text,
    channel_id uuid        NOT NULL,
    author_id  uuid,

    CONSTRAINT pk_messages_id PRIMARY KEY (id),
    CONSTRAINT fk_messages_channel_id FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_author_id FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS message_attachments
(
    message_id    uuid NOT NULL,
    attachment_id uuid NOT NULL,

    CONSTRAINT pk_message_attachments PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_message_attachments_message_id FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    CONSTRAINT fk_message_attachments_attachment_id FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);

SELECT *
FROM users;

SELECT *
FROM binary_contents;
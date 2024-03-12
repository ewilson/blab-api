CREATE TABLE IF NOT EXISTS blabs (
    id          VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY,
    title       VARCHAR      NOT NULL,
    user_id     VARCHAR      NOT NULL,
    created_time   TIMESTAMP    NOT NULL
);

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(60)  DEFAULT RANDOM_UUID() PRIMARY KEY,
    username    VARCHAR      NOT NULL
);

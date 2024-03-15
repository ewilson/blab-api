CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(60)  DEFAULT uuid_generate_v4() PRIMARY KEY,
    username    VARCHAR(30)      NOT NULL
);

CREATE TABLE IF NOT EXISTS blabs (
    id              VARCHAR(60)     DEFAULT uuid_generate_v4() PRIMARY KEY,
    title           VARCHAR(60)     NOT NULL,
    user_id         VARCHAR(60)     references users(id) NOT NULL,
    created_time    TIMESTAMP       NOT NULL
);


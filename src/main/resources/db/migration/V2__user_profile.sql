CREATE TABLE IF NOT EXISTS users (
    id          VARCHAR(60)  DEFAULT uuid_generate_v4() PRIMARY KEY,
    username    VARCHAR(30)      NOT NULL
);

ALTER TABLE users
ADD name VARCHAR(50),
ADD bio VARCHAR(200);






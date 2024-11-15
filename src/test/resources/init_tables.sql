CREATE TABLE users
(
    id       SERIAL PRIMARY KEY,
    login    VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100)        NOT NULL
);


CREATE TABLE locations
(
    id        SERIAL PRIMARY KEY,
    name      VARCHAR NOT NULL,
    user_id   INTEGER NOT NULL,
    latitute   DECIMAL(40, 35),
    longitude DECIMAL(40, 35),
    FOREIGN KEY (user_id) REFERENCES users (id),
    UNIQUE (User_Id, latitute, longitude)
);


CREATE TABLE sessions
(
    id         UUID PRIMARY KEY,
    user_id    INTEGER   NOT NULL,
    expires_at TIMESTAMP NOT NULL
);
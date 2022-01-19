CREATE TABLE IF NOT EXISTS users
(
    id SERIAL PRIMARY KEY,
    name TEXT,
    email TEXT,
    password TEXT
);

CREATE TABLE IF NOT EXISTS item
(
    id SERIAL PRIMARY KEY,
    description TEXT,
    created timestamp,
    done boolean,
    user_id int references users(id)

);

CREATE TABLE IF NOT EXISTS category
(
    id SERIAL PRIMARY KEY,
    name TEXT
)


ALTER TABLE sessions
    ADD CONSTRAINT fk_user_id
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
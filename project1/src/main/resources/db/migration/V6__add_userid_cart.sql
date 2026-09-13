ALTER TABLE cart
    DROP FOREIGN KEY fk_cart_on_user_userid;

ALTER TABLE cart
    ADD user_id BIGINT NULL;

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_USERID FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE cart
    DROP COLUMN user_user_id;
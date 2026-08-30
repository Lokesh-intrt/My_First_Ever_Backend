CREATE TABLE users (
                       user_id BIGINT NOT NULL AUTO_INCREMENT,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL,
                       password VARCHAR(255) NOT NULL,
                       `role` VARCHAR(255) NOT NULL,
                       PRIMARY KEY (user_id),
                       UNIQUE KEY uc_users_email (email),
                       UNIQUE KEY uc_users_password (password)
);

CREATE TABLE products (
                          product_id BIGINT NOT NULL AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          price DOUBLE NOT NULL,
                          seller_id BIGINT NOT NULL,
                          stock INT NOT NULL,
                          status VARCHAR(255) NOT NULL,
                          PRIMARY KEY (product_id),
                          CONSTRAINT fk_products_on_seller FOREIGN KEY (seller_id) REFERENCES users (user_id)
);

CREATE TABLE cart (
                      cart_id BIGINT NOT NULL AUTO_INCREMENT,
                      total_amount DOUBLE NOT NULL,
                      user_user_id BIGINT NULL,
                      PRIMARY KEY (cart_id),
                      CONSTRAINT fk_cart_on_user_userid FOREIGN KEY (user_user_id) REFERENCES users (user_id)
);

CREATE TABLE cart_item (
                           id BIGINT NOT NULL AUTO_INCREMENT,
                           quantity INT NULL,
                           cart_id BIGINT NULL,
                           product_id BIGINT NULL,
                           PRIMARY KEY (id),
                           CONSTRAINT fk_cart_item_on_cart FOREIGN KEY (cart_id) REFERENCES cart (cart_id),
                           CONSTRAINT fk_cart_item_on_product FOREIGN KEY (product_id) REFERENCES products (product_id)
);

CREATE TABLE orders (
                        order_id BIGINT NOT NULL AUTO_INCREMENT,
                        total_amount DOUBLE NOT NULL,
                        user_id BIGINT NULL,
                        order_status VARCHAR(255) NOT NULL,
                        PRIMARY KEY (order_id),
                        CONSTRAINT fk_orders_on_user FOREIGN KEY (user_id) REFERENCES users (user_id)
);

CREATE TABLE order_items (
                             item_id BIGINT NOT NULL AUTO_INCREMENT,
                             item_name VARCHAR(255) NOT NULL,
                             price DOUBLE NOT NULL,
                             product_id BIGINT NULL,
                             order_id BIGINT NULL,
                             PRIMARY KEY (item_id),
                             CONSTRAINT fk_order_items_on_product FOREIGN KEY (product_id) REFERENCES products (product_id),
                             CONSTRAINT fk_order_items_on_order FOREIGN KEY (order_id) REFERENCES orders (order_id)
);

CREATE TABLE revinfo (
                         rev BIGINT NOT NULL,
                         revtstmp BIGINT NULL,
                         PRIMARY KEY (rev)
);

CREATE TABLE revchanges (
                            rev BIGINT NOT NULL,
                            entityname VARCHAR(255) NULL,
                            CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev)
);
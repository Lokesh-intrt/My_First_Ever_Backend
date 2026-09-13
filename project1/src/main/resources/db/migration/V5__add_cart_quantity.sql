ALTER TABLE cart
    ADD total_quantity INT NULL;

ALTER TABLE cart
    MODIFY total_quantity INT NOT NULL;
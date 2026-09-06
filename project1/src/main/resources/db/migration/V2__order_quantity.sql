ALTER TABLE revchanges
    DROP FOREIGN KEY fk_revchanges_on_default_tracking_modified_entities_changelog;

ALTER TABLE orders
    ADD quantity INT NULL;

ALTER TABLE orders
    MODIFY quantity INT NOT NULL;

DROP TABLE revchanges;

DROP TABLE revinfo;
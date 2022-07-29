INSERT INTO category (id, description) VALUES (1001, 'Comic Books');
INSERT INTO category (id, description) VALUES (1002, 'Movies');
INSERT INTO category (id, description) VALUES (1003, 'Books');

INSERT INTO supplier (id, name) VALUES (1001, 'Panini Comics');
INSERT INTO supplier (id, name) VALUES (1002, 'Amazon');

INSERT INTO product (id, name, fk_supplier, fk_category, quantity_available, CREATED_AT) VALUES (1001, 'A morte do Superman', 1001, 1001, 10, CURRENT_TIMESTAMP);
INSERT INTO product (id, name, fk_supplier, fk_category, quantity_available, CREATED_AT) VALUES (1002, 'O Sexto Sentido', 1002, 1002, 20, CURRENT_TIMESTAMP);
INSERT INTO product (id, name, fk_supplier, fk_category, quantity_available, CREATED_AT) VALUES (1003, 'Harry Potter', 1002, 1003, 30, CURRENT_TIMESTAMP);

INSERT INTO categories (id, name) VALUES (1, 'Fiction'), (2, 'Non-Fiction');

INSERT INTO books (id, title, author, isbn, price)
VALUES (1, 'Test Book 1','Author', '1234567890123', 19.99);

INSERT INTO books (id, title, author, isbn, price)
VALUES (2, 'Test Book 2', 'Author', '1234567890124', 29.99);

INSERT INTO books (id, title, author, isbn, price)
VALUES (3, 'Test Book 3', 'Author', '12345678310124', 25.99);

INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 2),
    (1, 2);


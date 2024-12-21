INSERT INTO categories (name) VALUES ('Fiction'), ('Non-Fiction');

INSERT INTO books ( title, author, isbn, price)
VALUES ('Test Book 1','Author', '1234567890123', 19.99);

INSERT INTO books (title, author, isbn, price)
VALUES ('Test Book 2', 'Author', '1234567890124', 29.99);

INSERT INTO books (title, author, isbn, price)
VALUES ('Test Book 3', 'Author', '12345678310124', 25.99);

INSERT INTO books_categories (book_id, category_id)
VALUES
    (1, 1),
    (2, 1),
    (3, 2),
    (1, 2);


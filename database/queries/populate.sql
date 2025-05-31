INSERT IGNORE INTO publisher (name) VALUES
  ('Bloomsbury'),
  ('Allen & Unwin'),
  ('T. Egerton'),
  ('J.B. Lippincott & Co.'),
  ('Scribner'),
  ('Secker & Warburg'),
  ('Harcourt, Brace & Company'),
  ('Richard Bentley'),
  ('The Russian Messenger'),
  ('Little, Brown and Company'),
  ('HarperCollins'),
  ('Scholastic'),
  ('Penguin Books'),
  ('HarperOne'),
  ('Francisco de Robles'),
  ('Doubleday');
SELECT * FROM publisher;

INSERT IGNORE INTO tag (name) VALUES
  ('Fantasia'),
  ('Aventura'),
  ('Clássico'),
  ('Romance'),
  ('Drama'),
  ('Distopia'),
  ('Ficção Científica'),
  ('Alegoria'),
  ('Histórico'),
  ('Bildungsroman'),
  ('Mistério'),
  ('Filosófico');
SELECT * FROM tag;

INSERT IGNORE INTO author (id, name) VALUES
  (1, 'J.K. Rowling'),
  (2, 'J.R.R. Tolkien'),
  (3, 'Jane Austen'),
  (4, 'Harper Lee'),
  (5, 'F. Scott Fitzgerald'),
  (6, 'George Orwell'),
  (7, 'Herman Melville'),
  (8, 'Leo Tolstoy'),
  (9, 'J.D. Salinger'),
  (10, 'C.S. Lewis'),
  (11, 'Dan Brown'),
  (12, 'Paulo Coelho'),
  (13, 'Miguel de Cervantes');
SELECT * FROM author;

INSERT IGNORE INTO benefit (name) VALUES
  ('CAN_READ_BASIC_BOOKS'),
  ('CAN_READ_ALL_BOOKS'),
  ('CAN_EXPLORE_IN_GROUP'),
  ('CAN_DOWNLOAD_BOOKS');
SELECT * FROM benefit;

# DELETE FROM book WHERE id >= 1 AND id <= 15;
INSERT IGNORE INTO book (id, isbn, title, description, num_pages, published_in, publisher) VALUES
  (1, 9780747532743, 'Harry Potter e a Pedra Filosofal', 'Primeiro livro da série Harry Potter.', 223, '1997-06-26', 'Bloomsbury'),
  (2, 9780261103573, 'A Sociedade do Anel', 'Primeiro volume de O Senhor dos Anéis.', 423, '1954-07-29', 'Allen & Unwin'),
  (3, 9780140430721, 'Orgulho e Preconceito', 'Um romance clássico de Jane Austen.', 279, '1813-01-28', 'T. Egerton'),
  (4, 9780061120084, 'O Sol é para Todos', 'Romance sobre injustiça racial.', 281, '1960-07-11', 'J.B. Lippincott & Co.'),
  (5, 9780743273565, 'O Grande Gatsby', 'História da Era do Jazz.', 180, '1925-04-10', 'Scribner'),
  (6, 9780451524935, '1984', 'Romance distópico.', 328, '1949-06-08', 'Secker & Warburg'),
  (7, 9780451526342, 'A Revolução dos Bichos', 'Novela alegórica.', 112, '1945-08-17', 'Secker & Warburg'),
  (8, 9780142437247, 'Moby Dick', 'Obsessão de Ahab.', 635, '1851-10-18', 'Richard Bentley'),
  (9, 9780199232765, 'Guerra e Paz', 'Romance épico histórico.', 1225, '1869-01-01', 'The Russian Messenger'),
  (10, 9780316769488, 'O Apanhador no Campo de Centeio', 'Angústia adolescente.', 214, '1951-07-16', 'Little, Brown and Company'),
  (11, 9780547928227, 'O Hobbit', 'Aventura de fantasia.', 310, '1937-09-21', 'Allen & Unwin'),
  (12, 9780066238500, 'O Leão, a Feiticeira e o Guarda-Roupa', 'Início de As Crônicas de Nárnia.', 208, '1950-10-16', 'HarperCollins'),
  (13, 9780385504201, 'O Código Da Vinci', 'Thriller de mistério.', 454, '2003-03-18', 'Doubleday'),
  (14, 9780061122415, 'O Alquimista', 'Romance filosófico.', 208, '1988-05-01', 'HarperOne'),
  (15, 9780060934347, 'Dom Quixote', 'Romance espanhol de Cervantes.', 863, '1605-01-16', 'Francisco de Robles');
SELECT * FROM book;

INSERT IGNORE INTO book_benefit (benefit, book) VALUES 
  ('CAN_READ_ALL_BOOKS', 2),
  ('CAN_READ_ALL_BOOKS', 4),
  ('CAN_READ_ALL_BOOKS', 6);
SELECT * FROM book_benefit;

INSERT IGNORE INTO book_author (book, author) VALUES
  (1, 1),
  (2, 2),
  (3, 3),
  (4, 4),
  (5, 5),
  (6, 6),
  (7, 6),
  (8, 7),
  (9, 8),
  (10, 9),
  (11, 2),
  (12, 10),
  (13, 11),
  (14, 12),
  (15, 13);
SELECT * FROM book_author;


INSERT IGNORE INTO book_tag (book, tag) VALUES
  (1, 'Fantasia'),
  (1, 'Aventura'),
  (1, 'Bildungsroman'),
  (2, 'Fantasia'),
  (2, 'Aventura'),
  (3, 'Clássico'),
  (3, 'Romance'),
  (4, 'Clássico'),
  (4, 'Drama'),
  (5, 'Clássico'),
  (5, 'Drama'),
  (6, 'Distopia'),
  (7, 'Alegoria'),
  (8, 'Aventura'),
  (8, 'Clássico'),
  (9, 'Histórico'),
  (10, 'Clássico'),
  (11, 'Fantasia'),
  (11, 'Aventura'),
  (12, 'Fantasia'),
  (13, 'Mistério'),
  (13, 'Aventura'),
  (14, 'Filosófico'),
  (14, 'Aventura'),
  (15, 'Clássico'),
  (15, 'Aventura');
SELECT * FROM book_tag;

INSERT IGNORE INTO plan (id, name, description, duration) VALUES 
  (1, 'Básico', 'Desfrute dos benefícios básicos da plataforma.', 720),
  (2, 'Super', 'Tenha acesso a todos os livros e mais!', 720),
  (3, 'Mega', 'Tenha acesso a todos os benefícios da plataforma!', 8766);
SELECT * FROM plan;

INSERT IGNORE INTO plan_benefit (plan, benefit) VALUES 
  (2, 'CAN_READ_ALL_BOOKS'),
  (2, 'CAN_EXPLORE_IN_GROUP'),
  (3, 'CAN_READ_ALL_BOOKS'),
  (3, 'CAN_EXPLORE_IN_GROUP'),
  (3, 'CAN_DOWNLOAD_BOOKS');
SELECT * FROM plan_benefit;

INSERT IGNORE INTO plan_edition (id, started_in, price, plan) VALUES 
  (1, NOW(), 12.00, 1),
  (2, NOW(), 30.00, 2),
  (3, NOW(), 330.00, 3);
SELECT * FROM plan_edition;

# DELETE FROM user WHERE email = 'marcel@gmail.com';
INSERT IGNORE INTO user (uuid, name, email, password) VALUES
  ('a1b2c3d4-e5f6-7890-1234-56789abcdef0', 'Lucas Marcel', 'marcel@gmail.com', '$2a$10$Jop9tiqQd5PCuzOpF8MiVOTi/fuJ98PxBRrycvdi5Gt8/t2wOpqHm'),
  ('09876543-2109-fedc-ba98-7654321fedcb', 'Marcela Silva', 'marcela@gmail.com', '$2a$10$q1OKZbV/C9oJIXiep/ketei0dVYuQaEl2FvdQ9Dxf.YAh1oId1MO.'),
  ('e8d7c6b5-a4b3-c2d1-0f1e-2d3c4b5a6f7e', 'Eric Eduardo', 'eric@gmail.com', '$2a$10$IKVfl9fl/o6auLT00ArvMOPN9pObm7y0DI1Xwu6gbtlcVbHzSwTnC');
SELECT * FROM user;
# Senha 'marcel' criptografada: $2a$10$Jop9tiqQd5PCuzOpF8MiVOTi/fuJ98PxBRrycvdi5Gt8/t2wOpqHm
# Senha 'marcela' criptografada: $2a$10$q1OKZbV/C9oJIXiep/ketei0dVYuQaEl2FvdQ9Dxf.YAh1oId1MO.
# Senha 'eric' criptografada: $2a$10$IKVfl9fl/o6auLT00ArvMOPN9pObm7y0DI1Xwu6gbtlcVbHzSwTnC

INSERT INTO classification (book, subscriber, value) VALUES
  (1, 'e8d7c6b5-a4b3-c2d1-0f1e-2d3c4b5a6f7e', 10),
  (2, 'e8d7c6b5-a4b3-c2d1-0f1e-2d3c4b5a6f7e', 8),
  (1, '09876543-2109-fedc-ba98-7654321fedcb', 8),
  (2, 'a1b2c3d4-e5f6-7890-1234-56789abcdef0', 6);
SELECT * FROM classification;

INSERT INTO reading (id, book, subscriber, current_page, finished, started_in, stopped_in) VALUES
  (1, 1, 'e8d7c6b5-a4b3-c2d1-0f1e-2d3c4b5a6f7e', 10, false, NOW(), NOW()),
  (2, 2, '09876543-2109-fedc-ba98-7654321fedcb', 20, false, NOW(), NOW()),
  (3, 1, '09876543-2109-fedc-ba98-7654321fedcb', 223, true, NOW(), NOW());
SELECT * FROM reading;

CALL subscribe(1, 'a1b2c3d4-e5f6-7890-1234-56789abcdef0');
CALL subscribe(2, '09876543-2109-fedc-ba98-7654321fedcb');
CALL subscribe(3, 'e8d7c6b5-a4b3-c2d1-0f1e-2d3c4b5a6f7e');

SELECT * FROM subscription;
SELECT * FROM subscriber;
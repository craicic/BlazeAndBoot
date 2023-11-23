INSERT INTO post (id, body, title)
VALUES (1, 'A very long post about testing Blaze Persistence !', 'Blaze Persistence in Boot app'),
       (2, 'A talk on JPA !', 'JPA Tips'),
       (3, 'An article on Hibernate', 'Learn Hibernate in 17 seconds -_-'),
       (4, 'A promotional post about PH', 'Join our Working Group !'),
       (5, 'A book on Jooq in great details', 'Jooq in depth');
SELECT setval('post_seq', 5, true);



INSERT INTO image (id, post_id)
SELECT generate_series(1, 10), 1;
INSERT INTO image (id, post_id)
SELECT generate_series(11, 14), 2;
INSERT INTO image (id, post_id)
SELECT generate_series(15, 17), 3;
INSERT INTO image (id, post_id)
SELECT generate_series(18, 20), 4;
INSERT INTO image (id, post_id)
SELECT generate_series(21, 25), 5;
SELECT setval('image_seq', 25, true);

INSERT INTO image_blob (image_id, content)
SELECT generate_series(1, 25), md5(random()::text)::bytea;

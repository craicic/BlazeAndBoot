
INSERT INTO post (id, body, title)
VALUES (1, 'A very long post about testing Blaze Persistence !', 'Blaze Persistence in Boot app');
SELECT setval('post_seq', 1, true);


INSERT INTO image (id, post_id)
SELECT generate_series(1, 10), 1;
SELECT setval('image_seq', 10, true);

INSERT INTO image_blob (image_id, content)
SELECT generate_series(1,10), md5(random()::text)::bytea;

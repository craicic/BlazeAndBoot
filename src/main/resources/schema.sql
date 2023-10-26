drop schema if exists public cascade;
create schema public;

create sequence image_seq start 1 increment by 50;

create sequence post_seq start 1 increment by 50;

create table post
(
    id    integer not null,
    body  varchar(255),
    title varchar(255),
    primary key (id)
);

create table image
(
    id      integer not null,
    post_id integer,
    primary key (id),
    constraint fke2l07hc93u2bbjnl80meu3rn4
        foreign key (post_id) references post
);

create table image_blob
(
    image_id integer not null,
    content  bytea   not null,
    primary key (image_id),
    constraint fkwh9ounh5rvc9m2wn4ssawvyc
        foreign key (image_id) references image
);


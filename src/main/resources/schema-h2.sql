CREATE TABLE IF NOT EXISTS regions
(
    key        integer      not null auto_increment,
    id         varchar(3)   not null,
    name       varchar(255) not null,
    short_name varchar(10)  not null,
    primary key (key),
    unique (id)
);
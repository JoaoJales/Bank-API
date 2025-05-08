create table users(
    id bigint not null auto_increment,
    login varchar(30) not null,
    password varchar(255) not null,

    primary key(id)
);
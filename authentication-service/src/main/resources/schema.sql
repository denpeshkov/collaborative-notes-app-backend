create table USER_CREDENTIALS
(
    id       long auto_increment,
    username varchar not null,
    password varchar not null,
    constraint USER_CREDENTIALS_PK
        primary key (id)
);
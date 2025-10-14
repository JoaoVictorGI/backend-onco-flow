create type role as enum ('ADMIN', 'USER');

create table if not exists usuario (
    id uuid primary key not null,
    nome_completo text not null,
    email text not null,
    senha text not null,
    role role not null
);
alter table paciente
add dt_criacao timestamp with time zone not null default now();

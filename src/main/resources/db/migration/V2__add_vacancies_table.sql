
create table if not exists vacancies(
    id bigserial primary key,
    company varchar(255) not null,
    position varchar(255) not null,
    description varchar(255) not null,
    user_id bigint not null references users(id),
    status varchar(50) not null
);

alter table applications
add column vacancy_id bigint references vacancies(id);
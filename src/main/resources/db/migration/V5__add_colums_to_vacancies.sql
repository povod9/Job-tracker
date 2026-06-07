alter table vacancies
add column created_at timestamptz not null default now(),
add column updated_at timestamptz not null default now(),
add column version bigint not null default 0
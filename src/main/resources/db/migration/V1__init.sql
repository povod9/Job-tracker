create table if not exists users(
    id bigserial primary key,
    name varchar(255) not null,
    email varchar(255) unique not null,
    password_hash varchar(255) not null,
    role varchar(30) not null default 'USER',
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    is_active boolean not null default true
);

create table if not exists applications(
    id bigserial primary key,
    user_id bigint not null references users(id),
    company varchar(255) not null,
    position varchar(255) not null,
    status varchar(255) not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    version bigint not null default 0
);

create table if not exists activity_events(
    id bigserial primary key,
    application_id bigint not null references applications(id) on delete cascade,
    type varchar(50) not null,
    created_at timestamptz not null default now(),
    created_by bigint not null references users(id)
);

create table if not exists reminders(
    id bigserial primary key,
    user_id bigint references users(id),
    application_id bigint references applications(id) on delete cascade ,
    due_at timestamptz not null,
    status varchar(30) not null,
    message text null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

create table if not exists telegram_settings(
    user_id bigint primary key references users(id) on delete cascade ,
    telegram_chat_id bigint not null,
    is_enabled boolean not null default true,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
)

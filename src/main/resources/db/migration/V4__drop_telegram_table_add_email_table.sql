drop table telegram_settings;

create table if not exists email_queue(
    id bigserial primary key,
    user_id bigint not null references users(id) on delete cascade,
    reminder_id bigint not null references reminders(id) on delete cascade ,
    status varchar(55) not null,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now(),
    attempts smallint not null default 0,
    error_message text
);
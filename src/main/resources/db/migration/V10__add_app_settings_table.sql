create table if not exists app_settings(
    app_key varchar(100) primary key,
    app_value varchar(255) not null
);

insert into app_settings (app_key, app_value) values ('adzuna_current_page', '1')
on conflict (app_key) do nothing;
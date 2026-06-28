insert into app_settings (app_key, app_value)
values ('adzuna_keywords', 'java spring junior')
on conflict (app_key) do nothing;
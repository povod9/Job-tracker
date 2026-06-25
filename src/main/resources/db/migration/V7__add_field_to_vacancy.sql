alter table vacancies
add column salary_min decimal,
add column salary_max decimal,
add column source varchar(55),
add column external_id varchar(55),
add column redirect_url varchar(255);
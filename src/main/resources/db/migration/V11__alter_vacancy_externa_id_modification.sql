alter table vacancies
add constraint unique_external_id unique (external_id);
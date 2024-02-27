insert into role(code, description)
values ('ADM', 'Admin');

insert into consumer (disabled, created_date, last_modified_date, created_by, id, last_modified_by, password, role_code,
                      username)
values (false, now(), null, '99512e52-ec72-4448-9aed-f0ae802aff02', '99512e52-ec72-4448-9aed-f0ae802aff02', null, '$2a$12$WiL0s04ORzOdjornlwmAsO9f.vKgiwInU9GUdmkPd9yJ.Ht1Vke6i', 'ADM',
        'adm');
delete from roles where name = 'PARKINGCLERK';

insert into roles (name) values('ROLE_MANAGER');

insert into roles (name) values('ROLE_PARKINGCLERK');

create table manager (
  id bigint not null identity,
  name varchar(255),
  email varchar(255),
  phone_number varchar(11)
);
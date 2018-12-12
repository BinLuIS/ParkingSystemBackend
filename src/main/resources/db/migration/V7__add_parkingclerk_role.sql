alter table users add phone_number varchar(11);

alter table users add idInRole bigint;

insert into roles (name) values("PARKINGCLERK");
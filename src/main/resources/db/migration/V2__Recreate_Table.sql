drop table parking_boy;
drop table parking_lot;
drop table parking_order;

create table parking_boy (
  id bigint not null identity,
  name varchar(255),
  email varchar(255),
  phone_number varchar(11),
  status varchar(255)
);
create table parking_lot (
  id bigint not null identity,
  name varchar(255),
  capacity bigint not null
);
create table parking_order (
  id bigint not null identity,
  car_number varchar(255) not null,
  request_type varchar(255),
  status varchar(255),
  parking_boy_id bigint
);
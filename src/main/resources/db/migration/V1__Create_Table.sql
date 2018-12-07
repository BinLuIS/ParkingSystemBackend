create table parking_boy (
  id bigint not null,
  name varchar(255),
  email varchar(255),
  phoneNumber varchar(11),
  status varchar(255),
  primary key (id)
);
create table parking_lot (
  id bigint not null,
  name varchar(255),
  capacity bigint not null,
  primary key (id)
);
create table parking_order (
  id bigint not null,
  carNumber varchar(255) not null,
  requestType varchar(255),
  status varchar(255),
  primary key (id)
);
create table company (
  id bigint not null,
  name varchar(255),
  profile_id bigint,
  primary key (id)
);
create table company_profile (
  id bigint not null,
  cert_id varchar(255),
  registered_capital bigint not null,
  primary key (id)
);
create table employee (
  id bigint not null,
  age integer not null,
  name varchar(255), primary key (id)
);

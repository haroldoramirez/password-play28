# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table password (
  id                            bigint auto_increment not null,
  name                          varchar(255),
  url                           varchar(255),
  username                      varchar(255),
  password                      varchar(255),
  registration_date             timestamp not null,
  change_date                   timestamp not null,
  constraint pk_password primary key (id)
);


# --- !Downs

drop table if exists password;


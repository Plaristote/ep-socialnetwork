# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table message (
  id                        bigint not null,
  from_id                   bigint,
  to_id                     bigint,
  at                        timestamp,
  message                   varchar(255),
  read                      boolean,
  constraint pk_message primary key (id))
;

create table picture (
  id                        bigint not null,
  user_id                   bigint,
  description               varchar(255),
  uri                       varchar(255),
  constraint pk_picture primary key (id))
;

create table user (
  id                        bigint not null,
  email                     varchar(255),
  first_name                varchar(255),
  last_name                 varchar(255),
  password                  varchar(255),
  signup_at                 timestamp,
  birthday                  timestamp,
  picture_id                bigint,
  phone                     varchar(255),
  location                  varchar(255),
  about                     varchar(255),
  constraint uq_user_email unique (email),
  constraint pk_user primary key (id))
;

create sequence message_seq;

create sequence picture_seq;

create sequence user_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists message;

drop table if exists picture;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists message_seq;

drop sequence if exists picture_seq;

drop sequence if exists user_seq;


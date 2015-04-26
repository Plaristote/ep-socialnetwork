create table friendships (
  id                        bigint not null,
  user_id                   bigint,
  friend_id                 bigint,
  constraint pk_friendships primary key (id))
;

create table messages (
  id                        bigint not null,
  from_id                   bigint,
  to_id                     bigint,
  at                        timestamp,
  message                   varchar(255),
  read                      boolean,
  constraint pk_messages primary key (id))
;

create table pictures (
  id                        bigint not null,
  user_id                   bigint,
  description               varchar(255),
  uri                       varchar(255),
  constraint pk_pictures primary key (id))
;

create table posts (
  id                        bigint not null,
  from_id                   bigint,
  to_id                     bigint,
  created_at                timestamp,
  description               varchar(255),
  url                       varchar(255),
  picture_id                bigint,
  enable                    boolean,
  highlight                 boolean,
  constraint pk_posts primary key (id))
;

create table users (
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
  constraint uq_users_email unique (email),
  constraint pk_users primary key (id))
;

create sequence friendships_seq;

create sequence messages_seq;

create sequence pictures_seq;

create sequence posts_seq;

create sequence users_seq;



drop table if exists friendships cascade;

drop table if exists messages cascade;

drop table if exists pictures cascade;

drop table if exists posts cascade;

drop table if exists users cascade;

drop sequence if exists friendships_seq;

drop sequence if exists messages_seq;

drop sequence if exists pictures_seq;

drop sequence if exists posts_seq;

drop sequence if exists users_seq;


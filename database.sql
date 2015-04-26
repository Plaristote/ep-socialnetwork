DROP TABLE IF EXISTS messages CASCADE;
DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS likes CASCADE;
DROP TABLE IF EXISTS posts CASCADE;
DROP TABLE IF EXISTS friends CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS pictures CASCADE;

DROP TYPE IF EXISTS TGENDER_USER CASCADE;
CREATE TYPE TGENDER_USER AS ENUM('MALE','FEMALE');

CREATE TABLE pictures
(
  id uuid DEFAULT uuid_generate_v4(),
  user_id uuid NOT NULL REFERENCES users,
  at timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone,
  description text,
  uri text CONSTRAINT pictures_uri_unique UNIQUE,
  enable boolean DEFAULT true,
  CONSTRAINT pictures_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE);
COMMENT ON COLUMN pictures.id IS 'Pictures ID';
COMMENT ON COLUMN pictures.at IS 'Date of upload';
COMMENT ON COLUMN pictures.description IS 'Picture s description';
COMMENT ON COLUMN pictures.uri IS 'Picture s URI';
COMMENT ON COLUMN pictures.enable IS 'Enable flag';

CREATE TABLE users
(
  id uuid DEFAULT uuid_generate_v4(),
  email text NOT NULL,
  first_name text,
  last_name text,
  password text,
  signup_at timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone,
  enable boolean DEFAULT true,
  picture_id uuid REFERENCES pictures,
  phone text,
  location text,
  birthday date,
  gender TGENDER_USER,
  about text,
  CONSTRAINT users_pkey PRIMARY KEY (id),
  CONSTRAINT users_email_unique UNIQUE (email),
  CONSTRAINT users_phone_unique UNIQUE (phone)
)
WITH (OIDS=FALSE);
COMMENT ON COLUMN users.id IS 'Users ID';
COMMENT ON COLUMN users.email IS 'E-mail of the user';
COMMENT ON COLUMN users.first_name IS 'First name of the user';
COMMENT ON COLUMN users.last_name IS 'Last name of the user';
COMMENT ON COLUMN users.password IS 'Password hash of the user';
COMMENT ON COLUMN users.signup_at IS 'Creation date and time';
COMMENT ON COLUMN users.enable IS 'Enable flag';
COMMENT ON COLUMN users.picture_id IS 'Pictures ID';
COMMENT ON COLUMN users.phone IS 'Phone number of the user';
COMMENT ON COLUMN users.location IS 'Location of the user';
COMMENT ON COLUMN users.birthday IS 'Birth date of the user';
COMMENT ON COLUMN users.birthday IS 'Gender of the user';
COMMENT ON COLUMN users.about IS 'About ...';

ALTER TABLE pictures ADD user_id uuid NOT NULL REFERENCES users;
COMMENT ON COLUMN pictures.user_id IS 'Users ID';
CREATE INDEX ON pictures USING hash (user_id);

CREATE TABLE messages
(
  id uuid DEFAULT uuid_generate_v4(),
  from_id uuid NOT NULL REFERENCES users,
  to_id uuid NOT NULL REFERENCES users,
  at timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone,
  message text,
  read boolean DEFAULT false,
  CONSTRAINT messages_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE);
COMMENT ON COLUMN messages.id IS 'Messages ID';
COMMENT ON COLUMN messages.from_id IS 'From users ID';
COMMENT ON COLUMN messages.to_id IS 'To users ID';
COMMENT ON COLUMN messages.at IS 'Date of creation';
COMMENT ON COLUMN messages.message IS 'Message';
COMMENT ON COLUMN messages.read IS 'Read flag';
CREATE INDEX ON messages USING hash (from_id);
CREATE INDEX ON messages USING hash (to_id);

CREATE TABLE posts
(
  id uuid DEFAULT uuid_generate_v4(),
  from_id uuid NOT NULL REFERENCES users,
  to_id uuid REFERENCES users,
  at timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone,
  description text,
  url text,
  picture_id uuid REFERENCES pictures,
  enable boolean DEFAULT true,
  highlight boolean DEFAULT false,
  CONSTRAINT posts_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE);
COMMENT ON COLUMN posts.id IS 'Posts ID';
COMMENT ON COLUMN posts.from_id IS 'From users ID';
COMMENT ON COLUMN posts.to_id IS 'To users ID';
COMMENT ON COLUMN posts.at IS 'Date of creation';
COMMENT ON COLUMN posts.description IS 'Content/Description';
COMMENT ON COLUMN posts.url IS 'Full url';
COMMENT ON COLUMN posts.picture_id IS 'Pictures ID';
COMMENT ON COLUMN posts.enable IS 'Enable flag';
COMMENT ON COLUMN posts.highlight IS 'Highlight flag';
CREATE INDEX ON posts USING hash (from_id);
CREATE INDEX ON posts USING hash (to_id);

CREATE TABLE comments
(
  id uuid DEFAULT uuid_generate_v4(),
  from_id uuid NOT NULL REFERENCES users,
  post_id uuid NOT NULL REFERENCES posts,
  at timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone,
  message text,
  enable boolean DEFAULT true,
  CONSTRAINT comments_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE);
COMMENT ON COLUMN comments.id IS 'Comments ID';
COMMENT ON COLUMN comments.from_id IS 'From users ID';
COMMENT ON COLUMN comments.post_id IS 'Posts ID';
COMMENT ON COLUMN comments.at IS 'Date of creation';
COMMENT ON COLUMN comments.message IS 'Comment';
COMMENT ON COLUMN comments.enable IS 'Enable flag';
CREATE INDEX ON comments USING hash (from_id);
CREATE INDEX ON comments USING hash (post_id);

CREATE TABLE likes
(
  id uuid DEFAULT uuid_generate_v4(),
  from_id uuid NOT NULL REFERENCES users,
  post_id uuid NOT NULL REFERENCES posts,
  at timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone,
  CONSTRAINT likes_pkey PRIMARY KEY (id)
)
WITH (OIDS=FALSE);
COMMENT ON COLUMN likes.id IS 'Likes ID';
COMMENT ON COLUMN likes.from_id IS 'From users ID';
COMMENT ON COLUMN likes.post_id IS 'Posts ID';
COMMENT ON COLUMN likes.at IS 'Date of creation';
CREATE INDEX ON likes USING hash (from_id);
CREATE INDEX ON likes USING hash (post_id);

CREATE TABLE friends
(
  user_1 uuid NOT NULL REFERENCES users,
  user_2 uuid NOT NULL REFERENCES users,
  at timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone,
  CONSTRAINT friends_pkey PRIMARY KEY (user_1, user_2)
)
WITH (OIDS=FALSE);
COMMENT ON COLUMN friends.user_1 IS 'User 1 ID';
COMMENT ON COLUMN friends.user_2 IS 'User 2 ID';
COMMENT ON COLUMN friends.at IS 'Date of creation';
CREATE INDEX ON friends USING hash (user_1);
CREATE INDEX ON friends USING hash (user_2);

CREATE TABLE session
(
  id uuid DEFAULT uuid_generate_v4(),
  user_id uuid NOT NULL REFERENCES users,
  at timestamp without time zone NOT NULL DEFAULT ('now'::text)::timestamp without time zone
)
WITH (OIDS=FALSE);
COMMENT ON COLUMN session.id IS 'Session ID';
COMMENT ON COLUMN session.user_id IS 'User ID';
COMMENT ON COLUMN session.at IS 'Date of creation';
CREATE INDEX ON session USING hash (user_id);


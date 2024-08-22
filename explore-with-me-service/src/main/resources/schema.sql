drop table if exists REQUEST;
drop table if exists EVENT;
drop table if exists USERS;
drop table if exists EVENT_CATEGORY;


CREATE TABLE IF NOT EXISTS USERS
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL primary key,
    name  VARCHAR(255)                            NOT NULL,
    email VARCHAR(254)                            NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS EVENT_CATEGORY
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL primary key,
    name VARCHAR(1000)                           NOT NULL unique
);

CREATE TABLE IF NOT EXISTS EVENT
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL primary key,
    user_id            bigint references USERS (id),
    annotation         VARCHAR(2000),
    category_id        BIGINT references EVENT_CATEGORY (ID)   not null,
    description        VARCHAR(7000),
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    location_lat       real,
    location_lon       real,
    paid               boolean,
    participant_limit  int,
    published_on       Timestamp without time zone,
    request_moderation boolean,
    title              VARCHAR(1000),
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    status             varchar(100)
);

CREATE TABLE IF NOT EXISTS REQUEST
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL primary key,
    requester_id bigint references USERS (id),
    event_id     bigint references EVENT (id),
    created_on   TIMESTAMP WITHOUT TIME ZONE,
    status       varchar(100)
);


CREATE TABLE IF NOT EXISTS COMPILATION
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL primary key,
    pinned   boolean,
    title    VARCHAR(50)                             not null
);

CREATE TABLE IF NOT EXISTS COMPILATION_EVENT
(
    compilation_id bigint references COMPILATION (id) on delete cascade,
    event_id       bigint references EVENT (id) on delete cascade
);

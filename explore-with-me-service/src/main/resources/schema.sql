-- drop table if exists BOOKING;
--
-- drop table if exists COMMENT;
--
-- drop table if exists ITEM;
--
-- drop table if exists REQUEST;
--
-- drop table if exists REQUEST;
-- drop table if exists EVENT;
-- drop table if exists USERS;
-- drop table if exists EVENT_CATEGORY;


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
    title              VARCHAR2(1000),
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



-- CREATE TABLE IF NOT EXISTS REQUEST
-- (
--     id            BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
--         constraint pk_request primary key,
--     description   VARCHAR(512),
--     requestor_id BIGINT references USERS (ID) on delete cascade,
--     creation_date TIMESTAMP WITHOUT TIME ZONE
-- );
--
-- CREATE TABLE IF NOT EXISTS ITEM
-- (
--     id           BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
--         constraint pk_item primary key,
--     name         VARCHAR(255)                            NOT NULL,
--     description  VARCHAR(255),
--     is_available boolean,
--     owner_id     bigint references USERS (ID) on delete cascade,
--     request_id   bigint references REQUEST (ID)
-- )
-- ;
--
-- CREATE TABLE IF NOT EXISTS BOOKING
-- (
--     id             BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL
--         constraint pk_booking primary key,
--     start_date     TIMESTAMP WITHOUT TIME ZONE,
--     end_date       TIMESTAMP WITHOUT TIME ZONE,
--     item_id        BIGINT references ITEM (ID),
--     booker_id      BIGINT references USERS (ID) on delete cascade,
--     booking_status varchar(100)
-- )
-- ;
--
-- CREATE TABLE IF NOT EXISTS COMMENT
-- (
--     id        BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL primary key,
--     text      varchar(1024),
--     item_id   BIGINT references ITEM (ID),
--     author_id BIGINT references USERS (ID) on delete cascade,
--     created   TIMESTAMP WITHOUT TIME ZONE
-- )
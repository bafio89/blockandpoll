create schema blockandpoll;

create table blockandpoll.poll
(
    id                      serial      not null
        constraint poll_pkey
            primary key,
    name                    varchar(30) not null,
    start_subscription_time timestamp with time zone,
    end_subscription_time   timestamp with time zone,
    start_voting_time       timestamp with time zone,
    end_voting_time         timestamp with time zone,
    description             text,
    app_id                  varchar(50),
    question                varchar(60)
);

create table blockandpoll.poll_options
(
    id_poll bigint      not null,
    option  varchar(20) not null,
    constraint poll_options_pkey
        primary key (option, id_poll)
);
drop table if exists job_client;
create table job_client
(
    id             serial primary key,
    client_name    varchar(255) NOT NULL,
    client_address varchar(255) NOT NULL,
    update_time    timestamp DEFAULT NULL
);
create unique index job_client_unique on job_client(client_name, client_address);

drop table if exists job_client_handler;
create table job_client_handler
(
    id             serial primary key,
    client_id      int4         NOT NULL,
    client_handler varchar(255) NOT NULL
);
create unique index job_client_handler_unique on job_client_handler(client_id, client_handler);

drop table if exists job_trigger;
create table job_trigger
(
    id               serial primary key,
    status           int2         NOT NULL DEFAULT '0',
    trigger_type     varchar(50)  NOT NULL DEFAULT 'NONE',
    trigger_param    varchar(128)          DEFAULT NULL,
    handler_name     varchar(255)          DEFAULT NULL,
    handler_param    varchar(512)          DEFAULT NULL,
    misfire_strategy varchar(50)  NOT NULL DEFAULT 'DO_NOTHING',
    route_strategy   varchar(50)           DEFAULT NULL,
    block_strategy   varchar(50)           DEFAULT NULL,
    timeout          int4         NOT NULL DEFAULT '0',
    glue_type        varchar(50)  NOT NULL,
    glue_source      text,
    glue_remark      varchar(128)          DEFAULT NULL,
    glue_update      timestamp             DEFAULT NULL,
    last_time        int8         NOT NULL DEFAULT '0',
    next_time        int8         NOT NULL DEFAULT '0',
    job_desc         varchar(255) NOT NULL,
    alarm_email      varchar(255)          DEFAULT NULL,
    create_by        varchar(64),
    create_time      timestamp,
    update_time      timestamp
);
comment on column job_trigger.status is '调度状态：0-停止，1-运行';
comment on column job_trigger.trigger_type is '调度类型';
comment on column job_trigger.trigger_param is '调度参数';
comment on column job_trigger.handler_name is '执行handler';
comment on column job_trigger.handler_param is '执行参数';
comment on column job_trigger.misfire_strategy is '调度过期策略';
comment on column job_trigger.route_strategy is '执行路由策略';
comment on column job_trigger.block_strategy is '阻塞处理策略';
comment on column job_trigger.timeout is '任务执行超时时间，单位秒';
comment on column job_trigger.glue_type is 'GLUE类型';
comment on column job_trigger.glue_source is 'GLUE源代码';
comment on column job_trigger.glue_remark is 'GLUE备注';
comment on column job_trigger.glue_update is 'GLUE更新时间';
comment on column job_trigger.last_time is '上次调度时间';
comment on column job_trigger.next_time is '下次调度时间';

drop table if exists job_trigger_log;
create table job_trigger_log
(
    id             bigserial primary key,
    trigger_id     int4 NOT NULL,
    trigger_status int4         DEFAULT '0',
    trigger_type   varchar(64)  DEFAULT NULL,
    trigger_time   timestamp    DEFAULT NULL,
    client_address varchar(255) DEFAULT NULL,
    sharding_param varchar(20)  DEFAULT NULL,
    handler_name   varchar(255) DEFAULT NULL,
    handler_param  varchar(512) DEFAULT NULL,
    handle_time    timestamp    DEFAULT NULL,
    handle_cost    int8,
    fail_msg       text
);

drop table if exists job_trigger_glue;
create table job_trigger_glue
(
    id          serial primary key,
    trigger_id  int4         NOT NULL,
    glue_type   varchar(50) DEFAULT NULL,
    glue_source text,
    remark      varchar(128) NOT NULL,
    create_time timestamp   DEFAULT NULL,
    update_time timestamp   DEFAULT NULL
);

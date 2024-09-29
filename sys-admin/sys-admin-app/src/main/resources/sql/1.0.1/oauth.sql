-- oauth 配置
drop table if exists oauth_config;
create table oauth_config
(
    id            serial primary key,
    app_type      character varying(64),
    app_id        character varying(64),
    app_secret    character varying(64),
    auth_url      character varying(256),
    redirect_url  character varying(256),
    grant_type    character varying(64),
    response_type character varying(64),
    auth_scope    character varying(128),
    role_code     character varying(64),
    status        int2 default 0,
    create_by     character varying(64),
    create_time   timestamp,
    update_by     character varying(64),
    update_time   timestamp
);
create unique index oauth_config_unique on oauth_config(app_type);
comment on table oauth_config is 'oauth配置';
comment on column oauth_config.id is 'id';
comment on column oauth_config.status is '状态 0 关闭 1开启';
comment on column oauth_config.role_code is '用户角色';
comment on column oauth_config.app_type is '授权服务类';
comment on column oauth_config.app_id is '应用id';
comment on column oauth_config.app_secret is '应用密钥';
comment on column oauth_config.auth_url is '授权服务url';
comment on column oauth_config.redirect_url is '应用回调地址';
comment on column oauth_config.grant_type is '授权类型';
comment on column oauth_config.response_type is '响应类型';
comment on column oauth_config.auth_scope is '授权范围';
comment on column oauth_config.create_by is '创建人';
comment on column oauth_config.create_time is '创建时间';
comment on column oauth_config.update_by is '更新人';
comment on column oauth_config.update_time is '更新时间';

-- oauth 授权用户
drop table if exists oauth_user;
create table oauth_user
(
    id           bigserial primary key,
    user_code    character varying(64),
    role_code    character varying(64),
    app_type     character varying(64),
    user_name    character varying(64),
    user_account character varying(64),
    user_avatar  character varying(256),
    user_email   character varying(64),
    user_dept    character varying(256),
    create_time  timestamp,
    update_time  timestamp
);
create unique index oauth_user_unique on oauth_user(app_type, user_account);
comment on table oauth_user is '授权用户';
comment on column oauth_user.id is 'id';
comment on column oauth_user.app_type is '应用类型';
comment on column oauth_user.role_code is '用户角色';
comment on column oauth_user.user_name is '用户名称';
comment on column oauth_user.user_account is '用户账号';
comment on column oauth_user.user_avatar is '用户头像';
comment on column oauth_user.user_email is '用户邮箱';
comment on column oauth_user.user_dept is '用户部门';
comment on column oauth_user.create_time is '创建时间';
comment on column oauth_user.update_time is '更新时间';
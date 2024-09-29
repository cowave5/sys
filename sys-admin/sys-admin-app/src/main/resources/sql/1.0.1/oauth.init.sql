-- 配置
INSERT INTO oauth_config ("status", "user_role", "app_type", "app_id", "app_secret", "auth_url", "redirect_url", "grant_type", "response_type", "auth_scope", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES (1, 3, 'gitlab', '155cbf79f0e8dc8c6b912b2ac033e741e9efb9ecffe661571693d392c279bc5d', '5eb6f1172bb175145f29d772f8ebacde5e37be4743cebbf917da2e8c68d999ec', 'https://gitlab.cowave.com', 'http://10.64.4.74:81/oauth/gitlab', 'authorization_code', 'code', 'read_user', 1, NULL, '2024-03-18 13:57:36.631', 1, NULL, '2024-03-18 13:59:53.624');

-- 菜单
delete from sys_menu where menu_type = 'M' and menu_path = 'oauth';
delete from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:edit';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:query';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:user:query';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:user:edit';
delete from sys_menu where menu_type = 'B' and menu_permit = 'oauth:gitlab:user:delete';

INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_path = 'system'), 'OAuth认证', 9, NULL, 'oauth', NULL, 'M', 'vscope', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-02 23:08:01.779', 1, NULL, '2024-03-02 23:08:01.779');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_path = 'oauth'), 'Gitlab', 1, 'sys:oauth:config', 'gitlab', NULL, 'C', 'gitlab', 'system/oauth/gitlab', 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '配置', 1, 'oauth:gitlab:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '查询', 2, 'oauth:gitlab:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '用户查询', 3, 'oauth:gitlab:user:query', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '用户修改', 4, 'oauth:gitlab:user:edit', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'C' and component = 'system/oauth/gitlab'), '用户删除', 5, 'oauth:gitlab:user:delete', '#', NULL, 'B', '#', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-21 13:57:12.65', 1, NULL, '2024-03-21 13:57:12.65');

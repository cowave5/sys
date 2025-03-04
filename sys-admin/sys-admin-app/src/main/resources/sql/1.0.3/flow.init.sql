-- 菜单
delete from sys_menu where menu_type = 'M' and menu_name = '流程管理';
delete from sys_menu where menu_type = 'C' and component = 'system/flow/instance';
delete from sys_menu where menu_type = 'C' and component = 'system/flow/modeler';
delete from sys_menu where menu_type = 'C' and component = 'system/flow/deploy';

INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_path = 'system'), '流程管理', 11, NULL, 'flow', NULL, 'M', 'flow', NULL, 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-02 23:08:01.779', 1, NULL, '2024-03-02 23:08:01.779');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_name = '流程管理'), '流程实例', 3, 'sys:flow:instance', 'instance', NULL, 'C', 'flowinstance', 'system/flow/instance', 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_name = '流程管理'), '模型设计', 1, 'sys:flow:modeler', 'modeler', NULL, 'C', 'component', 'system/flow/modeler', 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_name = '流程管理'), '流程部署', 2, 'sys:flow:deploy', 'deploy', NULL, 'C', 'deploy', 'system/flow/deploy', 1, 1, 1, 1, 1, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');

delete from sys_menu where menu_type = 'M' and menu_name = '流程审批';
delete from sys_menu where menu_type = 'C' and component = 'workbench/leave';
delete from sys_menu where menu_type = 'C' and component = 'workbench/meeting';
delete from sys_menu where menu_type = 'C' and component = 'workbench/purchase';
delete from sys_menu where menu_type = 'C' and component = 'workbench/task';

INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES (0, '流程审批', 1, NULL, 'workbench', NULL, 'M', 'cascader', NULL, 1, 1, 1, 1, 0, NULL, 0, 1, NULL, '2023-08-10 06:43:00.55', 1, NULL, '2023-08-10 07:32:09.243');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_name = '流程审批'), '任务办理', 1, 'flow:task', 'task', NULL, 'C', 'task', 'workbench/task', 1, 1, 1, 1, 0, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_name = '流程审批'), '请假申请', 2, 'flow:leave', 'leave', NULL, 'C', 'leave', 'workbench/leave', 1, 1, 1, 1, 0, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_name = '流程审批'), '会议预约', 3, 'flow:meeting', 'meeting', NULL, 'C', 'meeting', 'workbench/meeting', 1, 1, 1, 1, 0, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');
INSERT INTO "sys_menu" ("parent_id", "menu_name", "menu_order", "menu_permit", "menu_path", "menu_param", "menu_type", "menu_icon", "component", "menu_status", "is_frame", "is_cache", "is_visible", "is_protected", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ((select menu_id from sys_menu where menu_type = 'M' and menu_name = '流程审批'), '采购申请', 4, 'flow:purchase', 'purchase', NULL, 'C', 'purchase', 'workbench/purchase', 1, 1, 1, 1, 0, NULL, 0, 1, NULL, '2024-03-02 23:10:39.323', 1, NULL, '2024-03-03 07:20:04.089');

-- 流程字典
delete from sys_dict where dict_code = 'dict_flow';
delete from sys_dict where dict_code = 'flow_leave';
delete from sys_dict where parent_code = 'flow_leave';

INSERT INTO "sys_dict" ("parent_code", "dict_code", "dict_label", "dict_en", "dict_value", "value_parser", "value_param", "dict_order", "is_default", "css", "status", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('dict_group', 'dict_flow', '流程字典', 'Flow Dict', 'group_flow', NULL, NULL, 1, 0, NULL, 1, NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO "sys_dict" ("parent_code", "dict_code", "dict_label", "dict_en", "dict_value", "value_parser", "value_param", "dict_order", "is_default", "css", "status", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('dict_flow', 'flow_leave', '请假类型', 'Leave Type', 'leave_type', NULL, NULL, 2, 0, NULL, 1, NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO "sys_dict" ("parent_code", "dict_code", "dict_label", "dict_en", "dict_value", "value_parser", "value_param", "dict_order", "is_default", "css", "status", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('flow_leave', 'annual', '年假', 'Annual Leave', '1', 'com.cowave.commons.framework.helper.redis.dict.DefaultValueParser', 'int', 181, 0, NULL, 1, NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO "sys_dict" ("parent_code", "dict_code", "dict_label", "dict_en", "dict_value", "value_parser", "value_param", "dict_order", "is_default", "css", "status", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('flow_leave', 'personal', '事假', 'Personal Leave', '2', 'com.cowave.commons.framework.helper.redis.dict.DefaultValueParser', 'int', 182, 0, NULL, 1, NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO "sys_dict" ("parent_code", "dict_code", "dict_label", "dict_en", "dict_value", "value_parser", "value_param", "dict_order", "is_default", "css", "status", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('flow_leave', 'sick', '病假', 'Sick Leave', '3', 'com.cowave.commons.framework.helper.redis.dict.DefaultValueParser', 'int', 183, 0, NULL, 1, NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO "sys_dict" ("parent_code", "dict_code", "dict_label", "dict_en", "dict_value", "value_parser", "value_param", "dict_order", "is_default", "css", "status", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('flow_leave', 'bereavement', '丧假', 'Bereavement Leave', '4', 'com.cowave.commons.framework.helper.redis.dict.DefaultValueParser', 'int', 184, 0, NULL, 1, NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO "sys_dict" ("parent_code", "dict_code", "dict_label", "dict_en", "dict_value", "value_parser", "value_param", "dict_order", "is_default", "css", "status", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('flow_leave', 'maternity', '产假', 'Maternity Leave', '5', 'com.cowave.commons.framework.helper.redis.dict.DefaultValueParser', 'int', 185, 0, NULL, 1, NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');

delete from sys_dict where dict_code = 'type_press';
INSERT INTO "sys_dict" ("parent_code", "dict_code", "dict_label", "dict_en", "dict_value", "value_parser", "value_param", "dict_order", "is_default", "css", "status", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('notice_type', 'type_press', '催办提醒', 'press', '3', 'com.cowave.commons.framework.helper.redis.dict.DefaultValueParser', 'int', 153, 0, NULL, 1, NULL, 0, 1, NULL, '2023-08-11 14:21:50.681', 1, NULL, '2023-08-11 14:35:58.346');

-- 序列
DROP SEQUENCE IF EXISTS flow_leave_SEQUENCE;
CREATE SEQUENCE flow_leave_SEQUENCE INCREMENT BY 1 NO MAXVALUE START WITH 1;

DROP SEQUENCE IF EXISTS flow_meeting_SEQUENCE;
CREATE SEQUENCE flow_meeting_SEQUENCE INCREMENT BY 1 NO MAXVALUE START WITH 1;

DROP SEQUENCE IF EXISTS flow_purchase_SEQUENCE;
CREATE SEQUENCE flow_purchase_SEQUENCE INCREMENT BY 1 NO MAXVALUE START WITH 1;

-- 角色权限
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, (select menu_id from sys_menu where menu_type = 'M' and menu_name = '流程管理'));
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, (select menu_id from sys_menu where menu_type = 'C' and component = 'system/flow/instance'));
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, (select menu_id from sys_menu where menu_type = 'C' and component = 'system/flow/modeler'));
INSERT INTO "sys_role_menu" ("role_id", "menu_id") VALUES (3, (select menu_id from sys_menu where menu_type = 'C' and component = 'system/flow/deploy'));

INSERT INTO "sys_user" ("user_name", "user_account", "user_passwd", "user_sex", "user_phone", "user_email", "user_status", "rank", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('大乔', 'daqiao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 1, '13288888888', 'daqiao@cowave.com', 1, 'M2', NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');
INSERT INTO "sys_user" ("user_name", "user_account", "user_passwd", "user_sex", "user_phone", "user_email", "user_status", "rank", "remark", "read_only", "create_user", "create_dept", "create_time", "update_user", "update_dept", "update_time")
VALUES ('小乔', 'xiaoqiao', '$2a$10$q8HvVpWNp0kadKq49IQO/OT2ZVK9HeimiEVNbb61LTWMmtvUIuZnq', 1, '13288888888', 'xiaoqiao@cowave.com', 1, 'M2', NULL, 1, 1, NULL, '2022-04-25 09:00:00', 1, NULL, '2022-04-25 09:00:00');

INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader")
VALUES ((select user_id from sys_user where user_account = 'daqiao'), (select dept_id from sys_dept where dept_code = 'FD'), (select post_id from sys_post where post_code = 'AC'), 1, 0);
INSERT INTO "sys_user_dept" ("user_id", "dept_id", "post_id", "is_default", "is_leader")
VALUES ((select user_id from sys_user where user_account = 'xiaoqiao'), (select dept_id from sys_dept where dept_code = 'FD'), (select post_id from sys_post where post_code = 'ACCT'), 1, 0);

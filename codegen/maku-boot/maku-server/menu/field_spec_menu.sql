
-- 初始化菜单
INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES (1, '字段信息表', 'maku/spec/index', NULL, 0, 0, 'icon-menu', 0, 0, 0, 10000, now(), 10000, now());

INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT max(id) from sys_menu where name = '字段信息表'), '查看', '', 'maku:spec:page', 1, 0, '', 0, 0, 0, 10000, now(), 10000, now());
INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT max(id) from sys_menu where name = '字段信息表'), '新增', '', 'maku:spec:save', 1, 0, '', 1, 0, 0, 10000, now(), 10000, now());
INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT max(id) from sys_menu where name = '字段信息表'), '修改', '', 'maku:spec:update,maku:spec:info', 1, 0, '', 2, 0, 0, 10000, now(), 10000, now());
INSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT max(id) from sys_menu where name = '字段信息表'), '删除', '', 'maku:spec:delete', 1, 0, '', 3, 0, 0, 10000, now(), 10000, now());

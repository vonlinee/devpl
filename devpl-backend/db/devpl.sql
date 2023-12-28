/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80035
 Source Host           : localhost:3306
 Source Schema         : devpl

 Target Server Type    : MySQL
 Target Server Version : 80035
 File Encoding         : 65001

 Date: 28/12/2023 21:49:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for column_info
-- ----------------------------
DROP TABLE IF EXISTS `column_info`;
CREATE TABLE `column_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `table_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '所属表的ID',
  `table_cat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'table catalog (maybe null)',
  `table_schem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'table schema (maybe null)',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名称',
  `column_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '列名称',
  `data_type` int NULL DEFAULT NULL COMMENT 'SQL type from java.sql.Type',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据源独立的类型名称, for a UDT the type name is fully qualified',
  `column_size` int NULL DEFAULT NULL COMMENT 'column size.有符号数长度会减少1，比如bigint(20)，此时columnSize=19',
  `buffer_length` int NULL DEFAULT NULL COMMENT 'not used.',
  `decimal_digits` int NULL DEFAULT NULL COMMENT '小数位数',
  `num_prec_radix` int NULL DEFAULT NULL COMMENT 'NUM_PREC_RADIX int => Radix (typically either 10 or 2) (基数,即十进制或者二进制)',
  `nullable` int UNSIGNED NULL DEFAULT NULL COMMENT '是否允许NULL. 0 - Indicates that the column definitely allows NULL values. 1 - Indicates that the column definitely allows NULL values. 2 - Indicates that the nullability of columns is unknown.',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该列的描述信息，可为null',
  `column_def` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该列的默认值, 如果值被单引号引起来，则表示该值是字符串(maybe null)',
  `sql_data_type` int NULL DEFAULT NULL COMMENT 'unused',
  `sql_datetime_sub` int NULL DEFAULT NULL COMMENT 'unused',
  `char_octet_length` int NULL DEFAULT NULL COMMENT '字符类型的最大字节数 CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column',
  `ordinal_position` int NULL DEFAULT NULL COMMENT '该列在表中的位置，开始为1',
  `is_nullable` tinyint UNSIGNED NULL DEFAULT NULL COMMENT 'ISO rules are used to determine the nullability for a column. YES --- if the column can include NULLs NO --- if the column cannot include NULLs empty string --- if the nullability for the column is unknown',
  `scope_catalog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'catalog of table that is the scope of a reference attribute (null if DATA_TYPE is not REF)',
  `scope_schema` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'schema of table that is the scope of a reference attribute (null if the DATA_TYPE is not REF)',
  `scope_table` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'table name that this the scope of a reference attribute (null if the DATA_TYPE is not REF)',
  `source_data_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'source type of distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE is not DISTINCT or user-generated REF)',
  `is_autoincrement` tinyint UNSIGNED NULL DEFAULT NULL COMMENT 'Indicates whether this column is auto incremented YES --- if the column is auto incremented NO --- if the column is not auto incremented empty string --- if it cannot be determined whether the column is auto incremented',
  `is_generated` tinyint UNSIGNED NULL DEFAULT NULL COMMENT 'Indicates whether this is a generated column YES --- if this a generated column NO --- if this not a generated column empty string --- if it cannot be determined whether this is a generated column',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据库表列信息记录表（对应JDBC的ColumnMetadata）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of column_info
-- ----------------------------

-- ----------------------------
-- Table structure for data_type_group
-- ----------------------------
DROP TABLE IF EXISTS `data_type_group`;
CREATE TABLE `data_type_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分组ID',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '分组名称',
  `internal` tinyint NULL DEFAULT 0 COMMENT '是否内置类型分组，内置类型分组不可更改',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据类型分组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of data_type_group
-- ----------------------------
INSERT INTO `data_type_group` VALUES (1, 'JSON', '标准JSON类型', 1, NULL, NULL, NULL, 0);
INSERT INTO `data_type_group` VALUES (2, 'JDBC', 'JDBC类型', 1, NULL, NULL, NULL, 0);
INSERT INTO `data_type_group` VALUES (3, 'JAVA', 'Java类型', 1, NULL, NULL, NULL, 0);
INSERT INTO `data_type_group` VALUES (11, 'MyBatisMS', 'MyBatis Mapper参数', 1, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for data_type_item
-- ----------------------------
DROP TABLE IF EXISTS `data_type_item`;
CREATE TABLE `data_type_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型分组名称',
  `type_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型ID',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型名称',
  `value_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该数据类型的值类型',
  `min_length` double NULL DEFAULT NULL COMMENT '最小长度',
  `max_length` double NULL DEFAULT NULL COMMENT '最大长度',
  `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型默认值',
  `precision` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '精度',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
  `operation` tinyint(1) NULL DEFAULT -1 COMMENT '操作',
  `internal` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统内部定义，不可删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unk_type`(`type_group_id` ASC, `type_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 311 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of data_type_item
-- ----------------------------
INSERT INTO `data_type_item` VALUES (144, 'JSON', 'Number', 'JSON数字类型', '8990', 10, 50, '0', '3434', 'JavaScript 中的双精度浮点型格式', NULL, '2023-10-21 10:20:55', 0, NULL, 1);
INSERT INTO `data_type_item` VALUES (145, 'JSON', 'String', '字符串', '555', NULL, NULL, '\"\"', '', '双引号包裹的 Unicode 字符和反斜杠转义字符', NULL, '2023-10-21 10:06:05', 0, NULL, 1);
INSERT INTO `data_type_item` VALUES (146, 'JSON', 'Array', 'JSON数字类型', NULL, NULL, NULL, '0', '', '有序的值序列，参考网站：https://www.w3cschool.c', NULL, NULL, 0, NULL, 1);
INSERT INTO `data_type_item` VALUES (147, 'JSON', 'null', 'NULL类型', NULL, 0, 0, 'null', '', '空', NULL, NULL, 0, NULL, 1);
INSERT INTO `data_type_item` VALUES (187, 'JSON', 'Boolean', '布尔型', NULL, 0, 0, 'false', '', 'true 或 false', NULL, NULL, 0, NULL, 1);
INSERT INTO `data_type_item` VALUES (227, 'JSON', 'Object', '对象', NULL, 0, 0, '{}', '', '无序的键:值对集合', NULL, NULL, 0, NULL, 1);
INSERT INTO `data_type_item` VALUES (267, 'JSON', 'Value', '值类型', NULL, 0, 0, '', '', '可以是字符串，数字，true 或 false，null 等等', NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (268, 'JSON', 'Whitespace', '空格', NULL, 0, 0, '', '', '可用于任意符号对之间', NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (269, 'JDBC', 'BIT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (270, 'JDBC', 'TINYINT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (271, 'JDBC', 'SMALLINT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (272, 'JDBC', 'INTEGER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (273, 'JDBC', 'BIGINT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (274, 'JDBC', 'FLOAT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (275, 'JDBC', 'REAL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (276, 'JDBC', 'DOUBLE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (277, 'JDBC', 'NUMERIC', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (278, 'JDBC', 'DECIMAL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (279, 'JDBC', 'CHAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (280, 'JDBC', 'VARCHAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (281, 'JDBC', 'LONGVARCHAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (282, 'JDBC', 'DATE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (283, 'JDBC', 'TIME', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (284, 'JDBC', 'TIMESTAMP', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (285, 'JDBC', 'BINARY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (286, 'JDBC', 'VARBINARY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (287, 'JDBC', 'LONGVARBINARY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (288, 'JDBC', 'NULL', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (289, 'JDBC', 'OTHER', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (290, 'JDBC', 'JAVA_OBJECT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (291, 'JDBC', 'DISTINCT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (292, 'JDBC', 'STRUCT', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (293, 'JDBC', 'ARRAY', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (294, 'JDBC', 'BLOB', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (295, 'JDBC', 'CLOB', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (296, 'JDBC', 'REF', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (297, 'JDBC', 'DATALINK', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (298, 'JDBC', 'BOOLEAN', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (299, 'JDBC', 'ROWID', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (300, 'JDBC', 'NCHAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (301, 'JDBC', 'NVARCHAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (302, 'JDBC', 'LONGNVARCHAR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (303, 'JDBC', 'NCLOB', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (304, 'JDBC', 'SQLXML', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (305, 'JDBC', 'REF_CURSOR', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (306, 'JDBC', 'TIME_WITH_TIMEZONE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (307, 'JDBC', 'TIMESTAMP_WITH_TIMEZONE', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 0, -1, 1);
INSERT INTO `data_type_item` VALUES (308, 'JAVA', 'String', '字符串', NULL, 0, 0, '', '', '', NULL, NULL, 0, -1, 0);
INSERT INTO `data_type_item` VALUES (309, 'MyBatisMS', 'Numeric', '数值类型', NULL, 0, 0, '', '', '数值类型', NULL, NULL, 0, -1, 0);
INSERT INTO `data_type_item` VALUES (310, 'MyBatisMS', 'String', '字符串类型', NULL, 0, 0, '', '', '字符串类型', NULL, NULL, 0, -1, 0);

-- ----------------------------
-- Table structure for data_type_mapping
-- ----------------------------
DROP TABLE IF EXISTS `data_type_mapping`;
CREATE TABLE `data_type_mapping`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_id` bigint NULL DEFAULT NULL COMMENT '主数据类型ID',
  `another_type_id` bigint NULL DEFAULT NULL COMMENT '映射数据类型ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据类型映射关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of data_type_mapping
-- ----------------------------
INSERT INTO `data_type_mapping` VALUES (1, 145, NULL);
INSERT INTO `data_type_mapping` VALUES (2, 146, NULL);
INSERT INTO `data_type_mapping` VALUES (3, 147, NULL);

-- ----------------------------
-- Table structure for database_backup_history
-- ----------------------------
DROP TABLE IF EXISTS `database_backup_history`;
CREATE TABLE `database_backup_history`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `save_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保存位置',
  `backup_time` datetime NULL DEFAULT NULL COMMENT '备份时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 87 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据库备份历史记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of database_backup_history
-- ----------------------------
INSERT INTO `database_backup_history` VALUES (1, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022102818.sql', '2023-10-22 10:28:19', '2023-10-22 10:28:18');
INSERT INTO `database_backup_history` VALUES (2, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022103801.sql', '2023-10-22 10:38:01', '2023-10-22 10:38:01');
INSERT INTO `database_backup_history` VALUES (3, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022103906.sql', '2023-10-22 10:39:06', '2023-10-22 10:39:06');
INSERT INTO `database_backup_history` VALUES (4, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022115533.sql', '2023-10-22 11:55:34', '2023-10-22 11:55:34');
INSERT INTO `database_backup_history` VALUES (5, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022153037.sql', '2023-10-22 15:30:38', '2023-10-22 15:30:37');
INSERT INTO `database_backup_history` VALUES (6, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022153202.sql', '2023-10-22 15:32:03', '2023-10-22 15:32:03');
INSERT INTO `database_backup_history` VALUES (7, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022164904.sql', '2023-10-22 16:49:05', '2023-10-22 16:49:04');
INSERT INTO `database_backup_history` VALUES (8, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022171500.sql', '2023-10-22 17:15:01', '2023-10-22 17:15:01');
INSERT INTO `database_backup_history` VALUES (9, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231022171931.sql', '2023-10-22 17:19:32', '2023-10-22 17:19:31');
INSERT INTO `database_backup_history` VALUES (10, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231029104903.sql', '2023-10-29 10:49:04', '2023-10-29 10:49:04');
INSERT INTO `database_backup_history` VALUES (11, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231029105536.sql', '2023-10-29 10:55:37', '2023-10-29 10:55:37');
INSERT INTO `database_backup_history` VALUES (12, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231029105648.sql', '2023-10-29 10:56:49', '2023-10-29 10:56:48');
INSERT INTO `database_backup_history` VALUES (13, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231029105734.sql', '2023-10-29 10:57:35', '2023-10-29 10:57:35');
INSERT INTO `database_backup_history` VALUES (14, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231126235505.sql', '2023-11-26 23:55:06', '2023-11-26 23:55:05');
INSERT INTO `database_backup_history` VALUES (15, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231127083537.sql', '2023-11-27 08:35:38', '2023-11-27 08:35:37');
INSERT INTO `database_backup_history` VALUES (16, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231129224003.sql', '2023-11-29 22:40:04', '2023-11-29 22:40:03');
INSERT INTO `database_backup_history` VALUES (17, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130083043.sql', '2023-11-30 08:30:44', '2023-11-30 08:30:44');
INSERT INTO `database_backup_history` VALUES (18, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130083056.sql', '2023-11-30 08:30:57', '2023-11-30 08:30:56');
INSERT INTO `database_backup_history` VALUES (19, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130083129.sql', '2023-11-30 08:31:29', '2023-11-30 08:31:29');
INSERT INTO `database_backup_history` VALUES (20, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130083815.sql', '2023-11-30 08:38:15', '2023-11-30 08:38:15');
INSERT INTO `database_backup_history` VALUES (21, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130085439.sql', '2023-11-30 08:54:39', '2023-11-30 08:54:39');
INSERT INTO `database_backup_history` VALUES (22, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130085719.sql', '2023-11-30 08:57:20', '2023-11-30 08:57:19');
INSERT INTO `database_backup_history` VALUES (23, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130085758.sql', '2023-11-30 08:57:59', '2023-11-30 08:57:58');
INSERT INTO `database_backup_history` VALUES (24, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130092615.sql', '2023-11-30 09:26:16', '2023-11-30 09:26:15');
INSERT INTO `database_backup_history` VALUES (25, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130131336.sql', '2023-11-30 13:13:37', '2023-11-30 13:13:37');
INSERT INTO `database_backup_history` VALUES (26, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130134030.sql', '2023-11-30 13:40:30', '2023-11-30 13:40:30');
INSERT INTO `database_backup_history` VALUES (27, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130135130.sql', '2023-11-30 13:51:31', '2023-11-30 13:51:30');
INSERT INTO `database_backup_history` VALUES (28, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130140521.sql', '2023-11-30 14:05:22', '2023-11-30 14:05:21');
INSERT INTO `database_backup_history` VALUES (29, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130154808.sql', '2023-11-30 15:48:09', '2023-11-30 15:48:08');
INSERT INTO `database_backup_history` VALUES (30, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130154831.sql', '2023-11-30 15:48:32', '2023-11-30 15:48:31');
INSERT INTO `database_backup_history` VALUES (31, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130155555.sql', '2023-11-30 15:55:56', '2023-11-30 15:55:56');
INSERT INTO `database_backup_history` VALUES (32, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130155651.sql', '2023-11-30 15:56:51', '2023-11-30 15:56:51');
INSERT INTO `database_backup_history` VALUES (33, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130155713.sql', '2023-11-30 15:57:13', '2023-11-30 15:57:13');
INSERT INTO `database_backup_history` VALUES (34, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130155925.sql', '2023-11-30 15:59:25', '2023-11-30 15:59:25');
INSERT INTO `database_backup_history` VALUES (35, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130162848.sql', '2023-11-30 16:28:49', '2023-11-30 16:28:49');
INSERT INTO `database_backup_history` VALUES (36, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130200647.sql', '2023-11-30 20:06:48', '2023-11-30 20:06:48');
INSERT INTO `database_backup_history` VALUES (37, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130204757.sql', '2023-11-30 20:47:58', '2023-11-30 20:47:58');
INSERT INTO `database_backup_history` VALUES (38, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130213137.sql', '2023-11-30 21:31:38', '2023-11-30 21:31:38');
INSERT INTO `database_backup_history` VALUES (39, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130223844.sql', '2023-11-30 22:38:46', '2023-11-30 22:38:45');
INSERT INTO `database_backup_history` VALUES (40, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130224124.sql', '2023-11-30 22:41:25', '2023-11-30 22:41:25');
INSERT INTO `database_backup_history` VALUES (41, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130224324.sql', '2023-11-30 22:43:25', '2023-11-30 22:43:25');
INSERT INTO `database_backup_history` VALUES (42, 'C:\\Users\\vonline\\Documents\\GitHub\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231130224820.sql', '2023-11-30 22:48:21', '2023-11-30 22:48:21');
INSERT INTO `database_backup_history` VALUES (43, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201101235.sql', '2023-12-01 10:12:36', '2023-12-01 10:12:36');
INSERT INTO `database_backup_history` VALUES (44, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201102448.sql', '2023-12-01 10:24:49', '2023-12-01 10:24:48');
INSERT INTO `database_backup_history` VALUES (45, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201103136.sql', '2023-12-01 10:31:36', '2023-12-01 10:31:36');
INSERT INTO `database_backup_history` VALUES (46, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201103305.sql', '2023-12-01 10:33:06', '2023-12-01 10:33:05');
INSERT INTO `database_backup_history` VALUES (47, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201112126.sql', '2023-12-01 11:21:27', '2023-12-01 11:21:27');
INSERT INTO `database_backup_history` VALUES (48, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201112902.sql', '2023-12-01 11:29:03', '2023-12-01 11:29:02');
INSERT INTO `database_backup_history` VALUES (49, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201113000.sql', '2023-12-01 11:30:01', '2023-12-01 11:30:00');
INSERT INTO `database_backup_history` VALUES (50, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201113045.sql', '2023-12-01 11:30:46', '2023-12-01 11:30:45');
INSERT INTO `database_backup_history` VALUES (51, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201113521.sql', '2023-12-01 11:35:22', '2023-12-01 11:35:22');
INSERT INTO `database_backup_history` VALUES (52, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201113550.sql', '2023-12-01 11:35:51', '2023-12-01 11:35:50');
INSERT INTO `database_backup_history` VALUES (53, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201114518.sql', '2023-12-01 11:45:18', '2023-12-01 11:45:18');
INSERT INTO `database_backup_history` VALUES (54, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201115635.sql', '2023-12-01 11:56:35', '2023-12-01 11:56:35');
INSERT INTO `database_backup_history` VALUES (55, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201135857.sql', '2023-12-01 13:58:58', '2023-12-01 13:58:57');
INSERT INTO `database_backup_history` VALUES (56, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201140128.sql', '2023-12-01 14:01:28', '2023-12-01 14:01:28');
INSERT INTO `database_backup_history` VALUES (57, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201140437.sql', '2023-12-01 14:04:37', '2023-12-01 14:04:37');
INSERT INTO `database_backup_history` VALUES (58, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201141327.sql', '2023-12-01 14:13:55', '2023-12-01 14:14:22');
INSERT INTO `database_backup_history` VALUES (59, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201141539.sql', '2023-12-01 14:15:40', '2023-12-01 14:15:39');
INSERT INTO `database_backup_history` VALUES (60, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201141558.sql', '2023-12-01 14:15:59', '2023-12-01 14:15:58');
INSERT INTO `database_backup_history` VALUES (61, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201142033.sql', '2023-12-01 14:20:34', '2023-12-01 14:20:34');
INSERT INTO `database_backup_history` VALUES (62, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201142150.sql', '2023-12-01 14:21:51', '2023-12-01 14:21:50');
INSERT INTO `database_backup_history` VALUES (63, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201153538.sql', '2023-12-01 15:35:38', '2023-12-01 15:35:38');
INSERT INTO `database_backup_history` VALUES (64, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201153836.sql', '2023-12-01 15:38:37', '2023-12-01 15:38:37');
INSERT INTO `database_backup_history` VALUES (65, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201155422.sql', '2023-12-01 15:54:23', '2023-12-01 15:54:23');
INSERT INTO `database_backup_history` VALUES (66, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201161020.sql', '2023-12-01 16:10:21', '2023-12-01 16:10:20');
INSERT INTO `database_backup_history` VALUES (67, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201161353.sql', '2023-12-01 16:13:54', '2023-12-01 16:13:53');
INSERT INTO `database_backup_history` VALUES (68, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201162449.sql', '2023-12-01 16:24:50', '2023-12-01 16:24:49');
INSERT INTO `database_backup_history` VALUES (69, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201162610.sql', '2023-12-01 16:26:11', '2023-12-01 16:26:10');
INSERT INTO `database_backup_history` VALUES (70, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201163058.sql', '2023-12-01 16:30:59', '2023-12-01 16:30:59');
INSERT INTO `database_backup_history` VALUES (71, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201164602.sql', '2023-12-01 16:46:03', '2023-12-01 16:46:02');
INSERT INTO `database_backup_history` VALUES (72, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201165344.sql', '2023-12-01 16:53:45', '2023-12-01 16:53:45');
INSERT INTO `database_backup_history` VALUES (73, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201170012.sql', '2023-12-01 17:00:12', '2023-12-01 17:00:12');
INSERT INTO `database_backup_history` VALUES (74, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201171338.sql', '2023-12-01 17:13:39', '2023-12-01 17:13:39');
INSERT INTO `database_backup_history` VALUES (75, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201171604.sql', '2023-12-01 17:16:05', '2023-12-01 17:16:04');
INSERT INTO `database_backup_history` VALUES (76, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201172016.sql', '2023-12-01 17:20:17', '2023-12-01 17:20:17');
INSERT INTO `database_backup_history` VALUES (77, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231201172855.sql', '2023-12-01 17:28:56', '2023-12-01 17:28:55');
INSERT INTO `database_backup_history` VALUES (78, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231202095316.sql', '2023-12-02 09:53:17', '2023-12-02 09:53:16');
INSERT INTO `database_backup_history` VALUES (79, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231202102603.sql', '2023-12-02 10:26:04', '2023-12-02 10:26:04');
INSERT INTO `database_backup_history` VALUES (80, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231202102913.sql', '2023-12-02 10:29:14', '2023-12-02 10:29:14');
INSERT INTO `database_backup_history` VALUES (81, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231202104454.sql', '2023-12-02 10:44:55', '2023-12-02 10:44:54');
INSERT INTO `database_backup_history` VALUES (82, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231202110552.sql', '2023-12-02 11:05:53', '2023-12-02 11:05:53');
INSERT INTO `database_backup_history` VALUES (83, 'D:\\Develop\\Code\\Github\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231207221704.sql', '2023-12-07 22:17:05', '2023-12-07 22:17:05');
INSERT INTO `database_backup_history` VALUES (84, 'E:\\Workspace\\Code\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231211144044.sql', '2023-12-11 14:40:45', '2023-12-11 14:40:45');
INSERT INTO `database_backup_history` VALUES (85, 'E:\\Workspace\\Code\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231211144445.sql', '2023-12-11 14:44:46', '2023-12-11 14:44:46');
INSERT INTO `database_backup_history` VALUES (86, 'E:\\Workspace\\Code\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231212151028.sql', '2023-12-12 15:10:31', '2023-12-12 15:10:31');
INSERT INTO `database_backup_history` VALUES (87, 'E:\\Workspace\\Code\\devpl-backend\\devpl-backend\\db\\backup\\devpl-20231212203603.sql', '2023-12-12 20:36:05', '2023-12-12 20:36:04');

-- ----------------------------
-- Table structure for db_conn_info
-- ----------------------------
DROP TABLE IF EXISTS `db_conn_info`;
CREATE TABLE `db_conn_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `db_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `port` int NULL DEFAULT 3306 COMMENT '端口号',
  `driver_class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驱动类名',
  `db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `conn_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '连接名',
  `conn_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'URL',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `driver_props` json NULL COMMENT '驱动属性',
  `driver_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '驱动类型',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据源管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of db_conn_info
-- ----------------------------
INSERT INTO `db_conn_info` VALUES (2, 'MYSQL', '127.0.0.1', 3306, 'com.mysql.cj.jdbc.Driver', NULL, 'devpl', 'jdbc:mysql://127.0.0.1:3306/devpl?useUnicode=true&characterEncoding=UTF-8&useSSL=true&serverTimezone=GMT%2B8', 'root', 'ha1OPkEUX39v7wx2PCXJww==', NULL, 'MYSQL5', 0, '2023-09-22 15:52:53', '2023-11-16 21:44:09');
INSERT INTO `db_conn_info` VALUES (9, 'MySQL', '127.0.0.1', 3306, 'com.mysql.cj.jdbc.Driver', 'mysql_learn', 'mysql_learn', 'jdbc:mysql://127.0.0.1:3306/mysql_learn', 'root', 'ha1OPkEUX39v7wx2PCXJww==', NULL, 'MYSQL8', 0, '2023-10-28 22:46:29', '2023-10-28 22:46:29');
INSERT INTO `db_conn_info` VALUES (10, 'MySQL', '127.0.0.1', 3306, 'com.mysql.jdbc.Driver', '', 'dsd', 'jdbc:mysql://127.0.0.1:3306', 'admin', 'ant.design', NULL, 'ORACLE', 0, '2023-10-29 20:48:32', '2023-10-29 20:48:32');

-- ----------------------------
-- Table structure for field_group
-- ----------------------------
DROP TABLE IF EXISTS `field_group`;
CREATE TABLE `field_group`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段组信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of field_group
-- ----------------------------
INSERT INTO `field_group` VALUES (26, '组2', NULL);

-- ----------------------------
-- Table structure for field_info
-- ----------------------------
DROP TABLE IF EXISTS `field_info`;
CREATE TABLE `field_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `field_key` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段ID',
  `field_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名',
  `data_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据类型',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述信息',
  `field_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '默认值',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字段信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of field_info
-- ----------------------------
INSERT INTO `field_info` VALUES (19, 'id', 'id', 'Long', '主键ID\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (20, 'fieldKey', 'fieldKey', 'String', '字段Key\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (21, 'fieldName', 'fieldName', 'String', '字段名，中文名\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (22, 'dataType', 'dataType', 'String', '数据类型\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (23, 'description', 'description', 'String', '描述信息\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (24, 'defaultValue', 'defaultValue', 'String', '默认值\r', 'ssdd', NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (25, 'updateTime', 'updateTime', 'LocalDateTime', '更新时间\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (26, 'createTime', 'createTime', 'LocalDateTime', '创建时间\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (27, 'deleted', 'deleted', 'Boolean', '是否逻辑删除\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (28, 'type', 'type', 'String', '输入类型 JSON/SQL/\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (29, 'content', 'content', 'String', '待解析的文本\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (30, 'id', 'id', 'Long', 'id\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (31, 'columnType', 'columnType', 'String', '字段类型\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (32, 'attrType', 'attrType', 'String', '属性类型\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (33, 'packageName', 'packageName', 'String', '属性包名\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (34, 'mysqlSqlType', 'mysqlSqlType', 'String', 'MySQL数据类型\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (35, 'jsonType', 'jsonType', 'String', 'JSON数据类型\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (36, 'createTime', 'createTime', 'Date', '创建时间\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (37, 'id', 'id', 'Integer', '主键ID\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (38, 'templateId', 'templateId', 'Long', '模板ID\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (39, 'generationId', 'generationId', 'Long', '模板ID\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (40, 'varKey', 'varKey', 'String', '参数key, 一般为出现在模板中的变量名,单个模板内唯一\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (41, 'value', 'value', 'String', '参数值\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (42, 'dataTypeId', 'dataTypeId', 'Long', '数据类型\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (43, 'updateTime', 'updateTime', 'LocalDateTime', '更新时间\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (44, 'createTime', 'createTime', 'LocalDateTime', '创建时间\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (45, 'templateName', 'templateName', 'String', '模板名称：唯一\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (46, 'type', 'type', 'Integer', '模板类型: 1-文件模板 2-字符串模板\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (47, 'templatePath', 'templatePath', 'String', '模板存放路径，相对路径\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (48, 'content', 'content', 'String', '模板内容: 如果为空则读取templatePath指定路径的文件作为模板内容\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (49, 'provider', 'provider', 'String', '技术提供方，例如Apache Velocity, Apache FreeMarker\r\r@see io.devpl.backend.domain.TemplateProvider\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (50, 'remark', 'remark', 'String', '备注信息\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (51, 'internal', 'internal', 'Boolean', '是否内置模板\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (52, 'generatorPath', 'generatorPath', 'String', '生成代码的路径\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (53, 'id', 'id', 'Integer', '主键ID\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (54, 'templateId', 'templateId', 'Long', '模板ID\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (55, 'generationId', 'generationId', 'Long', '模板ID\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (56, 'varKey', 'varKey', 'String', '参数key, 一般为出现在模板中的变量名,单个模板内唯一\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (57, 'value', 'value', 'String', '参数值\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (58, 'dataTypeId', 'dataTypeId', 'Long', '数据类型\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (59, 'updateTime', 'updateTime', 'LocalDateTime', '更新时间\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (60, 'createTime', 'createTime', 'LocalDateTime', '创建时间\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (61, 'templateName', 'templateName', 'String', '模板名称：唯一\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (62, 'type', 'type', 'Integer', '模板类型: 1-文件模板 2-字符串模板\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (63, 'templatePath', 'templatePath', 'String', '模板存放路径，相对路径\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (64, 'content', 'content', 'String', '模板内容: 如果为空则读取templatePath指定路径的文件作为模板内容\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (65, 'provider', 'provider', 'String', '技术提供方，例如Apache Velocity, Apache FreeMarker\r\r@see io.devpl.backend.domain.TemplateProvider\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (67, 'internal', 'internal', 'Boolean', '是否内置模板\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (68, 'generatorPath', 'generatorPath', 'String', '生成代码的路径\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (69, 'name', 'name', 'String', '', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (70, 'label', 'label', 'String', '', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (71, 'key', 'key', 'Integer', '这里的ID无意义，只是作为一个唯一的序号\r前端树形表格组件使用\rvxe-table使用id字段，react使用key字段\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (72, 'parentId', 'parentId', 'Integer', '父节点ID\r前端树形表格组件使用\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (73, 'parentKey', 'parentKey', 'Integer', '父节点ID\r前端树形表格组件使用\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (74, 'leaf', 'leaf', 'boolean', '是否叶子结点\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (75, 'children', 'children', 'List<ParamNode>', '子节点\r', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (76, 'valueType', 'valueType', 'MapperStatementParamValueType', '', NULL, NULL, NULL, NULL);
INSERT INTO `field_info` VALUES (77, 'groupId', 'groupId', 'String', '类型分组ID\r', NULL, '2023-12-26 15:39:05', '2023-12-26 15:39:05', NULL);
INSERT INTO `field_info` VALUES (78, 'groupName', 'groupName', 'String', '类型分组名称\r', NULL, '2023-12-26 15:39:05', '2023-12-26 15:39:05', NULL);

-- ----------------------------
-- Table structure for gen_field_type
-- ----------------------------
DROP TABLE IF EXISTS `gen_field_type`;
CREATE TABLE `gen_field_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `column_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段类型',
  `attr_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性包名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `json_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'JSON数据类型',
  `mysql_sql_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'MySQL SQL数据类型',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `column_type`(`column_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字段类型管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_field_type
-- ----------------------------
INSERT INTO `gen_field_type` VALUES (1, 'datetime', 'Date', 'java.util.Date', '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (2, 'date', 'Date', 'java.util.Date', '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (3, 'tinyint', 'Integer', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (4, 'smallint', 'Integer', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (5, 'mediumint', 'Integer', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (6, 'int', 'Integer', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (7, 'integer', 'Integer', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (8, 'bigint', 'Long', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (9, 'float', 'Float', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (10, 'double', 'Double', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (11, 'decimal', 'BigDecimal', 'java.math.BigDecimal', '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (12, 'bit', 'Boolean', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (13, 'char', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (14, 'varchar', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (15, 'tinytext', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (16, 'text', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (17, 'mediumtext', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (18, 'longtext', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (19, 'timestamp', 'Date', 'java.util.Date', '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (20, 'NUMBER', 'Integer', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (21, 'BINARY_INTEGER', 'Integer', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (22, 'BINARY_FLOAT', 'Float', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (23, 'BINARY_DOUBLE', 'Double', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (24, 'VARCHAR2', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (25, 'NVARCHAR', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (26, 'NVARCHAR2', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (27, 'CLOB', 'String', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (28, 'int8', 'Long', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (29, 'int4', 'Integer', NULL, '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (30, 'int2', 'Integer', '11111', '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `gen_field_type` VALUES (31, 'numeric', 'BigDecimal', 'java.math.BigDecimal', '2023-06-30 10:25:16', NULL, NULL);

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类名',
  `table_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '说明',
  `author` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '作者',
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮箱',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目包名',
  `version` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目版本号',
  `generator_type` tinyint NULL DEFAULT NULL COMMENT '生成方式  0：zip压缩包   1：自定义目录',
  `backend_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后端生成路径',
  `frontend_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端生成路径',
  `module_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模块名',
  `function_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '功能名',
  `form_layout` tinyint NULL DEFAULT NULL COMMENT '表单布局  1：一列   2：两列',
  `datasource_id` bigint NULL DEFAULT NULL COMMENT '数据源ID',
  `baseclass_id` bigint NULL DEFAULT NULL COMMENT '基类ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `table_name`(`table_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table
-- ----------------------------
INSERT INTO `gen_table` VALUES (50, 'column_info', 'ColumnInfo', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', 'author', 'email', 'com.lancoo', 'sdwwe', 0, 'backend', 'frontend', 'devpl', 'info', 1, 2, NULL, '2023-12-06 16:47:33', '2023-12-06 16:47:33');
INSERT INTO `gen_table` VALUES (51, 'field_group', 'FieldGroup', '字段组信息表', 'author', 'email', 'com.lancoo', 'sdwwe', 1, 'backend', 'frontend', 'devpl', 'group', 1, -1, NULL, '2023-12-26 13:27:41', '2023-12-26 13:27:41');

-- ----------------------------
-- Table structure for gen_table_field
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_field`;
CREATE TABLE `gen_table_field`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_id` bigint NULL DEFAULT NULL COMMENT '表ID',
  `field_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段名称',
  `field_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段类型',
  `field_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段说明',
  `attr_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性名',
  `attr_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '属性包名',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `auto_fill` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '自动填充  DEFAULT、INSERT、UPDATE、INSERT_UPDATE',
  `primary_key` tinyint NULL DEFAULT NULL COMMENT '主键 0：否  1：是',
  `base_field` tinyint NULL DEFAULT NULL COMMENT '基类字段 0：否  1：是',
  `form_item` tinyint NULL DEFAULT NULL COMMENT '表单项 0：否  1：是',
  `form_required` tinyint NULL DEFAULT NULL COMMENT '表单必填 0：否  1：是',
  `form_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表单类型',
  `form_dict` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表单字典类型',
  `form_validator` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表单效验',
  `grid_item` tinyint NULL DEFAULT NULL COMMENT '列表项 0：否  1：是',
  `grid_sort` tinyint NULL DEFAULT NULL COMMENT '列表排序 0：否  1：是',
  `query_item` tinyint NULL DEFAULT NULL COMMENT '查询项 0：否  1：是',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '查询方式',
  `query_form_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '查询表单类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 832 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of gen_table_field
-- ----------------------------
INSERT INTO `gen_table_field` VALUES (803, 50, 'id', 'bigint', '自增主键', 'id', 'Long', NULL, 0, 'DEFAULT', 1, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (804, 50, 'table_id', 'bigint', '所属表的ID', 'tableId', 'Long', NULL, 1, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (805, 50, 'table_cat', 'varchar', 'table catalog (maybe null)', 'tableCat', 'String', NULL, 2, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (806, 50, 'table_schem', 'varchar', 'table schema (maybe null)', 'tableSchem', 'String', NULL, 3, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (807, 50, 'table_name', 'varchar', '表名称', 'tableName', 'String', NULL, 4, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (808, 50, 'column_name', 'varchar', '列名称', 'columnName', 'String', NULL, 5, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (809, 50, 'data_type', 'int', 'SQL type from java.sql.Type', 'dataType', 'Integer', NULL, 6, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (810, 50, 'type_name', 'varchar', '数据源独立的类型名称, for a UDT the type name is fully qualified', 'typeName', 'String', NULL, 7, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (811, 50, 'column_size', 'int', 'column size.有符号数长度会减少1，比如bigint(20)，此时columnSize=19', 'columnSize', 'Integer', NULL, 8, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (812, 50, 'buffer_length', 'int', 'not used.', 'bufferLength', 'Integer', NULL, 9, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (813, 50, 'decimal_digits', 'int', '小数位数', 'decimalDigits', 'Integer', NULL, 10, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (814, 50, 'num_prec_radix', 'int', 'NUM_PREC_RADIX int => Radix (typically either 10 or 2) (基数,即十进制或者二进制)', 'numPrecRadix', 'Integer', NULL, 11, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (815, 50, 'nullable', 'int', '是否允许NULL. 0 - Indicates that the column definitely allows NULL values. 1 - Indicates that the column definitely allows NULL values. 2 - Indicates that the nullability of columns is unknown.', 'nullable', 'Integer', NULL, 12, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (816, 50, 'remarks', 'varchar', '该列的描述信息，可为null', 'remarks', 'String', NULL, 13, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (817, 50, 'column_def', 'varchar', '该列的默认值, 如果值被单引号引起来，则表示该值是字符串(maybe null)', 'columnDef', 'String', NULL, 14, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (818, 50, 'sql_data_type', 'int', 'unused', 'sqlDataType', 'Integer', NULL, 15, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (819, 50, 'sql_datetime_sub', 'int', 'unused', 'sqlDatetimeSub', 'Integer', NULL, 16, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (820, 50, 'char_octet_length', 'int', '字符类型的最大字节数 CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column', 'charOctetLength', 'Integer', NULL, 17, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (821, 50, 'ordinal_position', 'int', '该列在表中的位置，开始为1', 'ordinalPosition', 'Integer', NULL, 18, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (822, 50, 'is_nullable', 'tinyint', 'ISO rules are used to determine the nullability for a column. YES --- if the column can include NULLs NO --- if the column cannot include NULLs empty string --- if the nullability for the column is unknown', 'isNullable', 'Integer', NULL, 19, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (823, 50, 'scope_catalog', 'varchar', 'catalog of table that is the scope of a reference attribute (null if DATA_TYPE is not REF)', 'scopeCatalog', 'String', NULL, 20, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (824, 50, 'scope_schema', 'varchar', 'schema of table that is the scope of a reference attribute (null if the DATA_TYPE is not REF)', 'scopeSchema', 'String', NULL, 21, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (825, 50, 'scope_table', 'varchar', 'table name that this the scope of a reference attribute (null if the DATA_TYPE is not REF)', 'scopeTable', 'String', NULL, 22, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (826, 50, 'source_data_type', 'varchar', 'source type of distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE is not DISTINCT or user-generated REF)', 'sourceDataType', 'String', NULL, 23, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (827, 50, 'is_autoincrement', 'tinyint', 'Indicates whether this column is auto incremented YES --- if the column is auto incremented NO --- if the column is not auto incremented empty string --- if it cannot be determined whether the column is auto incremented', 'isAutoincrement', 'Integer', NULL, 24, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (828, 50, 'is_generated', 'tinyint', 'Indicates whether this is a generated column YES --- if this a generated column NO --- if this not a generated column empty string --- if it cannot be determined whether this is a generated column', 'isGenerated', 'Integer', NULL, 25, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (829, 51, 'group_id', 'int', '组ID', 'groupId', 'Integer', NULL, 0, 'DEFAULT', 1, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (830, 51, 'group_name', 'varchar', '组名称', 'groupName', 'String', NULL, 1, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');
INSERT INTO `gen_table_field` VALUES (831, 51, 'type', 'varchar', '组类型', 'type', 'String', NULL, 2, 'DEFAULT', 0, 0, 1, 0, 'text', NULL, NULL, 1, 0, 0, '=', 'text');

-- ----------------------------
-- Table structure for generator_config
-- ----------------------------
DROP TABLE IF EXISTS `generator_config`;
CREATE TABLE `generator_config`  (
  `name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL,
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成配置信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of generator_config
-- ----------------------------
INSERT INTO `generator_config` VALUES ('蓝鸽', '{\"name\":\"蓝鸽\",\"projectRootFolder\":\"D:\\\\Temp\",\"parentPackage\":\"com.lancoo.campuspotrait\",\"entityPackageName\":\"entity\",\"entityPackageFolder\":\"src/main/java\",\"mapperPackageName\":\"mapper\",\"mapperFolder\":\"src/main/java\",\"mapperXmlPackage\":\"mapping\",\"mapperXmlFolder\":\"src/main/resources\",\"authors\":null,\"projectLayout\":null}');

-- ----------------------------
-- Table structure for group_field
-- ----------------------------
DROP TABLE IF EXISTS `group_field`;
CREATE TABLE `group_field`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` bigint NOT NULL COMMENT '组ID',
  `field_id` bigint NOT NULL COMMENT '字段ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段和组关联信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of group_field
-- ----------------------------
INSERT INTO `group_field` VALUES (1, 11, 23);
INSERT INTO `group_field` VALUES (2, 11, 24);
INSERT INTO `group_field` VALUES (3, 11, 25);

-- ----------------------------
-- Table structure for mock_constraint
-- ----------------------------
DROP TABLE IF EXISTS `mock_constraint`;
CREATE TABLE `mock_constraint`  (
  `id` bigint NULL DEFAULT NULL COMMENT '主键ID',
  `constraint_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '约束类型',
  `expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '约束表达式',
  `table` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `column` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `related_table` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `related_column` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据模拟约束' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of mock_constraint
-- ----------------------------

-- ----------------------------
-- Table structure for model_field
-- ----------------------------
DROP TABLE IF EXISTS `model_field`;
CREATE TABLE `model_field`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `model_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '模型ID',
  `field_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模型字段关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of model_field
-- ----------------------------
INSERT INTO `model_field` VALUES (21, 2, '16');
INSERT INTO `model_field` VALUES (22, 2, '17');
INSERT INTO `model_field` VALUES (23, 2, '18');
INSERT INTO `model_field` VALUES (24, 2, '19');
INSERT INTO `model_field` VALUES (25, 2, '20');
INSERT INTO `model_field` VALUES (26, 2, '21');
INSERT INTO `model_field` VALUES (27, 2, '22');
INSERT INTO `model_field` VALUES (28, 2, '23');
INSERT INTO `model_field` VALUES (29, 2, '24');
INSERT INTO `model_field` VALUES (30, 2, '25');
INSERT INTO `model_field` VALUES (31, 1, '78');
INSERT INTO `model_field` VALUES (32, 1, '77');
INSERT INTO `model_field` VALUES (33, 1, '19');
INSERT INTO `model_field` VALUES (34, 1, '20');
INSERT INTO `model_field` VALUES (35, 1, '21');
INSERT INTO `model_field` VALUES (36, 1, '22');
INSERT INTO `model_field` VALUES (37, 1, '23');
INSERT INTO `model_field` VALUES (38, 1, '24');
INSERT INTO `model_field` VALUES (39, 1, '25');
INSERT INTO `model_field` VALUES (40, 1, '26');

-- ----------------------------
-- Table structure for model_info
-- ----------------------------
DROP TABLE IF EXISTS `model_info`;
CREATE TABLE `model_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基类包名',
  `code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基类编码',
  `fields` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '基类字段，多个用英文逗号分隔',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '领域模型信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of model_info
-- ----------------------------
INSERT INTO `model_info` VALUES (1, 'net.maku.framework.mybatis.entity', 'BaseEntity', 'id,creator,create_time,updater,update_time,version,deleted', '使用该基类，则需要表里有这些字段', '2023-06-30 10:25:16', NULL, NULL);
INSERT INTO `model_info` VALUES (2, 'io.devpl.sld', 'Main', NULL, 'dfsf', '2023-11-30 21:07:06', NULL, 0);

-- ----------------------------
-- Table structure for project_info
-- ----------------------------
DROP TABLE IF EXISTS `project_info`;
CREATE TABLE `project_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目名，如果是模块则为模块名',
  `project_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目标识',
  `project_package` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目包名',
  `project_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '项目路径',
  `modify_project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变更项目名',
  `modify_project_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变更标识',
  `modify_project_package` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变更包名',
  `exclusions` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '排除文件',
  `modify_suffix` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变更文件',
  `modify_tmp_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '变更临时路径',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '项目状态',
  `version` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本',
  `backend_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '后端项目路径',
  `frontend_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '前端项目路径',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `build_tool` tinyint(1) NULL DEFAULT NULL COMMENT '构建工具 1-Maven，2-Gradle',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '项目信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of project_info
-- ----------------------------
INSERT INTO `project_info` VALUES (3, 'devpl', 'devpl', 'com.lancoo', 'D:/Temp', NULL, NULL, NULL, '.git,.idea,target,logs', 'java,xml,yml,factories,md,txt', NULL, NULL, 'sdwwe', 'backend', 'frontend', '2023-11-26 11:06:29', '2023-11-27 20:49:07', 1);

-- ----------------------------
-- Table structure for province_city_district
-- ----------------------------
DROP TABLE IF EXISTS `province_city_district`;
CREATE TABLE `province_city_district`  (
  `id` int NOT NULL COMMENT '地区代码',
  `pid` int NULL DEFAULT NULL COMMENT '当前地区的上一级地区代码',
  `name` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NULL DEFAULT NULL COMMENT '地区名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_general_ci COMMENT = '省市县数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of province_city_district
-- ----------------------------
INSERT INTO `province_city_district` VALUES (11, 0, '北京');
INSERT INTO `province_city_district` VALUES (12, 0, '天津');
INSERT INTO `province_city_district` VALUES (13, 0, '河北');
INSERT INTO `province_city_district` VALUES (14, 0, '山西');
INSERT INTO `province_city_district` VALUES (15, 0, '内蒙古');
INSERT INTO `province_city_district` VALUES (21, 0, '辽宁');
INSERT INTO `province_city_district` VALUES (22, 0, '吉林');
INSERT INTO `province_city_district` VALUES (23, 0, '黑龙江');
INSERT INTO `province_city_district` VALUES (31, 0, '上海');
INSERT INTO `province_city_district` VALUES (32, 0, '江苏');
INSERT INTO `province_city_district` VALUES (33, 0, '浙江');
INSERT INTO `province_city_district` VALUES (34, 0, '安徽');
INSERT INTO `province_city_district` VALUES (35, 0, '福建');
INSERT INTO `province_city_district` VALUES (36, 0, '江西');
INSERT INTO `province_city_district` VALUES (37, 0, '山东');
INSERT INTO `province_city_district` VALUES (41, 0, '河南');
INSERT INTO `province_city_district` VALUES (42, 0, '湖北');
INSERT INTO `province_city_district` VALUES (43, 0, '湖南');
INSERT INTO `province_city_district` VALUES (44, 0, '广东');
INSERT INTO `province_city_district` VALUES (45, 0, '广西');
INSERT INTO `province_city_district` VALUES (46, 0, '海南');
INSERT INTO `province_city_district` VALUES (50, 0, '重庆');
INSERT INTO `province_city_district` VALUES (51, 0, '四川');
INSERT INTO `province_city_district` VALUES (52, 0, '贵州');
INSERT INTO `province_city_district` VALUES (53, 0, '云南');
INSERT INTO `province_city_district` VALUES (54, 0, '西藏');
INSERT INTO `province_city_district` VALUES (61, 0, '陕西');
INSERT INTO `province_city_district` VALUES (62, 0, '甘肃');
INSERT INTO `province_city_district` VALUES (63, 0, '青海');
INSERT INTO `province_city_district` VALUES (64, 0, '宁夏');
INSERT INTO `province_city_district` VALUES (65, 0, '新疆');
INSERT INTO `province_city_district` VALUES (71, 0, '台湾');
INSERT INTO `province_city_district` VALUES (81, 0, '香港');
INSERT INTO `province_city_district` VALUES (91, 0, '澳门');
INSERT INTO `province_city_district` VALUES (1101, 11, '北京市辖');
INSERT INTO `province_city_district` VALUES (1102, 11, '北京县辖');
INSERT INTO `province_city_district` VALUES (1201, 12, '天津市辖');
INSERT INTO `province_city_district` VALUES (1202, 12, '天津县辖');
INSERT INTO `province_city_district` VALUES (1301, 13, '石家庄');
INSERT INTO `province_city_district` VALUES (1302, 13, '唐山');
INSERT INTO `province_city_district` VALUES (1303, 13, '秦皇岛');
INSERT INTO `province_city_district` VALUES (1304, 13, '邯郸');
INSERT INTO `province_city_district` VALUES (1305, 13, '邢台');
INSERT INTO `province_city_district` VALUES (1306, 13, '保定');
INSERT INTO `province_city_district` VALUES (1307, 13, '张家口');
INSERT INTO `province_city_district` VALUES (1308, 13, '承德');
INSERT INTO `province_city_district` VALUES (1309, 13, '沧州');
INSERT INTO `province_city_district` VALUES (1310, 13, '廊坊');
INSERT INTO `province_city_district` VALUES (1311, 13, '衡水');
INSERT INTO `province_city_district` VALUES (1401, 14, '太原');
INSERT INTO `province_city_district` VALUES (1402, 14, '大同');
INSERT INTO `province_city_district` VALUES (1403, 14, '阳泉');
INSERT INTO `province_city_district` VALUES (1404, 14, '长治');
INSERT INTO `province_city_district` VALUES (1405, 14, '晋城');
INSERT INTO `province_city_district` VALUES (1406, 14, '朔州');
INSERT INTO `province_city_district` VALUES (1407, 14, '晋中');
INSERT INTO `province_city_district` VALUES (1408, 14, '运城');
INSERT INTO `province_city_district` VALUES (1409, 14, '忻州');
INSERT INTO `province_city_district` VALUES (1410, 14, '临汾');
INSERT INTO `province_city_district` VALUES (1423, 14, '吕梁地区');
INSERT INTO `province_city_district` VALUES (1501, 15, '呼和浩特');
INSERT INTO `province_city_district` VALUES (1502, 15, '包头');
INSERT INTO `province_city_district` VALUES (1503, 15, '乌海');
INSERT INTO `province_city_district` VALUES (1504, 15, '赤峰');
INSERT INTO `province_city_district` VALUES (1505, 15, '通辽');
INSERT INTO `province_city_district` VALUES (1521, 15, '呼伦贝尔盟');
INSERT INTO `province_city_district` VALUES (1522, 15, '兴安盟');
INSERT INTO `province_city_district` VALUES (1525, 15, '锡林郭勒盟');
INSERT INTO `province_city_district` VALUES (1526, 15, '乌兰察布盟');
INSERT INTO `province_city_district` VALUES (1527, 15, '伊克昭盟');
INSERT INTO `province_city_district` VALUES (1528, 15, '巴彦淖尔盟');
INSERT INTO `province_city_district` VALUES (1529, 15, '阿拉善盟');
INSERT INTO `province_city_district` VALUES (2101, 21, '沈阳');
INSERT INTO `province_city_district` VALUES (2102, 21, '大连');
INSERT INTO `province_city_district` VALUES (2103, 21, '鞍山');
INSERT INTO `province_city_district` VALUES (2104, 21, '抚顺');
INSERT INTO `province_city_district` VALUES (2105, 21, '本溪');
INSERT INTO `province_city_district` VALUES (2106, 21, '丹东');
INSERT INTO `province_city_district` VALUES (2107, 21, '锦州');
INSERT INTO `province_city_district` VALUES (2108, 21, '营口');
INSERT INTO `province_city_district` VALUES (2109, 21, '阜新');
INSERT INTO `province_city_district` VALUES (2110, 21, '辽阳');
INSERT INTO `province_city_district` VALUES (2111, 21, '盘锦');
INSERT INTO `province_city_district` VALUES (2112, 21, '铁岭');
INSERT INTO `province_city_district` VALUES (2113, 21, '朝阳');
INSERT INTO `province_city_district` VALUES (2114, 21, '葫芦岛');
INSERT INTO `province_city_district` VALUES (2201, 22, '长春');
INSERT INTO `province_city_district` VALUES (2202, 22, '吉林');
INSERT INTO `province_city_district` VALUES (2203, 22, '四平');
INSERT INTO `province_city_district` VALUES (2204, 22, '辽源');
INSERT INTO `province_city_district` VALUES (2205, 22, '通化');
INSERT INTO `province_city_district` VALUES (2206, 22, '白山');
INSERT INTO `province_city_district` VALUES (2207, 22, '松原');
INSERT INTO `province_city_district` VALUES (2208, 22, '白城');
INSERT INTO `province_city_district` VALUES (2224, 22, '延边朝鲜族自治州');
INSERT INTO `province_city_district` VALUES (2301, 23, '哈尔滨');
INSERT INTO `province_city_district` VALUES (2302, 23, '齐齐哈尔');
INSERT INTO `province_city_district` VALUES (2303, 23, '鸡西');
INSERT INTO `province_city_district` VALUES (2304, 23, '鹤岗');
INSERT INTO `province_city_district` VALUES (2305, 23, '双鸭山');
INSERT INTO `province_city_district` VALUES (2306, 23, '大庆');
INSERT INTO `province_city_district` VALUES (2307, 23, '伊春');
INSERT INTO `province_city_district` VALUES (2308, 23, '佳木斯');
INSERT INTO `province_city_district` VALUES (2309, 23, '七台河');
INSERT INTO `province_city_district` VALUES (2310, 23, '牡丹江');
INSERT INTO `province_city_district` VALUES (2311, 23, '黑河');
INSERT INTO `province_city_district` VALUES (2312, 23, '绥化');
INSERT INTO `province_city_district` VALUES (2327, 23, '大兴安岭地区');
INSERT INTO `province_city_district` VALUES (3101, 31, '上海市辖');
INSERT INTO `province_city_district` VALUES (3102, 31, '上海县辖');
INSERT INTO `province_city_district` VALUES (3201, 32, '南京');
INSERT INTO `province_city_district` VALUES (3202, 32, '无锡');
INSERT INTO `province_city_district` VALUES (3203, 32, '徐州');
INSERT INTO `province_city_district` VALUES (3204, 32, '常州');
INSERT INTO `province_city_district` VALUES (3205, 32, '苏州');
INSERT INTO `province_city_district` VALUES (3206, 32, '南通');
INSERT INTO `province_city_district` VALUES (3207, 32, '连云港');
INSERT INTO `province_city_district` VALUES (3208, 32, '淮安');
INSERT INTO `province_city_district` VALUES (3209, 32, '盐城');
INSERT INTO `province_city_district` VALUES (3210, 32, '扬州');
INSERT INTO `province_city_district` VALUES (3211, 32, '镇江');
INSERT INTO `province_city_district` VALUES (3212, 32, '泰州');
INSERT INTO `province_city_district` VALUES (3213, 32, '宿迁');
INSERT INTO `province_city_district` VALUES (3301, 33, '杭州');
INSERT INTO `province_city_district` VALUES (3302, 33, '宁波');
INSERT INTO `province_city_district` VALUES (3303, 33, '温州');
INSERT INTO `province_city_district` VALUES (3304, 33, '嘉兴');
INSERT INTO `province_city_district` VALUES (3305, 33, '湖州');
INSERT INTO `province_city_district` VALUES (3306, 33, '绍兴');
INSERT INTO `province_city_district` VALUES (3307, 33, '金华');
INSERT INTO `province_city_district` VALUES (3308, 33, '衢州');
INSERT INTO `province_city_district` VALUES (3309, 33, '舟山');
INSERT INTO `province_city_district` VALUES (3310, 33, '台州');
INSERT INTO `province_city_district` VALUES (3311, 33, '丽水');
INSERT INTO `province_city_district` VALUES (3401, 34, '合肥');
INSERT INTO `province_city_district` VALUES (3402, 34, '芜湖');
INSERT INTO `province_city_district` VALUES (3403, 34, '蚌埠');
INSERT INTO `province_city_district` VALUES (3404, 34, '淮南');
INSERT INTO `province_city_district` VALUES (3405, 34, '马鞍山');
INSERT INTO `province_city_district` VALUES (3406, 34, '淮北');
INSERT INTO `province_city_district` VALUES (3407, 34, '铜陵');
INSERT INTO `province_city_district` VALUES (3408, 34, '安庆');
INSERT INTO `province_city_district` VALUES (3410, 34, '黄山');
INSERT INTO `province_city_district` VALUES (3411, 34, '滁州');
INSERT INTO `province_city_district` VALUES (3412, 34, '阜阳');
INSERT INTO `province_city_district` VALUES (3413, 34, '宿州');
INSERT INTO `province_city_district` VALUES (3414, 34, '巢湖');
INSERT INTO `province_city_district` VALUES (3415, 34, '六安');
INSERT INTO `province_city_district` VALUES (3416, 34, '亳州');
INSERT INTO `province_city_district` VALUES (3417, 34, '池州');
INSERT INTO `province_city_district` VALUES (3418, 34, '宣城');
INSERT INTO `province_city_district` VALUES (3501, 35, '福州');
INSERT INTO `province_city_district` VALUES (3502, 35, '厦门');
INSERT INTO `province_city_district` VALUES (3503, 35, '莆田');
INSERT INTO `province_city_district` VALUES (3504, 35, '三明');
INSERT INTO `province_city_district` VALUES (3505, 35, '泉州');
INSERT INTO `province_city_district` VALUES (3506, 35, '漳州');
INSERT INTO `province_city_district` VALUES (3507, 35, '南平');
INSERT INTO `province_city_district` VALUES (3508, 35, '龙岩');
INSERT INTO `province_city_district` VALUES (3509, 35, '宁德');
INSERT INTO `province_city_district` VALUES (3601, 36, '南昌');
INSERT INTO `province_city_district` VALUES (3602, 36, '景德镇');
INSERT INTO `province_city_district` VALUES (3603, 36, '萍乡');
INSERT INTO `province_city_district` VALUES (3604, 36, '九江');
INSERT INTO `province_city_district` VALUES (3605, 36, '新余');
INSERT INTO `province_city_district` VALUES (3606, 36, '鹰潭');
INSERT INTO `province_city_district` VALUES (3607, 36, '赣州');
INSERT INTO `province_city_district` VALUES (3608, 36, '吉安');
INSERT INTO `province_city_district` VALUES (3609, 36, '宜春');
INSERT INTO `province_city_district` VALUES (3610, 36, '抚州');
INSERT INTO `province_city_district` VALUES (3611, 36, '上饶');
INSERT INTO `province_city_district` VALUES (3701, 37, '济南');
INSERT INTO `province_city_district` VALUES (3702, 37, '青岛');
INSERT INTO `province_city_district` VALUES (3703, 37, '淄博');
INSERT INTO `province_city_district` VALUES (3704, 37, '枣庄');
INSERT INTO `province_city_district` VALUES (3705, 37, '东营');
INSERT INTO `province_city_district` VALUES (3706, 37, '烟台');
INSERT INTO `province_city_district` VALUES (3707, 37, '潍坊');
INSERT INTO `province_city_district` VALUES (3708, 37, '济宁');
INSERT INTO `province_city_district` VALUES (3709, 37, '泰安');
INSERT INTO `province_city_district` VALUES (3710, 37, '威海');
INSERT INTO `province_city_district` VALUES (3711, 37, '日照');
INSERT INTO `province_city_district` VALUES (3712, 37, '莱芜');
INSERT INTO `province_city_district` VALUES (3713, 37, '临沂');
INSERT INTO `province_city_district` VALUES (3714, 37, '德州');
INSERT INTO `province_city_district` VALUES (3715, 37, '聊城');
INSERT INTO `province_city_district` VALUES (3716, 37, '滨州');
INSERT INTO `province_city_district` VALUES (3717, 37, '菏泽');
INSERT INTO `province_city_district` VALUES (4101, 41, '郑州');
INSERT INTO `province_city_district` VALUES (4102, 41, '开封');
INSERT INTO `province_city_district` VALUES (4103, 41, '洛阳');
INSERT INTO `province_city_district` VALUES (4104, 41, '平顶山');
INSERT INTO `province_city_district` VALUES (4105, 41, '安阳');
INSERT INTO `province_city_district` VALUES (4106, 41, '鹤壁');
INSERT INTO `province_city_district` VALUES (4107, 41, '新乡');
INSERT INTO `province_city_district` VALUES (4108, 41, '焦作');
INSERT INTO `province_city_district` VALUES (4109, 41, '濮阳');
INSERT INTO `province_city_district` VALUES (4110, 41, '许昌');
INSERT INTO `province_city_district` VALUES (4111, 41, '漯河');
INSERT INTO `province_city_district` VALUES (4112, 41, '三门峡');
INSERT INTO `province_city_district` VALUES (4113, 41, '南阳');
INSERT INTO `province_city_district` VALUES (4114, 41, '商丘');
INSERT INTO `province_city_district` VALUES (4115, 41, '信阳');
INSERT INTO `province_city_district` VALUES (4116, 41, '周口');
INSERT INTO `province_city_district` VALUES (4117, 41, '驻马店');
INSERT INTO `province_city_district` VALUES (4201, 42, '武汉');
INSERT INTO `province_city_district` VALUES (4202, 42, '黄石');
INSERT INTO `province_city_district` VALUES (4203, 42, '十堰');
INSERT INTO `province_city_district` VALUES (4205, 42, '宜昌');
INSERT INTO `province_city_district` VALUES (4206, 42, '襄樊');
INSERT INTO `province_city_district` VALUES (4207, 42, '鄂州');
INSERT INTO `province_city_district` VALUES (4208, 42, '荆门');
INSERT INTO `province_city_district` VALUES (4209, 42, '孝感');
INSERT INTO `province_city_district` VALUES (4210, 42, '荆州');
INSERT INTO `province_city_district` VALUES (4211, 42, '黄冈');
INSERT INTO `province_city_district` VALUES (4212, 42, '咸宁');
INSERT INTO `province_city_district` VALUES (4213, 42, '随州');
INSERT INTO `province_city_district` VALUES (4228, 42, '恩施土家族苗族自治州');
INSERT INTO `province_city_district` VALUES (4290, 42, '省直辖行政单位');
INSERT INTO `province_city_district` VALUES (4301, 43, '长沙');
INSERT INTO `province_city_district` VALUES (4302, 43, '株洲');
INSERT INTO `province_city_district` VALUES (4303, 43, '湘潭');
INSERT INTO `province_city_district` VALUES (4304, 43, '衡阳');
INSERT INTO `province_city_district` VALUES (4305, 43, '邵阳');
INSERT INTO `province_city_district` VALUES (4306, 43, '岳阳');
INSERT INTO `province_city_district` VALUES (4307, 43, '常德');
INSERT INTO `province_city_district` VALUES (4308, 43, '张家界');
INSERT INTO `province_city_district` VALUES (4309, 43, '益阳');
INSERT INTO `province_city_district` VALUES (4310, 43, '郴州');
INSERT INTO `province_city_district` VALUES (4311, 43, '永州');
INSERT INTO `province_city_district` VALUES (4312, 43, '怀化');
INSERT INTO `province_city_district` VALUES (4313, 43, '娄底');
INSERT INTO `province_city_district` VALUES (4331, 43, '湘西土家族苗族自治州');
INSERT INTO `province_city_district` VALUES (4401, 44, '广州');
INSERT INTO `province_city_district` VALUES (4402, 44, '韶关');
INSERT INTO `province_city_district` VALUES (4403, 44, '深圳');
INSERT INTO `province_city_district` VALUES (4404, 44, '珠海');
INSERT INTO `province_city_district` VALUES (4405, 44, '汕头');
INSERT INTO `province_city_district` VALUES (4406, 44, '佛山');
INSERT INTO `province_city_district` VALUES (4407, 44, '江门');
INSERT INTO `province_city_district` VALUES (4408, 44, '湛江');
INSERT INTO `province_city_district` VALUES (4409, 44, '茂名');
INSERT INTO `province_city_district` VALUES (4412, 44, '肇庆');
INSERT INTO `province_city_district` VALUES (4413, 44, '惠州');
INSERT INTO `province_city_district` VALUES (4414, 44, '梅州');
INSERT INTO `province_city_district` VALUES (4415, 44, '汕尾');
INSERT INTO `province_city_district` VALUES (4416, 44, '河源');
INSERT INTO `province_city_district` VALUES (4417, 44, '阳江');
INSERT INTO `province_city_district` VALUES (4418, 44, '清远');
INSERT INTO `province_city_district` VALUES (4419, 44, '东莞');
INSERT INTO `province_city_district` VALUES (4420, 44, '中山');
INSERT INTO `province_city_district` VALUES (4451, 44, '潮州');
INSERT INTO `province_city_district` VALUES (4452, 44, '揭阳');
INSERT INTO `province_city_district` VALUES (4453, 44, '云浮');
INSERT INTO `province_city_district` VALUES (4501, 45, '南宁');
INSERT INTO `province_city_district` VALUES (4502, 45, '柳州');
INSERT INTO `province_city_district` VALUES (4503, 45, '桂林');
INSERT INTO `province_city_district` VALUES (4504, 45, '梧州');
INSERT INTO `province_city_district` VALUES (4505, 45, '北海');
INSERT INTO `province_city_district` VALUES (4506, 45, '防城港');
INSERT INTO `province_city_district` VALUES (4507, 45, '钦州');
INSERT INTO `province_city_district` VALUES (4508, 45, '贵港');
INSERT INTO `province_city_district` VALUES (4509, 45, '玉林');
INSERT INTO `province_city_district` VALUES (4521, 45, '南宁地区');
INSERT INTO `province_city_district` VALUES (4522, 45, '柳州地区');
INSERT INTO `province_city_district` VALUES (4524, 45, '贺州地区');
INSERT INTO `province_city_district` VALUES (4526, 45, '百色地区');
INSERT INTO `province_city_district` VALUES (4527, 45, '河池地区');
INSERT INTO `province_city_district` VALUES (4601, 46, '海南');
INSERT INTO `province_city_district` VALUES (4602, 46, '海口');
INSERT INTO `province_city_district` VALUES (4603, 46, '三亚');
INSERT INTO `province_city_district` VALUES (5001, 50, '重庆市辖');
INSERT INTO `province_city_district` VALUES (5002, 50, '重庆县辖');
INSERT INTO `province_city_district` VALUES (5003, 50, '重庆县级');
INSERT INTO `province_city_district` VALUES (5101, 51, '成都');
INSERT INTO `province_city_district` VALUES (5103, 51, '自贡');
INSERT INTO `province_city_district` VALUES (5104, 51, '攀枝花');
INSERT INTO `province_city_district` VALUES (5105, 51, '泸州');
INSERT INTO `province_city_district` VALUES (5106, 51, '德阳');
INSERT INTO `province_city_district` VALUES (5107, 51, '绵阳');
INSERT INTO `province_city_district` VALUES (5108, 51, '广元');
INSERT INTO `province_city_district` VALUES (5109, 51, '遂宁');
INSERT INTO `province_city_district` VALUES (5110, 51, '内江');
INSERT INTO `province_city_district` VALUES (5111, 51, '乐山');
INSERT INTO `province_city_district` VALUES (5113, 51, '南充');
INSERT INTO `province_city_district` VALUES (5114, 51, '眉山');
INSERT INTO `province_city_district` VALUES (5115, 51, '宜宾');
INSERT INTO `province_city_district` VALUES (5116, 51, '广安');
INSERT INTO `province_city_district` VALUES (5117, 51, '达州');
INSERT INTO `province_city_district` VALUES (5118, 51, '雅安');
INSERT INTO `province_city_district` VALUES (5119, 51, '巴中');
INSERT INTO `province_city_district` VALUES (5120, 51, '资阳');
INSERT INTO `province_city_district` VALUES (5132, 51, '阿坝藏族羌族自治州');
INSERT INTO `province_city_district` VALUES (5133, 51, '甘孜藏族自治州');
INSERT INTO `province_city_district` VALUES (5134, 51, '凉山彝族自治州');
INSERT INTO `province_city_district` VALUES (5201, 52, '贵阳');
INSERT INTO `province_city_district` VALUES (5202, 52, '六盘水');
INSERT INTO `province_city_district` VALUES (5203, 52, '遵义');
INSERT INTO `province_city_district` VALUES (5204, 52, '安顺');
INSERT INTO `province_city_district` VALUES (5222, 52, '铜仁地区');
INSERT INTO `province_city_district` VALUES (5223, 52, '黔西南布依族苗族自治');
INSERT INTO `province_city_district` VALUES (5224, 52, '毕节地区');
INSERT INTO `province_city_district` VALUES (5226, 52, '黔东南苗族侗族自治州');
INSERT INTO `province_city_district` VALUES (5227, 52, '黔南布依族苗族自治州');
INSERT INTO `province_city_district` VALUES (5301, 53, '昆明');
INSERT INTO `province_city_district` VALUES (5303, 53, '曲靖');
INSERT INTO `province_city_district` VALUES (5304, 53, '玉溪');
INSERT INTO `province_city_district` VALUES (5305, 53, '保山');
INSERT INTO `province_city_district` VALUES (5321, 53, '昭通地区');
INSERT INTO `province_city_district` VALUES (5323, 53, '楚雄彝族自治州');
INSERT INTO `province_city_district` VALUES (5325, 53, '红河哈尼族彝族自治州');
INSERT INTO `province_city_district` VALUES (5326, 53, '文山壮族苗族自治州');
INSERT INTO `province_city_district` VALUES (5327, 53, '思茅地区');
INSERT INTO `province_city_district` VALUES (5328, 53, '西双版纳傣族自治州');
INSERT INTO `province_city_district` VALUES (5329, 53, '大理白族自治州');
INSERT INTO `province_city_district` VALUES (5331, 53, '德宏傣族景颇族自治州');
INSERT INTO `province_city_district` VALUES (5332, 53, '丽江地区');
INSERT INTO `province_city_district` VALUES (5333, 53, '怒江傈僳族自治州');
INSERT INTO `province_city_district` VALUES (5334, 53, '迪庆藏族自治州');
INSERT INTO `province_city_district` VALUES (5335, 53, '临沧地区');
INSERT INTO `province_city_district` VALUES (5401, 54, '拉萨');
INSERT INTO `province_city_district` VALUES (5421, 54, '昌都地区');
INSERT INTO `province_city_district` VALUES (5422, 54, '山南地区');
INSERT INTO `province_city_district` VALUES (5423, 54, '日喀则地区');
INSERT INTO `province_city_district` VALUES (5424, 54, '那曲地区');
INSERT INTO `province_city_district` VALUES (5425, 54, '阿里地区');
INSERT INTO `province_city_district` VALUES (5426, 54, '林芝地区');
INSERT INTO `province_city_district` VALUES (6101, 61, '西安');
INSERT INTO `province_city_district` VALUES (6102, 61, '铜川');
INSERT INTO `province_city_district` VALUES (6103, 61, '宝鸡');
INSERT INTO `province_city_district` VALUES (6104, 61, '咸阳');
INSERT INTO `province_city_district` VALUES (6105, 61, '渭南');
INSERT INTO `province_city_district` VALUES (6106, 61, '延安');
INSERT INTO `province_city_district` VALUES (6107, 61, '汉中');
INSERT INTO `province_city_district` VALUES (6108, 61, '榆林');
INSERT INTO `province_city_district` VALUES (6109, 61, '安康');
INSERT INTO `province_city_district` VALUES (6125, 61, '商洛地区');
INSERT INTO `province_city_district` VALUES (6201, 62, '兰州');
INSERT INTO `province_city_district` VALUES (6202, 62, '嘉峪关');
INSERT INTO `province_city_district` VALUES (6203, 62, '金昌');
INSERT INTO `province_city_district` VALUES (6204, 62, '白银');
INSERT INTO `province_city_district` VALUES (6205, 62, '天水');
INSERT INTO `province_city_district` VALUES (6221, 62, '酒泉地区');
INSERT INTO `province_city_district` VALUES (6222, 62, '张掖地区');
INSERT INTO `province_city_district` VALUES (6223, 62, '武威地区');
INSERT INTO `province_city_district` VALUES (6224, 62, '定西地区');
INSERT INTO `province_city_district` VALUES (6226, 62, '陇南地区');
INSERT INTO `province_city_district` VALUES (6227, 62, '平凉地区');
INSERT INTO `province_city_district` VALUES (6228, 62, '庆阳地区');
INSERT INTO `province_city_district` VALUES (6229, 62, '临夏回族自治州');
INSERT INTO `province_city_district` VALUES (6230, 62, '甘南藏族自治州');
INSERT INTO `province_city_district` VALUES (6301, 63, '西宁');
INSERT INTO `province_city_district` VALUES (6321, 63, '海东地区');
INSERT INTO `province_city_district` VALUES (6322, 63, '海北藏族自治州');
INSERT INTO `province_city_district` VALUES (6323, 63, '黄南藏族自治州');
INSERT INTO `province_city_district` VALUES (6325, 63, '海南藏族自治州');
INSERT INTO `province_city_district` VALUES (6326, 63, '果洛藏族自治州');
INSERT INTO `province_city_district` VALUES (6327, 63, '玉树藏族自治州');
INSERT INTO `province_city_district` VALUES (6328, 63, '海西蒙古族藏族自治州');
INSERT INTO `province_city_district` VALUES (6401, 64, '银川');
INSERT INTO `province_city_district` VALUES (6402, 64, '石嘴山');
INSERT INTO `province_city_district` VALUES (6403, 64, '吴忠');
INSERT INTO `province_city_district` VALUES (6422, 64, '固原地区');
INSERT INTO `province_city_district` VALUES (6501, 65, '乌鲁木齐');
INSERT INTO `province_city_district` VALUES (6502, 65, '克拉玛依');
INSERT INTO `province_city_district` VALUES (6521, 65, '吐鲁番地区');
INSERT INTO `province_city_district` VALUES (6522, 65, '哈密地区');
INSERT INTO `province_city_district` VALUES (6523, 65, '昌吉回族自治州');
INSERT INTO `province_city_district` VALUES (6527, 65, '博尔塔拉蒙古自治州');
INSERT INTO `province_city_district` VALUES (6528, 65, '巴音郭楞蒙古自治州');
INSERT INTO `province_city_district` VALUES (6529, 65, '阿克苏地区');
INSERT INTO `province_city_district` VALUES (6530, 65, '克孜勒苏柯尔克孜自治');
INSERT INTO `province_city_district` VALUES (6531, 65, '喀什地区');
INSERT INTO `province_city_district` VALUES (6532, 65, '和田地区');
INSERT INTO `province_city_district` VALUES (6540, 65, '伊犁哈萨克自治州');
INSERT INTO `province_city_district` VALUES (6541, 65, '伊犁地区');
INSERT INTO `province_city_district` VALUES (6542, 65, '塔城地区');
INSERT INTO `province_city_district` VALUES (6543, 65, '阿勒泰地区');
INSERT INTO `province_city_district` VALUES (6590, 65, '省直辖行政单位');
INSERT INTO `province_city_district` VALUES (7101, 71, '台湾市辖');
INSERT INTO `province_city_district` VALUES (8101, 81, '香港特区');
INSERT INTO `province_city_district` VALUES (9101, 91, '澳门特区');
INSERT INTO `province_city_district` VALUES (110101, 1101, '东城区');
INSERT INTO `province_city_district` VALUES (110102, 1101, '西城区');
INSERT INTO `province_city_district` VALUES (110103, 1101, '崇文区');
INSERT INTO `province_city_district` VALUES (110104, 1101, '宣武区');
INSERT INTO `province_city_district` VALUES (110105, 1101, '朝阳区');
INSERT INTO `province_city_district` VALUES (110106, 1101, '丰台区');
INSERT INTO `province_city_district` VALUES (110107, 1101, '石景山区');
INSERT INTO `province_city_district` VALUES (110108, 1101, '海淀区');
INSERT INTO `province_city_district` VALUES (110109, 1101, '门头沟区');
INSERT INTO `province_city_district` VALUES (110111, 1101, '房山区');
INSERT INTO `province_city_district` VALUES (110112, 1101, '通州区');
INSERT INTO `province_city_district` VALUES (110113, 1101, '顺义区');
INSERT INTO `province_city_district` VALUES (110114, 1101, '昌平区');
INSERT INTO `province_city_district` VALUES (110224, 1102, '大兴县');
INSERT INTO `province_city_district` VALUES (110226, 1102, '平谷县');
INSERT INTO `province_city_district` VALUES (110227, 1102, '怀柔县');
INSERT INTO `province_city_district` VALUES (110228, 1102, '密云县');
INSERT INTO `province_city_district` VALUES (110229, 1102, '延庆县');
INSERT INTO `province_city_district` VALUES (120101, 1201, '和平区');
INSERT INTO `province_city_district` VALUES (120102, 1201, '河东区');
INSERT INTO `province_city_district` VALUES (120103, 1201, '河西区');
INSERT INTO `province_city_district` VALUES (120104, 1201, '南开区');
INSERT INTO `province_city_district` VALUES (120105, 1201, '河北区');
INSERT INTO `province_city_district` VALUES (120106, 1201, '红桥区');
INSERT INTO `province_city_district` VALUES (120107, 1201, '塘沽区');
INSERT INTO `province_city_district` VALUES (120108, 1201, '汉沽区');
INSERT INTO `province_city_district` VALUES (120109, 1201, '大港区');
INSERT INTO `province_city_district` VALUES (120110, 1201, '东丽区');
INSERT INTO `province_city_district` VALUES (120111, 1201, '西青区');
INSERT INTO `province_city_district` VALUES (120112, 1201, '津南区');
INSERT INTO `province_city_district` VALUES (120113, 1201, '北辰区');
INSERT INTO `province_city_district` VALUES (120114, 1201, '武清区');
INSERT INTO `province_city_district` VALUES (120221, 1202, '宁河县');
INSERT INTO `province_city_district` VALUES (120223, 1202, '静海县');
INSERT INTO `province_city_district` VALUES (120224, 1202, '宝坻县');
INSERT INTO `province_city_district` VALUES (120225, 1202, '蓟  县');
INSERT INTO `province_city_district` VALUES (130101, 1301, '市辖区');
INSERT INTO `province_city_district` VALUES (130102, 1301, '长安区');
INSERT INTO `province_city_district` VALUES (130103, 1301, '桥东区');
INSERT INTO `province_city_district` VALUES (130104, 1301, '桥西区');
INSERT INTO `province_city_district` VALUES (130105, 1301, '新华区');
INSERT INTO `province_city_district` VALUES (130106, 1301, '郊  区');
INSERT INTO `province_city_district` VALUES (130107, 1301, '井陉矿区');
INSERT INTO `province_city_district` VALUES (130121, 1301, '井陉县');
INSERT INTO `province_city_district` VALUES (130123, 1301, '正定县');
INSERT INTO `province_city_district` VALUES (130124, 1301, '栾城县');
INSERT INTO `province_city_district` VALUES (130125, 1301, '行唐县');
INSERT INTO `province_city_district` VALUES (130126, 1301, '灵寿县');
INSERT INTO `province_city_district` VALUES (130127, 1301, '高邑县');
INSERT INTO `province_city_district` VALUES (130128, 1301, '深泽县');
INSERT INTO `province_city_district` VALUES (130129, 1301, '赞皇县');
INSERT INTO `province_city_district` VALUES (130130, 1301, '无极县');
INSERT INTO `province_city_district` VALUES (130131, 1301, '平山县');
INSERT INTO `province_city_district` VALUES (130132, 1301, '元氏县');
INSERT INTO `province_city_district` VALUES (130133, 1301, '赵  县');
INSERT INTO `province_city_district` VALUES (130181, 1301, '辛集市');
INSERT INTO `province_city_district` VALUES (130182, 1301, '藁城市');
INSERT INTO `province_city_district` VALUES (130183, 1301, '晋州市');
INSERT INTO `province_city_district` VALUES (130184, 1301, '新乐市');
INSERT INTO `province_city_district` VALUES (130185, 1301, '鹿泉市');
INSERT INTO `province_city_district` VALUES (130201, 1302, '市辖区');
INSERT INTO `province_city_district` VALUES (130202, 1302, '路南区');
INSERT INTO `province_city_district` VALUES (130203, 1302, '路北区');
INSERT INTO `province_city_district` VALUES (130204, 1302, '古冶区');
INSERT INTO `province_city_district` VALUES (130205, 1302, '开平区');
INSERT INTO `province_city_district` VALUES (130206, 1302, '新  区');
INSERT INTO `province_city_district` VALUES (130221, 1302, '丰润县');
INSERT INTO `province_city_district` VALUES (130223, 1302, '滦  县');
INSERT INTO `province_city_district` VALUES (130224, 1302, '滦南县');
INSERT INTO `province_city_district` VALUES (130225, 1302, '乐亭县');
INSERT INTO `province_city_district` VALUES (130227, 1302, '迁西县');
INSERT INTO `province_city_district` VALUES (130229, 1302, '玉田县');
INSERT INTO `province_city_district` VALUES (130230, 1302, '唐海县');
INSERT INTO `province_city_district` VALUES (130281, 1302, '遵化市');
INSERT INTO `province_city_district` VALUES (130282, 1302, '丰南市');
INSERT INTO `province_city_district` VALUES (130283, 1302, '迁安市');
INSERT INTO `province_city_district` VALUES (130301, 1303, '市辖区');
INSERT INTO `province_city_district` VALUES (130302, 1303, '海港区');
INSERT INTO `province_city_district` VALUES (130303, 1303, '山海关区');
INSERT INTO `province_city_district` VALUES (130304, 1303, '北戴河区');
INSERT INTO `province_city_district` VALUES (130321, 1303, '青龙满族自治县');
INSERT INTO `province_city_district` VALUES (130322, 1303, '昌黎县');
INSERT INTO `province_city_district` VALUES (130323, 1303, '抚宁县');
INSERT INTO `province_city_district` VALUES (130324, 1303, '卢龙县');
INSERT INTO `province_city_district` VALUES (130401, 1304, '市辖区');
INSERT INTO `province_city_district` VALUES (130402, 1304, '邯山区');
INSERT INTO `province_city_district` VALUES (130403, 1304, '丛台区');
INSERT INTO `province_city_district` VALUES (130404, 1304, '复兴区');
INSERT INTO `province_city_district` VALUES (130406, 1304, '峰峰矿区');
INSERT INTO `province_city_district` VALUES (130421, 1304, '邯郸县');
INSERT INTO `province_city_district` VALUES (130423, 1304, '临漳县');
INSERT INTO `province_city_district` VALUES (130424, 1304, '成安县');
INSERT INTO `province_city_district` VALUES (130425, 1304, '大名县');
INSERT INTO `province_city_district` VALUES (130426, 1304, '涉  县');
INSERT INTO `province_city_district` VALUES (130427, 1304, '磁  县');
INSERT INTO `province_city_district` VALUES (130428, 1304, '肥乡县');
INSERT INTO `province_city_district` VALUES (130429, 1304, '永年县');
INSERT INTO `province_city_district` VALUES (130430, 1304, '邱  县');
INSERT INTO `province_city_district` VALUES (130431, 1304, '鸡泽县');
INSERT INTO `province_city_district` VALUES (130432, 1304, '广平县');
INSERT INTO `province_city_district` VALUES (130433, 1304, '馆陶县');
INSERT INTO `province_city_district` VALUES (130434, 1304, '魏  县');
INSERT INTO `province_city_district` VALUES (130435, 1304, '曲周县');
INSERT INTO `province_city_district` VALUES (130481, 1304, '武安市');
INSERT INTO `province_city_district` VALUES (130501, 1305, '市辖区');
INSERT INTO `province_city_district` VALUES (130502, 1305, '桥东区');
INSERT INTO `province_city_district` VALUES (130503, 1305, '桥西区');
INSERT INTO `province_city_district` VALUES (130521, 1305, '邢台县');
INSERT INTO `province_city_district` VALUES (130522, 1305, '临城县');
INSERT INTO `province_city_district` VALUES (130523, 1305, '内丘县');
INSERT INTO `province_city_district` VALUES (130524, 1305, '柏乡县');
INSERT INTO `province_city_district` VALUES (130525, 1305, '隆尧县');
INSERT INTO `province_city_district` VALUES (130526, 1305, '任  县');
INSERT INTO `province_city_district` VALUES (130527, 1305, '南和县');
INSERT INTO `province_city_district` VALUES (130528, 1305, '宁晋县');
INSERT INTO `province_city_district` VALUES (130529, 1305, '巨鹿县');
INSERT INTO `province_city_district` VALUES (130530, 1305, '新河县');
INSERT INTO `province_city_district` VALUES (130531, 1305, '广宗县');
INSERT INTO `province_city_district` VALUES (130532, 1305, '平乡县');
INSERT INTO `province_city_district` VALUES (130533, 1305, '威  县');
INSERT INTO `province_city_district` VALUES (130534, 1305, '清河县');
INSERT INTO `province_city_district` VALUES (130535, 1305, '临西县');
INSERT INTO `province_city_district` VALUES (130581, 1305, '南宫市');
INSERT INTO `province_city_district` VALUES (130582, 1305, '沙河市');
INSERT INTO `province_city_district` VALUES (130601, 1306, '市辖区');
INSERT INTO `province_city_district` VALUES (130602, 1306, '新市区');
INSERT INTO `province_city_district` VALUES (130603, 1306, '北市区');
INSERT INTO `province_city_district` VALUES (130604, 1306, '南市区');
INSERT INTO `province_city_district` VALUES (130621, 1306, '满城县');
INSERT INTO `province_city_district` VALUES (130622, 1306, '清苑县');
INSERT INTO `province_city_district` VALUES (130623, 1306, '涞水县');
INSERT INTO `province_city_district` VALUES (130624, 1306, '阜平县');
INSERT INTO `province_city_district` VALUES (130625, 1306, '徐水县');
INSERT INTO `province_city_district` VALUES (130626, 1306, '定兴县');
INSERT INTO `province_city_district` VALUES (130627, 1306, '唐  县');
INSERT INTO `province_city_district` VALUES (130628, 1306, '高阳县');
INSERT INTO `province_city_district` VALUES (130629, 1306, '容城县');
INSERT INTO `province_city_district` VALUES (130630, 1306, '涞源县');
INSERT INTO `province_city_district` VALUES (130631, 1306, '望都县');
INSERT INTO `province_city_district` VALUES (130632, 1306, '安新县');
INSERT INTO `province_city_district` VALUES (130633, 1306, '易  县');
INSERT INTO `province_city_district` VALUES (130634, 1306, '曲阳县');
INSERT INTO `province_city_district` VALUES (130635, 1306, '蠡  县');
INSERT INTO `province_city_district` VALUES (130636, 1306, '顺平县');
INSERT INTO `province_city_district` VALUES (130637, 1306, '博野县');
INSERT INTO `province_city_district` VALUES (130638, 1306, '雄  县');
INSERT INTO `province_city_district` VALUES (130681, 1306, '涿州市');
INSERT INTO `province_city_district` VALUES (130682, 1306, '定州市');
INSERT INTO `province_city_district` VALUES (130683, 1306, '安国市');
INSERT INTO `province_city_district` VALUES (130684, 1306, '高碑店市');
INSERT INTO `province_city_district` VALUES (130701, 1307, '市辖区');
INSERT INTO `province_city_district` VALUES (130702, 1307, '桥东区');
INSERT INTO `province_city_district` VALUES (130703, 1307, '桥西区');
INSERT INTO `province_city_district` VALUES (130705, 1307, '宣化区');
INSERT INTO `province_city_district` VALUES (130706, 1307, '下花园区');
INSERT INTO `province_city_district` VALUES (130721, 1307, '宣化县');
INSERT INTO `province_city_district` VALUES (130722, 1307, '张北县');
INSERT INTO `province_city_district` VALUES (130723, 1307, '康保县');
INSERT INTO `province_city_district` VALUES (130724, 1307, '沽源县');
INSERT INTO `province_city_district` VALUES (130725, 1307, '尚义县');
INSERT INTO `province_city_district` VALUES (130726, 1307, '蔚  县');
INSERT INTO `province_city_district` VALUES (130727, 1307, '阳原县');
INSERT INTO `province_city_district` VALUES (130728, 1307, '怀安县');
INSERT INTO `province_city_district` VALUES (130729, 1307, '万全县');
INSERT INTO `province_city_district` VALUES (130730, 1307, '怀来县');
INSERT INTO `province_city_district` VALUES (130731, 1307, '涿鹿县');
INSERT INTO `province_city_district` VALUES (130732, 1307, '赤城县');
INSERT INTO `province_city_district` VALUES (130733, 1307, '崇礼县');
INSERT INTO `province_city_district` VALUES (130801, 1308, '市辖区');
INSERT INTO `province_city_district` VALUES (130802, 1308, '双桥区');
INSERT INTO `province_city_district` VALUES (130803, 1308, '双滦区');
INSERT INTO `province_city_district` VALUES (130804, 1308, '鹰手营子矿区');
INSERT INTO `province_city_district` VALUES (130821, 1308, '承德县');
INSERT INTO `province_city_district` VALUES (130822, 1308, '兴隆县');
INSERT INTO `province_city_district` VALUES (130823, 1308, '平泉县');
INSERT INTO `province_city_district` VALUES (130824, 1308, '滦平县');
INSERT INTO `province_city_district` VALUES (130825, 1308, '隆化县');
INSERT INTO `province_city_district` VALUES (130826, 1308, '丰宁满族自治县');
INSERT INTO `province_city_district` VALUES (130827, 1308, '宽城满族自治县');
INSERT INTO `province_city_district` VALUES (130828, 1308, '围场满族蒙古族自治县');
INSERT INTO `province_city_district` VALUES (130901, 1309, '市辖区');
INSERT INTO `province_city_district` VALUES (130902, 1309, '新华区');
INSERT INTO `province_city_district` VALUES (130903, 1309, '运河区');
INSERT INTO `province_city_district` VALUES (130921, 1309, '沧  县');
INSERT INTO `province_city_district` VALUES (130922, 1309, '青  县');
INSERT INTO `province_city_district` VALUES (130923, 1309, '东光县');
INSERT INTO `province_city_district` VALUES (130924, 1309, '海兴县');
INSERT INTO `province_city_district` VALUES (130925, 1309, '盐山县');
INSERT INTO `province_city_district` VALUES (130926, 1309, '肃宁县');
INSERT INTO `province_city_district` VALUES (130927, 1309, '南皮县');
INSERT INTO `province_city_district` VALUES (130928, 1309, '吴桥县');
INSERT INTO `province_city_district` VALUES (130929, 1309, '献  县');
INSERT INTO `province_city_district` VALUES (130930, 1309, '孟村回族自治县');
INSERT INTO `province_city_district` VALUES (130981, 1309, '泊头市');
INSERT INTO `province_city_district` VALUES (130982, 1309, '任丘市');
INSERT INTO `province_city_district` VALUES (130983, 1309, '黄骅市');
INSERT INTO `province_city_district` VALUES (130984, 1309, '河间市');
INSERT INTO `province_city_district` VALUES (131001, 1310, '市辖区');
INSERT INTO `province_city_district` VALUES (131002, 1310, '安次区');
INSERT INTO `province_city_district` VALUES (131003, 1310, '廊坊市广阳区');
INSERT INTO `province_city_district` VALUES (131022, 1310, '固安县');
INSERT INTO `province_city_district` VALUES (131023, 1310, '永清县');
INSERT INTO `province_city_district` VALUES (131024, 1310, '香河县');
INSERT INTO `province_city_district` VALUES (131025, 1310, '大城县');
INSERT INTO `province_city_district` VALUES (131026, 1310, '文安县');
INSERT INTO `province_city_district` VALUES (131028, 1310, '大厂回族自治县');
INSERT INTO `province_city_district` VALUES (131081, 1310, '霸州市');
INSERT INTO `province_city_district` VALUES (131082, 1310, '三河市');
INSERT INTO `province_city_district` VALUES (131101, 1311, '市辖区');
INSERT INTO `province_city_district` VALUES (131102, 1311, '桃城区');
INSERT INTO `province_city_district` VALUES (131121, 1311, '枣强县');
INSERT INTO `province_city_district` VALUES (131122, 1311, '武邑县');
INSERT INTO `province_city_district` VALUES (131123, 1311, '武强县');
INSERT INTO `province_city_district` VALUES (131124, 1311, '饶阳县');
INSERT INTO `province_city_district` VALUES (131125, 1311, '安平县');
INSERT INTO `province_city_district` VALUES (131126, 1311, '故城县');
INSERT INTO `province_city_district` VALUES (131127, 1311, '景  县');
INSERT INTO `province_city_district` VALUES (131128, 1311, '阜城县');
INSERT INTO `province_city_district` VALUES (131181, 1311, '冀州市');
INSERT INTO `province_city_district` VALUES (131182, 1311, '深州市');
INSERT INTO `province_city_district` VALUES (140101, 1401, '市辖区');
INSERT INTO `province_city_district` VALUES (140105, 1401, '小店区');
INSERT INTO `province_city_district` VALUES (140106, 1401, '迎泽区');
INSERT INTO `province_city_district` VALUES (140107, 1401, '杏花岭区');
INSERT INTO `province_city_district` VALUES (140108, 1401, '尖草坪区');
INSERT INTO `province_city_district` VALUES (140109, 1401, '万柏林区');
INSERT INTO `province_city_district` VALUES (140110, 1401, '晋源区');
INSERT INTO `province_city_district` VALUES (140121, 1401, '清徐县');
INSERT INTO `province_city_district` VALUES (140122, 1401, '阳曲县');
INSERT INTO `province_city_district` VALUES (140123, 1401, '娄烦县');
INSERT INTO `province_city_district` VALUES (140181, 1401, '古交市');
INSERT INTO `province_city_district` VALUES (140201, 1402, '市辖区');
INSERT INTO `province_city_district` VALUES (140202, 1402, '城  区');
INSERT INTO `province_city_district` VALUES (140203, 1402, '矿  区');
INSERT INTO `province_city_district` VALUES (140211, 1402, '南郊区');
INSERT INTO `province_city_district` VALUES (140212, 1402, '新荣区');
INSERT INTO `province_city_district` VALUES (140221, 1402, '阳高县');
INSERT INTO `province_city_district` VALUES (140222, 1402, '天镇县');
INSERT INTO `province_city_district` VALUES (140223, 1402, '广灵县');
INSERT INTO `province_city_district` VALUES (140224, 1402, '灵丘县');
INSERT INTO `province_city_district` VALUES (140225, 1402, '浑源县');
INSERT INTO `province_city_district` VALUES (140226, 1402, '左云县');
INSERT INTO `province_city_district` VALUES (140227, 1402, '大同县');
INSERT INTO `province_city_district` VALUES (140301, 1403, '市辖区');
INSERT INTO `province_city_district` VALUES (140302, 1403, '城  区');
INSERT INTO `province_city_district` VALUES (140303, 1403, '矿  区');
INSERT INTO `province_city_district` VALUES (140311, 1403, '郊  区');
INSERT INTO `province_city_district` VALUES (140321, 1403, '平定县');
INSERT INTO `province_city_district` VALUES (140322, 1403, '盂  县');
INSERT INTO `province_city_district` VALUES (140401, 1404, '市辖区');
INSERT INTO `province_city_district` VALUES (140402, 1404, '城  区');
INSERT INTO `province_city_district` VALUES (140411, 1404, '郊  区');
INSERT INTO `province_city_district` VALUES (140421, 1404, '长治县');
INSERT INTO `province_city_district` VALUES (140423, 1404, '襄垣县');
INSERT INTO `province_city_district` VALUES (140424, 1404, '屯留县');
INSERT INTO `province_city_district` VALUES (140425, 1404, '平顺县');
INSERT INTO `province_city_district` VALUES (140426, 1404, '黎城县');
INSERT INTO `province_city_district` VALUES (140427, 1404, '壶关县');
INSERT INTO `province_city_district` VALUES (140428, 1404, '长子县');
INSERT INTO `province_city_district` VALUES (140429, 1404, '武乡县');
INSERT INTO `province_city_district` VALUES (140430, 1404, '沁  县');
INSERT INTO `province_city_district` VALUES (140431, 1404, '沁源县');
INSERT INTO `province_city_district` VALUES (140481, 1404, '潞城市');
INSERT INTO `province_city_district` VALUES (140501, 1405, '市辖区');
INSERT INTO `province_city_district` VALUES (140502, 1405, '城  区');
INSERT INTO `province_city_district` VALUES (140521, 1405, '沁水县');
INSERT INTO `province_city_district` VALUES (140522, 1405, '阳城县');
INSERT INTO `province_city_district` VALUES (140524, 1405, '陵川县');
INSERT INTO `province_city_district` VALUES (140525, 1405, '泽州县');
INSERT INTO `province_city_district` VALUES (140581, 1405, '高平市');
INSERT INTO `province_city_district` VALUES (140601, 1406, '市辖区');
INSERT INTO `province_city_district` VALUES (140602, 1406, '朔城区');
INSERT INTO `province_city_district` VALUES (140603, 1406, '平鲁区');
INSERT INTO `province_city_district` VALUES (140621, 1406, '山阴县');
INSERT INTO `province_city_district` VALUES (140622, 1406, '应  县');
INSERT INTO `province_city_district` VALUES (140623, 1406, '右玉县');
INSERT INTO `province_city_district` VALUES (140624, 1406, '怀仁县');
INSERT INTO `province_city_district` VALUES (140701, 1407, '市辖区');
INSERT INTO `province_city_district` VALUES (140702, 1407, '榆次区');
INSERT INTO `province_city_district` VALUES (140721, 1407, '榆社县');
INSERT INTO `province_city_district` VALUES (140722, 1407, '左权县');
INSERT INTO `province_city_district` VALUES (140723, 1407, '和顺县');
INSERT INTO `province_city_district` VALUES (140724, 1407, '昔阳县');
INSERT INTO `province_city_district` VALUES (140725, 1407, '寿阳县');
INSERT INTO `province_city_district` VALUES (140726, 1407, '太谷县');
INSERT INTO `province_city_district` VALUES (140727, 1407, '祁  县');
INSERT INTO `province_city_district` VALUES (140728, 1407, '平遥县');
INSERT INTO `province_city_district` VALUES (140729, 1407, '灵石县');
INSERT INTO `province_city_district` VALUES (140781, 1407, '介休市');
INSERT INTO `province_city_district` VALUES (140801, 1408, '市辖区');
INSERT INTO `province_city_district` VALUES (140802, 1408, '盐湖区');
INSERT INTO `province_city_district` VALUES (140821, 1408, '临猗县');
INSERT INTO `province_city_district` VALUES (140822, 1408, '万荣县');
INSERT INTO `province_city_district` VALUES (140823, 1408, '闻喜县');
INSERT INTO `province_city_district` VALUES (140824, 1408, '稷山县');
INSERT INTO `province_city_district` VALUES (140825, 1408, '新绛县');
INSERT INTO `province_city_district` VALUES (140826, 1408, '绛  县');
INSERT INTO `province_city_district` VALUES (140827, 1408, '垣曲县');
INSERT INTO `province_city_district` VALUES (140828, 1408, '夏  县');
INSERT INTO `province_city_district` VALUES (140829, 1408, '平陆县');
INSERT INTO `province_city_district` VALUES (140830, 1408, '芮城县');
INSERT INTO `province_city_district` VALUES (140881, 1408, '永济市');
INSERT INTO `province_city_district` VALUES (140882, 1408, '河津市');
INSERT INTO `province_city_district` VALUES (140901, 1409, '市辖区');
INSERT INTO `province_city_district` VALUES (140902, 1409, '忻府区');
INSERT INTO `province_city_district` VALUES (140921, 1409, '定襄县');
INSERT INTO `province_city_district` VALUES (140922, 1409, '五台县');
INSERT INTO `province_city_district` VALUES (140923, 1409, '代  县');
INSERT INTO `province_city_district` VALUES (140924, 1409, '繁峙县');
INSERT INTO `province_city_district` VALUES (140925, 1409, '宁武县');
INSERT INTO `province_city_district` VALUES (140926, 1409, '静乐县');
INSERT INTO `province_city_district` VALUES (140927, 1409, '神池县');
INSERT INTO `province_city_district` VALUES (140928, 1409, '五寨县');
INSERT INTO `province_city_district` VALUES (140929, 1409, '岢岚县');
INSERT INTO `province_city_district` VALUES (140930, 1409, '河曲县');
INSERT INTO `province_city_district` VALUES (140931, 1409, '保德县');
INSERT INTO `province_city_district` VALUES (140932, 1409, '偏关县');
INSERT INTO `province_city_district` VALUES (140981, 1409, '原平市');
INSERT INTO `province_city_district` VALUES (141001, 1410, '市辖区');
INSERT INTO `province_city_district` VALUES (141002, 1410, '尧都区');
INSERT INTO `province_city_district` VALUES (141021, 1410, '曲沃县');
INSERT INTO `province_city_district` VALUES (141022, 1410, '翼城县');
INSERT INTO `province_city_district` VALUES (141023, 1410, '襄汾县');
INSERT INTO `province_city_district` VALUES (141024, 1410, '洪洞县');
INSERT INTO `province_city_district` VALUES (141025, 1410, '古  县');
INSERT INTO `province_city_district` VALUES (141026, 1410, '安泽县');
INSERT INTO `province_city_district` VALUES (141027, 1410, '浮山县');
INSERT INTO `province_city_district` VALUES (141028, 1410, '吉  县');
INSERT INTO `province_city_district` VALUES (141029, 1410, '乡宁县');
INSERT INTO `province_city_district` VALUES (141030, 1410, '大宁县');
INSERT INTO `province_city_district` VALUES (141031, 1410, '隰  县');
INSERT INTO `province_city_district` VALUES (141032, 1410, '永和县');
INSERT INTO `province_city_district` VALUES (141033, 1410, '蒲  县');
INSERT INTO `province_city_district` VALUES (141034, 1410, '汾西县');
INSERT INTO `province_city_district` VALUES (141081, 1410, '侯马市');
INSERT INTO `province_city_district` VALUES (141082, 1410, '霍州市');
INSERT INTO `province_city_district` VALUES (142301, 1423, '孝义市');
INSERT INTO `province_city_district` VALUES (142302, 1423, '离石市');
INSERT INTO `province_city_district` VALUES (142303, 1423, '汾阳市');
INSERT INTO `province_city_district` VALUES (142322, 1423, '文水县');
INSERT INTO `province_city_district` VALUES (142323, 1423, '交城县');
INSERT INTO `province_city_district` VALUES (142325, 1423, '兴  县');
INSERT INTO `province_city_district` VALUES (142326, 1423, '临  县');
INSERT INTO `province_city_district` VALUES (142327, 1423, '柳林县');
INSERT INTO `province_city_district` VALUES (142328, 1423, '石楼县');
INSERT INTO `province_city_district` VALUES (142329, 1423, '岚  县');
INSERT INTO `province_city_district` VALUES (142330, 1423, '方山县');
INSERT INTO `province_city_district` VALUES (142332, 1423, '中阳县');
INSERT INTO `province_city_district` VALUES (142333, 1423, '交口县');
INSERT INTO `province_city_district` VALUES (150101, 1501, '市辖区');
INSERT INTO `province_city_district` VALUES (150102, 1501, '新城区');
INSERT INTO `province_city_district` VALUES (150103, 1501, '回民区');
INSERT INTO `province_city_district` VALUES (150104, 1501, '玉泉区');
INSERT INTO `province_city_district` VALUES (150105, 1501, '赛罕区');
INSERT INTO `province_city_district` VALUES (150121, 1501, '土默特左旗');
INSERT INTO `province_city_district` VALUES (150122, 1501, '托克托县');
INSERT INTO `province_city_district` VALUES (150123, 1501, '和林格尔县');
INSERT INTO `province_city_district` VALUES (150124, 1501, '清水河县');
INSERT INTO `province_city_district` VALUES (150125, 1501, '武川县');
INSERT INTO `province_city_district` VALUES (150201, 1502, '市辖区');
INSERT INTO `province_city_district` VALUES (150202, 1502, '东河区');
INSERT INTO `province_city_district` VALUES (150203, 1502, '昆都伦区');
INSERT INTO `province_city_district` VALUES (150204, 1502, '青山区');
INSERT INTO `province_city_district` VALUES (150205, 1502, '石拐区');
INSERT INTO `province_city_district` VALUES (150206, 1502, '白云矿区');
INSERT INTO `province_city_district` VALUES (150207, 1502, '九原区');
INSERT INTO `province_city_district` VALUES (150221, 1502, '土默特右旗');
INSERT INTO `province_city_district` VALUES (150222, 1502, '固阳县');
INSERT INTO `province_city_district` VALUES (150223, 1502, '达尔罕茂明安联合旗');
INSERT INTO `province_city_district` VALUES (150301, 1503, '市辖区');
INSERT INTO `province_city_district` VALUES (150302, 1503, '海勃湾区');
INSERT INTO `province_city_district` VALUES (150303, 1503, '海南区');
INSERT INTO `province_city_district` VALUES (150304, 1503, '乌达区');
INSERT INTO `province_city_district` VALUES (150401, 1504, '市辖区');
INSERT INTO `province_city_district` VALUES (150402, 1504, '红山区');
INSERT INTO `province_city_district` VALUES (150403, 1504, '元宝山区');
INSERT INTO `province_city_district` VALUES (150404, 1504, '松山区');
INSERT INTO `province_city_district` VALUES (150421, 1504, '阿鲁科尔沁旗');
INSERT INTO `province_city_district` VALUES (150422, 1504, '巴林左旗');
INSERT INTO `province_city_district` VALUES (150423, 1504, '巴林右旗');
INSERT INTO `province_city_district` VALUES (150424, 1504, '林西县');
INSERT INTO `province_city_district` VALUES (150425, 1504, '克什克腾旗');
INSERT INTO `province_city_district` VALUES (150426, 1504, '翁牛特旗');
INSERT INTO `province_city_district` VALUES (150428, 1504, '喀喇沁旗');
INSERT INTO `province_city_district` VALUES (150429, 1504, '宁城县');
INSERT INTO `province_city_district` VALUES (150430, 1504, '敖汉旗');
INSERT INTO `province_city_district` VALUES (150501, 1505, '市辖区');
INSERT INTO `province_city_district` VALUES (150502, 1505, '科尔沁区');
INSERT INTO `province_city_district` VALUES (150521, 1505, '科尔沁左翼中旗');
INSERT INTO `province_city_district` VALUES (150522, 1505, '科尔沁左翼后旗');
INSERT INTO `province_city_district` VALUES (150523, 1505, '开鲁县');
INSERT INTO `province_city_district` VALUES (150524, 1505, '库伦旗');
INSERT INTO `province_city_district` VALUES (150525, 1505, '奈曼旗');
INSERT INTO `province_city_district` VALUES (150526, 1505, '扎鲁特旗');
INSERT INTO `province_city_district` VALUES (150581, 1505, '霍林郭勒市');
INSERT INTO `province_city_district` VALUES (152101, 1521, '海拉尔市');
INSERT INTO `province_city_district` VALUES (152102, 1521, '满洲里市');
INSERT INTO `province_city_district` VALUES (152103, 1521, '扎兰屯市');
INSERT INTO `province_city_district` VALUES (152104, 1521, '牙克石市');
INSERT INTO `province_city_district` VALUES (152105, 1521, '根河市');
INSERT INTO `province_city_district` VALUES (152106, 1521, '额尔古纳市');
INSERT INTO `province_city_district` VALUES (152122, 1521, '阿荣旗');
INSERT INTO `province_city_district` VALUES (152123, 1521, '莫力达瓦达斡尔族自治');
INSERT INTO `province_city_district` VALUES (152127, 1521, '鄂伦春自治旗');
INSERT INTO `province_city_district` VALUES (152128, 1521, '鄂温克族自治旗');
INSERT INTO `province_city_district` VALUES (152129, 1521, '新巴尔虎右旗');
INSERT INTO `province_city_district` VALUES (152130, 1521, '新巴尔虎左旗');
INSERT INTO `province_city_district` VALUES (152131, 1521, '陈巴尔虎旗');
INSERT INTO `province_city_district` VALUES (152201, 1522, '乌兰浩特市');
INSERT INTO `province_city_district` VALUES (152202, 1522, '阿尔山市');
INSERT INTO `province_city_district` VALUES (152221, 1522, '科尔沁右翼前旗');
INSERT INTO `province_city_district` VALUES (152222, 1522, '科尔沁右翼中旗');
INSERT INTO `province_city_district` VALUES (152223, 1522, '扎赉特旗');
INSERT INTO `province_city_district` VALUES (152224, 1522, '突泉县');
INSERT INTO `province_city_district` VALUES (152501, 1525, '二连浩特市');
INSERT INTO `province_city_district` VALUES (152502, 1525, '锡林浩特市');
INSERT INTO `province_city_district` VALUES (152522, 1525, '阿巴嘎旗');
INSERT INTO `province_city_district` VALUES (152523, 1525, '苏尼特左旗');
INSERT INTO `province_city_district` VALUES (152524, 1525, '苏尼特右旗');
INSERT INTO `province_city_district` VALUES (152525, 1525, '东乌珠穆沁旗');
INSERT INTO `province_city_district` VALUES (152526, 1525, '西乌珠穆沁旗');
INSERT INTO `province_city_district` VALUES (152527, 1525, '太仆寺旗');
INSERT INTO `province_city_district` VALUES (152528, 1525, '镶黄旗');
INSERT INTO `province_city_district` VALUES (152529, 1525, '正镶白旗');
INSERT INTO `province_city_district` VALUES (152530, 1525, '正蓝旗');
INSERT INTO `province_city_district` VALUES (152531, 1525, '多伦县');
INSERT INTO `province_city_district` VALUES (152601, 1526, '集宁市');
INSERT INTO `province_city_district` VALUES (152602, 1526, '丰镇市');
INSERT INTO `province_city_district` VALUES (152624, 1526, '卓资县');
INSERT INTO `province_city_district` VALUES (152625, 1526, '化德县');
INSERT INTO `province_city_district` VALUES (152626, 1526, '商都县');
INSERT INTO `province_city_district` VALUES (152627, 1526, '兴和县');
INSERT INTO `province_city_district` VALUES (152629, 1526, '凉城县');
INSERT INTO `province_city_district` VALUES (152630, 1526, '察哈尔右翼前旗');
INSERT INTO `province_city_district` VALUES (152631, 1526, '察哈尔右翼中旗');
INSERT INTO `province_city_district` VALUES (152632, 1526, '察哈尔右翼后旗');
INSERT INTO `province_city_district` VALUES (152634, 1526, '四子王旗');
INSERT INTO `province_city_district` VALUES (152701, 1527, '东胜市');
INSERT INTO `province_city_district` VALUES (152722, 1527, '达拉特旗');
INSERT INTO `province_city_district` VALUES (152723, 1527, '准格尔旗');
INSERT INTO `province_city_district` VALUES (152724, 1527, '鄂托克前旗');
INSERT INTO `province_city_district` VALUES (152725, 1527, '鄂托克旗');
INSERT INTO `province_city_district` VALUES (152726, 1527, '杭锦旗');
INSERT INTO `province_city_district` VALUES (152727, 1527, '乌审旗');
INSERT INTO `province_city_district` VALUES (152728, 1527, '伊金霍洛旗');
INSERT INTO `province_city_district` VALUES (152801, 1528, '临河市');
INSERT INTO `province_city_district` VALUES (152822, 1528, '五原县');
INSERT INTO `province_city_district` VALUES (152823, 1528, '磴口县');
INSERT INTO `province_city_district` VALUES (152824, 1528, '乌拉特前旗');
INSERT INTO `province_city_district` VALUES (152825, 1528, '乌拉特中旗');
INSERT INTO `province_city_district` VALUES (152826, 1528, '乌拉特后旗');
INSERT INTO `province_city_district` VALUES (152827, 1528, '杭锦后旗');
INSERT INTO `province_city_district` VALUES (152921, 1529, '阿拉善左旗');
INSERT INTO `province_city_district` VALUES (152922, 1529, '阿拉善右旗');
INSERT INTO `province_city_district` VALUES (152923, 1529, '额济纳旗');
INSERT INTO `province_city_district` VALUES (210101, 2101, '市辖区');
INSERT INTO `province_city_district` VALUES (210102, 2101, '和平区');
INSERT INTO `province_city_district` VALUES (210103, 2101, '沈河区');
INSERT INTO `province_city_district` VALUES (210104, 2101, '大东区');
INSERT INTO `province_city_district` VALUES (210105, 2101, '皇姑区');
INSERT INTO `province_city_district` VALUES (210106, 2101, '铁西区');
INSERT INTO `province_city_district` VALUES (210111, 2101, '苏家屯区');
INSERT INTO `province_city_district` VALUES (210112, 2101, '东陵区');
INSERT INTO `province_city_district` VALUES (210113, 2101, '新城子区');
INSERT INTO `province_city_district` VALUES (210114, 2101, '于洪区');
INSERT INTO `province_city_district` VALUES (210122, 2101, '辽中县');
INSERT INTO `province_city_district` VALUES (210123, 2101, '康平县');
INSERT INTO `province_city_district` VALUES (210124, 2101, '法库县');
INSERT INTO `province_city_district` VALUES (210181, 2101, '新民市');
INSERT INTO `province_city_district` VALUES (210201, 2102, '市辖区');
INSERT INTO `province_city_district` VALUES (210202, 2102, '中山区');
INSERT INTO `province_city_district` VALUES (210203, 2102, '西岗区');
INSERT INTO `province_city_district` VALUES (210204, 2102, '沙河口区');
INSERT INTO `province_city_district` VALUES (210211, 2102, '甘井子区');
INSERT INTO `province_city_district` VALUES (210212, 2102, '旅顺口区');
INSERT INTO `province_city_district` VALUES (210213, 2102, '金州区');
INSERT INTO `province_city_district` VALUES (210224, 2102, '长海县');
INSERT INTO `province_city_district` VALUES (210281, 2102, '瓦房店市');
INSERT INTO `province_city_district` VALUES (210282, 2102, '普兰店市');
INSERT INTO `province_city_district` VALUES (210283, 2102, '庄河市');
INSERT INTO `province_city_district` VALUES (210301, 2103, '市辖区');
INSERT INTO `province_city_district` VALUES (210302, 2103, '铁东区');
INSERT INTO `province_city_district` VALUES (210303, 2103, '铁西区');
INSERT INTO `province_city_district` VALUES (210304, 2103, '立山区');
INSERT INTO `province_city_district` VALUES (210311, 2103, '千山区');
INSERT INTO `province_city_district` VALUES (210321, 2103, '台安县');
INSERT INTO `province_city_district` VALUES (210323, 2103, '岫岩满族自治县');
INSERT INTO `province_city_district` VALUES (210381, 2103, '海城市');
INSERT INTO `province_city_district` VALUES (210401, 2104, '市辖区');
INSERT INTO `province_city_district` VALUES (210402, 2104, '新抚区');
INSERT INTO `province_city_district` VALUES (210403, 2104, '东洲区');
INSERT INTO `province_city_district` VALUES (210404, 2104, '望花区');
INSERT INTO `province_city_district` VALUES (210411, 2104, '顺城区');
INSERT INTO `province_city_district` VALUES (210421, 2104, '抚顺县');
INSERT INTO `province_city_district` VALUES (210422, 2104, '新宾满族自治县');
INSERT INTO `province_city_district` VALUES (210423, 2104, '清原满族自治县');
INSERT INTO `province_city_district` VALUES (210501, 2105, '市辖区');
INSERT INTO `province_city_district` VALUES (210502, 2105, '平山区');
INSERT INTO `province_city_district` VALUES (210503, 2105, '溪湖区');
INSERT INTO `province_city_district` VALUES (210504, 2105, '明山区');
INSERT INTO `province_city_district` VALUES (210505, 2105, '南芬区');
INSERT INTO `province_city_district` VALUES (210521, 2105, '本溪满族自治县');
INSERT INTO `province_city_district` VALUES (210522, 2105, '桓仁满族自治县');
INSERT INTO `province_city_district` VALUES (210601, 2106, '市辖区');
INSERT INTO `province_city_district` VALUES (210602, 2106, '元宝区');
INSERT INTO `province_city_district` VALUES (210603, 2106, '振兴区');
INSERT INTO `province_city_district` VALUES (210604, 2106, '振安区');
INSERT INTO `province_city_district` VALUES (210624, 2106, '宽甸满族自治县');
INSERT INTO `province_city_district` VALUES (210681, 2106, '东港市');
INSERT INTO `province_city_district` VALUES (210682, 2106, '凤城市');
INSERT INTO `province_city_district` VALUES (210701, 2107, '市辖区');
INSERT INTO `province_city_district` VALUES (210702, 2107, '古塔区');
INSERT INTO `province_city_district` VALUES (210703, 2107, '凌河区');
INSERT INTO `province_city_district` VALUES (210711, 2107, '太和区');
INSERT INTO `province_city_district` VALUES (210726, 2107, '黑山县');
INSERT INTO `province_city_district` VALUES (210727, 2107, '义  县');
INSERT INTO `province_city_district` VALUES (210781, 2107, '凌海市');
INSERT INTO `province_city_district` VALUES (210782, 2107, '北宁市');
INSERT INTO `province_city_district` VALUES (210801, 2108, '市辖区');
INSERT INTO `province_city_district` VALUES (210802, 2108, '站前区');
INSERT INTO `province_city_district` VALUES (210803, 2108, '西市区');
INSERT INTO `province_city_district` VALUES (210804, 2108, '鲅鱼圈区');
INSERT INTO `province_city_district` VALUES (210811, 2108, '老边区');
INSERT INTO `province_city_district` VALUES (210881, 2108, '盖州市');
INSERT INTO `province_city_district` VALUES (210882, 2108, '大石桥市');
INSERT INTO `province_city_district` VALUES (210901, 2109, '市辖区');
INSERT INTO `province_city_district` VALUES (210902, 2109, '海州区');
INSERT INTO `province_city_district` VALUES (210903, 2109, '新邱区');
INSERT INTO `province_city_district` VALUES (210904, 2109, '太平区');
INSERT INTO `province_city_district` VALUES (210905, 2109, '清河门区');
INSERT INTO `province_city_district` VALUES (210911, 2109, '细河区');
INSERT INTO `province_city_district` VALUES (210921, 2109, '阜新蒙古族自治县');
INSERT INTO `province_city_district` VALUES (210922, 2109, '彰武县');
INSERT INTO `province_city_district` VALUES (211001, 2110, '市辖区');
INSERT INTO `province_city_district` VALUES (211002, 2110, '白塔区');
INSERT INTO `province_city_district` VALUES (211003, 2110, '文圣区');
INSERT INTO `province_city_district` VALUES (211004, 2110, '宏伟区');
INSERT INTO `province_city_district` VALUES (211005, 2110, '弓长岭区');
INSERT INTO `province_city_district` VALUES (211011, 2110, '太子河区');
INSERT INTO `province_city_district` VALUES (211021, 2110, '辽阳县');
INSERT INTO `province_city_district` VALUES (211081, 2110, '灯塔市');
INSERT INTO `province_city_district` VALUES (211101, 2111, '市辖区');
INSERT INTO `province_city_district` VALUES (211102, 2111, '双台子区');
INSERT INTO `province_city_district` VALUES (211103, 2111, '兴隆台区');
INSERT INTO `province_city_district` VALUES (211121, 2111, '大洼县');
INSERT INTO `province_city_district` VALUES (211122, 2111, '盘山县');
INSERT INTO `province_city_district` VALUES (211201, 2112, '市辖区');
INSERT INTO `province_city_district` VALUES (211202, 2112, '银州区');
INSERT INTO `province_city_district` VALUES (211204, 2112, '清河区');
INSERT INTO `province_city_district` VALUES (211221, 2112, '铁岭县');
INSERT INTO `province_city_district` VALUES (211223, 2112, '西丰县');
INSERT INTO `province_city_district` VALUES (211224, 2112, '昌图县');
INSERT INTO `province_city_district` VALUES (211281, 2112, '铁法市');
INSERT INTO `province_city_district` VALUES (211282, 2112, '开原市');
INSERT INTO `province_city_district` VALUES (211301, 2113, '市辖区');
INSERT INTO `province_city_district` VALUES (211302, 2113, '双塔区');
INSERT INTO `province_city_district` VALUES (211303, 2113, '龙城区');
INSERT INTO `province_city_district` VALUES (211321, 2113, '朝阳县');
INSERT INTO `province_city_district` VALUES (211322, 2113, '建平县');
INSERT INTO `province_city_district` VALUES (211324, 2113, '喀喇沁左翼蒙古族自治');
INSERT INTO `province_city_district` VALUES (211381, 2113, '北票市');
INSERT INTO `province_city_district` VALUES (211382, 2113, '凌源市');
INSERT INTO `province_city_district` VALUES (211401, 2114, '市辖区');
INSERT INTO `province_city_district` VALUES (211402, 2114, '连山区');
INSERT INTO `province_city_district` VALUES (211403, 2114, '龙港区');
INSERT INTO `province_city_district` VALUES (211404, 2114, '南票区');
INSERT INTO `province_city_district` VALUES (211421, 2114, '绥中县');
INSERT INTO `province_city_district` VALUES (211422, 2114, '建昌县');
INSERT INTO `province_city_district` VALUES (211481, 2114, '兴城市');
INSERT INTO `province_city_district` VALUES (220101, 2201, '市辖区');
INSERT INTO `province_city_district` VALUES (220102, 2201, '南关区');
INSERT INTO `province_city_district` VALUES (220103, 2201, '宽城区');
INSERT INTO `province_city_district` VALUES (220104, 2201, '朝阳区');
INSERT INTO `province_city_district` VALUES (220105, 2201, '二道区');
INSERT INTO `province_city_district` VALUES (220106, 2201, '绿园区');
INSERT INTO `province_city_district` VALUES (220112, 2201, '双阳区');
INSERT INTO `province_city_district` VALUES (220122, 2201, '农安县');
INSERT INTO `province_city_district` VALUES (220181, 2201, '九台市');
INSERT INTO `province_city_district` VALUES (220182, 2201, '榆树市');
INSERT INTO `province_city_district` VALUES (220183, 2201, '德惠市');
INSERT INTO `province_city_district` VALUES (220201, 2202, '市辖区');
INSERT INTO `province_city_district` VALUES (220202, 2202, '昌邑区');
INSERT INTO `province_city_district` VALUES (220203, 2202, '龙潭区');
INSERT INTO `province_city_district` VALUES (220204, 2202, '船营区');
INSERT INTO `province_city_district` VALUES (220211, 2202, '丰满区');
INSERT INTO `province_city_district` VALUES (220221, 2202, '永吉县');
INSERT INTO `province_city_district` VALUES (220281, 2202, '蛟河市');
INSERT INTO `province_city_district` VALUES (220282, 2202, '桦甸市');
INSERT INTO `province_city_district` VALUES (220283, 2202, '舒兰市');
INSERT INTO `province_city_district` VALUES (220284, 2202, '磐石市');
INSERT INTO `province_city_district` VALUES (220301, 2203, '市辖区');
INSERT INTO `province_city_district` VALUES (220302, 2203, '铁西区');
INSERT INTO `province_city_district` VALUES (220303, 2203, '铁东区');
INSERT INTO `province_city_district` VALUES (220322, 2203, '梨树县');
INSERT INTO `province_city_district` VALUES (220323, 2203, '伊通满族自治县');
INSERT INTO `province_city_district` VALUES (220381, 2203, '公主岭市');
INSERT INTO `province_city_district` VALUES (220382, 2203, '双辽市');
INSERT INTO `province_city_district` VALUES (220401, 2204, '市辖区');
INSERT INTO `province_city_district` VALUES (220402, 2204, '龙山区');
INSERT INTO `province_city_district` VALUES (220403, 2204, '西安区');
INSERT INTO `province_city_district` VALUES (220421, 2204, '东丰县');
INSERT INTO `province_city_district` VALUES (220422, 2204, '东辽县');
INSERT INTO `province_city_district` VALUES (220501, 2205, '市辖区');
INSERT INTO `province_city_district` VALUES (220502, 2205, '东昌区');
INSERT INTO `province_city_district` VALUES (220503, 2205, '二道江区');
INSERT INTO `province_city_district` VALUES (220521, 2205, '通化县');
INSERT INTO `province_city_district` VALUES (220523, 2205, '辉南县');
INSERT INTO `province_city_district` VALUES (220524, 2205, '柳河县');
INSERT INTO `province_city_district` VALUES (220581, 2205, '梅河口市');
INSERT INTO `province_city_district` VALUES (220582, 2205, '集安市');
INSERT INTO `province_city_district` VALUES (220601, 2206, '市辖区');
INSERT INTO `province_city_district` VALUES (220602, 2206, '八道江区');
INSERT INTO `province_city_district` VALUES (220621, 2206, '抚松县');
INSERT INTO `province_city_district` VALUES (220622, 2206, '靖宇县');
INSERT INTO `province_city_district` VALUES (220623, 2206, '长白朝鲜族自治县');
INSERT INTO `province_city_district` VALUES (220625, 2206, '江源县');
INSERT INTO `province_city_district` VALUES (220681, 2206, '临江市');
INSERT INTO `province_city_district` VALUES (220701, 2207, '市辖区');
INSERT INTO `province_city_district` VALUES (220702, 2207, '宁江区');
INSERT INTO `province_city_district` VALUES (220721, 2207, '前郭尔罗斯蒙古族自治');
INSERT INTO `province_city_district` VALUES (220722, 2207, '长岭县');
INSERT INTO `province_city_district` VALUES (220723, 2207, '乾安县');
INSERT INTO `province_city_district` VALUES (220724, 2207, '扶余县');
INSERT INTO `province_city_district` VALUES (220801, 2208, '市辖区');
INSERT INTO `province_city_district` VALUES (220802, 2208, '洮北区');
INSERT INTO `province_city_district` VALUES (220821, 2208, '镇赉县');
INSERT INTO `province_city_district` VALUES (220822, 2208, '通榆县');
INSERT INTO `province_city_district` VALUES (220881, 2208, '洮南市');
INSERT INTO `province_city_district` VALUES (220882, 2208, '大安市');
INSERT INTO `province_city_district` VALUES (222401, 2224, '延吉市');
INSERT INTO `province_city_district` VALUES (222402, 2224, '图们市');
INSERT INTO `province_city_district` VALUES (222403, 2224, '敦化市');
INSERT INTO `province_city_district` VALUES (222404, 2224, '珲春市');
INSERT INTO `province_city_district` VALUES (222405, 2224, '龙井市');
INSERT INTO `province_city_district` VALUES (222406, 2224, '和龙市');
INSERT INTO `province_city_district` VALUES (222424, 2224, '汪清县');
INSERT INTO `province_city_district` VALUES (222426, 2224, '安图县');
INSERT INTO `province_city_district` VALUES (230101, 2301, '市辖区');
INSERT INTO `province_city_district` VALUES (230102, 2301, '道里区');
INSERT INTO `province_city_district` VALUES (230103, 2301, '南岗区');
INSERT INTO `province_city_district` VALUES (230104, 2301, '道外区');
INSERT INTO `province_city_district` VALUES (230105, 2301, '太平区');
INSERT INTO `province_city_district` VALUES (230106, 2301, '香坊区');
INSERT INTO `province_city_district` VALUES (230107, 2301, '动力区');
INSERT INTO `province_city_district` VALUES (230108, 2301, '平房区');
INSERT INTO `province_city_district` VALUES (230121, 2301, '呼兰县');
INSERT INTO `province_city_district` VALUES (230123, 2301, '依兰县');
INSERT INTO `province_city_district` VALUES (230124, 2301, '方正县');
INSERT INTO `province_city_district` VALUES (230125, 2301, '宾  县');
INSERT INTO `province_city_district` VALUES (230126, 2301, '巴彦县');
INSERT INTO `province_city_district` VALUES (230127, 2301, '木兰县');
INSERT INTO `province_city_district` VALUES (230128, 2301, '通河县');
INSERT INTO `province_city_district` VALUES (230129, 2301, '延寿县');
INSERT INTO `province_city_district` VALUES (230181, 2301, '阿城市');
INSERT INTO `province_city_district` VALUES (230182, 2301, '双城市');
INSERT INTO `province_city_district` VALUES (230183, 2301, '尚志市');
INSERT INTO `province_city_district` VALUES (230184, 2301, '五常市');
INSERT INTO `province_city_district` VALUES (230201, 2302, '市辖区');
INSERT INTO `province_city_district` VALUES (230202, 2302, '龙沙区');
INSERT INTO `province_city_district` VALUES (230203, 2302, '建华区');
INSERT INTO `province_city_district` VALUES (230204, 2302, '铁锋区');
INSERT INTO `province_city_district` VALUES (230205, 2302, '昂昂溪区');
INSERT INTO `province_city_district` VALUES (230206, 2302, '富拉尔基区');
INSERT INTO `province_city_district` VALUES (230207, 2302, '碾子山区');
INSERT INTO `province_city_district` VALUES (230208, 2302, '梅里斯达斡尔族区');
INSERT INTO `province_city_district` VALUES (230221, 2302, '龙江县');
INSERT INTO `province_city_district` VALUES (230223, 2302, '依安县');
INSERT INTO `province_city_district` VALUES (230224, 2302, '泰来县');
INSERT INTO `province_city_district` VALUES (230225, 2302, '甘南县');
INSERT INTO `province_city_district` VALUES (230227, 2302, '富裕县');
INSERT INTO `province_city_district` VALUES (230229, 2302, '克山县');
INSERT INTO `province_city_district` VALUES (230230, 2302, '克东县');
INSERT INTO `province_city_district` VALUES (230231, 2302, '拜泉县');
INSERT INTO `province_city_district` VALUES (230281, 2302, '讷河市');
INSERT INTO `province_city_district` VALUES (230301, 2303, '市辖区');
INSERT INTO `province_city_district` VALUES (230302, 2303, '鸡冠区');
INSERT INTO `province_city_district` VALUES (230303, 2303, '恒山区');
INSERT INTO `province_city_district` VALUES (230304, 2303, '滴道区');
INSERT INTO `province_city_district` VALUES (230305, 2303, '梨树区');
INSERT INTO `province_city_district` VALUES (230306, 2303, '城子河区');
INSERT INTO `province_city_district` VALUES (230307, 2303, '麻山区');
INSERT INTO `province_city_district` VALUES (230321, 2303, '鸡东县');
INSERT INTO `province_city_district` VALUES (230381, 2303, '虎林市');
INSERT INTO `province_city_district` VALUES (230382, 2303, '密山市');
INSERT INTO `province_city_district` VALUES (230401, 2304, '市辖区');
INSERT INTO `province_city_district` VALUES (230402, 2304, '向阳区');
INSERT INTO `province_city_district` VALUES (230403, 2304, '工农区');
INSERT INTO `province_city_district` VALUES (230404, 2304, '南山区');
INSERT INTO `province_city_district` VALUES (230405, 2304, '兴安区');
INSERT INTO `province_city_district` VALUES (230406, 2304, '东山区');
INSERT INTO `province_city_district` VALUES (230407, 2304, '兴山区');
INSERT INTO `province_city_district` VALUES (230421, 2304, '萝北县');
INSERT INTO `province_city_district` VALUES (230422, 2304, '绥滨县');
INSERT INTO `province_city_district` VALUES (230501, 2305, '市辖区');
INSERT INTO `province_city_district` VALUES (230502, 2305, '尖山区');
INSERT INTO `province_city_district` VALUES (230503, 2305, '岭东区');
INSERT INTO `province_city_district` VALUES (230505, 2305, '四方台区');
INSERT INTO `province_city_district` VALUES (230506, 2305, '宝山区');
INSERT INTO `province_city_district` VALUES (230521, 2305, '集贤县');
INSERT INTO `province_city_district` VALUES (230522, 2305, '友谊县');
INSERT INTO `province_city_district` VALUES (230523, 2305, '宝清县');
INSERT INTO `province_city_district` VALUES (230524, 2305, '饶河县');
INSERT INTO `province_city_district` VALUES (230601, 2306, '市辖区');
INSERT INTO `province_city_district` VALUES (230602, 2306, '萨尔图区');
INSERT INTO `province_city_district` VALUES (230603, 2306, '龙凤区');
INSERT INTO `province_city_district` VALUES (230604, 2306, '让胡路区');
INSERT INTO `province_city_district` VALUES (230605, 2306, '红岗区');
INSERT INTO `province_city_district` VALUES (230606, 2306, '大同区');
INSERT INTO `province_city_district` VALUES (230621, 2306, '肇州县');
INSERT INTO `province_city_district` VALUES (230622, 2306, '肇源县');
INSERT INTO `province_city_district` VALUES (230623, 2306, '林甸县');
INSERT INTO `province_city_district` VALUES (230624, 2306, '杜尔伯特蒙古族自治县');
INSERT INTO `province_city_district` VALUES (230701, 2307, '市辖区');
INSERT INTO `province_city_district` VALUES (230702, 2307, '伊春区');
INSERT INTO `province_city_district` VALUES (230703, 2307, '南岔区');
INSERT INTO `province_city_district` VALUES (230704, 2307, '友好区');
INSERT INTO `province_city_district` VALUES (230705, 2307, '西林区');
INSERT INTO `province_city_district` VALUES (230706, 2307, '翠峦区');
INSERT INTO `province_city_district` VALUES (230707, 2307, '新青区');
INSERT INTO `province_city_district` VALUES (230708, 2307, '美溪区');
INSERT INTO `province_city_district` VALUES (230709, 2307, '金山屯区');
INSERT INTO `province_city_district` VALUES (230710, 2307, '五营区');
INSERT INTO `province_city_district` VALUES (230711, 2307, '乌马河区');
INSERT INTO `province_city_district` VALUES (230712, 2307, '汤旺河区');
INSERT INTO `province_city_district` VALUES (230713, 2307, '带岭区');
INSERT INTO `province_city_district` VALUES (230714, 2307, '乌伊岭区');
INSERT INTO `province_city_district` VALUES (230715, 2307, '红星区');
INSERT INTO `province_city_district` VALUES (230716, 2307, '上甘岭区');
INSERT INTO `province_city_district` VALUES (230722, 2307, '嘉荫县');
INSERT INTO `province_city_district` VALUES (230781, 2307, '铁力市');
INSERT INTO `province_city_district` VALUES (230801, 2308, '市辖区');
INSERT INTO `province_city_district` VALUES (230802, 2308, '永红区');
INSERT INTO `province_city_district` VALUES (230803, 2308, '向阳区');
INSERT INTO `province_city_district` VALUES (230804, 2308, '前进区');
INSERT INTO `province_city_district` VALUES (230805, 2308, '东风区');
INSERT INTO `province_city_district` VALUES (230811, 2308, '郊  区');
INSERT INTO `province_city_district` VALUES (230822, 2308, '桦南县');
INSERT INTO `province_city_district` VALUES (230826, 2308, '桦川县');
INSERT INTO `province_city_district` VALUES (230828, 2308, '汤原县');
INSERT INTO `province_city_district` VALUES (230833, 2308, '抚远县');
INSERT INTO `province_city_district` VALUES (230881, 2308, '同江市');
INSERT INTO `province_city_district` VALUES (230882, 2308, '富锦市');
INSERT INTO `province_city_district` VALUES (230901, 2309, '市辖区');
INSERT INTO `province_city_district` VALUES (230902, 2309, '新兴区');
INSERT INTO `province_city_district` VALUES (230903, 2309, '桃山区');
INSERT INTO `province_city_district` VALUES (230904, 2309, '茄子河区');
INSERT INTO `province_city_district` VALUES (230921, 2309, '勃利县');
INSERT INTO `province_city_district` VALUES (231001, 2310, '市辖区');
INSERT INTO `province_city_district` VALUES (231002, 2310, '东安区');
INSERT INTO `province_city_district` VALUES (231003, 2310, '阳明区');
INSERT INTO `province_city_district` VALUES (231004, 2310, '爱民区');
INSERT INTO `province_city_district` VALUES (231005, 2310, '西安区');
INSERT INTO `province_city_district` VALUES (231024, 2310, '东宁县');
INSERT INTO `province_city_district` VALUES (231025, 2310, '林口县');
INSERT INTO `province_city_district` VALUES (231081, 2310, '绥芬河市');
INSERT INTO `province_city_district` VALUES (231083, 2310, '海林市');
INSERT INTO `province_city_district` VALUES (231084, 2310, '宁安市');
INSERT INTO `province_city_district` VALUES (231085, 2310, '穆棱市');
INSERT INTO `province_city_district` VALUES (231101, 2311, '市辖区');
INSERT INTO `province_city_district` VALUES (231102, 2311, '爱辉区');
INSERT INTO `province_city_district` VALUES (231121, 2311, '嫩江县');
INSERT INTO `province_city_district` VALUES (231123, 2311, '逊克县');
INSERT INTO `province_city_district` VALUES (231124, 2311, '孙吴县');
INSERT INTO `province_city_district` VALUES (231181, 2311, '北安市');
INSERT INTO `province_city_district` VALUES (231182, 2311, '五大连池市');
INSERT INTO `province_city_district` VALUES (231201, 2312, '市辖区');
INSERT INTO `province_city_district` VALUES (231202, 2312, '北林区');
INSERT INTO `province_city_district` VALUES (231221, 2312, '望奎县');
INSERT INTO `province_city_district` VALUES (231222, 2312, '兰西县');
INSERT INTO `province_city_district` VALUES (231223, 2312, '青冈县');
INSERT INTO `province_city_district` VALUES (231224, 2312, '庆安县');
INSERT INTO `province_city_district` VALUES (231225, 2312, '明水县');
INSERT INTO `province_city_district` VALUES (231226, 2312, '绥棱县');
INSERT INTO `province_city_district` VALUES (231281, 2312, '安达市');
INSERT INTO `province_city_district` VALUES (231282, 2312, '肇东市');
INSERT INTO `province_city_district` VALUES (231283, 2312, '海伦市');
INSERT INTO `province_city_district` VALUES (232721, 2327, '呼玛县');
INSERT INTO `province_city_district` VALUES (232722, 2327, '塔河县');
INSERT INTO `province_city_district` VALUES (232723, 2327, '漠河县');
INSERT INTO `province_city_district` VALUES (310101, 3101, '黄浦区');
INSERT INTO `province_city_district` VALUES (310103, 3101, '卢湾区');
INSERT INTO `province_city_district` VALUES (310104, 3101, '徐汇区');
INSERT INTO `province_city_district` VALUES (310105, 3101, '长宁区');
INSERT INTO `province_city_district` VALUES (310106, 3101, '静安区');
INSERT INTO `province_city_district` VALUES (310107, 3101, '普陀区');
INSERT INTO `province_city_district` VALUES (310108, 3101, '闸北区');
INSERT INTO `province_city_district` VALUES (310109, 3101, '虹口区');
INSERT INTO `province_city_district` VALUES (310110, 3101, '杨浦区');
INSERT INTO `province_city_district` VALUES (310112, 3101, '闵行区');
INSERT INTO `province_city_district` VALUES (310113, 3101, '宝山区');
INSERT INTO `province_city_district` VALUES (310114, 3101, '嘉定区');
INSERT INTO `province_city_district` VALUES (310115, 3101, '浦东新区');
INSERT INTO `province_city_district` VALUES (310116, 3101, '金山区');
INSERT INTO `province_city_district` VALUES (310117, 3101, '松江区');
INSERT INTO `province_city_district` VALUES (310118, 3101, '青浦区');
INSERT INTO `province_city_district` VALUES (310225, 3102, '南汇县');
INSERT INTO `province_city_district` VALUES (310226, 3102, '奉贤县');
INSERT INTO `province_city_district` VALUES (310230, 3102, '崇明县');
INSERT INTO `province_city_district` VALUES (320101, 3201, '市辖区');
INSERT INTO `province_city_district` VALUES (320102, 3201, '玄武区');
INSERT INTO `province_city_district` VALUES (320103, 3201, '白下区');
INSERT INTO `province_city_district` VALUES (320104, 3201, '秦淮区');
INSERT INTO `province_city_district` VALUES (320105, 3201, '建邺区');
INSERT INTO `province_city_district` VALUES (320106, 3201, '鼓楼区');
INSERT INTO `province_city_district` VALUES (320107, 3201, '下关区');
INSERT INTO `province_city_district` VALUES (320111, 3201, '浦口区');
INSERT INTO `province_city_district` VALUES (320112, 3201, '大厂区');
INSERT INTO `province_city_district` VALUES (320113, 3201, '栖霞区');
INSERT INTO `province_city_district` VALUES (320114, 3201, '雨花台区');
INSERT INTO `province_city_district` VALUES (320115, 3201, '江宁区');
INSERT INTO `province_city_district` VALUES (320122, 3201, '江浦县');
INSERT INTO `province_city_district` VALUES (320123, 3201, '六合县');
INSERT INTO `province_city_district` VALUES (320124, 3201, '溧水县');
INSERT INTO `province_city_district` VALUES (320125, 3201, '高淳县');
INSERT INTO `province_city_district` VALUES (320201, 3202, '市辖区');
INSERT INTO `province_city_district` VALUES (320202, 3202, '崇安区');
INSERT INTO `province_city_district` VALUES (320203, 3202, '南长区');
INSERT INTO `province_city_district` VALUES (320204, 3202, '北塘区');
INSERT INTO `province_city_district` VALUES (320205, 3202, '锡山区');
INSERT INTO `province_city_district` VALUES (320206, 3202, '惠山区');
INSERT INTO `province_city_district` VALUES (320211, 3202, '滨湖区');
INSERT INTO `province_city_district` VALUES (320281, 3202, '江阴市');
INSERT INTO `province_city_district` VALUES (320282, 3202, '宜兴市');
INSERT INTO `province_city_district` VALUES (320301, 3203, '市辖区');
INSERT INTO `province_city_district` VALUES (320302, 3203, '鼓楼区');
INSERT INTO `province_city_district` VALUES (320303, 3203, '云龙区');
INSERT INTO `province_city_district` VALUES (320304, 3203, '九里区');
INSERT INTO `province_city_district` VALUES (320305, 3203, '贾汪区');
INSERT INTO `province_city_district` VALUES (320311, 3203, '泉山区');
INSERT INTO `province_city_district` VALUES (320321, 3203, '丰  县');
INSERT INTO `province_city_district` VALUES (320322, 3203, '沛  县');
INSERT INTO `province_city_district` VALUES (320323, 3203, '铜山县');
INSERT INTO `province_city_district` VALUES (320324, 3203, '睢宁县');
INSERT INTO `province_city_district` VALUES (320381, 3203, '新沂市');
INSERT INTO `province_city_district` VALUES (320382, 3203, '邳州市');
INSERT INTO `province_city_district` VALUES (320401, 3204, '市辖区');
INSERT INTO `province_city_district` VALUES (320402, 3204, '天宁区');
INSERT INTO `province_city_district` VALUES (320404, 3204, '钟楼区');
INSERT INTO `province_city_district` VALUES (320405, 3204, '戚墅堰区');
INSERT INTO `province_city_district` VALUES (320411, 3204, '郊  区');
INSERT INTO `province_city_district` VALUES (320481, 3204, '溧阳市');
INSERT INTO `province_city_district` VALUES (320482, 3204, '金坛市');
INSERT INTO `province_city_district` VALUES (320483, 3204, '武进市');
INSERT INTO `province_city_district` VALUES (320501, 3205, '市辖区');
INSERT INTO `province_city_district` VALUES (320502, 3205, '沧浪区');
INSERT INTO `province_city_district` VALUES (320503, 3205, '平江区');
INSERT INTO `province_city_district` VALUES (320504, 3205, '金阊区');
INSERT INTO `province_city_district` VALUES (320505, 3205, '虎丘区');
INSERT INTO `province_city_district` VALUES (320506, 3205, '吴中区');
INSERT INTO `province_city_district` VALUES (320507, 3205, '相城区');
INSERT INTO `province_city_district` VALUES (320581, 3205, '常熟市');
INSERT INTO `province_city_district` VALUES (320582, 3205, '张家港市');
INSERT INTO `province_city_district` VALUES (320583, 3205, '昆山市');
INSERT INTO `province_city_district` VALUES (320584, 3205, '吴江市');
INSERT INTO `province_city_district` VALUES (320585, 3205, '太仓市');
INSERT INTO `province_city_district` VALUES (320601, 3206, '市辖区');
INSERT INTO `province_city_district` VALUES (320602, 3206, '崇川区');
INSERT INTO `province_city_district` VALUES (320611, 3206, '港闸区');
INSERT INTO `province_city_district` VALUES (320621, 3206, '海安县');
INSERT INTO `province_city_district` VALUES (320623, 3206, '如东县');
INSERT INTO `province_city_district` VALUES (320681, 3206, '启东市');
INSERT INTO `province_city_district` VALUES (320682, 3206, '如皋市');
INSERT INTO `province_city_district` VALUES (320683, 3206, '通州市');
INSERT INTO `province_city_district` VALUES (320684, 3206, '海门市');
INSERT INTO `province_city_district` VALUES (320701, 3207, '市辖区');
INSERT INTO `province_city_district` VALUES (320703, 3207, '连云区');
INSERT INTO `province_city_district` VALUES (320704, 3207, '云台区');
INSERT INTO `province_city_district` VALUES (320705, 3207, '新浦区');
INSERT INTO `province_city_district` VALUES (320706, 3207, '海州区');
INSERT INTO `province_city_district` VALUES (320721, 3207, '赣榆县');
INSERT INTO `province_city_district` VALUES (320722, 3207, '东海县');
INSERT INTO `province_city_district` VALUES (320723, 3207, '灌云县');
INSERT INTO `province_city_district` VALUES (320724, 3207, '灌南县');
INSERT INTO `province_city_district` VALUES (320801, 3208, '市辖区');
INSERT INTO `province_city_district` VALUES (320802, 3208, '清河区');
INSERT INTO `province_city_district` VALUES (320803, 3208, '楚州区');
INSERT INTO `province_city_district` VALUES (320804, 3208, '淮阴区');
INSERT INTO `province_city_district` VALUES (320811, 3208, '清浦区');
INSERT INTO `province_city_district` VALUES (320826, 3208, '涟水县');
INSERT INTO `province_city_district` VALUES (320829, 3208, '洪泽县');
INSERT INTO `province_city_district` VALUES (320830, 3208, '盱眙县');
INSERT INTO `province_city_district` VALUES (320831, 3208, '金湖县');
INSERT INTO `province_city_district` VALUES (320901, 3209, '市辖区');
INSERT INTO `province_city_district` VALUES (320902, 3209, '城  区');
INSERT INTO `province_city_district` VALUES (320921, 3209, '响水县');
INSERT INTO `province_city_district` VALUES (320922, 3209, '滨海县');
INSERT INTO `province_city_district` VALUES (320923, 3209, '阜宁县');
INSERT INTO `province_city_district` VALUES (320924, 3209, '射阳县');
INSERT INTO `province_city_district` VALUES (320925, 3209, '建湖县');
INSERT INTO `province_city_district` VALUES (320928, 3209, '盐都县');
INSERT INTO `province_city_district` VALUES (320981, 3209, '东台市');
INSERT INTO `province_city_district` VALUES (320982, 3209, '大丰市');
INSERT INTO `province_city_district` VALUES (321001, 3210, '市辖区');
INSERT INTO `province_city_district` VALUES (321002, 3210, '广陵区');
INSERT INTO `province_city_district` VALUES (321003, 3210, '邗江区');
INSERT INTO `province_city_district` VALUES (321011, 3210, '郊  区');
INSERT INTO `province_city_district` VALUES (321023, 3210, '宝应县');
INSERT INTO `province_city_district` VALUES (321081, 3210, '仪征市');
INSERT INTO `province_city_district` VALUES (321084, 3210, '高邮市');
INSERT INTO `province_city_district` VALUES (321088, 3210, '江都市');
INSERT INTO `province_city_district` VALUES (321101, 3211, '市辖区');
INSERT INTO `province_city_district` VALUES (321102, 3211, '京口区');
INSERT INTO `province_city_district` VALUES (321111, 3211, '润州区');
INSERT INTO `province_city_district` VALUES (321121, 3211, '丹徒县');
INSERT INTO `province_city_district` VALUES (321181, 3211, '丹阳市');
INSERT INTO `province_city_district` VALUES (321182, 3211, '扬中市');
INSERT INTO `province_city_district` VALUES (321183, 3211, '句容市');
INSERT INTO `province_city_district` VALUES (321201, 3212, '市辖区');
INSERT INTO `province_city_district` VALUES (321202, 3212, '海陵区');
INSERT INTO `province_city_district` VALUES (321203, 3212, '高港区');
INSERT INTO `province_city_district` VALUES (321281, 3212, '兴化市');
INSERT INTO `province_city_district` VALUES (321282, 3212, '靖江市');
INSERT INTO `province_city_district` VALUES (321283, 3212, '泰兴市');
INSERT INTO `province_city_district` VALUES (321284, 3212, '姜堰市');
INSERT INTO `province_city_district` VALUES (321301, 3213, '市辖区');
INSERT INTO `province_city_district` VALUES (321302, 3213, '宿城区');
INSERT INTO `province_city_district` VALUES (321321, 3213, '宿豫县');
INSERT INTO `province_city_district` VALUES (321322, 3213, '沭阳县');
INSERT INTO `province_city_district` VALUES (321323, 3213, '泗阳县');
INSERT INTO `province_city_district` VALUES (321324, 3213, '泗洪县');
INSERT INTO `province_city_district` VALUES (330101, 3301, '市辖区');
INSERT INTO `province_city_district` VALUES (330102, 3301, '上城区');
INSERT INTO `province_city_district` VALUES (330103, 3301, '下城区');
INSERT INTO `province_city_district` VALUES (330104, 3301, '江干区');
INSERT INTO `province_city_district` VALUES (330105, 3301, '拱墅区');
INSERT INTO `province_city_district` VALUES (330106, 3301, '西湖区');
INSERT INTO `province_city_district` VALUES (330108, 3301, '滨江区');
INSERT INTO `province_city_district` VALUES (330122, 3301, '桐庐县');
INSERT INTO `province_city_district` VALUES (330127, 3301, '淳安县');
INSERT INTO `province_city_district` VALUES (330181, 3301, '萧山市');
INSERT INTO `province_city_district` VALUES (330182, 3301, '建德市');
INSERT INTO `province_city_district` VALUES (330183, 3301, '富阳市');
INSERT INTO `province_city_district` VALUES (330184, 3301, '余杭市');
INSERT INTO `province_city_district` VALUES (330185, 3301, '临安市');
INSERT INTO `province_city_district` VALUES (330201, 3302, '市辖区');
INSERT INTO `province_city_district` VALUES (330203, 3302, '海曙区');
INSERT INTO `province_city_district` VALUES (330204, 3302, '江东区');
INSERT INTO `province_city_district` VALUES (330205, 3302, '江北区');
INSERT INTO `province_city_district` VALUES (330206, 3302, '北仑区');
INSERT INTO `province_city_district` VALUES (330211, 3302, '镇海区');
INSERT INTO `province_city_district` VALUES (330225, 3302, '象山县');
INSERT INTO `province_city_district` VALUES (330226, 3302, '宁海县');
INSERT INTO `province_city_district` VALUES (330227, 3302, '鄞  县');
INSERT INTO `province_city_district` VALUES (330281, 3302, '余姚市');
INSERT INTO `province_city_district` VALUES (330282, 3302, '慈溪市');
INSERT INTO `province_city_district` VALUES (330283, 3302, '奉化市');
INSERT INTO `province_city_district` VALUES (330301, 3303, '市辖区');
INSERT INTO `province_city_district` VALUES (330302, 3303, '鹿城区');
INSERT INTO `province_city_district` VALUES (330303, 3303, '龙湾区');
INSERT INTO `province_city_district` VALUES (330304, 3303, '瓯海区');
INSERT INTO `province_city_district` VALUES (330322, 3303, '洞头县');
INSERT INTO `province_city_district` VALUES (330324, 3303, '永嘉县');
INSERT INTO `province_city_district` VALUES (330326, 3303, '平阳县');
INSERT INTO `province_city_district` VALUES (330327, 3303, '苍南县');
INSERT INTO `province_city_district` VALUES (330328, 3303, '文成县');
INSERT INTO `province_city_district` VALUES (330329, 3303, '泰顺县');
INSERT INTO `province_city_district` VALUES (330381, 3303, '瑞安市');
INSERT INTO `province_city_district` VALUES (330382, 3303, '乐清市');
INSERT INTO `province_city_district` VALUES (330401, 3304, '市辖区');
INSERT INTO `province_city_district` VALUES (330402, 3304, '秀城区');
INSERT INTO `province_city_district` VALUES (330411, 3304, '秀洲区');
INSERT INTO `province_city_district` VALUES (330421, 3304, '嘉善县');
INSERT INTO `province_city_district` VALUES (330424, 3304, '海盐县');
INSERT INTO `province_city_district` VALUES (330481, 3304, '海宁市');
INSERT INTO `province_city_district` VALUES (330482, 3304, '平湖市');
INSERT INTO `province_city_district` VALUES (330483, 3304, '桐乡市');
INSERT INTO `province_city_district` VALUES (330501, 3305, '市辖区');
INSERT INTO `province_city_district` VALUES (330521, 3305, '德清县');
INSERT INTO `province_city_district` VALUES (330522, 3305, '长兴县');
INSERT INTO `province_city_district` VALUES (330523, 3305, '安吉县');
INSERT INTO `province_city_district` VALUES (330601, 3306, '市辖区');
INSERT INTO `province_city_district` VALUES (330602, 3306, '越城区');
INSERT INTO `province_city_district` VALUES (330621, 3306, '绍兴县');
INSERT INTO `province_city_district` VALUES (330624, 3306, '新昌县');
INSERT INTO `province_city_district` VALUES (330681, 3306, '诸暨市');
INSERT INTO `province_city_district` VALUES (330682, 3306, '上虞市');
INSERT INTO `province_city_district` VALUES (330683, 3306, '嵊州市');
INSERT INTO `province_city_district` VALUES (330701, 3307, '市辖区');
INSERT INTO `province_city_district` VALUES (330702, 3307, '婺城区');
INSERT INTO `province_city_district` VALUES (330703, 3307, '金东区');
INSERT INTO `province_city_district` VALUES (330723, 3307, '武义县');
INSERT INTO `province_city_district` VALUES (330726, 3307, '浦江县');
INSERT INTO `province_city_district` VALUES (330727, 3307, '磐安县');
INSERT INTO `province_city_district` VALUES (330781, 3307, '兰溪市');
INSERT INTO `province_city_district` VALUES (330782, 3307, '义乌市');
INSERT INTO `province_city_district` VALUES (330783, 3307, '东阳市');
INSERT INTO `province_city_district` VALUES (330784, 3307, '永康市');
INSERT INTO `province_city_district` VALUES (330801, 3308, '市辖区');
INSERT INTO `province_city_district` VALUES (330802, 3308, '柯城区');
INSERT INTO `province_city_district` VALUES (330821, 3308, '衢  县');
INSERT INTO `province_city_district` VALUES (330822, 3308, '常山县');
INSERT INTO `province_city_district` VALUES (330824, 3308, '开化县');
INSERT INTO `province_city_district` VALUES (330825, 3308, '龙游县');
INSERT INTO `province_city_district` VALUES (330881, 3308, '江山市');
INSERT INTO `province_city_district` VALUES (330901, 3309, '市辖区');
INSERT INTO `province_city_district` VALUES (330902, 3309, '定海区');
INSERT INTO `province_city_district` VALUES (330903, 3309, '普陀区');
INSERT INTO `province_city_district` VALUES (330921, 3309, '岱山县');
INSERT INTO `province_city_district` VALUES (330922, 3309, '嵊泗县');
INSERT INTO `province_city_district` VALUES (331001, 3310, '市辖区');
INSERT INTO `province_city_district` VALUES (331002, 3310, '椒江区');
INSERT INTO `province_city_district` VALUES (331003, 3310, '黄岩区');
INSERT INTO `province_city_district` VALUES (331004, 3310, '路桥区');
INSERT INTO `province_city_district` VALUES (331021, 3310, '玉环县');
INSERT INTO `province_city_district` VALUES (331022, 3310, '三门县');
INSERT INTO `province_city_district` VALUES (331023, 3310, '天台县');
INSERT INTO `province_city_district` VALUES (331024, 3310, '仙居县');
INSERT INTO `province_city_district` VALUES (331081, 3310, '温岭市');
INSERT INTO `province_city_district` VALUES (331082, 3310, '临海市');
INSERT INTO `province_city_district` VALUES (331101, 3311, '市辖区');
INSERT INTO `province_city_district` VALUES (331102, 3311, '莲都区');
INSERT INTO `province_city_district` VALUES (331121, 3311, '青田县');
INSERT INTO `province_city_district` VALUES (331122, 3311, '缙云县');
INSERT INTO `province_city_district` VALUES (331123, 3311, '遂昌县');
INSERT INTO `province_city_district` VALUES (331124, 3311, '松阳县');
INSERT INTO `province_city_district` VALUES (331125, 3311, '云和县');
INSERT INTO `province_city_district` VALUES (331126, 3311, '庆元县');
INSERT INTO `province_city_district` VALUES (331127, 3311, '景宁畲族自治县');
INSERT INTO `province_city_district` VALUES (331181, 3311, '龙泉市');
INSERT INTO `province_city_district` VALUES (340101, 3401, '市辖区');
INSERT INTO `province_city_district` VALUES (340102, 3401, '东市区');
INSERT INTO `province_city_district` VALUES (340103, 3401, '中市区');
INSERT INTO `province_city_district` VALUES (340104, 3401, '西市区');
INSERT INTO `province_city_district` VALUES (340111, 3401, '郊  区');
INSERT INTO `province_city_district` VALUES (340121, 3401, '长丰县');
INSERT INTO `province_city_district` VALUES (340122, 3401, '肥东县');
INSERT INTO `province_city_district` VALUES (340123, 3401, '肥西县');
INSERT INTO `province_city_district` VALUES (340201, 3402, '市辖区');
INSERT INTO `province_city_district` VALUES (340202, 3402, '镜湖区');
INSERT INTO `province_city_district` VALUES (340203, 3402, '马塘区');
INSERT INTO `province_city_district` VALUES (340204, 3402, '新芜区');
INSERT INTO `province_city_district` VALUES (340207, 3402, '鸠江区');
INSERT INTO `province_city_district` VALUES (340221, 3402, '芜湖县');
INSERT INTO `province_city_district` VALUES (340222, 3402, '繁昌县');
INSERT INTO `province_city_district` VALUES (340223, 3402, '南陵县');
INSERT INTO `province_city_district` VALUES (340301, 3403, '市辖区');
INSERT INTO `province_city_district` VALUES (340302, 3403, '东市区');
INSERT INTO `province_city_district` VALUES (340303, 3403, '中市区');
INSERT INTO `province_city_district` VALUES (340304, 3403, '西市区');
INSERT INTO `province_city_district` VALUES (340311, 3403, '郊  区');
INSERT INTO `province_city_district` VALUES (340321, 3403, '怀远县');
INSERT INTO `province_city_district` VALUES (340322, 3403, '五河县');
INSERT INTO `province_city_district` VALUES (340323, 3403, '固镇县');
INSERT INTO `province_city_district` VALUES (340401, 3404, '市辖区');
INSERT INTO `province_city_district` VALUES (340402, 3404, '大通区');
INSERT INTO `province_city_district` VALUES (340403, 3404, '田家庵区');
INSERT INTO `province_city_district` VALUES (340404, 3404, '谢家集区');
INSERT INTO `province_city_district` VALUES (340405, 3404, '八公山区');
INSERT INTO `province_city_district` VALUES (340406, 3404, '潘集区');
INSERT INTO `province_city_district` VALUES (340421, 3404, '凤台县');
INSERT INTO `province_city_district` VALUES (340501, 3405, '市辖区');
INSERT INTO `province_city_district` VALUES (340502, 3405, '金家庄区');
INSERT INTO `province_city_district` VALUES (340503, 3405, '花山区');
INSERT INTO `province_city_district` VALUES (340504, 3405, '雨山区');
INSERT INTO `province_city_district` VALUES (340505, 3405, '向山区');
INSERT INTO `province_city_district` VALUES (340521, 3405, '当涂县');
INSERT INTO `province_city_district` VALUES (340601, 3406, '市辖区');
INSERT INTO `province_city_district` VALUES (340602, 3406, '杜集区');
INSERT INTO `province_city_district` VALUES (340603, 3406, '相山区');
INSERT INTO `province_city_district` VALUES (340604, 3406, '烈山区');
INSERT INTO `province_city_district` VALUES (340621, 3406, '濉溪县');
INSERT INTO `province_city_district` VALUES (340701, 3407, '市辖区');
INSERT INTO `province_city_district` VALUES (340702, 3407, '铜官山区');
INSERT INTO `province_city_district` VALUES (340703, 3407, '狮子山区');
INSERT INTO `province_city_district` VALUES (340711, 3407, '郊  区');
INSERT INTO `province_city_district` VALUES (340721, 3407, '铜陵县');
INSERT INTO `province_city_district` VALUES (340801, 3408, '市辖区');
INSERT INTO `province_city_district` VALUES (340802, 3408, '迎江区');
INSERT INTO `province_city_district` VALUES (340803, 3408, '大观区');
INSERT INTO `province_city_district` VALUES (340811, 3408, '郊  区');
INSERT INTO `province_city_district` VALUES (340822, 3408, '怀宁县');
INSERT INTO `province_city_district` VALUES (340823, 3408, '枞阳县');
INSERT INTO `province_city_district` VALUES (340824, 3408, '潜山县');
INSERT INTO `province_city_district` VALUES (340825, 3408, '太湖县');
INSERT INTO `province_city_district` VALUES (340826, 3408, '宿松县');
INSERT INTO `province_city_district` VALUES (340827, 3408, '望江县');
INSERT INTO `province_city_district` VALUES (340828, 3408, '岳西县');
INSERT INTO `province_city_district` VALUES (340881, 3408, '桐城市');
INSERT INTO `province_city_district` VALUES (341001, 3410, '市辖区');
INSERT INTO `province_city_district` VALUES (341002, 3410, '屯溪区');
INSERT INTO `province_city_district` VALUES (341003, 3410, '黄山区');
INSERT INTO `province_city_district` VALUES (341004, 3410, '徽州区');
INSERT INTO `province_city_district` VALUES (341021, 3410, '歙  县');
INSERT INTO `province_city_district` VALUES (341022, 3410, '休宁县');
INSERT INTO `province_city_district` VALUES (341023, 3410, '黟  县');
INSERT INTO `province_city_district` VALUES (341024, 3410, '祁门县');
INSERT INTO `province_city_district` VALUES (341101, 3411, '市辖区');
INSERT INTO `province_city_district` VALUES (341102, 3411, '琅琊区');
INSERT INTO `province_city_district` VALUES (341103, 3411, '南谯区');
INSERT INTO `province_city_district` VALUES (341122, 3411, '来安县');
INSERT INTO `province_city_district` VALUES (341124, 3411, '全椒县');
INSERT INTO `province_city_district` VALUES (341125, 3411, '定远县');
INSERT INTO `province_city_district` VALUES (341126, 3411, '凤阳县');
INSERT INTO `province_city_district` VALUES (341181, 3411, '天长市');
INSERT INTO `province_city_district` VALUES (341182, 3411, '明光市');
INSERT INTO `province_city_district` VALUES (341201, 3412, '市辖区');
INSERT INTO `province_city_district` VALUES (341202, 3412, '颍州区');
INSERT INTO `province_city_district` VALUES (341203, 3412, '颍东区');
INSERT INTO `province_city_district` VALUES (341204, 3412, '颍泉区');
INSERT INTO `province_city_district` VALUES (341221, 3412, '临泉县');
INSERT INTO `province_city_district` VALUES (341222, 3412, '太和县');
INSERT INTO `province_city_district` VALUES (341225, 3412, '阜南县');
INSERT INTO `province_city_district` VALUES (341226, 3412, '颍上县');
INSERT INTO `province_city_district` VALUES (341282, 3412, '界首市');
INSERT INTO `province_city_district` VALUES (341301, 3413, '市辖区');
INSERT INTO `province_city_district` VALUES (341302, 3413, '墉桥区');
INSERT INTO `province_city_district` VALUES (341321, 3413, '砀山县');
INSERT INTO `province_city_district` VALUES (341322, 3413, '萧  县');
INSERT INTO `province_city_district` VALUES (341323, 3413, '灵璧县');
INSERT INTO `province_city_district` VALUES (341324, 3413, '泗  县');
INSERT INTO `province_city_district` VALUES (341401, 3414, '市辖区');
INSERT INTO `province_city_district` VALUES (341402, 3414, '居巢区');
INSERT INTO `province_city_district` VALUES (341421, 3414, '庐江县');
INSERT INTO `province_city_district` VALUES (341422, 3414, '无为县');
INSERT INTO `province_city_district` VALUES (341423, 3414, '含山县');
INSERT INTO `province_city_district` VALUES (341424, 3414, '和  县');
INSERT INTO `province_city_district` VALUES (341501, 3415, '市辖区');
INSERT INTO `province_city_district` VALUES (341502, 3415, '金安区');
INSERT INTO `province_city_district` VALUES (341503, 3415, '裕安区');
INSERT INTO `province_city_district` VALUES (341521, 3415, '寿  县');
INSERT INTO `province_city_district` VALUES (341522, 3415, '霍邱县');
INSERT INTO `province_city_district` VALUES (341523, 3415, '舒城县');
INSERT INTO `province_city_district` VALUES (341524, 3415, '金寨县');
INSERT INTO `province_city_district` VALUES (341525, 3415, '霍山县');
INSERT INTO `province_city_district` VALUES (341601, 3416, '市辖区');
INSERT INTO `province_city_district` VALUES (341602, 3416, '谯城区');
INSERT INTO `province_city_district` VALUES (341621, 3416, '涡阳县');
INSERT INTO `province_city_district` VALUES (341622, 3416, '蒙城县');
INSERT INTO `province_city_district` VALUES (341623, 3416, '利辛县');
INSERT INTO `province_city_district` VALUES (341701, 3417, '市辖区');
INSERT INTO `province_city_district` VALUES (341702, 3417, '贵池区');
INSERT INTO `province_city_district` VALUES (341721, 3417, '东至县');
INSERT INTO `province_city_district` VALUES (341722, 3417, '石台县');
INSERT INTO `province_city_district` VALUES (341723, 3417, '青阳县');
INSERT INTO `province_city_district` VALUES (341801, 3418, '市辖区');
INSERT INTO `province_city_district` VALUES (341802, 3418, '宣州区');
INSERT INTO `province_city_district` VALUES (341821, 3418, '郎溪县');
INSERT INTO `province_city_district` VALUES (341822, 3418, '广德县');
INSERT INTO `province_city_district` VALUES (341823, 3418, '泾  县');
INSERT INTO `province_city_district` VALUES (341824, 3418, '绩溪县');
INSERT INTO `province_city_district` VALUES (341825, 3418, '旌德县');
INSERT INTO `province_city_district` VALUES (341881, 3418, '宁国市');
INSERT INTO `province_city_district` VALUES (350101, 3501, '市辖区');
INSERT INTO `province_city_district` VALUES (350102, 3501, '鼓楼区');
INSERT INTO `province_city_district` VALUES (350103, 3501, '台江区');
INSERT INTO `province_city_district` VALUES (350104, 3501, '仓山区');
INSERT INTO `province_city_district` VALUES (350105, 3501, '马尾区');
INSERT INTO `province_city_district` VALUES (350111, 3501, '晋安区');
INSERT INTO `province_city_district` VALUES (350121, 3501, '闽侯县');
INSERT INTO `province_city_district` VALUES (350122, 3501, '连江县');
INSERT INTO `province_city_district` VALUES (350123, 3501, '罗源县');
INSERT INTO `province_city_district` VALUES (350124, 3501, '闽清县');
INSERT INTO `province_city_district` VALUES (350125, 3501, '永泰县');
INSERT INTO `province_city_district` VALUES (350128, 3501, '平潭县');
INSERT INTO `province_city_district` VALUES (350181, 3501, '福清市');
INSERT INTO `province_city_district` VALUES (350182, 3501, '长乐市');
INSERT INTO `province_city_district` VALUES (350201, 3502, '市辖区');
INSERT INTO `province_city_district` VALUES (350202, 3502, '鼓浪屿区');
INSERT INTO `province_city_district` VALUES (350203, 3502, '思明区');
INSERT INTO `province_city_district` VALUES (350204, 3502, '开元区');
INSERT INTO `province_city_district` VALUES (350205, 3502, '杏林区');
INSERT INTO `province_city_district` VALUES (350206, 3502, '湖里区');
INSERT INTO `province_city_district` VALUES (350211, 3502, '集美区');
INSERT INTO `province_city_district` VALUES (350212, 3502, '同安区');
INSERT INTO `province_city_district` VALUES (350301, 3503, '市辖区');
INSERT INTO `province_city_district` VALUES (350302, 3503, '城厢区');
INSERT INTO `province_city_district` VALUES (350303, 3503, '涵江区');
INSERT INTO `province_city_district` VALUES (350321, 3503, '莆田县');
INSERT INTO `province_city_district` VALUES (350322, 3503, '仙游县');
INSERT INTO `province_city_district` VALUES (350401, 3504, '市辖区');
INSERT INTO `province_city_district` VALUES (350402, 3504, '梅列区');
INSERT INTO `province_city_district` VALUES (350403, 3504, '三元区');
INSERT INTO `province_city_district` VALUES (350421, 3504, '明溪县');
INSERT INTO `province_city_district` VALUES (350423, 3504, '清流县');
INSERT INTO `province_city_district` VALUES (350424, 3504, '宁化县');
INSERT INTO `province_city_district` VALUES (350425, 3504, '大田县');
INSERT INTO `province_city_district` VALUES (350426, 3504, '尤溪县');
INSERT INTO `province_city_district` VALUES (350427, 3504, '沙  县');
INSERT INTO `province_city_district` VALUES (350428, 3504, '将乐县');
INSERT INTO `province_city_district` VALUES (350429, 3504, '泰宁县');
INSERT INTO `province_city_district` VALUES (350430, 3504, '建宁县');
INSERT INTO `province_city_district` VALUES (350481, 3504, '永安市');
INSERT INTO `province_city_district` VALUES (350501, 3505, '市辖区');
INSERT INTO `province_city_district` VALUES (350502, 3505, '鲤城区');
INSERT INTO `province_city_district` VALUES (350503, 3505, '丰泽区');
INSERT INTO `province_city_district` VALUES (350504, 3505, '洛江区');
INSERT INTO `province_city_district` VALUES (350505, 3505, '泉港区');
INSERT INTO `province_city_district` VALUES (350521, 3505, '惠安县');
INSERT INTO `province_city_district` VALUES (350524, 3505, '安溪县');
INSERT INTO `province_city_district` VALUES (350525, 3505, '永春县');
INSERT INTO `province_city_district` VALUES (350526, 3505, '德化县');
INSERT INTO `province_city_district` VALUES (350527, 3505, '金门县');
INSERT INTO `province_city_district` VALUES (350581, 3505, '石狮市');
INSERT INTO `province_city_district` VALUES (350582, 3505, '晋江市');
INSERT INTO `province_city_district` VALUES (350583, 3505, '南安市');
INSERT INTO `province_city_district` VALUES (350601, 3506, '市辖区');
INSERT INTO `province_city_district` VALUES (350602, 3506, '芗城区');
INSERT INTO `province_city_district` VALUES (350603, 3506, '龙文区');
INSERT INTO `province_city_district` VALUES (350622, 3506, '云霄县');
INSERT INTO `province_city_district` VALUES (350623, 3506, '漳浦县');
INSERT INTO `province_city_district` VALUES (350624, 3506, '诏安县');
INSERT INTO `province_city_district` VALUES (350625, 3506, '长泰县');
INSERT INTO `province_city_district` VALUES (350626, 3506, '东山县');
INSERT INTO `province_city_district` VALUES (350627, 3506, '南靖县');
INSERT INTO `province_city_district` VALUES (350628, 3506, '平和县');
INSERT INTO `province_city_district` VALUES (350629, 3506, '华安县');
INSERT INTO `province_city_district` VALUES (350681, 3506, '龙海市');
INSERT INTO `province_city_district` VALUES (350701, 3507, '市辖区');
INSERT INTO `province_city_district` VALUES (350702, 3507, '延平区');
INSERT INTO `province_city_district` VALUES (350721, 3507, '顺昌县');
INSERT INTO `province_city_district` VALUES (350722, 3507, '浦城县');
INSERT INTO `province_city_district` VALUES (350723, 3507, '光泽县');
INSERT INTO `province_city_district` VALUES (350724, 3507, '松溪县');
INSERT INTO `province_city_district` VALUES (350725, 3507, '政和县');
INSERT INTO `province_city_district` VALUES (350781, 3507, '邵武市');
INSERT INTO `province_city_district` VALUES (350782, 3507, '武夷山市');
INSERT INTO `province_city_district` VALUES (350783, 3507, '建瓯市');
INSERT INTO `province_city_district` VALUES (350784, 3507, '建阳市');
INSERT INTO `province_city_district` VALUES (350801, 3508, '市辖区');
INSERT INTO `province_city_district` VALUES (350802, 3508, '新罗区');
INSERT INTO `province_city_district` VALUES (350821, 3508, '长汀县');
INSERT INTO `province_city_district` VALUES (350822, 3508, '永定县');
INSERT INTO `province_city_district` VALUES (350823, 3508, '上杭县');
INSERT INTO `province_city_district` VALUES (350824, 3508, '武平县');
INSERT INTO `province_city_district` VALUES (350825, 3508, '连城县');
INSERT INTO `province_city_district` VALUES (350881, 3508, '漳平市');
INSERT INTO `province_city_district` VALUES (350901, 3509, '市辖区');
INSERT INTO `province_city_district` VALUES (350902, 3509, '蕉城区');
INSERT INTO `province_city_district` VALUES (350921, 3509, '霞浦县');
INSERT INTO `province_city_district` VALUES (350922, 3509, '古田县');
INSERT INTO `province_city_district` VALUES (350923, 3509, '屏南县');
INSERT INTO `province_city_district` VALUES (350924, 3509, '寿宁县');
INSERT INTO `province_city_district` VALUES (350925, 3509, '周宁县');
INSERT INTO `province_city_district` VALUES (350926, 3509, '柘荣县');
INSERT INTO `province_city_district` VALUES (350981, 3509, '福安市');
INSERT INTO `province_city_district` VALUES (350982, 3509, '福鼎市');
INSERT INTO `province_city_district` VALUES (360101, 3601, '市辖区');
INSERT INTO `province_city_district` VALUES (360102, 3601, '东湖区');
INSERT INTO `province_city_district` VALUES (360103, 3601, '西湖区');
INSERT INTO `province_city_district` VALUES (360104, 3601, '青云谱区');
INSERT INTO `province_city_district` VALUES (360105, 3601, '湾里区');
INSERT INTO `province_city_district` VALUES (360111, 3601, '郊  区');
INSERT INTO `province_city_district` VALUES (360121, 3601, '南昌县');
INSERT INTO `province_city_district` VALUES (360122, 3601, '新建县');
INSERT INTO `province_city_district` VALUES (360123, 3601, '安义县');
INSERT INTO `province_city_district` VALUES (360124, 3601, '进贤县');
INSERT INTO `province_city_district` VALUES (360201, 3602, '市辖区');
INSERT INTO `province_city_district` VALUES (360202, 3602, '昌江区');
INSERT INTO `province_city_district` VALUES (360203, 3602, '珠山区');
INSERT INTO `province_city_district` VALUES (360222, 3602, '浮梁县');
INSERT INTO `province_city_district` VALUES (360281, 3602, '乐平市');
INSERT INTO `province_city_district` VALUES (360301, 3603, '市辖区');
INSERT INTO `province_city_district` VALUES (360302, 3603, '安源区');
INSERT INTO `province_city_district` VALUES (360313, 3603, '湘东区');
INSERT INTO `province_city_district` VALUES (360321, 3603, '莲花县');
INSERT INTO `province_city_district` VALUES (360322, 3603, '上栗县');
INSERT INTO `province_city_district` VALUES (360323, 3603, '芦溪县');
INSERT INTO `province_city_district` VALUES (360401, 3604, '市辖区');
INSERT INTO `province_city_district` VALUES (360402, 3604, '庐山区');
INSERT INTO `province_city_district` VALUES (360403, 3604, '浔阳区');
INSERT INTO `province_city_district` VALUES (360421, 3604, '九江县');
INSERT INTO `province_city_district` VALUES (360423, 3604, '武宁县');
INSERT INTO `province_city_district` VALUES (360424, 3604, '修水县');
INSERT INTO `province_city_district` VALUES (360425, 3604, '永修县');
INSERT INTO `province_city_district` VALUES (360426, 3604, '德安县');
INSERT INTO `province_city_district` VALUES (360427, 3604, '星子县');
INSERT INTO `province_city_district` VALUES (360428, 3604, '都昌县');
INSERT INTO `province_city_district` VALUES (360429, 3604, '湖口县');
INSERT INTO `province_city_district` VALUES (360430, 3604, '彭泽县');
INSERT INTO `province_city_district` VALUES (360481, 3604, '瑞昌市');
INSERT INTO `province_city_district` VALUES (360501, 3605, '市辖区');
INSERT INTO `province_city_district` VALUES (360502, 3605, '渝水区');
INSERT INTO `province_city_district` VALUES (360521, 3605, '分宜县');
INSERT INTO `province_city_district` VALUES (360601, 3606, '市辖区');
INSERT INTO `province_city_district` VALUES (360602, 3606, '月湖区');
INSERT INTO `province_city_district` VALUES (360622, 3606, '余江县');
INSERT INTO `province_city_district` VALUES (360681, 3606, '贵溪市');
INSERT INTO `province_city_district` VALUES (360701, 3607, '市辖区');
INSERT INTO `province_city_district` VALUES (360702, 3607, '章贡区');
INSERT INTO `province_city_district` VALUES (360721, 3607, '赣  县');
INSERT INTO `province_city_district` VALUES (360722, 3607, '信丰县');
INSERT INTO `province_city_district` VALUES (360723, 3607, '大余县');
INSERT INTO `province_city_district` VALUES (360724, 3607, '上犹县');
INSERT INTO `province_city_district` VALUES (360725, 3607, '崇义县');
INSERT INTO `province_city_district` VALUES (360726, 3607, '安远县');
INSERT INTO `province_city_district` VALUES (360727, 3607, '龙南县');
INSERT INTO `province_city_district` VALUES (360728, 3607, '定南县');
INSERT INTO `province_city_district` VALUES (360729, 3607, '全南县');
INSERT INTO `province_city_district` VALUES (360730, 3607, '宁都县');
INSERT INTO `province_city_district` VALUES (360731, 3607, '于都县');
INSERT INTO `province_city_district` VALUES (360732, 3607, '兴国县');
INSERT INTO `province_city_district` VALUES (360733, 3607, '会昌县');
INSERT INTO `province_city_district` VALUES (360734, 3607, '寻乌县');
INSERT INTO `province_city_district` VALUES (360735, 3607, '石城县');
INSERT INTO `province_city_district` VALUES (360781, 3607, '瑞金市');
INSERT INTO `province_city_district` VALUES (360782, 3607, '南康市');
INSERT INTO `province_city_district` VALUES (360801, 3608, '市辖区');
INSERT INTO `province_city_district` VALUES (360802, 3608, '吉州区');
INSERT INTO `province_city_district` VALUES (360803, 3608, '青原区');
INSERT INTO `province_city_district` VALUES (360821, 3608, '吉安县');
INSERT INTO `province_city_district` VALUES (360822, 3608, '吉水县');
INSERT INTO `province_city_district` VALUES (360823, 3608, '峡江县');
INSERT INTO `province_city_district` VALUES (360824, 3608, '新干县');
INSERT INTO `province_city_district` VALUES (360825, 3608, '永丰县');
INSERT INTO `province_city_district` VALUES (360826, 3608, '泰和县');
INSERT INTO `province_city_district` VALUES (360827, 3608, '遂川县');
INSERT INTO `province_city_district` VALUES (360828, 3608, '万安县');
INSERT INTO `province_city_district` VALUES (360829, 3608, '安福县');
INSERT INTO `province_city_district` VALUES (360830, 3608, '永新县');
INSERT INTO `province_city_district` VALUES (360881, 3608, '井冈山市');
INSERT INTO `province_city_district` VALUES (360901, 3609, '市辖区');
INSERT INTO `province_city_district` VALUES (360902, 3609, '袁州区');
INSERT INTO `province_city_district` VALUES (360921, 3609, '奉新县');
INSERT INTO `province_city_district` VALUES (360922, 3609, '万载县');
INSERT INTO `province_city_district` VALUES (360923, 3609, '上高县');
INSERT INTO `province_city_district` VALUES (360924, 3609, '宜丰县');
INSERT INTO `province_city_district` VALUES (360925, 3609, '靖安县');
INSERT INTO `province_city_district` VALUES (360926, 3609, '铜鼓县');
INSERT INTO `province_city_district` VALUES (360981, 3609, '丰城市');
INSERT INTO `province_city_district` VALUES (360982, 3609, '樟树市');
INSERT INTO `province_city_district` VALUES (360983, 3609, '高安市');
INSERT INTO `province_city_district` VALUES (361001, 3610, '市辖区');
INSERT INTO `province_city_district` VALUES (361002, 3610, '临川区');
INSERT INTO `province_city_district` VALUES (361021, 3610, '南城县');
INSERT INTO `province_city_district` VALUES (361022, 3610, '黎川县');
INSERT INTO `province_city_district` VALUES (361023, 3610, '南丰县');
INSERT INTO `province_city_district` VALUES (361024, 3610, '崇仁县');
INSERT INTO `province_city_district` VALUES (361025, 3610, '乐安县');
INSERT INTO `province_city_district` VALUES (361026, 3610, '宜黄县');
INSERT INTO `province_city_district` VALUES (361027, 3610, '金溪县');
INSERT INTO `province_city_district` VALUES (361028, 3610, '资溪县');
INSERT INTO `province_city_district` VALUES (361029, 3610, '东乡县');
INSERT INTO `province_city_district` VALUES (361030, 3610, '广昌县');
INSERT INTO `province_city_district` VALUES (361101, 3611, '市辖区');
INSERT INTO `province_city_district` VALUES (361102, 3611, '信州区');
INSERT INTO `province_city_district` VALUES (361121, 3611, '上饶县');
INSERT INTO `province_city_district` VALUES (361122, 3611, '广丰县');
INSERT INTO `province_city_district` VALUES (361123, 3611, '玉山县');
INSERT INTO `province_city_district` VALUES (361124, 3611, '铅山县');
INSERT INTO `province_city_district` VALUES (361125, 3611, '横峰县');
INSERT INTO `province_city_district` VALUES (361126, 3611, '弋阳县');
INSERT INTO `province_city_district` VALUES (361127, 3611, '余干县');
INSERT INTO `province_city_district` VALUES (361128, 3611, '波阳县');
INSERT INTO `province_city_district` VALUES (361129, 3611, '万年县');
INSERT INTO `province_city_district` VALUES (361130, 3611, '婺源县');
INSERT INTO `province_city_district` VALUES (361181, 3611, '德兴市');
INSERT INTO `province_city_district` VALUES (370101, 3701, '市辖区');
INSERT INTO `province_city_district` VALUES (370102, 3701, '历下区');
INSERT INTO `province_city_district` VALUES (370103, 3701, '市中区');
INSERT INTO `province_city_district` VALUES (370104, 3701, '槐荫区');
INSERT INTO `province_city_district` VALUES (370105, 3701, '天桥区');
INSERT INTO `province_city_district` VALUES (370112, 3701, '历城区');
INSERT INTO `province_city_district` VALUES (370123, 3701, '长清县');
INSERT INTO `province_city_district` VALUES (370124, 3701, '平阴县');
INSERT INTO `province_city_district` VALUES (370125, 3701, '济阳县');
INSERT INTO `province_city_district` VALUES (370126, 3701, '商河县');
INSERT INTO `province_city_district` VALUES (370181, 3701, '章丘市');
INSERT INTO `province_city_district` VALUES (370201, 3702, '市辖区');
INSERT INTO `province_city_district` VALUES (370202, 3702, '市南区');
INSERT INTO `province_city_district` VALUES (370203, 3702, '市北区');
INSERT INTO `province_city_district` VALUES (370205, 3702, '四方区');
INSERT INTO `province_city_district` VALUES (370211, 3702, '黄岛区');
INSERT INTO `province_city_district` VALUES (370212, 3702, '崂山区');
INSERT INTO `province_city_district` VALUES (370213, 3702, '李沧区');
INSERT INTO `province_city_district` VALUES (370214, 3702, '城阳区');
INSERT INTO `province_city_district` VALUES (370281, 3702, '胶州市');
INSERT INTO `province_city_district` VALUES (370282, 3702, '即墨市');
INSERT INTO `province_city_district` VALUES (370283, 3702, '平度市');
INSERT INTO `province_city_district` VALUES (370284, 3702, '胶南市');
INSERT INTO `province_city_district` VALUES (370285, 3702, '莱西市');
INSERT INTO `province_city_district` VALUES (370301, 3703, '市辖区');
INSERT INTO `province_city_district` VALUES (370302, 3703, '淄川区');
INSERT INTO `province_city_district` VALUES (370303, 3703, '张店区');
INSERT INTO `province_city_district` VALUES (370304, 3703, '博山区');
INSERT INTO `province_city_district` VALUES (370305, 3703, '临淄区');
INSERT INTO `province_city_district` VALUES (370306, 3703, '周村区');
INSERT INTO `province_city_district` VALUES (370321, 3703, '桓台县');
INSERT INTO `province_city_district` VALUES (370322, 3703, '高青县');
INSERT INTO `province_city_district` VALUES (370323, 3703, '沂源县');
INSERT INTO `province_city_district` VALUES (370401, 3704, '市辖区');
INSERT INTO `province_city_district` VALUES (370402, 3704, '市中区');
INSERT INTO `province_city_district` VALUES (370403, 3704, '薛城区');
INSERT INTO `province_city_district` VALUES (370404, 3704, '峄城区');
INSERT INTO `province_city_district` VALUES (370405, 3704, '台儿庄区');
INSERT INTO `province_city_district` VALUES (370406, 3704, '山亭区');
INSERT INTO `province_city_district` VALUES (370481, 3704, '滕州市');
INSERT INTO `province_city_district` VALUES (370501, 3705, '市辖区');
INSERT INTO `province_city_district` VALUES (370502, 3705, '东营区');
INSERT INTO `province_city_district` VALUES (370503, 3705, '河口区');
INSERT INTO `province_city_district` VALUES (370521, 3705, '垦利县');
INSERT INTO `province_city_district` VALUES (370522, 3705, '利津县');
INSERT INTO `province_city_district` VALUES (370523, 3705, '广饶县');
INSERT INTO `province_city_district` VALUES (370601, 3706, '市辖区');
INSERT INTO `province_city_district` VALUES (370602, 3706, '芝罘区');
INSERT INTO `province_city_district` VALUES (370611, 3706, '福山区');
INSERT INTO `province_city_district` VALUES (370612, 3706, '牟平区');
INSERT INTO `province_city_district` VALUES (370613, 3706, '莱山区');
INSERT INTO `province_city_district` VALUES (370634, 3706, '长岛县');
INSERT INTO `province_city_district` VALUES (370681, 3706, '龙口市');
INSERT INTO `province_city_district` VALUES (370682, 3706, '莱阳市');
INSERT INTO `province_city_district` VALUES (370683, 3706, '莱州市');
INSERT INTO `province_city_district` VALUES (370684, 3706, '蓬莱市');
INSERT INTO `province_city_district` VALUES (370685, 3706, '招远市');
INSERT INTO `province_city_district` VALUES (370686, 3706, '栖霞市');
INSERT INTO `province_city_district` VALUES (370687, 3706, '海阳市');
INSERT INTO `province_city_district` VALUES (370701, 3707, '市辖区');
INSERT INTO `province_city_district` VALUES (370702, 3707, '潍城区');
INSERT INTO `province_city_district` VALUES (370703, 3707, '寒亭区');
INSERT INTO `province_city_district` VALUES (370704, 3707, '坊子区');
INSERT INTO `province_city_district` VALUES (370705, 3707, '奎文区');
INSERT INTO `province_city_district` VALUES (370724, 3707, '临朐县');
INSERT INTO `province_city_district` VALUES (370725, 3707, '昌乐县');
INSERT INTO `province_city_district` VALUES (370781, 3707, '青州市');
INSERT INTO `province_city_district` VALUES (370782, 3707, '诸城市');
INSERT INTO `province_city_district` VALUES (370783, 3707, '寿光市');
INSERT INTO `province_city_district` VALUES (370784, 3707, '安丘市');
INSERT INTO `province_city_district` VALUES (370785, 3707, '高密市');
INSERT INTO `province_city_district` VALUES (370786, 3707, '昌邑市');
INSERT INTO `province_city_district` VALUES (370801, 3708, '市辖区');
INSERT INTO `province_city_district` VALUES (370802, 3708, '市中区');
INSERT INTO `province_city_district` VALUES (370811, 3708, '任城区');
INSERT INTO `province_city_district` VALUES (370826, 3708, '微山县');
INSERT INTO `province_city_district` VALUES (370827, 3708, '鱼台县');
INSERT INTO `province_city_district` VALUES (370828, 3708, '金乡县');
INSERT INTO `province_city_district` VALUES (370829, 3708, '嘉祥县');
INSERT INTO `province_city_district` VALUES (370830, 3708, '汶上县');
INSERT INTO `province_city_district` VALUES (370831, 3708, '泗水县');
INSERT INTO `province_city_district` VALUES (370832, 3708, '梁山县');
INSERT INTO `province_city_district` VALUES (370881, 3708, '曲阜市');
INSERT INTO `province_city_district` VALUES (370882, 3708, '兖州市');
INSERT INTO `province_city_district` VALUES (370883, 3708, '邹城市');
INSERT INTO `province_city_district` VALUES (370901, 3709, '市辖区');
INSERT INTO `province_city_district` VALUES (370902, 3709, '泰山区');
INSERT INTO `province_city_district` VALUES (370903, 3709, '岱岳区');
INSERT INTO `province_city_district` VALUES (370921, 3709, '宁阳县');
INSERT INTO `province_city_district` VALUES (370923, 3709, '东平县');
INSERT INTO `province_city_district` VALUES (370982, 3709, '新泰市');
INSERT INTO `province_city_district` VALUES (370983, 3709, '肥城市');
INSERT INTO `province_city_district` VALUES (371001, 3710, '市辖区');
INSERT INTO `province_city_district` VALUES (371002, 3710, '环翠区');
INSERT INTO `province_city_district` VALUES (371081, 3710, '文登市');
INSERT INTO `province_city_district` VALUES (371082, 3710, '荣成市');
INSERT INTO `province_city_district` VALUES (371083, 3710, '乳山市');
INSERT INTO `province_city_district` VALUES (371101, 3711, '市辖区');
INSERT INTO `province_city_district` VALUES (371102, 3711, '东港区');
INSERT INTO `province_city_district` VALUES (371121, 3711, '五莲县');
INSERT INTO `province_city_district` VALUES (371122, 3711, '莒  县');
INSERT INTO `province_city_district` VALUES (371201, 3712, '市辖区');
INSERT INTO `province_city_district` VALUES (371202, 3712, '莱城区');
INSERT INTO `province_city_district` VALUES (371203, 3712, '钢城区');
INSERT INTO `province_city_district` VALUES (371301, 3713, '市辖区');
INSERT INTO `province_city_district` VALUES (371302, 3713, '兰山区');
INSERT INTO `province_city_district` VALUES (371311, 3713, '罗庄区');
INSERT INTO `province_city_district` VALUES (371312, 3713, '河东区');
INSERT INTO `province_city_district` VALUES (371321, 3713, '沂南县');
INSERT INTO `province_city_district` VALUES (371322, 3713, '郯城县');
INSERT INTO `province_city_district` VALUES (371323, 3713, '沂水县');
INSERT INTO `province_city_district` VALUES (371324, 3713, '苍山县');
INSERT INTO `province_city_district` VALUES (371325, 3713, '费  县');
INSERT INTO `province_city_district` VALUES (371326, 3713, '平邑县');
INSERT INTO `province_city_district` VALUES (371327, 3713, '莒南县');
INSERT INTO `province_city_district` VALUES (371328, 3713, '蒙阴县');
INSERT INTO `province_city_district` VALUES (371329, 3713, '临沭县');
INSERT INTO `province_city_district` VALUES (371401, 3714, '市辖区');
INSERT INTO `province_city_district` VALUES (371402, 3714, '德城区');
INSERT INTO `province_city_district` VALUES (371421, 3714, '陵  县');
INSERT INTO `province_city_district` VALUES (371422, 3714, '宁津县');
INSERT INTO `province_city_district` VALUES (371423, 3714, '庆云县');
INSERT INTO `province_city_district` VALUES (371424, 3714, '临邑县');
INSERT INTO `province_city_district` VALUES (371425, 3714, '齐河县');
INSERT INTO `province_city_district` VALUES (371426, 3714, '平原县');
INSERT INTO `province_city_district` VALUES (371427, 3714, '夏津县');
INSERT INTO `province_city_district` VALUES (371428, 3714, '武城县');
INSERT INTO `province_city_district` VALUES (371481, 3714, '乐陵市');
INSERT INTO `province_city_district` VALUES (371482, 3714, '禹城市');
INSERT INTO `province_city_district` VALUES (371501, 3715, '市辖区');
INSERT INTO `province_city_district` VALUES (371502, 3715, '东昌府区');
INSERT INTO `province_city_district` VALUES (371521, 3715, '阳谷县');
INSERT INTO `province_city_district` VALUES (371522, 3715, '莘  县');
INSERT INTO `province_city_district` VALUES (371523, 3715, '茌平县');
INSERT INTO `province_city_district` VALUES (371524, 3715, '东阿县');
INSERT INTO `province_city_district` VALUES (371525, 3715, '冠  县');
INSERT INTO `province_city_district` VALUES (371526, 3715, '高唐县');
INSERT INTO `province_city_district` VALUES (371581, 3715, '临清市');
INSERT INTO `province_city_district` VALUES (371601, 3716, '市辖区');
INSERT INTO `province_city_district` VALUES (371603, 3716, '滨城区');
INSERT INTO `province_city_district` VALUES (371621, 3716, '惠民县');
INSERT INTO `province_city_district` VALUES (371622, 3716, '阳信县');
INSERT INTO `province_city_district` VALUES (371623, 3716, '无棣县');
INSERT INTO `province_city_district` VALUES (371624, 3716, '沾化县');
INSERT INTO `province_city_district` VALUES (371625, 3716, '博兴县');
INSERT INTO `province_city_district` VALUES (371626, 3716, '邹平县');
INSERT INTO `province_city_district` VALUES (371701, 3717, '市辖区');
INSERT INTO `province_city_district` VALUES (371702, 3717, '牡丹区');
INSERT INTO `province_city_district` VALUES (371721, 3717, '曹  县');
INSERT INTO `province_city_district` VALUES (371722, 3717, '单  县');
INSERT INTO `province_city_district` VALUES (371723, 3717, '成武县');
INSERT INTO `province_city_district` VALUES (371724, 3717, '巨野县');
INSERT INTO `province_city_district` VALUES (371725, 3717, '郓城县');
INSERT INTO `province_city_district` VALUES (371726, 3717, '鄄城县');
INSERT INTO `province_city_district` VALUES (371727, 3717, '定陶县');
INSERT INTO `province_city_district` VALUES (371728, 3717, '东明县');
INSERT INTO `province_city_district` VALUES (410101, 4101, '市辖区');
INSERT INTO `province_city_district` VALUES (410102, 4101, '中原区');
INSERT INTO `province_city_district` VALUES (410103, 4101, '二七区');
INSERT INTO `province_city_district` VALUES (410104, 4101, '管城回族区');
INSERT INTO `province_city_district` VALUES (410105, 4101, '金水区');
INSERT INTO `province_city_district` VALUES (410106, 4101, '上街区');
INSERT INTO `province_city_district` VALUES (410108, 4101, '邙山区');
INSERT INTO `province_city_district` VALUES (410122, 4101, '中牟县');
INSERT INTO `province_city_district` VALUES (410181, 4101, '巩义市');
INSERT INTO `province_city_district` VALUES (410182, 4101, '荥阳市');
INSERT INTO `province_city_district` VALUES (410183, 4101, '新密市');
INSERT INTO `province_city_district` VALUES (410184, 4101, '新郑市');
INSERT INTO `province_city_district` VALUES (410185, 4101, '登封市');
INSERT INTO `province_city_district` VALUES (410201, 4102, '市辖区');
INSERT INTO `province_city_district` VALUES (410202, 4102, '龙亭区');
INSERT INTO `province_city_district` VALUES (410203, 4102, '顺河回族区');
INSERT INTO `province_city_district` VALUES (410204, 4102, '鼓楼区');
INSERT INTO `province_city_district` VALUES (410205, 4102, '南关区');
INSERT INTO `province_city_district` VALUES (410211, 4102, '郊  区');
INSERT INTO `province_city_district` VALUES (410221, 4102, '杞  县');
INSERT INTO `province_city_district` VALUES (410222, 4102, '通许县');
INSERT INTO `province_city_district` VALUES (410223, 4102, '尉氏县');
INSERT INTO `province_city_district` VALUES (410224, 4102, '开封县');
INSERT INTO `province_city_district` VALUES (410225, 4102, '兰考县');
INSERT INTO `province_city_district` VALUES (410301, 4103, '市辖区');
INSERT INTO `province_city_district` VALUES (410302, 4103, '老城区');
INSERT INTO `province_city_district` VALUES (410303, 4103, '西工区');
INSERT INTO `province_city_district` VALUES (410304, 4103, '廛河回族区');
INSERT INTO `province_city_district` VALUES (410305, 4103, '涧西区');
INSERT INTO `province_city_district` VALUES (410306, 4103, '吉利区');
INSERT INTO `province_city_district` VALUES (410307, 4103, '洛龙区');
INSERT INTO `province_city_district` VALUES (410322, 4103, '孟津县');
INSERT INTO `province_city_district` VALUES (410323, 4103, '新安县');
INSERT INTO `province_city_district` VALUES (410324, 4103, '栾川县');
INSERT INTO `province_city_district` VALUES (410325, 4103, '嵩  县');
INSERT INTO `province_city_district` VALUES (410326, 4103, '汝阳县');
INSERT INTO `province_city_district` VALUES (410327, 4103, '宜阳县');
INSERT INTO `province_city_district` VALUES (410328, 4103, '洛宁县');
INSERT INTO `province_city_district` VALUES (410329, 4103, '伊川县');
INSERT INTO `province_city_district` VALUES (410381, 4103, '偃师市');
INSERT INTO `province_city_district` VALUES (410401, 4104, '市辖区');
INSERT INTO `province_city_district` VALUES (410402, 4104, '新华区');
INSERT INTO `province_city_district` VALUES (410403, 4104, '卫东区');
INSERT INTO `province_city_district` VALUES (410404, 4104, '石龙区');
INSERT INTO `province_city_district` VALUES (410411, 4104, '湛河区');
INSERT INTO `province_city_district` VALUES (410421, 4104, '宝丰县');
INSERT INTO `province_city_district` VALUES (410422, 4104, '叶  县');
INSERT INTO `province_city_district` VALUES (410423, 4104, '鲁山县');
INSERT INTO `province_city_district` VALUES (410425, 4104, '郏  县');
INSERT INTO `province_city_district` VALUES (410481, 4104, '舞钢市');
INSERT INTO `province_city_district` VALUES (410482, 4104, '汝州市');
INSERT INTO `province_city_district` VALUES (410501, 4105, '市辖区');
INSERT INTO `province_city_district` VALUES (410502, 4105, '文峰区');
INSERT INTO `province_city_district` VALUES (410503, 4105, '北关区');
INSERT INTO `province_city_district` VALUES (410504, 4105, '铁西区');
INSERT INTO `province_city_district` VALUES (410511, 4105, '郊  区');
INSERT INTO `province_city_district` VALUES (410522, 4105, '安阳县');
INSERT INTO `province_city_district` VALUES (410523, 4105, '汤阴县');
INSERT INTO `province_city_district` VALUES (410526, 4105, '滑  县');
INSERT INTO `province_city_district` VALUES (410527, 4105, '内黄县');
INSERT INTO `province_city_district` VALUES (410581, 4105, '林州市');
INSERT INTO `province_city_district` VALUES (410601, 4106, '市辖区');
INSERT INTO `province_city_district` VALUES (410602, 4106, '鹤山区');
INSERT INTO `province_city_district` VALUES (410603, 4106, '山城区');
INSERT INTO `province_city_district` VALUES (410611, 4106, '郊  区');
INSERT INTO `province_city_district` VALUES (410621, 4106, '浚  县');
INSERT INTO `province_city_district` VALUES (410622, 4106, '淇  县');
INSERT INTO `province_city_district` VALUES (410701, 4107, '市辖区');
INSERT INTO `province_city_district` VALUES (410702, 4107, '红旗区');
INSERT INTO `province_city_district` VALUES (410703, 4107, '新华区');
INSERT INTO `province_city_district` VALUES (410704, 4107, '北站区');
INSERT INTO `province_city_district` VALUES (410711, 4107, '郊  区');
INSERT INTO `province_city_district` VALUES (410721, 4107, '新乡县');
INSERT INTO `province_city_district` VALUES (410724, 4107, '获嘉县');
INSERT INTO `province_city_district` VALUES (410725, 4107, '原阳县');
INSERT INTO `province_city_district` VALUES (410726, 4107, '延津县');
INSERT INTO `province_city_district` VALUES (410727, 4107, '封丘县');
INSERT INTO `province_city_district` VALUES (410728, 4107, '长垣县');
INSERT INTO `province_city_district` VALUES (410781, 4107, '卫辉市');
INSERT INTO `province_city_district` VALUES (410782, 4107, '辉县市');
INSERT INTO `province_city_district` VALUES (410801, 4108, '市辖区');
INSERT INTO `province_city_district` VALUES (410802, 4108, '解放区');
INSERT INTO `province_city_district` VALUES (410803, 4108, '中站区');
INSERT INTO `province_city_district` VALUES (410804, 4108, '马村区');
INSERT INTO `province_city_district` VALUES (410811, 4108, '山阳区');
INSERT INTO `province_city_district` VALUES (410821, 4108, '修武县');
INSERT INTO `province_city_district` VALUES (410822, 4108, '博爱县');
INSERT INTO `province_city_district` VALUES (410823, 4108, '武陟县');
INSERT INTO `province_city_district` VALUES (410825, 4108, '温  县');
INSERT INTO `province_city_district` VALUES (410881, 4108, '济源市');
INSERT INTO `province_city_district` VALUES (410882, 4108, '沁阳市');
INSERT INTO `province_city_district` VALUES (410883, 4108, '孟州市');
INSERT INTO `province_city_district` VALUES (410901, 4109, '市辖区');
INSERT INTO `province_city_district` VALUES (410902, 4109, '市  区');
INSERT INTO `province_city_district` VALUES (410922, 4109, '清丰县');
INSERT INTO `province_city_district` VALUES (410923, 4109, '南乐县');
INSERT INTO `province_city_district` VALUES (410926, 4109, '范  县');
INSERT INTO `province_city_district` VALUES (410927, 4109, '台前县');
INSERT INTO `province_city_district` VALUES (410928, 4109, '濮阳县');
INSERT INTO `province_city_district` VALUES (411001, 4110, '市辖区');
INSERT INTO `province_city_district` VALUES (411002, 4110, '魏都区');
INSERT INTO `province_city_district` VALUES (411023, 4110, '许昌县');
INSERT INTO `province_city_district` VALUES (411024, 4110, '鄢陵县');
INSERT INTO `province_city_district` VALUES (411025, 4110, '襄城县');
INSERT INTO `province_city_district` VALUES (411081, 4110, '禹州市');
INSERT INTO `province_city_district` VALUES (411082, 4110, '长葛市');
INSERT INTO `province_city_district` VALUES (411101, 4111, '市辖区');
INSERT INTO `province_city_district` VALUES (411102, 4111, '源汇区');
INSERT INTO `province_city_district` VALUES (411121, 4111, '舞阳县');
INSERT INTO `province_city_district` VALUES (411122, 4111, '临颍县');
INSERT INTO `province_city_district` VALUES (411123, 4111, '郾城县');
INSERT INTO `province_city_district` VALUES (411201, 4112, '市辖区');
INSERT INTO `province_city_district` VALUES (411202, 4112, '湖滨区');
INSERT INTO `province_city_district` VALUES (411221, 4112, '渑池县');
INSERT INTO `province_city_district` VALUES (411222, 4112, '陕  县');
INSERT INTO `province_city_district` VALUES (411224, 4112, '卢氏县');
INSERT INTO `province_city_district` VALUES (411281, 4112, '义马市');
INSERT INTO `province_city_district` VALUES (411282, 4112, '灵宝市');
INSERT INTO `province_city_district` VALUES (411301, 4113, '市辖区');
INSERT INTO `province_city_district` VALUES (411302, 4113, '宛城区');
INSERT INTO `province_city_district` VALUES (411303, 4113, '卧龙区');
INSERT INTO `province_city_district` VALUES (411321, 4113, '南召县');
INSERT INTO `province_city_district` VALUES (411322, 4113, '方城县');
INSERT INTO `province_city_district` VALUES (411323, 4113, '西峡县');
INSERT INTO `province_city_district` VALUES (411324, 4113, '镇平县');
INSERT INTO `province_city_district` VALUES (411325, 4113, '内乡县');
INSERT INTO `province_city_district` VALUES (411326, 4113, '淅川县');
INSERT INTO `province_city_district` VALUES (411327, 4113, '社旗县');
INSERT INTO `province_city_district` VALUES (411328, 4113, '唐河县');
INSERT INTO `province_city_district` VALUES (411329, 4113, '新野县');
INSERT INTO `province_city_district` VALUES (411330, 4113, '桐柏县');
INSERT INTO `province_city_district` VALUES (411381, 4113, '邓州市');
INSERT INTO `province_city_district` VALUES (411401, 4114, '市辖区');
INSERT INTO `province_city_district` VALUES (411402, 4114, '梁园区');
INSERT INTO `province_city_district` VALUES (411403, 4114, '睢阳区');
INSERT INTO `province_city_district` VALUES (411421, 4114, '民权县');
INSERT INTO `province_city_district` VALUES (411422, 4114, '睢  县');
INSERT INTO `province_city_district` VALUES (411423, 4114, '宁陵县');
INSERT INTO `province_city_district` VALUES (411424, 4114, '柘城县');
INSERT INTO `province_city_district` VALUES (411425, 4114, '虞城县');
INSERT INTO `province_city_district` VALUES (411426, 4114, '夏邑县');
INSERT INTO `province_city_district` VALUES (411481, 4114, '永城市');
INSERT INTO `province_city_district` VALUES (411501, 4115, '市辖区');
INSERT INTO `province_city_district` VALUES (411502, 4115, '师河区');
INSERT INTO `province_city_district` VALUES (411503, 4115, '平桥区');
INSERT INTO `province_city_district` VALUES (411521, 4115, '罗山县');
INSERT INTO `province_city_district` VALUES (411522, 4115, '光山县');
INSERT INTO `province_city_district` VALUES (411523, 4115, '新  县');
INSERT INTO `province_city_district` VALUES (411524, 4115, '商城县');
INSERT INTO `province_city_district` VALUES (411525, 4115, '固始县');
INSERT INTO `province_city_district` VALUES (411526, 4115, '潢川县');
INSERT INTO `province_city_district` VALUES (411527, 4115, '淮滨县');
INSERT INTO `province_city_district` VALUES (411528, 4115, '息  县');
INSERT INTO `province_city_district` VALUES (411601, 4116, '市辖区');
INSERT INTO `province_city_district` VALUES (411602, 4116, '川汇区');
INSERT INTO `province_city_district` VALUES (411621, 4116, '扶沟县');
INSERT INTO `province_city_district` VALUES (411622, 4116, '西华县');
INSERT INTO `province_city_district` VALUES (411623, 4116, '商水县');
INSERT INTO `province_city_district` VALUES (411624, 4116, '沈丘县');
INSERT INTO `province_city_district` VALUES (411625, 4116, '郸城县');
INSERT INTO `province_city_district` VALUES (411626, 4116, '淮阳县');
INSERT INTO `province_city_district` VALUES (411627, 4116, '太康县');
INSERT INTO `province_city_district` VALUES (411628, 4116, '鹿邑县');
INSERT INTO `province_city_district` VALUES (411681, 4116, '项城市');
INSERT INTO `province_city_district` VALUES (411701, 4117, '市辖区');
INSERT INTO `province_city_district` VALUES (411702, 4117, '驿城区');
INSERT INTO `province_city_district` VALUES (411721, 4117, '西平县');
INSERT INTO `province_city_district` VALUES (411722, 4117, '上蔡县');
INSERT INTO `province_city_district` VALUES (411723, 4117, '平舆县');
INSERT INTO `province_city_district` VALUES (411724, 4117, '正阳县');
INSERT INTO `province_city_district` VALUES (411725, 4117, '确山县');
INSERT INTO `province_city_district` VALUES (411726, 4117, '泌阳县');
INSERT INTO `province_city_district` VALUES (411727, 4117, '汝南县');
INSERT INTO `province_city_district` VALUES (411728, 4117, '遂平县');
INSERT INTO `province_city_district` VALUES (411729, 4117, '新蔡县');
INSERT INTO `province_city_district` VALUES (420101, 4201, '市辖区');
INSERT INTO `province_city_district` VALUES (420102, 4201, '江岸区');
INSERT INTO `province_city_district` VALUES (420103, 4201, '江汉区');
INSERT INTO `province_city_district` VALUES (420104, 4201, '乔口区');
INSERT INTO `province_city_district` VALUES (420105, 4201, '汉阳区');
INSERT INTO `province_city_district` VALUES (420106, 4201, '武昌区');
INSERT INTO `province_city_district` VALUES (420107, 4201, '青山区');
INSERT INTO `province_city_district` VALUES (420111, 4201, '洪山区');
INSERT INTO `province_city_district` VALUES (420112, 4201, '东西湖区');
INSERT INTO `province_city_district` VALUES (420113, 4201, '汉南区');
INSERT INTO `province_city_district` VALUES (420114, 4201, '蔡甸区');
INSERT INTO `province_city_district` VALUES (420115, 4201, '江夏区');
INSERT INTO `province_city_district` VALUES (420116, 4201, '黄陂区');
INSERT INTO `province_city_district` VALUES (420117, 4201, '新洲区');
INSERT INTO `province_city_district` VALUES (420201, 4202, '市辖区');
INSERT INTO `province_city_district` VALUES (420202, 4202, '黄石港区');
INSERT INTO `province_city_district` VALUES (420203, 4202, '石灰窑区');
INSERT INTO `province_city_district` VALUES (420204, 4202, '下陆区');
INSERT INTO `province_city_district` VALUES (420205, 4202, '铁山区');
INSERT INTO `province_city_district` VALUES (420222, 4202, '阳新县');
INSERT INTO `province_city_district` VALUES (420281, 4202, '大冶市');
INSERT INTO `province_city_district` VALUES (420301, 4203, '市辖区');
INSERT INTO `province_city_district` VALUES (420302, 4203, '茅箭区');
INSERT INTO `province_city_district` VALUES (420303, 4203, '张湾区');
INSERT INTO `province_city_district` VALUES (420321, 4203, '郧  县');
INSERT INTO `province_city_district` VALUES (420322, 4203, '郧西县');
INSERT INTO `province_city_district` VALUES (420323, 4203, '竹山县');
INSERT INTO `province_city_district` VALUES (420324, 4203, '竹溪县');
INSERT INTO `province_city_district` VALUES (420325, 4203, '房  县');
INSERT INTO `province_city_district` VALUES (420381, 4203, '丹江口市');
INSERT INTO `province_city_district` VALUES (420501, 4205, '市辖区');
INSERT INTO `province_city_district` VALUES (420502, 4205, '西陵区');
INSERT INTO `province_city_district` VALUES (420503, 4205, '伍家岗区');
INSERT INTO `province_city_district` VALUES (420504, 4205, '点军区');
INSERT INTO `province_city_district` VALUES (420505, 4205, '虎亭区');
INSERT INTO `province_city_district` VALUES (420521, 4205, '宜昌县');
INSERT INTO `province_city_district` VALUES (420525, 4205, '远安县');
INSERT INTO `province_city_district` VALUES (420526, 4205, '兴山县');
INSERT INTO `province_city_district` VALUES (420527, 4205, '秭归县');
INSERT INTO `province_city_district` VALUES (420528, 4205, '长阳土家族自治县');
INSERT INTO `province_city_district` VALUES (420529, 4205, '五峰土家族自治县');
INSERT INTO `province_city_district` VALUES (420581, 4205, '宜都市');
INSERT INTO `province_city_district` VALUES (420582, 4205, '当阳市');
INSERT INTO `province_city_district` VALUES (420583, 4205, '枝江市');
INSERT INTO `province_city_district` VALUES (420601, 4206, '市辖区');
INSERT INTO `province_city_district` VALUES (420602, 4206, '襄城区');
INSERT INTO `province_city_district` VALUES (420606, 4206, '樊城区');
INSERT INTO `province_city_district` VALUES (420621, 4206, '襄阳县');
INSERT INTO `province_city_district` VALUES (420624, 4206, '南漳县');
INSERT INTO `province_city_district` VALUES (420625, 4206, '谷城县');
INSERT INTO `province_city_district` VALUES (420626, 4206, '保康县');
INSERT INTO `province_city_district` VALUES (420682, 4206, '老河口市');
INSERT INTO `province_city_district` VALUES (420683, 4206, '枣阳市');
INSERT INTO `province_city_district` VALUES (420684, 4206, '宜城市');
INSERT INTO `province_city_district` VALUES (420701, 4207, '市辖区');
INSERT INTO `province_city_district` VALUES (420702, 4207, '梁子湖区');
INSERT INTO `province_city_district` VALUES (420703, 4207, '华容区');
INSERT INTO `province_city_district` VALUES (420704, 4207, '鄂城区');
INSERT INTO `province_city_district` VALUES (420801, 4208, '市辖区');
INSERT INTO `province_city_district` VALUES (420802, 4208, '东宝区');
INSERT INTO `province_city_district` VALUES (420821, 4208, '京山县');
INSERT INTO `province_city_district` VALUES (420822, 4208, '沙洋县');
INSERT INTO `province_city_district` VALUES (420881, 4208, '钟祥市');
INSERT INTO `province_city_district` VALUES (420901, 4209, '市辖区');
INSERT INTO `province_city_district` VALUES (420902, 4209, '孝南区');
INSERT INTO `province_city_district` VALUES (420921, 4209, '孝昌县');
INSERT INTO `province_city_district` VALUES (420922, 4209, '大悟县');
INSERT INTO `province_city_district` VALUES (420923, 4209, '云梦县');
INSERT INTO `province_city_district` VALUES (420981, 4209, '应城市');
INSERT INTO `province_city_district` VALUES (420982, 4209, '安陆市');
INSERT INTO `province_city_district` VALUES (420984, 4209, '汉川市');
INSERT INTO `province_city_district` VALUES (421001, 4210, '市辖区');
INSERT INTO `province_city_district` VALUES (421002, 4210, '沙市区');
INSERT INTO `province_city_district` VALUES (421003, 4210, '荆州区');
INSERT INTO `province_city_district` VALUES (421022, 4210, '公安县');
INSERT INTO `province_city_district` VALUES (421023, 4210, '监利县');
INSERT INTO `province_city_district` VALUES (421024, 4210, '江陵县');
INSERT INTO `province_city_district` VALUES (421081, 4210, '石首市');
INSERT INTO `province_city_district` VALUES (421083, 4210, '洪湖市');
INSERT INTO `province_city_district` VALUES (421087, 4210, '松滋市');
INSERT INTO `province_city_district` VALUES (421101, 4211, '市辖区');
INSERT INTO `province_city_district` VALUES (421102, 4211, '黄州区');
INSERT INTO `province_city_district` VALUES (421121, 4211, '团风县');
INSERT INTO `province_city_district` VALUES (421122, 4211, '红安县');
INSERT INTO `province_city_district` VALUES (421123, 4211, '罗田县');
INSERT INTO `province_city_district` VALUES (421124, 4211, '英山县');
INSERT INTO `province_city_district` VALUES (421125, 4211, '浠水县');
INSERT INTO `province_city_district` VALUES (421126, 4211, '蕲春县');
INSERT INTO `province_city_district` VALUES (421127, 4211, '黄梅县');
INSERT INTO `province_city_district` VALUES (421181, 4211, '麻城市');
INSERT INTO `province_city_district` VALUES (421182, 4211, '武穴市');
INSERT INTO `province_city_district` VALUES (421201, 4212, '市辖区');
INSERT INTO `province_city_district` VALUES (421202, 4212, '咸安区');
INSERT INTO `province_city_district` VALUES (421221, 4212, '嘉鱼县');
INSERT INTO `province_city_district` VALUES (421222, 4212, '通城县');
INSERT INTO `province_city_district` VALUES (421223, 4212, '崇阳县');
INSERT INTO `province_city_district` VALUES (421224, 4212, '通山县');
INSERT INTO `province_city_district` VALUES (421281, 4212, '赤壁市');
INSERT INTO `province_city_district` VALUES (421301, 4213, '市辖区');
INSERT INTO `province_city_district` VALUES (421302, 4213, '曾都区');
INSERT INTO `province_city_district` VALUES (421381, 4213, '广水市');
INSERT INTO `province_city_district` VALUES (422801, 4228, '恩施市');
INSERT INTO `province_city_district` VALUES (422802, 4228, '利川市');
INSERT INTO `province_city_district` VALUES (422822, 4228, '建始县');
INSERT INTO `province_city_district` VALUES (422823, 4228, '巴东县');
INSERT INTO `province_city_district` VALUES (422825, 4228, '宣恩县');
INSERT INTO `province_city_district` VALUES (422826, 4228, '咸丰县');
INSERT INTO `province_city_district` VALUES (422827, 4228, '来凤县');
INSERT INTO `province_city_district` VALUES (422828, 4228, '鹤峰县');
INSERT INTO `province_city_district` VALUES (429004, 4290, '仙桃市');
INSERT INTO `province_city_district` VALUES (429005, 4290, '潜江市');
INSERT INTO `province_city_district` VALUES (429006, 4290, '天门市');
INSERT INTO `province_city_district` VALUES (429021, 4290, '神农架林区');
INSERT INTO `province_city_district` VALUES (430101, 4301, '市辖区');
INSERT INTO `province_city_district` VALUES (430102, 4301, '芙蓉区');
INSERT INTO `province_city_district` VALUES (430103, 4301, '天心区');
INSERT INTO `province_city_district` VALUES (430104, 4301, '岳麓区');
INSERT INTO `province_city_district` VALUES (430105, 4301, '开福区');
INSERT INTO `province_city_district` VALUES (430111, 4301, '雨花区');
INSERT INTO `province_city_district` VALUES (430121, 4301, '长沙县');
INSERT INTO `province_city_district` VALUES (430122, 4301, '望城县');
INSERT INTO `province_city_district` VALUES (430124, 4301, '宁乡县');
INSERT INTO `province_city_district` VALUES (430181, 4301, '浏阳市');
INSERT INTO `province_city_district` VALUES (430201, 4302, '市辖区');
INSERT INTO `province_city_district` VALUES (430202, 4302, '荷塘区');
INSERT INTO `province_city_district` VALUES (430203, 4302, '芦淞区');
INSERT INTO `province_city_district` VALUES (430204, 4302, '石峰区');
INSERT INTO `province_city_district` VALUES (430211, 4302, '天元区');
INSERT INTO `province_city_district` VALUES (430221, 4302, '株洲县');
INSERT INTO `province_city_district` VALUES (430223, 4302, '攸  县');
INSERT INTO `province_city_district` VALUES (430224, 4302, '茶陵县');
INSERT INTO `province_city_district` VALUES (430225, 4302, '炎陵县');
INSERT INTO `province_city_district` VALUES (430281, 4302, '醴陵市');
INSERT INTO `province_city_district` VALUES (430301, 4303, '市辖区');
INSERT INTO `province_city_district` VALUES (430302, 4303, '雨湖区');
INSERT INTO `province_city_district` VALUES (430304, 4303, '岳塘区');
INSERT INTO `province_city_district` VALUES (430321, 4303, '湘潭县');
INSERT INTO `province_city_district` VALUES (430381, 4303, '湘乡市');
INSERT INTO `province_city_district` VALUES (430382, 4303, '韶山市');
INSERT INTO `province_city_district` VALUES (430401, 4304, '市辖区');
INSERT INTO `province_city_district` VALUES (430402, 4304, '江东区');
INSERT INTO `province_city_district` VALUES (430403, 4304, '城南区');
INSERT INTO `province_city_district` VALUES (430404, 4304, '城北区');
INSERT INTO `province_city_district` VALUES (430411, 4304, '郊   区');
INSERT INTO `province_city_district` VALUES (430412, 4304, '南岳区');
INSERT INTO `province_city_district` VALUES (430421, 4304, '衡阳县');
INSERT INTO `province_city_district` VALUES (430422, 4304, '衡南县');
INSERT INTO `province_city_district` VALUES (430423, 4304, '衡山县');
INSERT INTO `province_city_district` VALUES (430424, 4304, '衡东县');
INSERT INTO `province_city_district` VALUES (430426, 4304, '祁东县');
INSERT INTO `province_city_district` VALUES (430481, 4304, '耒阳市');
INSERT INTO `province_city_district` VALUES (430482, 4304, '常宁市');
INSERT INTO `province_city_district` VALUES (430501, 4305, '市辖区');
INSERT INTO `province_city_district` VALUES (430502, 4305, '双清区');
INSERT INTO `province_city_district` VALUES (430503, 4305, '大祥区');
INSERT INTO `province_city_district` VALUES (430511, 4305, '北塔区');
INSERT INTO `province_city_district` VALUES (430521, 4305, '邵东县');
INSERT INTO `province_city_district` VALUES (430522, 4305, '新邵县');
INSERT INTO `province_city_district` VALUES (430523, 4305, '邵阳县');
INSERT INTO `province_city_district` VALUES (430524, 4305, '隆回县');
INSERT INTO `province_city_district` VALUES (430525, 4305, '洞口县');
INSERT INTO `province_city_district` VALUES (430527, 4305, '绥宁县');
INSERT INTO `province_city_district` VALUES (430528, 4305, '新宁县');
INSERT INTO `province_city_district` VALUES (430529, 4305, '城步苗族自治县');
INSERT INTO `province_city_district` VALUES (430581, 4305, '武冈市');
INSERT INTO `province_city_district` VALUES (430601, 4306, '市辖区');
INSERT INTO `province_city_district` VALUES (430602, 4306, '岳阳楼区');
INSERT INTO `province_city_district` VALUES (430603, 4306, '云溪区');
INSERT INTO `province_city_district` VALUES (430611, 4306, '君山区');
INSERT INTO `province_city_district` VALUES (430621, 4306, '岳阳县');
INSERT INTO `province_city_district` VALUES (430623, 4306, '华容县');
INSERT INTO `province_city_district` VALUES (430624, 4306, '湘阴县');
INSERT INTO `province_city_district` VALUES (430626, 4306, '平江县');
INSERT INTO `province_city_district` VALUES (430681, 4306, '汨罗市');
INSERT INTO `province_city_district` VALUES (430682, 4306, '临湘市');
INSERT INTO `province_city_district` VALUES (430701, 4307, '市辖区');
INSERT INTO `province_city_district` VALUES (430702, 4307, '武陵区');
INSERT INTO `province_city_district` VALUES (430703, 4307, '鼎城区');
INSERT INTO `province_city_district` VALUES (430721, 4307, '安乡县');
INSERT INTO `province_city_district` VALUES (430722, 4307, '汉寿县');
INSERT INTO `province_city_district` VALUES (430723, 4307, '澧  县');
INSERT INTO `province_city_district` VALUES (430724, 4307, '临澧县');
INSERT INTO `province_city_district` VALUES (430725, 4307, '桃源县');
INSERT INTO `province_city_district` VALUES (430726, 4307, '石门县');
INSERT INTO `province_city_district` VALUES (430781, 4307, '津市市');
INSERT INTO `province_city_district` VALUES (430801, 4308, '市辖区');
INSERT INTO `province_city_district` VALUES (430802, 4308, '永定区');
INSERT INTO `province_city_district` VALUES (430811, 4308, '武陵源区');
INSERT INTO `province_city_district` VALUES (430821, 4308, '慈利县');
INSERT INTO `province_city_district` VALUES (430822, 4308, '桑植县');
INSERT INTO `province_city_district` VALUES (430901, 4309, '市辖区');
INSERT INTO `province_city_district` VALUES (430902, 4309, '资阳区');
INSERT INTO `province_city_district` VALUES (430903, 4309, '赫山区');
INSERT INTO `province_city_district` VALUES (430921, 4309, '南  县');
INSERT INTO `province_city_district` VALUES (430922, 4309, '桃江县');
INSERT INTO `province_city_district` VALUES (430923, 4309, '安化县');
INSERT INTO `province_city_district` VALUES (430981, 4309, '沅江市');
INSERT INTO `province_city_district` VALUES (431001, 4310, '市辖区');
INSERT INTO `province_city_district` VALUES (431002, 4310, '北湖区');
INSERT INTO `province_city_district` VALUES (431003, 4310, '苏仙区');
INSERT INTO `province_city_district` VALUES (431021, 4310, '桂阳县');
INSERT INTO `province_city_district` VALUES (431022, 4310, '宜章县');
INSERT INTO `province_city_district` VALUES (431023, 4310, '永兴县');
INSERT INTO `province_city_district` VALUES (431024, 4310, '嘉禾县');
INSERT INTO `province_city_district` VALUES (431025, 4310, '临武县');
INSERT INTO `province_city_district` VALUES (431026, 4310, '汝城县');
INSERT INTO `province_city_district` VALUES (431027, 4310, '桂东县');
INSERT INTO `province_city_district` VALUES (431028, 4310, '安仁县');
INSERT INTO `province_city_district` VALUES (431081, 4310, '资兴市');
INSERT INTO `province_city_district` VALUES (431101, 4311, '市辖区');
INSERT INTO `province_city_district` VALUES (431102, 4311, '芝山区');
INSERT INTO `province_city_district` VALUES (431103, 4311, '冷水滩区');
INSERT INTO `province_city_district` VALUES (431121, 4311, '祁阳县');
INSERT INTO `province_city_district` VALUES (431122, 4311, '东安县');
INSERT INTO `province_city_district` VALUES (431123, 4311, '双牌县');
INSERT INTO `province_city_district` VALUES (431124, 4311, '道  县');
INSERT INTO `province_city_district` VALUES (431125, 4311, '江永县');
INSERT INTO `province_city_district` VALUES (431126, 4311, '宁远县');
INSERT INTO `province_city_district` VALUES (431127, 4311, '蓝山县');
INSERT INTO `province_city_district` VALUES (431128, 4311, '新田县');
INSERT INTO `province_city_district` VALUES (431129, 4311, '江华瑶族自治县');
INSERT INTO `province_city_district` VALUES (431201, 4312, '市辖区');
INSERT INTO `province_city_district` VALUES (431202, 4312, '鹤城区');
INSERT INTO `province_city_district` VALUES (431221, 4312, '中方县');
INSERT INTO `province_city_district` VALUES (431222, 4312, '沅陵县');
INSERT INTO `province_city_district` VALUES (431223, 4312, '辰溪县');
INSERT INTO `province_city_district` VALUES (431224, 4312, '溆浦县');
INSERT INTO `province_city_district` VALUES (431225, 4312, '会同县');
INSERT INTO `province_city_district` VALUES (431226, 4312, '麻阳苗族自治县');
INSERT INTO `province_city_district` VALUES (431227, 4312, '新晃侗族自治县');
INSERT INTO `province_city_district` VALUES (431228, 4312, '芷江侗族自治县');
INSERT INTO `province_city_district` VALUES (431229, 4312, '靖州苗族侗族自治县');
INSERT INTO `province_city_district` VALUES (431230, 4312, '通道侗族自治县');
INSERT INTO `province_city_district` VALUES (431281, 4312, '洪江市');
INSERT INTO `province_city_district` VALUES (431301, 4313, '市辖区');
INSERT INTO `province_city_district` VALUES (431302, 4313, '娄星区');
INSERT INTO `province_city_district` VALUES (431321, 4313, '双峰县');
INSERT INTO `province_city_district` VALUES (431322, 4313, '新化县');
INSERT INTO `province_city_district` VALUES (431381, 4313, '冷水江市');
INSERT INTO `province_city_district` VALUES (431382, 4313, '涟源市');
INSERT INTO `province_city_district` VALUES (433101, 4331, '吉首市');
INSERT INTO `province_city_district` VALUES (433122, 4331, '泸溪县');
INSERT INTO `province_city_district` VALUES (433123, 4331, '凤凰县');
INSERT INTO `province_city_district` VALUES (433124, 4331, '花垣县');
INSERT INTO `province_city_district` VALUES (433125, 4331, '保靖县');
INSERT INTO `province_city_district` VALUES (433126, 4331, '古丈县');
INSERT INTO `province_city_district` VALUES (433127, 4331, '永顺县');
INSERT INTO `province_city_district` VALUES (433130, 4331, '龙山县');
INSERT INTO `province_city_district` VALUES (440101, 4401, '市辖区');
INSERT INTO `province_city_district` VALUES (440102, 4401, '东山区');
INSERT INTO `province_city_district` VALUES (440103, 4401, '荔湾区');
INSERT INTO `province_city_district` VALUES (440104, 4401, '越秀区');
INSERT INTO `province_city_district` VALUES (440105, 4401, '海珠区');
INSERT INTO `province_city_district` VALUES (440106, 4401, '天河区');
INSERT INTO `province_city_district` VALUES (440107, 4401, '芳村区');
INSERT INTO `province_city_district` VALUES (440111, 4401, '白云区');
INSERT INTO `province_city_district` VALUES (440112, 4401, '黄埔区');
INSERT INTO `province_city_district` VALUES (440113, 4401, '番禺区');
INSERT INTO `province_city_district` VALUES (440114, 4401, '花都区');
INSERT INTO `province_city_district` VALUES (440183, 4401, '增城市');
INSERT INTO `province_city_district` VALUES (440184, 4401, '从化市');
INSERT INTO `province_city_district` VALUES (440201, 4402, '市辖区');
INSERT INTO `province_city_district` VALUES (440202, 4402, '北江区');
INSERT INTO `province_city_district` VALUES (440203, 4402, '武江区');
INSERT INTO `province_city_district` VALUES (440204, 4402, '浈江区');
INSERT INTO `province_city_district` VALUES (440221, 4402, '曲江县');
INSERT INTO `province_city_district` VALUES (440222, 4402, '始兴县');
INSERT INTO `province_city_district` VALUES (440224, 4402, '仁化县');
INSERT INTO `province_city_district` VALUES (440229, 4402, '翁源县');
INSERT INTO `province_city_district` VALUES (440232, 4402, '乳源瑶族自治县');
INSERT INTO `province_city_district` VALUES (440233, 4402, '新丰县');
INSERT INTO `province_city_district` VALUES (440281, 4402, '乐昌市');
INSERT INTO `province_city_district` VALUES (440282, 4402, '南雄市');
INSERT INTO `province_city_district` VALUES (440301, 4403, '市辖区');
INSERT INTO `province_city_district` VALUES (440303, 4403, '罗湖区');
INSERT INTO `province_city_district` VALUES (440304, 4403, '福田区');
INSERT INTO `province_city_district` VALUES (440305, 4403, '南山区');
INSERT INTO `province_city_district` VALUES (440306, 4403, '宝安区');
INSERT INTO `province_city_district` VALUES (440307, 4403, '龙岗区');
INSERT INTO `province_city_district` VALUES (440308, 4403, '盐田区');
INSERT INTO `province_city_district` VALUES (440401, 4404, '市辖区');
INSERT INTO `province_city_district` VALUES (440402, 4404, '香洲区');
INSERT INTO `province_city_district` VALUES (440421, 4404, '斗门县');
INSERT INTO `province_city_district` VALUES (440501, 4405, '市辖区');
INSERT INTO `province_city_district` VALUES (440506, 4405, '达濠区');
INSERT INTO `province_city_district` VALUES (440507, 4405, '龙湖区');
INSERT INTO `province_city_district` VALUES (440508, 4405, '金园区');
INSERT INTO `province_city_district` VALUES (440509, 4405, '升平区');
INSERT INTO `province_city_district` VALUES (440510, 4405, '河浦区');
INSERT INTO `province_city_district` VALUES (440523, 4405, '南澳县');
INSERT INTO `province_city_district` VALUES (440582, 4405, '潮阳市');
INSERT INTO `province_city_district` VALUES (440583, 4405, '澄海市');
INSERT INTO `province_city_district` VALUES (440601, 4406, '市辖区');
INSERT INTO `province_city_district` VALUES (440602, 4406, '城  区');
INSERT INTO `province_city_district` VALUES (440603, 4406, '石湾区');
INSERT INTO `province_city_district` VALUES (440681, 4406, '顺德市');
INSERT INTO `province_city_district` VALUES (440682, 4406, '南海市');
INSERT INTO `province_city_district` VALUES (440683, 4406, '三水市');
INSERT INTO `province_city_district` VALUES (440684, 4406, '高明市');
INSERT INTO `province_city_district` VALUES (440701, 4407, '市辖区');
INSERT INTO `province_city_district` VALUES (440703, 4407, '蓬江区');
INSERT INTO `province_city_district` VALUES (440704, 4407, '江海区');
INSERT INTO `province_city_district` VALUES (440781, 4407, '台山市');
INSERT INTO `province_city_district` VALUES (440782, 4407, '新会市');
INSERT INTO `province_city_district` VALUES (440783, 4407, '开平市');
INSERT INTO `province_city_district` VALUES (440784, 4407, '鹤山市');
INSERT INTO `province_city_district` VALUES (440785, 4407, '恩平市');
INSERT INTO `province_city_district` VALUES (440801, 4408, '市辖区');
INSERT INTO `province_city_district` VALUES (440802, 4408, '赤坎区');
INSERT INTO `province_city_district` VALUES (440803, 4408, '霞山区');
INSERT INTO `province_city_district` VALUES (440804, 4408, '坡头区');
INSERT INTO `province_city_district` VALUES (440811, 4408, '麻章区');
INSERT INTO `province_city_district` VALUES (440823, 4408, '遂溪县');
INSERT INTO `province_city_district` VALUES (440825, 4408, '徐闻县');
INSERT INTO `province_city_district` VALUES (440881, 4408, '廉江市');
INSERT INTO `province_city_district` VALUES (440882, 4408, '雷州市');
INSERT INTO `province_city_district` VALUES (440883, 4408, '吴川市');
INSERT INTO `province_city_district` VALUES (440901, 4409, '市辖区');
INSERT INTO `province_city_district` VALUES (440902, 4409, '茂南区');
INSERT INTO `province_city_district` VALUES (440923, 4409, '电白县');
INSERT INTO `province_city_district` VALUES (440981, 4409, '高州市');
INSERT INTO `province_city_district` VALUES (440982, 4409, '化州市');
INSERT INTO `province_city_district` VALUES (440983, 4409, '信宜市');
INSERT INTO `province_city_district` VALUES (441201, 4412, '市辖区');
INSERT INTO `province_city_district` VALUES (441202, 4412, '端州区');
INSERT INTO `province_city_district` VALUES (441203, 4412, '鼎湖区');
INSERT INTO `province_city_district` VALUES (441223, 4412, '广宁县');
INSERT INTO `province_city_district` VALUES (441224, 4412, '怀集县');
INSERT INTO `province_city_district` VALUES (441225, 4412, '封开县');
INSERT INTO `province_city_district` VALUES (441226, 4412, '德庆县');
INSERT INTO `province_city_district` VALUES (441283, 4412, '高要市');
INSERT INTO `province_city_district` VALUES (441284, 4412, '四会市');
INSERT INTO `province_city_district` VALUES (441301, 4413, '市辖区');
INSERT INTO `province_city_district` VALUES (441302, 4413, '惠城区');
INSERT INTO `province_city_district` VALUES (441322, 4413, '博罗县');
INSERT INTO `province_city_district` VALUES (441323, 4413, '惠东县');
INSERT INTO `province_city_district` VALUES (441324, 4413, '龙门县');
INSERT INTO `province_city_district` VALUES (441381, 4413, '惠阳市');
INSERT INTO `province_city_district` VALUES (441401, 4414, '市辖区');
INSERT INTO `province_city_district` VALUES (441402, 4414, '梅江区');
INSERT INTO `province_city_district` VALUES (441421, 4414, '梅  县');
INSERT INTO `province_city_district` VALUES (441422, 4414, '大埔县');
INSERT INTO `province_city_district` VALUES (441423, 4414, '丰顺县');
INSERT INTO `province_city_district` VALUES (441424, 4414, '五华县');
INSERT INTO `province_city_district` VALUES (441426, 4414, '平远县');
INSERT INTO `province_city_district` VALUES (441427, 4414, '蕉岭县');
INSERT INTO `province_city_district` VALUES (441481, 4414, '兴宁市');
INSERT INTO `province_city_district` VALUES (441501, 4415, '市辖区');
INSERT INTO `province_city_district` VALUES (441502, 4415, '城  区');
INSERT INTO `province_city_district` VALUES (441521, 4415, '海丰县');
INSERT INTO `province_city_district` VALUES (441523, 4415, '陆河县');
INSERT INTO `province_city_district` VALUES (441581, 4415, '陆丰市');
INSERT INTO `province_city_district` VALUES (441601, 4416, '市辖区');
INSERT INTO `province_city_district` VALUES (441602, 4416, '源城区');
INSERT INTO `province_city_district` VALUES (441621, 4416, '紫金县');
INSERT INTO `province_city_district` VALUES (441622, 4416, '龙川县');
INSERT INTO `province_city_district` VALUES (441623, 4416, '连平县');
INSERT INTO `province_city_district` VALUES (441624, 4416, '和平县');
INSERT INTO `province_city_district` VALUES (441625, 4416, '东源县');
INSERT INTO `province_city_district` VALUES (441701, 4417, '市辖区');
INSERT INTO `province_city_district` VALUES (441702, 4417, '江城区');
INSERT INTO `province_city_district` VALUES (441721, 4417, '阳西县');
INSERT INTO `province_city_district` VALUES (441723, 4417, '阳东县');
INSERT INTO `province_city_district` VALUES (441781, 4417, '阳春市');
INSERT INTO `province_city_district` VALUES (441801, 4418, '市辖区');
INSERT INTO `province_city_district` VALUES (441802, 4418, '清城区');
INSERT INTO `province_city_district` VALUES (441821, 4418, '佛冈县');
INSERT INTO `province_city_district` VALUES (441823, 4418, '阳山县');
INSERT INTO `province_city_district` VALUES (441825, 4418, '连山壮族瑶族自治县');
INSERT INTO `province_city_district` VALUES (441826, 4418, '连南瑶族自治县');
INSERT INTO `province_city_district` VALUES (441827, 4418, '清新县');
INSERT INTO `province_city_district` VALUES (441881, 4418, '英德市');
INSERT INTO `province_city_district` VALUES (441882, 4418, '连州市');
INSERT INTO `province_city_district` VALUES (441901, 4419, '莞城区');
INSERT INTO `province_city_district` VALUES (441902, 4419, '东城区');
INSERT INTO `province_city_district` VALUES (441903, 4419, '南城区');
INSERT INTO `province_city_district` VALUES (441904, 4419, '万江区');
INSERT INTO `province_city_district` VALUES (442001, 4420, '石岐区');
INSERT INTO `province_city_district` VALUES (442002, 4420, '东区');
INSERT INTO `province_city_district` VALUES (442003, 4420, '西区');
INSERT INTO `province_city_district` VALUES (442004, 4420, '南区');
INSERT INTO `province_city_district` VALUES (442005, 4420, '五桂山');
INSERT INTO `province_city_district` VALUES (445101, 4451, '市辖区');
INSERT INTO `province_city_district` VALUES (445102, 4451, '湘桥区');
INSERT INTO `province_city_district` VALUES (445121, 4451, '潮安县');
INSERT INTO `province_city_district` VALUES (445122, 4451, '饶平县');
INSERT INTO `province_city_district` VALUES (445201, 4452, '市辖区');
INSERT INTO `province_city_district` VALUES (445202, 4452, '榕城区');
INSERT INTO `province_city_district` VALUES (445221, 4452, '揭东县');
INSERT INTO `province_city_district` VALUES (445222, 4452, '揭西县');
INSERT INTO `province_city_district` VALUES (445224, 4452, '惠来县');
INSERT INTO `province_city_district` VALUES (445281, 4452, '普宁市');
INSERT INTO `province_city_district` VALUES (445301, 4453, '市辖区');
INSERT INTO `province_city_district` VALUES (445302, 4453, '云城区');
INSERT INTO `province_city_district` VALUES (445321, 4453, '新兴县');
INSERT INTO `province_city_district` VALUES (445322, 4453, '郁南县');
INSERT INTO `province_city_district` VALUES (445323, 4453, '云安县');
INSERT INTO `province_city_district` VALUES (445381, 4453, '罗定市');
INSERT INTO `province_city_district` VALUES (450101, 4501, '市辖区');
INSERT INTO `province_city_district` VALUES (450102, 4501, '兴宁区');
INSERT INTO `province_city_district` VALUES (450103, 4501, '新城区');
INSERT INTO `province_city_district` VALUES (450104, 4501, '城北区');
INSERT INTO `province_city_district` VALUES (450105, 4501, '江南区');
INSERT INTO `province_city_district` VALUES (450106, 4501, '永新区');
INSERT INTO `province_city_district` VALUES (450111, 4501, '市郊区');
INSERT INTO `province_city_district` VALUES (450121, 4501, '邕宁县');
INSERT INTO `province_city_district` VALUES (450122, 4501, '武鸣县');
INSERT INTO `province_city_district` VALUES (450201, 4502, '市辖区');
INSERT INTO `province_city_district` VALUES (450202, 4502, '城中区');
INSERT INTO `province_city_district` VALUES (450203, 4502, '鱼峰区');
INSERT INTO `province_city_district` VALUES (450204, 4502, '柳南区');
INSERT INTO `province_city_district` VALUES (450205, 4502, '柳北区');
INSERT INTO `province_city_district` VALUES (450211, 4502, '市郊区');
INSERT INTO `province_city_district` VALUES (450221, 4502, '柳江县');
INSERT INTO `province_city_district` VALUES (450222, 4502, '柳城县');
INSERT INTO `province_city_district` VALUES (450301, 4503, '市辖区');
INSERT INTO `province_city_district` VALUES (450302, 4503, '秀峰区');
INSERT INTO `province_city_district` VALUES (450303, 4503, '叠彩区');
INSERT INTO `province_city_district` VALUES (450304, 4503, '象山区');
INSERT INTO `province_city_district` VALUES (450305, 4503, '七星区');
INSERT INTO `province_city_district` VALUES (450311, 4503, '雁山区');
INSERT INTO `province_city_district` VALUES (450321, 4503, '阳朔县');
INSERT INTO `province_city_district` VALUES (450322, 4503, '临桂县');
INSERT INTO `province_city_district` VALUES (450323, 4503, '灵川县');
INSERT INTO `province_city_district` VALUES (450324, 4503, '全州县');
INSERT INTO `province_city_district` VALUES (450325, 4503, '兴安县');
INSERT INTO `province_city_district` VALUES (450326, 4503, '永福县');
INSERT INTO `province_city_district` VALUES (450327, 4503, '灌阳县');
INSERT INTO `province_city_district` VALUES (450328, 4503, '龙胜各县自治区');
INSERT INTO `province_city_district` VALUES (450329, 4503, '资源县');
INSERT INTO `province_city_district` VALUES (450330, 4503, '平乐县');
INSERT INTO `province_city_district` VALUES (450331, 4503, '荔蒲县');
INSERT INTO `province_city_district` VALUES (450332, 4503, '恭城瑶族自治县');
INSERT INTO `province_city_district` VALUES (450401, 4504, '市辖区');
INSERT INTO `province_city_district` VALUES (450403, 4504, '万秀区');
INSERT INTO `province_city_district` VALUES (450404, 4504, '蝶山区');
INSERT INTO `province_city_district` VALUES (450411, 4504, '市郊区');
INSERT INTO `province_city_district` VALUES (450421, 4504, '苍梧县');
INSERT INTO `province_city_district` VALUES (450422, 4504, '藤  县');
INSERT INTO `province_city_district` VALUES (450423, 4504, '蒙山县');
INSERT INTO `province_city_district` VALUES (450481, 4504, '岑溪市');
INSERT INTO `province_city_district` VALUES (450501, 4505, '市辖区');
INSERT INTO `province_city_district` VALUES (450502, 4505, '海城区');
INSERT INTO `province_city_district` VALUES (450503, 4505, '银海区');
INSERT INTO `province_city_district` VALUES (450512, 4505, '铁山港区');
INSERT INTO `province_city_district` VALUES (450521, 4505, '合浦县');
INSERT INTO `province_city_district` VALUES (450601, 4506, '市辖区');
INSERT INTO `province_city_district` VALUES (450602, 4506, '港口区');
INSERT INTO `province_city_district` VALUES (450603, 4506, '防城区');
INSERT INTO `province_city_district` VALUES (450621, 4506, '上思县');
INSERT INTO `province_city_district` VALUES (450681, 4506, '东兴市');
INSERT INTO `province_city_district` VALUES (450701, 4507, '市辖区');
INSERT INTO `province_city_district` VALUES (450702, 4507, '钦南区');
INSERT INTO `province_city_district` VALUES (450703, 4507, '钦北区');
INSERT INTO `province_city_district` VALUES (450721, 4507, '浦北县');
INSERT INTO `province_city_district` VALUES (450722, 4507, '灵山县');
INSERT INTO `province_city_district` VALUES (450801, 4508, '市辖区');
INSERT INTO `province_city_district` VALUES (450802, 4508, '港北区');
INSERT INTO `province_city_district` VALUES (450803, 4508, '港南区');
INSERT INTO `province_city_district` VALUES (450821, 4508, '平南县');
INSERT INTO `province_city_district` VALUES (450881, 4508, '桂平市');
INSERT INTO `province_city_district` VALUES (450901, 4509, '市辖区');
INSERT INTO `province_city_district` VALUES (450902, 4509, '玉州区');
INSERT INTO `province_city_district` VALUES (450921, 4509, '容  县');
INSERT INTO `province_city_district` VALUES (450922, 4509, '陆川县');
INSERT INTO `province_city_district` VALUES (450923, 4509, '博白县');
INSERT INTO `province_city_district` VALUES (450924, 4509, '兴业县');
INSERT INTO `province_city_district` VALUES (450981, 4509, '北流市');
INSERT INTO `province_city_district` VALUES (452101, 4521, '凭祥市');
INSERT INTO `province_city_district` VALUES (452122, 4521, '横  县');
INSERT INTO `province_city_district` VALUES (452123, 4521, '宾阳县');
INSERT INTO `province_city_district` VALUES (452124, 4521, '上林县');
INSERT INTO `province_city_district` VALUES (452126, 4521, '隆安县');
INSERT INTO `province_city_district` VALUES (452127, 4521, '马山县');
INSERT INTO `province_city_district` VALUES (452128, 4521, '扶绥县');
INSERT INTO `province_city_district` VALUES (452129, 4521, '崇左县');
INSERT INTO `province_city_district` VALUES (452130, 4521, '大新县');
INSERT INTO `province_city_district` VALUES (452131, 4521, '天等县');
INSERT INTO `province_city_district` VALUES (452132, 4521, '宁明县');
INSERT INTO `province_city_district` VALUES (452133, 4521, '龙州县');
INSERT INTO `province_city_district` VALUES (452201, 4522, '合山市');
INSERT INTO `province_city_district` VALUES (452223, 4522, '鹿寨县');
INSERT INTO `province_city_district` VALUES (452224, 4522, '象州县');
INSERT INTO `province_city_district` VALUES (452225, 4522, '武宣县');
INSERT INTO `province_city_district` VALUES (452226, 4522, '来宾县');
INSERT INTO `province_city_district` VALUES (452227, 4522, '融安县');
INSERT INTO `province_city_district` VALUES (452228, 4522, '三江侗族自治县');
INSERT INTO `province_city_district` VALUES (452229, 4522, '融水苗族自治县');
INSERT INTO `province_city_district` VALUES (452230, 4522, '金秀瑶族自治县');
INSERT INTO `province_city_district` VALUES (452231, 4522, '忻城县');
INSERT INTO `province_city_district` VALUES (452402, 4524, '贺州市');
INSERT INTO `province_city_district` VALUES (452424, 4524, '昭平县');
INSERT INTO `province_city_district` VALUES (452427, 4524, '钟山县');
INSERT INTO `province_city_district` VALUES (452428, 4524, '富川瑶族自治县');
INSERT INTO `province_city_district` VALUES (452601, 4526, '百色市');
INSERT INTO `province_city_district` VALUES (452622, 4526, '田阳县');
INSERT INTO `province_city_district` VALUES (452623, 4526, '田东县');
INSERT INTO `province_city_district` VALUES (452624, 4526, '平果县');
INSERT INTO `province_city_district` VALUES (452625, 4526, '德保县');
INSERT INTO `province_city_district` VALUES (452626, 4526, '靖西县');
INSERT INTO `province_city_district` VALUES (452627, 4526, '那坡县');
INSERT INTO `province_city_district` VALUES (452628, 4526, '凌云县');
INSERT INTO `province_city_district` VALUES (452629, 4526, '乐业县');
INSERT INTO `province_city_district` VALUES (452630, 4526, '田林县');
INSERT INTO `province_city_district` VALUES (452631, 4526, '隆林各族自治县');
INSERT INTO `province_city_district` VALUES (452632, 4526, '西林县');
INSERT INTO `province_city_district` VALUES (452701, 4527, '河池市');
INSERT INTO `province_city_district` VALUES (452702, 4527, '宜州市');
INSERT INTO `province_city_district` VALUES (452723, 4527, '罗城仫佬族自治县');
INSERT INTO `province_city_district` VALUES (452724, 4527, '环江毛南族自治县');
INSERT INTO `province_city_district` VALUES (452725, 4527, '南丹县');
INSERT INTO `province_city_district` VALUES (452726, 4527, '天峨县');
INSERT INTO `province_city_district` VALUES (452727, 4527, '凤山县');
INSERT INTO `province_city_district` VALUES (452728, 4527, '东兰县');
INSERT INTO `province_city_district` VALUES (452729, 4527, '巴马瑶族自治县');
INSERT INTO `province_city_district` VALUES (452730, 4527, '都安瑶族自治县');
INSERT INTO `province_city_district` VALUES (452731, 4527, '大化瑶族自治县');
INSERT INTO `province_city_district` VALUES (460101, 4601, '通什市');
INSERT INTO `province_city_district` VALUES (460102, 4601, '琼海市');
INSERT INTO `province_city_district` VALUES (460103, 4601, '儋州市');
INSERT INTO `province_city_district` VALUES (460104, 4601, '琼山市');
INSERT INTO `province_city_district` VALUES (460105, 4601, '文昌市');
INSERT INTO `province_city_district` VALUES (460106, 4601, '万宁市');
INSERT INTO `province_city_district` VALUES (460107, 4601, '东方市');
INSERT INTO `province_city_district` VALUES (460125, 4601, '定安县');
INSERT INTO `province_city_district` VALUES (460126, 4601, '屯昌县');
INSERT INTO `province_city_district` VALUES (460127, 4601, '澄迈县');
INSERT INTO `province_city_district` VALUES (460128, 4601, '临高县');
INSERT INTO `province_city_district` VALUES (460130, 4601, '白沙黎族自治县');
INSERT INTO `province_city_district` VALUES (460131, 4601, '昌江黎族自治县');
INSERT INTO `province_city_district` VALUES (460133, 4601, '乐东黎族自治县');
INSERT INTO `province_city_district` VALUES (460134, 4601, '陵水黎族自治县');
INSERT INTO `province_city_district` VALUES (460135, 4601, '保亭黎族苗族自治县');
INSERT INTO `province_city_district` VALUES (460136, 4601, '琼中黎族苗族自治县');
INSERT INTO `province_city_district` VALUES (460137, 4601, '西沙群岛');
INSERT INTO `province_city_district` VALUES (460138, 4601, '南沙群岛');
INSERT INTO `province_city_district` VALUES (460139, 4601, '中沙群岛的岛礁及其海');
INSERT INTO `province_city_district` VALUES (460201, 4602, '市辖区');
INSERT INTO `province_city_district` VALUES (460202, 4602, '振东区');
INSERT INTO `province_city_district` VALUES (460203, 4602, '新华区');
INSERT INTO `province_city_district` VALUES (460204, 4602, '秀英区');
INSERT INTO `province_city_district` VALUES (460301, 4603, '市辖区');
INSERT INTO `province_city_district` VALUES (500101, 5001, '万州区');
INSERT INTO `province_city_district` VALUES (500102, 5001, '涪陵区');
INSERT INTO `province_city_district` VALUES (500103, 5001, '渝中区');
INSERT INTO `province_city_district` VALUES (500104, 5001, '大渡口区');
INSERT INTO `province_city_district` VALUES (500105, 5001, '江北区');
INSERT INTO `province_city_district` VALUES (500106, 5001, '沙坪坝区');
INSERT INTO `province_city_district` VALUES (500107, 5001, '九龙坡区');
INSERT INTO `province_city_district` VALUES (500108, 5001, '南岸区');
INSERT INTO `province_city_district` VALUES (500109, 5001, '北碚区');
INSERT INTO `province_city_district` VALUES (500110, 5001, '万盛区');
INSERT INTO `province_city_district` VALUES (500111, 5001, '双桥区');
INSERT INTO `province_city_district` VALUES (500112, 5001, '渝北区');
INSERT INTO `province_city_district` VALUES (500113, 5001, '巴南区');
INSERT INTO `province_city_district` VALUES (500114, 5001, '黔江区');
INSERT INTO `province_city_district` VALUES (500221, 5002, '长寿县');
INSERT INTO `province_city_district` VALUES (500222, 5002, '綦江县');
INSERT INTO `province_city_district` VALUES (500223, 5002, '潼南县');
INSERT INTO `province_city_district` VALUES (500224, 5002, '铜梁县');
INSERT INTO `province_city_district` VALUES (500225, 5002, '大足县');
INSERT INTO `province_city_district` VALUES (500226, 5002, '荣昌县');
INSERT INTO `province_city_district` VALUES (500227, 5002, '璧山县');
INSERT INTO `province_city_district` VALUES (500228, 5002, '梁平县');
INSERT INTO `province_city_district` VALUES (500229, 5002, '城口县');
INSERT INTO `province_city_district` VALUES (500230, 5002, '丰都县');
INSERT INTO `province_city_district` VALUES (500231, 5002, '垫江县');
INSERT INTO `province_city_district` VALUES (500232, 5002, '武隆县');
INSERT INTO `province_city_district` VALUES (500233, 5002, '忠  县');
INSERT INTO `province_city_district` VALUES (500234, 5002, '开  县');
INSERT INTO `province_city_district` VALUES (500235, 5002, '云阳县');
INSERT INTO `province_city_district` VALUES (500236, 5002, '奉节县');
INSERT INTO `province_city_district` VALUES (500237, 5002, '巫山县');
INSERT INTO `province_city_district` VALUES (500238, 5002, '巫溪县');
INSERT INTO `province_city_district` VALUES (500240, 5002, '石柱土家族自治县');
INSERT INTO `province_city_district` VALUES (500241, 5002, '秀山土家族苗族自治县');
INSERT INTO `province_city_district` VALUES (500242, 5002, '酉阳土家族苗族自治县');
INSERT INTO `province_city_district` VALUES (500243, 5002, '彭水苗族土家族自治县');
INSERT INTO `province_city_district` VALUES (500381, 5003, '江津市');
INSERT INTO `province_city_district` VALUES (500382, 5003, '合川市');
INSERT INTO `province_city_district` VALUES (500383, 5003, '永川市');
INSERT INTO `province_city_district` VALUES (500384, 5003, '南川市');
INSERT INTO `province_city_district` VALUES (510101, 5101, '市辖区');
INSERT INTO `province_city_district` VALUES (510103, 5101, '高新区');
INSERT INTO `province_city_district` VALUES (510104, 5101, '锦江区');
INSERT INTO `province_city_district` VALUES (510105, 5101, '青羊区');
INSERT INTO `province_city_district` VALUES (510106, 5101, '金牛区');
INSERT INTO `province_city_district` VALUES (510107, 5101, '武侯区');
INSERT INTO `province_city_district` VALUES (510108, 5101, '成华区');
INSERT INTO `province_city_district` VALUES (510112, 5101, '龙泉驿区');
INSERT INTO `province_city_district` VALUES (510113, 5101, '青白江区');
INSERT INTO `province_city_district` VALUES (510121, 5101, '金堂县');
INSERT INTO `province_city_district` VALUES (510122, 5101, '双流县');
INSERT INTO `province_city_district` VALUES (510123, 5101, '温江县');
INSERT INTO `province_city_district` VALUES (510124, 5101, '郫  县');
INSERT INTO `province_city_district` VALUES (510125, 5101, '新都县');
INSERT INTO `province_city_district` VALUES (510129, 5101, '大邑县');
INSERT INTO `province_city_district` VALUES (510131, 5101, '蒲江县');
INSERT INTO `province_city_district` VALUES (510132, 5101, '新津县');
INSERT INTO `province_city_district` VALUES (510181, 5101, '都江堰市');
INSERT INTO `province_city_district` VALUES (510182, 5101, '彭州市');
INSERT INTO `province_city_district` VALUES (510183, 5101, '邛崃市');
INSERT INTO `province_city_district` VALUES (510184, 5101, '崇州市');
INSERT INTO `province_city_district` VALUES (510301, 5103, '市辖区');
INSERT INTO `province_city_district` VALUES (510302, 5103, '自流井区');
INSERT INTO `province_city_district` VALUES (510303, 5103, '贡井区');
INSERT INTO `province_city_district` VALUES (510304, 5103, '大安区');
INSERT INTO `province_city_district` VALUES (510311, 5103, '沿滩区');
INSERT INTO `province_city_district` VALUES (510321, 5103, '荣  县');
INSERT INTO `province_city_district` VALUES (510322, 5103, '富顺县');
INSERT INTO `province_city_district` VALUES (510401, 5104, '市辖区');
INSERT INTO `province_city_district` VALUES (510402, 5104, '东  区');
INSERT INTO `province_city_district` VALUES (510403, 5104, '西  区');
INSERT INTO `province_city_district` VALUES (510411, 5104, '仁和区');
INSERT INTO `province_city_district` VALUES (510421, 5104, '米易县');
INSERT INTO `province_city_district` VALUES (510422, 5104, '盐边县');
INSERT INTO `province_city_district` VALUES (510501, 5105, '市辖区');
INSERT INTO `province_city_district` VALUES (510502, 5105, '江阳区');
INSERT INTO `province_city_district` VALUES (510503, 5105, '纳溪区');
INSERT INTO `province_city_district` VALUES (510504, 5105, '龙马潭区');
INSERT INTO `province_city_district` VALUES (510521, 5105, '泸  县');
INSERT INTO `province_city_district` VALUES (510522, 5105, '合江县');
INSERT INTO `province_city_district` VALUES (510524, 5105, '叙永县');
INSERT INTO `province_city_district` VALUES (510525, 5105, '古蔺县');
INSERT INTO `province_city_district` VALUES (510601, 5106, '市辖区');
INSERT INTO `province_city_district` VALUES (510603, 5106, '旌阳区');
INSERT INTO `province_city_district` VALUES (510623, 5106, '中江县');
INSERT INTO `province_city_district` VALUES (510626, 5106, '罗江县');
INSERT INTO `province_city_district` VALUES (510681, 5106, '广汉市');
INSERT INTO `province_city_district` VALUES (510682, 5106, '什邡市');
INSERT INTO `province_city_district` VALUES (510683, 5106, '绵竹市');
INSERT INTO `province_city_district` VALUES (510701, 5107, '市辖区');
INSERT INTO `province_city_district` VALUES (510703, 5107, '涪城区');
INSERT INTO `province_city_district` VALUES (510704, 5107, '游仙区');
INSERT INTO `province_city_district` VALUES (510710, 5107, '科学城区');
INSERT INTO `province_city_district` VALUES (510722, 5107, '三台县');
INSERT INTO `province_city_district` VALUES (510723, 5107, '盐亭县');
INSERT INTO `province_city_district` VALUES (510724, 5107, '安  县');
INSERT INTO `province_city_district` VALUES (510725, 5107, '梓潼县');
INSERT INTO `province_city_district` VALUES (510726, 5107, '北川县');
INSERT INTO `province_city_district` VALUES (510727, 5107, '平武县');
INSERT INTO `province_city_district` VALUES (510781, 5107, '江油市');
INSERT INTO `province_city_district` VALUES (510801, 5108, '市辖区');
INSERT INTO `province_city_district` VALUES (510802, 5108, '市中区');
INSERT INTO `province_city_district` VALUES (510811, 5108, '元坝区');
INSERT INTO `province_city_district` VALUES (510812, 5108, '朝天区');
INSERT INTO `province_city_district` VALUES (510821, 5108, '旺苍县');
INSERT INTO `province_city_district` VALUES (510822, 5108, '青川县');
INSERT INTO `province_city_district` VALUES (510823, 5108, '剑阁县');
INSERT INTO `province_city_district` VALUES (510824, 5108, '苍溪县');
INSERT INTO `province_city_district` VALUES (510901, 5109, '市辖区');
INSERT INTO `province_city_district` VALUES (510902, 5109, '市中区');
INSERT INTO `province_city_district` VALUES (510921, 5109, '蓬溪县');
INSERT INTO `province_city_district` VALUES (510922, 5109, '射洪县');
INSERT INTO `province_city_district` VALUES (510923, 5109, '大英县');
INSERT INTO `province_city_district` VALUES (511001, 5110, '市辖区');
INSERT INTO `province_city_district` VALUES (511002, 5110, '市中区');
INSERT INTO `province_city_district` VALUES (511011, 5110, '东兴区');
INSERT INTO `province_city_district` VALUES (511024, 5110, '威远县');
INSERT INTO `province_city_district` VALUES (511025, 5110, '资中县');
INSERT INTO `province_city_district` VALUES (511028, 5110, '隆昌县');
INSERT INTO `province_city_district` VALUES (511101, 5111, '市辖区');
INSERT INTO `province_city_district` VALUES (511102, 5111, '市中区');
INSERT INTO `province_city_district` VALUES (511111, 5111, '沙湾区');
INSERT INTO `province_city_district` VALUES (511112, 5111, '五通桥区');
INSERT INTO `province_city_district` VALUES (511113, 5111, '金口河区');
INSERT INTO `province_city_district` VALUES (511123, 5111, '犍为县');
INSERT INTO `province_city_district` VALUES (511124, 5111, '井研县');
INSERT INTO `province_city_district` VALUES (511126, 5111, '夹江县');
INSERT INTO `province_city_district` VALUES (511129, 5111, '沐川县');
INSERT INTO `province_city_district` VALUES (511132, 5111, '峨边彝族自治县');
INSERT INTO `province_city_district` VALUES (511133, 5111, '马边彝族自治县');
INSERT INTO `province_city_district` VALUES (511181, 5111, '峨眉山市');
INSERT INTO `province_city_district` VALUES (511301, 5113, '市辖区');
INSERT INTO `province_city_district` VALUES (511302, 5113, '顺庆区');
INSERT INTO `province_city_district` VALUES (511303, 5113, '高坪区');
INSERT INTO `province_city_district` VALUES (511304, 5113, '嘉陵区');
INSERT INTO `province_city_district` VALUES (511321, 5113, '南部县');
INSERT INTO `province_city_district` VALUES (511322, 5113, '营山县');
INSERT INTO `province_city_district` VALUES (511323, 5113, '蓬安县');
INSERT INTO `province_city_district` VALUES (511324, 5113, '仪陇县');
INSERT INTO `province_city_district` VALUES (511325, 5113, '西充县');
INSERT INTO `province_city_district` VALUES (511381, 5113, '阆中市');
INSERT INTO `province_city_district` VALUES (511401, 5114, '市辖区');
INSERT INTO `province_city_district` VALUES (511402, 5114, '东坡区');
INSERT INTO `province_city_district` VALUES (511421, 5114, '仁寿县');
INSERT INTO `province_city_district` VALUES (511422, 5114, '彭山县');
INSERT INTO `province_city_district` VALUES (511423, 5114, '洪雅县');
INSERT INTO `province_city_district` VALUES (511424, 5114, '丹棱县');
INSERT INTO `province_city_district` VALUES (511425, 5114, '青神县');
INSERT INTO `province_city_district` VALUES (511501, 5115, '市辖区');
INSERT INTO `province_city_district` VALUES (511502, 5115, '翠屏区');
INSERT INTO `province_city_district` VALUES (511521, 5115, '宜宾县');
INSERT INTO `province_city_district` VALUES (511522, 5115, '南溪县');
INSERT INTO `province_city_district` VALUES (511523, 5115, '江安县');
INSERT INTO `province_city_district` VALUES (511524, 5115, '长宁县');
INSERT INTO `province_city_district` VALUES (511525, 5115, '高  县');
INSERT INTO `province_city_district` VALUES (511526, 5115, '珙  县');
INSERT INTO `province_city_district` VALUES (511527, 5115, '筠连县');
INSERT INTO `province_city_district` VALUES (511528, 5115, '兴文县');
INSERT INTO `province_city_district` VALUES (511529, 5115, '屏山县');
INSERT INTO `province_city_district` VALUES (511601, 5116, '市辖区');
INSERT INTO `province_city_district` VALUES (511602, 5116, '广安区');
INSERT INTO `province_city_district` VALUES (511621, 5116, '岳池县');
INSERT INTO `province_city_district` VALUES (511622, 5116, '武胜县');
INSERT INTO `province_city_district` VALUES (511623, 5116, '邻水县');
INSERT INTO `province_city_district` VALUES (511681, 5116, '华蓥市');
INSERT INTO `province_city_district` VALUES (511701, 5117, '市辖区');
INSERT INTO `province_city_district` VALUES (511702, 5117, '通川区');
INSERT INTO `province_city_district` VALUES (511721, 5117, '达  县');
INSERT INTO `province_city_district` VALUES (511722, 5117, '宣汉县');
INSERT INTO `province_city_district` VALUES (511723, 5117, '开江县');
INSERT INTO `province_city_district` VALUES (511724, 5117, '大竹县');
INSERT INTO `province_city_district` VALUES (511725, 5117, '渠  县');
INSERT INTO `province_city_district` VALUES (511781, 5117, '万源市');
INSERT INTO `province_city_district` VALUES (511801, 5118, '市辖区');
INSERT INTO `province_city_district` VALUES (511802, 5118, '雨城区');
INSERT INTO `province_city_district` VALUES (511821, 5118, '名山县');
INSERT INTO `province_city_district` VALUES (511822, 5118, '荥经县');
INSERT INTO `province_city_district` VALUES (511823, 5118, '汉源县');
INSERT INTO `province_city_district` VALUES (511824, 5118, '石棉县');
INSERT INTO `province_city_district` VALUES (511825, 5118, '天全县');
INSERT INTO `province_city_district` VALUES (511826, 5118, '芦山县');
INSERT INTO `province_city_district` VALUES (511827, 5118, '宝兴县');
INSERT INTO `province_city_district` VALUES (511901, 5119, '市辖区');
INSERT INTO `province_city_district` VALUES (511902, 5119, '巴州区');
INSERT INTO `province_city_district` VALUES (511921, 5119, '通江县');
INSERT INTO `province_city_district` VALUES (511922, 5119, '南江县');
INSERT INTO `province_city_district` VALUES (511923, 5119, '平昌县');
INSERT INTO `province_city_district` VALUES (512001, 5120, '市辖区');
INSERT INTO `province_city_district` VALUES (512002, 5120, '雁江区');
INSERT INTO `province_city_district` VALUES (512021, 5120, '安岳县');
INSERT INTO `province_city_district` VALUES (512022, 5120, '乐至县');
INSERT INTO `province_city_district` VALUES (512081, 5120, '简阳市');
INSERT INTO `province_city_district` VALUES (513221, 5132, '汶川县');
INSERT INTO `province_city_district` VALUES (513222, 5132, '理  县');
INSERT INTO `province_city_district` VALUES (513223, 5132, '茂  县');
INSERT INTO `province_city_district` VALUES (513224, 5132, '松潘县');
INSERT INTO `province_city_district` VALUES (513225, 5132, '九寨沟县');
INSERT INTO `province_city_district` VALUES (513226, 5132, '金川县');
INSERT INTO `province_city_district` VALUES (513227, 5132, '小金县');
INSERT INTO `province_city_district` VALUES (513228, 5132, '黑水县');
INSERT INTO `province_city_district` VALUES (513229, 5132, '马尔康县');
INSERT INTO `province_city_district` VALUES (513230, 5132, '壤塘县');
INSERT INTO `province_city_district` VALUES (513231, 5132, '阿坝县');
INSERT INTO `province_city_district` VALUES (513232, 5132, '若尔盖县');
INSERT INTO `province_city_district` VALUES (513233, 5132, '红原县');
INSERT INTO `province_city_district` VALUES (513321, 5133, '康定县');
INSERT INTO `province_city_district` VALUES (513322, 5133, '泸定县');
INSERT INTO `province_city_district` VALUES (513323, 5133, '丹巴县');
INSERT INTO `province_city_district` VALUES (513324, 5133, '九龙县');
INSERT INTO `province_city_district` VALUES (513325, 5133, '雅江县');
INSERT INTO `province_city_district` VALUES (513326, 5133, '道孚县');
INSERT INTO `province_city_district` VALUES (513327, 5133, '炉霍县');
INSERT INTO `province_city_district` VALUES (513328, 5133, '甘孜县');
INSERT INTO `province_city_district` VALUES (513329, 5133, '新龙县');
INSERT INTO `province_city_district` VALUES (513330, 5133, '德格县');
INSERT INTO `province_city_district` VALUES (513331, 5133, '白玉县');
INSERT INTO `province_city_district` VALUES (513332, 5133, '石渠县');
INSERT INTO `province_city_district` VALUES (513333, 5133, '色达县');
INSERT INTO `province_city_district` VALUES (513334, 5133, '理塘县');
INSERT INTO `province_city_district` VALUES (513335, 5133, '巴塘县');
INSERT INTO `province_city_district` VALUES (513336, 5133, '乡城县');
INSERT INTO `province_city_district` VALUES (513337, 5133, '稻城县');
INSERT INTO `province_city_district` VALUES (513338, 5133, '得荣县');
INSERT INTO `province_city_district` VALUES (513401, 5134, '西昌市');
INSERT INTO `province_city_district` VALUES (513422, 5134, '木里藏族自治县');
INSERT INTO `province_city_district` VALUES (513423, 5134, '盐源县');
INSERT INTO `province_city_district` VALUES (513424, 5134, '德昌县');
INSERT INTO `province_city_district` VALUES (513425, 5134, '会理县');
INSERT INTO `province_city_district` VALUES (513426, 5134, '会东县');
INSERT INTO `province_city_district` VALUES (513427, 5134, '宁南县');
INSERT INTO `province_city_district` VALUES (513428, 5134, '普格县');
INSERT INTO `province_city_district` VALUES (513429, 5134, '布拖县');
INSERT INTO `province_city_district` VALUES (513430, 5134, '金阳县');
INSERT INTO `province_city_district` VALUES (513431, 5134, '昭觉县');
INSERT INTO `province_city_district` VALUES (513432, 5134, '喜德县');
INSERT INTO `province_city_district` VALUES (513433, 5134, '冕宁县');
INSERT INTO `province_city_district` VALUES (513434, 5134, '越西县');
INSERT INTO `province_city_district` VALUES (513435, 5134, '甘洛县');
INSERT INTO `province_city_district` VALUES (513436, 5134, '美姑县');
INSERT INTO `province_city_district` VALUES (513437, 5134, '雷波县');
INSERT INTO `province_city_district` VALUES (520101, 5201, '市辖区');
INSERT INTO `province_city_district` VALUES (520102, 5201, '南明区');
INSERT INTO `province_city_district` VALUES (520103, 5201, '云岩区');
INSERT INTO `province_city_district` VALUES (520111, 5201, '花溪区');
INSERT INTO `province_city_district` VALUES (520112, 5201, '乌当区');
INSERT INTO `province_city_district` VALUES (520113, 5201, '白云区');
INSERT INTO `province_city_district` VALUES (520114, 5201, '小河区');
INSERT INTO `province_city_district` VALUES (520121, 5201, '开阳县');
INSERT INTO `province_city_district` VALUES (520122, 5201, '息烽县');
INSERT INTO `province_city_district` VALUES (520123, 5201, '修文县');
INSERT INTO `province_city_district` VALUES (520181, 5201, '清镇市');
INSERT INTO `province_city_district` VALUES (520201, 5202, '钟山区');
INSERT INTO `province_city_district` VALUES (520203, 5202, '六枝特区');
INSERT INTO `province_city_district` VALUES (520221, 5202, '水城县');
INSERT INTO `province_city_district` VALUES (520222, 5202, '盘  县');
INSERT INTO `province_city_district` VALUES (520301, 5203, '市辖区');
INSERT INTO `province_city_district` VALUES (520302, 5203, '红花岗区');
INSERT INTO `province_city_district` VALUES (520321, 5203, '遵义县');
INSERT INTO `province_city_district` VALUES (520322, 5203, '桐梓县');
INSERT INTO `province_city_district` VALUES (520323, 5203, '绥阳县');
INSERT INTO `province_city_district` VALUES (520324, 5203, '正安县');
INSERT INTO `province_city_district` VALUES (520325, 5203, '道真仡佬族苗族自治县');
INSERT INTO `province_city_district` VALUES (520326, 5203, '务川仡佬族苗族自治县');
INSERT INTO `province_city_district` VALUES (520327, 5203, '凤冈县');
INSERT INTO `province_city_district` VALUES (520328, 5203, '湄潭县');
INSERT INTO `province_city_district` VALUES (520329, 5203, '余庆县');
INSERT INTO `province_city_district` VALUES (520330, 5203, '习水县');
INSERT INTO `province_city_district` VALUES (520381, 5203, '赤水市');
INSERT INTO `province_city_district` VALUES (520382, 5203, '仁怀市');
INSERT INTO `province_city_district` VALUES (520401, 5204, '市辖区');
INSERT INTO `province_city_district` VALUES (520402, 5204, '西秀区');
INSERT INTO `province_city_district` VALUES (520421, 5204, '平坝县');
INSERT INTO `province_city_district` VALUES (520422, 5204, '普定县');
INSERT INTO `province_city_district` VALUES (520423, 5204, '镇宁布依族苗族自治县');
INSERT INTO `province_city_district` VALUES (520424, 5204, '关岭布依族苗族自治县');
INSERT INTO `province_city_district` VALUES (520425, 5204, '紫云苗族布依族自治县');
INSERT INTO `province_city_district` VALUES (522201, 5222, '铜仁市');
INSERT INTO `province_city_district` VALUES (522222, 5222, '江口县');
INSERT INTO `province_city_district` VALUES (522223, 5222, '玉屏侗族自治县');
INSERT INTO `province_city_district` VALUES (522224, 5222, '石阡县');
INSERT INTO `province_city_district` VALUES (522225, 5222, '思南县');
INSERT INTO `province_city_district` VALUES (522226, 5222, '印江土家族苗族自治县');
INSERT INTO `province_city_district` VALUES (522227, 5222, '德江县');
INSERT INTO `province_city_district` VALUES (522228, 5222, '沿河土家族自治县');
INSERT INTO `province_city_district` VALUES (522229, 5222, '松桃苗族自治县');
INSERT INTO `province_city_district` VALUES (522230, 5222, '万山特区');
INSERT INTO `province_city_district` VALUES (522301, 5223, '兴义市');
INSERT INTO `province_city_district` VALUES (522322, 5223, '兴仁县');
INSERT INTO `province_city_district` VALUES (522323, 5223, '普安县');
INSERT INTO `province_city_district` VALUES (522324, 5223, '晴隆县');
INSERT INTO `province_city_district` VALUES (522325, 5223, '贞丰县');
INSERT INTO `province_city_district` VALUES (522326, 5223, '望谟县');
INSERT INTO `province_city_district` VALUES (522327, 5223, '册亨县');
INSERT INTO `province_city_district` VALUES (522328, 5223, '安龙县');
INSERT INTO `province_city_district` VALUES (522401, 5224, '毕节市');
INSERT INTO `province_city_district` VALUES (522422, 5224, '大方县');
INSERT INTO `province_city_district` VALUES (522423, 5224, '黔西县');
INSERT INTO `province_city_district` VALUES (522424, 5224, '金沙县');
INSERT INTO `province_city_district` VALUES (522425, 5224, '织金县');
INSERT INTO `province_city_district` VALUES (522426, 5224, '纳雍县');
INSERT INTO `province_city_district` VALUES (522427, 5224, '威宁彝族回族苗族自治');
INSERT INTO `province_city_district` VALUES (522428, 5224, '赫章县');
INSERT INTO `province_city_district` VALUES (522601, 5226, '凯里市');
INSERT INTO `province_city_district` VALUES (522622, 5226, '黄平县');
INSERT INTO `province_city_district` VALUES (522623, 5226, '施秉县');
INSERT INTO `province_city_district` VALUES (522624, 5226, '三穗县');
INSERT INTO `province_city_district` VALUES (522625, 5226, '镇远县');
INSERT INTO `province_city_district` VALUES (522626, 5226, '岑巩县');
INSERT INTO `province_city_district` VALUES (522627, 5226, '天柱县');
INSERT INTO `province_city_district` VALUES (522628, 5226, '锦屏县');
INSERT INTO `province_city_district` VALUES (522629, 5226, '剑河县');
INSERT INTO `province_city_district` VALUES (522630, 5226, '台江县');
INSERT INTO `province_city_district` VALUES (522631, 5226, '黎平县');
INSERT INTO `province_city_district` VALUES (522632, 5226, '榕江县');
INSERT INTO `province_city_district` VALUES (522633, 5226, '从江县');
INSERT INTO `province_city_district` VALUES (522634, 5226, '雷山县');
INSERT INTO `province_city_district` VALUES (522635, 5226, '麻江县');
INSERT INTO `province_city_district` VALUES (522636, 5226, '丹寨县');
INSERT INTO `province_city_district` VALUES (522701, 5227, '都匀市');
INSERT INTO `province_city_district` VALUES (522702, 5227, '福泉市');
INSERT INTO `province_city_district` VALUES (522722, 5227, '荔波县');
INSERT INTO `province_city_district` VALUES (522723, 5227, '贵定县');
INSERT INTO `province_city_district` VALUES (522725, 5227, '瓮安县');
INSERT INTO `province_city_district` VALUES (522726, 5227, '独山县');
INSERT INTO `province_city_district` VALUES (522727, 5227, '平塘县');
INSERT INTO `province_city_district` VALUES (522728, 5227, '罗甸县');
INSERT INTO `province_city_district` VALUES (522729, 5227, '长顺县');
INSERT INTO `province_city_district` VALUES (522730, 5227, '龙里县');
INSERT INTO `province_city_district` VALUES (522731, 5227, '惠水县');
INSERT INTO `province_city_district` VALUES (522732, 5227, '三都水族自治县');
INSERT INTO `province_city_district` VALUES (530101, 5301, '市辖区');
INSERT INTO `province_city_district` VALUES (530102, 5301, '五华区');
INSERT INTO `province_city_district` VALUES (530103, 5301, '盘龙区');
INSERT INTO `province_city_district` VALUES (530111, 5301, '官渡区');
INSERT INTO `province_city_district` VALUES (530112, 5301, '西山区');
INSERT INTO `province_city_district` VALUES (530113, 5301, '东川区');
INSERT INTO `province_city_district` VALUES (530121, 5301, '呈贡县');
INSERT INTO `province_city_district` VALUES (530122, 5301, '晋宁县');
INSERT INTO `province_city_district` VALUES (530124, 5301, '富民县');
INSERT INTO `province_city_district` VALUES (530125, 5301, '宜良县');
INSERT INTO `province_city_district` VALUES (530126, 5301, '石林彝族自治县');
INSERT INTO `province_city_district` VALUES (530127, 5301, '嵩明县');
INSERT INTO `province_city_district` VALUES (530128, 5301, '禄劝彝族苗族自治县');
INSERT INTO `province_city_district` VALUES (530129, 5301, '寻甸回族彝族自治县');
INSERT INTO `province_city_district` VALUES (530181, 5301, '安宁市');
INSERT INTO `province_city_district` VALUES (530301, 5303, '市辖区');
INSERT INTO `province_city_district` VALUES (530302, 5303, '麒麟区');
INSERT INTO `province_city_district` VALUES (530321, 5303, '马龙县');
INSERT INTO `province_city_district` VALUES (530322, 5303, '陆良县');
INSERT INTO `province_city_district` VALUES (530323, 5303, '师宗县');
INSERT INTO `province_city_district` VALUES (530324, 5303, '罗平县');
INSERT INTO `province_city_district` VALUES (530325, 5303, '富源县');
INSERT INTO `province_city_district` VALUES (530326, 5303, '会泽县');
INSERT INTO `province_city_district` VALUES (530328, 5303, '沾益县');
INSERT INTO `province_city_district` VALUES (530381, 5303, '宣威市');
INSERT INTO `province_city_district` VALUES (530401, 5304, '市辖区');
INSERT INTO `province_city_district` VALUES (530402, 5304, '红塔区');
INSERT INTO `province_city_district` VALUES (530421, 5304, '江川县');
INSERT INTO `province_city_district` VALUES (530422, 5304, '澄江县');
INSERT INTO `province_city_district` VALUES (530423, 5304, '通海县');
INSERT INTO `province_city_district` VALUES (530424, 5304, '华宁县');
INSERT INTO `province_city_district` VALUES (530425, 5304, '易门县');
INSERT INTO `province_city_district` VALUES (530426, 5304, '峨山彝族自治县');
INSERT INTO `province_city_district` VALUES (530427, 5304, '新平彝族傣族自治县');
INSERT INTO `province_city_district` VALUES (530428, 5304, '元江哈尼族彝族傣族自');
INSERT INTO `province_city_district` VALUES (530501, 5305, '市辖区');
INSERT INTO `province_city_district` VALUES (530502, 5305, '隆阳区');
INSERT INTO `province_city_district` VALUES (530521, 5305, '施甸县');
INSERT INTO `province_city_district` VALUES (530522, 5305, '腾冲县');
INSERT INTO `province_city_district` VALUES (530523, 5305, '龙陵县');
INSERT INTO `province_city_district` VALUES (530524, 5305, '昌宁县');
INSERT INTO `province_city_district` VALUES (532101, 5321, '昭通市');
INSERT INTO `province_city_district` VALUES (532122, 5321, '鲁甸县');
INSERT INTO `province_city_district` VALUES (532123, 5321, '巧家县');
INSERT INTO `province_city_district` VALUES (532124, 5321, '盐津县');
INSERT INTO `province_city_district` VALUES (532125, 5321, '大关县');
INSERT INTO `province_city_district` VALUES (532126, 5321, '永善县');
INSERT INTO `province_city_district` VALUES (532127, 5321, '绥江县');
INSERT INTO `province_city_district` VALUES (532128, 5321, '镇雄县');
INSERT INTO `province_city_district` VALUES (532129, 5321, '彝良县');
INSERT INTO `province_city_district` VALUES (532130, 5321, '威信县');
INSERT INTO `province_city_district` VALUES (532131, 5321, '水富县');
INSERT INTO `province_city_district` VALUES (532301, 5323, '楚雄市');
INSERT INTO `province_city_district` VALUES (532322, 5323, '双柏县');
INSERT INTO `province_city_district` VALUES (532323, 5323, '牟定县');
INSERT INTO `province_city_district` VALUES (532324, 5323, '南华县');
INSERT INTO `province_city_district` VALUES (532325, 5323, '姚安县');
INSERT INTO `province_city_district` VALUES (532326, 5323, '大姚县');
INSERT INTO `province_city_district` VALUES (532327, 5323, '永仁县');
INSERT INTO `province_city_district` VALUES (532328, 5323, '元谋县');
INSERT INTO `province_city_district` VALUES (532329, 5323, '武定县');
INSERT INTO `province_city_district` VALUES (532331, 5323, '禄丰县');
INSERT INTO `province_city_district` VALUES (532501, 5325, '个旧市');
INSERT INTO `province_city_district` VALUES (532502, 5325, '开远市');
INSERT INTO `province_city_district` VALUES (532522, 5325, '蒙自县');
INSERT INTO `province_city_district` VALUES (532523, 5325, '屏边苗族自治县');
INSERT INTO `province_city_district` VALUES (532524, 5325, '建水县');
INSERT INTO `province_city_district` VALUES (532525, 5325, '石屏县');
INSERT INTO `province_city_district` VALUES (532526, 5325, '弥勒县');
INSERT INTO `province_city_district` VALUES (532527, 5325, '泸西县');
INSERT INTO `province_city_district` VALUES (532528, 5325, '元阳县');
INSERT INTO `province_city_district` VALUES (532529, 5325, '红河县');
INSERT INTO `province_city_district` VALUES (532530, 5325, '金平苗族瑶族傣族自治');
INSERT INTO `province_city_district` VALUES (532531, 5325, '绿春县');
INSERT INTO `province_city_district` VALUES (532532, 5325, '河口瑶族自治县');
INSERT INTO `province_city_district` VALUES (532621, 5326, '文山县');
INSERT INTO `province_city_district` VALUES (532622, 5326, '砚山县');
INSERT INTO `province_city_district` VALUES (532623, 5326, '西畴县');
INSERT INTO `province_city_district` VALUES (532624, 5326, '麻栗坡县');
INSERT INTO `province_city_district` VALUES (532625, 5326, '马关县');
INSERT INTO `province_city_district` VALUES (532626, 5326, '丘北县');
INSERT INTO `province_city_district` VALUES (532627, 5326, '广南县');
INSERT INTO `province_city_district` VALUES (532628, 5326, '富宁县');
INSERT INTO `province_city_district` VALUES (532701, 5327, '思茅市');
INSERT INTO `province_city_district` VALUES (532722, 5327, '普洱哈尼族彝族自治县');
INSERT INTO `province_city_district` VALUES (532723, 5327, '墨江哈尼族自治县');
INSERT INTO `province_city_district` VALUES (532724, 5327, '景东彝族自治县');
INSERT INTO `province_city_district` VALUES (532725, 5327, '景谷傣族彝族自治县');
INSERT INTO `province_city_district` VALUES (532726, 5327, '镇沅彝族哈尼族拉祜族');
INSERT INTO `province_city_district` VALUES (532727, 5327, '江城哈尼族彝族自治县');
INSERT INTO `province_city_district` VALUES (532728, 5327, '孟连傣族拉祜族佤族自');
INSERT INTO `province_city_district` VALUES (532729, 5327, '澜沧拉祜族自治县');
INSERT INTO `province_city_district` VALUES (532730, 5327, '西盟佤族自治县');
INSERT INTO `province_city_district` VALUES (532801, 5328, '景洪市');
INSERT INTO `province_city_district` VALUES (532822, 5328, '勐海县');
INSERT INTO `province_city_district` VALUES (532823, 5328, '勐腊县');
INSERT INTO `province_city_district` VALUES (532901, 5329, '大理市');
INSERT INTO `province_city_district` VALUES (532922, 5329, '漾濞彝族自治县');
INSERT INTO `province_city_district` VALUES (532923, 5329, '祥云县');
INSERT INTO `province_city_district` VALUES (532924, 5329, '宾川县');
INSERT INTO `province_city_district` VALUES (532925, 5329, '弥渡县');
INSERT INTO `province_city_district` VALUES (532926, 5329, '南涧彝族自治县');
INSERT INTO `province_city_district` VALUES (532927, 5329, '巍山彝族回族自治县');
INSERT INTO `province_city_district` VALUES (532928, 5329, '永平县');
INSERT INTO `province_city_district` VALUES (532929, 5329, '云龙县');
INSERT INTO `province_city_district` VALUES (532930, 5329, '洱源县');
INSERT INTO `province_city_district` VALUES (532931, 5329, '剑川县');
INSERT INTO `province_city_district` VALUES (532932, 5329, '鹤庆县');
INSERT INTO `province_city_district` VALUES (533102, 5331, '瑞丽市');
INSERT INTO `province_city_district` VALUES (533103, 5331, '潞西市');
INSERT INTO `province_city_district` VALUES (533122, 5331, '梁河县');
INSERT INTO `province_city_district` VALUES (533123, 5331, '盈江县');
INSERT INTO `province_city_district` VALUES (533124, 5331, '陇川县');
INSERT INTO `province_city_district` VALUES (533221, 5332, '丽江纳西族自治县');
INSERT INTO `province_city_district` VALUES (533222, 5332, '永胜县');
INSERT INTO `province_city_district` VALUES (533223, 5332, '华坪县');
INSERT INTO `province_city_district` VALUES (533224, 5332, '宁蒗彝族自治县');
INSERT INTO `province_city_district` VALUES (533321, 5333, '泸水县');
INSERT INTO `province_city_district` VALUES (533323, 5333, '福贡县');
INSERT INTO `province_city_district` VALUES (533324, 5333, '贡山独龙族怒族自治县');
INSERT INTO `province_city_district` VALUES (533325, 5333, '兰坪白族普米族自治县');
INSERT INTO `province_city_district` VALUES (533421, 5334, '中甸县');
INSERT INTO `province_city_district` VALUES (533422, 5334, '德钦县');
INSERT INTO `province_city_district` VALUES (533423, 5334, '维西傈僳族自治县');
INSERT INTO `province_city_district` VALUES (533521, 5335, '临沧县');
INSERT INTO `province_city_district` VALUES (533522, 5335, '凤庆县');
INSERT INTO `province_city_district` VALUES (533523, 5335, '云  县');
INSERT INTO `province_city_district` VALUES (533524, 5335, '永德县');
INSERT INTO `province_city_district` VALUES (533525, 5335, '镇康县');
INSERT INTO `province_city_district` VALUES (533526, 5335, '双江拉祜族佤族布朗族');
INSERT INTO `province_city_district` VALUES (533527, 5335, '耿马傣族佤族自治县');
INSERT INTO `province_city_district` VALUES (533528, 5335, '沧源佤族自治县');
INSERT INTO `province_city_district` VALUES (540101, 5401, '市辖区');
INSERT INTO `province_city_district` VALUES (540102, 5401, '城关区');
INSERT INTO `province_city_district` VALUES (540121, 5401, '林周县');
INSERT INTO `province_city_district` VALUES (540122, 5401, '当雄县');
INSERT INTO `province_city_district` VALUES (540123, 5401, '尼木县');
INSERT INTO `province_city_district` VALUES (540124, 5401, '曲水县');
INSERT INTO `province_city_district` VALUES (540125, 5401, '堆龙德庆县');
INSERT INTO `province_city_district` VALUES (540126, 5401, '达孜县');
INSERT INTO `province_city_district` VALUES (540127, 5401, '墨竹工卡县');
INSERT INTO `province_city_district` VALUES (542121, 5421, '昌都县');
INSERT INTO `province_city_district` VALUES (542122, 5421, '江达县');
INSERT INTO `province_city_district` VALUES (542123, 5421, '贡觉县');
INSERT INTO `province_city_district` VALUES (542124, 5421, '类乌齐县');
INSERT INTO `province_city_district` VALUES (542125, 5421, '丁青县');
INSERT INTO `province_city_district` VALUES (542126, 5421, '察雅县');
INSERT INTO `province_city_district` VALUES (542127, 5421, '八宿县');
INSERT INTO `province_city_district` VALUES (542128, 5421, '左贡县');
INSERT INTO `province_city_district` VALUES (542129, 5421, '芒康县');
INSERT INTO `province_city_district` VALUES (542132, 5421, '洛隆县');
INSERT INTO `province_city_district` VALUES (542133, 5421, '边坝县');
INSERT INTO `province_city_district` VALUES (542221, 5422, '乃东县');
INSERT INTO `province_city_district` VALUES (542222, 5422, '扎囊县');
INSERT INTO `province_city_district` VALUES (542223, 5422, '贡嘎县');
INSERT INTO `province_city_district` VALUES (542224, 5422, '桑日县');
INSERT INTO `province_city_district` VALUES (542225, 5422, '琼结县');
INSERT INTO `province_city_district` VALUES (542226, 5422, '曲松县');
INSERT INTO `province_city_district` VALUES (542227, 5422, '措美县');
INSERT INTO `province_city_district` VALUES (542228, 5422, '洛扎县');
INSERT INTO `province_city_district` VALUES (542229, 5422, '加查县');
INSERT INTO `province_city_district` VALUES (542231, 5422, '隆子县');
INSERT INTO `province_city_district` VALUES (542232, 5422, '错那县');
INSERT INTO `province_city_district` VALUES (542233, 5422, '浪卡子县');
INSERT INTO `province_city_district` VALUES (542301, 5423, '日喀则市');
INSERT INTO `province_city_district` VALUES (542322, 5423, '南木林县');
INSERT INTO `province_city_district` VALUES (542323, 5423, '江孜县');
INSERT INTO `province_city_district` VALUES (542324, 5423, '定日县');
INSERT INTO `province_city_district` VALUES (542325, 5423, '萨迦县');
INSERT INTO `province_city_district` VALUES (542326, 5423, '拉孜县');
INSERT INTO `province_city_district` VALUES (542327, 5423, '昂仁县');
INSERT INTO `province_city_district` VALUES (542328, 5423, '谢通门县');
INSERT INTO `province_city_district` VALUES (542329, 5423, '白朗县');
INSERT INTO `province_city_district` VALUES (542330, 5423, '仁布县');
INSERT INTO `province_city_district` VALUES (542331, 5423, '康马县');
INSERT INTO `province_city_district` VALUES (542332, 5423, '定结县');
INSERT INTO `province_city_district` VALUES (542333, 5423, '仲巴县');
INSERT INTO `province_city_district` VALUES (542334, 5423, '亚东县');
INSERT INTO `province_city_district` VALUES (542335, 5423, '吉隆县');
INSERT INTO `province_city_district` VALUES (542336, 5423, '聂拉木县');
INSERT INTO `province_city_district` VALUES (542337, 5423, '萨嘎县');
INSERT INTO `province_city_district` VALUES (542338, 5423, '岗巴县');
INSERT INTO `province_city_district` VALUES (542421, 5424, '那曲县');
INSERT INTO `province_city_district` VALUES (542422, 5424, '嘉黎县');
INSERT INTO `province_city_district` VALUES (542423, 5424, '比如县');
INSERT INTO `province_city_district` VALUES (542424, 5424, '聂荣县');
INSERT INTO `province_city_district` VALUES (542425, 5424, '安多县');
INSERT INTO `province_city_district` VALUES (542426, 5424, '申扎县');
INSERT INTO `province_city_district` VALUES (542427, 5424, '索  县');
INSERT INTO `province_city_district` VALUES (542428, 5424, '班戈县');
INSERT INTO `province_city_district` VALUES (542429, 5424, '巴青县');
INSERT INTO `province_city_district` VALUES (542430, 5424, '尼玛县');
INSERT INTO `province_city_district` VALUES (542521, 5425, '普兰县');
INSERT INTO `province_city_district` VALUES (542522, 5425, '札达县');
INSERT INTO `province_city_district` VALUES (542523, 5425, '噶尔县');
INSERT INTO `province_city_district` VALUES (542524, 5425, '日土县');
INSERT INTO `province_city_district` VALUES (542525, 5425, '革吉县');
INSERT INTO `province_city_district` VALUES (542526, 5425, '改则县');
INSERT INTO `province_city_district` VALUES (542527, 5425, '措勤县');
INSERT INTO `province_city_district` VALUES (542621, 5426, '林芝县');
INSERT INTO `province_city_district` VALUES (542622, 5426, '工布江达县');
INSERT INTO `province_city_district` VALUES (542623, 5426, '米林县');
INSERT INTO `province_city_district` VALUES (542624, 5426, '墨脱县');
INSERT INTO `province_city_district` VALUES (542625, 5426, '波密县');
INSERT INTO `province_city_district` VALUES (542626, 5426, '察隅县');
INSERT INTO `province_city_district` VALUES (542627, 5426, '朗  县');
INSERT INTO `province_city_district` VALUES (610101, 6101, '市辖区');
INSERT INTO `province_city_district` VALUES (610102, 6101, '新城区');
INSERT INTO `province_city_district` VALUES (610103, 6101, '碑林区');
INSERT INTO `province_city_district` VALUES (610104, 6101, '莲湖区');
INSERT INTO `province_city_district` VALUES (610111, 6101, '灞桥区');
INSERT INTO `province_city_district` VALUES (610112, 6101, '未央区');
INSERT INTO `province_city_district` VALUES (610113, 6101, '雁塔区');
INSERT INTO `province_city_district` VALUES (610114, 6101, '阎良区');
INSERT INTO `province_city_district` VALUES (610115, 6101, '临潼区');
INSERT INTO `province_city_district` VALUES (610121, 6101, '长安县');
INSERT INTO `province_city_district` VALUES (610122, 6101, '蓝田县');
INSERT INTO `province_city_district` VALUES (610124, 6101, '周至县');
INSERT INTO `province_city_district` VALUES (610125, 6101, '户  县');
INSERT INTO `province_city_district` VALUES (610126, 6101, '高陵县');
INSERT INTO `province_city_district` VALUES (610201, 6102, '市辖区');
INSERT INTO `province_city_district` VALUES (610202, 6102, '王益区');
INSERT INTO `province_city_district` VALUES (610203, 6102, '印台区');
INSERT INTO `province_city_district` VALUES (610221, 6102, '耀  县');
INSERT INTO `province_city_district` VALUES (610222, 6102, '宜君县');
INSERT INTO `province_city_district` VALUES (610301, 6103, '市辖区');
INSERT INTO `province_city_district` VALUES (610302, 6103, '渭滨区');
INSERT INTO `province_city_district` VALUES (610303, 6103, '金台区');
INSERT INTO `province_city_district` VALUES (610321, 6103, '宝鸡县');
INSERT INTO `province_city_district` VALUES (610322, 6103, '凤翔县');
INSERT INTO `province_city_district` VALUES (610323, 6103, '岐山县');
INSERT INTO `province_city_district` VALUES (610324, 6103, '扶风县');
INSERT INTO `province_city_district` VALUES (610326, 6103, '眉  县');
INSERT INTO `province_city_district` VALUES (610327, 6103, '陇  县');
INSERT INTO `province_city_district` VALUES (610328, 6103, '千阳县');
INSERT INTO `province_city_district` VALUES (610329, 6103, '麟游县');
INSERT INTO `province_city_district` VALUES (610330, 6103, '凤  县');
INSERT INTO `province_city_district` VALUES (610331, 6103, '太白县');
INSERT INTO `province_city_district` VALUES (610401, 6104, '市辖区');
INSERT INTO `province_city_district` VALUES (610402, 6104, '秦都区');
INSERT INTO `province_city_district` VALUES (610403, 6104, '杨陵区');
INSERT INTO `province_city_district` VALUES (610404, 6104, '渭城区');
INSERT INTO `province_city_district` VALUES (610422, 6104, '三原县');
INSERT INTO `province_city_district` VALUES (610423, 6104, '泾阳县');
INSERT INTO `province_city_district` VALUES (610424, 6104, '乾  县');
INSERT INTO `province_city_district` VALUES (610425, 6104, '礼泉县');
INSERT INTO `province_city_district` VALUES (610426, 6104, '永寿县');
INSERT INTO `province_city_district` VALUES (610427, 6104, '彬  县');
INSERT INTO `province_city_district` VALUES (610428, 6104, '长武县');
INSERT INTO `province_city_district` VALUES (610429, 6104, '旬邑县');
INSERT INTO `province_city_district` VALUES (610430, 6104, '淳化县');
INSERT INTO `province_city_district` VALUES (610431, 6104, '武功县');
INSERT INTO `province_city_district` VALUES (610481, 6104, '兴平市');
INSERT INTO `province_city_district` VALUES (610501, 6105, '市辖区');
INSERT INTO `province_city_district` VALUES (610502, 6105, '临渭区');
INSERT INTO `province_city_district` VALUES (610521, 6105, '华  县');
INSERT INTO `province_city_district` VALUES (610522, 6105, '潼关县');
INSERT INTO `province_city_district` VALUES (610523, 6105, '大荔县');
INSERT INTO `province_city_district` VALUES (610524, 6105, '合阳县');
INSERT INTO `province_city_district` VALUES (610525, 6105, '澄城县');
INSERT INTO `province_city_district` VALUES (610526, 6105, '蒲城县');
INSERT INTO `province_city_district` VALUES (610527, 6105, '白水县');
INSERT INTO `province_city_district` VALUES (610528, 6105, '富平县');
INSERT INTO `province_city_district` VALUES (610581, 6105, '韩城市');
INSERT INTO `province_city_district` VALUES (610582, 6105, '华阴市');
INSERT INTO `province_city_district` VALUES (610601, 6106, '市辖区');
INSERT INTO `province_city_district` VALUES (610602, 6106, '宝塔区');
INSERT INTO `province_city_district` VALUES (610621, 6106, '延长县');
INSERT INTO `province_city_district` VALUES (610622, 6106, '延川县');
INSERT INTO `province_city_district` VALUES (610623, 6106, '子长县');
INSERT INTO `province_city_district` VALUES (610624, 6106, '安塞县');
INSERT INTO `province_city_district` VALUES (610625, 6106, '志丹县');
INSERT INTO `province_city_district` VALUES (610626, 6106, '吴旗县');
INSERT INTO `province_city_district` VALUES (610627, 6106, '甘泉县');
INSERT INTO `province_city_district` VALUES (610628, 6106, '富  县');
INSERT INTO `province_city_district` VALUES (610629, 6106, '洛川县');
INSERT INTO `province_city_district` VALUES (610630, 6106, '宜川县');
INSERT INTO `province_city_district` VALUES (610631, 6106, '黄龙县');
INSERT INTO `province_city_district` VALUES (610632, 6106, '黄陵县');
INSERT INTO `province_city_district` VALUES (610701, 6107, '市辖区');
INSERT INTO `province_city_district` VALUES (610702, 6107, '汉台区');
INSERT INTO `province_city_district` VALUES (610721, 6107, '南郑县');
INSERT INTO `province_city_district` VALUES (610722, 6107, '城固县');
INSERT INTO `province_city_district` VALUES (610723, 6107, '洋  县');
INSERT INTO `province_city_district` VALUES (610724, 6107, '西乡县');
INSERT INTO `province_city_district` VALUES (610725, 6107, '勉  县');
INSERT INTO `province_city_district` VALUES (610726, 6107, '宁强县');
INSERT INTO `province_city_district` VALUES (610727, 6107, '略阳县');
INSERT INTO `province_city_district` VALUES (610728, 6107, '镇巴县');
INSERT INTO `province_city_district` VALUES (610729, 6107, '留坝县');
INSERT INTO `province_city_district` VALUES (610730, 6107, '佛坪县');
INSERT INTO `province_city_district` VALUES (610801, 6108, '市辖区');
INSERT INTO `province_city_district` VALUES (610802, 6108, '榆阳区');
INSERT INTO `province_city_district` VALUES (610821, 6108, '神木县');
INSERT INTO `province_city_district` VALUES (610822, 6108, '府谷县');
INSERT INTO `province_city_district` VALUES (610823, 6108, '横山县');
INSERT INTO `province_city_district` VALUES (610824, 6108, '靖边县');
INSERT INTO `province_city_district` VALUES (610825, 6108, '定边县');
INSERT INTO `province_city_district` VALUES (610826, 6108, '绥德县');
INSERT INTO `province_city_district` VALUES (610827, 6108, '米脂县');
INSERT INTO `province_city_district` VALUES (610828, 6108, '佳  县');
INSERT INTO `province_city_district` VALUES (610829, 6108, '吴堡县');
INSERT INTO `province_city_district` VALUES (610830, 6108, '清涧县');
INSERT INTO `province_city_district` VALUES (610831, 6108, '子洲县');
INSERT INTO `province_city_district` VALUES (610901, 6109, '市辖区');
INSERT INTO `province_city_district` VALUES (610902, 6109, '汉滨区');
INSERT INTO `province_city_district` VALUES (610921, 6109, '汉阴县');
INSERT INTO `province_city_district` VALUES (610922, 6109, '石泉县');
INSERT INTO `province_city_district` VALUES (610923, 6109, '宁陕县');
INSERT INTO `province_city_district` VALUES (610924, 6109, '紫阳县');
INSERT INTO `province_city_district` VALUES (610925, 6109, '岚皋县');
INSERT INTO `province_city_district` VALUES (610926, 6109, '平利县');
INSERT INTO `province_city_district` VALUES (610927, 6109, '镇坪县');
INSERT INTO `province_city_district` VALUES (610928, 6109, '旬阳县');
INSERT INTO `province_city_district` VALUES (610929, 6109, '白河县');
INSERT INTO `province_city_district` VALUES (612501, 6125, '商州市');
INSERT INTO `province_city_district` VALUES (612522, 6125, '洛南县');
INSERT INTO `province_city_district` VALUES (612523, 6125, '丹凤县');
INSERT INTO `province_city_district` VALUES (612524, 6125, '商南县');
INSERT INTO `province_city_district` VALUES (612525, 6125, '山阳县');
INSERT INTO `province_city_district` VALUES (612526, 6125, '镇安县');
INSERT INTO `province_city_district` VALUES (612527, 6125, '柞水县');
INSERT INTO `province_city_district` VALUES (620101, 6201, '市辖区');
INSERT INTO `province_city_district` VALUES (620102, 6201, '城关区');
INSERT INTO `province_city_district` VALUES (620103, 6201, '七里河区');
INSERT INTO `province_city_district` VALUES (620104, 6201, '西固区');
INSERT INTO `province_city_district` VALUES (620105, 6201, '安宁区');
INSERT INTO `province_city_district` VALUES (620111, 6201, '红古区');
INSERT INTO `province_city_district` VALUES (620121, 6201, '永登县');
INSERT INTO `province_city_district` VALUES (620122, 6201, '皋兰县');
INSERT INTO `province_city_district` VALUES (620123, 6201, '榆中县');
INSERT INTO `province_city_district` VALUES (620201, 6202, '市辖区');
INSERT INTO `province_city_district` VALUES (620301, 6203, '市辖区');
INSERT INTO `province_city_district` VALUES (620302, 6203, '金川区');
INSERT INTO `province_city_district` VALUES (620321, 6203, '永昌县');
INSERT INTO `province_city_district` VALUES (620401, 6204, '市辖区');
INSERT INTO `province_city_district` VALUES (620402, 6204, '白银区');
INSERT INTO `province_city_district` VALUES (620403, 6204, '平川区');
INSERT INTO `province_city_district` VALUES (620421, 6204, '靖远县');
INSERT INTO `province_city_district` VALUES (620422, 6204, '会宁县');
INSERT INTO `province_city_district` VALUES (620423, 6204, '景泰县');
INSERT INTO `province_city_district` VALUES (620501, 6205, '市辖区');
INSERT INTO `province_city_district` VALUES (620502, 6205, '秦城区');
INSERT INTO `province_city_district` VALUES (620503, 6205, '北道区');
INSERT INTO `province_city_district` VALUES (620521, 6205, '清水县');
INSERT INTO `province_city_district` VALUES (620522, 6205, '秦安县');
INSERT INTO `province_city_district` VALUES (620523, 6205, '甘谷县');
INSERT INTO `province_city_district` VALUES (620524, 6205, '武山县');
INSERT INTO `province_city_district` VALUES (620525, 6205, '张家川回族自治县');
INSERT INTO `province_city_district` VALUES (622101, 6221, '玉门市');
INSERT INTO `province_city_district` VALUES (622102, 6221, '酒泉市');
INSERT INTO `province_city_district` VALUES (622103, 6221, '敦煌市');
INSERT INTO `province_city_district` VALUES (622123, 6221, '金塔县');
INSERT INTO `province_city_district` VALUES (622124, 6221, '肃北蒙古族自治县');
INSERT INTO `province_city_district` VALUES (622125, 6221, '阿克塞哈萨克族自治县');
INSERT INTO `province_city_district` VALUES (622126, 6221, '安西县');
INSERT INTO `province_city_district` VALUES (622201, 6222, '张掖市');
INSERT INTO `province_city_district` VALUES (622222, 6222, '肃南裕固族自治县');
INSERT INTO `province_city_district` VALUES (622223, 6222, '民乐县');
INSERT INTO `province_city_district` VALUES (622224, 6222, '临泽县');
INSERT INTO `province_city_district` VALUES (622225, 6222, '高台县');
INSERT INTO `province_city_district` VALUES (622226, 6222, '山丹县');
INSERT INTO `province_city_district` VALUES (622301, 6223, '武威市');
INSERT INTO `province_city_district` VALUES (622322, 6223, '民勤县');
INSERT INTO `province_city_district` VALUES (622323, 6223, '古浪县');
INSERT INTO `province_city_district` VALUES (622326, 6223, '天祝藏族自治县');
INSERT INTO `province_city_district` VALUES (622421, 6224, '定西县');
INSERT INTO `province_city_district` VALUES (622424, 6224, '通渭县');
INSERT INTO `province_city_district` VALUES (622425, 6224, '陇西县');
INSERT INTO `province_city_district` VALUES (622426, 6224, '渭源县');
INSERT INTO `province_city_district` VALUES (622427, 6224, '临洮县');
INSERT INTO `province_city_district` VALUES (622428, 6224, '漳  县');
INSERT INTO `province_city_district` VALUES (622429, 6224, '岷  县');
INSERT INTO `province_city_district` VALUES (622621, 6226, '武都县');
INSERT INTO `province_city_district` VALUES (622623, 6226, '宕昌县');
INSERT INTO `province_city_district` VALUES (622624, 6226, '成  县');
INSERT INTO `province_city_district` VALUES (622625, 6226, '康  县');
INSERT INTO `province_city_district` VALUES (622626, 6226, '文  县');
INSERT INTO `province_city_district` VALUES (622627, 6226, '西和县');
INSERT INTO `province_city_district` VALUES (622628, 6226, '礼  县');
INSERT INTO `province_city_district` VALUES (622629, 6226, '两当县');
INSERT INTO `province_city_district` VALUES (622630, 6226, '徽  县');
INSERT INTO `province_city_district` VALUES (622701, 6227, '平凉市');
INSERT INTO `province_city_district` VALUES (622722, 6227, '泾川县');
INSERT INTO `province_city_district` VALUES (622723, 6227, '灵台县');
INSERT INTO `province_city_district` VALUES (622724, 6227, '崇信县');
INSERT INTO `province_city_district` VALUES (622725, 6227, '华亭县');
INSERT INTO `province_city_district` VALUES (622726, 6227, '庄浪县');
INSERT INTO `province_city_district` VALUES (622727, 6227, '静宁县');
INSERT INTO `province_city_district` VALUES (622801, 6228, '西峰市');
INSERT INTO `province_city_district` VALUES (622821, 6228, '庆阳县');
INSERT INTO `province_city_district` VALUES (622822, 6228, '环  县');
INSERT INTO `province_city_district` VALUES (622823, 6228, '华池县');
INSERT INTO `province_city_district` VALUES (622824, 6228, '合水县');
INSERT INTO `province_city_district` VALUES (622825, 6228, '正宁县');
INSERT INTO `province_city_district` VALUES (622826, 6228, '宁  县');
INSERT INTO `province_city_district` VALUES (622827, 6228, '镇原县');
INSERT INTO `province_city_district` VALUES (622901, 6229, '临夏市');
INSERT INTO `province_city_district` VALUES (622921, 6229, '临夏县');
INSERT INTO `province_city_district` VALUES (622922, 6229, '康乐县');
INSERT INTO `province_city_district` VALUES (622923, 6229, '永靖县');
INSERT INTO `province_city_district` VALUES (622924, 6229, '广河县');
INSERT INTO `province_city_district` VALUES (622925, 6229, '和政县');
INSERT INTO `province_city_district` VALUES (622926, 6229, '东乡族自治县');
INSERT INTO `province_city_district` VALUES (622927, 6229, '积石山保安族东乡族撒');
INSERT INTO `province_city_district` VALUES (623001, 6230, '合作市');
INSERT INTO `province_city_district` VALUES (623021, 6230, '临潭县');
INSERT INTO `province_city_district` VALUES (623022, 6230, '卓尼县');
INSERT INTO `province_city_district` VALUES (623023, 6230, '舟曲县');
INSERT INTO `province_city_district` VALUES (623024, 6230, '迭部县');
INSERT INTO `province_city_district` VALUES (623025, 6230, '玛曲县');
INSERT INTO `province_city_district` VALUES (623026, 6230, '碌曲县');
INSERT INTO `province_city_district` VALUES (623027, 6230, '夏河县');
INSERT INTO `province_city_district` VALUES (630101, 6301, '市辖区');
INSERT INTO `province_city_district` VALUES (630102, 6301, '城东区');
INSERT INTO `province_city_district` VALUES (630103, 6301, '城中区');
INSERT INTO `province_city_district` VALUES (630104, 6301, '城西区');
INSERT INTO `province_city_district` VALUES (630105, 6301, '城北区');
INSERT INTO `province_city_district` VALUES (630121, 6301, '大通回族土族自治县');
INSERT INTO `province_city_district` VALUES (630122, 6301, '湟中县');
INSERT INTO `province_city_district` VALUES (630123, 6301, '湟源县');
INSERT INTO `province_city_district` VALUES (632121, 6321, '平安县');
INSERT INTO `province_city_district` VALUES (632122, 6321, '民和回族土族自治县');
INSERT INTO `province_city_district` VALUES (632123, 6321, '乐都县');
INSERT INTO `province_city_district` VALUES (632126, 6321, '互助土族自治县');
INSERT INTO `province_city_district` VALUES (632127, 6321, '化隆回族自治县');
INSERT INTO `province_city_district` VALUES (632128, 6321, '循化撒拉族自治县');
INSERT INTO `province_city_district` VALUES (632221, 6322, '门源回族自治县');
INSERT INTO `province_city_district` VALUES (632222, 6322, '祁连县');
INSERT INTO `province_city_district` VALUES (632223, 6322, '海晏县');
INSERT INTO `province_city_district` VALUES (632224, 6322, '刚察县');
INSERT INTO `province_city_district` VALUES (632321, 6323, '同仁县');
INSERT INTO `province_city_district` VALUES (632322, 6323, '尖扎县');
INSERT INTO `province_city_district` VALUES (632323, 6323, '泽库县');
INSERT INTO `province_city_district` VALUES (632324, 6323, '河南蒙古族自治县');
INSERT INTO `province_city_district` VALUES (632521, 6325, '共和县');
INSERT INTO `province_city_district` VALUES (632522, 6325, '同德县');
INSERT INTO `province_city_district` VALUES (632523, 6325, '贵德县');
INSERT INTO `province_city_district` VALUES (632524, 6325, '兴海县');
INSERT INTO `province_city_district` VALUES (632525, 6325, '贵南县');
INSERT INTO `province_city_district` VALUES (632621, 6326, '玛沁县');
INSERT INTO `province_city_district` VALUES (632622, 6326, '班玛县');
INSERT INTO `province_city_district` VALUES (632623, 6326, '甘德县');
INSERT INTO `province_city_district` VALUES (632624, 6326, '达日县');
INSERT INTO `province_city_district` VALUES (632625, 6326, '久治县');
INSERT INTO `province_city_district` VALUES (632626, 6326, '玛多县');
INSERT INTO `province_city_district` VALUES (632721, 6327, '玉树县');
INSERT INTO `province_city_district` VALUES (632722, 6327, '杂多县');
INSERT INTO `province_city_district` VALUES (632723, 6327, '称多县');
INSERT INTO `province_city_district` VALUES (632724, 6327, '治多县');
INSERT INTO `province_city_district` VALUES (632725, 6327, '囊谦县');
INSERT INTO `province_city_district` VALUES (632726, 6327, '曲麻莱县');
INSERT INTO `province_city_district` VALUES (632801, 6328, '格尔木市');
INSERT INTO `province_city_district` VALUES (632802, 6328, '德令哈市');
INSERT INTO `province_city_district` VALUES (632821, 6328, '乌兰县');
INSERT INTO `province_city_district` VALUES (632822, 6328, '都兰县');
INSERT INTO `province_city_district` VALUES (632823, 6328, '天峻县');
INSERT INTO `province_city_district` VALUES (640101, 6401, '市辖区');
INSERT INTO `province_city_district` VALUES (640102, 6401, '城  区');
INSERT INTO `province_city_district` VALUES (640103, 6401, '新城区');
INSERT INTO `province_city_district` VALUES (640111, 6401, '郊  区');
INSERT INTO `province_city_district` VALUES (640121, 6401, '永宁县');
INSERT INTO `province_city_district` VALUES (640122, 6401, '贺兰县');
INSERT INTO `province_city_district` VALUES (640201, 6402, '市辖区');
INSERT INTO `province_city_district` VALUES (640202, 6402, '大武口区');
INSERT INTO `province_city_district` VALUES (640203, 6402, '石嘴山区');
INSERT INTO `province_city_district` VALUES (640204, 6402, '石炭井区');
INSERT INTO `province_city_district` VALUES (640221, 6402, '平罗县');
INSERT INTO `province_city_district` VALUES (640222, 6402, '陶乐县');
INSERT INTO `province_city_district` VALUES (640223, 6402, '惠农县');
INSERT INTO `province_city_district` VALUES (640301, 6403, '市辖区');
INSERT INTO `province_city_district` VALUES (640302, 6403, '利通区');
INSERT INTO `province_city_district` VALUES (640321, 6403, '中卫县');
INSERT INTO `province_city_district` VALUES (640322, 6403, '中宁县');
INSERT INTO `province_city_district` VALUES (640323, 6403, '盐池县');
INSERT INTO `province_city_district` VALUES (640324, 6403, '同心县');
INSERT INTO `province_city_district` VALUES (640381, 6403, '青铜峡市');
INSERT INTO `province_city_district` VALUES (640382, 6403, '灵武市');
INSERT INTO `province_city_district` VALUES (642221, 6422, '固原县');
INSERT INTO `province_city_district` VALUES (642222, 6422, '海原县');
INSERT INTO `province_city_district` VALUES (642223, 6422, '西吉县');
INSERT INTO `province_city_district` VALUES (642224, 6422, '隆德县');
INSERT INTO `province_city_district` VALUES (642225, 6422, '泾源县');
INSERT INTO `province_city_district` VALUES (642226, 6422, '彭阳县');
INSERT INTO `province_city_district` VALUES (650101, 6501, '市辖区');
INSERT INTO `province_city_district` VALUES (650102, 6501, '天山区');
INSERT INTO `province_city_district` VALUES (650103, 6501, '沙依巴克区');
INSERT INTO `province_city_district` VALUES (650104, 6501, '新市区');
INSERT INTO `province_city_district` VALUES (650105, 6501, '水磨沟区');
INSERT INTO `province_city_district` VALUES (650106, 6501, '头屯河区');
INSERT INTO `province_city_district` VALUES (650107, 6501, '南泉区');
INSERT INTO `province_city_district` VALUES (650108, 6501, '东山区');
INSERT INTO `province_city_district` VALUES (650121, 6501, '乌鲁木齐县');
INSERT INTO `province_city_district` VALUES (650201, 6502, '市辖区');
INSERT INTO `province_city_district` VALUES (650202, 6502, '独山子区');
INSERT INTO `province_city_district` VALUES (650203, 6502, '克拉玛依区');
INSERT INTO `province_city_district` VALUES (650204, 6502, '白碱滩区');
INSERT INTO `province_city_district` VALUES (650205, 6502, '乌尔禾区');
INSERT INTO `province_city_district` VALUES (652101, 6521, '吐鲁番市');
INSERT INTO `province_city_district` VALUES (652122, 6521, '鄯善县');
INSERT INTO `province_city_district` VALUES (652123, 6521, '托克逊县');
INSERT INTO `province_city_district` VALUES (652201, 6522, '哈密市');
INSERT INTO `province_city_district` VALUES (652222, 6522, '巴里坤哈萨克自治县');
INSERT INTO `province_city_district` VALUES (652223, 6522, '伊吾县');
INSERT INTO `province_city_district` VALUES (652301, 6523, '昌吉市');
INSERT INTO `province_city_district` VALUES (652302, 6523, '阜康市');
INSERT INTO `province_city_district` VALUES (652303, 6523, '米泉市');
INSERT INTO `province_city_district` VALUES (652323, 6523, '呼图壁县');
INSERT INTO `province_city_district` VALUES (652324, 6523, '玛纳斯县');
INSERT INTO `province_city_district` VALUES (652325, 6523, '奇台县');
INSERT INTO `province_city_district` VALUES (652327, 6523, '吉木萨尔县');
INSERT INTO `province_city_district` VALUES (652328, 6523, '木垒哈萨克自治县');
INSERT INTO `province_city_district` VALUES (652701, 6527, '博乐市');
INSERT INTO `province_city_district` VALUES (652722, 6527, '精河县');
INSERT INTO `province_city_district` VALUES (652723, 6527, '温泉县');
INSERT INTO `province_city_district` VALUES (652801, 6528, '库尔勒市');
INSERT INTO `province_city_district` VALUES (652822, 6528, '轮台县');
INSERT INTO `province_city_district` VALUES (652823, 6528, '尉犁县');
INSERT INTO `province_city_district` VALUES (652824, 6528, '若羌县');
INSERT INTO `province_city_district` VALUES (652825, 6528, '且末县');
INSERT INTO `province_city_district` VALUES (652826, 6528, '焉耆回族自治县');
INSERT INTO `province_city_district` VALUES (652827, 6528, '和静县');
INSERT INTO `province_city_district` VALUES (652828, 6528, '和硕县');
INSERT INTO `province_city_district` VALUES (652829, 6528, '博湖县');
INSERT INTO `province_city_district` VALUES (652901, 6529, '阿克苏市');
INSERT INTO `province_city_district` VALUES (652922, 6529, '温宿县');
INSERT INTO `province_city_district` VALUES (652923, 6529, '库车县');
INSERT INTO `province_city_district` VALUES (652924, 6529, '沙雅县');
INSERT INTO `province_city_district` VALUES (652925, 6529, '新和县');
INSERT INTO `province_city_district` VALUES (652926, 6529, '拜城县');
INSERT INTO `province_city_district` VALUES (652927, 6529, '乌什县');
INSERT INTO `province_city_district` VALUES (652928, 6529, '阿瓦提县');
INSERT INTO `province_city_district` VALUES (652929, 6529, '柯坪县');
INSERT INTO `province_city_district` VALUES (653001, 6530, '阿图什市');
INSERT INTO `province_city_district` VALUES (653022, 6530, '阿克陶县');
INSERT INTO `province_city_district` VALUES (653023, 6530, '阿合奇县');
INSERT INTO `province_city_district` VALUES (653024, 6530, '乌恰县');
INSERT INTO `province_city_district` VALUES (653101, 6531, '喀什市');
INSERT INTO `province_city_district` VALUES (653121, 6531, '疏附县');
INSERT INTO `province_city_district` VALUES (653122, 6531, '疏勒县');
INSERT INTO `province_city_district` VALUES (653123, 6531, '英吉沙县');
INSERT INTO `province_city_district` VALUES (653124, 6531, '泽普县');
INSERT INTO `province_city_district` VALUES (653125, 6531, '莎车县');
INSERT INTO `province_city_district` VALUES (653126, 6531, '叶城县');
INSERT INTO `province_city_district` VALUES (653127, 6531, '麦盖提县');
INSERT INTO `province_city_district` VALUES (653128, 6531, '岳普湖县');
INSERT INTO `province_city_district` VALUES (653129, 6531, '伽师县');
INSERT INTO `province_city_district` VALUES (653130, 6531, '巴楚县');
INSERT INTO `province_city_district` VALUES (653131, 6531, '塔什库尔干塔吉克自治');
INSERT INTO `province_city_district` VALUES (653201, 6532, '和田市');
INSERT INTO `province_city_district` VALUES (653221, 6532, '和田县');
INSERT INTO `province_city_district` VALUES (653222, 6532, '墨玉县');
INSERT INTO `province_city_district` VALUES (653223, 6532, '皮山县');
INSERT INTO `province_city_district` VALUES (653224, 6532, '洛浦县');
INSERT INTO `province_city_district` VALUES (653225, 6532, '策勒县');
INSERT INTO `province_city_district` VALUES (653226, 6532, '于田县');
INSERT INTO `province_city_district` VALUES (653227, 6532, '民丰县');
INSERT INTO `province_city_district` VALUES (654001, 6540, '奎屯市');
INSERT INTO `province_city_district` VALUES (654101, 6541, '伊宁市');
INSERT INTO `province_city_district` VALUES (654121, 6541, '伊宁县');
INSERT INTO `province_city_district` VALUES (654122, 6541, '察布查尔锡伯自治县');
INSERT INTO `province_city_district` VALUES (654123, 6541, '霍城县');
INSERT INTO `province_city_district` VALUES (654124, 6541, '巩留县');
INSERT INTO `province_city_district` VALUES (654125, 6541, '新源县');
INSERT INTO `province_city_district` VALUES (654126, 6541, '昭苏县');
INSERT INTO `province_city_district` VALUES (654127, 6541, '特克斯县');
INSERT INTO `province_city_district` VALUES (654128, 6541, '尼勒克县');
INSERT INTO `province_city_district` VALUES (654201, 6542, '塔城市');
INSERT INTO `province_city_district` VALUES (654202, 6542, '乌苏市');
INSERT INTO `province_city_district` VALUES (654221, 6542, '额敏县');
INSERT INTO `province_city_district` VALUES (654223, 6542, '沙湾县');
INSERT INTO `province_city_district` VALUES (654224, 6542, '托里县');
INSERT INTO `province_city_district` VALUES (654225, 6542, '裕民县');
INSERT INTO `province_city_district` VALUES (654226, 6542, '和布克赛尔蒙古自治县');
INSERT INTO `province_city_district` VALUES (654301, 6543, '阿勒泰市');
INSERT INTO `province_city_district` VALUES (654321, 6543, '布尔津县');
INSERT INTO `province_city_district` VALUES (654322, 6543, '富蕴县');
INSERT INTO `province_city_district` VALUES (654323, 6543, '福海县');
INSERT INTO `province_city_district` VALUES (654324, 6543, '哈巴河县');
INSERT INTO `province_city_district` VALUES (654325, 6543, '青河县');
INSERT INTO `province_city_district` VALUES (654326, 6543, '吉木乃县');
INSERT INTO `province_city_district` VALUES (659001, 6590, '石河子市');
INSERT INTO `province_city_district` VALUES (710101, 7101, '请选择');
INSERT INTO `province_city_district` VALUES (710102, 7101, '市辖区');
INSERT INTO `province_city_district` VALUES (710103, 7101, '台湾省');
INSERT INTO `province_city_district` VALUES (810101, 8101, '请选择');
INSERT INTO `province_city_district` VALUES (810102, 8101, '市辖区');
INSERT INTO `province_city_district` VALUES (810103, 8101, '香港特区');
INSERT INTO `province_city_district` VALUES (910101, 9101, '请选择');
INSERT INTO `province_city_district` VALUES (910102, 9101, '市辖区');
INSERT INTO `province_city_district` VALUES (910103, 9101, '澳门特区');

-- ----------------------------
-- Table structure for table_data_mock
-- ----------------------------
DROP TABLE IF EXISTS `table_data_mock`;
CREATE TABLE `table_data_mock`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名称',
  `order_num` int UNSIGNED NULL DEFAULT 0 COMMENT '顺序号，小的在前',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表数据模拟记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of table_data_mock
-- ----------------------------

-- ----------------------------
-- Table structure for table_file_generation
-- ----------------------------
DROP TABLE IF EXISTS `table_file_generation`;
CREATE TABLE `table_file_generation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `table_id` bigint NULL DEFAULT NULL COMMENT '表ID',
  `generation_id` bigint NULL DEFAULT NULL COMMENT '文件生成ID',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `save_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保存路径',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 345 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '表文件生成记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of table_file_generation
-- ----------------------------
INSERT INTO `table_file_generation` VALUES (102, 23, 93, NULL, '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (103, 23, 94, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (104, 23, 95, 'Dao.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Mapper.java');
INSERT INTO `table_file_generation` VALUES (105, 23, 96, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (106, 23, 97, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (107, 23, 98, NULL, '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/${ClassName}Service.java');
INSERT INTO `table_file_generation` VALUES (108, 23, 99, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (109, 23, 100, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (110, 23, 101, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (111, 26, 105, 'column_infoController.java', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (112, 26, 106, 'column_infoConvert.java', NULL);
INSERT INTO `table_file_generation` VALUES (113, 26, 107, 'column_infoConvert.java', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Mapper.java');
INSERT INTO `table_file_generation` VALUES (114, 26, 108, 'column_infoEntity.java', NULL);
INSERT INTO `table_file_generation` VALUES (115, 26, 109, 'column_infoService.java', NULL);
INSERT INTO `table_file_generation` VALUES (116, 26, 110, 'column_infoServiceImpl.java', 'frontend/src/main/java/${packagePath}/${moduleName}/service/${ClassName}Service.java');
INSERT INTO `table_file_generation` VALUES (117, 26, 111, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (118, 26, 112, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (119, 26, 113, NULL, NULL);
INSERT INTO `table_file_generation` VALUES (120, 27, 114, 'columnInfoController.java', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (121, 27, 115, 'columnInfoConvert.java', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (122, 27, 116, 'columnInfoConvert.java', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Mapper.java');
INSERT INTO `table_file_generation` VALUES (123, 27, 117, 'columnInfoEntity.java', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (124, 27, 118, 'columnInfoService.java', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (125, 27, 119, 'columnInfoServiceImpl.java', 'frontend/src/main/java/${packagePath}/${moduleName}/service/${ClassName}Service.java');
INSERT INTO `table_file_generation` VALUES (126, 27, 120, 'columnInfoApi.ts', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (127, 27, 121, 'columnInfoMapper.xml', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (128, 27, 122, 'columnInfoVO.java', 'frontend/src/main/java/${packagePath}/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (129, 28, 123, 'columnInfoController.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (130, 28, 124, 'columnInfoConvert.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (131, 28, 125, 'columnInfoConvert.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/${ClassName}Mapper.java');
INSERT INTO `table_file_generation` VALUES (132, 28, 126, 'columnInfoEntity.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (133, 28, 127, 'columnInfoService.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (134, 28, 128, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com.lancoo/${moduleName}/service/${ClassName}Service.java');
INSERT INTO `table_file_generation` VALUES (135, 28, 129, 'columnInfoApi.ts', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (136, 28, 130, 'columnInfoMapper.xml', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (137, 28, 131, 'columnInfoVO.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (138, 29, 132, 'columnInfoController.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (139, 29, 133, 'columnInfoConvert.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (140, 29, 134, 'columnInfoMappr.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (141, 29, 135, 'columnInfoEntity.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (142, 29, 136, 'columnInfoService.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (143, 29, 137, 'columnInfoServiceImpl.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (144, 29, 138, 'columnInfoApi.ts', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (145, 29, 139, 'columnInfoMapper.xml', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (146, 29, 140, 'columnInfoVO.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (147, 30, 141, 'columnInfoController.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (148, 30, 142, 'columnInfoConvert.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (149, 30, 143, 'columnInfoMappr.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (150, 30, 144, 'columnInfoEntity.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (151, 30, 145, 'columnInfoService.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (152, 30, 146, 'columnInfoServiceImpl.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (153, 30, 147, 'columnInfoApi.ts', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (154, 30, 148, 'columnInfoMapper.xml', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (155, 30, 149, 'columnInfoVO.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (156, 31, 150, 'connectionConfigController.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ConnectionConfigController.java');
INSERT INTO `table_file_generation` VALUES (157, 31, 151, 'connectionConfigConvert.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ConnectionConfigController.java');
INSERT INTO `table_file_generation` VALUES (158, 31, 152, 'connectionConfigMappr.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ConnectionConfigMapper.java');
INSERT INTO `table_file_generation` VALUES (159, 31, 153, 'connectionConfigEntity.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ConnectionConfigController.java');
INSERT INTO `table_file_generation` VALUES (160, 31, 154, 'connectionConfigService.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ConnectionConfigController.java');
INSERT INTO `table_file_generation` VALUES (161, 31, 155, 'connectionConfigServiceImpl.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/ConnectionConfigService.java');
INSERT INTO `table_file_generation` VALUES (162, 31, 156, 'connectionConfigApi.ts', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ConnectionConfigController.java');
INSERT INTO `table_file_generation` VALUES (163, 31, 157, 'connectionConfigMapper.xml', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ConnectionConfigController.java');
INSERT INTO `table_file_generation` VALUES (164, 31, 158, 'connectionConfigVO.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ConnectionConfigController.java');
INSERT INTO `table_file_generation` VALUES (165, 32, 159, 'templateArgumentController.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/TemplateArgumentController.java');
INSERT INTO `table_file_generation` VALUES (166, 32, 160, 'templateArgumentConvert.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/TemplateArgumentController.java');
INSERT INTO `table_file_generation` VALUES (167, 32, 161, 'templateArgumentMappr.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/TemplateArgumentMapper.java');
INSERT INTO `table_file_generation` VALUES (168, 32, 162, 'templateArgumentEntity.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/TemplateArgumentController.java');
INSERT INTO `table_file_generation` VALUES (169, 32, 163, 'templateArgumentService.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/TemplateArgumentController.java');
INSERT INTO `table_file_generation` VALUES (170, 32, 164, 'templateArgumentServiceImpl.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/TemplateArgumentService.java');
INSERT INTO `table_file_generation` VALUES (171, 32, 165, 'templateArgumentApi.ts', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/TemplateArgumentController.java');
INSERT INTO `table_file_generation` VALUES (172, 32, 166, 'templateArgumentMapper.xml', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/TemplateArgumentController.java');
INSERT INTO `table_file_generation` VALUES (173, 32, 167, 'templateArgumentVO.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/TemplateArgumentController.java');
INSERT INTO `table_file_generation` VALUES (174, 33, 168, 'columnInfoController.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (175, 33, 169, 'columnInfoConvert.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (176, 33, 170, 'columnInfoMappr.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (177, 33, 171, 'columnInfoEntity.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (178, 33, 172, 'columnInfoService.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (179, 33, 173, 'columnInfoServiceImpl.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (180, 33, 174, 'columnInfoApi.ts', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (181, 33, 175, 'columnInfoMapper.xml', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (182, 33, 176, 'columnInfoVO.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (183, 34, 177, 'columnInfoController.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (184, 34, 178, 'columnInfoConvert.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (185, 34, 179, 'columnInfoMappr.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (186, 34, 180, 'columnInfoEntity.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (187, 34, 181, 'columnInfoService.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (188, 34, 182, 'columnInfoServiceImpl.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (189, 34, 183, 'columnInfoApi.ts', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (190, 34, 184, 'columnInfoMapper.xml', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (191, 34, 185, 'columnInfoVO.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (192, 35, 186, 'columnInfoController.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (193, 35, 187, 'columnInfoConvert.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (194, 35, 188, 'columnInfoMappr.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (195, 35, 189, 'columnInfoEntity.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (196, 35, 190, 'columnInfoService.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (197, 35, 191, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com.lancoo/${moduleName}/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (198, 35, 192, 'columnInfoApi.ts', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (199, 35, 193, 'columnInfoMapper.xml', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (200, 35, 194, 'columnInfoVO.java', 'frontend/src/main/java/com.lancoo/${moduleName}/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (201, 36, 195, 'columnInfoController.java', 'frontend/src/main/java/com.lancoo/devpl/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (202, 36, 196, 'columnInfoConvert.java', 'frontend/src/main/java/com.lancoo/devpl/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (203, 36, 197, 'columnInfoMappr.java', 'frontend/src/main/java/com.lancoo/devpl/controller/${ClassName}Mapper.java');
INSERT INTO `table_file_generation` VALUES (204, 36, 198, 'columnInfoEntity.java', 'frontend/src/main/java/com.lancoo/devpl/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (205, 36, 199, 'columnInfoService.java', 'frontend/src/main/java/com.lancoo/devpl/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (206, 36, 200, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com.lancoo/devpl/service/${ClassName}Service.java');
INSERT INTO `table_file_generation` VALUES (207, 36, 201, 'columnInfoApi.ts', 'frontend/src/main/java/com.lancoo/devpl/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (208, 36, 202, 'columnInfoMapper.xml', 'frontend/src/main/java/com.lancoo/devpl/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (209, 36, 203, 'columnInfoVO.java', 'frontend/src/main/java/com.lancoo/devpl/controller/${ClassName}Controller.java');
INSERT INTO `table_file_generation` VALUES (210, 37, 204, 'columnInfoController.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (211, 37, 205, 'columnInfoConvert.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (212, 37, 206, 'columnInfoMappr.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (213, 37, 207, 'columnInfoEntity.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (214, 37, 208, 'columnInfoService.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (215, 37, 209, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com/lancoo/devpl/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (216, 37, 210, 'columnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (217, 37, 211, 'columnInfoMapper.xml', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (218, 37, 212, 'columnInfoVO.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (219, 38, 213, 'columnInfoController.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (220, 38, 214, 'columnInfoConvert.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (221, 38, 215, 'columnInfoMappr.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (222, 38, 216, 'columnInfoEntity.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (223, 38, 217, 'columnInfoService.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (224, 38, 218, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com/lancoo/devpl/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (225, 38, 219, 'columnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (226, 38, 220, 'columnInfoMapper.xml', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (227, 38, 221, 'columnInfoVO.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (228, 39, 222, 'columnInfoController.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (229, 39, 223, 'columnInfoConvert.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (230, 39, 224, 'columnInfoMappr.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (231, 39, 225, 'columnInfoEntity.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (232, 39, 226, 'columnInfoService.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (233, 39, 227, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com/lancoo/devpl/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (234, 39, 228, 'columnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (235, 39, 229, 'columnInfoMapper.xml', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (236, 39, 230, 'columnInfoVO.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (237, 40, 231, 'columnInfoController.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (238, 40, 232, 'columnInfoConvert.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (239, 40, 233, 'columnInfoMappr.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (240, 40, 234, 'columnInfoEntity.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (241, 40, 235, 'columnInfoService.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (242, 40, 236, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com/lancoo/devpl/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (243, 40, 237, 'columnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (244, 40, 238, 'columnInfoMapper.xml', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (245, 40, 239, 'columnInfoVO.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (246, 41, 240, 'columnInfoController.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (247, 41, 241, 'columnInfoConvert.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (248, 41, 242, 'columnInfoMappr.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (249, 41, 243, 'columnInfoEntity.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (250, 41, 244, 'columnInfoService.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (251, 41, 245, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com/lancoo/devpl/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (252, 41, 246, 'columnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (253, 41, 247, 'columnInfoMapper.xml', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (254, 41, 248, 'columnInfoVO.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (255, 42, 249, 'columnInfoController.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (256, 42, 250, 'columnInfoConvert.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (257, 42, 251, 'columnInfoMappr.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (258, 42, 252, 'columnInfoEntity.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (259, 42, 253, 'columnInfoService.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (260, 42, 254, 'columnInfoServiceImpl.java', 'frontend/src/main/java/com/lancoo/devpl/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (261, 42, 255, 'columnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (262, 42, 256, 'columnInfoMapper.xml', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (263, 42, 257, 'columnInfoVO.java', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (264, 43, 258, 'tableInfoController.java', 'backend/src/main/java/com/lancoo/devpl/controller/TableInfoController.java');
INSERT INTO `table_file_generation` VALUES (265, 43, 259, 'tableInfoConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/TableInfoController.java');
INSERT INTO `table_file_generation` VALUES (266, 43, 260, 'tableInfoMappr.java', 'backend/src/main/java/com/lancoo/devpl/controller/TableInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (267, 43, 261, 'tableInfoEntity.java', 'backend/src/main/java/com/lancoo/devpl/controller/TableInfoController.java');
INSERT INTO `table_file_generation` VALUES (268, 43, 262, 'tableInfoService.java', 'backend/src/main/java/com/lancoo/devpl/controller/TableInfoController.java');
INSERT INTO `table_file_generation` VALUES (269, 43, 263, 'tableInfoServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/TableInfoService.java');
INSERT INTO `table_file_generation` VALUES (270, 43, 264, 'tableInfoApi.ts', 'backend/src/main/java/com/lancoo/devpl/controller/TableInfoController.java');
INSERT INTO `table_file_generation` VALUES (271, 43, 265, 'tableInfoMapper.xml', 'backend/src/main/java/com/lancoo/devpl/controller/TableInfoController.java');
INSERT INTO `table_file_generation` VALUES (272, 43, 266, 'tableInfoVO.java', 'backend/src/main/java/com/lancoo/devpl/controller/TableInfoController.java');
INSERT INTO `table_file_generation` VALUES (273, 44, 267, 'columnInfoController.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (274, 44, 268, 'columnInfoConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (275, 44, 269, 'columnInfoMappr.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (276, 44, 270, 'columnInfoEntity.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (277, 44, 271, 'columnInfoService.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (278, 44, 272, 'columnInfoServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (279, 44, 273, 'columnInfoApi.ts', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (280, 44, 274, 'columnInfoMapper.xml', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (281, 44, 275, 'columnInfoVO.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (282, 45, 276, 'ColumnInfoController.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (283, 45, 277, 'ColumnInfoConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (284, 45, 278, 'ColumnInfoMappr.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (285, 45, 279, 'ColumnInfoEntity.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (286, 45, 280, 'ColumnInfoService.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (287, 45, 281, 'ColumnInfoServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (288, 45, 282, 'ColumnInfoApi.ts', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (289, 45, 283, 'ColumnInfoMapper.xml', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (290, 45, 284, 'ColumnInfoVO.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (291, 46, 285, 'ColumnInfoController.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (292, 46, 286, 'ColumnInfoConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (293, 46, 287, 'ColumnInfoMappr.java', 'backend/src/main/java/com/lancoo/devpl/mapper/ColumnInfoMapper.java');
INSERT INTO `table_file_generation` VALUES (294, 46, 288, 'ColumnInfoEntity.java', 'backend/src/main/java/com/lancoo/devpl/entity/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (295, 46, 289, 'ColumnInfoService.java', 'backend/src/main/java/com/lancoo/devpl/service/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (296, 46, 290, 'ColumnInfoServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/impl/ColumnInfoService.java');
INSERT INTO `table_file_generation` VALUES (297, 46, 291, 'ColumnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (298, 46, 292, 'ColumnInfoMapper.xml', 'backend/src/main/resource/mapper/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (299, 46, 293, 'ColumnInfoVO.java', 'backend/src/main/java/com/lancoo/devpl/domain/ColumnInfoController.java');
INSERT INTO `table_file_generation` VALUES (300, 47, 294, 'ColumnInfoController.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (301, 47, 295, 'ColumnInfoConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (302, 47, 296, 'ColumnInfoMappr.java', 'backend/src/main/java/com/lancoo/devpl/mapper/');
INSERT INTO `table_file_generation` VALUES (303, 47, 297, 'ColumnInfoEntity.java', 'backend/src/main/java/com/lancoo/devpl/entity/');
INSERT INTO `table_file_generation` VALUES (304, 47, 298, 'ColumnInfoService.java', 'backend/src/main/java/com/lancoo/devpl/service/');
INSERT INTO `table_file_generation` VALUES (305, 47, 299, 'ColumnInfoServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/impl/');
INSERT INTO `table_file_generation` VALUES (306, 47, 300, 'ColumnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (307, 47, 301, 'ColumnInfoMapper.xml', 'backend/src/main/resource/mapper/');
INSERT INTO `table_file_generation` VALUES (308, 47, 302, 'ColumnInfoVO.java', 'backend/src/main/java/com/lancoo/devpl/domain/');
INSERT INTO `table_file_generation` VALUES (309, 48, 303, 'ColumnInfoController.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (310, 48, 304, 'ColumnInfoConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (311, 48, 305, 'ColumnInfoMappr.java', 'backend/src/main/java/com/lancoo/devpl/mapper/');
INSERT INTO `table_file_generation` VALUES (312, 48, 306, 'ColumnInfoEntity.java', 'backend/src/main/java/com/lancoo/devpl/entity/');
INSERT INTO `table_file_generation` VALUES (313, 48, 307, 'ColumnInfoService.java', 'backend/src/main/java/com/lancoo/devpl/service/');
INSERT INTO `table_file_generation` VALUES (314, 48, 308, 'ColumnInfoServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/impl/');
INSERT INTO `table_file_generation` VALUES (315, 48, 309, 'ColumnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (316, 48, 310, 'ColumnInfoMapper.xml', 'backend/src/main/resource/mapper/');
INSERT INTO `table_file_generation` VALUES (317, 48, 311, 'ColumnInfoVO.java', 'backend/src/main/java/com/lancoo/devpl/domain/');
INSERT INTO `table_file_generation` VALUES (318, 49, 312, 'GenTableController.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (319, 49, 313, 'GenTableConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (320, 49, 314, 'GenTableMappr.java', 'backend/src/main/java/com/lancoo/devpl/mapper/');
INSERT INTO `table_file_generation` VALUES (321, 49, 315, 'GenTableEntity.java', 'backend/src/main/java/com/lancoo/devpl/entity/');
INSERT INTO `table_file_generation` VALUES (322, 49, 316, 'GenTableService.java', 'backend/src/main/java/com/lancoo/devpl/service/');
INSERT INTO `table_file_generation` VALUES (323, 49, 317, 'GenTableServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/impl/');
INSERT INTO `table_file_generation` VALUES (324, 49, 318, 'GenTableApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (325, 49, 319, 'GenTableMapper.xml', 'backend/src/main/resource/mapper/');
INSERT INTO `table_file_generation` VALUES (326, 49, 320, 'GenTableVO.java', 'backend/src/main/java/com/lancoo/devpl/domain/');
INSERT INTO `table_file_generation` VALUES (327, 50, 321, 'ColumnInfoController.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (328, 50, 322, 'ColumnInfoConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (329, 50, 323, 'ColumnInfoMappr.java', 'backend/src/main/java/com/lancoo/devpl/mapper/');
INSERT INTO `table_file_generation` VALUES (330, 50, 324, 'ColumnInfoEntity.java', 'backend/src/main/java/com/lancoo/devpl/entity/');
INSERT INTO `table_file_generation` VALUES (331, 50, 325, 'ColumnInfoService.java', 'backend/src/main/java/com/lancoo/devpl/service/');
INSERT INTO `table_file_generation` VALUES (332, 50, 326, 'ColumnInfoServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/impl/');
INSERT INTO `table_file_generation` VALUES (333, 50, 327, 'ColumnInfoApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (334, 50, 328, 'ColumnInfoMapper.xml', 'backend/src/main/resource/mapper/');
INSERT INTO `table_file_generation` VALUES (335, 50, 329, 'ColumnInfoVO.java', 'backend/src/main/java/com/lancoo/devpl/domain/');
INSERT INTO `table_file_generation` VALUES (336, 51, 330, 'FieldGroupController.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (337, 51, 331, 'FieldGroupConvert.java', 'backend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (338, 51, 332, 'FieldGroupMappr.java', 'backend/src/main/java/com/lancoo/devpl/mapper/');
INSERT INTO `table_file_generation` VALUES (339, 51, 333, 'FieldGroupEntity.java', 'backend/src/main/java/com/lancoo/devpl/entity/');
INSERT INTO `table_file_generation` VALUES (340, 51, 334, 'FieldGroupService.java', 'backend/src/main/java/com/lancoo/devpl/service/');
INSERT INTO `table_file_generation` VALUES (341, 51, 335, 'FieldGroupServiceImpl.java', 'backend/src/main/java/com/lancoo/devpl/service/impl/');
INSERT INTO `table_file_generation` VALUES (342, 51, 336, 'FieldGroupApi.ts', 'frontend/src/main/java/com/lancoo/devpl/controller/');
INSERT INTO `table_file_generation` VALUES (343, 51, 337, 'FieldGroupMapper.xml', 'backend/src/main/resource/mapper/');
INSERT INTO `table_file_generation` VALUES (344, 51, 338, 'FieldGroupVO.java', 'backend/src/main/java/com/lancoo/devpl/domain/');

-- ----------------------------
-- Table structure for table_info
-- ----------------------------
DROP TABLE IF EXISTS `table_info`;
CREATE TABLE `table_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `table_cat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'table catalog (may be null)',
  `table_schem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'table schema (maybe null)',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表名称',
  `table_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '表类型. 常见类型包括\"TABLE\", \"VIEW\", \"SYSTEM TABLE\", \"GLOBAL TEMPORARY\", \"LOCAL TEMPORARY\", \"ALIAS\", \"SYNONYM\".',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该表的描述文本',
  `type_cat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'the types catalog (maybe null)',
  `type_schem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'the types schema (maybe null)',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型名称',
  `self_referencing_col_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'name of the designated \"identifier\" column of a typed table (may be null)',
  `ref_generation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'specifies how values in SELF_REFERENCING_COL_NAME are created. Values are \"SYSTEM\", \"USER\", \"DERIVED\". (may be null)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据库表信息记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of table_info
-- ----------------------------

-- ----------------------------
-- Table structure for target_generation_file
-- ----------------------------
DROP TABLE IF EXISTS `target_generation_file`;
CREATE TABLE `target_generation_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型名称',
  `template_id` bigint NULL DEFAULT NULL COMMENT '模板ID',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `save_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保存路径',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `builtin` tinyint(1) NULL DEFAULT 0 COMMENT '是否内置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '目标生成文件类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of target_generation_file
-- ----------------------------
INSERT INTO `target_generation_file` VALUES (1, 'Controller.java', 304, '#toCamelCase(${tableName})Controller.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/', 'Controller文件', 1);
INSERT INTO `target_generation_file` VALUES (2, 'Convert.java', 264, '#toCamelCase(${tableName})Convert.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/controller/', '', 1);
INSERT INTO `target_generation_file` VALUES (3, 'Dao.java', 265, '#toCamelCase(${tableName})Mappr.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/mapper/', '', 1);
INSERT INTO `target_generation_file` VALUES (4, 'Entity.java', 266, '#toCamelCase(${tableName})Entity.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/entity/', '', 1);
INSERT INTO `target_generation_file` VALUES (5, 'Service.java', 268, '#toCamelCase(${tableName})Service.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/', '', 1);
INSERT INTO `target_generation_file` VALUES (6, 'ServiceImpl.java', 269, '#toCamelCase(${tableName})ServiceImpl.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/service/impl/', '', 1);
INSERT INTO `target_generation_file` VALUES (8, 'api.ts', 273, '#toCamelCase(${tableName})Api.ts', '${frontendPath}/src/main/java/${packagePath}/${moduleName}/controller/', '', 1);
INSERT INTO `target_generation_file` VALUES (10, 'Mapper.xml', 275, '#toCamelCase(${tableName})Mapper.xml', '${backendPath}/src/main/resource/mapper/', '', 1);
INSERT INTO `target_generation_file` VALUES (11, 'VO.java', 270, '#toCamelCase(${tableName})VO.java', '${backendPath}/src/main/java/${packagePath}/${moduleName}/domain/', '', 1);

-- ----------------------------
-- Table structure for template_argument
-- ----------------------------
DROP TABLE IF EXISTS `template_argument`;
CREATE TABLE `template_argument`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` bigint NULL DEFAULT NULL COMMENT '模板ID',
  `generation_id` bigint NULL DEFAULT NULL COMMENT '模板文件生成ID',
  `var_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数key, 一般为出现在模板中的变量名,单个模板内唯一',
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数值',
  `data_type_id` bigint NULL DEFAULT NULL COMMENT '数据类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 474 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板参数表，模板实际的参数值' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of template_argument
-- ----------------------------
INSERT INTO `template_argument` VALUES (1, 263, 177, 'date', '2023-11-26', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (2, 263, 177, 'ModuleName', 'Module', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (3, 263, 177, 'primaryList', '[io.devpl.generator.entity.GenTableField@73957fd]', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (4, 263, 177, 'moduleName', 'module', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (5, 263, 177, 'importList', '[11111]', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (6, 263, 177, 'className', 'columnInfo', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (7, 263, 177, 'frontendPath', 'fontend', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (8, 263, 177, 'queryList', '[]', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (9, 263, 177, 'tableName', 'column_info', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (10, 263, 177, 'FunctionName', 'Info', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (11, 263, 177, 'datetime', '2023-11-26 23:49:55', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (12, 263, 177, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:49:56', '2023-11-26 23:49:56');
INSERT INTO `template_argument` VALUES (13, 263, 177, 'date', '2023-11-26', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (14, 263, 177, 'ModuleName', 'Module', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (15, 263, 177, 'primaryList', '[io.devpl.generator.entity.GenTableField@173b13c5]', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (16, 263, 177, 'moduleName', 'module', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (17, 263, 177, 'importList', '[11111]', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (18, 263, 177, 'className', 'columnInfo', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (19, 263, 177, 'frontendPath', 'fontend', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (20, 263, 177, 'queryList', '[]', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (21, 263, 177, 'tableName', 'column_info', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (22, 263, 177, 'FunctionName', 'Info', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (23, 263, 177, 'datetime', '2023-11-26 23:50:17', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (24, 263, 177, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:50:18', '2023-11-26 23:50:18');
INSERT INTO `template_argument` VALUES (25, 263, 177, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (26, 263, 177, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (27, 263, 177, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (28, 263, 177, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (29, 263, 177, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (30, 263, 177, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (31, 263, 177, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (32, 263, 177, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (33, 263, 177, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (34, 263, 177, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (35, 263, 177, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (36, 263, 177, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (37, 263, 177, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (38, 263, 177, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (39, 263, 177, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (40, 263, 177, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (41, 263, 177, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (42, 263, 177, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (43, 263, 177, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (44, 263, 177, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (45, 263, 177, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (46, 263, 177, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (47, 263, 177, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (48, 263, 177, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (49, 263, 177, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (50, 264, 178, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (51, 264, 178, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (52, 264, 178, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (53, 264, 178, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (54, 264, 178, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (55, 264, 178, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (56, 264, 178, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (57, 264, 178, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (58, 264, 178, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (59, 264, 178, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (60, 264, 178, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (61, 264, 178, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (62, 264, 178, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (63, 264, 178, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (64, 264, 178, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (65, 264, 178, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (66, 264, 178, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (67, 264, 178, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (68, 264, 178, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (69, 264, 178, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (70, 264, 178, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (71, 264, 178, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (72, 264, 178, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (73, 264, 178, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (74, 264, 178, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (75, 265, 179, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (76, 265, 179, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (77, 265, 179, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (78, 265, 179, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (79, 265, 179, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (80, 265, 179, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (81, 265, 179, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (82, 265, 179, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (83, 265, 179, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (84, 265, 179, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (85, 265, 179, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (86, 265, 179, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (87, 265, 179, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (88, 265, 179, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (89, 265, 179, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (90, 265, 179, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (91, 265, 179, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (92, 265, 179, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (93, 265, 179, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (94, 265, 179, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (95, 265, 179, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (96, 265, 179, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (97, 265, 179, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (98, 265, 179, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (99, 265, 179, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (100, 266, 180, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (101, 266, 180, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (102, 266, 180, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (103, 266, 180, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (104, 266, 180, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (105, 266, 180, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (106, 266, 180, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (107, 266, 180, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (108, 266, 180, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (109, 266, 180, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (110, 266, 180, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (111, 266, 180, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (112, 266, 180, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (113, 266, 180, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (114, 266, 180, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (115, 266, 180, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (116, 266, 180, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (117, 266, 180, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (118, 266, 180, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (119, 266, 180, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (120, 266, 180, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (121, 266, 180, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (122, 266, 180, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (123, 266, 180, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (124, 266, 180, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (125, 268, 181, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (126, 268, 181, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (127, 268, 181, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (128, 268, 181, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (129, 268, 181, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (130, 268, 181, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (131, 268, 181, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (132, 268, 181, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (133, 268, 181, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (134, 268, 181, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (135, 268, 181, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (136, 268, 181, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (137, 268, 181, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (138, 268, 181, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (139, 268, 181, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (140, 268, 181, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (141, 268, 181, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (142, 268, 181, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (143, 268, 181, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (144, 268, 181, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (145, 268, 181, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (146, 268, 181, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (147, 268, 181, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (148, 268, 181, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (149, 268, 181, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (150, 269, 182, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (151, 269, 182, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (152, 269, 182, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (153, 269, 182, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (154, 269, 182, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (155, 269, 182, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (156, 269, 182, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (157, 269, 182, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (158, 269, 182, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (159, 269, 182, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (160, 269, 182, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (161, 269, 182, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (162, 269, 182, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (163, 269, 182, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (164, 269, 182, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (165, 269, 182, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (166, 269, 182, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (167, 269, 182, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (168, 269, 182, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (169, 269, 182, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (170, 269, 182, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (171, 269, 182, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (172, 269, 182, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (173, 269, 182, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (174, 269, 182, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (175, 273, 183, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (176, 273, 183, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (177, 273, 183, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (178, 273, 183, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (179, 273, 183, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (180, 273, 183, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (181, 273, 183, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (182, 273, 183, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (183, 273, 183, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (184, 273, 183, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (185, 273, 183, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (186, 273, 183, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (187, 273, 183, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (188, 273, 183, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (189, 273, 183, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (190, 273, 183, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (191, 273, 183, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (192, 273, 183, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (193, 273, 183, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (194, 273, 183, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (195, 273, 183, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (196, 273, 183, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (197, 273, 183, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (198, 273, 183, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (199, 273, 183, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (200, 275, 184, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (201, 275, 184, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (202, 275, 184, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (203, 275, 184, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (204, 275, 184, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (205, 275, 184, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (206, 275, 184, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (207, 275, 184, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (208, 275, 184, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (209, 275, 184, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (210, 275, 184, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (211, 275, 184, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (212, 275, 184, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (213, 275, 184, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (214, 275, 184, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (215, 275, 184, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (216, 275, 184, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (217, 275, 184, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (218, 275, 184, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (219, 275, 184, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (220, 275, 184, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (221, 275, 184, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (222, 275, 184, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (223, 275, 184, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (224, 275, 184, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (225, 270, 185, 'date', '2023-11-26', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (226, 270, 185, 'ModuleName', 'Module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (227, 270, 185, 'primaryList', '[io.devpl.generator.entity.GenTableField@2b920aeb]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (228, 270, 185, 'moduleName', 'module', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (229, 270, 185, 'importList', '[11111]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (230, 270, 185, 'className', 'columnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (231, 270, 185, 'frontendPath', 'fontend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (232, 270, 185, 'queryList', '[]', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (233, 270, 185, 'tableName', 'column_info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (234, 270, 185, 'FunctionName', 'Info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (235, 270, 185, 'datetime', '2023-11-26 23:51:43', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (236, 270, 185, 'ClassName', 'ColumnInfo', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (237, 270, 185, 'fieldList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (238, 270, 185, 'email', 'email', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (239, 270, 185, 'packagePath', 'io\\devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (240, 270, 185, 'package', 'io.devpl', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (241, 270, 185, 'functionName', 'info', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (242, 270, 185, 'author', 'author', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (243, 270, 185, 'dbType', 'MYSQL', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (244, 270, 185, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (245, 270, 185, 'version', 'version', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (246, 270, 185, 'formLayout', '1', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (247, 270, 185, 'gridList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (248, 270, 185, 'formList', '[io.devpl.generator.entity.GenTableField@2b920aeb, io.devpl.generator.entity.GenTableField@58977e68, io.devpl.generator.entity.GenTableField@78d41839, io.devpl.generator.entity.GenTableField@6d237d2, io.devpl.generator.entity.GenTableField@1ad613ea, io.devpl.generator.entity.GenTableField@79514ae6, io.devpl.generator.entity.GenTableField@211c9025, io.devpl.generator.entity.GenTableField@36674f73, io.devpl.generator.entity.GenTableField@1c845316, io.devpl.generator.entity.GenTableField@6ff12c1a,', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (249, 270, 185, 'backendPath', 'backend', NULL, '2023-11-26 23:51:44', '2023-11-26 23:51:44');
INSERT INTO `template_argument` VALUES (250, 263, 249, 'date', '2023-11-27', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (251, 263, 249, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (252, 263, 249, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (253, 263, 249, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (254, 263, 249, 'importList', '[11111]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (255, 263, 249, 'className', 'columnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (256, 263, 249, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (257, 263, 249, 'queryList', '[]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (258, 263, 249, 'tableName', 'column_info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (259, 263, 249, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (260, 263, 249, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (261, 263, 249, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (262, 263, 249, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (263, 263, 249, 'email', 'email', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (264, 263, 249, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (265, 263, 249, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (266, 263, 249, 'functionName', 'info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (267, 263, 249, 'author', 'author', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (268, 263, 249, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (269, 263, 249, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (270, 263, 249, 'version', 'sdwwe', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (271, 263, 249, 'formLayout', '1', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (272, 263, 249, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (273, 263, 249, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (274, 263, 249, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (275, 264, 250, 'date', '2023-11-27', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (276, 264, 250, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (277, 264, 250, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (278, 264, 250, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (279, 264, 250, 'importList', '[11111]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (280, 264, 250, 'className', 'columnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (281, 264, 250, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (282, 264, 250, 'queryList', '[]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (283, 264, 250, 'tableName', 'column_info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (284, 264, 250, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (285, 264, 250, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (286, 264, 250, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (287, 264, 250, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (288, 264, 250, 'email', 'email', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (289, 264, 250, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (290, 264, 250, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (291, 264, 250, 'functionName', 'info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (292, 264, 250, 'author', 'author', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (293, 264, 250, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (294, 264, 250, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (295, 264, 250, 'version', 'sdwwe', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (296, 264, 250, 'formLayout', '1', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (297, 264, 250, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (298, 264, 250, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (299, 264, 250, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (300, 265, 251, 'date', '2023-11-27', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (301, 265, 251, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (302, 265, 251, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (303, 265, 251, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (304, 265, 251, 'importList', '[11111]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (305, 265, 251, 'className', 'columnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (306, 265, 251, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (307, 265, 251, 'queryList', '[]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (308, 265, 251, 'tableName', 'column_info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (309, 265, 251, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (310, 265, 251, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (311, 265, 251, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (312, 265, 251, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (313, 265, 251, 'email', 'email', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (314, 265, 251, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (315, 265, 251, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (316, 265, 251, 'functionName', 'info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (317, 265, 251, 'author', 'author', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (318, 265, 251, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (319, 265, 251, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (320, 265, 251, 'version', 'sdwwe', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (321, 265, 251, 'formLayout', '1', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (322, 265, 251, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (323, 265, 251, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (324, 265, 251, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (325, 266, 252, 'date', '2023-11-27', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (326, 266, 252, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (327, 266, 252, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (328, 266, 252, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (329, 266, 252, 'importList', '[11111]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (330, 266, 252, 'className', 'columnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (331, 266, 252, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (332, 266, 252, 'queryList', '[]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (333, 266, 252, 'tableName', 'column_info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (334, 266, 252, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (335, 266, 252, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (336, 266, 252, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (337, 266, 252, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (338, 266, 252, 'email', 'email', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (339, 266, 252, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (340, 266, 252, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (341, 266, 252, 'functionName', 'info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (342, 266, 252, 'author', 'author', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (343, 266, 252, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (344, 266, 252, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (345, 266, 252, 'version', 'sdwwe', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (346, 266, 252, 'formLayout', '1', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (347, 266, 252, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (348, 266, 252, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (349, 266, 252, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (350, 268, 253, 'date', '2023-11-27', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (351, 268, 253, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (352, 268, 253, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (353, 268, 253, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (354, 268, 253, 'importList', '[11111]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (355, 268, 253, 'className', 'columnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (356, 268, 253, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (357, 268, 253, 'queryList', '[]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (358, 268, 253, 'tableName', 'column_info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (359, 268, 253, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (360, 268, 253, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (361, 268, 253, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (362, 268, 253, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (363, 268, 253, 'email', 'email', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (364, 268, 253, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (365, 268, 253, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (366, 268, 253, 'functionName', 'info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (367, 268, 253, 'author', 'author', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (368, 268, 253, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (369, 268, 253, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (370, 268, 253, 'version', 'sdwwe', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (371, 268, 253, 'formLayout', '1', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (372, 268, 253, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (373, 268, 253, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (374, 268, 253, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (375, 269, 254, 'date', '2023-11-27', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (376, 269, 254, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (377, 269, 254, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (378, 269, 254, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (379, 269, 254, 'importList', '[11111]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (380, 269, 254, 'className', 'columnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (381, 269, 254, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (382, 269, 254, 'queryList', '[]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (383, 269, 254, 'tableName', 'column_info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (384, 269, 254, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (385, 269, 254, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (386, 269, 254, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (387, 269, 254, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (388, 269, 254, 'email', 'email', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (389, 269, 254, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (390, 269, 254, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (391, 269, 254, 'functionName', 'info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (392, 269, 254, 'author', 'author', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (393, 269, 254, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (394, 269, 254, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (395, 269, 254, 'version', 'sdwwe', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (396, 269, 254, 'formLayout', '1', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (397, 269, 254, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (398, 269, 254, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (399, 269, 254, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (400, 273, 255, 'date', '2023-11-27', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (401, 273, 255, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (402, 273, 255, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (403, 273, 255, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (404, 273, 255, 'importList', '[11111]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (405, 273, 255, 'className', 'columnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (406, 273, 255, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (407, 273, 255, 'queryList', '[]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (408, 273, 255, 'tableName', 'column_info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (409, 273, 255, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (410, 273, 255, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (411, 273, 255, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (412, 273, 255, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (413, 273, 255, 'email', 'email', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (414, 273, 255, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (415, 273, 255, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (416, 273, 255, 'functionName', 'info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (417, 273, 255, 'author', 'author', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (418, 273, 255, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (419, 273, 255, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (420, 273, 255, 'version', 'sdwwe', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (421, 273, 255, 'formLayout', '1', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (422, 273, 255, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (423, 273, 255, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (424, 273, 255, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (425, 275, 256, 'date', '2023-11-27', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (426, 275, 256, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (427, 275, 256, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (428, 275, 256, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (429, 275, 256, 'importList', '[11111]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (430, 275, 256, 'className', 'columnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (431, 275, 256, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (432, 275, 256, 'queryList', '[]', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (433, 275, 256, 'tableName', 'column_info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (434, 275, 256, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (435, 275, 256, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (436, 275, 256, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (437, 275, 256, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (438, 275, 256, 'email', 'email', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (439, 275, 256, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (440, 275, 256, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (441, 275, 256, 'functionName', 'info', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (442, 275, 256, 'author', 'author', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (443, 275, 256, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (444, 275, 256, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (445, 275, 256, 'version', 'sdwwe', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (446, 275, 256, 'formLayout', '1', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (447, 275, 256, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (448, 275, 256, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (449, 275, 256, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:53', '2023-11-27 16:27:53');
INSERT INTO `template_argument` VALUES (450, 270, 257, 'date', '2023-11-27', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (451, 270, 257, 'ModuleName', 'Devpl', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (452, 270, 257, 'primaryList', '[io.devpl.generator.entity.GenTableField@3426311e]', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (453, 270, 257, 'moduleName', 'devpl', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (454, 270, 257, 'importList', '[11111]', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (455, 270, 257, 'className', 'columnInfo', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (456, 270, 257, 'frontendPath', 'backend', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (457, 270, 257, 'queryList', '[]', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (458, 270, 257, 'tableName', 'column_info', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (459, 270, 257, 'FunctionName', 'Info', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (460, 270, 257, 'datetime', '2023-11-27 16:27:52', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (461, 270, 257, 'ClassName', 'ColumnInfo', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (462, 270, 257, 'fieldList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (463, 270, 257, 'email', 'email', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (464, 270, 257, 'packagePath', 'com\\lancoo', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (465, 270, 257, 'package', 'com.lancoo', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (466, 270, 257, 'functionName', 'info', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (467, 270, 257, 'author', 'author', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (468, 270, 257, 'dbType', 'MYSQL', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (469, 270, 257, 'tableComment', '数据库表列信息记录表（对应JDBC的ColumnMetadata）', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (470, 270, 257, 'version', 'sdwwe', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (471, 270, 257, 'formLayout', '1', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (472, 270, 257, 'gridList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (473, 270, 257, 'formList', '[io.devpl.generator.entity.GenTableField@3426311e, io.devpl.generator.entity.GenTableField@57790e1f, io.devpl.generator.entity.GenTableField@31fd6d24, io.devpl.generator.entity.GenTableField@18d01823, io.devpl.generator.entity.GenTableField@9001225, io.devpl.generator.entity.GenTableField@1696fcec, io.devpl.generator.entity.GenTableField@4cd82757, io.devpl.generator.entity.GenTableField@54512fef, io.devpl.generator.entity.GenTableField@5d498e7a, io.devpl.generator.entity.GenTableField@7c9978a3,', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');
INSERT INTO `template_argument` VALUES (474, 270, 257, 'backendPath', 'frontend', NULL, '2023-11-27 16:27:54', '2023-11-27 16:27:54');

-- ----------------------------
-- Table structure for template_file_generation
-- ----------------------------
DROP TABLE IF EXISTS `template_file_generation`;
CREATE TABLE `template_file_generation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名称',
  `template_id` bigint NULL DEFAULT NULL COMMENT '模板ID',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板名称',
  `config_table_id` bigint NULL DEFAULT NULL COMMENT '配置表主键ID',
  `config_table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置表名称',
  `fill_strategy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '数据填充策略',
  `save_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '保存路径',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `builtin` tinyint(1) NULL DEFAULT 0 COMMENT '是否内置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 339 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '基于模板的文件生成记录表(每行对应一个生成的文件)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of template_file_generation
-- ----------------------------
INSERT INTO `template_file_generation` VALUES (93, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (94, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (95, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (96, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (97, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (98, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (99, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (100, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (101, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (105, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (106, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (107, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (108, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (109, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (110, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (111, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (112, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (113, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (114, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (115, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (116, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (117, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (118, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (119, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (120, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (121, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (122, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (123, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (124, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (125, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (126, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (127, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (128, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (129, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (130, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (131, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (132, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (133, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (134, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (135, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (136, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (137, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (138, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (139, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (140, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (141, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (142, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (143, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (144, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (145, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (146, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (147, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (148, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (149, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (150, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (151, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (152, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (153, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (154, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (155, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (156, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (157, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (158, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (159, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (160, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (161, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (162, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (163, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (164, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (165, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (166, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (167, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (168, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (169, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (170, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (171, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (172, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (173, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (174, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (175, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (176, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (177, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (178, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (179, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (180, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (181, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (182, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (183, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (184, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (185, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (186, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (187, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (188, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (189, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (190, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (191, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (192, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (193, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (194, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (195, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (196, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (197, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (198, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (199, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (200, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (201, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (202, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (203, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (204, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (205, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (206, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (207, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (208, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (209, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (210, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (211, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (212, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (213, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (214, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (215, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (216, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (217, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (218, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (219, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (220, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (221, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (222, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (223, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (224, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (225, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (226, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (227, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (228, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (229, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (230, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (231, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (232, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (233, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (234, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (235, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (236, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (237, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (238, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (239, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (240, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (241, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (242, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (243, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (244, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (245, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (246, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (247, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (248, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (249, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (250, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (251, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (252, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (253, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (254, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (255, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (256, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (257, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (258, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (259, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (260, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (261, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (262, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (263, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (264, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (265, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (266, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (267, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (268, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (269, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (270, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (271, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (272, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (273, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (274, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (275, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (276, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (277, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (278, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (279, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (280, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (281, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (282, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (283, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (284, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (285, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (286, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (287, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (288, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (289, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (290, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (291, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (292, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (293, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (294, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (295, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (296, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (297, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (298, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (299, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (300, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (301, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (302, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (303, NULL, 263, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (304, NULL, 264, 'Convert.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (305, NULL, 265, 'Dao.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (306, NULL, 266, 'Entity.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (307, NULL, 268, 'Service.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (308, NULL, 269, 'ServiceImpl.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (309, NULL, 273, 'api.ts.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (310, NULL, 275, 'Dao.xml.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (311, NULL, 270, 'VO.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (312, NULL, 304, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (313, NULL, 264, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (314, NULL, 265, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (315, NULL, 266, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (316, NULL, 268, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (317, NULL, 269, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (318, NULL, 273, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (319, NULL, 275, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (320, NULL, 270, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (321, NULL, 304, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (322, NULL, 264, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (323, NULL, 265, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (324, NULL, 266, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (325, NULL, 268, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (326, NULL, 269, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (327, NULL, 273, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (328, NULL, 275, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (329, NULL, 270, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (330, NULL, 304, 'Controller.java.ftl', NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (331, NULL, 264, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (332, NULL, 265, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (333, NULL, 266, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (334, NULL, 268, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (335, NULL, 269, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (336, NULL, 273, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (337, NULL, 275, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);
INSERT INTO `template_file_generation` VALUES (338, NULL, 270, NULL, NULL, NULL, 'db_table', NULL, NULL, 0);

-- ----------------------------
-- Table structure for template_info
-- ----------------------------
DROP TABLE IF EXISTS `template_info`;
CREATE TABLE `template_info`  (
  `template_id` bigint NOT NULL AUTO_INCREMENT COMMENT '模板ID主键',
  `template_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板名称',
  `type` tinyint NULL DEFAULT NULL COMMENT '模板类型',
  `provider` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '技术提供方',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '字符串模板内容',
  `path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模板文件路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除状态',
  `is_internal` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统内置模板，不可删除修改',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`template_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 347 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of template_info
-- ----------------------------
INSERT INTO `template_info` VALUES (304, 'Controller.java.ftl', 1, 'FreeMarker', 'package ${package}.${moduleName}.controller;\r\n\r\nimport io.swagger.v3.oas.annotations.Operation;\r\nimport io.swagger.v3.oas.annotations.tags.Tag;\r\nimport lombok.AllArgsConstructor;\r\nimport ${package}.framework.common.utils.PageResult;\r\nimport ${package}.framework.common.utils.Result;\r\nimport ${package}.${moduleName}.convert.${ClassName}Convert;\r\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\r\nimport ${package}.${moduleName}.service.${ClassName}Service;\r\nimport ${package}.${moduleName}.query.${ClassName}Query;\r\nimport ${package}.${moduleName}.vo.${ClassName}VO;\r\nimport org.springdoc.core.annotations.ParameterObject;\r\nimport org.springframework.security.access.prepost.PreAuthorize;\r\nimport org.springframework.web.bind.annotation.*;\r\n\r\nimport jakarta.validation.Valid;\r\nimport java.util.List;\r\n\r\n/**\r\n* ${tableComment}\r\n*\r\n* @author ${author} ${email}\r\n* @since ${version} ${date}\r\n*/\r\n@RestController\r\n@RequestMapping(\"${moduleName}/${functionName}\")\r\n@Tag(name=\"${tableComment}\")\r\n@AllArgsConstructor\r\npublic class ${ClassName}Controller {\r\n    private final ${ClassName}Service ${className}Service;\r\n\r\n    @GetMapping(\"page\")\r\n    @Operation(summary = \"分页\")\r\n    @PreAuthorize(\"hasAuthority(\'${moduleName}:${functionName}:page\')\")\r\n    public Result<PageResult<${ClassName}VO>> page(@ParameterObject @Valid ${ClassName}Query query) {\r\n        PageResult <${ClassName}VO> page = ${className}Service.page(query);\r\n        return Result.ok(page);\r\n    }\r\n\r\n    @GetMapping(\"{id}\")\r\n    @Operation(summary = \"信息\")\r\n    @PreAuthorize(\"hasAuthority(\'${moduleName}:${functionName}:info\')\")\r\n    public Result<${ClassName}VO> get(@PathVariable(\"id\") Long id){\r\n        ${ClassName}Entity entity = ${className}Service.getById(id);\r\n        return Result.ok(${ClassName}Convert.INSTANCE.convert(entity));\r\n    }\r\n\r\n    @PostMapping\r\n    @Operation(summary = \"保存\")\r\n    @PreAuthorize(\"hasAuthority(\'${moduleName}:${functionName}:save\')\")\r\n    public Result<String> save(@RequestBody ${ClassName}VO vo){\r\n        ${className}Service.save(vo);\r\n        return Result.ok();\r\n    }\r\n\r\n    @PutMapping\r\n    @Operation(summary = \"修改\")\r\n    @PreAuthorize(\"hasAuthority(\'${moduleName}:${functionName}:update\')\")\r\n    public Result<String> update(@RequestBody @Valid ${ClassName}VO vo){\r\n        ${className}Service.update(vo);\r\n        return Result.ok();\r\n    }\r\n\r\n    @DeleteMapping\r\n    @Operation(summary = \"删除\")\r\n    @PreAuthorize(\"hasAuthority(\'${moduleName}:${functionName}:delete\')\")\r\n    public Result<String> delete(@RequestBody List<Long> idList){\r\n        ${className}Service.delete(idList);\r\n        return Result.ok();\r\n    }\r\n}\r\n', '/java/Controller.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (305, 'Convert.java.ftl', 1, 'FreeMarker', 'package ${package}.${moduleName}.convert;\r\n\r\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\r\nimport ${package}.${moduleName}.vo.${ClassName}VO;\r\nimport org.mapstruct.Mapper;\r\nimport org.mapstruct.factory.Mappers;\r\n\r\nimport java.util.List;\r\n\r\n/**\r\n * ${tableComment}\r\n *\r\n * @author ${author} ${email}\r\n * @since ${version} ${date}\r\n **/\r\n@Mapper\r\npublic interface ${ClassName}Convert {\r\n    ${ClassName}Convert INSTANCE = Mappers.getMapper(${ClassName}Convert.class);\r\n\r\n    ${ClassName}Entity convert(${ClassName}VO vo);\r\n\r\n    ${ClassName}VO convert(${ClassName}Entity entity);\r\n\r\n    List<${ClassName}VO> convertList(List<${ClassName}Entity> list);\r\n}\r\n', '/java/Convert.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (306, 'Dao.java.ftl', 1, 'FreeMarker', 'package ${package}.${moduleName}.dao;\r\n\r\nimport ${package}.framework.mybatis.dao.BaseDao;\r\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\r\nimport org.apache.ibatis.annotations.Mapper;\r\n\r\n/**\r\n * ${tableComment}\r\n *\r\n * @author ${author} ${email}\r\n * @since ${version} ${date}\r\n **/\r\n@Mapper\r\npublic interface ${ClassName}Dao extends BaseDao<${ClassName}Entity> {\r\n\r\n}\r\n', '/java/Dao.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (307, 'Entity.java.ftl', 1, 'FreeMarker', 'package ${package}.${moduleName}.entity;\r\n\r\nimport lombok.Data;\r\nimport lombok.EqualsAndHashCode;\r\nimport com.baomidou.mybatisplus.annotation.IdType;\r\nimport com.baomidou.mybatisplus.annotation.*;\r\n<#list importList as i>\r\nimport ${i!};\r\n</#list>\r\n<#if baseClass??>\r\nimport ${baseClass.packageName}.${baseClass.code};\r\n</#if>\r\n\r\n/**\r\n* ${tableComment}\r\n*\r\n* @author ${author} ${email}\r\n* @since ${version} ${date}\r\n*/\r\n<#if baseClass??>@EqualsAndHashCode(callSuper=false)</#if>\r\n@Data\r\n@TableName(\"${tableName}\")\r\npublic class ${ClassName}Entity<#if baseClass??> extends ${baseClass.code}</#if> {\r\n<#list fieldList as field>\r\n<#if !field.baseField>\r\n    <#if field.fieldComment!?length gt 0>\r\n    /**\r\n     * ${field.fieldComment}\r\n     */\r\n    </#if>\r\n    <#if field.autoFill == \"INSERT\">\r\n    @TableField(value = \"${field.fieldName}\", fill = FieldFill.INSERT)\r\n    <#elseif field.autoFill == \"INSERT_UPDATE\">\r\n    @TableField(value = \"${field.fieldName}\", fill = FieldFill.INSERT_UPDATE)\r\n    <#elseif field.autoFill == \"UPDATE\">\r\n    @TableField(value = \"${field.fieldName}\", fill = FieldFill.UPDATE)\r\n    <#elseif field.primaryKey>\r\n        <#--如果是主键，仅使用@TableId注解-->\r\n    <#else>\r\n    @TableField(value = \"${field.fieldName}\")\r\n    </#if>\r\n    <#if field.primaryKey>\r\n    @TableId(value = \"${field.fieldName}\", type = IdType.AUTO)\r\n    </#if>\r\n    private ${field.attrType} ${field.attrName};\r\n</#if>\r\n\r\n</#list>\r\n}\r\n', '/java/Entity.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (308, 'Query.java.ftl', 1, 'FreeMarker', 'package ${package}.${moduleName}.query;\r\n\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\nimport lombok.Data;\r\nimport lombok.EqualsAndHashCode;\r\nimport ${package}.framework.common.query.Query;\r\n\r\n<#list importList as i>\r\nimport ${i!};\r\n</#list>\r\n\r\n/**\r\n * ${tableComment}查询\r\n *\r\n * @author ${author} ${email}\r\n * @since ${version} ${date}\r\n **/\r\n@Data\r\n@EqualsAndHashCode(callSuper = false)\r\n@Schema(description = \"${tableComment}查询\")\r\npublic class ${ClassName}Query extends Query {\r\n<#list queryList as field>\r\n    <#if field.fieldComment!?length gt 0>\r\n    @Schema(description = \"${field.fieldComment}\")\r\n    </#if>\r\n    private ${field.attrType} ${field.attrName};\r\n</#list>\r\n}\r\n', '/java/Query.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (309, 'Service.java.ftl', 1, 'FreeMarker', 'package ${package}.${moduleName}.service;\r\n\r\nimport ${package}.framework.common.utils.PageResult;\r\nimport ${package}.framework.mybatis.service.BaseService;\r\nimport ${package}.${moduleName}.vo.${ClassName}VO;\r\nimport ${package}.${moduleName}.query.${ClassName}Query;\r\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\r\n\r\nimport java.util.List;\r\n\r\n/**\r\n * ${tableComment}\r\n *\r\n * @author ${author} ${email}\r\n * @since ${version} ${date}\r\n **/\r\npublic interface ${ClassName}Service extends BaseService<${ClassName}Entity> {\r\n\r\n    PageResult<${ClassName}VO> page(${ClassName}Query query);\r\n\r\n    void save(${ClassName}VO vo);\r\n\r\n    void update(${ClassName}VO vo);\r\n\r\n    void delete(List<Long> idList);\r\n}\r\n', '/java/Service.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (310, 'ServiceImpl.java.ftl', 1, 'FreeMarker', 'package ${package}.${moduleName}.service.impl;\r\n\r\nimport com.baomidou.mybatisplus.core.toolkit.Wrappers;\r\nimport com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;\r\nimport com.baomidou.mybatisplus.core.metadata.IPage;\r\nimport lombok.AllArgsConstructor;\r\nimport ${package}.framework.common.utils.PageResult;\r\nimport ${package}.framework.mybatis.service.impl.BaseServiceImpl;\r\nimport ${package}.${moduleName}.convert.${ClassName}Convert;\r\nimport ${package}.${moduleName}.entity.${ClassName}Entity;\r\nimport ${package}.${moduleName}.query.${ClassName}Query;\r\nimport ${package}.${moduleName}.vo.${ClassName}VO;\r\nimport ${package}.${moduleName}.dao.${ClassName}Dao;\r\nimport ${package}.${moduleName}.service.${ClassName}Service;\r\nimport org.springframework.stereotype.Service;\r\nimport org.springframework.transaction.annotation.Transactional;\r\n\r\nimport java.util.List;\r\n\r\n/**\r\n * ${tableComment}\r\n *\r\n * @author ${author} ${email}\r\n * @since ${version} ${date}\r\n **/\r\n@Service\r\n@AllArgsConstructor\r\npublic class ${ClassName}ServiceImpl extends BaseServiceImpl<${ClassName}Dao, ${ClassName}Entity> implements ${ClassName}Service {\r\n\r\n    @Override\r\n    public PageResult<${ClassName}VO> page(${ClassName}Query query) {\r\n        IPage<${ClassName}Entity> page = baseMapper.selectPage(getPage(query), getWrapper(query));\r\n        return new PageResult<>(${ClassName}Convert.INSTANCE.convertList(page.getRecords()), page.getTotal());\r\n    }\r\n\r\n    private LambdaQueryWrapper<${ClassName}Entity> getWrapper(${ClassName}Query query){\r\n        LambdaQueryWrapper<${ClassName}Entity> wrapper = Wrappers.lambdaQuery();\r\n        return wrapper;\r\n    }\r\n\r\n    @Override\r\n    public void save(${ClassName}VO vo) {\r\n        ${ClassName}Entity entity = ${ClassName}Convert.INSTANCE.convert(vo);\r\n        baseMapper.insert(entity);\r\n    }\r\n\r\n    @Override\r\n    public void update(${ClassName}VO vo) {\r\n        ${ClassName}Entity entity = ${ClassName}Convert.INSTANCE.convert(vo);\r\n        updateById(entity);\r\n    }\r\n\r\n    @Override\r\n    @Transactional(rollbackFor = Exception.class)\r\n    public void delete(List<Long> idList) {\r\n        removeByIds(idList);\r\n    }\r\n}\r\n', '/java/ServiceImpl.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (311, 'VO.java.ftl', 1, 'FreeMarker', 'package ${package}.${moduleName}.vo;\r\n\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\nimport com.fasterxml.jackson.annotation.JsonFormat;\r\nimport lombok.Data;\r\nimport java.io.Serializable;\r\nimport ${package}.framework.common.utils.DateUtils;\r\n<#list importList as i>\r\nimport ${i!};\r\n</#list>\r\n\r\n/**\r\n * ${tableComment}\r\n *\r\n * @author ${author} ${email}\r\n * @since ${version} ${date}\r\n **/\r\n@Data\r\n@Schema(description = \"${tableComment}\")\r\npublic class ${ClassName}VO implements Serializable {\r\n    private static final long serialVersionUID = 1L;\r\n\r\n<#list fieldList as field>\r\n    <#if field.fieldComment!?length gt 0>\r\n    @Schema(description = \"${field.fieldComment}\")\r\n    </#if>\r\n    <#if field.attrType == \'Date\'>\r\n    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)\r\n    </#if>\r\n    private ${field.attrType} ${field.attrName};\r\n</#list>\r\n}\r\n', '/java/VO.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (312, 'controller.java.btl', 1, 'Beetl', 'package ${package.Controller};\r\n\r\nimport org.springframework.web.bind.annotation.RequestMapping;\r\n<% if(restControllerStyle){ %>\r\nimport org.springframework.web.bind.annotation.RestController;\r\n<% }else{ %>\r\nimport org.springframework.stereotype.Controller;\r\n<% } %>\r\n<% if(isNotEmpty(superControllerClassPackage)){ %>\r\nimport ${superControllerClassPackage};\r\n<% } %>\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!} 前端控制器\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<% if(restControllerStyle){ %>\r\n@RestController\r\n<% }else{ %>\r\n@Controller\r\n<% } %>\r\n@RequestMapping(\"<% if(isNotEmpty(package.ModuleName)){ %>/${package.ModuleName}<% } %>/<% if(controllerMappingHyphenStyle){ %>${controllerMappingHyphen}<% }else{ %>${table.entityPath}<% } %>\")\r\n<% if(kotlin){ %>\r\nclass ${table.controllerName}<% if(isNotEmpty(superControllerClass)){ %> : ${superControllerClass}()<% } %>\r\n<% }else{ %>\r\n    <% if(isNotEmpty(superControllerClass)){ %>\r\npublic class ${table.controllerName} extends ${superControllerClass} {\r\n    <% }else{ %>\r\npublic class ${table.controllerName} {\r\n    <% } %>\r\n\r\n}\r\n<% } %>\r\n', '/others/controller.java.btl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:47');
INSERT INTO `template_info` VALUES (313, 'controller.java.ej', 1, 'Enjoy', 'package #(package.Controller);\r\n\r\nimport org.springframework.web.bind.annotation.RequestMapping;\r\n#if(restControllerStyle)\r\nimport org.springframework.web.bind.annotation.RestController;\r\n#else\r\nimport org.springframework.stereotype.Controller;\r\n#end\r\n#if(superControllerClassPackage)\r\nimport #(superControllerClassPackage);\r\n#end\r\n\r\n/**\r\n * <p>\r\n * #(table.comment ??) 前端控制器\r\n * </p>\r\n *\r\n * @author #(author)\r\n * @since #(date)\r\n */\r\n#if(restControllerStyle)\r\n@RestController\r\n#else\r\n@Controller\r\n#end\r\n@RequestMapping(\"#if(package.ModuleName)/#(package.ModuleName)#end/#if(controllerMappingHyphenStyle)#(controllerMappingHyphen)#else#(table.entityPath)#end\")\r\n#if(kotlin)\r\nclass #(table.controllerName)#if(superControllerClass) : #(superControllerClass)()#end\r\n\r\n#else\r\n#if(superControllerClass)\r\npublic class #(table.controllerName) extends #(superControllerClass) {\r\n#else\r\npublic class #(table.controllerName) {\r\n#end\r\n\r\n}\r\n#end\r\n', '/others/controller.java.ej', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (314, 'controller.java.ftl', 1, 'FreeMarker', 'package ${package.Controller};\r\n\r\nimport org.springframework.web.bind.annotation.RequestMapping;\r\n<#if restControllerStyle>\r\nimport org.springframework.web.bind.annotation.RestController;\r\n<#else>\r\nimport org.springframework.stereotype.Controller;\r\n</#if>\r\n<#if superControllerClassPackage??>\r\nimport ${superControllerClassPackage};\r\n</#if>\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!} 前端控制器\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<#if restControllerStyle>\r\n@RestController\r\n<#else>\r\n@Controller\r\n</#if>\r\n@RequestMapping(\"<#if package.ModuleName?? && package.ModuleName != \"\">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>\")\r\n<#if kotlin>\r\nclass ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>\r\n<#else>\r\n<#if superControllerClass??>\r\npublic class ${table.controllerName} extends ${superControllerClass} {\r\n<#else>\r\npublic class ${table.controllerName} {\r\n</#if>\r\n\r\n}\r\n</#if>\r\n', '/others/controller.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (315, 'controller.java.vm', 1, 'Velocity', 'package ${package.Controller};\r\n\r\nimport org.springframework.web.bind.annotation.RequestMapping;\r\n#if(${restControllerStyle})\r\nimport org.springframework.web.bind.annotation.RestController;\r\n#else\r\nimport org.springframework.stereotype.Controller;\r\n#end\r\n#if(${superControllerClassPackage})\r\nimport ${superControllerClassPackage};\r\n#end\r\n\r\n/**\r\n * <p>\r\n * $!{table.comment} 前端控制器\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n#if(${restControllerStyle})\r\n@RestController\r\n#else\r\n@Controller\r\n#end\r\n@RequestMapping(\"#if(${package.ModuleName})/${package.ModuleName}#end/#if(${controllerMappingHyphenStyle})${controllerMappingHyphen}#else${table.entityPath}#end\")\r\n#if(${kotlin})\r\nclass ${table.controllerName}#if(${superControllerClass}) : ${superControllerClass}()#end\r\n\r\n#else\r\n#if(${superControllerClass})\r\npublic class ${table.controllerName} extends ${superControllerClass} {\r\n#else\r\npublic class ${table.controllerName} {\r\n#end\r\n\r\n}\r\n#end\r\n', '/others/controller.java.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (316, 'entity.java.btl', 1, 'Beetl', 'package ${package.Entity};\r\n\r\n<% for(pkg in table.importPackages){ %>\r\nimport ${pkg};\r\n<% } %>\r\n<% if(springdoc){ %>\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n<% }else if(swagger){ %>\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n<% } %>\r\n<% if(entityLombokModel){ %>\r\nimport lombok.Getter;\r\nimport lombok.Setter;\r\n<% if(chainModel){ %>\r\nimport lombok.experimental.Accessors;\r\n<% } %>\r\n<% } %>\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!}\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<% if(entityLombokModel){ %>\r\n@Getter\r\n@Setter\r\n    <% if(chainModel){ %>\r\n@Accessors(chain = true)\r\n    <% } %>\r\n<% } %>\r\n<% if(table.convert){ %>\r\n@TableName(\"${schemaName}${table.name}\")\r\n<% } %>\r\n<% if(springdoc){ %>\r\n@Schema(name = \"${entity}\", description = \"$!{table.comment}\")\r\n<% }else if(swagger){ %>\r\n@ApiModel(value = \"${entity}对象\", description = \"${table.comment!\'\'}\")\r\n<% } %>\r\n<% if(isNotEmpty(superEntityClass)){ %>\r\npublic class ${entity} extends ${superEntityClass}<% if(activeRecord){ %><${entity}><%}%>{\r\n<% }else if(activeRecord){ %>\r\npublic class ${entity} extends Model<${entity}> {\r\n<% }else if(entitySerialVersionUID){ %>\r\npublic class ${entity} implements Serializable {\r\n<% }else{ %>\r\npublic class ${entity} {\r\n<% } %>\r\n<% if(entitySerialVersionUID){ %>\r\n\r\n    private static final long serialVersionUID = 1L;\r\n<% } %>\r\n<% var keyPropertyName; %>\r\n<% /** -----------BEGIN 字段循环遍历----------- **/ %>\r\n<% for(field in table.fields){ %>\r\n    <%\r\n    if(field.keyFlag){\r\n        keyPropertyName = field.propertyName;\r\n    }\r\n    %>\r\n    <% if(isNotEmpty(field.comment)){ %>\r\n\r\n        <% if(springdoc){ %>\r\n    @Schema(description = \"${field.comment}\")\r\n        <% }else if(swagger){ %>\r\n    @ApiModelProperty(value = \"${field.comment}\")\r\n        <% }else{ %>\r\n    /**\r\n     * ${field.comment}\r\n     */\r\n        <% } %>\r\n    <% } %>\r\n    <% if(field.keyFlag){ %>\r\n    <%\r\n    /*主键*/\r\n    %>\r\n        <% if(field.keyIdentityFlag){ %>\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.AUTO)\r\n        <% }else if(isNotEmpty(idType)){ %>\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.${idType})\r\n        <% }else if(field.convert){ %>\r\n    @TableId(\"${field.annotationColumnName}\")\r\n         <% } %>\r\n    <%\r\n    /*普通字段*/\r\n    %>\r\n    <% }else if(isNotEmpty(field.fill)){ %>\r\n        <% if(field.convert){ %>\r\n    @TableField(value = \"${field.annotationColumnName}\", fill = FieldFill.${field.fill})\r\n        <% }else{ %>\r\n    @TableField(fill = FieldFill.${field.fill})\r\n        <% } %>\r\n    <% }else if(field.convert){ %>\r\n    @TableField(\"${field.annotationColumnName}\")\r\n    <% } %>\r\n    <%\r\n    /*乐观锁注解*/\r\n    %>\r\n    <% if(field.versionField){ %>\r\n    @Version\r\n    <% } %>\r\n    <%\r\n    /*逻辑删除注解*/\r\n    %>\r\n    <% if(field.logicDeleteField){ %>\r\n    @TableLogic\r\n    <% } %>\r\n    private ${field.propertyType} ${field.propertyName};\r\n<% } %>\r\n<% /** -----------END 字段循环遍历----------- **/ %>\r\n<% if(!entityLombokModel){ %>\r\n    <% for(field in table.fields){ %>\r\n        <%\r\n        var getprefix =\'\';\r\n        if(field.propertyType==\'boolean\'){\r\n            getprefix=\'is\';\r\n        }else{\r\n            getprefix=\'get\';\r\n        }\r\n        %>\r\n\r\n    public ${field.propertyType} ${getprefix}${field.capitalName}() {\r\n        return ${field.propertyName};\r\n    }\r\n\r\n        <% if(chainModel){ %>\r\n    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n        <% }else{ %>\r\n    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n        <% } %>\r\n        this.${field.propertyName} = ${field.propertyName};\r\n        <% if(chainModel){ %>\r\n        return this;\r\n        <% } %>\r\n    }\r\n    <% } %>\r\n<% } %>\r\n<% if(entityColumnConstant){ %>\r\n   <% for(field in table.fields){ %>\r\n\r\n    public static final String ${strutil.toUpperCase(field.name)} = \"${field.name}\";\r\n   <% } %>\r\n<% } %>\r\n<% if(activeRecord){ %>\r\n\r\n    @Override\r\n    public Serializable pkVal() {\r\n    <% if(isNotEmpty(keyPropertyName)){ %>\r\n        return this.${keyPropertyName};\r\n    <% }else{ %>\r\n        return null;\r\n    <% } %>\r\n    }\r\n<% } %>\r\n<% if(!entityLombokModel){ %>\r\n\r\n    @Override\r\n    public String toString() {\r\n        return \"${entity}{\" +\r\n    <% for(field in table.fields){ %>\r\n       <% if(fieldLP.index==0){ %>\r\n        \"${field.propertyName} = \" + ${field.propertyName} +\r\n       <% }else{ %>\r\n        \", ${field.propertyName} = \" + ${field.propertyName} +\r\n       <% } %>\r\n    <% } %>\r\n        \"}\";\r\n    }\r\n<% } %>\r\n}\r\n', '/others/entity.java.btl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (317, 'entity.java.ej', 1, 'Enjoy', 'package #(package.Entity);\r\n\r\n#for(pkg : table.importPackages)\r\nimport #(pkg);\r\n#end\r\n#if(springdoc)\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n#elseif(swagger)\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n#end\r\n#if(entityLombokModel)\r\nimport lombok.Getter;\r\nimport lombok.Setter;\r\n#if(chainModel)\r\nimport lombok.experimental.Accessors;\r\n#end\r\n#end\r\n\r\n/**\r\n * <p>\r\n * #(table.comment)\r\n * </p>\r\n *\r\n * @author #(author)\r\n * @since #(date)\r\n */\r\n#if(entityLombokModel)\r\n@Getter\r\n@Setter\r\n  #if(chainModel)\r\n@Accessors(chain = true)\r\n  #end\r\n#end\r\n#if(table.isConvert())\r\n@TableName(\"#(schemaName)#(table.name)\")\r\n#end\r\n#if(springdoc)\r\n@Schema(name = \"#(entity)\", description = \"#(table.comment)\")\r\n#elseif(swagger)\r\n@ApiModel(value = \"#(entity)对象\", description = \"#(table.comment)\")\r\n#end\r\n#if(superEntityClass)\r\npublic class #(entity) extends #(superEntityClass)#if(activeRecord)<#(entity)>#end {\r\n#elseif(activeRecord)\r\npublic class #(entity) extends Model<#(entity)> {\r\n#elseif(entitySerialVersionUID)\r\npublic class #(entity) implements Serializable {\r\n#else\r\npublic class #(entity) {\r\n#end\r\n#if(entitySerialVersionUID)\r\n\r\n    private static final long serialVersionUID = 1L;\r\n#end\r\n### ----------  BEGIN 字段循环遍历  ----------\r\n#for(field : table.fields)\r\n\r\n#if(field.isKeyFlag())\r\n#set(keyPropertyName = field.propertyName)\r\n#end\r\n#if(field.comment != null)\r\n  #if(springdoc)\r\n    @Schema(description = \"#(field.comment)\")\r\n  #elseif(swagger)\r\n    @ApiModelProperty(\"#(field.comment)\")\r\n  #else\r\n    /**\r\n     * #(field.comment)\r\n     */\r\n  #end\r\n#end\r\n#if(field.isKeyFlag())\r\n### 主键\r\n  #if(field.isKeyIdentityFlag())\r\n    @TableId(value = \"#(field.annotationColumnName)\", type = IdType.AUTO)\r\n  #elseif(idType != null && idType != \"\")\r\n    @TableId(value = \"#(field.annotationColumnName)\", type = IdType.#(idType))\r\n  #elseif(field.isConvert())\r\n    @TableId(\"#(field.annotationColumnName)\")\r\n  #end\r\n### 普通字段\r\n#elseif(field.fill)\r\n### -----   存在字段填充设置   -----\r\n  #if(field.convert)\r\n    @TableField(value = \"#(field.annotationColumnName)\", fill = FieldFill.#(field.fill))\r\n  #else\r\n    @TableField(fill = FieldFill.#(field.fill))\r\n  #end\r\n#elseif(field.isConvert())\r\n    @TableField(\"#(field.annotationColumnName)\")\r\n#end\r\n### 乐观锁注解\r\n#if(field.isVersionField())\r\n    @Version\r\n#end\r\n### 逻辑删除注解\r\n#if(field.isLogicDeleteField())\r\n    @TableLogic\r\n#end\r\n    private #(field.propertyType) #(field.propertyName);\r\n#end\r\n### ----------  END 字段循环遍历  ----------\r\n#if(!entityLombokModel)\r\n#for(field : table.fields)\r\n  #if(field.propertyType.equals(\"boolean\"))\r\n    #set(getprefix=\"is\")\r\n  #else\r\n    #set(getprefix=\"get\")\r\n  #end\r\n\r\n    public #(field.propertyType) #(getprefix)#(field.capitalName)() {\r\n        return #(field.propertyName);\r\n    }\r\n\r\n  #if(chainModel)\r\n    public #(entity) set#(field.capitalName)(#(field.propertyType) #(field.propertyName)) {\r\n  #else\r\n    public void set#(field.capitalName)(#(field.propertyType) #(field.propertyName)) {\r\n  #end\r\n        this.#(field.propertyName) = #(field.propertyName);\r\n  #if(chainModel)\r\n        return this;\r\n  #end\r\n    }\r\n#end\r\n### --foreach end---\r\n#end\r\n### --end of #if(entityLombokModel)--\r\n#if(entityColumnConstant)\r\n  #for(field : table.fields)\r\n\r\n    public static final String #(field.name.toUpperCase()) = \"#(field.name)\";\r\n  #end\r\n#end\r\n#if(activeRecord)\r\n\r\n    @Override\r\n    public Serializable pkVal() {\r\n  #if(keyPropertyName)\r\n        return this.#(keyPropertyName);\r\n  #else\r\n        return null;\r\n  #end\r\n    }\r\n#end\r\n#if(!entityLombokModel)\r\n\r\n    @Override\r\n    public String toString() {\r\n        return \"#(entity){\" +\r\n  #for(field : table.fields)\r\n    #if(for.index == 0)\r\n        \"#(field.propertyName) = \" + #(field.propertyName) +\r\n    #else\r\n        \", #(field.propertyName) = \" + #(field.propertyName) +\r\n    #end\r\n  #end\r\n        \"}\";\r\n    }\r\n#end\r\n}\r\n', '/others/entity.java.ej', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (318, 'entity.java.ftl', 1, 'FreeMarker', 'package ${package.Entity};\r\n\r\n<#list table.importPackages as pkg>\r\nimport ${pkg};\r\n</#list>\r\n<#if springdoc>\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n<#elseif swagger>\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n</#if>\r\n<#if entityLombokModel>\r\nimport lombok.Getter;\r\nimport lombok.Setter;\r\n    <#if chainModel>\r\nimport lombok.experimental.Accessors;\r\n    </#if>\r\n</#if>\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!}\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<#if entityLombokModel>\r\n@Getter\r\n@Setter\r\n    <#if chainModel>\r\n@Accessors(chain = true)\r\n    </#if>\r\n</#if>\r\n<#if table.convert>\r\n@TableName(\"${schemaName}${table.name}\")\r\n</#if>\r\n<#if springdoc>\r\n@Schema(name = \"${entity}\", description = \"$!{table.comment}\")\r\n<#elseif swagger>\r\n@ApiModel(value = \"${entity}对象\", description = \"${table.comment!}\")\r\n</#if>\r\n<#if superEntityClass??>\r\npublic class ${entity} extends ${superEntityClass}<#if activeRecord><${entity}></#if> {\r\n<#elseif activeRecord>\r\npublic class ${entity} extends Model<${entity}> {\r\n<#elseif entitySerialVersionUID>\r\npublic class ${entity} implements Serializable {\r\n<#else>\r\npublic class ${entity} {\r\n</#if>\r\n<#if entitySerialVersionUID>\r\n\r\n    private static final long serialVersionUID = 1L;\r\n</#if>\r\n<#-- ----------  BEGIN 字段循环遍历  ---------->\r\n<#list table.fields as field>\r\n    <#if field.keyFlag>\r\n        <#assign keyPropertyName=\"${field.propertyName}\"/>\r\n    </#if>\r\n\r\n    <#if field.comment!?length gt 0>\r\n        <#if springdoc>\r\n    @Schema(description = \"${field.comment}\")\r\n        <#elseif swagger>\r\n    @ApiModelProperty(\"${field.comment}\")\r\n        <#else>\r\n    /**\r\n     * ${field.comment}\r\n     */\r\n        </#if>\r\n    </#if>\r\n    <#if field.keyFlag>\r\n        <#-- 主键 -->\r\n        <#if field.keyIdentityFlag>\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.AUTO)\r\n        <#elseif idType??>\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.${idType})\r\n        <#elseif field.convert>\r\n    @TableId(\"${field.annotationColumnName}\")\r\n        </#if>\r\n        <#-- 普通字段 -->\r\n    <#elseif field.fill??>\r\n    <#-- -----   存在字段填充设置   ----->\r\n        <#if field.convert>\r\n    @TableField(value = \"${field.annotationColumnName}\", fill = FieldFill.${field.fill})\r\n        <#else>\r\n    @TableField(fill = FieldFill.${field.fill})\r\n        </#if>\r\n    <#elseif field.convert>\r\n    @TableField(\"${field.annotationColumnName}\")\r\n    </#if>\r\n    <#-- 乐观锁注解 -->\r\n    <#if field.versionField>\r\n    @Version\r\n    </#if>\r\n    <#-- 逻辑删除注解 -->\r\n    <#if field.logicDeleteField>\r\n    @TableLogic\r\n    </#if>\r\n    private ${field.propertyType} ${field.propertyName};\r\n</#list>\r\n<#------------  END 字段循环遍历  ---------->\r\n<#if !entityLombokModel>\r\n    <#list table.fields as field>\r\n        <#if field.propertyType == \"boolean\">\r\n            <#assign getprefix=\"is\"/>\r\n        <#else>\r\n            <#assign getprefix=\"get\"/>\r\n        </#if>\r\n\r\n    public ${field.propertyType} ${getprefix}${field.capitalName}() {\r\n        return ${field.propertyName};\r\n    }\r\n\r\n    <#if chainModel>\r\n    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n    <#else>\r\n    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n    </#if>\r\n        this.${field.propertyName} = ${field.propertyName};\r\n        <#if chainModel>\r\n        return this;\r\n        </#if>\r\n    }\r\n    </#list>\r\n</#if>\r\n<#if entityColumnConstant>\r\n    <#list table.fields as field>\r\n\r\n    public static final String ${field.name?upper_case} = \"${field.name}\";\r\n    </#list>\r\n</#if>\r\n<#if activeRecord>\r\n\r\n    @Override\r\n    public Serializable pkVal() {\r\n    <#if keyPropertyName??>\r\n        return this.${keyPropertyName};\r\n    <#else>\r\n        return null;\r\n    </#if>\r\n    }\r\n</#if>\r\n<#if !entityLombokModel>\r\n\r\n    @Override\r\n    public String toString() {\r\n        return \"${entity}{\" +\r\n    <#list table.fields as field>\r\n        <#if field_index==0>\r\n            \"${field.propertyName} = \" + ${field.propertyName} +\r\n        <#else>\r\n            \", ${field.propertyName} = \" + ${field.propertyName} +\r\n        </#if>\r\n    </#list>\r\n        \"}\";\r\n    }\r\n</#if>\r\n}\r\n', '/others/entity.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (319, 'entity.java.vm', 1, 'Velocity', 'package ${package.Entity};\r\n\r\n#foreach($pkg in ${table.importPackages})\r\nimport ${pkg};\r\n#end\r\n#if(${springdoc})\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n#elseif(${swagger})\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n#end\r\n#if(${entityLombokModel})\r\nimport lombok.Getter;\r\nimport lombok.Setter;\r\n#if(${chainModel})\r\nimport lombok.experimental.Accessors;\r\n#end\r\n#end\r\n\r\n/**\r\n * <p>\r\n * $!{table.comment}\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n#if(${entityLombokModel})\r\n@Getter\r\n@Setter\r\n  #if(${chainModel})\r\n@Accessors(chain = true)\r\n  #end\r\n#end\r\n#if(${table.convert})\r\n@TableName(\"${schemaName}${table.name}\")\r\n#end\r\n#if(${springdoc})\r\n@Schema(name = \"${entity}\", description = \"$!{table.comment}\")\r\n#elseif(${swagger})\r\n@ApiModel(value = \"${entity}对象\", description = \"$!{table.comment}\")\r\n#end\r\n#if(${superEntityClass})\r\npublic class ${entity} extends ${superEntityClass}#if(${activeRecord})<${entity}>#end {\r\n#elseif(${activeRecord})\r\npublic class ${entity} extends Model<${entity}> {\r\n#elseif(${entitySerialVersionUID})\r\npublic class ${entity} implements Serializable {\r\n#else\r\npublic class ${entity} {\r\n#end\r\n#if(${entitySerialVersionUID})\r\n\r\n    private static final long serialVersionUID = 1L;\r\n#end\r\n## ----------  BEGIN 字段循环遍历  ----------\r\n#foreach($field in ${table.columns})\r\n\r\n#if(${field.keyFlag})\r\n#set($keyPropertyName=${field.propertyName})\r\n#end\r\n#if(\"$!field.comment\" != \"\")\r\n  #if(${springdoc})\r\n    @Schema(description = \"${field.comment}\")\r\n  #elseif(${swagger})\r\n    @ApiModelProperty(\"${field.comment}\")\r\n  #else\r\n    /**\r\n     * ${field.comment}\r\n     */\r\n  #end\r\n#end\r\n#if(${field.keyFlag})\r\n## 主键\r\n  #if(${field.keyIdentityFlag})\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.AUTO)\r\n  #elseif(!$null.isNull(${idType}) && \"$!idType\" != \"\")\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.${idType})\r\n  #elseif(${field.convert})\r\n    @TableId(\"${field.annotationColumnName}\")\r\n  #end\r\n## 普通字段\r\n#elseif(${field.fill})\r\n## -----   存在字段填充设置   -----\r\n  #if(${field.convert})\r\n    @TableField(value = \"${field.annotationColumnName}\", fill = FieldFill.${field.fill})\r\n  #else\r\n    @TableField(fill = FieldFill.${field.fill})\r\n  #end\r\n#elseif(${field.convert})\r\n    @TableField(\"${field.annotationColumnName}\")\r\n#end\r\n## 乐观锁注解\r\n#if(${field.versionField})\r\n    @Version\r\n#end\r\n## 逻辑删除注解\r\n#if(${field.logicDeleteField})\r\n    @TableLogic\r\n#end\r\n    private ${field.propertyType} ${field.propertyName};\r\n#end\r\n## ----------  END 字段循环遍历  ----------\r\n#if(!${entityLombokModel})\r\n#foreach($field in ${table.fields})\r\n  #if(${field.propertyType.equals(\"boolean\")})\r\n    #set($getprefix=\"is\")\r\n  #else\r\n    #set($getprefix=\"get\")\r\n  #end\r\n\r\n    public ${field.propertyType} ${getprefix}${field.capitalName}() {\r\n        return ${field.propertyName};\r\n    }\r\n\r\n  #if(${chainModel})\r\n    public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n  #else\r\n    public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n  #end\r\n        this.${field.propertyName} = ${field.propertyName};\r\n  #if(${chainModel})\r\n        return this;\r\n  #end\r\n    }\r\n#end\r\n## --foreach end---\r\n#end\r\n## --end of #if(!${entityLombokModel})--\r\n#if(${entityColumnConstant})\r\n  #foreach($field in ${table.fields})\r\n\r\n    public static final String ${field.name.toUpperCase()} = \"${field.name}\";\r\n  #end\r\n#end\r\n#if(${activeRecord})\r\n\r\n    @Override\r\n    public Serializable pkVal() {\r\n  #if(${keyPropertyName})\r\n        return this.${keyPropertyName};\r\n  #else\r\n        return null;\r\n  #end\r\n    }\r\n#end\r\n#if(!${entityLombokModel})\r\n\r\n    @Override\r\n    public String toString() {\r\n        return \"${entity}{\" +\r\n  #foreach($field in ${table.fields})\r\n    #if($!{foreach.index}==0)\r\n        \"${field.propertyName} = \" + ${field.propertyName} +\r\n    #else\r\n        \", ${field.propertyName} = \" + ${field.propertyName} +\r\n    #end\r\n  #end\r\n        \"}\";\r\n    }\r\n#end\r\n}\r\n', '/others/entity.java.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (320, 'entity.kt.btl', 1, 'Beetl', 'package ${package.Entity}\r\n\r\n<% for(pkg in table.importPackages){ %>\r\nimport ${pkg}\r\n<% } %>\r\n<% if(springdoc){ %>\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n<% }else if(swagger){ %>\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n<% } %>\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!}\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<% if(table.convert){ %>\r\n@TableName(\"${schemaName}${table.name}\")\r\n<% } %>\r\n<% if(springdoc){ %>\r\n@Schema(name = \"${entity}\", description = \"$!{table.comment}\")\r\n<% }else if(swagger){ %>\r\n@ApiModel(value = \"${entity}对象\", description = \"${table.comment!\'\'}\")\r\n<% } %>\r\n<% if(isNotEmpty(superEntityClass)){ %>\r\nclass ${entity} : ${superEntityClass}<% if(activeRecord){ %><${entity}><%}%>{\r\n<% }else if(activeRecord){ %>\r\nclass ${entity} : Model<${entity}> {\r\n<% }else if(entitySerialVersionUID){ %>\r\nclass ${entity} : Serializable {\r\n<% }else{ %>\r\nclass ${entity} {\r\n<% } %>\r\n\r\n<% /** -----------BEGIN 字段循环遍历----------- **/ %>\r\n<% for(field in table.fields){ %>\r\n    <%\r\n    if(field.keyFlag){\r\n        var keyPropertyName = field.propertyName;\r\n    }\r\n    %>\r\n    <% if(isNotEmpty(field.comment)){ %>\r\n        <% if(springdoc){ %>\r\n    @Schema(description = \"${field.comment}\")\r\n        <% }else if(swagger){ %>\r\n    @ApiModelProperty(value = \"${field.comment}\")\r\n        <% }else{ %>\r\n    /**\r\n     * ${field.comment}\r\n     */\r\n        <% } %>\r\n    <% } %>\r\n    <% if(field.keyFlag){ %>\r\n    <%\r\n    /*主键*/\r\n    %>\r\n        <% if(field.keyIdentityFlag){ %>\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.AUTO)\r\n        <% }else if(isNotEmpty(idType)){ %>\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.${idType})\r\n        <% }else if(field.convert){ %>\r\n    @TableId(\"${field.columnName}\")\r\n         <% } %>\r\n    <%\r\n    /*普通字段*/\r\n    %>\r\n    <% }else if(isNotEmpty(field.fill)){ %>\r\n        <% if(field.convert){ %>\r\n    @TableField(value = \"${field.annotationColumnName}\", fill = FieldFill.${field.fill})\r\n        <% }else{ %>\r\n    @TableField(fill = FieldFill.${field.fill})\r\n        <% } %>\r\n    <% }else if(field.convert){ %>\r\n    @TableField(\"${field.annotationColumnName}\")\r\n    <% } %>\r\n    <%\r\n    /*乐观锁注解*/\r\n    %>\r\n    <% if(field.versionField){ %>\r\n    @Version\r\n    <% } %>\r\n    <%\r\n    /*逻辑删除注解*/\r\n    %>\r\n    <% if(field.logicDeleteField){ %>\r\n    @TableLogic\r\n    <% } %>\r\n    <% if(field.propertyType == \'Integer\'){ %>\r\n    var ${field.propertyName}: Int ? = null\r\n    <% }else{ %>\r\n    var ${field.propertyName}: ${field.propertyType} ? = null\r\n    <% } %>\r\n\r\n<% } %>\r\n<% /** -----------END 字段循环遍历----------- **/ %>\r\n<% if(entityColumnConstant){ %>\r\n    companion object {\r\n   <% for(field in table.fields){ %>\r\n    const val ${strutil.toUpperCase(field.name)} : String = \"${field.name}\"\r\n   <% } %>\r\n    }\r\n<% } %>\r\n<% if(activeRecord){ %>\r\n    @Override\r\n    override fun pkVal(): Serializable? {\r\n    <% if(isNotEmpty(keyPropertyName)){ %>\r\n        return this.${keyPropertyName}\r\n    <% }else{ %>\r\n        return null;\r\n    <% } %>\r\n    }\r\n\r\n<% } %>\r\n<% if(!entityLombokModel){ %>\r\n    @Override\r\n    override fun toString(): String  {\r\n        return \"${entity}{\" +\r\n    <% for(field in table.fields){ %>\r\n       <% if(fieldLP.index==0){ %>\r\n        \"${field.propertyName}=\" + ${field.propertyName} +\r\n       <% }else{ %>\r\n        \", ${field.propertyName}=\" + ${field.propertyName} +\r\n       <% } %>\r\n    <% } %>\r\n        \"}\"\r\n    }\r\n<% } %>\r\n}\r\n', '/others/entity.kt.btl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (321, 'entity.kt.ej', 1, 'Enjoy', 'package #(package.Entity);\r\n\r\n#for(pkg : table.importPackages)\r\nimport #(pkg);\r\n#end\r\n#if(springdoc)\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n#elseif(swagger)\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n#end\r\n\r\n/**\r\n * <p>\r\n * #(table.comment)\r\n * </p>\r\n *\r\n * @author #(author)\r\n * @since #(date)\r\n */\r\n#if(table.isConvert())\r\n@TableName(\"#(schemaName)#(table.name)\")\r\n#end\r\n#if(springdoc)\r\n@Schema(name = \"#(entity)\", description = \"#(table.comment)\")\r\n#elseif(swagger)\r\n@ApiModel(value = \"${entity}对象\", description = \"$!{table.comment}\")\r\n#end\r\n#if(superEntityClass)\r\nclass #(entity) : #(superEntityClass)#if(activeRecord)<#(entity)>#end() {\r\n#elseif(activeRecord)\r\nclass #(entity) : Model<#(entity)>() {\r\n#elseif(entitySerialVersionUID)\r\nclass #(entity) : Serializable {\r\n#else\r\nclass #(entity) {\r\n#end\r\n\r\n### ----------  BEGIN 字段循环遍历  ----------\r\n#for(field : table.fields)\r\n#if(field.keyFlag)\r\n#set(keyPropertyName = field.propertyName)\r\n#end\r\n#if(field.comment != null)\r\n    #if(springdoc)\r\n    @Schema(description = \"#(field.comment)\")\r\n    #elseif(swagger)\r\n    @ApiModelProperty(\"#(field.comment)\")\r\n    #else\r\n    /**\r\n     * #(field.comment)\r\n     */\r\n    #end\r\n#end\r\n#if(field.isKeyFlag())\r\n### 主键\r\n#if(field.isKeyIdentityFlag())\r\n    @TableId(value = \"#(field.annotationColumnName)\", type = IdType.AUTO)\r\n#elseif(idType != null && idType != \"\")\r\n    @TableId(value = \"#(field.annotationColumnName)\", type = IdType.#(idType))\r\n#elseif(field.isConvert())\r\n    @TableId(\"#(field.annotationColumnName)\")\r\n#end\r\n### 普通字段\r\n#elseif(field.fill)\r\n### -----   存在字段填充设置   -----\r\n#if(field.convert)\r\n    @TableField(value = \"#(field.annotationColumnName)\", fill = FieldFill.#(field.fill))\r\n#else\r\n    @TableField(fill = FieldFill.#(field.fill))\r\n#end\r\n#elseif(field.isConvert())\r\n    @TableField(\"#(field.annotationColumnName)\")\r\n#end\r\n### 乐观锁注解\r\n#if(field.isVersionField())\r\n    @Version\r\n#end\r\n### 逻辑删除注解\r\n#if(field.isLogicDeleteField())\r\n    @TableLogic\r\n#end\r\n    #if(field.propertyType == \"Integer\")\r\n    var #(field.propertyName): Int? = null\r\n    #else\r\n    var #(field.propertyName): #(field.propertyType)? = null\r\n    #end\r\n\r\n#end\r\n### ----------  END 字段循环遍历  ----------\r\n#if(entityColumnConstant)\r\n    companion object {\r\n#for(field : table.fields)\r\n\r\n        const val #(field.name.toUpperCase()) : String = \"#(field.name)\"\r\n\r\n#end\r\n    }\r\n\r\n#end\r\n#if(activeRecord)\r\n    override fun pkVal(): Serializable? {\r\n#if(#(keyPropertyName))\r\n        return #(keyPropertyName)\r\n#else\r\n        return null\r\n#end\r\n    }\r\n\r\n#end\r\n    override fun toString(): String {\r\n        return \"#(entity){\" +\r\n#for(field : table.fields)\r\n#if($!{foreach.index}==0)\r\n        \"#(field.propertyName)=\" + #(field.propertyName) +\r\n#else\r\n        \", #(field.propertyName)=\" + #(field.propertyName) +\r\n#end\r\n#end\r\n        \"}\"\r\n    }\r\n}\r\n', '/others/entity.kt.ej', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:48');
INSERT INTO `template_info` VALUES (322, 'entity.kt.ftl', 1, 'FreeMarker', 'package ${package.Entity}\r\n\r\n<#list table.importPackages as pkg>\r\nimport ${pkg}\r\n</#list>\r\n<#if springdoc>\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n<#elseif swagger>\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n</#if>\r\n\r\n/**\r\n * <p>\r\n * ${table.comment}\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<#if table.convert>\r\n@TableName(\"${schemaName}${table.name}\")\r\n</#if>\r\n<#if springdoc>\r\n@Schema(name = \"${entity}\", description = \"$!{table.comment}\")\r\n<#elseif swagger>\r\n@ApiModel(value = \"${entity}对象\", description = \"${table.comment!}\")\r\n</#if>\r\n<#if superEntityClass??>\r\nclass ${entity} : ${superEntityClass}<#if activeRecord><${entity}></#if> {\r\n<#elseif activeRecord>\r\nclass ${entity} : Model<${entity}>() {\r\n<#elseif entitySerialVersionUID>\r\nclass ${entity} : Serializable {\r\n<#else>\r\nclass ${entity} {\r\n</#if>\r\n\r\n<#-- ----------  BEGIN 字段循环遍历  ---------->\r\n<#list table.fields as field>\r\n<#if field.keyFlag>\r\n    <#assign keyPropertyName=\"${field.propertyName}\"/>\r\n</#if>\r\n<#if field.comment!?length gt 0>\r\n    <#if springdoc>\r\n    @Schema(description = \"${field.comment}\")\r\n    <#elseif swagger>\r\n    @ApiModelProperty(\"${field.comment}\")\r\n    <#else>\r\n    /**\r\n     * ${field.comment}\r\n     */\r\n    </#if>\r\n</#if>\r\n<#if field.keyFlag>\r\n<#-- 主键 -->\r\n<#if field.keyIdentityFlag>\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.AUTO)\r\n<#elseif idType ??>\r\n    @TableId(value = \"${field.annotationColumnName}\", type = IdType.${idType})\r\n<#elseif field.convert>\r\n    @TableId(\"${field.annotationColumnName}\")\r\n</#if>\r\n<#-- 普通字段 -->\r\n<#elseif field.fill??>\r\n<#-- -----   存在字段填充设置   ----->\r\n<#if field.convert>\r\n    @TableField(value = \"${field.annotationColumnName}\", fill = FieldFill.${field.fill})\r\n<#else>\r\n    @TableField(fill = FieldFill.${field.fill})\r\n</#if>\r\n<#elseif field.convert>\r\n    @TableField(\"${field.annotationColumnName}\")\r\n</#if>\r\n<#-- 乐观锁注解 -->\r\n<#if field.versionField>\r\n    @Version\r\n</#if>\r\n<#-- 逻辑删除注解 -->\r\n<#if field.logicDeleteField>\r\n    @TableLogic\r\n</#if>\r\n    <#if field.propertyType == \"Integer\">\r\n    var ${field.propertyName}: Int? = null\r\n    <#else>\r\n    var ${field.propertyName}: ${field.propertyType}? = null\r\n    </#if>\r\n\r\n</#list>\r\n<#-- ----------  END 字段循环遍历  ---------->\r\n<#if entityColumnConstant>\r\n    companion object {\r\n<#list table.fields as field>\r\n\r\n        const val ${field.name?upper_case} : String = \"${field.name}\"\r\n\r\n</#list>\r\n    }\r\n\r\n</#if>\r\n<#if activeRecord>\r\n    override fun pkVal(): Serializable? {\r\n<#if keyPropertyName??>\r\n        return ${keyPropertyName}\r\n<#else>\r\n        return null\r\n</#if>\r\n    }\r\n\r\n</#if>\r\n    override fun toString(): String {\r\n        return \"${entity}{\" +\r\n<#list table.fields as field>\r\n<#if field_index==0>\r\n        \"${field.propertyName}=\" + ${field.propertyName} +\r\n<#else>\r\n        \", ${field.propertyName}=\" + ${field.propertyName} +\r\n</#if>\r\n</#list>\r\n        \"}\"\r\n    }\r\n}\r\n', '/others/entity.kt.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (323, 'entity.kt.vm', 1, 'Velocity', 'package ${package.Entity};\r\n\r\n#foreach($pkg in ${table.importPackages})\r\nimport ${pkg};\r\n#end\r\n#if(${springdoc})\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n#elseif(${swagger})\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n#end\r\n\r\n/**\r\n * <p>\r\n * $!{table.comment}\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n#if(${table.convert})\r\n@TableName(\"${schemaName}${table.name}\")\r\n#end\r\n#if(${springdoc})\r\n@Schema(name = \"${entity}\", description = \"$!{table.comment}\")\r\n#elseif(${swagger})\r\n@ApiModel(value = \"${entity}对象\", description = \"$!{table.comment}\")\r\n#end\r\n#if(${superEntityClass})\r\nclass ${entity} : ${superEntityClass}#if(${activeRecord})<${entity}>#end() {\r\n#elseif(${activeRecord})\r\nclass ${entity} : Model<${entity}>() {\r\n#elseif(${entitySerialVersionUID})\r\nclass ${entity} : Serializable {\r\n#else\r\nclass ${entity} {\r\n#end\r\n\r\n## ----------  BEGIN 字段循环遍历  ----------\r\n#foreach($field in ${table.fields})\r\n    #if(${field.keyFlag})\r\n        #set($keyPropertyName=${field.propertyName})\r\n    #end\r\n    #if(\"$!field.comment\" != \"\")\r\n        #if(${springdoc})\r\n        @Schema(description = \"${field.comment}\")\r\n        #elseif(${swagger})\r\n        @ApiModelProperty(\"${field.comment}\")\r\n        #else\r\n        /**\r\n         * ${field.comment}\r\n         */\r\n        #end\r\n    #end\r\n    #if(${field.keyFlag})\r\n        ## 主键\r\n        #if(${field.keyIdentityFlag})\r\n        @TableId(value = \"${field.annotationColumnName}\", type = IdType.AUTO)\r\n        #elseif(!$null.isNull(${idType}) && \"$!idType\" != \"\")\r\n        @TableId(value = \"${field.annotationColumnName}\", type = IdType.${idType})\r\n        #elseif(${field.convert})\r\n        @TableId(\"${field.annotationColumnName}\")\r\n        #end\r\n        ## 普通字段\r\n    #elseif(${field.fill})\r\n        ## -----   存在字段填充设置   -----\r\n        #if(${field.convert})\r\n        @TableField(value = \"${field.annotationColumnName}\", fill = FieldFill.${field.fill})\r\n        #else\r\n        @TableField(fill = FieldFill.${field.fill})\r\n        #end\r\n    #elseif(${field.convert})\r\n    @TableField(\"${field.annotationColumnName}\")\r\n    #end\r\n    ## 乐观锁注解\r\n    #if(${field.versionField})\r\n    @Version\r\n    #end\r\n    ## 逻辑删除注解\r\n    #if(${field.logicDeleteField})\r\n    @TableLogic\r\n    #end\r\n    #if(${field.propertyType} == \"Integer\")\r\n    var ${field.propertyName}: Int? = null\r\n    #else\r\n        var ${field.propertyName}: ${field.propertyType}? = null\r\n    #end\r\n\r\n#end\r\n## ----------  END 字段循环遍历  ----------\r\n#if(${entityColumnConstant})\r\n    companion object {\r\n        #foreach($field in ${table.fields})\r\n\r\n            const val ${field.name.toUpperCase()} : String = \"${field.name}\"\r\n\r\n        #end\r\n    }\r\n\r\n#end\r\n#if(${activeRecord})\r\n    override fun pkVal(): Serializable? {\r\n        #if(${keyPropertyName})\r\n            return ${keyPropertyName}\r\n        #else\r\n            return null\r\n        #end\r\n    }\r\n\r\n#end\r\n    override fun toString(): String {\r\n        return \"${entity}{\" +\r\n            #foreach($field in ${table.fields})\r\n                #if($!{foreach.index}==0)\r\n                    \"${field.propertyName}=\" + ${field.propertyName} +\r\n                #else\r\n                \", ${field.propertyName}=\" + ${field.propertyName} +\r\n                #end\r\n            #end\r\n        \"}\"\r\n    }\r\n}\r\n', '/others/entity.kt.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (324, 'mapper.java.btl', 1, 'Beetl', 'package ${package.Mapper};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${superMapperClassPackage};\r\n<% if(mapperAnnotationClass!=null){ %>\r\nimport ${mapperAnnotationClass.name};\r\n<% } %>\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!} Mapper 接口\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<% if(mapperAnnotationClass!=null){ %>\r\n@${mapperAnnotationClass.simpleName}\r\n<% } %>\r\n<% if(kotlin){ %>\r\ninterface ${table.mapperName} : ${superMapperClass}<${entity}>\r\n<% }else{ %>\r\npublic interface ${table.mapperName} extends ${superMapperClass}<${entity}> {\r\n\r\n}\r\n<% } %>\r\n', '/others/mapper.java.btl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (325, 'mapper.java.ej', 1, 'Enjoy', 'package #(package.Mapper);\r\n\r\nimport #(package.Entity).#(entity);\r\nimport #(superMapperClassPackage);\r\n#if(mapperAnnotationClass)\r\nimport #(mapperAnnotationClass.name);\r\n#end\r\n\r\n/**\r\n * <p>\r\n * #(table.comment) Mapper 接口\r\n * </p>\r\n *\r\n * @author #(author)\r\n * @since #(date)\r\n */\r\n#if(mapperAnnotationClass)\r\n@#(mapperAnnotationClass.simpleName)\r\n#end\r\n#if(kotlin)\r\ninterface #(table.mapperName) : #(superMapperClass)<#(entity)>\r\n#else\r\npublic interface #(table.mapperName) extends #(superMapperClass)<#(entity)> {\r\n\r\n}\r\n#end\r\n', '/others/mapper.java.ej', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (326, 'mapper.java.ftl', 1, 'FreeMarker', 'package ${package.Mapper};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${superMapperClassPackage};\r\n<#if mapperAnnotationClass??>\r\nimport ${mapperAnnotationClass.name};\r\n</#if>\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!} Mapper 接口\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<#if mapperAnnotationClass??>\r\n@${mapperAnnotationClass.simpleName}\r\n</#if>\r\n<#if kotlin>\r\ninterface ${table.mapperName} : ${superMapperClass}<${entity}>\r\n<#else>\r\npublic interface ${table.mapperName} extends ${superMapperClass}<${entity}> {\r\n\r\n}\r\n</#if>\r\n', '/others/mapper.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (327, 'mapper.java.vm', 1, 'Velocity', 'package ${package.Mapper};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${superMapperClassPackage};\r\n#if(${mapperAnnotationClass})\r\nimport ${mapperAnnotationClass.name};\r\n#end\r\n\r\n/**\r\n * <p>\r\n * $!{table.comment} Mapper 接口\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n#if(${mapperAnnotationClass})\r\n@${mapperAnnotationClass.simpleName}\r\n#end\r\n#if(${kotlin})\r\ninterface ${table.mapperName} : ${superMapperClass}<${entity}>\r\n#else\r\npublic interface ${table.mapperName} extends ${superMapperClass}<${entity}> {\r\n\r\n}\r\n#end\r\n', '/others/mapper.java.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (328, 'mapper.xml.btl', 1, 'Beetl', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n<mapper namespace=\"${package.Mapper}.${table.mapperName}\">\r\n\r\n<% if(enableCache){ %>\r\n    <!-- 开启二级缓存 -->\r\n    <cache type=\"${cacheClassName}\"/>\r\n\r\n<% } %>\r\n<% if(baseResultMap){ %>\r\n    <!-- 通用查询映射结果 -->\r\n    <resultMap id=\"BaseResultMap\" type=\"${package.Entity}.${entity}\">\r\n<% for(field in table.fields){ %>\r\n   <% /** 生成主键排在第一位 **/ %>\r\n   <% if(field.keyFlag){ %>\r\n        <id column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n   <% } %>\r\n<% } %>\r\n<% for(field in table.commonFields){ %>\r\n    <% /** 生成公共字段 **/ %>\r\n        <result column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n<% } %>\r\n<% for(field in table.fields){ %>\r\n   <% /** 生成普通字段 **/ %>\r\n   <% if(!field.keyFlag){ %>\r\n        <result column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n   <% } %>\r\n<% } %>\r\n    </resultMap>\r\n<% } %>\r\n<% if(baseColumnList){ %>\r\n    <!-- 通用查询结果列 -->\r\n    <sql id=\"Base_Column_List\">\r\n<% for(field in table.commonFields){ %>\r\n        ${field.columnName},\r\n<% } %>\r\n        ${table.fieldNames}\r\n    </sql>\r\n\r\n<% } %>\r\n</mapper>\r\n', '/others/mapper.xml.btl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (329, 'mapper.xml.ej', 1, 'Enjoy', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n<mapper namespace=\"#(package.Mapper).#(table.mapperName)\">\r\n\r\n#if(enableCache)\r\n    <!-- 开启二级缓存 -->\r\n    <cache type=\"#(cacheClassName)\"/>\r\n\r\n#end\r\n#if(baseResultMap)\r\n    <!-- 通用查询映射结果 -->\r\n    <resultMap id=\"BaseResultMap\" type=\"#(package.Entity).#(entity)\">\r\n#for(field : table.fields)\r\n#if(field.keyFlag)###生成主键排在第一位\r\n        <id column=\"#(field.name)\" property=\"#(field.propertyName)\" />\r\n#end\r\n#end\r\n#for(field : table.commonFields)###生成公共字段\r\n        <result column=\"#(field.name)\" property=\"#(field.propertyName)\" />\r\n#end\r\n#for(field : table.fields)\r\n#if(!field.keyFlag)###生成普通字段\r\n        <result column=\"#(field.name)\" property=\"#(field.propertyName)\" />\r\n#end\r\n#end\r\n    </resultMap>\r\n\r\n#end\r\n#if(baseColumnList)\r\n    <!-- 通用查询结果列 -->\r\n    <sql id=\"Base_Column_List\">\r\n#for(field : table.commonFields)\r\n        #(field.columnName),\r\n#end\r\n        #(table.fieldNames)\r\n    </sql>\r\n\r\n#end\r\n</mapper>\r\n', '/others/mapper.xml.ej', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (330, 'mapper.xml.ftl', 1, 'FreeMarker', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n<mapper namespace=\"${package.Mapper}.${table.mapperName}\">\r\n\r\n<#if enableCache>\r\n    <!-- 开启二级缓存 -->\r\n    <cache type=\"${cacheClassName}\"/>\r\n\r\n</#if>\r\n<#if baseResultMap>\r\n    <!-- 通用查询映射结果 -->\r\n    <resultMap id=\"BaseResultMap\" type=\"${package.Entity}.${entity}\">\r\n<#list table.fields as field>\r\n<#if field.keyFlag><#--生成主键排在第一位-->\r\n        <id column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n</#if>\r\n</#list>\r\n<#list table.commonFields as field><#--生成公共字段 -->\r\n        <result column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n</#list>\r\n<#list table.fields as field>\r\n<#if !field.keyFlag><#--生成普通字段 -->\r\n        <result column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n</#if>\r\n</#list>\r\n    </resultMap>\r\n\r\n</#if>\r\n<#if baseColumnList>\r\n    <!-- 通用查询结果列 -->\r\n    <sql id=\"Base_Column_List\">\r\n<#list table.commonFields as field>\r\n        ${field.columnName},\r\n</#list>\r\n        ${table.fieldNames}\r\n    </sql>\r\n\r\n</#if>\r\n</mapper>\r\n', '/others/mapper.xml.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (331, 'mapper.xml.vm', 1, 'Velocity', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n<mapper namespace=\"${package.Mapper}.${table.mapperName}\">\r\n\r\n#if(${enableCache})\r\n    <!-- 开启二级缓存 -->\r\n    <cache type=\"${cacheClassName}\"/>\r\n\r\n#end\r\n#if(${baseResultMap})\r\n    <!-- 通用查询映射结果 -->\r\n    <resultMap id=\"BaseResultMap\" type=\"${package.Entity}.${entity}\">\r\n#foreach($field in ${table.fields})\r\n#if(${field.keyFlag})##生成主键排在第一位\r\n        <id column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n#end\r\n#end\r\n#foreach($field in ${table.commonFields})##生成公共字段\r\n        <result column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n#end\r\n#foreach($field in ${table.fields})\r\n#if(!${field.keyFlag})##生成普通字段\r\n        <result column=\"${field.name}\" property=\"${field.propertyName}\" />\r\n#end\r\n#end\r\n    </resultMap>\r\n\r\n#end\r\n#if(${baseColumnList})\r\n    <!-- 通用查询结果列 -->\r\n    <sql id=\"Base_Column_List\">\r\n#foreach($field in ${table.commonFields})\r\n        ${field.columnName},\r\n#end\r\n        ${table.fieldNames}\r\n    </sql>\r\n\r\n#end\r\n</mapper>\r\n', '/others/mapper.xml.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (332, 'service.java.btl', 1, 'Beetl', 'package ${package.Service};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${superServiceClassPackage};\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!} 服务类\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<% if(kotlin){ %>\r\ninterface ${table.serviceName} : ${superServiceClass}<${entity}>\r\n<% }else{ %>\r\npublic interface ${table.serviceName} extends ${superServiceClass}<${entity}> {\r\n\r\n}\r\n<% } %>\r\n', '/others/service.java.btl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (333, 'service.java.ej', 1, 'Enjoy', 'package #(package.Service);\r\n\r\nimport #(package.Entity).#(entity);\r\nimport #(superServiceClassPackage);\r\n\r\n/**\r\n * <p>\r\n * #(table.comment) 服务类\r\n * </p>\r\n *\r\n * @author #(author)\r\n * @since #(date)\r\n */\r\n#if(kotlin)\r\ninterface #(table.serviceName) : #(superServiceClass)<#(entity)>\r\n#else\r\npublic interface #(table.serviceName) extends #(superServiceClass)<#(entity)> {\r\n\r\n}\r\n#end\r\n', '/others/service.java.ej', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (334, 'service.java.ftl', 1, 'FreeMarker', 'package ${package.Service};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${superServiceClassPackage};\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!} 服务类\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n<#if kotlin>\r\ninterface ${table.serviceName} : ${superServiceClass}<${entity}>\r\n<#else>\r\npublic interface ${table.serviceName} extends ${superServiceClass}<${entity}> {\r\n\r\n}\r\n</#if>\r\n', '/others/service.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:49');
INSERT INTO `template_info` VALUES (335, 'service.java.vm', 1, 'Velocity', 'package ${package.Service};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${superServiceClassPackage};\r\n\r\n/**\r\n * <p>\r\n * $!{table.comment} 服务类\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n#if(${kotlin})\r\ninterface ${table.serviceName} : ${superServiceClass}<${entity}>\r\n#else\r\npublic interface ${table.serviceName} extends ${superServiceClass}<${entity}> {\r\n\r\n}\r\n#end\r\n', '/others/service.java.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (336, 'serviceImpl.java.btl', 1, 'Beetl', 'package ${package.ServiceImpl};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${package.Mapper}.${table.mapperName};\r\nimport ${package.Service}.${table.serviceName};\r\nimport ${superServiceImplClassPackage};\r\nimport org.springframework.stereotype.Service;\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!} 服务实现类\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n@Service\r\n<% if(kotlin){ %>\r\nopen class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {\r\n\r\n}\r\n<% }else{ %>\r\npublic class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {\r\n\r\n}\r\n<% } %>\r\n', '/others/serviceImpl.java.btl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (337, 'serviceImpl.java.ej', 1, 'Enjoy', 'package #(package.ServiceImpl);\r\n\r\nimport #(package.Entity).#(entity);\r\nimport #(package.Mapper).#(table.mapperName);\r\nimport #(package.Service).#(table.serviceName);\r\nimport #(superServiceImplClassPackage);\r\nimport org.springframework.stereotype.Service;\r\n\r\n/**\r\n * <p>\r\n * #(table.comment) 服务实现类\r\n * </p>\r\n *\r\n * @author #(author)\r\n * @since #(date)\r\n */\r\n@Service\r\n#if(kotlin)\r\nopen class #(table.serviceImplName) : #(superServiceImplClass)<#(table.mapperName), #(entity)>(), #(table.serviceName) {\r\n\r\n}\r\n#else\r\npublic class #(table.serviceImplName) extends #(superServiceImplClass)<#(table.mapperName), #(entity)> implements #(table.serviceName) {\r\n\r\n}\r\n#end\r\n', '/others/serviceImpl.java.ej', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (338, 'serviceImpl.java.ftl', 1, 'FreeMarker', 'package ${package.ServiceImpl};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${package.Mapper}.${table.mapperName};\r\nimport ${package.Service}.${table.serviceName};\r\nimport ${superServiceImplClassPackage};\r\nimport org.springframework.stereotype.Service;\r\n\r\n/**\r\n * <p>\r\n * ${table.comment!} 服务实现类\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n@Service\r\n<#if kotlin>\r\nopen class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {\r\n\r\n}\r\n<#else>\r\npublic class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {\r\n\r\n}\r\n</#if>\r\n', '/others/serviceImpl.java.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (339, 'serviceImpl.java.vm', 1, 'Velocity', 'package ${package.ServiceImpl};\r\n\r\nimport ${package.Entity}.${entity};\r\nimport ${package.Mapper}.${table.mapperName};\r\nimport ${package.Service}.${table.serviceName};\r\nimport ${superServiceImplClassPackage};\r\nimport org.springframework.stereotype.Service;\r\n\r\n/**\r\n * <p>\r\n * $!{table.comment} 服务实现类\r\n * </p>\r\n *\r\n * @author ${author}\r\n * @since ${date}\r\n */\r\n@Service\r\n#if(${kotlin})\r\nopen class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {\r\n\r\n}\r\n#else\r\npublic class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {\r\n\r\n}\r\n#end\r\n', '/others/serviceImpl.java.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (340, 'menu.sql.ftl', 1, 'FreeMarker', '<#assign dbTime = \"now()\">\r\n<#if dbType==\"SQLServer\">\r\n    <#assign dbTime = \"getDate()\">\r\n</#if>\r\n\r\n-- 初始化菜单\r\nINSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES (1, \'${tableComment!}\', \'${moduleName}/${functionName}/index\', NULL, 0, 0, \'icon-menu\', 0, 0, 0, 10000, ${dbTime}, 10000, ${dbTime});\r\n\r\nINSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT max(id) from sys_menu where name = \'${tableComment!}\'), \'查看\', \'\', \'${moduleName}:${functionName}:page\', 1, 0, \'\', 0, 0, 0, 10000, ${dbTime}, 10000, ${dbTime});\r\nINSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT max(id) from sys_menu where name = \'${tableComment!}\'), \'新增\', \'\', \'${moduleName}:${functionName}:save\', 1, 0, \'\', 1, 0, 0, 10000, ${dbTime}, 10000, ${dbTime});\r\nINSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT max(id) from sys_menu where name = \'${tableComment!}\'), \'修改\', \'\', \'${moduleName}:${functionName}:update,${moduleName}:${functionName}:info\', 1, 0, \'\', 2, 0, 0, 10000, ${dbTime}, 10000, ${dbTime});\r\nINSERT INTO sys_menu (pid, name, url, authority, type, open_style, icon, sort, version, deleted, creator, create_time, updater, update_time) VALUES ((SELECT max(id) from sys_menu where name = \'${tableComment!}\'), \'删除\', \'\', \'${moduleName}:${functionName}:delete\', 1, 0, \'\', 3, 0, 0, 10000, ${dbTime}, 10000, ${dbTime});\r\n', '/sql/menu.sql.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (341, 'add-or-update.vue.ftl', 1, 'FreeMarker', '<template>\r\n    <el-dialog v-model=\"visible\" :title=\"!dataForm.id ? \'新增\' : \'修改\'\" :close-on-click-modal=\"false\">\r\n        <el-form ref=\"dataFormRef\" :model=\"dataForm\" :rules=\"dataRules\" label-width=\"100px\"\r\n                 @keyup.enter=\"submitHandle()\">\r\n            <#list formList as field>\r\n                <#if field.formType == \'text\'>\r\n                    <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                        <el-input v-model=\"dataForm.${field.attrName}\" placeholder=\"${field.fieldComment!}\"></el-input>\r\n                    </el-form-item>\r\n                <#elseif field.formType == \'textarea\'>\r\n                    <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                        <el-input type=\"textarea\" v-model=\"dataForm.${field.attrName}\"></el-input>\r\n                    </el-form-item>\r\n                <#elseif field.formType == \'editor\'>\r\n                    <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                        <el-input type=\"textarea\" v-model=\"dataForm.${field.attrName}\"></el-input>\r\n                    </el-form-item>\r\n                <#elseif field.formType == \'select\'>\r\n                    <#if field.formDict??>\r\n                        <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                            <fast-select v-model=\"dataForm.${field.attrName}\" dict-type=\"${field.formDict}\"\r\n                                         placeholder=\"${field.fieldComment!}\"></fast-select>\r\n                        </el-form-item>\r\n                    <#else>\r\n                        <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                            <el-select v-model=\"dataForm.${field.attrName}\" placeholder=\"请选择\">\r\n                                <el-option label=\"请选择\" value=\"0\"></el-option>\r\n                            </el-select>\r\n                        </el-form-item>\r\n                    </#if>\r\n                <#elseif field.formType == \'radio\'>\r\n                    <#if field.formDict??>\r\n                        <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                            <fast-radio-group v-model=\"dataForm.${field.attrName}\"\r\n                                              dict-type=\"${field.formDict}\"></fast-radio-group>\r\n                        </el-form-item>\r\n                    <#else>\r\n                        <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                            <el-radio-group v-model=\"dataForm.${field.attrName}\">\r\n                                <el-radio :label=\"0\">启用</el-radio>\r\n                                <el-radio :label=\"1\">禁用</el-radio>\r\n                            </el-radio-group>\r\n                        </el-form-item>\r\n                    </#if>\r\n                <#elseif field.formType == \'checkbox\'>\r\n                    <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                        <el-checkbox-group v-model=\"dataForm.${field.attrName}\">\r\n                            <el-checkbox label=\"启用\" name=\"type\"></el-checkbox>\r\n                            <el-checkbox label=\"禁用\" name=\"type\"></el-checkbox>\r\n                        </el-checkbox-group>\r\n                    </el-form-item>\r\n                <#elseif field.formType == \'date\'>\r\n                    <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                        <el-date-picker type=\"date\" placeholder=\"${field.fieldComment!}\"\r\n                                        v-model=\"dataForm.${field.attrName}\"></el-date-picker>\r\n                    </el-form-item>\r\n                <#elseif field.formType == \'datetime\'>\r\n                    <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                        <el-date-picker type=\"datetime\" placeholder=\"${field.fieldComment!}\"\r\n                                        v-model=\"dataForm.${field.attrName}\"></el-date-picker>\r\n                    </el-form-item>\r\n                <#else>\r\n                    <el-form-item label=\"${field.fieldComment!}\" prop=\"${field.attrName}\">\r\n                        <el-input v-model=\"dataForm.${field.attrName}\" placeholder=\"${field.fieldComment!}\"></el-input>\r\n                    </el-form-item>\r\n                </#if>\r\n            </#list>\r\n        </el-form>\r\n        <template #footer>\r\n            <el-button @click=\"visible = false\">取消</el-button>\r\n            <el-button type=\"primary\" @click=\"submitHandle()\">确定</el-button>\r\n        </template>\r\n    </el-dialog>\r\n</template>\r\n\r\n<script setup lang=\"ts\">\r\n    import {reactive, ref} from \'vue\'\r\n    import {ElMessage} from \'element-plus/es\'\r\n\r\n    const emit = defineEmits([\'refreshDataList\'])\r\n\r\n    const visible = ref(false)\r\n    const dataFormRef = ref()\r\n\r\n    const dataForm = reactive({\r\n        <#list fieldList as field>\r\n        ${field.attrName}: \'\'<#sep>,\r\n        </#list>\r\n    })\r\n\r\n    const init = (id\r\n    ? : number\r\n    ) =>\r\n    {\r\n        visible.value = true\r\n        dataForm.id = \'\'\r\n\r\n        // 重置表单数据\r\n        if (dataFormRef.value) {\r\n            dataFormRef.value.resetFields()\r\n        }\r\n\r\n        if (id) {\r\n            get${FunctionName}(id)\r\n        }\r\n    }\r\n\r\n    const get${FunctionName} = (id: number) => {\r\n        use${FunctionName}Api(id).then(res => {\r\n            Object.assign(dataForm, res.data)\r\n        })\r\n    }\r\n\r\n    const dataRules = ref({\r\n        <#list formList as field>\r\n        <#if field.formRequired>\r\n        ${field.attrName}: [{required: true, message: \'必填项不能为空\', trigger: \'blur\'}]<#sep>,\r\n        </#if>\r\n        </#list>\r\n    })\r\n\r\n    // 表单提交\r\n    const submitHandle = () => {\r\n        dataFormRef.value.validate((valid: boolean) => {\r\n            if (!valid) {\r\n                return false\r\n            }\r\n\r\n            use${FunctionName}SubmitApi(dataForm).then(() => {\r\n                ElMessage.success({\r\n                    message: \'操作成功\',\r\n                    duration: 500,\r\n                    onClose: () => {\r\n                        visible.value = false\r\n                        emit(\'refreshDataList\')\r\n                    }\r\n                })\r\n            })\r\n        })\r\n    }\r\n\r\n    defineExpose({\r\n        init\r\n    })\r\n</script>\r\n', '/vue/add-or-update.vue.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (342, 'api.ts.ftl', 1, 'FreeMarker', 'import service from \'@/utils/request\'\r\n\r\nexport const use${FunctionName}Api = (id: number) => {\r\nreturn service.get(\'/${moduleName}/${functionName}/\' + id)\r\n}\r\n\r\nexport const use${FunctionName}SubmitApi = (dataForm: any) => {\r\nif (dataForm.id) {\r\nreturn service.put(\'/${moduleName}/${functionName}\', dataForm)\r\n} else {\r\nreturn service.post(\'/${moduleName}/${functionName}\', dataForm)\r\n}\r\n}\r\n', '/vue/api.ts.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (343, 'index.vue.ftl', 1, 'FreeMarker', '<template>\r\n    <el-card>\r\n        <el-form :inline=\"true\" :model=\"state.queryForm\" @keyup.enter=\"getDataList()\">\r\n            <#list queryList as field>\r\n                <el-form-item>\r\n                    <#if field.formType == \'text\' || field.formType == \'textarea\' || field.formType == \'editor\'>\r\n                        <el-input v-model=\"state.queryForm.${field.attrName}\"\r\n                                  placeholder=\"${field.fieldComment!}\"></el-input>\r\n                    <#elseif field.queryFormType == \'select\'>\r\n                        <#if field.formDict??>\r\n                            <fast-select v-model=\"state.queryForm.${field.attrName}\" dict-type=\"${field.formDict}\"\r\n                                         placeholder=\"${field.fieldComment!}\" clearable></fast-select>\r\n                        <#else>\r\n                            <el-select v-model=\"state.queryForm.${field.attrName}\" placeholder=\"${field.fieldComment!}\">\r\n                                <el-option label=\"选择\" value=\"0\"></el-option>\r\n                            </el-select>\r\n                        </#if>\r\n                    <#elseif field.queryFormType == \'radio\'>\r\n                        <#if field.formDict??>\r\n                            <fast-radio-group v-model=\"state.queryForm.${field.attrName}\"\r\n                                              dict-type=\"${field.formDict}\"></fast-radio-group>\r\n                        <#else>\r\n                            <el-radio-group v-model=\"state.queryForm.${field.attrName}\">\r\n                                <el-radio :label=\"0\">单选</el-radio>\r\n                            </el-radio-group>\r\n                        </#if>\r\n                    <#elseif field.queryFormType == \'date\'>\r\n                        <el-date-picker\r\n                            v-model=\"daterange\"\r\n                            type=\"daterange\"\r\n                            value-format=\"yyyy-MM-dd\">\r\n                        </el-date-picker>\r\n                    <#elseif field.queryFormType == \'datetime\'>\r\n                        <el-date-picker\r\n                            v-model=\"datetimerange\"\r\n                            type=\"datetimerange\"\r\n                            value-format=\"yyyy-MM-dd HH:mm:ss\">\r\n                        </el-date-picker>\r\n                    <#else>\r\n                        <el-input v-model=\"state.queryForm.${field.attrName}\"\r\n                                  placeholder=\"${field.fieldComment!}\"></el-input>\r\n                    </#if>\r\n                </el-form-item>\r\n            </#list>\r\n            <el-form-item>\r\n                <el-button @click=\"getDataList()\">查询</el-button>\r\n            </el-form-item>\r\n            <el-form-item>\r\n                <el-button v-auth=\"\'${moduleName}:${functionName}:save\'\" type=\"primary\" @click=\"addOrUpdateHandle()\">\r\n                    新增\r\n                </el-button>\r\n            </el-form-item>\r\n            <el-form-item>\r\n                <el-button v-auth=\"\'${moduleName}:${functionName}:delete\'\" type=\"danger\" @click=\"deleteBatchHandle()\">\r\n                    删除\r\n                </el-button>\r\n            </el-form-item>\r\n        </el-form>\r\n        <el-table v-loading=\"state.dataListLoading\" :data=\"state.dataList\" border style=\"width: 100%\"\r\n                  @selection-change=\"selectionChangeHandle\">\r\n            <el-table-column type=\"selection\" header-align=\"center\" align=\"center\" width=\"50\"></el-table-column>\r\n            <#list gridList as field>\r\n                <#if field.formDict??>\r\n                    <fast-table-column prop=\"${field.attrName}\" label=\"${field.fieldComment!}\"\r\n                                       dict-type=\"${field.formDict}\"></fast-table-column>\r\n                <#else>\r\n                    <el-table-column prop=\"${field.attrName}\" label=\"${field.fieldComment!}\" header-align=\"center\"\r\n                                     align=\"center\"></el-table-column>\r\n                </#if>\r\n            </#list>\r\n            <el-table-column label=\"操作\" fixed=\"right\" header-align=\"center\" align=\"center\" width=\"150\">\r\n                <template #default=\"scope\">\r\n                    <el-button v-auth=\"\'${moduleName}:${functionName}:update\'\" type=\"primary\" link\r\n                               @click=\"addOrUpdateHandle(scope.row.id)\">修改\r\n                    </el-button>\r\n                    <el-button v-auth=\"\'${moduleName}:${functionName}:delete\'\" type=\"primary\" link\r\n                               @click=\"deleteBatchHandle(scope.row.id)\">删除\r\n                    </el-button>\r\n                </template>\r\n            </el-table-column>\r\n        </el-table>\r\n        <el-pagination\r\n            :current-page=\"state.page\"\r\n            :page-sizes=\"state.pageSizes\"\r\n            :page-size=\"state.limit\"\r\n            :total=\"state.total\"\r\n            layout=\"total, sizes, prev, pager, next, jumper\"\r\n            @size-change=\"sizeChangeHandle\"\r\n            @current-change=\"currentChangeHandle\"\r\n        >\r\n        </el-pagination>\r\n\r\n        <!-- 弹窗, 新增 / 修改 -->\r\n        <add-or-update ref=\"addOrUpdateRef\" @refreshDataList=\"getDataList\"></add-or-update>\r\n    </el-card>\r\n</template>\r\n\r\n<script setup lang=\"ts\" name=\"${ModuleName}${FunctionName}Index\">\r\n    import {useCrud} from \'@/hooks\'\r\n    import {reactive, ref} from \'vue\'\r\n    import {IHooksOptions} from \'@/hooks/interface\'\r\n\r\n    const state: IHooksOptions = reactive({\r\n        dataListUrl: \'/${moduleName}/${functionName}/page\',\r\n        deleteUrl: \'/${moduleName}/${functionName}\',\r\n        queryForm: {\r\n            <#list queryList as field>\r\n            <#if field.formType == \'date\'>\r\n            startDate: \'\',\r\n            endDate: \'\'<#sep>, </#sep>\r\n            <#elseif field.formType == \'datetime\'>\r\n            startDateTime: \'\',\r\n            endDateTime: \'\'<#sep>, </#sep>\r\n            <#else>\r\n            ${field.attrName}: \'\'<#sep>, </#sep>\r\n            </#if>\r\n            </#list>\r\n        }\r\n    })\r\n\r\n    const addOrUpdateRef = ref()\r\n    const addOrUpdateHandle = (id\r\n    ? : number\r\n    ) =>\r\n    {\r\n        addOrUpdateRef.value.init(id)\r\n    }\r\n\r\n    const {\r\n        getDataList,\r\n        selectionChangeHandle,\r\n        sizeChangeHandle,\r\n        currentChangeHandle,\r\n        deleteBatchHandle\r\n    } = useCrud(state)\r\n</script>\r\n', '/vue/index.vue.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (344, 'Dao.xml.ftl', 1, 'FreeMarker', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\r\n<mapper namespace=\"${package}.${moduleName}.dao.${ClassName}Dao\">\r\n\r\n    <resultMap type=\"${package}.${moduleName}.entity.${ClassName}Entity\" id=\"${className}Map\">\r\n        <#list fieldList as field>\r\n        <result property=\"${field.attrName}\" column=\"${field.fieldName}\"/>\r\n        </#list>\r\n    </resultMap>\r\n\r\n</mapper>\r\n', '/xml/Dao.xml.ftl', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (345, 'ApiResponse.vm', 1, 'Velocity', 'package ${package.Entity};\r\n\r\n#foreach($pkg in ${table.importPackages})\r\nimport ${pkg};\r\n#end\r\n#if(${springdoc})\r\nimport io.swagger.v3.oas.annotations.media.Schema;\r\n#elseif(${swagger})\r\nimport io.swagger.annotations.ApiModel;\r\nimport io.swagger.annotations.ApiModelProperty;\r\n#end\r\n#if(${entityLombokModel})\r\nimport lombok.Getter;\r\nimport lombok.Setter;\r\n    #if(${chainModel})\r\n    import lombok.experimental.Accessors;\r\n    #end\r\n#end\r\n\r\n/**\r\n* <p>\r\n    * $!{table.comment}\r\n    * </p>\r\n*\r\n* @author ${author}\r\n* @since ${date}\r\n*/\r\n#if(${entityLombokModel})\r\n@Getter\r\n@Setter\r\n    #if(${chainModel})\r\n    @Accessors(chain = true)\r\n    #end\r\n#end\r\n#if(${table.convert})\r\n@TableName(\"${schemaName}${table.name}\")\r\n#end\r\n#if(${springdoc})\r\n@Schema(name = \"${entity}\", description = \"$!{table.comment}\")\r\n#elseif(${swagger})\r\n@ApiModel(value = \"${entity}对象\", description = \"$!{table.comment}\")\r\n#end\r\n#if(${superEntityClass})\r\npublic class ${entity} extends ${superEntityClass}#if(${activeRecord})<${entity}>#end {\r\n#elseif(${activeRecord})\r\npublic class ${entity} extends Model<${entity}> {\r\n#elseif(${entitySerialVersionUID})\r\npublic class ${entity} implements Serializable {\r\n#else\r\npublic class ${entity} {\r\n#end\r\n#if(${entitySerialVersionUID})\r\n\r\nprivate static final long serialVersionUID = 1L;\r\n#end\r\n## ----------  BEGIN 字段循环遍历  ----------\r\n#foreach($field in ${table.columns})\r\n\r\n    #if(${field.keyFlag})\r\n        #set($keyPropertyName=${field.propertyName})\r\n    #end\r\n    #if(\"$!field.comment\" != \"\")\r\n        #if(${springdoc})\r\n        @Schema(description = \"${field.comment}\")\r\n        #elseif(${swagger})\r\n        @ApiModelProperty(\"${field.comment}\")\r\n        #else\r\n        /**\r\n        * ${field.comment}\r\n        */\r\n        #end\r\n    #end\r\n    #if(${field.keyFlag})\r\n        ## 主键\r\n        #if(${field.keyIdentityFlag})\r\n        @TableId(value = \"${field.annotationColumnName}\", type = IdType.AUTO)\r\n        #elseif(!$null.isNull(${idType}) && \"$!idType\" != \"\")\r\n        @TableId(value = \"${field.annotationColumnName}\", type = IdType.${idType})\r\n        #elseif(${field.convert})\r\n        @TableId(\"${field.annotationColumnName}\")\r\n        #end\r\n        ## 普通字段\r\n    #elseif(${field.fill})\r\n        ## -----   存在字段填充设置   -----\r\n        #if(${field.convert})\r\n        @TableField(value = \"${field.annotationColumnName}\", fill = FieldFill.${field.fill})\r\n        #else\r\n        @TableField(fill = FieldFill.${field.fill})\r\n        #end\r\n    #elseif(${field.convert})\r\n    @TableField(\"${field.annotationColumnName}\")\r\n    #end\r\n    ## 乐观锁注解\r\n    #if(${field.versionField})\r\n    @Version\r\n    #end\r\n    ## 逻辑删除注解\r\n    #if(${field.logicDeleteField})\r\n    @TableLogic\r\n    #end\r\nprivate ${field.propertyType} ${field.propertyName};\r\n#end\r\n## ----------  END 字段循环遍历  ----------\r\n#if(!${entityLombokModel})\r\n    #foreach($field in ${table.fields})\r\n        #if(${field.propertyType.equals(\"boolean\")})\r\n            #set($getprefix=\"is\")\r\n        #else\r\n            #set($getprefix=\"get\")\r\n        #end\r\n\r\n    public ${field.propertyType} ${getprefix}${field.capitalName}() {\r\n    return ${field.propertyName};\r\n    }\r\n\r\n        #if(${chainModel})\r\n        public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n        #else\r\n        public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {\r\n        #end\r\n    this.${field.propertyName} = ${field.propertyName};\r\n        #if(${chainModel})\r\n        return this;\r\n        #end\r\n    }\r\n    #end\r\n    ## --foreach end---\r\n#end\r\n## --end of #if(!${entityLombokModel})--\r\n#if(${entityColumnConstant})\r\n    #foreach($field in ${table.fields})\r\n\r\n    public static final String ${field.name.toUpperCase()} = \"${field.name}\";\r\n    #end\r\n#end\r\n#if(${activeRecord})\r\n\r\n@Override\r\npublic Serializable pkVal() {\r\n    #if(${keyPropertyName})\r\n    return this.${keyPropertyName};\r\n    #else\r\n    return null;\r\n    #end\r\n}\r\n#end\r\n#if(!${entityLombokModel})\r\n\r\n@Override\r\npublic String toString() {\r\nreturn \"${entity}{\" +\r\n    #foreach($field in ${table.fields})\r\n        #if($!{foreach.index}==0)\r\n        \"${field.propertyName} = \" + ${field.propertyName} +\r\n        #else\r\n        \", ${field.propertyName} = \" + ${field.propertyName} +\r\n        #end\r\n    #end\r\n\"}\";\r\n}\r\n#end\r\n}\r\n', '/vm/ApiResponse.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');
INSERT INTO `template_info` VALUES (346, 'pojo-java.vm', 1, 'Velocity', 'package ${packageName};\n\n#foreach($importItem in ${importItems})\nimport ${importItem};\n#end\n', '/vm/pojo-java.vm', '', 0, 1, '2023-12-28 21:42:47', '2023-12-28 21:42:50');

-- ----------------------------
-- Table structure for template_param
-- ----------------------------
DROP TABLE IF EXISTS `template_param`;
CREATE TABLE `template_param`  (
  `id` int NOT NULL COMMENT '主键ID',
  `template_id` int NULL DEFAULT NULL COMMENT '模板ID',
  `key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数key, 一般为出现在模板中的变量名,单个模板内唯一',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数名',
  `default_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数值,默认参数值, 未提供该参数时使用此值',
  `data_type_id` bigint NULL DEFAULT NULL COMMENT '数据类型ID，关联data_type_item#id字段',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板参数元数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of template_param
-- ----------------------------

-- ----------------------------
-- Table structure for template_variable_info
-- ----------------------------
DROP TABLE IF EXISTS `template_variable_info`;
CREATE TABLE `template_variable_info`  (
  `id` int NOT NULL COMMENT '主键ID',
  `template_id` int NULL DEFAULT NULL COMMENT '模板ID',
  `key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数key, 一般为出现在模板中的变量名,单个模板内唯一',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数名',
  `default_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '参数值,默认参数值, 未提供该参数时使用此值',
  `data_type_id` bigint NULL DEFAULT NULL COMMENT '数据类型ID，关联data_type_item#id字段',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模板参数元数据表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of template_variable_info
-- ----------------------------

-- ----------------------------
-- Table structure for type_mapping
-- ----------------------------
DROP TABLE IF EXISTS `type_mapping`;
CREATE TABLE `type_mapping`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `java_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `json_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '类型映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of type_mapping
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;

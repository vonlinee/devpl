-- 删除数据库
drop database if exists `devpl`;
-- 新建数据库
create database `devpl` default character set utf8mb4;
-- 使用数据库
use `devpl`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for column_metadata
-- ----------------------------
DROP TABLE IF EXISTS `column_metadata`;
CREATE TABLE `column_metadata`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `table_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '所属表的ID',
  `table_cat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'table catalog (maybe null)',
  `table_schem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'table schema (maybe null)',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名称',
  `column_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `data_type` int NULL DEFAULT NULL COMMENT 'SQL type from java.sql.Type',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据源独立的类型名称, for a UDT the type name is fully qualified',
  `column_size` int NULL DEFAULT NULL COMMENT '列大小,有符号数长度会减少1，比如bigint(20)，此时columnSize=19',
  `buffer_length` int NULL DEFAULT NULL COMMENT '暂未使用(jdbc specification4.3)',
  `decimal_digits` int NULL DEFAULT NULL COMMENT '小数位数',
  `num_prec_radix` int NULL DEFAULT NULL COMMENT 'NUM_PREC_RADIX int => Radix (typically either 10 or 2) (基数,即十进制或者二进制)',
  `nullable` int UNSIGNED NULL DEFAULT NULL COMMENT '是否允许NULL. 0 - Indicates that the column definitely allows NULL values. 1 - Indicates that the column definitely allows NULL values. 2 - Indicates that the nullability of columns is unknown.',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '该列的描述信息，可为null',
  `column_def` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '该列的默认值, 如果值被单引号引起来，则表示该值是字符串(maybe null)',
  `sql_data_type` int NULL DEFAULT NULL COMMENT 'unused',
  `sql_datetime_sub` int NULL DEFAULT NULL COMMENT 'unused',
  `char_octet_length` int NULL DEFAULT NULL COMMENT '字符类型的最大字节数 CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column',
  `ordinal_position` int NULL DEFAULT NULL COMMENT '该列在表中的位置，开始为1',
  `is_nullable` tinyint UNSIGNED NULL DEFAULT NULL COMMENT 'ISO rules are used to determine the nullability for a column. YES --- if the column can include NULLs NO --- if the column cannot include NULLs empty string --- if the nullability for the column is unknown',
  `scope_catalog` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'catalog of table that is the scope of a reference attribute (null if DATA_TYPE is not REF)',
  `scope_schema` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'schema of table that is the scope of a reference attribute (null if the DATA_TYPE is not REF)',
  `scope_table` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'table name that this the scope of a reference attribute (null if the DATA_TYPE is not REF)',
  `source_data_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'source type of distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE is not DISTINCT or user-generated REF)',
  `is_autoincrement` tinyint UNSIGNED NULL DEFAULT NULL COMMENT 'Indicates whether this column is auto incremented YES --- if the column is auto incremented NO --- if the column is not auto incremented empty string --- if it cannot be determined whether the column is auto incremented',
  `is_generated` tinyint UNSIGNED NULL DEFAULT NULL COMMENT 'Indicates whether this is a generated column YES --- if this a generated column NO --- if this not a generated column empty string --- if it cannot be determined whether this is a generated column',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据库表列信息记录表（对应JDBC的ColumnMetadata）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for custom_directive
-- ----------------------------
DROP TABLE IF EXISTS `custom_directive`;
CREATE TABLE `custom_directive`  (
  `directive_id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '指令ID',
  `directive_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '指令名称',
  `source_code` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '实现部分代码',
  `status` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`directive_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '自定义指令记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_type_category
-- ----------------------------
DROP TABLE IF EXISTS `data_type_category`;
CREATE TABLE `data_type_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组ID',
  `category_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据类型分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for data_type_group
-- ----------------------------
DROP TABLE IF EXISTS `data_type_group`;
CREATE TABLE `data_type_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组ID',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分组名称',
  `internal` tinyint NULL DEFAULT 0 COMMENT '是否内置类型分组，内置类型分组不可更改',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据类型分组' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_type_item
-- ----------------------------
DROP TABLE IF EXISTS `data_type_item`;
CREATE TABLE `data_type_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型分组ID',
  `category_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '分类ID，为空则未进行分类',
  `type_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型ID，全局唯一',
  `type_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型Key，单个类型组内唯一',
  `full_type_key` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型Key，限定名称',
  `locale_type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称，中文名称',
  `value_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '该数据类型的值类型',
  `min_length` double NULL DEFAULT NULL COMMENT '最小长度',
  `max_length` double NULL DEFAULT NULL COMMENT '最大长度',
  `default_value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型默认值',
  `precision` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '精度',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  `operation` tinyint(1) NULL DEFAULT -1 COMMENT '操作',
  `internal` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统内部定义，不可删除',
  `is_deleted` tinyint UNSIGNED NULL DEFAULT 0 COMMENT '是否删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unk_type`(`type_group_id` ASC, `type_key` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 355 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_type_mapping
-- ----------------------------
DROP TABLE IF EXISTS `data_type_mapping`;
CREATE TABLE `data_type_mapping`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` bigint NULL DEFAULT NULL COMMENT '数据类型映射分组ID',
  `type_id` bigint NULL DEFAULT NULL COMMENT '主数据类型ID',
  `type_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型Key',
  `another_type_id` bigint NULL DEFAULT NULL COMMENT '映射数据类型id',
  `another_type_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '映射数据类型key',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据类型映射关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for data_type_mapping_group
-- ----------------------------
DROP TABLE IF EXISTS `data_type_mapping_group`;
CREATE TABLE `data_type_mapping_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型映射分组名称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据类型映射关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for database_backup_history
-- ----------------------------
DROP TABLE IF EXISTS `database_backup_history`;
CREATE TABLE `database_backup_history`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `save_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保存位置',
  `backup_time` datetime NULL DEFAULT NULL COMMENT '备份时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 233 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据库备份历史记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for database_metadata
-- ----------------------------
DROP TABLE IF EXISTS `database_metadata`;
CREATE TABLE `database_metadata`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `database_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保存位置',
  `database_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '备份时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据库源数据记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for field_constraint
-- ----------------------------
DROP TABLE IF EXISTS `field_constraint`;
CREATE TABLE `field_constraint`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '约束类型(PK主键约束，FK外键约束，UK唯一约束)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段约束记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for field_group
-- ----------------------------
DROP TABLE IF EXISTS `field_group`;
CREATE TABLE `field_group`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '组ID',
  `parent_id` bigint NULL DEFAULT NULL COMMENT '父组ID',
  `group_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组名称',
  `type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 102 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段组信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for field_info
-- ----------------------------
DROP TABLE IF EXISTS `field_info`;
CREATE TABLE `field_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `parent_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '父级字段主键ID',
  `field_key` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字段ID',
  `field_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名',
  `type_group_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型分组ID',
  `data_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型',
  `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '注释信息',
  `description` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述信息',
  `literal_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字面值',
  `default_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '默认值',
  `optional` tinyint(1) NULL DEFAULT 1 COMMENT '是否可选',
  `temporary` tinyint(1) NULL DEFAULT 0 COMMENT '是否是临时字段(允许重名)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 175 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for gen_field_type
-- ----------------------------
DROP TABLE IF EXISTS `gen_field_type`;
CREATE TABLE `gen_field_type`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `mysql_sql_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'MySQL SQL数据类型',
  `column_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `attr_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性包名',
  `json_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JSON数据类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `column_type`(`column_type` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段类型管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for group_field
-- ----------------------------
DROP TABLE IF EXISTS `group_field`;
CREATE TABLE `group_field`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` bigint NOT NULL COMMENT '组ID',
  `field_id` bigint NOT NULL COMMENT '字段ID',
  `order_num` int NULL DEFAULT 0 COMMENT '排序号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 160 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字段和组关联信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mapped_statement_item
-- ----------------------------
DROP TABLE IF EXISTS `mapped_statement_item`;
CREATE TABLE `mapped_statement_item`  (
  `id` varchar(26) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `project_root` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目根路径',
  `belong_file` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '所属文件路径',
  `statement_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语句ID',
  `statement_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语句类型',
  `namespace` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '命名空间',
  `param_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数类型',
  `result_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '结果类型',
  `statement` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '语句内容',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'MyBatis Mapper语句记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mapped_statement_param_mapping
-- ----------------------------
DROP TABLE IF EXISTS `mapped_statement_param_mapping`;
CREATE TABLE `mapped_statement_param_mapping`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `mapped_statement_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'Mapper语句ID',
  `param_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数key',
  `param_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 60 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'MyBatis Mapper标签参数映射' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mock_constraint
-- ----------------------------
DROP TABLE IF EXISTS `mock_constraint`;
CREATE TABLE `mock_constraint`  (
  `id` bigint NULL DEFAULT NULL COMMENT '主键ID',
  `constraint_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '约束类型',
  `expression` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '约束表达式',
  `table` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `column` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `related_table` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `related_column` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据模拟约束' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for mock_field
-- ----------------------------
DROP TABLE IF EXISTS `mock_field`;
CREATE TABLE `mock_field`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '模型ID',
  `field_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段key',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据模拟字段表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mock_field_group
-- ----------------------------
DROP TABLE IF EXISTS `mock_field_group`;
CREATE TABLE `mock_field_group`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `group_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '模型ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据模拟字段组信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for model_field
-- ----------------------------
DROP TABLE IF EXISTS `model_field`;
CREATE TABLE `model_field`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `model_id` bigint UNSIGNED NULL DEFAULT NULL COMMENT '模型ID',
  `field_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 66 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模型字段关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for model_info
-- ----------------------------
DROP TABLE IF EXISTS `model_info`;
CREATE TABLE `model_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基类包名',
  `code` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基类编码',
  `fields` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '基类字段，多个用英文逗号分隔',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '领域模型信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for project_info
-- ----------------------------
DROP TABLE IF EXISTS `project_info`;
CREATE TABLE `project_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目名，如果是模块则为模块名',
  `project_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目标识',
  `project_package` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目包名',
  `project_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目路径',
  `modify_project_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更项目名',
  `modify_project_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更标识',
  `modify_project_package` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更包名',
  `exclusions` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '排除文件',
  `modify_suffix` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更文件',
  `modify_tmp_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '变更临时路径',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '项目状态',
  `version` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本',
  `backend_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后端项目路径',
  `frontend_path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端项目路径',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `build_tool` tinyint(1) NULL DEFAULT NULL COMMENT '构建工具 1-Maven，2-Gradle',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 95 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '项目信息' ROW_FORMAT = DYNAMIC;

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
-- Table structure for rdbms_connection_info
-- ----------------------------
DROP TABLE IF EXISTS `rdbms_connection_info`;
CREATE TABLE `rdbms_connection_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `db_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `port` int NULL DEFAULT 3306 COMMENT '端口号',
  `driver_class_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驱动类名',
  `db_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `connection_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '连接名',
  `connection_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'URL',
  `username` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `driver_props` json NULL COMMENT '驱动属性',
  `driver_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '驱动类型',
  `is_deleted` tinyint(1) NULL DEFAULT NULL COMMENT '是否逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据源连接信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for table_file_generation
-- ----------------------------
DROP TABLE IF EXISTS `table_file_generation`;
CREATE TABLE `table_file_generation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `table_id` bigint NULL DEFAULT NULL COMMENT '表ID',
  `generation_id` bigint NULL DEFAULT NULL COMMENT '文件生成ID',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `save_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保存路径',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1133 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '表文件生成记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for table_generation
-- ----------------------------
DROP TABLE IF EXISTS `table_generation`;
CREATE TABLE `table_generation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `datasource_id` bigint NULL DEFAULT NULL COMMENT '数据源ID',
  `database_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `table_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名',
  `class_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类名',
  `table_comment` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '说明',
  `author` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '作者',
  `email` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目包名',
  `version` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '项目版本号',
  `generator_type` tinyint NULL DEFAULT NULL COMMENT '生成方式  0：zip压缩包   1：自定义目录',
  `backend_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '后端生成路径',
  `frontend_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '前端生成路径',
  `module_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块名',
  `function_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '功能名',
  `form_layout` tinyint NULL DEFAULT NULL COMMENT '表单布局  1：一列   2：两列',
  `baseclass_id` bigint NULL DEFAULT NULL COMMENT '基类ID',
  `template_arguments` json NULL COMMENT '模板参数',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否逻辑删除',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `table_name`(`table_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 160 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for table_generation_field
-- ----------------------------
DROP TABLE IF EXISTS `table_generation_field`;
CREATE TABLE `table_generation_field`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `table_id` bigint NULL DEFAULT NULL COMMENT '表ID',
  `field_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `field_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段类型',
  `data_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型名称，简写形式',
  `full_data_type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型名称，限定名称',
  `field_comment` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段说明',
  `attr_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性名',
  `attr_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性类型',
  `package_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '属性包名',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `auto_fill` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自动填充  DEFAULT、INSERT、UPDATE、INSERT_UPDATE',
  `primary_key` tinyint NULL DEFAULT NULL COMMENT '主键 0：否  1：是',
  `base_field` tinyint NULL DEFAULT NULL COMMENT '基类字段 0：否  1：是',
  `form_item` tinyint NULL DEFAULT NULL COMMENT '表单项 0：否  1：是',
  `form_required` tinyint NULL DEFAULT NULL COMMENT '表单必填 0：否  1：是',
  `form_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单类型',
  `form_dict` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单字典类型',
  `form_validator` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表单效验',
  `grid_item` tinyint NULL DEFAULT NULL COMMENT '列表项 0：否  1：是',
  `grid_sort` tinyint NULL DEFAULT NULL COMMENT '列表排序 0：否  1：是',
  `query_item` tinyint NULL DEFAULT NULL COMMENT '查询项 0：否  1：是',
  `query_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询方式',
  `query_form_type` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询表单类型',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2376 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成表字段' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for table_metadata
-- ----------------------------
DROP TABLE IF EXISTS `table_metadata`;
CREATE TABLE `table_metadata`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `table_cat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表catalog值，可为null',
  `table_schem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表schema值，可为null',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表名称',
  `table_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表类型. 常见类型包括\"TABLE\", \"VIEW\", \"SYSTEM TABLE\", \"GLOBAL TEMPORARY\", \"LOCAL TEMPORARY\", \"ALIAS\", \"SYNONYM\".',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '该表的描述文本',
  `type_cat` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'the types catalog (maybe null)',
  `type_schem` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'the types schema (maybe null)',
  `type_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '类型名称',
  `self_referencing_col_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'name of the designated \"identifier\" column of a typed table (may be null)',
  `ref_generation` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'specifies how values in SELF_REFERENCING_COL_NAME are created. Values are \"SYSTEM\", \"USER\", \"DERIVED\". (may be null)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据库表信息记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for target_generation_file
-- ----------------------------
DROP TABLE IF EXISTS `target_generation_file`;
CREATE TABLE `target_generation_file`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `type_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件类型名称',
  `template_id` bigint NULL DEFAULT NULL COMMENT '模板ID',
  `file_name` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `save_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保存路径',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `builtin` tinyint(1) NULL DEFAULT 0 COMMENT '是否内置',
  `default_target` tinyint UNSIGNED NULL DEFAULT NULL COMMENT '是否默认在代码生成中生成此类型的文件',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '目标生成文件类型表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for template_argument
-- ----------------------------
DROP TABLE IF EXISTS `template_argument`;
CREATE TABLE `template_argument`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` bigint NULL DEFAULT NULL COMMENT '模板ID',
  `generation_id` bigint NULL DEFAULT NULL COMMENT '模板文件生成ID',
  `var_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数key, 一般为出现在模板中的变量名,单个模板内唯一',
  `value` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值',
  `data_type_id` bigint NULL DEFAULT NULL COMMENT '数据类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3575 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模板参数表，模板实际的参数值' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for template_file_generation
-- ----------------------------
DROP TABLE IF EXISTS `template_file_generation`;
CREATE TABLE `template_file_generation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `file_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `template_id` bigint NULL DEFAULT NULL COMMENT '模板ID',
  `template_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `config_table_id` bigint NULL DEFAULT NULL COMMENT '配置表主键ID',
  `config_table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '配置表名称',
  `fill_strategy` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据填充策略',
  `save_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '保存路径',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `builtin` tinyint(1) NULL DEFAULT 0 COMMENT '是否内置',
  `template_arguments` json NULL COMMENT '模板参数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1127 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '基于模板的文件生成记录表(每行对应一个生成的文件)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for template_info
-- ----------------------------
DROP TABLE IF EXISTS `template_info`;
CREATE TABLE `template_info`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板ID',
  `template_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `type` tinyint NULL DEFAULT NULL COMMENT '模板类型',
  `provider` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '技术提供方',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '字符串模板内容',
  `template_file_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板文件路径',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除状态',
  `is_internal` tinyint(1) NULL DEFAULT 0 COMMENT '是否系统内置模板，不可删除修改',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 49 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模板记录表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for template_param
-- ----------------------------
DROP TABLE IF EXISTS `template_param`;
CREATE TABLE `template_param`  (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` int NULL DEFAULT NULL COMMENT '模板ID,为空表示全局模板参数',
  `param_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数key, 一般为出现在模板中的变量名,单个模板内唯一',
  `param_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名',
  `param_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值,默认参数值, 未提供该参数时使用此值',
  `data_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据类型',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模板参数元数据表(实参)' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for template_variable_metadata
-- ----------------------------
DROP TABLE IF EXISTS `template_variable_metadata`;
CREATE TABLE `template_variable_metadata`  (
  `id` int NOT NULL COMMENT '主键ID',
  `template_id` int NULL DEFAULT NULL COMMENT '模板ID',
  `key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数key, 一般为出现在模板中的变量名,单个模板内唯一',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数名',
  `default_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '参数值,默认参数值, 未提供该参数时使用此值',
  `data_type_id` bigint NULL DEFAULT NULL COMMENT '数据类型ID，关联data_type_item#id字段',
  `remark` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除状态',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模板参数元数据表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

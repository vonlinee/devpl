-- ----------------------------
-- Table structure for connection_config
-- ----------------------------
DROP TABLE IF EXISTS `connection_config`;
CREATE TABLE `connection_config`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接名称',
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '主机地址，IP地址',
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '端口号',
  `db_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库类型',
  `db_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据库名称',
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `encoding` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '连接编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unk_conn_name`(`name`) USING BTREE,
  UNIQUE INDEX `unk_ip_port`(`host`, `port`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of connection_config
-- ----------------------------
INSERT INTO `connection_config` VALUES ('2f48daad-0066-4d77-8609-6f4f6d6e4c8c', '127.0.0.1-3307-MySQL8', '127.0.0.1', '3307', 'MySQL8', NULL, 'root', '123456', 'utf8');
INSERT INTO `connection_config` VALUES ('9c4c93ee-c0d6-4b10-b83d-2702756c6d11', '127.0.0.1-3306-MySQL5', '127.0.0.1', '3306', 'MySQL5', NULL, 'root', '123456', 'utf8');

-- ----------------------------
-- Table structure for connection_info
-- ----------------------------
DROP TABLE IF EXISTS `connection_info`;
CREATE TABLE `connection_info`  (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `port` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `db_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `host` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `db_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `encoding` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of connection_info
-- ----------------------------
INSERT INTO `connection_info` VALUES ('28e0988f-a052-4694-8e93-8607ceaeb586', '3306', NULL, 'localhost-3306', 'localhost', 'MYSQL5', 'root', '123456', 'utf8');

-- ----------------------------
-- Table structure for dbs
-- ----------------------------
DROP TABLE IF EXISTS `dbs`;
CREATE TABLE `dbs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `value` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '数据库信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dbs
-- ----------------------------
INSERT INTO `dbs` VALUES (2, 'mysql_learn', '{\"dbType\":\"MySQL5\",\"name\":\"mysql_learn\",\"host\":\"127.0.0.1\",\"port\":\"3306\",\"schema\":\"mysql_learn\",\"username\":\"root\",\"password\":\"123456\",\"encoding\":\"utf8\"}');
INSERT INTO `dbs` VALUES (3, 'ruoyi', '{\"dbType\":\"MySQL5\",\"name\":\"ruoyi\",\"host\":\"127.0.0.1\",\"port\":\"3306\",\"schema\":\"ruoyi\",\"username\":\"root\",\"password\":\"123456\",\"encoding\":\"utf8\"}');

-- ----------------------------
-- Table structure for generator_config
-- ----------------------------
DROP TABLE IF EXISTS `generator_config`;
CREATE TABLE `generator_config`  (
  `name` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成配置信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of generator_config
-- ----------------------------
INSERT INTO `generator_config` VALUES ('本地', '{\"name\":\"本地\",\"projectRootFolder\":\"D:\\\\Temp\",\"parentPackage\":null,\"entityPackageName\":\"entity\",\"entityPackageFolder\":\"src/main/java\",\"mapperPackageName\":\"mapper\",\"mapperFolder\":\"src/main/java\",\"mapperXmlPackage\":\"mapping\",\"mapperXmlFolder\":\"src/main/resources\"}');
INSERT INTO `generator_config` VALUES ('Lancoo', '{\"name\":\"Lancoo\",\"projectRootFolder\":\"D:\\\\Temp\",\"parentPackage\":\"com.lancoo.campusportrait\",\"entityPackageName\":\"entity\",\"entityPackageFolder\":\"src/main/java\",\"mapperPackageName\":\"mapper\",\"mapperFolder\":\"src/main/java\",\"mapperXmlPackage\":\"mapping\",\"mapperXmlFolder\":\"src/main/resources\"}');

-- ----------------------------
-- Table structure for meta_fields
-- ----------------------------
DROP TABLE IF EXISTS `meta_fields`;
CREATE TABLE `meta_fields`  (
  `field_id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键',
  `field_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段名称',
  `field_description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字段描述信息',
  PRIMARY KEY (`field_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for template_info
-- ----------------------------
DROP TABLE IF EXISTS `template_info`;
CREATE TABLE `template_info`  (
  `template_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '模板ID',
  `template_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板名称',
  `template_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模板所在路径',
  `builtin` tinyint(1) NULL DEFAULT NULL COMMENT '是否内置，内置模板不可更改',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`template_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '模板信息表' ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;

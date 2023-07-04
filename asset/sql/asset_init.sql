DROP TABLE IF EXISTS `asset_template`;
CREATE TABLE `asset_template`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '名称',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '名称',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '软删除标识，1删除；0未删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) NULL DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_parent_id`(`parent_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '资产模板表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `asset_parameter`;
CREATE TABLE `asset_parameter`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '名称',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '模板id',
  `type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '1:字符串；2:数字；3:文件；4:资产；5:公司；6:角色；7:巡检..',
  `required` tinyint(4) NOT NULL DEFAULT 0 COMMENT '1：必填；0：非必填',
  `description` varchar(255) NULL DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_template_id`(`template_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '资产形参表' ROW_FORMAT = DYNAMIC;

DROP TABLE IF EXISTS `asset_instance`;
CREATE TABLE `asset_instance`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '名称',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '模板id',
  `rf_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '',
  `phy_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '软删除标识，1删除；0未删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) NULL DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '资产实例表' ROW_FORMAT = Dynamic;

DROP TABLE IF EXISTS `asset_argument`;
CREATE TABLE `asset_argument`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '主键',
  `instance_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '实例id',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '模板id',
  `parameter_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '形参id',
  `value` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '实参值',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_instance_id`(`instance_id`) USING BTREE,
  INDEX `index_template_id`(`template_id`) USING BTREE,
  INDEX `index_parameter_id`(`parameter_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '资产实参表' ROW_FORMAT = Dynamic;
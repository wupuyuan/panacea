/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.10.30
 Source Server Type    : MySQL
 Source Server Version : 50737
 Source Host           : 192.168.10.30:3306
 Source Schema         : wpy_qjt

 Target Server Type    : MySQL
 Target Server Version : 50737
 File Encoding         : 65001

 Date: 31/01/2023 16:28:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for wf_argument
-- ----------------------------
drop table IF EXISTS `wf_argument`;
create TABLE `wf_argument`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '主键',
  `instance_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '实例id',
  `instance_node_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '实例节点id',
  `argument_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '形参key',
  `argument_type` tinyint(4) NOT NULL DEFAULT 0 comment '冗余字段，同形参type',
  `argument_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '值',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_instance_id`(`instance_id`) USING BTREE,
  INDEX `index_instance_node_id`(`instance_node_id`) USING BTREE,
  INDEX `index_argument_key`(`argument_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin comment = '工作流实例-实参' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wf_instance
-- ----------------------------
drop table IF EXISTS `wf_instance`;
create TABLE `wf_instance`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL comment '名称',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '模板id',
  `instance_key` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '发起方业务key，用于防止重复提交',
  `status` tinyint(4) NOT NULL DEFAULT 0 comment '0:草稿；1:审批中；2:驳回；3:结束；-1:异常',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 comment '软删除标识，1删除；0未删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_template_id`(`template_id`) USING BTREE,
  INDEX `index_instance_key`(`instance_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin comment = '工作流实例表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wf_instance_line
-- ----------------------------
drop table IF EXISTS `wf_instance_line`;
create TABLE `wf_instance_line`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '主键',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '模板id',
  `template_line_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '模板链接id',
  `instance_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '实例id',
  `status` tinyint(4) NOT NULL DEFAULT 0 comment '0:待审核；1:通过；2:驳回；-1:异常',
  `prev_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '前序节点id',
  `next_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '后续节点id',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_instance_id`(`instance_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin comment = '工作流实例-链接表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wf_instance_node
-- ----------------------------
drop table IF EXISTS `wf_instance_node`;
create TABLE `wf_instance_node`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL comment '名称',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '模板id',
  `template_node_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '模板节点id',
  `instance_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '实例id',
  `node_type` tinyint(4) NOT NULL DEFAULT 0 comment '1:起点；2:终点；3:条件节点；4:会签节点；5:汇签节点；6:加签节点',
  `status` tinyint(4) NOT NULL DEFAULT 0 comment '0:待审核；1:通过；2:驳回；-1:异常',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 comment '软删除标识，1删除；0未删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_instance_id`(`instance_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin comment = '工作流实例-节点表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wf_parameter
-- ----------------------------
drop table IF EXISTS `wf_parameter`;
create TABLE `wf_parameter`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '主键',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '模板id',
  `template_node_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '模板节点id',
  `parameter_key` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '形参',
  `parameter_type` tinyint(4) NOT NULL DEFAULT 0 comment '1:string；2:number；3:文件；4:资产；5:公司；6角色；7:范围',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_template_id`(`template_id`) USING BTREE,
  INDEX `index_template_node_id`(`template_node_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin comment = '工作流模板-形参表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wf_template
-- ----------------------------
drop table IF EXISTS `wf_template`;
create TABLE `wf_template`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL comment '名称',
  `template_key` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '流程key，版本更新时，id会变化',
  `version` tinyint(4) NOT NULL DEFAULT 0 comment '版本号',
  `type` tinyint(4) NOT NULL DEFAULT 0 comment '1:申领；2:工单',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 comment '软删除标识，1删除；0未删除',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `create_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_person` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_key`(`template_key`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin comment = '工作流模板表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wf_template_line
-- ----------------------------
drop table IF EXISTS `wf_template_line`;
create TABLE `wf_template_line`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '主键',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '模板id',
  `prev_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '前序节点id',
  `next_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '后续节点id',
  `type` tinyint(4) NOT NULL DEFAULT 0 comment '1:true；-1:false；0：略',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '说明',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_template_id`(`template_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin comment = '工作流模板-链接表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for wf_template_node
-- ----------------------------
drop table IF EXISTS `wf_template_node`;
create TABLE `wf_template_node`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL comment '名称',
  `deleted` tinyint(4) NOT NULL DEFAULT 0 comment '软删除标识，1删除；0未删除',
  `template_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL comment '模板id',
  `node_type` tinyint(4) NOT NULL DEFAULT 0 comment '1:起点；2:终点；3:条件节点；4:会签节点；5:汇签节点；6:加签节点',
  `flow_type` tinyint(4) NOT NULL DEFAULT 0 comment '1:并行；2:串行',
  `expression` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '条件表达式,回调表达式',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '说明',
  `coordinate` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL comment '座标，前端展示使用',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_template_id`(`template_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin comment = '工作流模板-节点表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;

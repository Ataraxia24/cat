/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : cat

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2023-12-20 15:49:34
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for cat_admin
-- ----------------------------
DROP TABLE IF EXISTS `cat_admin`;
CREATE TABLE `cat_admin` (
  `admin_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `admin_username` varchar(50) NOT NULL,
  `admin_password` varchar(255) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `admin_username` (`admin_username`) USING BTREE,
  UNIQUE KEY `user_id` (`user_id`) USING BTREE,
  CONSTRAINT `cat_admin_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `cat_users` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1628355789457178628 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_admin
-- ----------------------------
INSERT INTO `cat_admin` VALUES ('1628355789457178627', '1627582436685377538', 'admin', '202cb962ac59075b964b07152d234b70');

-- ----------------------------
-- Table structure for cat_article
-- ----------------------------
DROP TABLE IF EXISTS `cat_article`;
CREATE TABLE `cat_article` (
  `article_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `publish_time` date NOT NULL,
  `article_thumb` int(255) DEFAULT '0',
  `article_collect` int(255) DEFAULT '0',
  PRIMARY KEY (`article_id`),
  UNIQUE KEY `cat_id` (`cat_id`),
  KEY `cat_article_ibfk_2` (`user_id`),
  CONSTRAINT `cat_article_ibfk_1` FOREIGN KEY (`cat_id`) REFERENCES `cat_message` (`cat_id`) ON DELETE CASCADE,
  CONSTRAINT `cat_article_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `cat_users` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1647165048718327810 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_article
-- ----------------------------
INSERT INTO `cat_article` VALUES ('1647164690528960514', '8335505705732177920', '1627582436685377538', '2023-04-15', '0', '0');
INSERT INTO `cat_article` VALUES ('1647165048718327809', '8335506063887990784', '1627582436685377538', '2023-04-15', '0', '0');

-- ----------------------------
-- Table structure for cat_comment
-- ----------------------------
DROP TABLE IF EXISTS `cat_comment`;
CREATE TABLE `cat_comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `article_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `other_user_id` bigint(20) NOT NULL,
  `comment_message` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_comment
-- ----------------------------

-- ----------------------------
-- Table structure for cat_image
-- ----------------------------
DROP TABLE IF EXISTS `cat_image`;
CREATE TABLE `cat_image` (
  `image_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_id` bigint(20) NOT NULL,
  `image_name` varchar(255) DEFAULT NULL,
  `image_remark` varchar(50) DEFAULT NULL,
  `image_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8335506125078691841 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_image
-- ----------------------------
INSERT INTO `cat_image` VALUES ('8334798293901541376', '8334798293855404032', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8334798297592528896', '8334798297575751680', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8334798300155248640', '8334798300134277120', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8334798302269177856', '8334798302252400640', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8334822757273468928', '8334822757231525888', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8334833036495978496', '8334833036433063936', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8335499445990359040', '8335499445893890048', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8335504582680543232', '8335504582554714112', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8335504901839290368', '8335504901747015680', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8335505705744760832', '8335505705732177920', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8335506063908962304', '8335506063887990784', null, null, ' ');
INSERT INTO `cat_image` VALUES ('8335506125078691840', '8335506125066108928', null, null, ' ');

-- ----------------------------
-- Table structure for cat_interaction
-- ----------------------------
DROP TABLE IF EXISTS `cat_interaction`;
CREATE TABLE `cat_interaction` (
  `interaction_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `interaction_message` varchar(255) NOT NULL,
  `publish_time` date NOT NULL,
  PRIMARY KEY (`interaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1647199392266702850 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_interaction
-- ----------------------------
INSERT INTO `cat_interaction` VALUES ('1647166682013851650', '1628305070960136193', '{\r\n    \"message\": \"哥哥好帅!\"\r\n}', '2023-04-15');
INSERT INTO `cat_interaction` VALUES ('1647167401722863618', '1628305070960136193', 'message=1233', '2023-04-15');
INSERT INTO `cat_interaction` VALUES ('1647169069424271361', '1628305070960136193', '{\r\n    \r\n   \"interactionMessage\" : \"123\"\r\n}', '2023-04-15');
INSERT INTO `cat_interaction` VALUES ('1647172639775215617', '1628305070960136193', '123', '2023-04-15');
INSERT INTO `cat_interaction` VALUES ('1647199392266702849', '1628305070960136193', '你好，*', '2023-04-15');

-- ----------------------------
-- Table structure for cat_medium
-- ----------------------------
DROP TABLE IF EXISTS `cat_medium`;
CREATE TABLE `cat_medium` (
  `cat_id` bigint(20) NOT NULL,
  `tag_id` bigint(20) NOT NULL,
  KEY `cat_medium_ibfk_1` (`cat_id`),
  KEY `cat_medium_ibfk_2` (`tag_id`),
  CONSTRAINT `cat_medium_ibfk_1` FOREIGN KEY (`cat_id`) REFERENCES `cat_message` (`cat_id`) ON DELETE CASCADE,
  CONSTRAINT `cat_medium_ibfk_2` FOREIGN KEY (`tag_id`) REFERENCES `cat_tag` (`tag_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_medium
-- ----------------------------
INSERT INTO `cat_medium` VALUES ('8334798293855404032', '1635979205605543937');
INSERT INTO `cat_medium` VALUES ('8334798293855404032', '1635979205605543938');
INSERT INTO `cat_medium` VALUES ('8334798297575751680', '1635979205605543937');
INSERT INTO `cat_medium` VALUES ('8334798297575751680', '1635979205605543938');
INSERT INTO `cat_medium` VALUES ('8334798300134277120', '1635979205605543937');
INSERT INTO `cat_medium` VALUES ('8334798300134277120', '1635979205605543938');
INSERT INTO `cat_medium` VALUES ('8334798302252400640', '1635979205605543937');
INSERT INTO `cat_medium` VALUES ('8334798302252400640', '1635979205605543938');
INSERT INTO `cat_medium` VALUES ('8334822757231525888', '1635979205605543937');
INSERT INTO `cat_medium` VALUES ('8334822757231525888', '1635979205605543938');
INSERT INTO `cat_medium` VALUES ('8334833036433063936', '1635979086197903362');
INSERT INTO `cat_medium` VALUES ('8335499445893890048', '1635979086197903362');
INSERT INTO `cat_medium` VALUES ('8335504582554714112', '1635979086197903362');
INSERT INTO `cat_medium` VALUES ('8335504901747015680', '1635979086197903362');
INSERT INTO `cat_medium` VALUES ('8335505705732177920', '1635979086197903362');
INSERT INTO `cat_medium` VALUES ('8335506063887990784', '1635979086197903362');
INSERT INTO `cat_medium` VALUES ('8335506125066108928', '1635979086197903362');

-- ----------------------------
-- Table structure for cat_message
-- ----------------------------
DROP TABLE IF EXISTS `cat_message`;
CREATE TABLE `cat_message` (
  `cat_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cat_kind` varchar(10) NOT NULL COMMENT '猫咪品种',
  `cat_birthplace` varchar(20) NOT NULL,
  `cat_feature` varchar(255) NOT NULL,
  `cat_describe` varchar(255) DEFAULT NULL,
  `cat_detail` longtext NOT NULL,
  `cat_advice` int(5) DEFAULT '0',
  `cat_name` varchar(20) DEFAULT NULL COMMENT '猫咪详细名',
  PRIMARY KEY (`cat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8335506125066108929 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_message
-- ----------------------------
INSERT INTO `cat_message` VALUES ('8334798293855404032', '11', '111', '111', null, '111', '0', null);
INSERT INTO `cat_message` VALUES ('8334798297575751680', '11', '111', '111', null, '111', '0', null);
INSERT INTO `cat_message` VALUES ('8334798300134277120', '11', '111', '111', null, '111', '0', null);
INSERT INTO `cat_message` VALUES ('8334798302252400640', '11', '111', '111', null, '111', '0', null);
INSERT INTO `cat_message` VALUES ('8334822757231525888', '11', '111', '111', null, '111', '0', 'maomi');
INSERT INTO `cat_message` VALUES ('8334833036433063936', '11', '111', '111', null, '111', '0', 'maomimi');
INSERT INTO `cat_message` VALUES ('8335499445893890048', '11', '111', '111', null, '111', '0', 'maomimi');
INSERT INTO `cat_message` VALUES ('8335504582554714112', '11', '111', '111', null, '111', '0', 'maomimi');
INSERT INTO `cat_message` VALUES ('8335504901747015680', '11', '111', '111', null, '111', '0', 'maomimi');
INSERT INTO `cat_message` VALUES ('8335505705732177920', '11', '111', '111', null, '111', '0', 'maomimi');
INSERT INTO `cat_message` VALUES ('8335506063887990784', '11', '111', '111', null, '111', '0', 'maomimi');
INSERT INTO `cat_message` VALUES ('8335506125066108928', '11', '111', '111', null, '111', '0', 'maomimi');

-- ----------------------------
-- Table structure for cat_school_animal
-- ----------------------------
DROP TABLE IF EXISTS `cat_school_animal`;
CREATE TABLE `cat_school_animal` (
  `animal_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL,
  `animal_kind` varchar(10) NOT NULL,
  `animal_name` varchar(20) DEFAULT NULL,
  `animal_birthplace` varchar(20) NOT NULL,
  `animal_describe` varchar(255) DEFAULT NULL,
  `animal_profile_photo` varchar(255) NOT NULL,
  `animal_deleted_flag` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`animal_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_school_animal
-- ----------------------------

-- ----------------------------
-- Table structure for cat_speak
-- ----------------------------
DROP TABLE IF EXISTS `cat_speak`;
CREATE TABLE `cat_speak` (
  `speak_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `speak_message` varchar(255) NOT NULL,
  `publish_time` datetime NOT NULL,
  PRIMARY KEY (`speak_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1647197594206318595 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_speak
-- ----------------------------
INSERT INTO `cat_speak` VALUES ('1647197589881991169', '111', '2023-04-15 00:00:00');
INSERT INTO `cat_speak` VALUES ('1647197591916228610', '123', '2023-04-15 00:00:00');
INSERT INTO `cat_speak` VALUES ('1647197594206318594', '123', '2023-04-15 00:00:00');

-- ----------------------------
-- Table structure for cat_tag
-- ----------------------------
DROP TABLE IF EXISTS `cat_tag`;
CREATE TABLE `cat_tag` (
  `tag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(50) NOT NULL,
  `tag_make_time` datetime DEFAULT NULL,
  `tag_sort` int(20) DEFAULT '1',
  PRIMARY KEY (`tag_id`),
  UNIQUE KEY `tag_name` (`tag_name`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=1635979205605543939 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_tag
-- ----------------------------
INSERT INTO `cat_tag` VALUES ('1635979086197903362', '巨大猫', '2023-03-15 20:19:58', '2');
INSERT INTO `cat_tag` VALUES ('1635979143315935233', '暹罗猫', '2023-03-15 20:19:57', '2');
INSERT INTO `cat_tag` VALUES ('1635979205605543937', '超可爱', '2023-03-15 20:23:32', '2');
INSERT INTO `cat_tag` VALUES ('1635979205605543938', 'haihao', '2023-03-02 20:43:40', '1');

-- ----------------------------
-- Table structure for cat_users
-- ----------------------------
DROP TABLE IF EXISTS `cat_users`;
CREATE TABLE `cat_users` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_ip` varchar(30) NOT NULL,
  `user_password` varchar(255) NOT NULL,
  `user_email` varchar(20) NOT NULL,
  `user_profile_photo` varchar(255) DEFAULT NULL,
  `user_register_time` datetime DEFAULT NULL,
  `user_birthday` date NOT NULL,
  `user_age` int(4) DEFAULT NULL,
  `user_nickname` varchar(10) NOT NULL,
  `user_sex` tinyint(1) NOT NULL DEFAULT '1',
  `deleted_flag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_email` (`user_email`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1627582436685377539 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cat_users
-- ----------------------------
INSERT INTO `cat_users` VALUES ('1627582436685377538', '上海', '202cb962ac59075b964b07152d234b70', '1589532402@qq.com', null, '2023-02-20 16:14:26', '2003-06-06', '19', '管理员', '1', '0');

-- ----------------------------
-- Table structure for cat_user_collect
-- ----------------------------
DROP TABLE IF EXISTS `cat_user_collect`;
CREATE TABLE `cat_user_collect` (
  `collect_user_id` bigint(32) NOT NULL COMMENT '点赞的用户id',
  `collect_article_id` bigint(32) NOT NULL COMMENT '被点赞的文章id',
  `status` tinyint(1) DEFAULT '1' COMMENT '点赞状态，0取消，1点赞',
  KEY `liked_user_id` (`collect_user_id`),
  KEY `liked_post_id` (`collect_article_id`),
  CONSTRAINT `cat_user_collect_ibfk_1` FOREIGN KEY (`collect_user_id`) REFERENCES `cat_users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cat_user_collect_ibfk_2` FOREIGN KEY (`collect_article_id`) REFERENCES `cat_article` (`article_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户点赞表';

-- ----------------------------
-- Records of cat_user_collect
-- ----------------------------

-- ----------------------------
-- Table structure for cat_user_like
-- ----------------------------
DROP TABLE IF EXISTS `cat_user_like`;
CREATE TABLE `cat_user_like` (
  `liked_user_id` bigint(32) NOT NULL COMMENT '点赞的用户id',
  `liked_article_id` bigint(32) NOT NULL COMMENT '被点赞的文章id',
  `status` tinyint(1) DEFAULT '1' COMMENT '点赞状态，0取消，1点赞',
  KEY `liked_user_id` (`liked_user_id`),
  KEY `liked_post_id` (`liked_article_id`),
  CONSTRAINT `cat_user_like_ibfk_1` FOREIGN KEY (`liked_user_id`) REFERENCES `cat_users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `cat_user_like_ibfk_2` FOREIGN KEY (`liked_article_id`) REFERENCES `cat_article` (`article_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户点赞表';

-- ----------------------------
-- Records of cat_user_like
-- ----------------------------

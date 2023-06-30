CREATE TABLE `pub_dependent_component` (
  `id` varchar(36) NOT NULL,
  `component_cn_name` varchar(64) NOT NULL COMMENT '组件中文名',
  `component_en_name` varchar(64) NOT NULL COMMENT '组件英文名',
  `version` varchar(32) NOT NULL COMMENT '组件版本',
  `publishlist_id` varchar(64) NOT NULL COMMENT '发布单id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `pub_issue` (
  `id` varchar(36) NOT NULL,
  `issue_num` varchar(32) NOT NULL COMMENT 'jira的issue号',
  `issue_name` varchar(256) NOT NULL COMMENT 'jira的issue名',
  `issue_en_name` varchar(256) DEFAULT NULL COMMENT 'issue英文名',
  `issue_ch_name` varchar(256) DEFAULT NULL COMMENT 'issue中文名',
  `issue_content` varchar(1024) DEFAULT NULL COMMENT 'issue内容',
  `issue_type` varchar(32) NOT NULL COMMENT 'jira的issue类型',
  `issue_link` varchar(256) NOT NULL COMMENT 'jira的issue链接',
  `publishlist_id` varchar(32) NOT NULL COMMENT '发布单号',
  `project_id` varchar(32) NOT NULL COMMENT '项目id',
  `jira_version_name` varchar(32) NOT NULL COMMENT 'jira版本名',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `issue_id` varchar(32) NOT NULL COMMENT 'jira的issue id号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `pub_issue_history` (
  `id` varchar(36) NOT NULL,
  `issue_num` varchar(32) NOT NULL COMMENT 'jira的issue号',
  `issue_name` varchar(256) NOT NULL COMMENT 'jira的issue名',
  `issue_en_name` varchar(256) DEFAULT NULL COMMENT 'issue英文名',
  `issue_ch_name` varchar(256) DEFAULT NULL COMMENT 'issue中文名',
  `issue_content` varchar(1024) DEFAULT NULL COMMENT 'issue内容',
  `issue_type` varchar(32) NOT NULL COMMENT 'jira的issue类型',
  `issue_link` varchar(256) NOT NULL COMMENT 'jira的issue链接',
  `publishlist_id` varchar(32) NOT NULL COMMENT '发布单号',
  `project_id` varchar(32) NOT NULL COMMENT '项目id',
  `jira_version_name` varchar(32) NOT NULL COMMENT 'jira版本名',
  `issue_create_datetime` datetime NOT NULL COMMENT '当初创建的日期时间',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `issue_id` varchar(32) DEFAULT NULL COMMENT 'jira的issue id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `pub_package_url` (
  `id` varchar(36) NOT NULL,
  `storage_type` varchar(32) NOT NULL COMMENT '云存储类型',
  `package_url` varchar(256) NOT NULL COMMENT '包url',
  `publishlist_id` varchar(64) NOT NULL COMMENT '发布单id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `pub_project` (
  `id` varchar(36) NOT NULL,
  `project_key` varchar(32) NOT NULL COMMENT 'project key',
  `name` varchar(64) NOT NULL COMMENT '项目名',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `pub_publishlist` (
  `id` varchar(36) NOT NULL,
  `name` varchar(32) DEFAULT NULL COMMENT '发布单名',
  `product_line_name` varchar(32) DEFAULT NULL COMMENT '产品线名',
  `product_name` varchar(32) DEFAULT NULL COMMENT '产品名',
  `version_name` varchar(32) DEFAULT NULL COMMENT '版本名',
  `version_type` varchar(32) DEFAULT NULL COMMENT '版本类型',
  `jira_version_name` varchar(32) DEFAULT NULL COMMENT 'jira版本名',
  `document_version` varchar(32) DEFAULT NULL COMMENT '文档版本',
  `scrum_num` varchar(32) DEFAULT NULL COMMENT '迭代冲刺号',
  `scrum_stage` varchar(32) DEFAULT NULL COMMENT '迭代阶段',
  `publishlist_stage` varchar(32) DEFAULT NULL COMMENT '发布单状态',
  `publish_datetime` varchar(32) DEFAULT NULL COMMENT '发布时间',
  `pm_id` varchar(32) DEFAULT NULL COMMENT '产品经理id',
  `pm_name` varchar(32) DEFAULT NULL COMMENT '产品经理名',
  `commit_id` varchar(128) DEFAULT NULL COMMENT 'commit_id',
  `document_url_id` varchar(256) DEFAULT NULL COMMENT '页内链接ID',
  `user_manual_en_link` varchar(256) DEFAULT NULL COMMENT '用户手册中文链接',
  `user_manual_ch_link` varchar(256) DEFAULT NULL COMMENT '用户手册英文链接',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `pub_publishlist_project` (
  `id` varchar(36) NOT NULL,
  `publishlist_id` varchar(36) NOT NULL COMMENT 'publishlist的id',
  `project_id` varchar(32) NOT NULL COMMENT '项目id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
CREATE TABLE `pub_template` (
  `id` varchar(36) NOT NULL,
  `type` varchar(32) NOT NULL COMMENT '类型',
  `product_line_name` varchar(32) NOT NULL COMMENT '产品线名',
  `product_name` varchar(32) NOT NULL COMMENT '产品名',
  `document_version` varchar(32) NOT NULL COMMENT '文档版本',
  `content` text NOT NULL COMMENT '内容',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

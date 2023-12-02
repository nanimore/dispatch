DROP TABLE IF EXISTS job;

create table job
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `job_name` varchar(255) NOT NULL COMMENT '任务名',
    `job_detail` varchar(255) NOT NULL COMMENT '任务详情',
    `job_executor` varchar(255) NOT NULL COMMENT '任务执行器',
    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '任务创建时间',
    `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '任务更新时间',
    `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除，0-否，1-是',

    PRIMARY KEY (`id`) USING BTREE
);

DROP TABLE IF EXISTS job_rel;

create table job_rel
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `rel_name` varchar(255) NOT NULL COMMENT '关系名',
    `rel_head` bigint(20) NOT NULL COMMENT '关系头',
    `rel_tail` bigint(20) NOT NULL COMMENT '关系尾',
    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '任务创建时间',
    `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '任务更新时间',
    `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除，0-否，1-是',

    PRIMARY KEY (`id`) USING BTREE
);

DROP TABLE IF EXISTS executor;

create table executor
(
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
    `exe_name` varchar(255) NOT NULL COMMENT '执行器名',
    `exe_api` varchar(255) NOT NULL COMMENT '执行器api',
    `exe_type` varchar(255) NOT NULL COMMENT '执行器类型',
    `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '任务创建时间',
    `update_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '任务更新时间',
    `is_deleted` tinyint(4) NOT NULL DEFAULT 0 COMMENT '是否删除，0-否，1-是',

    PRIMARY KEY (`id`) USING BTREE
)
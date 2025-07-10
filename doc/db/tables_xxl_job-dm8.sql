
-- ==========================================
-- drop tables
-- ==========================================
-- drop table if EXISTS xxl_job_info;
-- drop table if EXISTS xxl_job_log;
-- drop table if EXISTS xxl_job_log_report;
-- drop table if EXISTS xxl_job_logglue;
-- drop table if EXISTS xxl_job_registry;
-- drop table if EXISTS xxl_job_group;
-- drop table if EXISTS xxl_job_user;
-- drop table if EXISTS xxl_job_lock;


-- ----------------------------
-- Table structure for xxl_job_group
-- ----------------------------
CREATE TABLE xxl_job_group (
                               id NUMBER(11) IDENTITY(1, 1),
                               app_name VARCHAR2(64 CHAR) NOT NULL,
                               title VARCHAR2(12 CHAR) NOT NULL,
                               address_type NUMBER(4) DEFAULT 0 NOT NULL,
                               address_list CLOB,
                               update_time TIMESTAMP,
                               CONSTRAINT pk_xxl_job_group PRIMARY KEY (id)
);

COMMENT ON TABLE xxl_job_group IS '执行器信息表';
COMMENT ON COLUMN xxl_job_group.app_name IS '执行器AppName';
COMMENT ON COLUMN xxl_job_group.title IS '执行器名称';
COMMENT ON COLUMN xxl_job_group.address_type IS '执行器地址类型：0=自动注册、1=手动录入';
COMMENT ON COLUMN xxl_job_group.address_list IS '执行器地址列表，多地址逗号分隔';


-- ----------------------------
-- Table structure for xxl_job_info
-- ----------------------------
CREATE TABLE xxl_job_info (
                              id NUMBER(11) IDENTITY(1, 1),
                              job_group NUMBER(11) NOT NULL,
                              job_desc VARCHAR2(255 CHAR) NOT NULL,
                              add_time TIMESTAMP DEFAULT NULL,
                              update_time TIMESTAMP DEFAULT NULL,
                              author VARCHAR2(64 CHAR) DEFAULT NULL,
                              alarm_email VARCHAR2(255 CHAR) DEFAULT NULL,
                              schedule_type VARCHAR2(50 CHAR) DEFAULT 'NONE' NOT NULL,
                              schedule_conf VARCHAR2(128 CHAR) DEFAULT NULL,
                              misfire_strategy VARCHAR2(50 CHAR) DEFAULT 'DO_NOTHING' NOT NULL,
                              executor_route_strategy VARCHAR2(50 CHAR) DEFAULT NULL,
                              executor_handler VARCHAR2(255 CHAR) DEFAULT NULL,
                              executor_param VARCHAR2(512 CHAR) DEFAULT NULL,
                              executor_block_strategy VARCHAR2(50 CHAR) DEFAULT NULL,
                              executor_timeout NUMBER(11) DEFAULT 0 NOT NULL,
                              executor_fail_retry_count NUMBER(11) DEFAULT 0 NOT NULL,
                              glue_type VARCHAR2(50 CHAR) NOT NULL,
                              glue_source CLOB,
                              glue_remark VARCHAR2(128 CHAR) DEFAULT NULL,
                              glue_updatetime TIMESTAMP DEFAULT NULL,
                              child_jobid VARCHAR2(255 CHAR) DEFAULT NULL,
                              trigger_status NUMBER(4) DEFAULT 0 NOT NULL,
                              trigger_last_time NUMBER(13) DEFAULT 0 NOT NULL,
                              trigger_next_time NUMBER(13) DEFAULT 0 NOT NULL,
                              CONSTRAINT pk_xxl_job_info PRIMARY KEY (id)
);

COMMENT ON TABLE xxl_job_info IS '任务信息表';
COMMENT ON COLUMN xxl_job_info.job_group IS '执行器主键ID';
COMMENT ON COLUMN xxl_job_info.author IS '作者';
COMMENT ON COLUMN xxl_job_info.alarm_email IS '报警邮件';
COMMENT ON COLUMN xxl_job_info.schedule_type IS '调度类型';
COMMENT ON COLUMN xxl_job_info.schedule_conf IS '调度配置，值含义取决于调度类型';
COMMENT ON COLUMN xxl_job_info.misfire_strategy IS '调度过期策略';
COMMENT ON COLUMN xxl_job_info.executor_route_strategy IS '执行器路由策略';
COMMENT ON COLUMN xxl_job_info.executor_handler IS '执行器任务handler';
COMMENT ON COLUMN xxl_job_info.executor_param IS '执行器任务参数';
COMMENT ON COLUMN xxl_job_info.executor_block_strategy IS '阻塞处理策略';
COMMENT ON COLUMN xxl_job_info.executor_timeout IS '任务执行超时时间，单位秒';
COMMENT ON COLUMN xxl_job_info.executor_fail_retry_count IS '失败重试次数';
COMMENT ON COLUMN xxl_job_info.glue_type IS 'GLUE类型';
COMMENT ON COLUMN xxl_job_info.glue_source IS 'GLUE源代码';
COMMENT ON COLUMN xxl_job_info.glue_remark IS 'GLUE备注';
COMMENT ON COLUMN xxl_job_info.glue_updatetime IS 'GLUE更新时间';
COMMENT ON COLUMN xxl_job_info.child_jobid IS '子任务ID，多个逗号分隔';
COMMENT ON COLUMN xxl_job_info.trigger_status IS '调度状态：0-停止，1-运行';
COMMENT ON COLUMN xxl_job_info.trigger_last_time IS '上次调度时间';
COMMENT ON COLUMN xxl_job_info.trigger_next_time IS '下次调度时间';


-- ----------------------------
-- Table structure for xxl_job_lock
-- ----------------------------
CREATE TABLE xxl_job_lock (
                              lock_name VARCHAR2(50 CHAR) NOT NULL,
                              CONSTRAINT pk_xxl_job_lock PRIMARY KEY (lock_name)
);

COMMENT ON TABLE xxl_job_lock IS '任务调度锁';
COMMENT ON COLUMN xxl_job_lock.lock_name IS '锁名称';


-- ----------------------------
-- Table structure for xxl_job_log
-- ----------------------------
CREATE TABLE xxl_job_log (
                             id NUMBER(20) IDENTITY(1, 1),
                             job_group NUMBER(11) NOT NULL,
                             job_id NUMBER(11) NOT NULL,
                             executor_address VARCHAR2(255 CHAR) DEFAULT NULL,
                             executor_handler VARCHAR2(255 CHAR) DEFAULT NULL,
                             executor_param VARCHAR2(512 CHAR) DEFAULT NULL,
                             executor_sharding_param VARCHAR2(20 CHAR) DEFAULT NULL,
                             executor_fail_retry_count NUMBER(11) DEFAULT 0 NOT NULL,
                             trigger_time TIMESTAMP DEFAULT NULL,
                             trigger_code NUMBER(11) NOT NULL,
                             trigger_msg CLOB,
                             handle_time TIMESTAMP DEFAULT NULL,
                             handle_code NUMBER(11) NOT NULL,
                             handle_msg CLOB,
                             alarm_status NUMBER(4) DEFAULT 0 NOT NULL,
                             CONSTRAINT pk_xxl_job_log PRIMARY KEY (id)
);

CREATE INDEX I_trigger_time ON xxl_job_log(trigger_time);
CREATE INDEX I_handle_code ON xxl_job_log(handle_code);
CREATE INDEX I_jobid_jobgroup ON xxl_job_log(job_id, job_group);
CREATE INDEX I_job_id ON xxl_job_log(job_id);

COMMENT ON TABLE xxl_job_log IS '任务日志表';
COMMENT ON COLUMN xxl_job_log.job_group IS '执行器主键ID';
COMMENT ON COLUMN xxl_job_log.job_id IS '任务，主键ID';
COMMENT ON COLUMN xxl_job_log.executor_address IS '执行器地址，本次执行的地址';
COMMENT ON COLUMN xxl_job_log.executor_handler IS '执行器任务handler';
COMMENT ON COLUMN xxl_job_log.executor_param IS '执行器任务参数';
COMMENT ON COLUMN xxl_job_log.executor_sharding_param IS '执行器任务分片参数，格式如 1/2';
COMMENT ON COLUMN xxl_job_log.executor_fail_retry_count IS '失败重试次数';
COMMENT ON COLUMN xxl_job_log.trigger_time IS '调度-时间';
COMMENT ON COLUMN xxl_job_log.trigger_code IS '调度-结果';
COMMENT ON COLUMN xxl_job_log.trigger_msg IS '调度-日志';
COMMENT ON COLUMN xxl_job_log.handle_time IS '执行-时间';
COMMENT ON COLUMN xxl_job_log.handle_code IS '执行-状态';
COMMENT ON COLUMN xxl_job_log.handle_msg IS '执行-日志';
COMMENT ON COLUMN xxl_job_log.alarm_status IS '告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败';


-- ----------------------------
-- Table structure for xxl_job_log_report
-- ----------------------------
CREATE TABLE xxl_job_log_report (
                                    id NUMBER(11) IDENTITY(1, 1),
                                    trigger_day TIMESTAMP DEFAULT NULL,
                                    running_count NUMBER(11) DEFAULT 0 NOT NULL,
                                    suc_count NUMBER(11) DEFAULT 0 NOT NULL,
                                    fail_count NUMBER(11) DEFAULT 0 NOT NULL,
                                    update_time TIMESTAMP DEFAULT NULL,
                                    CONSTRAINT pk_xxl_job_log_report PRIMARY KEY (id),
                                    CONSTRAINT uq_trigger_day UNIQUE (trigger_day)
);

COMMENT ON TABLE xxl_job_log_report IS '任务日志报表';
COMMENT ON COLUMN xxl_job_log_report.trigger_day IS '调度-时间';
COMMENT ON COLUMN xxl_job_log_report.running_count IS '运行中-日志数量';
COMMENT ON COLUMN xxl_job_log_report.suc_count IS '执行成功-日志数量';
COMMENT ON COLUMN xxl_job_log_report.fail_count IS '执行失败-日志数量';


-- ----------------------------
-- Table structure for xxl_job_logglue
-- ----------------------------
CREATE TABLE xxl_job_logglue (
                                 id NUMBER(11) IDENTITY(1, 1),
                                 job_id NUMBER(11) NOT NULL,
                                 glue_type VARCHAR2(50 CHAR) DEFAULT NULL,
                                 glue_source CLOB,
                                 glue_remark VARCHAR2(128 CHAR) NOT NULL,
                                 add_time TIMESTAMP DEFAULT NULL,
                                 update_time TIMESTAMP DEFAULT NULL,
                                 CONSTRAINT pk_xxl_job_logglue PRIMARY KEY (id)
);

COMMENT ON TABLE xxl_job_logglue IS '任务GLUE日志';
COMMENT ON COLUMN xxl_job_logglue.job_id IS '任务，主键ID';
COMMENT ON COLUMN xxl_job_logglue.glue_type IS 'GLUE类型';
COMMENT ON COLUMN xxl_job_logglue.glue_source IS 'GLUE源代码';
COMMENT ON COLUMN xxl_job_logglue.glue_remark IS 'GLUE备注';


-- ----------------------------
-- Table structure for xxl_job_registry
-- ----------------------------
CREATE TABLE xxl_job_registry (
                                  id NUMBER(11) IDENTITY(1, 1),
                                  registry_group VARCHAR2(50 CHAR) NOT NULL,
                                  registry_key VARCHAR2(255 CHAR) NOT NULL,
                                  registry_value VARCHAR2(255 CHAR) NOT NULL,
                                  update_time TIMESTAMP,
                                  CONSTRAINT pk_xxl_job_registry PRIMARY KEY (id),
                                  CONSTRAINT uq_i_g_k_v UNIQUE (registry_group, registry_key, registry_value)
);

COMMENT ON TABLE xxl_job_registry IS '执行器注册表';


-- ----------------------------
-- Table structure for xxl_job_user
-- ----------------------------
CREATE TABLE xxl_job_user (
                              id NUMBER(11) IDENTITY(1, 1),
                              username VARCHAR2(50 CHAR) NOT NULL,
                              password VARCHAR2(50 CHAR) NOT NULL,
                              role NUMBER(4) NOT NULL,
                              permission VARCHAR2(255 CHAR) DEFAULT NULL,
                              CONSTRAINT pk_xxl_job_user PRIMARY KEY (id),
                              CONSTRAINT uq_i_username UNIQUE (username)
);

COMMENT ON TABLE xxl_job_user IS '系统用户表';
COMMENT ON COLUMN xxl_job_user.username IS '账号';
COMMENT ON COLUMN xxl_job_user.password IS '密码';
COMMENT ON COLUMN xxl_job_user.role IS '角色：0-普通用户、1-管理员';
COMMENT ON COLUMN xxl_job_user.permission IS '权限：执行器ID列表，多个逗号分割';


-- ####################################################################################
-- ##                                                                                ##
-- ##                               INIT DATA                                        ##
-- ##                                                                                ##
-- ####################################################################################

INSERT INTO xxl_job_group( app_name, title, address_type, address_list, update_time) VALUES ( 'xxl-job-executor-sample', '通用执行器Sample', 0, NULL, CURRENT_TIMESTAMP);
INSERT INTO xxl_job_group( app_name, title, address_type, address_list, update_time) VALUES ('xxl-job-executor-sample-ai', 'AI执行器Sample', 0, NULL, CURRENT_TIMESTAMP);

INSERT INTO xxl_job_info(job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid) VALUES (1, '示例任务01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'XXL', '', 'CRON', '0 0 0 * * ? *', 'DO_NOTHING', 'FIRST', 'demoJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', CURRENT_TIMESTAMP, '');
INSERT INTO xxl_job_info(job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid) VALUES (2, 'Ollama示例任务01', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'XXL', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'ollamaJobHandler', '{
    "input": "慢SQL问题分析思路",
    "prompt": "你是一个研发工程师，擅长解决技术类问题。"
}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', CURRENT_TIMESTAMP, '');
INSERT INTO xxl_job_info( job_group, job_desc, add_time, update_time, author, alarm_email, schedule_type, schedule_conf, misfire_strategy, executor_route_strategy, executor_handler, executor_param, executor_block_strategy, executor_timeout, executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime, child_jobid) VALUES ( 2, 'Dify示例任务', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 'XXL', '', 'NONE', '', 'DO_NOTHING', 'FIRST', 'difyWorkflowJobHandler', '{
    "inputs":{
        "input":"查询班级各学科前三名"
    },
    "user": "xxl-job",
    "baseUrl": "http://localhost/v1",
    "apiKey": "app-OUVgNUOQRIMokfmuJvBJoUTN"
}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化', CURRENT_TIMESTAMP, '');

INSERT INTO xxl_job_user( username, password, role, permission) VALUES ( 'admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);

INSERT INTO xxl_job_lock (lock_name) VALUES ('schedule_lock');

COMMIT;
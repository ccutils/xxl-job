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

-- ==========================================
-- create tables
-- ==========================================
-- xxl_job_info table
CREATE TABLE xxl_job_info
(
    id INT NOT NULL IDENTITY(1,1),  -- 自增
    job_group INT NOT NULL,  -- 执行器主键ID
    job_desc NVARCHAR(255) NOT NULL,
    add_time DATETIME NULL,
    update_time DATETIME NULL,
    author NVARCHAR(64) NULL,  -- 作者
    alarm_email NVARCHAR(255) NULL,  -- 报警邮件
    schedule_type NVARCHAR(50) NOT NULL DEFAULT 'NONE',  -- 调度类型
    schedule_conf NVARCHAR(128) NULL,  -- 调度配置
    misfire_strategy NVARCHAR(50) NOT NULL DEFAULT 'DO_NOTHING',  -- 调度过期策略
    executor_route_strategy NVARCHAR(50) NULL,  -- 执行器路由策略
    executor_handler NVARCHAR(255) NULL,  -- 执行器任务handler
    executor_param NVARCHAR(512) NULL,  -- 执行器任务参数
    executor_block_strategy NVARCHAR(50) NULL,  -- 阻塞处理策略
    executor_timeout INT NOT NULL DEFAULT 0,  -- 任务执行超时时间，单位秒
    executor_fail_retry_count INT NOT NULL DEFAULT 0,  -- 失败重试次数
    glue_type NVARCHAR(50) NOT NULL,  -- GLUE类型
    glue_source NVARCHAR(MAX) NULL,  -- GLUE源代码
    glue_remark NVARCHAR(128) NULL,  -- GLUE备注
    glue_updatetime DATETIME NULL,  -- GLUE更新时间
    child_jobid NVARCHAR(255) NULL,  -- 子任务ID
    trigger_status INT NOT NULL DEFAULT 0,  -- 调度状态
    trigger_last_time BIGINT NOT NULL DEFAULT 0,  -- 上次调度时间
    trigger_next_time BIGINT NOT NULL DEFAULT 0,  -- 下次调度时间
    CONSTRAINT PK_xxl_job_info PRIMARY KEY (id)
);

-- xxl_job_log table
CREATE TABLE xxl_job_log
(
    id BIGINT NOT NULL IDENTITY(1,1),  -- 自增
    job_group INT NOT NULL,  -- 执行器主键ID
    job_id INT NOT NULL,  -- 任务主键ID
    executor_address NVARCHAR(255) NULL,  -- 执行器地址
    executor_handler NVARCHAR(255) NULL,  -- 执行器任务handler
    executor_param NVARCHAR(512) NULL,  -- 执行器任务参数
    executor_sharding_param NVARCHAR(20) NULL,  -- 执行器任务分片参数
    executor_fail_retry_count INT NOT NULL DEFAULT 0,  -- 失败重试次数
    trigger_time DATETIME NULL,  -- 调度时间
    trigger_code INT NOT NULL,  -- 调度结果
    trigger_msg NTEXT NULL,  -- 调度日志
    handle_time DATETIME NULL,  -- 执行时间
    handle_code INT NOT NULL,  -- 执行状态
    handle_msg NTEXT NULL,  -- 执行日志
    alarm_status INT NOT NULL DEFAULT 0,  -- 告警状态
    CONSTRAINT PK_xxl_job_log PRIMARY KEY (id),
    INDEX I_trigger_time (trigger_time),
    INDEX I_handle_code (handle_code),
    INDEX I_jobid_jobgroup (job_id, job_group),
    INDEX I_job_id (job_id)
);

-- xxl_job_log_report table
CREATE TABLE xxl_job_log_report
(
    id INT NOT NULL IDENTITY(1,1),  -- 自增
    trigger_day DATETIME NULL,  -- 调度时间
    running_count INT NOT NULL DEFAULT 0,  -- 运行中日志数量
    suc_count INT NOT NULL DEFAULT 0,  -- 执行成功日志数量
    fail_count INT NOT NULL DEFAULT 0,  -- 执行失败日志数量
    update_time DATETIME NULL,
    CONSTRAINT PK_xxl_job_log_report PRIMARY KEY (id),
    UNIQUE (trigger_day)
);

-- xxl_job_logglue table
CREATE TABLE xxl_job_logglue
(
    id INT NOT NULL IDENTITY(1,1),  -- 自增
    job_id INT NOT NULL,  -- 任务主键ID
    glue_type NVARCHAR(50) NULL,  -- GLUE类型
    glue_source NVARCHAR(MAX) NULL,  -- GLUE源代码
    glue_remark NVARCHAR(128) NOT NULL,  -- GLUE备注
    add_time DATETIME NULL,
    update_time DATETIME NULL,
    CONSTRAINT PK_xxl_job_logglue PRIMARY KEY (id)
);

-- xxl_job_registry table
CREATE TABLE xxl_job_registry
(
    id INT NOT NULL IDENTITY(1,1),  -- 自增
    registry_group NVARCHAR(50) NOT NULL,
    registry_key NVARCHAR(255) NOT NULL,
    registry_value NVARCHAR(255) NOT NULL,
    update_time DATETIME NULL,
    CONSTRAINT PK_xxl_job_registry PRIMARY KEY (id),
    UNIQUE (registry_group, registry_key, registry_value)
);

-- xxl_job_group table
CREATE TABLE xxl_job_group
(
    id INT NOT NULL IDENTITY(1,1),  -- 自增
    app_name NVARCHAR(64) NOT NULL,  -- 执行器AppName
    title NVARCHAR(12) NOT NULL,  -- 执行器名称
    address_type INT NOT NULL DEFAULT 0,  -- 执行器地址类型
    address_list NTEXT NULL,  -- 执行器地址列表
    update_time DATETIME NULL,
    CONSTRAINT PK_xxl_job_group PRIMARY KEY (id)
);

-- xxl_job_user table
CREATE TABLE xxl_job_user
(
    id INT NOT NULL IDENTITY(1,1),  -- 自增
    username NVARCHAR(50) NOT NULL,  -- 账号
    password NVARCHAR(50) NOT NULL,  -- 密码
    role INT NOT NULL,  -- 角色
    permission NVARCHAR(255) NULL,  -- 权限
    CONSTRAINT PK_xxl_job_user PRIMARY KEY (id),
    UNIQUE (username)
);

-- xxl_job_lock table
CREATE TABLE xxl_job_lock
(
    lock_name NVARCHAR(50) NOT NULL,  -- 锁名称
    CONSTRAINT PK_xxl_job_lock PRIMARY KEY (lock_name)
);

-- ==========================================
-- init data
-- ==========================================
INSERT INTO xxl_job_group(app_name, title, address_type, address_list, update_time)
VALUES ('xxl-job-executor-sample', N'通用执行器Sample', 0, NULL, GETDATE()),
       ('xxl-job-executor-sample-ai', N'AI执行器Sample', 0, NULL, GETDATE());

INSERT INTO xxl_job_info(job_group, job_desc, add_time, update_time, author, alarm_email,
                         schedule_type, schedule_conf, misfire_strategy, executor_route_strategy,
                         executor_handler, executor_param, executor_block_strategy, executor_timeout,
                         executor_fail_retry_count, glue_type, glue_source, glue_remark, glue_updatetime,
                         child_jobid)
VALUES (1, N'示例任务01', GETDATE(), GETDATE(), 'XXL', '', 'CRON', '0 0 0 * * ? *',
        'DO_NOTHING', 'FIRST', 'demoJobHandler', '', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', N'GLUE代码初始化',
        GETDATE(), ''),
       (2, N'Ollama示例任务01', GETDATE(), GETDATE(), 'XXL', '', 'NONE', '',
        N'DO_NOTHING', 'FIRST', 'ollamaJobHandler', '{
    "input": "慢SQL问题分析思路",
    "prompt": "你是一个研发工程师，擅长解决技术类问题。"
}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        GETDATE(), ''),
       (2, N'Dify示例任务', GETDATE(), GETDATE(), 'XXL', '', 'NONE', '',
        N'DO_NOTHING', 'FIRST', 'difyWorkflowJobHandler', '{
    "inputs":{
        "input":"查询班级各学科前三名"
    },
    "user": "xxl-job",
    "baseUrl": "http://localhost/v1",
    "apiKey": "app-OUVgNUOQRIMokfmuJvBJoUTN"
}', 'SERIAL_EXECUTION', 0, 0, 'BEAN', '', 'GLUE代码初始化',
        GETDATE(), '')
;

INSERT INTO xxl_job_user(username, password, role, permission)
VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', 1, NULL);

INSERT INTO xxl_job_lock (lock_name)
VALUES ('schedule_lock');




-- ==========================================
-- set comment
-- ==========================================


EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行器主键ID', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = job_group;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'作者', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = author;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'报警邮件', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = alarm_email;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'调度类型', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = schedule_type;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'调度配置，值含义取决于调度类型', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = schedule_conf;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'调度过期策略', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = misfire_strategy;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行器路由策略', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = executor_route_strategy;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行器任务handler', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = executor_handler;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行器任务参数', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = executor_param;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'阻塞处理策略', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = executor_block_strategy;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'任务执行超时时间，单位秒', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = executor_timeout;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'失败重试次数', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_info, 
    @level2type = N'COLUMN', @level2name = executor_fail_retry_count;

-- xxl_job_log 表字段注释转换为 SQL Server 扩展属性注释

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'调度-时间', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log, 
    @level2type = N'COLUMN', @level2name = trigger_time;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'调度-结果', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log, 
    @level2type = N'COLUMN', @level2name = trigger_code;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'调度-日志', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log, 
    @level2type = N'COLUMN', @level2name = trigger_msg;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行-时间', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log, 
    @level2type = N'COLUMN', @level2name = handle_time;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行-状态', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log, 
    @level2type = N'COLUMN', @level2name = handle_code;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行-日志', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log, 
    @level2type = N'COLUMN', @level2name = handle_msg;

-- EXEC sp_dropextendedproperty 
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'告警状态：0-默认、1-无需告警、2-告警成功、3-告警失败', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log, 
    @level2type = N'COLUMN', @level2name = alarm_status;

-- xxl_job_log_report 表字段注释转换为 SQL Server 扩展属性注释

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'调度-时间', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log_report, 
    @level2type = N'COLUMN', @level2name = trigger_day;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'运行中-日志数量', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log_report, 
    @level2type = N'COLUMN', @level2name = running_count;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行成功-日志数量', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log_report, 
    @level2type = N'COLUMN', @level2name = suc_count;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行失败-日志数量', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_log_report, 
    @level2type = N'COLUMN', @level2name = fail_count;

-- xxl_job_logglue 表字段注释转换为 SQL Server 扩展属性注释

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'任务，主键ID', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_logglue, 
    @level2type = N'COLUMN', @level2name = job_id;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'GLUE类型', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_logglue, 
    @level2type = N'COLUMN', @level2name = glue_type;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'GLUE源代码', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_logglue, 
    @level2type = N'COLUMN', @level2name = glue_source;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'GLUE备注', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_logglue, 
    @level2type = N'COLUMN', @level2name = glue_remark;

-- xxl_job_group 表字段注释转换为 SQL Server 扩展属性注释

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行器AppName', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_group, 
    @level2type = N'COLUMN', @level2name = app_name;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行器名称', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_group, 
    @level2type = N'COLUMN', @level2name = title;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行器地址类型：0=自动注册、1=手动录入', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_group, 
    @level2type = N'COLUMN', @level2name = address_type;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'执行器地址列表，多地址逗号分隔', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_group, 
    @level2type = N'COLUMN', @level2name = address_list;



EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'账号', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_user, 
    @level2type = N'COLUMN', @level2name = username;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'密码', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_user, 
    @level2type = N'COLUMN', @level2name = password;

EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'角色：0-普通用户、1-管理员', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_user, 
    @level2type = N'COLUMN', @level2name = role;


-- xxl_job_user 表字段注释转换为 SQL Server 扩展属性注释
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'权限：执行器ID列表，多个逗号分割', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_user, 
    @level2type = N'COLUMN', @level2name = permission;

-- xxl_job_lock 表字段注释转换为 SQL Server 扩展属性注释
EXEC sp_addextendedproperty 
    @name = N'MS_Description', 
    @value = N'锁名称', 
    @level0type = N'SCHEMA', @level0name = dbo, 
    @level1type = N'TABLE', @level1name = xxl_job_lock, 
    @level2type = N'COLUMN', @level2name = lock_name;

INSERT INTO biz_task (TASK_NAME,CONTENT) VALUES ('定时插入任务','一时负气成今日四海无人对夕阳');
-- unix_timestamp函数将时间转换为时间戳
INSERT INTO USER (USER_NAME,PASSWORD,AGE,AREA,PHONE,REGISTER_DATE,REGISTER_IP) VALUES ('kanyun','123456',10,'北京市',123213,unix_timestamp(NOW()),11111);
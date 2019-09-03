INSERT INTO BIZ_TASK (TASK_NAME,CONTENT) VALUES ('定时插入任务','一时负气成今日四海无人对夕阳');
-- unix_timestamp函数将时间转换为时间戳
INSERT INTO USER (USER_NAME,PASSWORD,AGE,AREA,PHONE,REGISTER_DATE,REGISTER_IP) VALUES ('kanyun','123456',27,'北京市',17127290867,unix_timestamp(NOW()),11111);
INSERT INTO USER (USER_NAME,PASSWORD,AGE,AREA,PHONE,REGISTER_DATE,REGISTER_IP) VALUES ('chenwuwen','123456',28,'郑州市',19965412404,unix_timestamp(NOW()),11111);
INSERT INTO TENANT (TENANT_NAME,SIMPLICITY,DOMAIN,DB_URL,DB_USER,DB_PASS,REGISTER_DATE) VALUES ('苏宁','suning','suning.kanyun.cn','jdbc:mysql://localhost:3306/','root','root',unix_timestamp(NOW()));
INSERT INTO TENANT (TENANT_NAME,SIMPLICITY,DOMAIN,DB_URL,DB_USER,DB_PASS,REGISTER_DATE) VALUES ('物美','wumei','wumei.kanyun.cn','jdbc:mysql://localhost:3306/','root','root',unix_timestamp(NOW()));
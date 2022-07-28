# spring-boot-annotation SpringBoot整合canal实现数据同步
canal的主要用途是基于MySQL数据库增量日志解析，提供增量数据订阅和消费
* canal 模拟 MySQL slave 的交互协议，伪装自己为 MySQL slave ，向 MySQL master 发送dump 协议
* MySQL master 收到 dump 请求，开始推送 binary log 给 slave (即 canal )
* canal 解析 binary log 对象(原始为 byte 流)

## 1.开启MySQL的Binlog,在my.conf中添加配置（MySQL8+默认开启）
```bash
log-bin=mysql-bin # 开启binlog
binlog-format=ROW # 选择ROW模式
server_id=1 # 配置MySQL主节点的serverId，不要和canal的slaveId重复
```
配置完重启mysql，然后看是否开启成功
```bash
mysql> show variables like 'binlog_format';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| binlog_format | ROW   |
+---------------+-------+

mysql> show variables like 'log_bin';
+---------------+-------+
| Variable_name | Value |
+---------------+-------+
| log_bin       | ON    |
+---------------+-------+
```

## 2.授权canal链接MySQL账号具有slave的权限
```bash
-- 创建mysql用户并给slave权限
create user 'canal'@'%' identified by 'canal'; 
grant select, replication slave, replication client on *.* to 'canal'@'%';
-- 刷新权限
flush privileges;
```

# 3.配置canal
1. conf\canal.properties配置文件(这里只介绍我用到的配置)
    ```bash
    # canal-deployer的ip地址，不配置默认是本机
    canal.ip =
    # canal-deployer的端口号
    canal.port = 11111
    # 模式
    canal.serverMode = tcp
    # 加载多个配置文件，需在canal.deployer-1.1.5\conf下创建对应的文件夹，并加上instance.properties配置文件
    canal.destinations = example
    # 配置多个使用英文逗号隔开
    # canal.destinations = example,example2
    ```
2. conf\example\instance.properties配置文件(这里只介绍我用到的配置)
    ```bash
   # 不能与my.ini下的server_id=1相同
   canal.instance.mysql.slaveId=1234
   
   # mysql地址和端口
   canal.instance.master.address=127.0.0.1:3306
   
   # 数据库账号密码，可以使用root账号或刚创建的canal账号
   canal.instance.dbUsername=canal
   canal.instance.dbPassword=canal
   canal.instance.connectionCharset=UTF-8
   
   # 配置监听，支持正则表达式
   canal.instance.filter.regex=spring-boot-elasticsearch\\..*
   # 配置不监听，支持正则表达式
   canal.instance.filter.black.regex=mysql\\.slave_.*
    ```

## MySQL与ES数据同步
部署步骤：

1. 从以下地址下载OpenJDK 11：
    https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz

2. 将第一步下载的tar.gz文件（不要改名字）、scripts/start.sh文件、noinvade-0.0.1.jar
    共三个文件放在同一目录下；

3. 执行start.sh

4. 如果用systemd管理服务，可基于noinvade-webapp.service文件进行修改，写入正确的start.sh所在位置即可。


数据初始化步骤：

1. 工程代码中/src/main/resources/mysql/init.sql里，跑一边即可。

2. 因为初始化事件的数据量可能较大，可以更改init.sql初始化脚本中的时间。这个时间控制从多久开始刷事件。
具体为 “INSERT INTO `event_timestamp` (`id`,`time_stamp`) values(1,1569575934000);”这一行。1569575934000可以改为某一个时间点，网上有各种转换的方式，举例：2019年10月30号为1572364800000。修改id必须为1这一条记录，否则无效。

3. 如果遇到数据需要重新刷的情况，重新跑一边init.sql即可恢复初始值。


权限校验相关：

执行int.sql后，系统中初始内置用户：admin，密码：admin

如需修改密码，或新增用户，需要在start.sh中的启动参数里加入：
-DenableUserManagement=true
并重启应用。启动后，登录用户可以调用新增用户和修改密码的接口。

新增用户接口：
curl -X POST \
  http://localhost:8080/auth/create \
  -H 'Content-Type: application/json' \
  -H 'Cookie: JSESSIONID=登录时取到的Cookie' \
  -d '{"username":"admin2","password":"admin"}'

修改密码接口：
curl -X POST \
  http://localhost:8080/auth/update \
  -H 'Content-Type: application/json' \
  -H 'Cookie: JSESSIONID=登录时取到的Cookie' \
  -d '{"username":"admin","password":"admin"}'
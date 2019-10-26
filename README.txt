部署步骤：

1. 从以下地址下载OpenJDK 11：
    https://download.java.net/java/GA/jdk11/9/GPL/openjdk-11.0.2_linux-x64_bin.tar.gz

2. 将第一步下载的tar.gz文件（不要改名字）、scripts/start.sh文件、noinvade-0.0.1.jar
    共三个文件放在同一目录下；

3. 执行start.sh


数据初始化步骤：

1. 工程代码中/src/main/resources/mysql/init.sql里，跑一边即可。
2. 因为初始化数据量较大，可以更改init.sql初始化脚本中的时间。这个时间控制从多久开始刷设备的事件。
具体为 “INSERT INTO `event_timestamp` (`id`,`time_stamp`) values(1,1569575934000);”这一行。1569575934000可以改为某一个时间点，网上有各种转换的方式，举例：2019年10月30号为1572364800000。
3. 如果遇到数据需要重新刷的情况，重新跑一边init.sql即可恢复初始值。

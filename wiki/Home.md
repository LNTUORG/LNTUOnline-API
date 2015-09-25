# 部署 #

## 环境要求 ##

请确保你拥有以下环境：

- [Maven](http://maven.apache.org) 或者 [Gradle](http://gradle.org)

- [JDK 6](http://www.oracle.com/technetwork/java/javase/downloads/index-jsp-138363.html)

- [Tomcat 8](http://tomcat.apache.org/download-80.cgi) 或者 [Jetty 9](http://www.eclipse.org/jetty) 或者其他容器

- [MySql](http://www.mysql.com) 或者 [MariaDB](https://mariadb.org)

环境的安装不再赘述。

## 修改配置 ##

导入根目录下的“lntuonline.sql”文件到数据库生成表结构 

找到“src/resources/default_config.properties”文件，重命名为“config.properties”

打开config.properties文件配置相关信息：

    firstWeekMonday.2015_1=2015-03-09T00:00:00.000+08:00
    
    secretKey=xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
    
    db.jdbcUrl=jdbc:mysql://localhost:3306/lntuonline
    db.driverClass=com.mysql.jdbc.Driver
    db.username=root
    db.password=123
    
    mail.enable=false
    mail.smtp=SMTP.lntu.org
    mail.from=online@lntu.org
    mail.username=online@lntu.org
    mail.password=123
    
    admin.enable=false
    admin.userId=1000000
    admin.password=123

## 打包部署 ##

Tomcat等容器通常使用war文件来进行部署

在Maven环境下输入以下命令，生成文件在“target/LntuOnline-API.war”

    $ mvn package

在Gradle环境下输入以下命令，生成文件在“build/libs/LntuOnline-API.war”

    $ gradle war

打包过程需要从Maven中心库中下载依赖包，该过程取决于网速（天朝环境），请耐心等待。

war部署方式不再赘述，请参考不同容器文档。

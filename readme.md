数据库文档生成工具
====
    简单配置，就能够生成数据库文档；支持word文档和excel文档的生成。欢迎大家提意见

使用方法  
----
    1.修改application.properties的数据库配置信息  
    2.修改application.properties中的属性 application.generator中的信息
    3.运行Application.java  
    4.生成文件的格式类似2018-03-07_05-45-453.xls

使用的框架  
----
    1.spring boot  
    2.spring jdbc  
    3.freemaker  

支持的数据库 
----
    1.mysql  

如何扩展已支持更多数据库
----
    1.在pom.xml加入数据库驱动包
    2.修改application.properties的数据库配置信息
    3.创建一个新的类，继承cn.wuwenyao.db.doc.generator.dao.impl.AbstractDbInfoDao
    4.在枚举类cn.wuwenyao.db.doc.generator.enums.DbType增加一个新的枚举
    
如何扩展已支持更多的生成文档类型
----
	1.创建一个新的类，继承cn.wuwenyao.db.doc.generator.service.impl.AbstractGeneratorServiceImpl
	2.在枚举类cn.wuwenyao.db.doc.generator.enums.TargetFileType中增加一个新的枚举
	
更新日志
----
	1.v1.0,完成基本架构，支持生成word文档
	2.v1.1,优化架构，支持生成excel文档，比word文档好看点，笑
	
效果图
----
 ![WORD例子](https://gitee.com/shiqiyue/dbDocGenerator/raw/master/doc/example.png "例子")
 ![EXCEL例子-首页](https://gitee.com/shiqiyue/dbDocGenerator/raw/master/doc/excel-index.png "EXCEL例子-首页")
 ![EXCEL例子-表页](https://gitee.com/shiqiyue/dbDocGenerator/raw/master/doc/excel-table.png "EXCEL例子-表页")
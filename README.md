# Kieslect Cloud 项目

## 项目简介
Kieslect Cloud 是一个基于 Spring Boot 和微服务架构的后台管理系统，旨在为智能穿戴设备提供高效、灵活的云端管理解决方案。

---

## 项目结构
- **kieslect-api**  
  用户服务模块，支持新增字段（如 `newSex`）。

- **kieslect-common**  
  公共工具模块，为其他模块提供基础支持。

- **kieslect-gateway**  
  网关模块，支持设备服务接口的自定义表格查询。

- **kieslect-generator**  
  代码生成模块，使用 Git LFS 管理大文件。

- **kieslect-job**  
  定时任务模块，支持分布式任务调度（如 Strava 分布式任务）。

- **kieslect-modules**  
  核心功能模块，存储地理位置等大文件资源。

- **kieslect-oms**  
  OMS 服务模块，提供国家管理和动态版本优化。

- **kieslect-outapi**  
  外部 API 模块，支持 OTA 更新服务。

- **sql**  
  数据库脚本文件夹。

- **.gitattributes**  
  配置 Git LFS 跟踪大文件。

---

## 特性
1. **分布式架构支持**  
   支持高并发分布式任务调度与数据处理。

2. **大文件管理**  
   使用 Git LFS 管理大文件（如 GeoLite2 数据库和 JSON 数据）。

3. **灵活的接口设计**  
   提供高扩展性的 API 接口以支持智能穿戴设备需求。

4. **多模块化设计**  
   各模块功能独立，便于维护与扩展。

---

## 环境要求
- JDK 17+
- Maven 3.8+
- Git LFS

---

## 使用指南
1. 克隆项目：
   ```bash
   git clone https://github.com/axiong5500/Kieslect-Cloud.git
   cd Kieslect-Cloud
   ```
2. 安装依赖：   
   ```bash
   mvn clean install
   ```
3. 配置数据库： 将 sql 文件夹中的脚本导入到你的数据库。
4. 启动服务：
   ```bash
     mvn spring-boot:run 
   ```
   
## 注意事项
1. Git LFS 

   本项目包含大文件（如 GeoLite2 数据库），请确保安装并启用了 Git LFS：
   ```bash
   git lfs install
   ```
2. 隐私配置

   推送代码时，请避免泄露私人邮箱，建议使用 GitHub 提供的匿名邮箱。

## 作者
**Kieslect Cloud 团队**

专注智能穿戴设备云端管理解决方案。






---


#### Spring Cloud 微服务 启动失败常见问题

##### 问题 1: `BeanCreationException` - 数据源初始化失败
**描述**: 启动应用时出现以下错误：
``` java
2024-11-11 11:37:29.269 [main] WARN  o.s.b.w.s.c.AnnotationConfigServletWebServerApplicationContext - [refresh,592] - Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'stravaOAuthController': Unsatisfied dependency expressed through field 'stravaTokenInfoService': Error creating bean with name 'stravaTokenInfoServiceImpl': Unsatisfied dependency expressed through field 'baseMapper': Error creating bean with name 'stravaTokenInfoMapper' defined in file [D:\IdeaProjects\Kieslect-Cloud\kieslect-modules\kieslect-auth\target\classes\com\kieslect\auth\mapper\StravaTokenInfoMapper.class]: Unsatisfied dependency expressed through bean property 'sqlSessionFactory': Error creating bean with name 'sqlSessionFactory' defined in class path resource [com/baomidou/mybatisplus/autoconfigure/MybatisPlusAutoConfiguration.class]: Unsatisfied dependency expressed through method 'sqlSessionFactory' parameter 0: Error creating bean with name 'dataSource' defined in class path resource [org/springframework/boot/autoconfigure/jdbc/DataSourceConfiguration$Hikari.class]: Failed to instantiate [com.zaxxer.hikari.HikariDataSource]: Factory method 'dataSource' threw exception with message: Failed to determine a suitable driver class
2024-11-11 11:37:29.270 [main] INFO  o.a.c.c.StandardService - [log,173] - Stopping service [Tomcat]
2024-11-11 11:37:29.293 [main] ERROR o.s.b.d.LoggingFailureAnalysisReporter - [report,40] - 
2024-11-11 11:37:29.293 [main] ERROR o.s.b.d.LoggingFailureAnalysisReporter - [report,40] - 

***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
	If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (the profiles prod are currently active).
```
**原因**: 数据源的驱动类没有正确配置。

**解决方法**:

1. 确保pom已经添加一下引入
    ```properties
    <!-- Kieslect Common Datasource-->
    <dependency>
        <groupId>com.kieslect</groupId>
        <artifactId>kieslect-common-datasource</artifactId>
    </dependency>
   ```

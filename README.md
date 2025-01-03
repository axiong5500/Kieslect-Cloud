## Spring Cloud 微服务 启动失败常见问题

### 问题 1: `BeanCreationException` - 数据源初始化失败
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

该plugin有两种实现

### 默认访问日志记录Filter
`org.codelogger.plugin.log.filter.DefaultLogFilter`，如果使用该Filter，将包`org.codelogger.plugin.log`的日志级别设置为debug，则会默认打印出访问请求及响应请求，一般用于REST工程调试，输出结果如:

> Received POST to /init/init.do with queryString:userId=10004&lat=1&lon=1&os=ios&deviceId=1 and parameterMap:{"userId":"10004","lat":"1","lon":"1","os":"ios","deviceId":"1"} and requestBody:null from ip:127.0.0.1, responseBody:{"code":0,"msg":"success","ts":1520174569158,"data":{"appConfig":null,"ossConfig":null,"token":"ac399088a4764c7e91dead3080eb35e9"}}

当然，也可以自己实现一个Filter，使用`org.codelogger.plugin.log.core.AbstractLogProcessor`来处理请求，得到处理后的结果，自定义日志处理。

### 默认请求记录Filter
`org.codelogger.plugin.log.filter.DefaultAccessRecordFilter#recordAccessLog`，如果使用该Filter，将包`org.codelogger.plugin.log`的日志级别设置为debug，则会默认打印出访问请求，一般用于后台管理请求记录，输出结果如:

> Received POST to /demo/cats/save.do with queryString:null and parameterMap:{"id":"","name":"李四","age":"22","saleStatus":"FOR_SALE","status":"ALIVE"} and requestBody:null from ip:127.0.0.1 

当然，也可以自己实现一个Filter，继承于`org.codelogger.plugin.log.filter.AbstractAccessRecordFilter`，实现`org.codelogger.plugin.log.filter.AbstractAccessRecordFilter#recordAccessLog`方法，来处理访问请求的自定义记录。

> **注意:`AbstractAccessRecordFilter`只会处理请求行为，而不会处理响应行为**

---
maven下载:
`<dependency>
    <groupId>org.codelogger.plugin</groupId>
    <artifactId>log</artifactId>
    <version>1.0.0</version>
</dependency>
`

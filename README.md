# springboot-web 常用注解

* @Profile("prod")
该注解修饰的类只能在指定的环境当中被实例化，如果你用这个注解修饰了一个Service
那么该Service只有prod(生产环境下才被实例化)
* @PathVariable
```java
@RestController
@RequestMapping("/user")
public class PathLearCotroller {
    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        // 处理"/users/{id}"的GET请求，用来获取url中id值的User信息
        // url中的id可通过@PathVariable绑定到函数的参数中
        return users.get(id);
    }
}
```
@Qualifier  
@Autowired是根据类型进行自动装配的。如果当spring上下文中存在不止一个UserDao类型的bean时，就会抛出BeanCreationException异常;如果Spring上下文中不存在UserDao类型的bean，也会抛出BeanCreationException异常。我们可以使用@Qualifier配合@Autowired来解决这些问题。如下
```java
@Service("service")
public class EmployeeServiceImpl implements EmployeeService {
    public EmployeeDto getEmployeeById(Long id) {
        return new EmployeeDto();
    }
}

@Service("service1")
public class EmployeeServiceImpl1 implements EmployeeService {
    public EmployeeDto getEmployeeById(Long id) {
        return new EmployeeDto();
    }
}

@Controller
@RequestMapping("/emplayee.do")
public class EmployeeInfoControl {
    
    @Autowired
    @Qualifier("service")
    EmployeeService employeeService;
    
    @RequestMapping(params = "method=showEmplayeeInfo")
    public void showEmplayeeInfo(HttpServletRequest request, HttpServletResponse response, EmployeeDto dto) {
      
    }
}
```
@ModelAttribute
@SessionAttributes

# springboot-web 多环境的配置
resource 目录下新建三个文件，分别为application-dev.properties：开发环境 application-test.properties：测试环境 
application-prod.properties：生产环境,然后在application.propperties文件中添加
spring.profiles.active=dev（默认激活application-dev.properties配置）

# springboot-web 日志输出控制
* 不同日志系统文件配置

由于日志服务一般都在ApplicationContext创建前就初始化了，它并不是必须通过Spring的配置文件控制。因此通过系统属性和传统的Spring Boot外部配置文件依然可以很好的支持日志控制和管理。
根据不同的日志系统，你可以按如下规则组织配置文件名，就能被正确加载：  
Logback：logback-spring.xml, logback-spring.groovy, logback.xml, logback.groovy
Log4j：log4j-spring.properties, log4j-spring.xml, log4j.properties, log4j.xml
Log4j2：log4j2-spring.xml, log4j2.xml
JDK (Java Util Logging)：logging.properties  
Spring Boot官方推荐优先使用带有-spring的文件名作为你的日志配置（如使用logback-spring.xml，而不是logback.xml）

* 多彩输出  

如果你的终端支持ANSI，设置彩色输出会让日志更具可读性。通过在application.properties中设置spring.output.ansi.enabled参数来支持
NEVER：禁用ANSI-colored输出（默认项）  
DETECT：会检查终端是否支持ANSI，是的话就采用彩色输出（推荐项）  
ALWAYS：总是使用ANSI-colored格式输出，若终端不支持的时候，会有很多干扰信息，不推荐使用

* 文件输出

Spring Boot默认配置只会输出到控制台，并不会记录到文件中，但是我们通常生产环境使用时都需要以文件方式记录 
若要增加文件输出，需要在application.properties中配置logging.file或logging.path属性 
logging.file，设置文件，可以是绝对路径，也可以是相对路径。如：logging.file=my.log
logging.path，设置目录，会在该目录下创建spring.log文件，并写入日志内容，如：logging.path=/var/log   
日志文件会在10Mb大小的时候被截断，产生新的日志文件，默认级别为：ERROR、WARN、INFO 

  
* 级别控制

在Spring Boot中只需要在application.properties中进行配置完成日志记录的级别控制。  
配置格式：logging.level.*=LEVEL  
logging.level：日志级别控制前缀，*为包名或Logger名 
LEVEL：选项TRACE, DEBUG, INFO, WARN, ERROR, FATAL, OFF   
举例：   
logging.level.com.didispace=DEBUG：com.didispace包下所有class以DEBUG级别输出 
logging.level.root=WARN：root日志以WARN级别输出  
还有一种配置方式 ：在resources目录下新建logback.xml，请注意这个文件不用在其他地方指定，spirng根据这个文件名读取配置
信息
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <include resource="org/springframework/boot/logging/logback/base.xml" />
    <logger name="org.springframework.web" level="INFO"/>
    <logger name="org.springboot.sample" level="TRACE" />
   
    <!-- 测试环境+开发环境. 多个使用逗号隔开. -->
     <springProfile name="test,dev">
        <logger name="org.springframework.web" level="INFO"/>
        <logger name="org.springboot.sample" level="INFO" />
        <logger name="com.kfit" level="info" />
    </springProfile>
 
   
    <!-- 生产环境. -->
    <springProfile name="prod">
        <logger name="org.springframework.web" level="ERROR"/>
        <logger name="org.springboot.sample" level="ERROR" />
        <logger name="com.kfit" level="ERROR" />
    </springProfile>
   
</configuration>
```
这个base.xml是Spring Boot的日志系统预先定义了一些系统变量：  
PID，当前进程ID{LOG_FILE}，Spring Boot配置文件（application.properties|.yml）中logging.file的值，${LOG_PATH}, Spring Boot配置文件中logging.path的值 
同时默认情况下包含另个appender——一个是控制台，一个是文件，分别定义在console-appender.xml和file-appender.xml中。同时对于应用的日志级别也可以通过application.properties进行定义：  
如果在 logback.xml 和 application.properties 中定义了相同的配置（如都配置了 org.springframework.web）但是输出级别不同，则实际上 application.properties 的优先级高于 logback.xml       

* 自定义输出格式   

日志一般包含时间，输出级别，方法，类全名，日志内容 自定义输出输出指的的是对上述这些的样式  
进行控制，比如你可以指定时间输出格式之类的
在Spring Boot中可以通过在application.properties配置如下参数控制输出格式： 
logging.pattern.console：定义输出到控制台的样式（不支持JDK Logger）  
logging.pattern.file：定义输出到文件的样式（不支持JDK Logger）

* debug模式

application.properties文件中添加debug=true;debug模式下会输出更多的日志

* 编写测试

要添加 testCompile('org.springframework.boot:spring-boot-starter-test')  
```java
/**
 * Created by Administrator on 12/20/2016.
 * 要添加 testCompile('org.springframework.boot:spring-boot-starter-test')
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest  extends MockMvcResultMatchers {

    //模拟mvc对象类.
    private MockMvc mvc;

    @Before
    public void setup(){
       /*
        * MockMvcBuilders使用构建MockMvc对象.
        */
        mvc = MockMvcBuilders.standaloneSetup(new UserCotroller()).build();
    }

    @Test
    public void testUserController() throws Exception{
        RequestBuilder request = null;
        //1. get 以下user列表，应该为空》

        //1、构建一个get请求.
        request = MockMvcRequestBuilders.get("/users");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("[]"))
        ;
        System.out.println("UserControllerTest.testUserController().get");

        // 2、post提交一个user
        request = MockMvcRequestBuilders.post("/users")
                .param("id","1")
                .param("name","林峰")
                .param("age","20")
        ;


        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string("success"));

        // 3、get获取user列表，应该有刚才插入的数据
        request = MockMvcRequestBuilders.get("/users");
        mvc.perform(request).andExpect(status().isOk()).andExpect(content().string("[{\"id\":1,\"name\":\"林峰\",\"age\":20}]"));


        // 4、put修改id为1的user
        request = MockMvcRequestBuilders.put("/users/1")
                .param("name", "林则徐")
                .param("age", "30");
        mvc.perform(request)
                .andExpect(content().string("success"));

        // 5、get一个id为1的user
        request = MockMvcRequestBuilders.get("/users/1");
        mvc.perform(request)
                .andExpect(content().string("{\"id\":1,\"name\":\"林则徐\",\"age\":30}"));



        // 6、del删除id为1的user
        request = MockMvcRequestBuilders.delete("/users/1");
        mvc.perform(request)
                .andExpect(content().string("success"));

        // 7、get查一下user列表，应该为空
        request = MockMvcRequestBuilders.get("/users");
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

    }
}
```
* 多数据源配置

# springboot所有可配置属性集合
http://docs.spring.io/spring-boot/docs/1.4.0.RELEASE/reference/htmlsingle/#common-application-properties



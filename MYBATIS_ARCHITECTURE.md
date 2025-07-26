# ForzaTune.PRO 纯MyBatis架构说明

## 架构概述

本项目采用纯MyBatis架构，完全移除了JPA依赖，实现了更直接、更高效的数据库访问层。

## 架构优势

### 1. 更好的SQL控制
- 可以精确控制SQL语句，优化性能
- 支持复杂的动态SQL查询
- 避免JPA的抽象层开销

### 2. 更直接的数据库操作
- 减少ORM框架的额外开销
- 更简单的学习曲线
- 团队更容易理解和维护

### 3. 更灵活的查询
- 支持复杂的多表关联查询
- 动态SQL条件构建
- 自定义结果映射

## 项目结构

```
src/main/java/com/forzatune/backend/
├── entity/                              # 实体类（纯POJO）
├── mapper/                              # MyBatis Mapper接口
├── service/                             # 业务逻辑层
├── controller/                          # 控制器层
└── handler/                             # 类型处理器

src/main/resources/
├── mapper/                              # MyBatis XML映射文件
└── schema.sql                          # 数据库初始化脚本
```

## 核心组件

### 1. Entity（实体类）
- 纯POJO类，不包含JPA注解
- 使用Lombok简化代码
- 支持JSON序列化/反序列化

### 2. Mapper接口
- 定义数据库操作方法
- 使用@Mapper注解标记
- 方法名对应XML中的SQL ID

### 3. XML映射文件
- 定义具体的SQL语句
- 支持动态SQL
- 自定义结果映射

### 4. Service层
- 业务逻辑封装
- 事务管理
- 调用Mapper接口

### 5. Controller层
- REST API接口
- 参数验证
- 调用Service层

## 数据访问流程

```
Controller → Service → Mapper → XML → Database
```

### 示例流程

1. **Controller接收请求**
```java
@GetMapping("/users/{id}")
public ResponseEntity<User> getUserById(@PathVariable String id) {
    User user = userService.findById(id);
    return ResponseEntity.ok(user);
}
```

2. **Service处理业务逻辑**
```java
public User findById(String id) {
    return userMapper.selectById(id);
}
```

3. **Mapper接口定义**
```java
@Mapper
public interface UserMapper {
    User selectById(@Param("id") String id);
}
```

4. **XML映射文件实现**
```xml
<select id="selectById" resultMap="UserResultMap">
    SELECT * FROM users WHERE id = #{id} AND is_active = 1
</select>
```

## 配置说明

### application.yml配置
```yaml
mybatis:
  mapper-locations: classpath:mapper/*.xml
  type-aliases-package: com.forzatune.backend.entity
  configuration:
    map-underscore-to-camel-case: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

### 主启动类配置
```java
@SpringBootApplication
@MapperScan("com.forzatune.backend.mapper")
public class ForzaTuneProBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForzaTuneProBackendApplication.class, args);
    }
}
```

## 特殊功能

### 1. JSON类型处理器
```java
@MappedTypes({List.class})
public class JsonTypeHandler extends BaseTypeHandler<List<String>> {
    // 处理数据库中的JSON字段
}
```

### 2. 动态SQL
```xml
<select id="searchUsers" resultMap="UserResultMap">
    SELECT * FROM users WHERE 1=1
    <if test="keyword != null and keyword != ''">
        AND (gamertag LIKE CONCAT('%', #{keyword}, '%') 
             OR email LIKE CONCAT('%', #{keyword}, '%'))
    </if>
    <if test="isProPlayer != null">
        AND is_pro_player = #{isProPlayer}
    </if>
</select>
```

### 3. 复杂结果映射
```xml
<resultMap id="TeamResultMap" type="com.forzatune.backend.entity.Team">
    <id property="id" column="id"/>
    <result property="name" column="name"/>
    <association property="stats" javaType="com.forzatune.backend.entity.Team$TeamStats">
        <result property="totalTunes" column="total_tunes"/>
        <result property="totalDownloads" column="total_downloads"/>
    </association>
</resultMap>
```

## 性能优化

### 1. 数据库索引
```sql
CREATE INDEX idx_tunes_car_id ON tunes(car_id);
CREATE INDEX idx_tunes_author_id ON tunes(author_id);
CREATE INDEX idx_tunes_is_pro_tune ON tunes(is_pro_tune);
```

### 2. 连接池配置
```yaml
spring:
  datasource:
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      connection-timeout: 30000
```

### 3. 查询优化
- 使用分页查询处理大数据量
- 避免N+1查询问题
- 合理使用缓存

## 事务管理

### 1. 声明式事务
```java
@Service
@Transactional
public class UserService {
    // 方法自动具有事务性
}
```

### 2. 编程式事务
```java
@Transactional
public void complexOperation() {
    // 复杂业务逻辑
    userMapper.insert(user);
    teamMapper.insert(team);
    // 如果任何操作失败，整个事务回滚
}
```

## 错误处理

### 1. 统一异常处理
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
```

### 2. 业务异常
```java
public User findById(String id) {
    User user = userMapper.selectById(id);
    if (user == null) {
        throw new RuntimeException("User not found");
    }
    return user;
}
```

## 测试

### 1. 单元测试
```java
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    
    @Test
    void testFindById() {
        User user = userService.findById("user1");
        assertNotNull(user);
    }
}
```

### 2. 集成测试
```java
@SpringBootTest
@AutoConfigureTestDatabase
class UserControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void testGetUser() {
        ResponseEntity<User> response = restTemplate.getForEntity("/api/users/user1", User.class);
        assertEquals(200, response.getStatusCodeValue());
    }
}
```

## 总结

通过采用纯MyBatis架构，我们实现了：

1. **更好的性能** - 减少ORM框架开销
2. **更简单的维护** - 直接的SQL控制
3. **更灵活的查询** - 支持复杂业务需求
4. **更好的学习曲线** - 团队更容易上手

这种架构特别适合需要精确控制SQL和性能优化的项目，为ForzaTune.PRO提供了稳定、高效的数据访问层。 
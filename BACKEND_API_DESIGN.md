# ForzaTune.PRO 后端接口设计文档

## 枚举值规范

### 调校倾向 (Preference)
- `Power` - 动力优先
- `Handling` - 操控优先  
- `Balance` - 平衡

### 路面条件 (Surface Condition)
- `Dry` - 干燥
- `Wet` - 潮湿
- `Snow` - 雪地

### 驱动形式 (Drivetrain)
- `FWD` - 前轮驱动
- `RWD` - 后轮驱动
- `AWD` - 全轮驱动

### 轮胎化合物 (Tire Compound)
- `Stock` - 原厂胎
- `Street` - 街道胎
- `Sport` - 运动胎
- `Semi-Slick` - 半光头胎
- `Slick` - 光头胎
- `Rally` - 拉力胎
- `Snow` - 雪地胎
- `Off-Road` - 越野胎
- `Drag` - 直线加速胎
- `Drift` - 漂移胎

### 差速器类型 (Differential Type)
- `Stock` - 原厂差速器
- `Street` - 街道差速器
- `Sport` - 运动差速器
- `Off-Road` - 越野差速器
- `Rally` - 拉力差速器
- `Drift` - 漂移差速器

### 变速箱档位 (Transmission Speeds)
- `6` - 6速
- `7` - 7速
- `8` - 8速
- `9` - 9速

### PI等级 (PI Class)
- `X` - X级
- `S2` - S2级
- `S1` - S1级
- `A` - A级
- `B` - B级
- `C` - C级
- `D` - D级

### 比赛类型 (Race Type)
- `Road` - 公路赛
- `Dirt` - 泥地赛
- `Cross Country` - 越野赛

## 概述

本文档定义了 ForzaTune.PRO 前端应用所需的后端 API 接口。接口采用 RESTful 设计风格，使用 JSON 格式进行数据交换。

## 基础信息

- **基础URL**: `https://api.forzatune.pro`
- **认证方式**: JWT Token (Bearer Token)
- **数据格式**: JSON
- **字符编码**: UTF-8

## 技术栈说明

### 后端技术栈
- **框架**: Spring Boot 2.7+ / 3.0+
- **数据库**: MySQL 8.0+
- **安全框架**: Spring Security + JWT
- **JWT库**: jjwt (io.jsonwebtoken)
- **构建工具**: Maven / Gradle

### JWT 在 Spring Boot 中的支持
Spring Boot 对 JWT 有完整的支持：
- ✅ Spring Security 内置 JWT 支持
- ✅ 成熟的 JWT 库生态
- ✅ 自动配置和依赖注入
- ✅ 与 MySQL 数据库完美集成

## 开发环境配置

### Spring Boot 环境变量配置
```bash
# 开发环境 - 跳过token验证
AUTH_SKIP_VERIFICATION=true

# 生产环境 - 严格token验证
AUTH_SKIP_VERIFICATION=false

# JWT 密钥
JWT_SECRET=your-super-secret-jwt-key-here

# Mock Token (仅开发环境有效)
MOCK_TOKEN=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.development

# 数据库配置
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/forzatune_pro
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=password
```

### 接口认证要求
- **公开接口**：无需token验证
- **私有接口**：必须提供有效的JWT token
- **调试接口**：开发环境可跳过验证

## 通用响应格式

### 成功响应
```json
{
  "success": true,
  "data": {},
  "message": "操作成功"
}
```

### 错误响应
```json
{
  "success": false,
  "error": {
    "code": "ERROR_CODE",
    "message": "错误描述",
    "details": {}
  }
}
```

### 分页响应
```json
{
  "success": true,
  "data": {
    "items": [],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 100,
      "totalPages": 5,
      "hasNext": true,
      "hasPrev": false
    }
  }
}
```

## Spring Boot 认证实现

### Maven 依赖配置
```xml
<dependencies>
    <!-- Spring Boot Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Security -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- JWT 库 -->
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-api</artifactId>
        <version>0.11.5</version>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-impl</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    <dependency>
        <groupId>io.jsonwebtoken</groupId>
        <artifactId>jjwt-jackson</artifactId>
        <version>0.11.5</version>
        <scope>runtime</scope>
    </dependency>
    
    <!-- MySQL 驱动 -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- MyBatis 或 JPA -->
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.2.2</version>
    </dependency>
</dependencies>
```

### JWT 工具类
```java
@Component
public class JwtTokenUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration}")
    private Long expiration;
    
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }
    
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }
    
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
    
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
```

### JWT 认证过滤器
```java
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Value("${auth.skip-verification:false}")
    private boolean skipVerification;
    
    @Value("${auth.mock-token:}")
    private String mockToken;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        // 开发环境跳过验证
        if (skipVerification) {
            SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("mock_user", null, new ArrayList<>())
            );
            filterChain.doFilter(request, response);
            return;
        }
        
        final String authorizationHeader = request.getHeader("Authorization");
        
        String username = null;
        String jwt = null;
        
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            
            // Mock token 验证（仅开发环境）
            if (!mockToken.isEmpty() && jwt.equals(mockToken)) {
                SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken("mock_user", null, new ArrayList<>())
                );
                filterChain.doFilter(request, response);
                return;
            }
            
            try {
                username = jwtTokenUtil.extractUsername(jwt);
            } catch (Exception e) {
                // Token 无效，继续处理
            }
        }
        
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            
            if (jwtTokenUtil.validateToken(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        
        filterChain.doFilter(request, response);
    }
}
```

### Spring Security 配置
```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtAuthFilter;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests()
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                .requestMatchers("/api/games", "/api/home/dashboard").permitAll()
                .requestMatchers("/api/cars/search", "/api/cars/*").permitAll()
                .requestMatchers("/api/tunes/*", "/api/tunes/*/comments").permitAll()
                .anyRequest().authenticated()
            .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

### 应用配置 (application.yml)
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/forzatune_pro
    username: ${SPRING_DATASOURCE_USERNAME:root}
    password: ${SPRING_DATASOURCE_PASSWORD:password}
    driver-class-name: com.mysql.cj.jdbc.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true

jwt:
  secret: ${JWT_SECRET:your-super-secret-jwt-key-here}
  expiration: 86400 # 24小时

auth:
  skip-verification: ${AUTH_SKIP_VERIFICATION:false}
  mock-token: ${MOCK_TOKEN:}

logging:
  level:
    com.forzatune: DEBUG
    org.springframework.security: DEBUG
```

## 认证相关接口

**接口**: `POST /api/auth/login`

**描述**: 用户登录获取访问令牌

**请求参数**:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "refresh_token_here",
    "expiresIn": 3600,
    "user": {
      "id": "user_123",
      "email": "user@example.com",
      "gamertag": "SpeedyRacer",
      "isProPlayer": false,
      "hasLinkedXboxId": true,
      "avatarUrl": "https://example.com/avatar.jpg",
      "createdAt": "2024-01-01T00:00:00Z"
    }
  }
}
```

### 2. 用户注册

**接口**: `POST /api/auth/register`

**描述**: 新用户注册

**请求参数**:
```json
{
  "email": "newuser@example.com",
  "password": "password123",
  "gamertag": "NewRacer"  // 可选
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": "user_456",
      "email": "newuser@example.com",
      "gamertag": "NewRacer",
      "isProPlayer": false,
      "hasLinkedXboxId": false,
      "createdAt": "2024-01-01T00:00:00Z"
    }
  }
}
```

### 3. 用户登出

**接口**: `POST /api/auth/logout`

**描述**: 用户登出，使当前token失效

**请求头**: `Authorization: Bearer <token>`

**响应**:
```json
{
  "success": true,
  "message": "登出成功"
}
```

### 4. 获取用户信息

**接口**: `GET /api/auth/profile`

**描述**: 获取当前登录用户的详细信息

**请求头**: `Authorization: Bearer <token>`

**响应**:
```json
{
  "success": true,
  "data": {
    "user": {
      "id": "user_123",
      "email": "user@example.com",
      "gamertag": "SpeedyRacer",
      "isProPlayer": true,
      "hasLinkedXboxId": true,
      "avatarUrl": "https://example.com/avatar.jpg",
      "totalTunes": 45,
      "totalLikes": 1234,
      "proPlayerSince": "2023-06-15",
      "createdAt": "2022-03-10T00:00:00Z",
      "bio": "专业赛车调教师，专注于高性能车辆调校。"
    }
  }
}
```

## 游戏相关接口

### 5. 获取支持的游戏列表

**接口**: `GET /api/games`

**描述**: 获取系统支持的游戏列表

**响应**:
```json
{
  "success": true,
  "data": {
    "games": [
      {
        "id": "fh5",
        "name": "Forza Horizon 5",
        "displayName": "极限竞速：地平线5",
        "displayNameEn": "Forza Horizon 5",
        "isActive": true,
        "releaseDate": "2021-11-09",
        "coverImage": "https://example.com/fh5-cover.jpg"
      },
      {
        "id": "fm",
        "name": "Forza Motorsport",
        "displayName": "极限竞速",
        "displayNameEn": "Forza Motorsport",
        "isActive": true,
        "releaseDate": "2023-10-10",
        "coverImage": "https://example.com/fm-cover.jpg"
      }
    ]
  }
}
```

## 车辆相关接口

### 6. 车辆搜索

**接口**: `GET /api/cars/search`

**描述**: 根据关键词搜索车辆

**请求参数**:
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| query | string | 是 | 搜索关键词 |
| game | string | 否 | 游戏版本 (fh5/fm) |
| limit | number | 否 | 返回数量限制 (默认10) |
| offset | number | 否 | 分页偏移 (默认0) |
| category | string | 否 | 车辆类别过滤 |
| manufacturer | string | 否 | 制造商过滤 |

**响应**:
```json
{
  "success": true,
  "data": {
    "cars": [
      {
        "id": "car_123",
        "name": "Supra",
        "manufacturer": "Toyota",
        "year": 2020,
        "category": "Sports Cars",
        "pi": 800,
        "drivetrain": "RWD",
        "imageUrl": "https://example.com/supra.jpg",
        "tuneCount": 156,
        "viewCount": 2340
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 10,
      "total": 45,
      "totalPages": 5,
      "hasNext": true,
      "hasPrev": false
    }
  }
}
```

### 7. 获取车辆详情

**接口**: `GET /api/cars/{carId}`

**描述**: 获取指定车辆的详细信息

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| carId | string | 车辆ID |

**响应**:
```json
{
  "success": true,
  "data": {
    "car": {
      "id": "car_123",
      "name": "Supra",
      "manufacturer": "Toyota",
      "year": 2020,
      "category": "Sports Cars",
      "pi": 800,
      "drivetrain": "RWD",
      "imageUrl": "https://example.com/supra.jpg",
      "description": "经典跑车，性能卓越",
      "specs": {
        "engine": "3.0L I6 Twin-Turbo",
        "power": 340,
        "torque": 495,
        "weight": 1540,
        "topSpeed": 250
      }
    },
    "tuneStats": {
      "totalTunes": 156,
      "proTunes": 23,
      "averageRating": 4.2,
      "mostPopularTune": {
        "id": "tune_456",
        "shareCode": "123 456 789",
        "likeCount": 89
      }
    }
  }
}
```

### 8. 记录车辆查看

**接口**: `POST /api/cars/{carId}/view`

**描述**: 记录车辆查看次数（用于热门车辆统计）

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| carId | string | 车辆ID |

**响应**:
```json
{
  "success": true,
  "message": "查看记录已保存"
}
```

## 调校相关接口

### 9. 获取调校详情

**接口**: `GET /api/tunes/{tuneId}`

**描述**: 获取指定调校的详细信息

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| tuneId | string | 调校ID |

**响应**:
```json
{
  "success": true,
  "data": {
    "tune": {
      "id": "tune_123",
      "shareCode": "123 456 789",
      "carId": "car_123",
      "carName": "2020 Toyota Supra",
      "authorId": "user_123",
      "authorGamertag": "SpeedyRacer",
      "isProTune": true,
      "preference": "Handling",
      "piClass": "S1",
      "finalPI": 800,
      "drivetrain": "RWD",
      "tireCompound": "Sport",
      "surfaceConditions": ["Dry", "Wet"],
      "description": "专注于操控性的调校设置",
      "likeCount": 89,
      "favoriteCount": 23,
      "downloadCount": 156,
      "createdAt": "2024-01-01T10:00:00Z",
      "updatedAt": "2024-01-01T10:00:00Z",
      "parameters": {
        "transmissionSpeeds": 6,
        "finalDrive": 3.42,
        "gear1Ratio": 3.36,
        "gear2Ratio": 2.17,
        "gear3Ratio": 1.51,
        "gear4Ratio": 1.13,
        "gear5Ratio": 0.89,
        "gear6Ratio": 0.71,
        "differentialType": "Sport",
        "frontAcceleration": 50,
        "frontDeceleration": 50,
        "rearAcceleration": 50,
        "rearDeceleration": 50,
        "frontTirePressure": 2.2,
        "rearTirePressure": 2.0,
        "frontCamber": -1.5,
        "rearCamber": -1.0,
        "frontCaster": 5.0,
        "frontToe": 0.0,
        "rearToe": 0.0,
        "frontAntiRollBar": 20,
        "rearAntiRollBar": 15,
        "frontSprings": 400,
        "rearSprings": 350,
        "frontDamping": 8,
        "rearDamping": 7,
        "frontBump": 6,
        "rearBump": 5,
        "frontRebound": 10,
        "rearRebound": 9,
        "frontArb": 20,
        "rearArb": 15,
        "brakeForce": 100,
        "brakeBalance": 50
      },
      "lapTimes": [
        {
          "trackId": "track_123",
          "trackName": "Goliath",
          "time": "10:23.456",
          "date": "2024-01-01T10:00:00Z"
        }
      ]
    },
    "userInteraction": {
      "isLiked": true,
      "isFavorited": false,
      "favoriteNote": null
    }
  }
}
```

### 10. 获取调校评论

**接口**: `GET /api/tunes/{tuneId}/comments`

**描述**: 获取指定调校的评论列表

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| tuneId | string | 调校ID |

**请求参数**:
| 参数 | 类型 | 必填 | 描述 |
|------|------|------|------|
| page | number | 否 | 页码 (默认1) |
| limit | number | 否 | 每页数量 (默认20) |

**响应**:
```json
{
  "success": true,
  "data": {
    "comments": [
      {
        "id": "comment_123",
        "content": "这个调校真的很棒，操控性提升明显！",
        "authorId": "user_456",
        "authorGamertag": "RacingFan",
        "authorAvatar": "https://example.com/avatar.jpg",
        "likeCount": 5,
        "createdAt": "2024-01-01T12:00:00Z",
        "replies": [
          {
            "id": "reply_123",
            "content": "同意，我也觉得很好用",
            "authorId": "user_789",
            "authorGamertag": "SpeedDemon",
            "likeCount": 2,
            "createdAt": "2024-01-01T13:00:00Z"
          }
        ]
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 45,
      "totalPages": 3,
      "hasNext": true,
      "hasPrev": false
    }
  }
}
```

### 11. 调校点赞/取消点赞

**接口**: `POST /api/tunes/{tuneId}/like`

**描述**: 点赞或取消点赞调校

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| tuneId | string | 调校ID |

**请求头**: `Authorization: Bearer <token>`

**响应**:
```json
{
  "success": true,
  "data": {
    "liked": true,
    "likeCount": 90
  }
}
```

### 12. 调校收藏/取消收藏

**接口**: `POST /api/tunes/{tuneId}/favorite`

**描述**: 收藏或取消收藏调校

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| tuneId | string | 调校ID |

**请求头**: `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "note": "我的收藏备注"  // 可选
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "favorited": true
  }
}
```

### 13. 记录调校下载

**接口**: `POST /api/tunes/{tuneId}/download`

**描述**: 记录调校下载次数

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| tuneId | string | 调校ID |

**响应**:
```json
{
  "success": true,
  "message": "下载记录已保存"
}
```

### 14. 评论点赞

**接口**: `POST /api/comments/{commentId}/like`

**描述**: 点赞评论

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| commentId | string | 评论ID |

**请求头**: `Authorization: Bearer <token>`

**响应**:
```json
{
  "success": true,
  "data": {
    "likeCount": 6
  }
}
```

### 15. 回复评论

**接口**: `POST /api/comments/{commentId}/replies`

**描述**: 回复指定评论

**路径参数**:
| 参数 | 类型 | 描述 |
|------|------|------|
| commentId | string | 评论ID |

**请求头**: `Authorization: Bearer <token>`

**请求参数**:
```json
{
  "content": "回复内容"
}
```

**响应**:
```json
{
  "success": true,
  "data": {
    "reply": {
      "id": "reply_456",
      "content": "回复内容",
      "authorId": "user_123",
      "authorGamertag": "SpeedyRacer",
      "likeCount": 0,
      "createdAt": "2024-01-01T14:00:00Z"
    }
  }
}
```

## 首页数据接口

### 16. 获取首页展示数据

**接口**: `GET /api/home/dashboard`

**描述**: 获取首页需要的所有展示数据

**响应**:
```json
{
  "success": true,
  "data": {
    "popularCars": [
      {
        "id": "car_123",
        "name": "Supra",
        "manufacturer": "Toyota",
        "year": 2020,
        "category": "Sports Cars",
        "pi": 800,
        "imageUrl": "https://example.com/supra.jpg",
        "tuneCount": 156,
        "viewCount": 2340
      }
    ],
    "recentTunes": [
      {
        "id": "tune_123",
        "shareCode": "123 456 789",
        "carName": "2020 Toyota Supra",
        "authorGamertag": "SpeedyRacer",
        "isProTune": false,
        "preference": "Handling",
        "piClass": "S1",
        "finalPI": 800,
        "likeCount": 89,
        "createdAt": "2024-01-01T10:00:00Z",
        "bestLapTime": "10:23.456"
      }
    ],
    "proTunes": [
      {
        "id": "tune_456",
        "shareCode": "987 654 321",
        "carName": "2021 BMW M3",
        "authorGamertag": "ProRacer",
        "preference": "Power",
        "piClass": "S2",
        "finalPI": 900,
        "likeCount": 234,
        "createdAt": "2024-01-01T09:00:00Z",
        "bestLapTime": "09:45.123"
      }
    ],
    "stats": {
      "totalCars": 1234,
      "totalTunes": 5678,
      "totalUsers": 8901,
      "totalProPlayers": 234
    }
  }
}
```

## 错误码定义

| 错误码 | 描述 | HTTP状态码 |
|--------|------|------------|
| AUTH_INVALID_CREDENTIALS | 用户名或密码错误 | 401 |
| AUTH_TOKEN_EXPIRED | 访问令牌已过期 | 401 |
| AUTH_TOKEN_INVALID | 访问令牌无效 | 401 |
| AUTH_INSUFFICIENT_PERMISSIONS | 权限不足 | 403 |
| RESOURCE_NOT_FOUND | 资源不存在 | 404 |
| VALIDATION_ERROR | 参数验证失败 | 400 |
| RATE_LIMIT_EXCEEDED | 请求频率超限 | 429 |
| INTERNAL_SERVER_ERROR | 服务器内部错误 | 500 |

## 接口调用示例

### JavaScript/TypeScript 示例

```typescript
// 用户登录
const loginResponse = await fetch('/api/auth/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'user@example.com',
    password: 'password123'
  })
});

const loginData = await loginResponse.json();
const token = loginData.data.token;

// 获取首页数据
const dashboardResponse = await fetch('/api/home/dashboard', {
  headers: {
    'Authorization': `Bearer ${token}`
  }
});

const dashboardData = await dashboardResponse.json();
```

### cURL 示例

```bash
# 用户登录
curl -X POST https://api.forzatune.pro/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@example.com","password":"password123"}'

# 获取车辆搜索
curl -X GET "https://api.forzatune.pro/api/cars/search?query=supra&limit=10" \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"

# 获取调校详情
curl -X GET https://api.forzatune.pro/api/tunes/tune_123 \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## 注意事项

1. **认证要求**: 除登录、注册、游戏列表、首页数据等公开接口外，其他接口都需要有效的JWT token
2. **请求频率**: 所有接口都有频率限制，建议合理控制请求频率
3. **数据缓存**: 首页数据、游戏列表等静态数据建议在前端进行适当缓存
4. **错误处理**: 客户端需要妥善处理各种错误情况，特别是网络错误和认证错误
5. **分页处理**: 列表接口都支持分页，建议使用分页来优化性能
6. **数据验证**: 客户端应该对用户输入进行基本验证，减少无效请求

## 开发调试指南

### 前端调试配置

1. **环境变量设置**：
   ```bash
   # 在项目根目录创建 .env.development 文件
   VITE_API_BASE_URL=http://localhost:3000/api
   VITE_MOCK_TOKEN=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.development
   VITE_DEBUG_MODE=true
   ```

2. **Token 管理**：
   - 开发环境自动使用 mock token
   - 登录时自动设置 token
   - 401 错误时自动清除 token 并跳转登录页

3. **API 请求调试**：
   - 开发环境会输出所有 API 请求日志
   - 可以在浏览器控制台查看请求详情
   - 支持手动清除 token 测试过期处理

### Spring Boot 后端调试配置

1. **环境变量设置**：
   ```bash
   # 跳过 token 验证（仅开发环境）
   AUTH_SKIP_VERIFICATION=true
   
   # Mock Token（仅开发环境有效）
   MOCK_TOKEN=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.development
   
   # JWT 密钥
   JWT_SECRET=your-secret-key-here
   
   # 数据库配置
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/forzatune_pro
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=password
   ```

2. **Spring Boot 项目结构**：
   ```
   src/main/java/com/forzatune/
   ├── ForzaTuneApplication.java
   ├── config/
   │   ├── SecurityConfig.java
   │   └── WebConfig.java
   ├── security/
   │   ├── JwtAuthenticationFilter.java
   │   └── JwtTokenUtil.java
   ├── controller/
   │   ├── AuthController.java
   │   ├── CarController.java
   │   └── TuneController.java
   ├── service/
   │   ├── AuthService.java
   │   ├── CarService.java
   │   └── TuneService.java
   ├── repository/
   │   ├── UserRepository.java
   │   ├── CarRepository.java
   │   └── TuneRepository.java
   └── model/
       ├── User.java
       ├── Car.java
       └── Tune.java
   ```

3. **认证中间件调试**：
   - Spring Security 过滤器链自动处理
   - 开发环境可以跳过 token 验证
   - 支持 mock token 验证
   - 详细的错误日志输出
   - 自动的 CORS 配置

### 调试流程

1. **启动开发环境**：
   ```bash
   # 前端
   npm run dev
   
   # Spring Boot 后端
   ./mvnw spring-boot:run
   # 或者
   java -jar target/forzatune-pro-0.0.1-SNAPSHOT.jar
   ```

2. **数据库准备**：
   ```sql
   -- 创建数据库
   CREATE DATABASE forzatune_pro CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 使用数据库
   USE forzatune_pro;
   
   -- 执行数据库脚本（参考 DATABASE_SCHEMA.md）
   ```

3. **测试认证流程**：
   - 访问需要认证的页面
   - 检查 token 是否正确添加
   - 测试 token 过期处理
   - 查看 Spring Boot 控制台日志

4. **API 调试**：
   - 查看浏览器控制台日志
   - 检查网络请求详情
   - 验证响应数据格式
   - 查看 Spring Boot 应用日志

### 常见问题

1. **Token 无效**：检查 mock token 是否正确设置
2. **401 错误**：确认 Spring Security 配置和 JWT 过滤器
3. **跨域问题**：检查 CORS 配置和 API 基础 URL
4. **请求失败**：查看浏览器控制台和 Spring Boot 日志
5. **数据库连接失败**：检查 MySQL 配置和连接参数
6. **Spring Boot 启动失败**：检查依赖配置和端口占用
7. **JWT 解析错误**：检查 JWT 密钥配置和 token 格式

## 版本控制

- 当前版本: v1.0
- 接口版本通过URL路径控制: `/api/v1/`
- 向后兼容性: 新版本会保持向后兼容，废弃的接口会提前通知

---

*最后更新: 2024-01-01*
*文档维护: ForzaTune.PRO 开发团队* 
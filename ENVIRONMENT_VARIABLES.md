# 环境变量配置说明

## 必需的环境变量

### 数据库配置
- `DB_URL`: 数据库连接URL (默认: `jdbc:mysql://localhost:3306/forzatune_pro?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true`)
- `DB_USERNAME`: 数据库用户名 (默认: `root`)
- `DB_PASSWORD`: 数据库密码 (默认: `12345678`)

### 邮件服务配置
- `MAIL_USERNAME`: SMTP用户名 (默认: `93fcd2001@smtp-brevo.com`)
- `MAIL_PASSWORD`: SMTP密码 **必须设置真实值**

### JWT配置
- `JWT_SECRET`: JWT签名密钥 (默认: `forzatune-pro-secret-key-2024-very-long-and-secure`)
- `JWT_EXPIRATION`: JWT过期时间毫秒数 (默认: `86400000` = 24小时)

## 设置方法

### 方法1: 系统环境变量
```bash
export MAIL_PASSWORD="your-real-smtp-password"
export DB_PASSWORD="your-real-db-password"
```

### 方法2: IDE环境变量
在IDE的运行配置中设置环境变量

### 方法3: application-local.yml
创建 `src/main/resources/application-local.yml`:
```yaml
spring:
  mail:
    password: your-real-smtp-password
  datasource:
    password: your-real-db-password
```
然后使用 `--spring.profiles.active=local` 启动

## 安全注意事项
- 永远不要将真实的密码/密钥提交到Git仓库
- 生产环境必须使用强密码和安全的JWT密钥
- 定期更换密钥和密码
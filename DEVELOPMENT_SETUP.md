# 开发环境配置说明

## 环境变量配置

在项目根目录创建 `.env.development` 文件：

```bash
# 开发环境配置

# API 基础URL
VITE_API_BASE_URL=http://localhost:3000/api

# Mock Token（用于开发调试）
VITE_MOCK_TOKEN=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.development

# 调试开关
VITE_DEBUG_MODE=true
VITE_LOG_API_REQUESTS=true
```

## Token 校验机制

### 前端 Token 管理

1. **自动添加 Token**：所有 API 请求会自动添加 Authorization header
2. **Token 存储**：Token 存储在 localStorage 中
3. **Token 验证**：简单的格式验证（JWT 三段式）
4. **自动清理**：401 错误时自动清除无效 token

### 开发环境调试

1. **Mock Token**：开发环境可以使用预定义的 mock token
2. **跳过验证**：后端可以配置跳过 token 验证
3. **调试日志**：开发环境会输出 API 请求日志

### 使用方式

```typescript
// 在组件中使用 API
import { api } from '@/utils/api'

// 公开接口（无需 token）
const cars = await api.get('/cars/search?query=supra')

// 私有接口（自动添加 token）
const profile = await api.get('/auth/profile')
const likeResult = await api.post('/tunes/123/like')
```

## 后端调试配置

在后端项目中设置环境变量：

```bash
# 跳过 token 验证（仅开发环境）
AUTH_SKIP_VERIFICATION=true

# Mock Token（仅开发环境有效）
MOCK_TOKEN=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.development

# JWT 密钥
JWT_SECRET=your-secret-key-here
```

## 认证流程

### 登录流程
1. 用户输入邮箱密码
2. 前端调用 `/api/auth/login`
3. 后端验证并返回 JWT token
4. 前端保存 token 到 localStorage
5. 后续请求自动携带 token

### 登出流程
1. 用户点击登出
2. 前端调用 `/api/auth/logout`
3. 清除本地存储的 token 和用户信息
4. 跳转到登录页

### Token 过期处理
1. 后端返回 401 错误
2. 前端拦截器自动处理
3. 清除本地存储
4. 跳转到登录页（保留当前页面路径）

## 调试技巧

### 查看 Token
```javascript
// 在浏览器控制台查看当前 token
console.log(localStorage.getItem('forzatune.token'))
```

### 模拟 Token 过期
```javascript
// 手动清除 token 测试过期处理
localStorage.removeItem('forzatune.token')
```

### 查看 API 请求
开发环境会在控制台输出所有 API 请求日志：
```
[API Request] GET /api/cars/search
[API Response] 200 /api/cars/search
```

## 注意事项

1. **生产环境**：必须启用严格的 token 验证
2. **开发环境**：可以使用 mock token 和跳过验证
3. **安全性**：不要在代码中硬编码真实的 JWT 密钥
4. **调试**：开发环境可以查看详细的请求日志
5. **兼容性**：确保前后端 token 格式一致 
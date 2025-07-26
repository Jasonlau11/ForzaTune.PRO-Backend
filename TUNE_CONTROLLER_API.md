# ForzaTune.PRO 调校管理接口文档

## 基础信息
- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **字符编码**: UTF-8
- **认证**: 部分接口需要用户登录认证

## 接口列表

### 1. 创建调校

创建一个新的车辆调校记录，包含基本信息和详细参数。

#### 基本信息
- **接口路径**: `/tunes`
- **请求方法**: `POST`
- **是否需要认证**: 是
- **权限要求**: 登录用户
- **缓存策略**: 创建后清除首页缓存

#### 请求参数

##### 请求体结构 (TuneSubmissionDto)
```json
{
  "carId": "string",
  "shareCode": "string",
  "preference": "string",
  "piClass": "string",
  "finalPI": number,
  "drivetrain": "string",
  "tireCompound": "string",
  "raceType": "string",
  "surfaceConditions": ["string"],
  "description": "string",
  "isProTune": boolean,
  "isParametersPublic": boolean,
  "screenshotUrl": "string",
  "parameters": {
    "frontTirePressure": number,
    "rearTirePressure": number,
    "transmissionSpeeds": number,
    "finalDrive": number,
    "gear1Ratio": number,
    "gear2Ratio": number,
    "gear3Ratio": number,
    "gear4Ratio": number,
    "gear5Ratio": number,
    "gear6Ratio": number,
    "gear7Ratio": number,
    "gear8Ratio": number,
    "gear9Ratio": number,
    "frontCamber": number,
    "rearCamber": number,
    "frontToe": number,
    "rearToe": number,
    "frontCaster": number,
    "frontAntiRollBar": number,
    "rearAntiRollBar": number,
    "frontSprings": number,
    "rearSprings": number,
    "frontRideHeight": number,
    "rearRideHeight": number,
    "frontRebound": number,
    "rearRebound": number,
    "frontBump": number,
    "rearBump": number,
    "differentialType": "string",
    "frontAcceleration": number,
    "frontDeceleration": number,
    "rearAcceleration": number,
    "rearDeceleration": number,
    "centerBalance": number,
    "brakePressure": number,
    "frontBrakeBalance": number,
    "frontDownforce": number,
    "rearDownforce": number
  }
}
```

#### 请求示例

```bash
curl -X POST "http://localhost:8080/api/tunes" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "carId": "car_001",
    "shareCode": "123 456 789",
    "preference": "Balance",
    "piClass": "S1",
    "finalPI": 900,
    "drivetrain": "AWD",
    "tireCompound": "Sport",
    "raceType": "Road",
    "surfaceConditions": ["Dry"],
    "description": "完美平衡的全能调校，适合各种赛道和天气条件。经过精心调试，在保持高速性能的同时提供卓越的操控性。",
    "isProTune": false,
    "isParametersPublic": true,
    "screenshotUrl": "/images/screenshots/tune_001.jpg",
    "parameters": {
      "frontTirePressure": 31.5,
      "rearTirePressure": 30.8,
      "transmissionSpeeds": 6,
      "finalDrive": 3.42,
      "gear1Ratio": 3.31,
      "gear2Ratio": 2.08,
      "gear3Ratio": 1.46,
      "gear4Ratio": 1.12,
      "gear5Ratio": 0.89,
      "gear6Ratio": 0.74,
      "gear7Ratio": null,
      "gear8Ratio": null,
      "gear9Ratio": null,
      "frontCamber": -1.2,
      "rearCamber": -0.8,
      "frontToe": 0.1,
      "rearToe": 0.2,
      "frontCaster": 6.5,
      "frontAntiRollBar": 28,
      "rearAntiRollBar": 25,
      "frontSprings": 145.2,
      "rearSprings": 138.7,
      "frontRideHeight": 12.5,
      "rearRideHeight": 13.0,
      "frontRebound": 8.2,
      "rearRebound": 7.8,
      "frontBump": 6.5,
      "rearBump": 6.2,
      "differentialType": "Sport",
      "frontAcceleration": 45,
      "frontDeceleration": 20,
      "rearAcceleration": 65,
      "rearDeceleration": 30,
      "centerBalance": 55,
      "brakePressure": 105,
      "frontBrakeBalance": 52,
      "frontDownforce": 180,
      "rearDownforce": 320
    }
  }'
```

#### 成功响应示例

```json
{
  "success": true,
  "data": {
    "id": "tune_12345",
    "carId": "car_001",
    "authorId": "user_001",
    "authorGamertag": "SpeedMaster2024",
    "shareCode": "123 456 789",
    "preference": "Balance",
    "piClass": "S1",
    "finalPI": 900,
    "drivetrain": "AWD",
    "tireCompound": "Sport",
    "raceType": "Road",
    "surfaceConditions": ["Dry"],
    "description": "完美平衡的全能调校，适合各种赛道和天气条件。经过精心调试，在保持高速性能的同时提供卓越的操控性。",
    "isProTune": false,
    "isParametersPublic": true,
    "hasDetailedParameters": true,
    "screenshotUrl": "/images/screenshots/tune_001.jpg",
    "createdAt": "2024-01-15T14:30:00Z",
    "updatedAt": "2024-01-15T14:30:00Z",
    "likeCount": 0,
    "tuneParameters": {
      "id": "param_12345",
      "tuneId": "tune_12345",
      "frontTirePressure": 31.5,
      "rearTirePressure": 30.8,
      "transmissionSpeeds": 6,
      "finalDrive": 3.42,
      "gear1Ratio": 3.31,
      "gear2Ratio": 2.08,
      "gear3Ratio": 1.46,
      "gear4Ratio": 1.12,
      "gear5Ratio": 0.89,
      "gear6Ratio": 0.74,
      "gear7Ratio": null,
      "gear8Ratio": null,
      "gear9Ratio": null,
      "frontCamber": -1.2,
      "rearCamber": -0.8,
      "frontToe": 0.1,
      "rearToe": 0.2,
      "frontCaster": 6.5,
      "frontAntiRollBar": 28,
      "rearAntiRollBar": 25,
      "frontSprings": 145.2,
      "rearSprings": 138.7,
      "frontRideHeight": 12.5,
      "rearRideHeight": 13.0,
      "frontRebound": 8.2,
      "rearRebound": 7.8,
      "frontBump": 6.5,
      "rearBump": 6.2,
      "differentialType": "Sport",
      "frontAcceleration": 45,
      "frontDeceleration": 20,
      "rearAcceleration": 65,
      "rearDeceleration": 30,
      "centerBalance": 55,
      "brakePressure": 105,
      "frontBrakeBalance": 52,
      "frontDownforce": 180,
      "rearDownforce": 320,
      "createdAt": "2024-01-15T14:30:00Z",
      "updatedAt": "2024-01-15T14:30:00Z"
    }
  },
  "error": null
}
```

---

### 2. 更新调校

更新已存在的调校记录，只有调校作者本人可以进行更新操作。

#### 基本信息
- **接口路径**: `/tunes/{id}`
- **请求方法**: `PUT`
- **是否需要认证**: 是
- **权限要求**: 调校作者本人
- **缓存策略**: 更新后清除相关缓存

#### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 调校ID |

#### 请求参数
请求体结构与创建调校相同 (TuneSubmissionDto)

#### 请求示例

```bash
curl -X PUT "http://localhost:8080/api/tunes/tune_12345" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "carId": "car_001",
    "shareCode": "123 456 789",
    "preference": "Handling",
    "piClass": "S1",
    "finalPI": 895,
    "drivetrain": "AWD",
    "tireCompound": "SemiSlick",
    "raceType": "Road",
    "surfaceConditions": ["Dry", "Wet"],
    "description": "优化后的操控版本，提升了湿地性能和过弯稳定性。适合技术型车手使用。",
    "isProTune": false,
    "isParametersPublic": true,
    "screenshotUrl": "/images/screenshots/tune_001_v2.jpg",
    "parameters": {
      "frontTirePressure": 32.0,
      "rearTirePressure": 31.2,
      "transmissionSpeeds": 6,
      "finalDrive": 3.35,
      "gear1Ratio": 3.25,
      "gear2Ratio": 2.05,
      "gear3Ratio": 1.48,
      "gear4Ratio": 1.15,
      "gear5Ratio": 0.92,
      "gear6Ratio": 0.76,
      "gear7Ratio": null,
      "gear8Ratio": null,
      "gear9Ratio": null,
      "frontCamber": -1.5,
      "rearCamber": -1.0,
      "frontToe": 0.05,
      "rearToe": 0.15,
      "frontCaster": 6.8,
      "frontAntiRollBar": 30,
      "rearAntiRollBar": 27,
      "frontSprings": 148.5,
      "rearSprings": 142.0,
      "frontRideHeight": 12.0,
      "rearRideHeight": 12.8,
      "frontRebound": 8.5,
      "rearRebound": 8.0,
      "frontBump": 6.8,
      "rearBump": 6.5,
      "differentialType": "Sport",
      "frontAcceleration": 48,
      "frontDeceleration": 22,
      "rearAcceleration": 68,
      "rearDeceleration": 32,
      "centerBalance": 58,
      "brakePressure": 108,
      "frontBrakeBalance": 54,
      "frontDownforce": 195,
      "rearDownforce": 340
    }
  }'
```

#### 成功响应示例
响应格式与创建调校相同，但包含更新后的数据和时间戳。

---

### 3. 删除调校

删除指定的调校记录，只有调校作者本人可以进行删除操作。

#### 基本信息
- **接口路径**: `/tunes/{id}`
- **请求方法**: `DELETE`
- **是否需要认证**: 是
- **权限要求**: 调校作者本人
- **缓存策略**: 删除后清除所有相关缓存

#### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 调校ID |

#### 请求示例

```bash
curl -X DELETE "http://localhost:8080/api/tunes/tune_12345" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 成功响应示例

```json
{
  "success": true,
  "data": null,
  "error": null
}
```

---

### 4. 获取调校详情

根据调校ID获取完整的调校信息，包含详细参数。

#### 基本信息
- **接口路径**: `/tunes/{id}`
- **请求方法**: `GET`
- **是否需要认证**: 否
- **缓存策略**: 启用读取缓存

#### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 调校ID |

#### 请求示例

```bash
curl -X GET "http://localhost:8080/api/tunes/tune_12345" \
  -H "Content-Type: application/json"
```

#### 成功响应示例

```json
{
  "success": true,
  "data": {
    "id": "tune_12345",
    "shareCode": "123 456 789",
    "carId": "car_001",
    "authorGamertag": "SpeedMaster2024",
    "isProTune": false,
    "preference": "Balance",
    "piClass": "S1",
    "finalPI": 900,
    "drivetrain": "AWD",
    "tireCompound": "Sport",
    "surfaceConditions": ["Dry"],
    "description": "完美平衡的全能调校，适合各种赛道和天气条件。经过精心调试，在保持高速性能的同时提供卓越的操控性。",
    "likeCount": 45,
    "favoriteCount": 23,
    "createdAt": "2024-01-15T14:30:00Z",
    "parameters": {
      "frontTirePressure": 31.5,
      "rearTirePressure": 30.8,
      "transmissionSpeeds": 6,
      "finalDrive": 3.42,
      "gear1Ratio": 3.31,
      "gear2Ratio": 2.08,
      "gear3Ratio": 1.46,
      "gear4Ratio": 1.12,
      "gear5Ratio": 0.89,
      "gear6Ratio": 0.74,
      "gear7Ratio": null,
      "gear8Ratio": null,
      "gear9Ratio": null,
      "frontCamber": -1.2,
      "rearCamber": -0.8,
      "frontToe": 0.1,
      "rearToe": 0.2,
      "frontCaster": 6.5,
      "frontAntiRollBar": 28,
      "rearAntiRollBar": 25,
      "frontSprings": 145.2,
      "rearSprings": 138.7,
      "frontRideHeight": 12.5,
      "rearRideHeight": 13.0,
      "frontRebound": 8.2,
      "rearRebound": 7.8,
      "frontBump": 6.5,
      "rearBump": 6.2,
      "differentialType": "Sport",
      "frontAcceleration": 45,
      "frontDeceleration": 20,
      "rearAcceleration": 65,
      "rearDeceleration": 30,
      "centerBalance": 55,
      "brakePressure": 105,
      "frontBrakeBalance": 52,
      "frontDownforce": 180,
      "rearDownforce": 320
    }
  },
  "error": null
}
```

---

### 5. 调校点赞

为指定调校增加点赞数，用户可以重复点赞。

#### 基本信息
- **接口路径**: `/tunes/{id}/like`
- **请求方法**: `POST`
- **是否需要认证**: 否
- **幂等性**: 非幂等(可重复点赞)

#### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 调校ID |

#### 请求示例

```bash
curl -X POST "http://localhost:8080/api/tunes/tune_12345/like" \
  -H "Content-Type: application/json"
```

#### 成功响应示例

```json
{
  "success": true,
  "data": "ok",
  "error": null
}
```

---

### 6. 调校收藏

收藏指定的调校到用户收藏列表。

#### 基本信息
- **接口路径**: `/tunes/{id}/favorite`
- **请求方法**: `POST`
- **是否需要认证**: 是
- **权限要求**: 登录用户
- **业务逻辑**: 重复收藏会取消收藏

#### 路径参数
| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 调校ID |

#### 请求示例

```bash
curl -X POST "http://localhost:8080/api/tunes/tune_12345/favorite" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

#### 成功响应示例

```json
{
  "success": true,
  "data": "ok",
  "error": null
}
```

---

## 错误响应示例

### 认证失败
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "认证失败，请先登录"
  }
}
```

### 权限不足
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "无权修改此调校"
  }
}
```

### 调校不存在
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "获取调校数据失败: 调校不存在"
  }
}
```

### 参数验证失败
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "创建调校失败: shareCode不能为空"
  }
}
```

### 分享代码重复
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "创建调校失败: 分享代码已存在"
  }
}
```

---

## 数据字典

### 枚举值说明

#### TunePreference (调校偏好)
- `Power` - 动力优先
- `Handling` - 操控优先  
- `Balance` - 平衡设置

#### PIClass (PI等级)
- `X` - X级 (999-1000)
- `S2` - S2级 (900-998)
- `S1` - S1级 (800-899)
- `A` - A级 (700-799)
- `B` - B级 (600-699)
- `C` - C级 (500-599)
- `D` - D级 (100-499)

#### Drivetrain (驱动方式)
- `RWD` - 后轮驱动
- `FWD` - 前轮驱动
- `AWD` - 全轮驱动

#### TireCompound (轮胎类型)
- `Stock` - 原厂轮胎
- `Street` - 街道轮胎
- `Sport` - 运动轮胎
- `SemiSlick` - 半热熔轮胎
- `Slick` - 光头胎
- `Rally` - 拉力轮胎
- `Snow` - 雪地轮胎
- `OffRoad` - 越野轮胎
- `Drag` - 直线加速轮胎
- `Drift` - 漂移轮胎

#### RaceType (比赛类型)
- `Road` - 公路赛
- `Dirt` - 泥地赛
- `CrossCountry` - 越野赛

#### SurfaceCondition (路面条件)
- `Dry` - 干燥路面
- `Wet` - 湿滑路面
- `Snow` - 雪地路面

#### DifferentialType (差速器类型)
- `Stock` - 原厂差速器
- `Street` - 街道差速器
- `Sport` - 运动差速器
- `OffRoad` - 越野差速器
- `Rally` - 拉力差速器
- `Drift` - 漂移差速器

---

## 字段验证规则

### 必填字段
- `carId` - 车辆ID，必须存在于车辆数据中
- `shareCode` - 分享代码，格式: "XXX XXX XXX"，全局唯一
- `preference` - 调校偏好，必须为有效枚举值
- `piClass` - PI等级，必须为有效枚举值
- `finalPI` - 最终PI值，范围: 100-1000
- `drivetrain` - 驱动方式，必须为有效枚举值

### 可选字段
- `description` - 调校描述，最大长度: 1000字符
- `isProTune` - 默认值: false
- `isParametersPublic` - 默认值: false
- `screenshotUrl` - 截图URL，需要有效的图片格式
- `parameters` - 调校参数对象，可以为null

### 参数数值范围及单位说明

**注意**: 后端统一使用公制单位存储，前端负责英制/公制转换显示

#### 轮胎系统 (公制存储)
- **轮胎压力**: 1.0 - 3.5 BAR (前端可显示为 PSI, 1 BAR ≈ 14.5 PSI)

#### 变速箱系统
- **齿轮比**: 0.50 - 5.00 (无单位)
- **最终传动比**: 2.0 - 6.0 (无单位)

#### 校准系统
- **外倾角**: -5.0° - +5.0° (角度，无需转换)
- **前束角**: -2.0° - +2.0° (角度，无需转换)  
- **主销后倾角**: 3.0° - 9.0° (角度，无需转换)

#### 悬挂系统 (公制存储)
- **防倾杆**: 1 - 65 (等级，无需转换)
- **弹簧硬度**: 50.0 - 300.0 kg/mm (前端可显示为 lbs/in, 1 kg/mm ≈ 5.71 lbs/in)
- **车身高度**: 5.0 - 25.0 cm (前端可显示为 inches, 1 inch = 2.54 cm)
- **阻尼强度**: 1.0 - 20.0 (等级，无需转换)

#### 差速器系统
- **差速器设置**: 0 - 100% (百分比，无需转换)

#### 制动系统
- **制动力**: 80 - 130% (百分比，无需转换)
- **制动平衡**: 40 - 60% (百分比，无需转换)

#### 空气动力学 (公制存储)
- **下压力**: 50 - 500 kg (前端可显示为 lbs, 1 kg ≈ 2.20 lbs)

---

## 前端集成示例

### JavaScript/TypeScript

```javascript
// 调校服务类
class TuneService {
  constructor(baseURL = 'http://localhost:8080/api') {
    this.baseURL = baseURL;
  }

  // 创建调校
  async createTune(tuneData) {
    const response = await fetch(`${this.baseURL}/tunes`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.getToken()}`
      },
      body: JSON.stringify(tuneData)
    });

    const result = await response.json();
    if (!result.success) {
      throw new Error(result.error.message);
    }
    return result.data;
  }

  // 获取调校详情
  async getTuneById(tuneId) {
    const response = await fetch(`${this.baseURL}/tunes/${tuneId}`);
    const result = await response.json();
    
    if (!result.success) {
      throw new Error(result.error.message);
    }
    return result.data;
  }

  // 更新调校
  async updateTune(tuneId, tuneData) {
    const response = await fetch(`${this.baseURL}/tunes/${tuneId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.getToken()}`
      },
      body: JSON.stringify(tuneData)
    });

    const result = await response.json();
    if (!result.success) {
      throw new Error(result.error.message);
    }
    return result.data;
  }

  // 删除调校
  async deleteTune(tuneId) {
    const response = await fetch(`${this.baseURL}/tunes/${tuneId}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${this.getToken()}`
      }
    });

    const result = await response.json();
    if (!result.success) {
      throw new Error(result.error.message);
    }
  }

  // 点赞调校
  async likeTune(tuneId) {
    const response = await fetch(`${this.baseURL}/tunes/${tuneId}/like`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      }
    });

    const result = await response.json();
    if (!result.success) {
      throw new Error(result.error.message);
    }
  }

  // 收藏调校
  async favoriteTune(tuneId) {
    const response = await fetch(`${this.baseURL}/tunes/${tuneId}/favorite`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${this.getToken()}`
      }
    });

    const result = await response.json();
    if (!result.success) {
      throw new Error(result.error.message);
    }
  }

  getToken() {
    return localStorage.getItem('authToken');
  }
}

// 单位转换工具类 (前端实现)
class UnitConverter {
  // 轮胎压力转换 (PSI ↔ BAR)
  static tire = {
    psiToBar: (psi) => Math.round(psi * 0.0689476 * 100) / 100,
    barToPsi: (bar) => Math.round(bar * 14.5038 * 100) / 100
  }
  
  // 弹簧硬度转换 (lbs/in ↔ kg/mm)
  static springs = {
    lbsInToKgMm: (lbsIn) => Math.round(lbsIn * 0.175127 * 100) / 100,
    kgMmToLbsIn: (kgMm) => Math.round(kgMm * 5.71015 * 100) / 100
  }
  
  // 车身高度转换 (inches ↔ cm)
  static height = {
    inchesToCm: (inches) => Math.round(inches * 2.54 * 100) / 100,
    cmToInches: (cm) => Math.round(cm * 0.393701 * 100) / 100
  }
  
  // 下压力转换 (lbs ↔ kg)
  static downforce = {
    lbsToKg: (lbs) => Math.round(lbs * 0.453592 * 100) / 100,
    kgToLbs: (kg) => Math.round(kg * 2.20462 * 100) / 100
  }
}

// 使用示例
const tuneService = new TuneService();

// 创建调校 (用户输入英制，转换为公制发送给后端)
const userInput = {
  frontTirePressure: 31.5, // PSI (用户输入)
  rearTirePressure: 30.8,  // PSI
  frontSprings: 500,       // lbs/in
  rearSprings: 480,        // lbs/in
  frontRideHeight: 4.5,    // inches
  rearRideHeight: 4.8      // inches
};

// 转换为公制发送给后端
const tuneData = {
  carId: "car_001",
  shareCode: "123 456 789", 
  preference: "Balance",
  piClass: "S1",
  finalPI: 900,
  drivetrain: "AWD",
  tireCompound: "Sport",
  raceType: "Road",
  surfaceConditions: ["Dry"],
  description: "平衡性调校，适合新手使用",
  isProTune: false,
  isParametersPublic: true,
  parameters: {
    frontTirePressure: UnitConverter.tire.psiToBar(userInput.frontTirePressure), // 2.17 BAR
    rearTirePressure: UnitConverter.tire.psiToBar(userInput.rearTirePressure),   // 2.12 BAR
    frontSprings: UnitConverter.springs.lbsInToKgMm(userInput.frontSprings),     // 87.56 kg/mm
    rearSprings: UnitConverter.springs.lbsInToKgMm(userInput.rearSprings),       // 84.06 kg/mm
    frontRideHeight: UnitConverter.height.inchesToCm(userInput.frontRideHeight), // 11.43 cm
    rearRideHeight: UnitConverter.height.inchesToCm(userInput.rearRideHeight),   // 12.19 cm
    // ... 其他参数
  }
};

tuneService.createTune(tuneData)
  .then(tune => console.log('调校创建成功:', tune))
  .catch(error => console.error('创建失败:', error));
```

### Vue.js 组合式API示例

```javascript
import { ref, reactive } from 'vue'

export function useTuneManagement() {
  const loading = ref(false)
  const error = ref(null)
  const tuneService = new TuneService()

  // 调校表单数据
  const tuneForm = reactive({
    carId: '',
    shareCode: '',
    preference: 'Balance',
    piClass: 'S1',
    finalPI: 800,
    drivetrain: 'RWD',
    tireCompound: 'Sport',
    raceType: 'Road',
    surfaceConditions: ['Dry'],
    description: '',
    isProTune: false,
    isParametersPublic: true,
    screenshotUrl: '',
    parameters: {
      frontTirePressure: 30.0,
      rearTirePressure: 30.0,
      // ... 其他默认参数
    }
  })

  // 创建调校
  const createTune = async () => {
    loading.value = true
    error.value = null

    try {
      const result = await tuneService.createTune(tuneForm)
      return result
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  // 获取调校详情
  const getTuneDetail = async (tuneId) => {
    loading.value = true
    error.value = null

    try {
      const result = await tuneService.getTuneById(tuneId)
      return result
    } catch (err) {
      error.value = err.message
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    loading,
    error,
    tuneForm,
    createTune,
    getTuneDetail
  }
}
```

---

## 测试用例

### 功能测试

```bash
# 1. 创建调校测试
curl -X POST "http://localhost:8080/api/tunes" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test_token" \
  -d @tune_create_test.json

# 2. 获取调校详情测试
curl -X GET "http://localhost:8080/api/tunes/tune_12345" \
  -w "HTTP Status: %{http_code}\nResponse Time: %{time_total}s\n"

# 3. 更新调校测试
curl -X PUT "http://localhost:8080/api/tunes/tune_12345" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test_token" \
  -d @tune_update_test.json

# 4. 点赞测试
curl -X POST "http://localhost:8080/api/tunes/tune_12345/like"

# 5. 收藏测试
curl -X POST "http://localhost:8080/api/tunes/tune_12345/favorite" \
  -H "Authorization: Bearer test_token"

# 6. 删除调校测试
curl -X DELETE "http://localhost:8080/api/tunes/tune_12345" \
  -H "Authorization: Bearer test_token"
```

### 边界测试

```bash
# 测试无效的调校ID
curl -X GET "http://localhost:8080/api/tunes/invalid_id"

# 测试未认证的创建请求
curl -X POST "http://localhost:8080/api/tunes" \
  -H "Content-Type: application/json" \
  -d @tune_create_test.json

# 测试无权限的更新请求
curl -X PUT "http://localhost:8080/api/tunes/other_user_tune" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer test_token" \
  -d @tune_update_test.json
```

---

## HTTP状态码

- `200 OK` - 请求成功
- `201 Created` - 创建成功
- `400 Bad Request` - 请求参数错误
- `401 Unauthorized` - 未认证
- `403 Forbidden` - 权限不足
- `404 Not Found` - 资源不存在
- `409 Conflict` - 资源冲突(如分享代码重复)
- `500 Internal Server Error` - 服务器内部错误

---

## 注意事项

1. **认证要求**: 创建、更新、删除、收藏操作需要用户认证
2. **权限控制**: 只有调校作者可以更新和删除自己的调校
3. **分享代码唯一性**: 每个分享代码在系统中必须唯一
4. **参数公开性**: 只有设置为公开的调校参数才会在接口中返回
5. **缓存机制**: 创建和更新操作会影响相关缓存
6. **事务安全**: 调校和参数的操作在同一事务中执行
7. **单位系统**: 后端统一使用公制单位存储，前端负责英制/公制转换显示
8. **数值精度**: 浮点数保持2位小数精度即可满足游戏要求
9. **错误处理**: 提供详细的错误信息便于调试 
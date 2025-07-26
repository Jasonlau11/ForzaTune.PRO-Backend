# ForzaTune.PRO 首页接口文档

## 基础信息
- **Base URL**: `http://localhost:8080/api`
- **Content-Type**: `application/json`
- **字符编码**: UTF-8

## 接口列表

### 1. 获取首页仪表盘数据

获取首页展示所需的所有核心数据，包括热门车辆、最新调校、PRO调校和统计信息。

#### 基本信息
- **接口路径**: `/home/dashboard`
- **请求方法**: `GET`
- **是否需要认证**: 否
- **缓存策略**: 启用缓存(5分钟)

#### 请求参数
无需任何参数

#### 请求示例
```bash
curl -X GET "http://localhost:8080/api/home/dashboard" \
  -H "Content-Type: application/json"
```

#### 响应数据结构

```json
{
  "success": boolean,
  "data": {
    "popularCars": [
      {
        "id": "string",
        "name": "string", 
        "manufacturer": "string",
        "year": number,
        "category": "string",
        "pi": number,
        "drivetrain": "string",
        "imageUrl": "string",
        "tuneCount": number,
        "tunes": []
      }
    ],
    "recentTunes": [
      {
        "id": "string",
        "shareCode": "string",
        "carId": "string",
        "authorGamertag": "string",
        "isProTune": boolean,
        "preference": "string",
        "piClass": "string", 
        "finalPI": number,
        "drivetrain": "string",
        "tireCompound": "string",
        "surfaceConditions": ["string"],
        "description": "string",
        "likeCount": number,
        "favoriteCount": number,
        "createdAt": "string",
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
    ],
    "proTunes": [
      // 与 recentTunes 结构相同
    ],
    "stats": {
      "totalCars": number,
      "totalTunes": number,
      "totalUsers": number,
      "totalProPlayers": number
    }
  },
  "error": null
}
```

#### 成功响应示例

```json
{
  "success": true,
  "data": {
    "popularCars": [
      {
        "id": "car_001",
        "name": "RS6 Avant",
        "manufacturer": "Audi",
        "year": 2020,
        "category": "SportsCars",
        "pi": 860,
        "drivetrain": "AWD",
        "imageUrl": "/images/cars/audi-rs6-2020.jpg",
        "tuneCount": 127,
        "tunes": []
      },
      {
        "id": "car_002", 
        "name": "Supra RZ",
        "manufacturer": "Toyota",
        "year": 1998,
        "category": "SportsCars", 
        "pi": 842,
        "drivetrain": "RWD",
        "imageUrl": "/images/cars/toyota-supra-1998.jpg",
        "tuneCount": 98,
        "tunes": []
      },
      {
        "id": "car_003",
        "name": "911 GT3 RS",
        "manufacturer": "Porsche", 
        "year": 2019,
        "category": "Supercars",
        "pi": 920,
        "drivetrain": "RWD",
        "imageUrl": "/images/cars/porsche-911-gt3rs-2019.jpg",
        "tuneCount": 76,
        "tunes": []
      },
      {
        "id": "car_004",
        "name": "AMG GT R",
        "manufacturer": "Mercedes-Benz",
        "year": 2017,
        "category": "Supercars",
        "pi": 905,
        "drivetrain": "RWD", 
        "imageUrl": "/images/cars/mercedes-amg-gtr-2017.jpg",
        "tuneCount": 63,
        "tunes": []
      }
    ],
    "recentTunes": [
      {
        "id": "tune_001",
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
        "description": "完美平衡的设置，适合各种赛道条件。经过大量测试优化，能够在保持高速的同时提供excellent的操控性。",
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
      {
        "id": "tune_002",
        "shareCode": "987 654 321", 
        "carId": "car_002",
        "authorGamertag": "DriftKingJP",
        "isProTune": true,
        "preference": "Power",
        "piClass": "S1",
        "finalPI": 885,
        "drivetrain": "RWD",
        "tireCompound": "SemiSlick",
        "surfaceConditions": ["Dry", "Wet"],
        "description": "专为漂移和功率输出优化的调校。后轮驱动配置提供卓越的牵引力控制。",
        "likeCount": 78,
        "favoriteCount": 34,
        "createdAt": "2024-01-15T12:15:00Z",
        "parameters": {
          "frontTirePressure": 29.8,
          "rearTirePressure": 28.5,
          "transmissionSpeeds": 6,
          "finalDrive": 3.73,
          "gear1Ratio": 3.62,
          "gear2Ratio": 2.19,
          "gear3Ratio": 1.52,
          "gear4Ratio": 1.18,
          "gear5Ratio": 0.93,
          "gear6Ratio": 0.78,
          "gear7Ratio": null,
          "gear8Ratio": null,
          "gear9Ratio": null,
          "frontCamber": -1.8,
          "rearCamber": -1.5,
          "frontToe": 0.0,
          "rearToe": 0.3,
          "frontCaster": 7.0,
          "frontAntiRollBar": 22,
          "rearAntiRollBar": 18,
          "frontSprings": 142.8,
          "rearSprings": 135.3,
          "frontRideHeight": 11.8,
          "rearRideHeight": 12.5,
          "frontRebound": 9.1,
          "rearRebound": 8.5,
          "frontBump": 7.2,
          "rearBump": 6.8,
          "differentialType": "Race",
          "frontAcceleration": 35,
          "frontDeceleration": 15,
          "rearAcceleration": 75,
          "rearDeceleration": 25,
          "centerBalance": 45,
          "brakePressure": 110,
          "frontBrakeBalance": 48,
          "frontDownforce": 150,
          "rearDownforce": 280
        }
      },
      {
        "id": "tune_003",
        "shareCode": "456 789 123",
        "carId": "car_003", 
        "authorGamertag": "TrackDayPro",
        "isProTune": false,
        "preference": "Handling",
        "piClass": "S2",
        "finalPI": 920,
        "drivetrain": "RWD",
        "tireCompound": "Slick",
        "surfaceConditions": ["Dry"],
        "description": "赛道日专用调校，极致的过弯性能和稳定性。适合专业车手使用。",
        "likeCount": 62,
        "favoriteCount": 28,
        "createdAt": "2024-01-15T09:45:00Z",
        "parameters": {
          "frontTirePressure": 33.2,
          "rearTirePressure": 32.5,
          "transmissionSpeeds": 7,
          "finalDrive": 3.15,
          "gear1Ratio": 3.25,
          "gear2Ratio": 2.12,
          "gear3Ratio": 1.55,
          "gear4Ratio": 1.25,
          "gear5Ratio": 1.02,
          "gear6Ratio": 0.86,
          "gear7Ratio": 0.73,
          "gear8Ratio": null,
          "gear9Ratio": null,
          "frontCamber": -2.5,
          "rearCamber": -2.0,
          "frontToe": -0.1,
          "rearToe": 0.1,
          "frontCaster": 7.5,
          "frontAntiRollBar": 35,
          "rearAntiRollBar": 32,
          "frontSprings": 155.8,
          "rearSprings": 148.2,
          "frontRideHeight": 10.2,
          "rearRideHeight": 11.0,
          "frontRebound": 10.5,
          "rearRebound": 9.8,
          "frontBump": 8.2,
          "rearBump": 7.5,
          "differentialType": "Race",
          "frontAcceleration": 50,
          "frontDeceleration": 30,
          "rearAcceleration": 80,
          "rearDeceleration": 40,
          "centerBalance": 50,
          "brakePressure": 120,
          "frontBrakeBalance": 55,
          "frontDownforce": 250,
          "rearDownforce": 420
        }
      }
    ],
    "proTunes": [
      {
        "id": "tune_pro_001",
        "shareCode": "111 222 333",
        "carId": "car_004",
        "authorGamertag": "ProRacer_Official",
        "isProTune": true,
        "preference": "Balance",
        "piClass": "S2",
        "finalPI": 905,
        "drivetrain": "RWD",
        "tireCompound": "SemiSlick",
        "surfaceConditions": ["Dry"],
        "description": "官方PRO车手认证调校。基于real-world数据优化，提供championship级别的性能表现。",
        "likeCount": 156,
        "favoriteCount": 89,
        "createdAt": "2024-01-14T16:20:00Z",
        "parameters": {
          "frontTirePressure": 32.8,
          "rearTirePressure": 31.5,
          "transmissionSpeeds": 7,
          "finalDrive": 3.25,
          "gear1Ratio": 3.18,
          "gear2Ratio": 2.05,
          "gear3Ratio": 1.48,
          "gear4Ratio": 1.15,
          "gear5Ratio": 0.95,
          "gear6Ratio": 0.82,
          "gear7Ratio": 0.71,
          "gear8Ratio": null,
          "gear9Ratio": null,
          "frontCamber": -2.2,
          "rearCamber": -1.8,
          "frontToe": 0.05,
          "rearToe": 0.15,
          "frontCaster": 7.2,
          "frontAntiRollBar": 32,
          "rearAntiRollBar": 29,
          "frontSprings": 152.5,
          "rearSprings": 145.8,
          "frontRideHeight": 11.5,
          "rearRideHeight": 12.2,
          "frontRebound": 9.8,
          "rearRebound": 9.2,
          "frontBump": 7.8,
          "rearBump": 7.2,
          "differentialType": "Race",
          "frontAcceleration": 48,
          "frontDeceleration": 25,
          "rearAcceleration": 72,
          "rearDeceleration": 35,
          "centerBalance": 52,
          "brakePressure": 115,
          "frontBrakeBalance": 53,
          "frontDownforce": 220,
          "rearDownforce": 380
        }
      },
      {
        "id": "tune_pro_002",
        "shareCode": "444 555 666",
        "carId": "car_001",
        "authorGamertag": "ChampionDriver",
        "isProTune": true,
        "preference": "Handling",
        "piClass": "S1", 
        "finalPI": 860,
        "drivetrain": "AWD",
        "tireCompound": "Sport",
        "surfaceConditions": ["Dry", "Wet"],
        "description": "锦标赛获胜调校，专注于wet和dry条件下的optimal操控性能。",
        "likeCount": 203,
        "favoriteCount": 112,
        "createdAt": "2024-01-14T11:30:00Z",
        "parameters": {
          "frontTirePressure": 30.5,
          "rearTirePressure": 29.8,
          "transmissionSpeeds": 6,
          "finalDrive": 3.52,
          "gear1Ratio": 3.45,
          "gear2Ratio": 2.15,
          "gear3Ratio": 1.58,
          "gear4Ratio": 1.22,
          "gear5Ratio": 0.98,
          "gear6Ratio": 0.82,
          "gear7Ratio": null,
          "gear8Ratio": null,
          "gear9Ratio": null,
          "frontCamber": -1.8,
          "rearCamber": -1.2,
          "frontToe": 0.08,
          "rearToe": 0.18,
          "frontCaster": 6.8,
          "frontAntiRollBar": 30,
          "rearAntiRollBar": 27,
          "frontSprings": 148.8,
          "rearSprings": 142.2,
          "frontRideHeight": 12.0,
          "rearRideHeight": 12.8,
          "frontRebound": 8.8,
          "rearRebound": 8.2,
          "frontBump": 7.0,
          "rearBump": 6.5,
          "differentialType": "Sport",
          "frontAcceleration": 42,
          "frontDeceleration": 22,
          "rearAcceleration": 68,
          "rearDeceleration": 32,
          "centerBalance": 58,
          "brakePressure": 108,
          "frontBrakeBalance": 51,
          "frontDownforce": 195,
          "rearDownforce": 345
        }
      },
      {
        "id": "tune_pro_003",
        "shareCode": "777 888 999",
        "carId": "car_002",
        "authorGamertag": "WorldRecordHolder",
        "isProTune": true,
        "preference": "Power",
        "piClass": "S1",
        "finalPI": 842,
        "drivetrain": "RWD",
        "tireCompound": "SemiSlick",
        "surfaceConditions": ["Dry"],
        "description": "世界纪录保持者的signature调校。在保持controllability的同时maximize功率输出。",
        "likeCount": 289,
        "favoriteCount": 145,
        "createdAt": "2024-01-13T20:45:00Z",
        "parameters": {
          "frontTirePressure": 28.5,
          "rearTirePressure": 27.2,
          "transmissionSpeeds": 6,
          "finalDrive": 3.95,
          "gear1Ratio": 3.88,
          "gear2Ratio": 2.35,
          "gear3Ratio": 1.68,
          "gear4Ratio": 1.28,
          "gear5Ratio": 1.02,
          "gear6Ratio": 0.85,
          "gear7Ratio": null,
          "gear8Ratio": null,
          "gear9Ratio": null,
          "frontCamber": -2.0,
          "rearCamber": -1.8,
          "frontToe": -0.05,
          "rearToe": 0.25,
          "frontCaster": 7.8,
          "frontAntiRollBar": 25,
          "rearAntiRollBar": 20,
          "frontSprings": 140.5,
          "rearSprings": 132.8,
          "frontRideHeight": 11.2,
          "rearRideHeight": 12.0,
          "frontRebound": 9.5,
          "rearRebound": 8.8,
          "frontBump": 7.5,
          "rearBump": 7.0,
          "differentialType": "Race",
          "frontAcceleration": 40,
          "frontDeceleration": 18,
          "rearAcceleration": 85,
          "rearDeceleration": 22,
          "centerBalance": 40,
          "brakePressure": 118,
          "frontBrakeBalance": 45,
          "frontDownforce": 135,
          "rearDownforce": 265
        }
      }
    ],
    "stats": {
      "totalCars": 342,
      "totalTunes": 1567,
      "totalUsers": 2048,
      "totalProPlayers": 87
    }
  },
  "error": null
}
```

#### 错误响应示例

##### 服务内部错误
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "获取首页数据失败: 数据库连接超时"
  }
}
```

##### 数据库查询异常
```json
{
  "success": false,
  "data": null,
  "error": {
    "message": "获取首页数据失败: SQL查询异常 - 表不存在"
  }
}
```

#### 字段说明

##### HomeDataDto 字段说明
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| popularCars | Array<CarDto> | 是 | 热门车辆列表(按调校数量排序) |
| recentTunes | Array<TuneDto> | 是 | 最新调校列表(按创建时间排序) |
| proTunes | Array<TuneDto> | 是 | PRO调校列表(PRO用户的精选调校) |
| stats | HomeStatsDto | 是 | 网站统计数据 |

##### CarDto 字段说明
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 车辆唯一标识 |
| name | String | 是 | 车辆名称 |
| manufacturer | String | 是 | 制造商 |
| year | Number | 是 | 年份 |
| category | String | 是 | 车辆分类: SportsCars/MuscleCars/Supercars/ClassicCars/Hypercars/TrackToys |
| pi | Number | 是 | 性能指数 |
| drivetrain | String | 是 | 驱动方式: RWD/FWD/AWD |
| imageUrl | String | 否 | 车辆图片URL |
| tuneCount | Number | 是 | 该车辆的调校数量 |
| tunes | Array | 是 | 车辆调校列表(通常为空数组) |

##### TuneDto 字段说明
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | String | 是 | 调校唯一标识 |
| shareCode | String | 是 | 游戏内分享代码 |
| carId | String | 是 | 关联车辆ID |
| authorGamertag | String | 是 | 作者游戏标签 |
| isProTune | Boolean | 是 | 是否为PRO调校 |
| preference | String | 是 | 调校偏好: Power/Handling/Balance |
| piClass | String | 是 | PI等级: X/S2/S1/A/B/C/D |
| finalPI | Number | 是 | 最终性能指数 |
| drivetrain | String | 是 | 驱动方式 |
| tireCompound | String | 是 | 轮胎类型: Stock/Street/Sport/SemiSlick/Slick/Rally/Snow/OffRoad/Drag/Drift |
| surfaceConditions | Array<String> | 是 | 路面条件: Dry/Wet/Snow |
| description | String | 否 | 调校描述 |
| likeCount | Number | 是 | 点赞数 |
| favoriteCount | Number | 是 | 收藏数 |
| createdAt | String | 是 | 创建时间(ISO 8601格式) |
| parameters | TuneParametersDto | 否 | 详细调校参数 |

##### HomeStatsDto 字段说明
| 字段名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| totalCars | Number | 是 | 车辆总数 |
| totalTunes | Number | 是 | 调校总数 |
| totalUsers | Number | 是 | 用户总数 |
| totalProPlayers | Number | 是 | PRO玩家总数 |

##### TuneParametersDto 字段说明
详细的调校参数对象，包含轮胎、变速箱、校准、防倾杆、弹簧、阻尼、差速器、制动、空气动力学等配置参数。所有参数均为可选字段。

#### HTTP状态码
- `200 OK` - 请求成功
- `500 Internal Server Error` - 服务器内部错误

#### 注意事项
1. 接口启用了缓存机制，相同请求在短时间内会返回缓存结果
2. `parameters` 字段可能为 `null`，取决于用户是否公开调校参数
3. 所有数组字段保证不为 `null`，但可能为空数组 `[]`
4. 时间格式统一使用 ISO 8601 标准
5. 数值字段使用合理的精度，浮点数保留1-2位小数

#### 前端调用示例

##### JavaScript/TypeScript
```javascript
// 使用 fetch API
async function getHomeData() {
  try {
    const response = await fetch('http://localhost:8080/api/home/dashboard');
    const result = await response.json();
    
    if (result.success) {
      console.log('首页数据:', result.data);
      return result.data;
    } else {
      console.error('获取失败:', result.error.message);
      throw new Error(result.error.message);
    }
  } catch (error) {
    console.error('网络错误:', error);
    throw error;
  }
}

// 使用 axios
import axios from 'axios';

async function getHomeDataWithAxios() {
  try {
    const response = await axios.get('http://localhost:8080/api/home/dashboard');
    return response.data.data; // 直接返回data字段
  } catch (error) {
    console.error('请求失败:', error.response?.data?.error?.message || error.message);
    throw error;
  }
}
```

##### Vue.js 组合式API示例
```javascript
import { ref, onMounted } from 'vue'

export default {
  setup() {
    const homeData = ref(null)
    const loading = ref(false)
    const error = ref(null)

    const fetchHomeData = async () => {
      loading.value = true
      error.value = null
      
      try {
        const response = await fetch('http://localhost:8080/api/home/dashboard')
        const result = await response.json()
        
        if (result.success) {
          homeData.value = result.data
        } else {
          error.value = result.error.message
        }
      } catch (err) {
        error.value = '网络连接失败'
        console.error('获取首页数据失败:', err)
      } finally {
        loading.value = false
      }
    }

    onMounted(() => {
      fetchHomeData()
    })

    return {
      homeData,
      loading,
      error,
      fetchHomeData
    }
  }
}
```

#### 测试用例

##### 正常场景测试
```bash
# 基础功能测试
curl -X GET "http://localhost:8080/api/home/dashboard" \
  -H "Content-Type: application/json" \
  -w "HTTP Status: %{http_code}\nResponse Time: %{time_total}s\n"

# 预期结果: HTTP 200, 返回完整的首页数据
```

##### 性能测试
```bash
# 并发请求测试
for i in {1..10}; do
  curl -X GET "http://localhost:8080/api/home/dashboard" &
done
wait

# 预期结果: 所有请求成功，缓存生效
``` 
# 优惠券管理系统 (coupon-lite)

基于 Spring Boot 的轻量级优惠券管理系统，提供会员管理、优惠券发放与核销等核心功能。

## 技术栈

- **框架**: Spring Boot 2.3.1.RELEASE
- **语言**: Java 8
- **数据库**: MySQL + Redis + MongoDB
- **ORM**: MyBatis Plus 3.5.3.1
- **文档**: Swagger 2.9.2

## 项目结构

```
src/main/java/com/coupon/
├── common/           # 通用模块
│   ├── annotation/   # 注解（如 @ApiLimit）
│   ├── config/       # 配置类（Web、Redis、Swagger等）
│   ├── enums/        # 枚举（优惠券类型、角色等）
│   ├── exception/    # 异常处理
│   └── util/         # 工具类
├── controller/       # 控制器
├── dto/              # 数据传输对象
├── entity/           # 实体类
├── mapper/           # 数据访问层
├── service/          # 业务逻辑层
│   └── impl/         # 服务实现
└── DemoApplication.java
```

## 核心功能

### 1. 会员管理

| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| `/api/v1/member/login-or-register` | POST | 微信登录/注册 |
| `/api/v1/member` | GET | 查询会员详情 |
| `/api/v1/member/page` | GET | 分页条件查询会员列表 |
| `/api/v1/member/my-coupon` | GET | 查看我的优惠券列表 |

### 2. 优惠券管理

| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| `/api/v1/coupon` | POST | 新增/编辑优惠券模板 |
| `/api/v1/coupon` | DELETE | 删除优惠券 |
| `/api/v1/coupon` | GET | 查询单个优惠券详情 |
| `/api/v1/coupon/page` | GET | 分页条件查询优惠券列表 |
| `/api/v1/coupon/receive` | POST | 领取优惠券 |

### 3. 核销管理

| API路径 | 方法 | 功能描述 |
|---------|------|----------|
| `/api/v1/verification/generate` | POST | 生成核销记录（用户点击去核销） |
| `/api/v1/verification/my-list` | GET | 查询我的核销记录列表 |

## 数据模型

| 实体 | 说明 |
|------|------|
| **Member** | 会员表 |
| **CouponTemplate** | 优惠券模板表 |
| **MemberCoupon** | 用户优惠券表 |
| **VerificationRecord** | 核销记录表 |

## 特性亮点

- **高性能**: Redis缓存 + 数据库原子扣减，支持高并发领取
- **安全性**: 签名验证、防重放攻击、请求限流、分布式锁
- **代码规范**: 分层架构、异常统一处理、DTO与Entity分离
- **可扩展性**: 枚举配置优惠券类型、分页查询支持多条件筛选

## 快速开始

### 环境要求

- JDK 8+
- MySQL 5.7+
- Redis 3.2+

### 启动方式

```bash
# 克隆项目
git clone <repository-url>

# 进入项目目录
cd coupon-lite

# 配置数据库连接 (src/main/resources/application.yml)

# 运行项目
mvn spring-boot:run
```

### 访问地址

- **API文档**: http://localhost:8080/swagger-ui.html
- **应用首页**: http://localhost:8080

## 许可证

MIT License
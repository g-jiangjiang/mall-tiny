# 安全功能增强说明

本项目已实现以下安全功能增强：

## 1. 用户列表导出功能

### 功能描述
- 仅超级管理员可以导出用户列表
- 支持按关键字搜索过滤
- 导出格式为Excel文件
- 对敏感数据（如邮箱）进行脱敏处理

### 使用方式
- 登录超级管理员账号
- 访问 `/admin/export?keyword=关键字` 接口
- 系统将返回Excel文件下载

### 数据脱敏规则
- 邮箱：只显示前3位和@后面的部分，例如 `abc***@example.com`

### 权限控制
- 通过检查用户角色ID（默认为5）或角色名称（"超级管理员"）来验证权限
- 非超级管理员用户访问将返回403 Forbidden错误
- 未认证用户访问将返回401 Unauthorized错误

## 2. 登录防爆破功能

### 功能描述
- 监控用户登录失败行为
- 10分钟内连续登录错误达到5次，锁定账号30分钟
- 锁定时间到期后自动解锁
- 记录登录失败IP和时间

### 实现细节
- 使用数据库表 `ums_admin_login_failure` 记录登录失败信息
- 使用数据库表 `ums_admin_locked` 记录用户锁定状态
- 自动清理过期的失败记录
- 所有配置参数通过 `SecurityConstants` 类统一管理

### 错误提示
- 普通密码错误：提示"密码不正确"
- 触发锁定：提示"密码错误次数过多，账号已被锁定30分钟"
- 已锁定账号登录：提示"账号已被锁定，原因：xxx，解锁时间：xxx"

## 3. 互斥登录功能

### 功能描述
- 同一用户在同一时间只能有一个有效token
- 新登录会使旧token失效
- 登出后清除token记录，允许下次登录

### 实现细节
- 使用Redis存储用户token信息
- JWT过滤器中验证token有效性
- 登录时记录token，登出时清除token
- 所有配置参数通过 `SecurityConstants` 类统一管理

### 用户体验
- 用户在新设备登录后，旧设备的请求将失效
- 旧设备会收到"token已失效，用户已在其他设备登录"的提示

## 数据库表结构

### ums_admin_login_failure（用户登录失败记录表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键ID |
| username | varchar(64) | 用户名 |
| ip | varchar(64) | 登录IP |
| failure_time | datetime | 失败时间 |
| failure_reason | varchar(255) | 失败原因 |

### ums_admin_locked（用户锁定状态表）
| 字段名 | 类型 | 说明 |
|--------|------|------|
| id | bigint | 主键ID |
| username | varchar(64) | 用户名 |
| lock_time | datetime | 锁定时间 |
| unlock_time | datetime | 解锁时间 |
| lock_reason | varchar(255) | 锁定原因 |

## 依赖项

新增了以下依赖项：
- Apache POI (poi): 5.2.3 - Excel文件处理
- Apache POI (poi-ooxml): 5.2.3 - Excel OOXML格式支持

## 配置说明

所有配置参数通过 `SecurityConstants` 类统一管理：

```java
// 超级管理员角色ID
public static final Long SUPER_ADMIN_ROLE_ID = 5L;

// 超级管理员角色名称
public static final String SUPER_ADMIN_ROLE_NAME = "超级管理员";

// 登录失败最大次数
public static final int MAX_LOGIN_FAILURES = 5;

// 登录失败时间窗口（分钟）
public static final int LOGIN_FAILURE_WINDOW_MINUTES = 10;

// 锁定时长（分钟）
public static final int LOCK_DURATION_MINUTES = 30;

// Redis用户token键前缀
public static final String REDIS_KEY_USER_TOKEN_PREFIX = "ums:user:token:";
```

## 测试

提供了以下测试类：
- `UmsSecurityServiceTest`: 基础安全服务测试
- `SecurityIntegrationTest`: 安全功能集成测试
- `UmsAdminControllerTest`: 控制器功能测试

测试覆盖了以下场景：
- 登录防爆破功能（正常登录和超过阈值）
- 互斥登录功能（新登录使旧token失效）
- 用户导出权限控制（超级管理员和普通用户）
- 数据脱敏功能
- 账号自动解锁功能
- 登出功能

## 代码质量改进

1. **消除硬编码**：将所有常量提取到 `SecurityConstants` 类中统一管理
2. **权限控制优化**：超级管理员权限检查同时支持角色ID和角色名称
3. **错误处理增强**：提供更友好的错误提示信息
4. **代码复用**：提取公共方法，减少重复代码
5. **测试覆盖**：添加全面的单元测试和集成测试
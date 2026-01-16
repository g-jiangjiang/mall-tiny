# 角色组织架构树功能说明

## 功能概述

本功能实现了基于角色的组织架构树管理，支持最多5层层级结构，顶层固定为"全公司"，二层默认为部门管理员。支持灵活的数据范围配置，包括全部数据权限、自定义数据权限、本部门数据权限、本部门及以下数据权限、仅本人数据权限。

## 数据库设计

### 新增表

1. **ums_department** - 组织架构表
   - id: 部门ID
   - parent_id: 父部门ID，0表示顶级部门（全公司）
   - name: 部门名称
   - level: 层级（1-5）
   - sort: 排序
   - status: 状态（0->禁用；1->启用）
   - description: 描述
   - create_time: 创建时间
   - update_time: 更新时间

2. **ums_role_dept_relation** - 角色部门关联表
   - id: 主键ID
   - role_id: 角色ID
   - dept_id: 部门ID
   - create_time: 创建时间

### 扩展表

1. **ums_role** - 角色表新增字段
   - data_scope: 数据范围（1->全部数据权限；2->自定义数据权限；3->本部门数据权限；4->本部门及以下数据权限；5->仅本人数据权限）
   - dept_id: 部门ID，当数据范围为3或4时使用

2. **ums_admin** - 用户表新增字段
   - dept_id: 部门ID

## 数据范围说明

| data_scope | 说明 | 使用场景 |
|-----------|------|---------|
| 1 | 全部数据权限 | 超级管理员，可以查看和操作所有数据 |
| 2 | 自定义数据权限 | 可以查看和操作指定部门的数据 |
| 3 | 本部门数据权限 | 只能查看和操作本部门的数据 |
| 4 | 本部门及以下数据权限 | 可以查看和操作本部门及子部门的数据 |
| 5 | 仅本人数据权限 | 只能查看和操作自己的数据 |

## API接口

### 组织架构管理 (UmsDepartmentController)

| 接口 | 方法 | 说明 |
|------|------|------|
| /department/tree | GET | 获取组织架构树 |
| /department/tree/{rootId} | GET | 根据根节点ID获取组织架构树 |
| /department/create | POST | 创建部门 |
| /department/update | POST | 更新部门 |
| /department/delete/{id} | POST | 删除部门 |
| /department/{id} | GET | 获取部门详情 |
| /department/children/{parentId} | GET | 获取子部门列表 |
| /department/allChildren/{deptId} | GET | 获取所有子部门ID（包含所有层级） |

### 角色数据范围管理 (UmsRoleDataScopeController)

| 接口 | 方法 | 说明 |
|------|------|------|
| /role/dataScope/update | POST | 更新角色数据范围 |
| /role/dataScope/deptIds/{roleId} | GET | 获取角色数据范围部门ID列表 |
| /role/dataScope/{roleId} | GET | 获取角色详情（包含部门信息） |
| /role/dataScope/allocDept | POST | 为角色分配部门（自定义数据范围） |
| /role/dataScope/allocatedDeptIds/{roleId} | GET | 获取角色已分配的部门ID列表 |

## 使用示例

### 1. 初始化数据库

执行SQL脚本：`sql/role_dept_migration.sql`

该脚本会：
- 创建组织架构表
- 修改角色表，添加数据范围字段
- 修改用户表，添加部门字段
- 创建角色部门关联表
- 初始化默认数据（全公司部门）
- 为现有角色设置默认数据范围

### 2. 创建部门

```json
POST /department/create
{
  "parentId": 1,
  "name": "技术部",
  "sort": 0,
  "status": 1,
  "description": "技术研发部门"
}
```

### 3. 获取组织架构树

```json
GET /department/tree
```

返回树形结构的组织架构数据。

### 4. 设置角色数据范围

```json
POST /role/dataScope/update
{
  "roleId": 5,
  "dataScope": 1
}
```

超管设置为全部数据权限。

```json
POST /role/dataScope/update
{
  "roleId": 1,
  "dataScope": 3,
  "deptId": 2
}
```

部门管理员设置为本部门数据权限。

```json
POST /role/dataScope/update
{
  "roleId": 2,
  "dataScope": 2,
  "deptIds": [2, 3, 4]
}
```

自定义数据权限，指定多个部门。

### 5. 获取角色的数据范围部门ID列表

```json
GET /role/dataScope/deptIds/5
```

返回该角色可以访问的部门ID列表。

## 业务规则

1. **层级限制**：部门层级最多为5层，顶层固定为"全公司"（ID=1）
2. **部门名称唯一性**：同级部门名称不能重复
3. **删除限制**：存在子部门的部门不允许删除
4. **全公司保护**：全公司部门（ID=1）不允许修改和删除
5. **数据范围默认值**：新角色默认数据范围为1（全部数据权限）
6. **旧数据兼容**：现有角色默认设置为本部门数据权限（dept_id=1）

## 代码结构

```
src/main/java/com/macro/mall/tiny/modules/ums/
├── controller/
│   ├── UmsDepartmentController.java          # 组织架构控制器
│   └── UmsRoleDataScopeController.java       # 角色数据范围控制器
├── dto/
│   ├── UmsDepartmentNode.java               # 组织架构树节点DTO
│   ├── RoleDataScopeParam.java              # 角色数据范围参数
│   └── UmsDepartmentParam.java              # 部门参数
├── mapper/
│   ├── UmsDepartmentMapper.java            # 组织架构Mapper
│   └── UmsRoleDeptRelationMapper.java       # 角色部门关联Mapper
├── model/
│   ├── UmsDepartment.java                   # 组织架构实体
│   └── UmsRoleDeptRelation.java            # 角色部门关联实体
└── service/
    ├── UmsDepartmentService.java            # 组织架构服务接口
    ├── impl/
    │   └── UmsDepartmentServiceImpl.java    # 组织架构服务实现
    ├── UmsRoleDataScopeService.java         # 角色数据范围服务接口
    └── impl/
        └── UmsRoleDataScopeServiceImpl.java # 角色数据范围服务实现
```

## 注意事项

1. **禁止滥改pom.xml**：本功能不涉及pom.xml修改，完全基于现有依赖
2. **旧数据兼容**：SQL脚本会为现有角色设置默认数据范围，确保旧数据兼容
3. **代码健壮性**：所有Service方法都进行了参数校验和异常处理
4. **事务管理**：涉及多表操作的方法都添加了@Transactional注解
5. **递归查询**：使用MySQL的WITH RECURSIVE语法实现递归查询子部门

# 1. **需求**

1.  添加任务，支持多父多子
2. 添加关系，任务之间不能闭环
3. 不同任务使用不同的执行器
4. 按序提交任务，并发执行任务。用**RabbitMQ(RocketMQ/Kafka)**

1. 1. [消息队列原理和选型：Kafka、RocketMQ、RabbitMQ 和 ActiveMQ - 掘金](https://juejin.cn/post/7096095180536676365)

1. 两个E 情况，查询实例状态加锁
2. 设计消息投递内容
3. 生产者提交A，消费者自己投递剩下的
4. 节点执行内容
5. 节点版本（历史版本）
6. 节点超时终止（线程交互，共享变量，volatile）
7. 自定义数据源（每个任务设置MySQL配置信息）
8. 节点重试
9. 前端页面
10. 定时调度
11. 重构
12. 设置查询引擎、计算引擎
13. 无缝切换执行引擎
14. SQL语法转换（待定）
15. K8s一体化平台
16. JVM 调优





![img](D:\文档\Markdown\新建 MD 文件\image.png)

# 2. **设计**

## 2.1. **数据库设计**

1. 任务表

| 字段         | 类型        |
| ------------ | ----------- |
| id           | 任务id/主键 |
| job_name     |             |
| job_detail   |             |
| job_executor |             |
| create_time  |             |
| update_time  |             |
| is_deleted   |             |

1. 实例表

| 字段        | 类型        |
| ----------- | ----------- |
| id          | 实例id/主键 |
| job_id      |             |
| status      |             |
| batch       |             |
| message     |             |
| create_time |             |
| update_time |             |
| is_deleted  |             |

1. 关系表

| 字段        | 类型        |
| ----------- | ----------- |
| id          | 关系id/主键 |
| rel_name    |             |
| rel_head    |             |
| rel_tail    |             |
| create_time |             |
| update_time |             |
| is_deleted  |             |

1. 执行器表

| 字段        | 类型          |
| ----------- | ------------- |
| id          | 执行器id/主键 |
| exe_name    |               |
| exe_api     |               |
| exe_type    |               |
| create_time |               |
| update_time |               |
| is_deleted  |               |





## 2.2. **接口设计**

1. int createJob(Sting jobName, Sting jobDetail, Sting jobExecutor)

   a.  jobExecutor用枚举类型

2. Boolean createRelation(String relName, int relHeadId, int relTailId)

   a. 判断关系头、尾是否存在

   b. 判断是否有环

3. Boolean startJob(int jobId)

   a. 判断任务是否存在

   b. 判断任务是否为根节点

   c. 判断是否有环

   d. 提交根节点任务

# 3. **开发**

# 4. **测试**

# 5. **文档**

# 6. **上线**

# 7. **运维**
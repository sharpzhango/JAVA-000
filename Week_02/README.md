学习笔记

## GC分析

### 工具

- GCeasy
- GCViewer

### 串行GC 日志情况

`java -XX:+UseSerialGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

单线程执行，minor gc执行次数 `约等于` full gc 执行次数，

minor gc 平均执行时间：26.7ms

full gc 平均执行时间：45ms

![](https://raw.githubusercontent.com/sharpzhango/JAVA-000/main/Week_02/GC%20%20%E7%BB%9F%E8%AE%A1%E5%9B%BE/Serial%20GC.png)

### 并行GC 日志情况

`java -XX:+UseParallelGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

**Key Performance Indicators**：

> 吞吐量：30.57%

> 延时：min：17.7ms  max： 40ms

多线程执行，minor gc执行次数 `三倍于` full gc 执行次数，

minor gc 平均执行时间：7.65ms

full gc 平均执行时间：35.6ms

![](https://raw.githubusercontent.com/sharpzhango/JAVA-000/main/Week_02/GC%20%20%E7%BB%9F%E8%AE%A1%E5%9B%BE/Parellal%20GC.png)

`java -XX:+UseParallelGC -Xms4g -Xmx4g -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

**Key Performance Indicators**：

> 吞吐量：44.32%

> 延时：min：83.3ms  max： 100ms



### CMS GC 日志情况

java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis

**Key Performance Indicators**：

> 吞吐量：32.99%

> 延时：min：19.7ms  max： 60ms



并发执行，minor gc执行次数 `六倍于` full gc 执行次数

![](https://raw.githubusercontent.com/sharpzhango/JAVA-000/main/Week_02/GC%20%20%E7%BB%9F%E8%AE%A1%E5%9B%BE/CMS_512m%20GC.png)

`java -XX:+UseConcMarkSweepGC -Xms4g -Xmx4g -Xloggc:gc.demo.log_CMS_4g -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

**Key Performance Indicators**：

> 吞吐量：35.43%

> 延时：min：71.7ms  max： 80ms



### G1 GC 日志情况

`java -XX:+UseG1GC -Xms512m -Xmx512m -Xloggc:gc.demo.log -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

**Key Performance Indicators**：

> 吞吐量：45.75%

> 延时：min：3.56ms  max： 40ms

并发执行，Young GC 执行次数 `17倍于` Full GC

![](https://raw.githubusercontent.com/sharpzhango/JAVA-000/main/Week_02/GC%20%20%E7%BB%9F%E8%AE%A1%E5%9B%BE/G1_512m.png)

`java -XX:+UseG1GC -Xms4g -Xmx4g -Xloggc:gc.demo.log_G1_4g -XX:+PrintGCDetails -XX:+PrintGCDateStamps GCLogAnalysis`

**Key Performance Indicators**：

> 吞吐量：60.63%

> 延时：min：26.9ms  max： 60ms

![](https://raw.githubusercontent.com/sharpzhango/JAVA-000/main/Week_02/GC%20%20%E7%BB%9F%E8%AE%A1%E5%9B%BE/G1_4g.png)

### 总结

> 对于这个例子来说512m 4g Xmx来说，G1 GC 从吞吐量和延时上来说都是最好的选择
>
> 对于以后的大堆程序，觉得以后可以首选G1 

### 线程堆栈 分析工具（jstack -l pid > xxxstack.log）

- fastthread
- jconsole
- VisualVM 

### 内存分析工具

- jhat

## JVM问题调优经验

1. 高分配速率（新对象生成速度太快）

>  分配速率(Allocation rate)表示单位时间内分配的内存量。通常 使用 MB/sec 作为单位。上一次垃圾收集之后，与下一次 GC 开 始之前的年轻代使用量，两者的差值除以时间,就是分配速率。
>
> 分配速率过高就会严重影响程序的性能，在 JVM 中可能会导致巨 大的 GC 开销。
>
> 正常系统:分配速率较低 ~ 回收速率 -> 健康 内存泄漏:分配速率 持续大于 回收速率 -> OOM 性能劣化:分配速率较高 ~ 回收速率 -> 亚健康



在某些情况下，只要增加年轻代的大小，即可降低分配速率过高所造成的影响。 增加年轻代空间并不会降低分配速率，但是会减少 GC 的频率。如果每次 GC 后 只有少量对象存活，minor GC 的暂停时间就不会明显增加。



2. 过早提升（年轻代 放不下大量的对象）

> 提升速率(promotion rate)用于衡量单位时间内从年轻代提 升到老年代的数据量。一般使用 MB/sec 作为单位, 和分配速率 类似。
>
> JVM 会将长时间存活的对象从年轻代提升到老年代。根据分代假 设，可能存在一种情况，老年代中不仅有存活时间长的对象,， 也可能有存活时间短的对象。这就是过早提升:对象存活时间还 不够长的时候就被提升到了老年代。
>
> major GC 不是为频繁回收而设计的，但 major GC 现在也要清 理这些生命短暂的对象，就会导致 GC 暂停时间过长。这会严重 影响系统的吞吐量。

一是设置 JVM 启动参数,增加年轻代的大小。

二是减少每次批处理的数量，也能得到类似的结果。

至于选用哪个方案，要根据业务需求决定。 在某些情况下，业务 逻辑不允许减少批处理的数量，那就只能增加堆内存，或者重新 指定年轻代的大小。 如果都不可行，就只能优化数据结构，减少 内存消耗。

但总体目标依然是一致的:让临时数据能够在年轻代存放得下



## GC疑难情况问题分析（先码上，慢慢理解）

1、查询业务日志，可以发现这类问题:请求压力大，波峰，遭遇降级，熔断等等， 基础服务、外部 API

2、查看系统资源和监控信息:

硬件信息、操作系统平台、系统架构;

排查 CPU 负载、内存不足，磁盘使用量、硬件故障、磁盘分区用满、IO 等待、IO 密集、丢数据、并发竞 争等情况;

排查网络:流量打满，响应超时，无响应，DNS 问题，网络抖动，防火墙问题，物理故障，网络参数调整、 超时、连接数。

3、查看性能指标，包括实时监控、历史数据。可以发现 假死，卡顿、响应变慢等现象;

排查数据库， 并发连接数、慢查询、索引、磁盘空间使用量、内存使用量、网络带宽、死锁、TPS、查询 数据量、redo日志、undo、 binlog 日志、代理、工具 BUG。可以考虑的优化包括: 集群、主备、只读 实例、分片、分区;

大数据，中间件，JVM 参数。

 4、排查系统日志， 比如重启、崩溃、Kill 。 

5、APM，比如发现有些链路请求变慢等等。

6、排查应用系统

排查配置文件: 启动参数配置、Spring 配置、JVM 监控参数、数据库参数、Log 参数、APM 配置、

内存问题，比如是否存在内存泄漏，内存溢出、批处理导致的内存放大、GC 问题等等;

GC 问题， 确定 GC 算法、确定 GC 的 KPI，GC 总耗时、GC 最大暂停时间、分析 GC 日志和监控指标: 内存 分配速度，分代提升速度，内存使用率等数据。适当时修改内存配置;

排查线程, 理解线程状态、并发线程数，线程 Dump，锁资源、锁等待，死锁;
 排查代码， 比如安全漏洞、低效代码、算法优化、存储优化、架构调整、重构、解决业务代码 BUG、第三方

库、XSS、CORS、正则;
 单元测试: 覆盖率、边界值、Mock 测试、集成测试。

7、 排除资源竞争、坏邻居效应

 8、疑难问题排查分析手段
 DUMP 线程\内存; 抽样分析\调整代码、异步化、削峰填谷。
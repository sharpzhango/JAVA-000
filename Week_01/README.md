[toc]

# 学习笔记

## 类加载

类的生命周期：类加载 使用 卸载

类加载 ： 加载 链接 初始化（生成Class对象，保存在Metaspace中）

### 类加载器

> 目的：将 .calss文件 变成 Class对象

双亲委托模式： BootStrap --> Extension --> app

双亲委托的目的：保护Java核心类，当你写一个 java.lang.String 的时候，JVM不会用直接加载使用，而是向上查找看双亲有没有加载。

### 类加载的作用

- 自定义类加载器可以用来实现给 .class文件加密的功能。

- 查看Jar包路径问题 

## JMM

> JVM是一个完整的计算机模型，所以它有对应的内存模型，Java内存模型（JMM），广义来说JMM分为两个部分：
>
> - JVM 内存结构
> - JMM 与线程规范（第一次课提及较少）

![简单关系图](https://raw.githubusercontent.com/sharpzhango/JAVA-000/main/Week_01/%E5%86%85%E5%AD%98%E5%8F%82%E6%95%B0%E5%85%B3%E7%B3%BB%E5%9B%BE/JVM%20Paramaters.png)

Java栈帧组成：

1. 本地方法表
2. 操作数栈
3. 返回值
4. Class对象指针

几种变量的存储：

1. 局部基本变量 局部引用变量的地址值 存在Java栈中
2. 引用变量本体（对象实例）包括实例的成员变量 存在堆中
3. 类信息 包括类的静态变量。存在non-heap中

常量池中保存 字面量 符号引用量

> 字面量：基本数据类型 字符串 被 final 声明的值

> 符号引用量：类和接口全限定名 字段的名称和描述 方法的名称和描述

## 工具

### 命令行

1. jps 查找java进程

- `jps -l` 如果是以 class 方式运行，会显示进程的主类 `main.class` 的全名，如果是以 jar 包方式运行的，就会输出 jar 包的完整路径名：

- `jps -v` 输出传递给 JVM 的参数，`v` 表示虚拟机，`jps -vl` 比较常见的组合；
- `jps -m` 输出传递给 `main.class` 方法的参数，实用的一个命令，`jps -ml` 比较实用的组合，会显示包名/类名/参数

2. jstat 查看gc信息

   jstat -gcutil pid 1000 1000

3. jmap 查看heap **jamp 会产生 STW**

   Options: -heap - histo -dump (_获取堆转储信息_)

4. jstack 查看线程

5. jcmd 整合命令

启动的命令：

> java命令 【这里是jvm参数，java命令用的】 jar包或类名 【这里是命令行参数，要给类的main方法的】
>
> 一句话理解一下，java用什么参数启动什么类，最后给这个类传什么参数

### 图形化

1. jconsole
2. visualGC
3. jvisualvm
4. jmc

## GC

### GC 一般原理

> GC作用于 heap ，heap 是JVM内存结构中最大的一块，新生对象很快就会无用，年老对象往往可以存很久 

1. 引用追踪(_不用引用计数的原因_)，清除不可达的对象

   - GC root 的对象 ：
     1. 当前方法的参数 变量
     2. 活动的线程
     3. 所有类的静态字段

2. 三个GC策略

   - mark & copy 新生代
   - mark & sweep 老年代
   - mark sweep & compress 老年代

   

### 具体GC方法

- 串行

  1. Serial GC (STW 年轻代 mark-copy  老年代  mark-sweep-compress 都使用单线程处理)
  2. ParNew GC(STW  年轻代，可以开启多个线程 配合 CMS GC 使用)

- 并行

  1. Parallel GC (STW 年轻代 mark-copy  老年代 mark-sweep-compress 运行时占据所有CPU核心，**吞吐量最大**，两次GC期间，没有GC线程运行，不占用资源)

- 并发 

  1. CMS GC (年轻代 mark-copy STW  ｜老年代 mark-sweep，GC线程与应用线程一起抢占CPU时间，**降低延时**)

     > CMS GC **复杂 实验性 过时** ，为了降低延时现在有更好的选择

  2. G1 GC (不再单独划分年轻代 老年代，默认化为2048的 region )

     > 将STW停顿的时间和 分布，变成可预期且可配置的，堆内存超过4G以上选择G1 比较好

  3. ZGC （JDK11 登陆Linux  JDK15 Mac Windows）

     > 目前最强GC 延时<10ms  堆内存支持 TB级别

  总结：

  脱离场景谈性能都是耍流氓”。 目前绝大部分Java应用系统，堆内存并不大比如2G-4G以内，而且对10ms这种低延迟的GC暂停不敏感，也就 是说处理一个业务步骤，大概几百毫秒都是可以接受的，GC暂停100ms还是10ms没多大区别。另一方面， 系统的吞吐量反而往往是我们追求的重点，这时候就需要考虑采用**并行GC**。

  如果堆内存再大一些，可以考虑G1 GC。如果内存非常大(比如超过16G，甚至是64G、128G)，或者是对 延迟非常敏感(比如高频量化交易系统)，就需要考虑使用本节提到的新GC(ZGC/Shenandoah)

### GC核心参数

- NewRatio:  Old/Young
- SurvivorRatio: Eden/Survivor  
- NewSize: 初始 Young区 大小
- OldSize: 初始 Old区 大小


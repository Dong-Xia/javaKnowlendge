# 在项目中部署redis的读写分离架构（包含节点间认证口令）
<!-- MarkdownTOC -->
- [1. 部署redis从节点slaveNode](#1-部署redis从节点slaveNode)
- [2. redis从节点的生产环境启动方案](#2-redis的生产环境启动方案)
- [3. 强制读写分离](#3-强制读写分离)
- [4. 集群安全认证](#4-集群安全认证)
- [5. 读写分离架构的测试](#5-读写分离架构的测试)
- [6. 操作指令](#4-操作指令)
<!-- /MarkdownTOC -->
# 1. 部署redis从节点slaveNode
    wget http://downloads.sourceforge.net/tcl/tcl8.6.1-src.tar.gz
    tar -xzvf tcl8.6.1-src.tar.gz
    cd  /usr/local/tcl8.6.1/unix/
    ./configure  
    make && make install
    
    使用redis-redis-4.0.11.tar.gz
    tar -zxvf redis-redis-4.0.11.tar.gz
    cd redis-4.0.1
    make && make test && make install

# 2. redis的生产环境启动方案

如果一般的学习课程，你就随便用redis-server启动一下redis，做一些实验，这样的话，没什么意义

要把redis作为一个系统的daemon进程去运行的，每次系统启动，redis进程一起启动

 1. redis utils目录下，有个redis_init_script脚本
 2. 将redis_init_script脚本拷贝到linux的/etc/init.d目录中，将redis_init_script重命名为redis_6379，6379是我们希望这个redis实例监听的端口号
 3. 修改redis_6379脚本的第6行的REDISPORT，设置为相同的端口号（默认就是6379）
 4. 创建两个目录：/etc/redis（存放redis的配置文件），/var/redis/6379（存放redis的持久化文件）
 5. 修改redis配置文件（默认在根目录下，redis.conf），拷贝到/etc/redis目录中，修改名称为6379.conf

6. 修改redis.conf中的部分配置为生产环境


    daemonize	yes		让redis以daemon后台守护进程运行
    pidfile		/var/run/redis_6379.pid 	设置redis的pid文件位置
    port		6379						设置redis的监听端口号
    dir 		/var/redis/6379		设置redis运行时持久化文件的存储位置(RDB和AOF)

7. 启动redis，执行cd /etc/init.d, chmod 777 redis_6379（将该文件赋予为可读可写可执行的权限），./redis_6379 start

8. 确认redis进程是否启动，ps -ef | grep redis

9. 让redis跟随系统启动自动启动


    在redis_6379脚本中，最上面，加入两行注释

        # chkconfig:   2345 90 10

        # description:  Redis is a persistent key-value database
     
     #使配置生效
        chkconfig redis_6379 on

在slave node上6379.conf配置：slaveof 主机ip 主机端口 如：192.168.1.1 6379

# 3. 强制读写分离

基于主从复制架构，实现读写分离

redis slave node只读，默认开启，

    #修改6379.conf配置文件中从节点只允许读操作
    slave-read-only yes

开启了只读的redis slave node，会拒绝所有的写操作，这样可以强制搭建成读写分离的架构

# 4. 集群安全认证

- master主节点上启用安全认证，在6379.conf文件中配置：requirepass 从节点密码
- slave从节点连接口令，在6379.conf文件中配置： masterauth 密码

# 5. 读写分离架构的测试

先启动主节点，eshop-cache01上的redis实例
再启动从节点，eshop-cache02上的redis实例

刚才调试了一下，redis slave node一直说没法连接到主节点的6379的端口

**在搭建生产环境的集群的时候，不要忘记修改一个配置bind，bind 127.0.0.1 -> 本地的开发调试的模式，就只能127.0.0.1本地才能访问到6379的端口。**

    #修改绑定的ip，配置redis所有的主节点和从节点
    redis.conf中的bind 127.0.0.1 -> bind主机的ip地址
    #开启6379端口
    在每个节点上都: iptables -A INPUT -ptcp --dport  6379 -j ACCEPT

# 6. 操作指令
    #连接redis
    redis-cli -h ipaddr -a 密码
    
    # 看配置的主从节点信息
    info replication
    
    #关闭redis
    redis-cli shutdown
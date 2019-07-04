# 哨兵sentinel配置文件解读和在虚拟机中3台主机上搭建3个节点的哨兵集群
<!-- MarkdownTOC -->
- [1. 前期准备](#1-前期准备)
- [2. 哨兵的配置文件解读](#2-哨兵的配置文件解读)
- [3. 配置3个节点的哨兵集群](#3-配置3个节点的哨兵集群)
- [4. 搭建哨兵集群遇见的问题记录](#4-搭建哨兵集群遇见的问题记录)
<!-- /MarkdownTOC -->
# 1. 前期准备
在虚拟机上安装好3台centos主机集群，并按照前面讲到的部署好redis读写分离的架构；在redis 安装的文件中
（如：/home/ncut/redis）下找到哨兵sentinel.conf配置文件。

# 2. 哨兵的配置文件解读
1. 每一个哨兵都可以去监控多个maser-slaves的主从架构，因为可能你的公司里，为不同的项目，部署了多个master-slaves的redis主从集群
相同的一套哨兵集群，就可以去监控不同的多个redis主从集群。
你自己给每个redis主从集群分配一个逻辑的名称

        # 给redis集群分配一个名称 mymaster,设置不同的名称可以监控多个redis主从集群
        sentinel monitor mymaster 127.0.0.1 6379 2
        sentinel down-after-milliseconds mymaster 60000
        sentinel failover-timeout mymaster 180000
        sentinel parallel-syncs mymaster 1

        # 给redis集群分配一个名称 resque
        sentinel monitor resque 192.168.1.3 6380 4
        sentinel down-after-milliseconds resque 10000
        sentinel failover-timeout resque 180000
        sentinel parallel-syncs resque 5

上面这段配置，就监控了两个master node

    sentinel monitor mymaster 127.0.0.1 6379 
    
    类似这种配置，来指定对一个master的监控，给监控的master指定的一个名称，因为后面分布式集群架构里会讲解，可以配置多个master做数据拆分

2. 配置参数解读
- 这是最小的哨兵配置，如果发生了master-slave故障转移，或者新的哨兵进程加入哨兵集群，那么哨兵会自动更新自己的配置文件

            sentinel monitor master-group-name hostname port quorum (如：sentinel monitor mymaster 127.0.0.1 6379)
    
        quorum的解释如下：
        
        （1）至少多少个哨兵要一致同意，master进程挂掉了，或者slave进程挂掉了，或者要启动一个故障转移操作
        （2）quorum是用来识别故障的，真正执行故障转移的时候，还是要在哨兵集群执行选举，选举一个哨兵进程出来执行故障转移操作
        （3）假设有5个哨兵，quorum设置了2，那么如果5个哨兵中的2个都认为master挂掉了; 2个哨兵中的一个就会做一个选举，选举一个哨兵出来，执行故障转移; 如果5个哨兵中有3个哨兵都是运行的，那么故障转移就会被允许执行

- sentinel哨兵判断redis失去连接的配置参数解读
        
        # 超过10秒没有连接上redis,就认为redis挂掉了
        sentinel down-after-milliseconds resque 10000
        
        # 故障转移执行时间超过3分钟则失败
        sentinel failover-timeout resque 180000
        
        # 设置数字5：表示同时进行5个slave从主机挂到新的master上面去
        sentinel parallel-syncs resque 5
 　　上面的3个配置作用：       
(1) down-after-milliseconds：超过多少毫秒跟一个redis实例断了连接，哨兵就可能认为这个redis实例挂了。

(2) parallel-syncs：新的master被切换之后，同时有多少个slave被切换到去连接新master，重新做同步，数字越低，花费的时间越多。

假设你的redis是1个master，4个slave；然后master宕机了，4个slave中有1个切换成了master，剩下3个slave就要挂到新的master上面去；

这个时候，如果parallel-syncs是1，那么3个slave，一个一个地挂接到新的master上面去，1个挂接完，而且从新的master同步完数据之后，再挂接下一个；

如果parallel-syncs是3，那么一次性就会把所有slave挂接到新的master上去

(3) failover-timeout：执行故障转移的timeout超时时长

# 3. 配置3个节点的哨兵集群
　　**本次使用了三个sentinel来构成sentinel网络，所以在这里设置为2，一般要启动奇数个sentinel，以保证不会出现正反方投票数相等的情况。**
1. 配置master中哨兵sentinel.conf相关配置

- 哨兵默认用26379端口，默认不能跟其他机器在指定端口连通，只能在本地访问，设置端口为5000
    
        # 新建哨兵配置文件的存放目录       
        mkdir /etc/sentinal
        
        # 新建哨兵的工作目录 
        mkdir -p /var/sentinal/5000
        
        # 将redis目录下sentinel.conf复制到/etc/sentinel目录下
        /etc/sentinel/sentinel.conf
        # 重名配置文件
        mv /etc/sentinel/sentinel.conf /etc/sentinel/5000.conf

        # 编辑5000.conf
        vi /etc/sentinel/5000.conf
        
        # master配置
        port 5000
        # 绑定自己的主机的ip，不然不能进行主机间访问
        bind 10.4.9.20
        dir /var/sentinal/5000
        sentinel monitor mymaster 10.4.9.20 6379 2
        sentinel down-after-milliseconds mymaster 30000
        sentinel failover-timeout mymaster 60000
        sentinel parallel-syncs mymaster 1
        # 设置保护模式
        protected-mode no
        # 设置访问redis的权限密码，和redis指点的密码相同
        sentinel auth-pass mymaster ncut
        
        # slave配置
        port 5000
        bind 10.4.9.211
        dir /var/sentinal/5000
        sentinel monitor mymaster 10.4.9.20 6379 2
        sentinel down-after-milliseconds mymaster 30000
        sentinel failover-timeout mymaster 60000
        sentinel parallel-syncs mymaster 1
         # 设置保护模式
         protected-mode no
        # 设置访问redis的权限密码，和redis指点的密码相同
        sentinel auth-pass mymaster ncut         
        
        # slave配置
        port 5000
        bind 10.4.9.222
        dir /var/sentinal/5000
        sentinel monitor mymaster 10.4.9.20 6379 2
        sentinel down-after-milliseconds mymaster 30000
        sentinel failover-timeout mymaster 60000
        sentinel parallel-syncs mymaster 1
        # 设置保护模式
        protected-mode no
        # 设置访问redis的权限密码，和redis指点的密码相同
        sentinel auth-pass mymaster ncut         

2. 配置redis主从集群中redis.conf相关配置
    protected-mode ：关闭保护模式。**默认情况下，redis node和sentinel的protected-mode都是yes，在搭建集群时，
    若想从远程连接redis集群，需要将redis node和sentinel的protected-mode修改为no，若只修改redis node，
    从远程连接sentinel后，依然是无法正常使用的，且sentinel的配置文件中protected-mode配置为no**。依据redis文档的说明，若protected-mode设置为no后，需要增加密码证或是IP限制等保护机制，否则是极度危险的，
    所以我们集群中都是添加了密码访问。
        
        # 配置redis配置文件中关闭保护模式（所有的redis主从主机都需配置）
        vi /etc/redis/6379.conf
        # 关闭保护模式
        protected-mode no
        
        # 配置哨兵配置文件关闭保护模式（所有的哨兵主从主机都需配置）
        vi /etc/sentinel/5000.conf
        # 关闭保护模式
        protected-mode no        
 
3. 启动哨兵进程

　　在准备好的ncut-cache01、ncut-cache02、ncut-cache03三台机器上，分别启动三个哨兵进程，组成一个集群，观察一下日志的输出

    # 下面两个命令都可以启动哨兵
    redis-sentinel /etc/sentinel/5000.conf
    redis-server /etc/sentinel/5000.conf --sentinel

日志里会显示出来，每个哨兵都能去监控到对应的redis master，并能够自动发现对应的slave

哨兵之间，互相会自动进行发现，用的就是之前说的pub/sub，消息发布和订阅channel消息系统和机制

4. 检查哨兵状态

        # 5000为哨兵的端口号
        redis-cli -h 10.4.9.20 -p 5000
        
        # 查看哨兵集群中master信息
        sentinel master mymaster
        
         # 查看哨兵集群中所有slaves信息
        sentinel slaves mymaster
        
         # 查看哨兵集群中所有sentinels哨兵节点信息
        sentinel sentinels mymaster
        
        # 通过哨兵集群的名称获取master信息
        sentinel get-master-addr-by-name mymaster

# 4. 搭建哨兵集群遇见的问题记录
1. 启动哨兵集群时就出现sdown（一个哨兵主观判断自己监控的redis主机已经挂掉了）
- 原因分析：哨兵通过远程没有访问到redis主机，由于redis和哨兵都设置了保护模式。
　　　　　　**默认情况下，redis node和sentinel的protected-mode都是yes，在搭建集群时，
          　若想从远程连接redis集群，需要将redis node和sentinel的protected-mode修改为no，若只修改redis node，
           从远程连接sentinel后，依然是无法正常使用的，且sentinel的配置文件中protected-mode配置为no**。
- 解决办法：同时对redis.conf和sentinel.conf关闭保护模式。

2. 在redis主从集群完全正常的情况下，启动哨兵时无法加载slave相关信息，始终没有在sentinel的窗口中看到slave

![哨兵没发现slave节点](/src/main/images/redis/哨兵没发现slave节点.jpg)
- 原因分析：redis配置的密码访问权限，哨兵没有配置密码访问，导致无法访问redis主机
- 解决办法：在sentinel配置文件中配置密码访问
    
         # 设置访问redis的权限密码，和redis指点的密码相同
         sentinel auth-pass mymaster ncut
         
3. 密码权限设置正确，但是启动哨兵报错

![哨兵问题错误.png](/src/main/images/redis/哨兵问题错误.png)
- 解决办法：调整配置的放置顺序
    
        此配置　　　sentinel monitor <master-name> <ip> <redis-port> <quorum>
        
        需放在　　　
        
        此配置　　　sentinel auth-pass <mymaster> <0123passw0rd>
        
        的前面。

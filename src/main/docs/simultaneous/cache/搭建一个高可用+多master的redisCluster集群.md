# 在项目中搭建一个高可用+多master的redis cluster集群
<!-- MarkdownTOC -->
- [1. redis-cluster基本知识](#1-redis-cluster基本知识)
- [2. 在三台机器上准备6个redis实例](#2-在三台机器上准备6个redis实例)
- [3. 建立集群](#3-建立集群)
    - [3.1 系统默认ruby版本过低，导致Redis接口安装失败](#31-系统默认ruby版本过低，导致Redis接口安装失败)
- [4. redis-cluster进行水平扩容](#4-redis-cluster进行水平扩容)
<!-- /MarkdownTOC -->
# 1. redis-cluster基本知识
### redis cluster: **自动完成master+slave复制和读写分离，master+slave高可用和主备切换**，支持多个master的hash slot支持数据分布式存储。
### redis cluster的重要配置
- cluster-enabled <yes/no> : 开启redis cluster集群配置

- cluster-config-file <filename>：这是指定一个文件，供cluster模式下的redis实例将集群状态保存在那里，包括集群中其他机器的信息，比如节点的上线和下限，故障转移，不是我们去维护的，给它指定一个文件，让redis自己去维护的

- cluster-node-timeout <milliseconds>：节点存活超时时长，超过一定时长，认为节点宕机，master宕机的话就会触发主备切换，slave宕机就不会提供服务

## 2. 在三台机器上准备6个redis实例
（1）在ncut-cache01、ncut-cache02、ncut-cache03上部署目录

/etc/redis（存放redis的配置文件），/var/redis/6379（存放redis的持久化文件）

（2）编写配置文件

redis cluster集群，要求至少3个master，去组成一个高可用，健壮的分布式的集群，每个master都建议至少给一个slave，3个master，3个slave，最少的要求

正式环境下，建议都是说在6台机器上去搭建，至少3台机器。

保证，每个master都跟自己的slave不在同一台机器上，如果是6台自然更好，一个master+一个slave就死了

    3台机器去搭建6个redis实例的redis cluster
    
    mkdir -p /etc/redis-cluster
    mkdir -p /var/log/redis
    mkdir -p /var/redis/7001
    
    vi /etc/redis/7001.conf
    port 7001
    cluster-enabled yes
    cluster-config-file /etc/redis-cluster/node-7001.conf
    cluster-node-timeout 15000
    daemonize	yes							
    pidfile		/var/run/redis_7001.pid 						
    dir 		/var/redis/7001		
    logfile /var/log/redis/7001.log
    bind 10.4.9.20		
    appendonly yes
    # 安全配置（所有6台redis机器都需要设置相同的访问密码）
    masterauth ncut 
    requirepass ncut

像上面的配置，在/etc/redis/下放置需要配置的6台redis实例，分别端口为7001、7002、7003、7004、7005、7006，命名为7001.conf，7002.conf，7003.conf，7004.conf，7005.conf，7006.conf。

（3）准备生产环境的启动脚本

在/etc/init.d下，放6个启动脚本，分别为: redis_7001, redis_7002, redis_7003, redis_7004, redis_7005, redis_7006，
并分别配置这六个文件中端口号为对应的端口
    
    # 配置端口
    REDISPORT=7001

（4）分别在3台机器上，启动6个redis实例。

## 3. 建立集群
(1) 准备ruby的运行环境，是由于redis-trib.rb是采用ruby语言进行编写的：

    yum install -y ruby
    yum install -y rubygems
    gem install redis

(2) 配置gem编译的redis文件

由于我们的6个redis实例都是设置了安全认证的密码，在建立集群的时候回导致redis实例不能访问的如下错误：

    ./redis-trib.rb check 10.4.9.20:7001，则会报错ERR] Sorry, can’t connect to node 10.4.9.20:7001
解决办法：

    vim /usr/local/rvm/gems/ruby-2.4.6/gems/redis-4.0.0/lib/redis/client.rb，然后修改passord为对应redis的密码

(3) 执行redis-trib.rb建立集群

    cp /usr/local/redis-4.0.0/src/redis-trib.rb /usr/local/bin
    
    redis-trib.rb create --replicas 1 10.4.9.20:7001 10.4.9.20:7002 10.4.9.211:7003 10.4.9.211:7004 10.4.9.222:7005 10.4.9.222:7006    
    
    --replicas: 每个master有几个slave

建议：6台机器，3个master，3个slave，尽量自己让master和slave不在一台机器上，不然机器出问题后，主备都挂掉了，不能保证可用性。

(4) 查看建立好的集群状态：
    
    redis-trib.rb check 10.4.9.20:7001
## 3.1 系统默认ruby版本过低，导致Redis接口安装失败
   在执行gem install redis安装redis接口时，提示ruby版本过低问题，如图：
   ![安装ruby版本过低](/src/main/images/redis/安装ruby版本过低.jpg)

解决步骤：   
   (1) 查看系统默认当前 ruby 版本，输入命令 " ruby -v "；
   
   (2) RVM需要通过CRUL来进行下载，那么我们要先下载CUEL，CURL是什么呢，它是Linux下的文件传输工具，利用URL的规则在命令行下工作，
        
        输入命令 " yum install curl " 进行安装;
  
   (3) 使用curl安装rvm ，
   
        输入命令 " curl -L get.rvm.io | bash -s stable " 进行安装
        
        # 提示无法检查签名：没有公钥，运行下面命令
        gpg --keyserver hkp://keys.gnupg.net --recv-keys 409B6B1796C275462A1703113804BB82D39DC0E3
        curl -L get.rvm.io | bash -s stable
        
   (4) 使用source让当前shell读入路径为" /usr/local/rvm/scripts/rvm "（路径可以自定义）的shell文件并依次执行文件中的所有语句，并重新执行刚修改的初始化文件，使之立即生效，而不必注销并重新登录，
   
   (5) 输入命令 " rvm list known " 进行查询ruby版本
   
   (6) 选择一个你喜欢的版本进行安装，但首先提醒一下，你所选择的版本不能低于提示要求的版本就可以了，输入命令 " rvm install 2.4.6 " 进行安装
   
   (7) 使用刚才安装完成的Ruby版本，输入命令 " rvm use 2.4.6 " 
  
   (8) 移除系统中默认的版本号，输入命令 " rvm remove 2.0.0 " 进行移除
   
   (9) 输入命令 " ruby -v "查看是否成功 

   
## 4. redis-cluster进行水平扩容
redis cluster模式下，不建议做物理的读写分离了。这个和哨兵模式下的一主多从的读写分离不一样。

**建议通过master的水平扩容，来横向扩展读写吞吐量，还有支撑更多的海量数据**，比如下面举例：

    redis单机，读吞吐是5w/s，写吞吐2w/s
    
    扩展redis更多master，那么如果有5台master，不就读吞吐可以达到总量25/s QPS，写可以达到10w/s QPS
    
    redis单机，内存，6G，8G，fork类操作的时候很耗时，会导致请求延时的问题
    
    扩容到5台master，能支撑的总的缓存数据量就是30G，40G -> 100台，600G，800G，甚至1T+，海量数据

(1) 加入新master

    mkdir -p /var/redis/7007
    
    port 7007
    cluster-enabled yes
    cluster-config-file /etc/redis-cluster/node-7007.conf
    cluster-node-timeout 15000
    daemonize	yes							
    pidfile		/var/run/redis_7007.pid 						
    dir 		/var/redis/7007		
    logfile /var/log/redis/7007.log
    bind 10.4.9.223		
    appendonly yes

搞一个7007.conf，再搞一个redis_7007启动脚本

手动启动一个新的redis实例，在7007端口上

    # redis-trib.rb add-node 新master 集群中的redis实例
    redis-trib.rb add-node 10.4.9.223:7007 10.4.9.20:7001
    
    redis-trib.rb check 10.4.9.20:7001

连接到新的redis实例上，cluster nodes，确认自己是否加入了集群，作为了一个新的master

(2) reshard一些数据过去

resharding的意思就是把一部分hash slot从一些node上迁移到另外一些node上

    redis-trib.rb reshard 10.4.9.20:7001

要把之前3个master上，总共4096个hashslot迁移到新的第四个master上去

How many slots do you want to move (from 1 to 16384)?

1000

(3) 添加node作为slave

    ncut-cache04
    
    mkdir -p /var/redis/7008
    
    port 7008
    cluster-enabled yes
    cluster-config-file /etc/redis-cluster/node-7008.conf
    cluster-node-timeout 15000
    daemonize	yes							
    pidfile		/var/run/redis_7008.pid 						
    dir 		/var/redis/7008		
    logfile /var/log/redis/7008.log
    bind 10.4.9.223	
    appendonly yes
    
    redis-trib.rb add-node --slave --master-id 28927912ea0d59f6b790a50cf606602a5ee48108 10.4.9.223:7008 10.4.9.20:7001

(4) 删除node

先用resharding将数据都移除到其他节点，确保node为空之后，才能执行remove操作

    redis-trib.rb del-node 10.4.9.20:7001 bd5a40a6ddccbd46a0f4a2208eb25d2453c2a8db

2个是1365，1个是1366

当你清空了一个master的hashslot时，redis cluster就会自动将其slave挂载到其他master上去

这个时候就只要删除掉master就可以了

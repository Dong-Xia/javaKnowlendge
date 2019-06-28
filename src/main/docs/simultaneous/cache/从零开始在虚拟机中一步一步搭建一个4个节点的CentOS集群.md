# 从零开始在虚拟机中一步一步搭建一个4个节点的CentOS集群
<!-- MarkdownTOC -->
- [1. 在虚拟机中安装CentOS](#1-在虚拟机中安装CentOS)
    - [1.1 软件](#11-软件)
    - [1.2 具体步骤](#12-具体步骤)
- [2. 在每个CentOS中都安装Java和Perl](#2-在每个CentOS中都安装Java和Perl)
- [3. 在4个虚拟机中安装CentOS集群](#3-在4个虚拟机中安装CentOS集群)
- [4. 配置4台CentOS为ssh免密码互相通信](#4-配置4台CentOS为ssh免密码互相通信)
<!-- /MarkdownTOC -->
# 1. 在虚拟机中安装CentOS
## 1.1 软件
    cenos版本：CentOS-7-x86_64-DVD-1804.iso
    虚拟机：Oracle VM VirtualBox
    window：win10  内存16G  
    jdk版本：jdk1.8.0_181
    perl版本：perl 5.16.3
## 1.2 具体步骤
启动一个virtual box虚拟机管理软件（vmware，发现不太稳定，发现每次休眠以后再重启，集群就挂掉了）

virtual box，发现很稳定，集群从来不会随便乱挂，所以就一直用virtual box了

（1）使用课程提供的CentOS 7镜像即可，CentOS-7-x86_64-DVD-1804.iso。
（2）创建虚拟机：打开Virtual Box，点击“新建”按钮，点击“下一步”，输入虚拟机名称为eshop-cache01，选择操作系统为Linux，选择版本为Other linux(64-bit)，分配2048MB内存，后面的选项全部用默认，在Virtual Disk File location and size中，一定要自己选择一个目录来存放虚拟机文件，最后点击“create”按钮，开始创建虚拟机。
（3）设置虚拟机网卡：选择创建好的虚拟机，点击“设置”按钮，在网络一栏中，连接方式中，选择“Bridged Adapter”。
（4）安装虚拟机中的CentOS 7操作系统：选择创建好的虚拟机，点击“开始”按钮，选择安装介质（即本地的CentOS 7镜像文件），自己开始安装。
（5）安装完以后，CentOS会提醒你要重启一下，就是reboot，你就reboot就可以了。

（6）配置网络

    vi /etc/sysconfig/network-scripts/ifcfg-enp0s3
    #设置为静态ip 
    BOOTPROTO=static
    #配置ip地址和网关，按照宿主机的ip网段和网关进行配置
    IPADDR=10.4.9.20
    NETMASK=255.255.255.0
    GATEWAY=10.4.9.1
    DNS1=183.221.253.100
    DNS2=10.4.9.2
    
    #编辑完成后，按ESC -> : -> WQ 保存修改，重启网卡后生效
    service network restart

（7）配置hosts

- 为主机命名使用hostnamectl set-hostname 命令修改主机名，可永久生效。
    
        hostnamectl set-hostname ncut-cache01
    
- 配置本机的hostname到ip地址的映射

        vi /etc/hosts
        10.4.9.20 ncut-cache01

（8）配置xshell

此时就可以使用xshell从本机连接到虚拟机进行操作了

一般来说，虚拟机管理软件，virtual box，可以用来创建和管理虚拟机，但是一般不会直接在virtualbox里面去操作，因为比较麻烦，没有办法复制粘贴

比如后面我们要安装很多其他的一些东西，perl，java，redis，storm，复制一些命令直接去执行

xshell，在windows宿主机中，去连接virtual box中的虚拟机

可以下载xshell for home 面向家庭版免费

（9）关闭防火墙

    service iptables stop
    service ip6tables stop
    chkconfig iptables off
    chkconfig ip6tables off
    
    vi /etc/selinux/config
    SELINUX=disabled

关闭windows的防火墙

后面要搭建集群，有的大数据技术的集群之间，在本地你给了防火墙的话，可能会没有办法互相连接，会导致搭建失败

（10）配置yum

    yum clean all
    yum makecache
    yum install wget
    
# 2. 在每个CentOS中都安装Java和Perl
yum install -y gcc

wget http://www.cpan.org/src/5.0/perl-5.16.1.tar.gz
tar -xzf perl-5.16.1.tar.gz
cd perl-5.16.1
./Configure -des -Dprefix=/usr/local/perl
make && make test && make install
perl -v

为什么要装perl？我们整个大型电商网站的详情页系统，复杂。java+nginx+lua，需要perl。

perl，是一个基础的编程语言的安装，tomcat，跑java web应用

# 3. 在4个虚拟机中安装CentOS集群
（1）按照上述步骤，采用VirtualBox镜像（.vdi)复制备份，复制出三台一样的机器：
        
        进入VirtualBox的安装目录，里面有个VBoxManage.exe文件，我们可以通过这个文件来复制vdi。通过cmd进入安装目录，执行如下：
        VBoxManage clonehd    E:\centos1.vdi     E:\centos2.vdi
（2）在VirtualBox中新建虚拟机后，在虚拟硬盘中选择使用已有的虚拟硬盘文件对上面复制的。vdi文件进行加载

![新建虚拟机](/src/main/images/simultaneous/新建虚拟机.jpg)

（3）另外三台机器的hostname分别设置为ncut-cache02，ncut-cache03，ncut-cache04
（4）配置网络，按照修改相应的ip地址
    
        vi /etc/sysconfig/network-scripts/ifcfg-enp0s3
        #设置为静态ip 
        BOOTPROTO=static
        #配置ip地址和网关，按照宿主机的ip网段和网关进行配置
        IPADDR=10.4.9.20
        NETMASK=255.255.255.0
        GATEWAY=10.4.9.1
        DNS1=183.221.253.100
        DNS2=10.4.9.2
        
        #编辑完成后，按ESC -> : -> WQ 保存修改，重启网卡后生效
        service network restart
（5）安装好之后，在每台机器的hosts文件里面，配置好所有的机器的ip地址到hostname的映射关系

在ncut-cache01、ncut-cache02、ncut-cache03、ncut-cache04的hosts里面进行配置
    
    vi /etc/hosts
    10.4.9.20  ncut-cache01
    10.4.9.211 ncut-cache02
    10.4.9.39  ncut-cache03
    10.4.9.30  ncut-cache04

# 4. 配置4台CentOS为ssh免密码互相通信

- 首先在三台机器上配置对本机的ssh免密码登录

    
    ssh-keygen -t rsa
    生成本机的公钥，过程中不断敲回车即可，ssh-keygen命令默认会将公钥放在/root/.ssh目录下
    cd /root/.ssh
    将公钥复制为authorized_keys文件
    cp id_rsa.pub authorized_keys
    此时使用ssh连接本机就不需要输入密码了:ssh hostname
    ssh ncut-cache01

- 接着配置三台机器互相之间的ssh免密码登录
　　
        
        使用 ssh-copy-id -i hostname 命令将**本机的公钥拷贝到指定机器**的authorized_keys文件中

　　如需将主机名ncut-cache02的公钥拷贝到ncut-cache01中，则需在主机 ncut-cache02 中使用 ssh-copy-id -i ncut-cache01，

    在主机ncut-cache02、ncut-cache03、ncut-cache04上都执行 ssh-copy-id -i ncut-cache01后，将各自的公钥
    全部拷贝至主机ncut-cache01文件/root/.ssh/authorized_keys 中。这样就实现主机ncut-cache01到主机ncut-cache02、ncut-cache03、ncut-cache04
    的免密登录访问

- 将配置好所有主机的公钥文件进行复制到其他主机，不用其它主机再使用 ssh-copy-id -i hostname命令一台一台地重复配置
    
    
    scp authorized_keys ncut-cache02:/root/.ssh
    scp authorized_keys ncut-cache03:/root/.ssh
    scp authorized_keys ncut-cache04:/root/.ssh


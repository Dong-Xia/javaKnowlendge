# 传统IO和NIO的区别
# 传统IO
### 特点
　　每建立一次连接则会被阻塞，一个连接只为一个客户请求服务，比较浪费资源和影响性能。如图所示：相当于每一个客人需配一个服务员，这样
如果来了100客人，则需相应搭配100个服务员，这样在现实中是不存在的。
　　
    
    重要特点：
    １、单线程只能接受一个客户端访问。
    ２、使用线程池可以有多个客户端访问，但是非常耗费性能。
    

![传统阻塞io举例](/src/main/images/networkProgramming/传统阻塞io举例.jpg)

### 具体实现
　　在下面建立了一个socket服务端，用于接收客户端请求
    
        public void bioServer(){
            //建立线程池
            ExecutorService executor = Executors.newCachedThreadPool();
    
            // 建立socket服务器
            try {
                ServerSocket serverSocket = new ServerSocket(10010);
                System.out.println("服务器启动");
                // 获取连接
                while (true) {
                    // 获取一个套接字（阻塞）
                    final Socket socket = serverSocket.accept();
                    System.out.println("来了一个新的客户端");
                    // 来一个就新建一个线程为其提供服务，弊端：浪费资源
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // 业务处理
                            handle(socket);
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
　　业务处理实现，用于接收客户端输入

    public void handle(Socket socket){
        try {
            byte[] bytes = new byte[1024];
            InputStream inputStream = socket.getInputStream();

            while (true){
                //读取数据（阻塞）
                int read = inputStream.read(bytes);
                if (read != -1) {
                    System.out.println("输入处理完成");
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            System.out.println("socket关闭");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
# NIO
### 特点
　　使用方法对比如下表所示：

  NIO | IO 
  
  :-: | :-:
  
  ServerSocketChannel |	ServerSocket
    
  SocketChannel	| Socket
    
  Selector |
    
  SelectionKey |
 
　　NIO核心采用多路复用，其在单线程下可以有多个客服端进行连接，如下图所示，当客户端连接到达
selector选择器后，可以安排空闲的服务员对客户进行服务，当该服务员服务完成后，又可以对其它客户提供服务。
从而节约资源，符合现实中的资源最大利用率。

![NIO多路复用图](/src/main/images/networkProgramming/NIO多路复用图.jpg)


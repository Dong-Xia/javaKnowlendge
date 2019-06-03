package com.java.javaknowledge.bio;

import com.sun.org.apache.xpath.internal.operations.String;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 传统io的流程
 */
public class BioServer {

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


    public static void main(String[] args) {
        new BioServer().bioServer();
    }
}

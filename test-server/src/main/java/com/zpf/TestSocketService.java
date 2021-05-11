package com.zpf;

import com.zpf.rpc.registry.DefaultServiceRegistry;
import com.zpf.rpc.registry.ServiceRegistry;
import com.zpf.rpc.socket.server.SocketServer;

/**
 * @author zpf
 * @createTime 2021-05-11 9:45
 * 测试：
 * 服务提供方 com.zpf.rpc.socket.server
 */
public class TestSocketService {
    public static void main(String[] args) {
        // 服务提供方返回的应该是接口
        HelloService helloService = new HelloServiceImpl();
        // 服务的注册 注册完服务后，服务器就启动了
        // 应该将服务器和注册中心解耦
        // 将要提供的服务对象注册到注册中心
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer rpcServer = new SocketServer(serviceRegistry);
        rpcServer.start(9000);
    }
}
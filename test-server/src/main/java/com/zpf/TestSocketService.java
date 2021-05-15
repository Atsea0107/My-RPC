package com.zpf;

import com.zpf.rpc.provider.ServiceProviderImpl;
import com.zpf.rpc.provider.ServiceProvider;
import com.zpf.rpc.serializer.CommonSerializer;
import com.zpf.rpc.serializer.KryoSerializer;
import com.zpf.rpc.transport.socket.server.SocketServer;

/**
 * @author zpf
 * @createTime 2021-05-11 9:45
 * 测试：
 * 服务提供方 com.zpf.rpc.transport.socket.server
 */
public class TestSocketService {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl2();
        SocketServer socketServer = new SocketServer("127.0.0.1", 9998, CommonSerializer.HESSIAN_SERIALIZER);
        socketServer.publishService(helloService, HelloService.class);
    }
}

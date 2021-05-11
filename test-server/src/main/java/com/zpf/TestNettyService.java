package com.zpf;

import com.zpf.rpc.netty.server.NettyServer;
import com.zpf.rpc.registry.DefaultServiceRegistry;
import com.zpf.rpc.registry.ServiceRegistry;

/**
 * @author zpf
 * @createTime 2021-05-11 9:45
 * 测试：
 * 服务提供方
 */
public class TestNettyService {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry registry = new DefaultServiceRegistry();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }
}

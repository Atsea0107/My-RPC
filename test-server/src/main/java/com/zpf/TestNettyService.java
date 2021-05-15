package com.zpf;

import com.zpf.rpc.serializer.CommonSerializer;
import com.zpf.rpc.serializer.HessianSerializer;
import com.zpf.rpc.serializer.KryoSerializer;
import com.zpf.rpc.serializer.ProtobufSerializer;
import com.zpf.rpc.transport.netty.server.NettyServer;
import com.zpf.rpc.provider.ServiceProviderImpl;
import com.zpf.rpc.provider.ServiceProvider;

/**
 * @author zpf
 * @createTime 2021-05-11 9:45
 * 测试：
 * 服务提供方
 */
public class TestNettyService {
    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        NettyServer server = new NettyServer("127.0.0.1", 9999, CommonSerializer.PROTOBUF_SERIALIZER);
        server.publishService(helloService, HelloService.class);
    }
}

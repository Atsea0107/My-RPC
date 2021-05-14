package com.zpf;

import com.zpf.rpc.serializer.KryoSerializer;
import com.zpf.rpc.transport.RpcClient;
import com.zpf.rpc.transport.RpcClientProxy;
import com.zpf.rpc.transport.netty.client.NettyClient;

/**
 * @author zpf
 * @createTime 2021-05-11 17:02
 */
public class TestNettyClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient();
        client.setSerializer(new KryoSerializer());

        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}

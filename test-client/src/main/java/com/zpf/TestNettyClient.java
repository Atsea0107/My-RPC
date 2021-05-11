package com.zpf;

import com.zpf.rpc.RpcClient;
import com.zpf.rpc.RpcClientProxy;
import com.zpf.rpc.netty.client.NettyClient;

/**
 * @author zpf
 * @createTime 2021-05-11 17:02
 */
public class TestNettyClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.hello(object);
        System.out.println(res);
    }
}

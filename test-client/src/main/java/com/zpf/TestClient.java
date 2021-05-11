package com.zpf;

import client.RpcClientProxy;

/**
 * @author zpf
 * @createTime 2021-05-11 9:49
 * 测试：
 * 消费者 —— client
 */
public class TestClient {
    public static void main(String[] args) {
        // 使用代理类去指定的ip:port调用服务
        RpcClientProxy rpcClientProxy = new RpcClientProxy("127.0.0.1", 9000);
        // 通过代理类获取实例对象
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);

        // 服务端返回方法调用的返回值
        HelloObject msg = new HelloObject(231, "ahigh is");
        String hello = helloService.hello(msg);
        System.out.println(hello);
    }
}

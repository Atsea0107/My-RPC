package com.zpf;

import com.zpf.rpc.RpcClient;
import com.zpf.rpc.RpcClientProxy;
import com.zpf.rpc.socket.client.SocketClient;

/**
 * @author zpf
 * @createTime 2021-05-11 9:49
 * 测试：
 * 消费者 —— com.zpf.rpc.socket.client
 */
public class TestClient {
    public static void main(String[] args) {
        RpcClient rpcClient = new SocketClient("127.0.0.1", 9000);
        // 使用代理类去指定的ip:port调用服务
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient);
        // 通过代理类获取实例对象
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);

        // 服务端返回方法调用的返回值
        HelloObject msg = new HelloObject(231, "ahigh is");
        String hello = helloService.hello(msg);
        System.out.println(hello);
    }
}

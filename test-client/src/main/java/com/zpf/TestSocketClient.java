package com.zpf;

import com.zpf.rpc.serializer.CommonSerializer;
import com.zpf.rpc.serializer.KryoSerializer;
import com.zpf.rpc.transport.RpcClient;
import com.zpf.rpc.transport.RpcClientProxy;
import com.zpf.rpc.transport.socket.client.SocketClient;

/**
 * @author zpf
 * @createTime 2021-05-11 9:49
 * 测试：
 * 消费者 —— com.zpf.rpc.transport.socket.client
 */
public class TestSocketClient {
    public static void main(String[] args) {
        SocketClient client = new SocketClient();
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        for(int i = 0; i < 20; i ++) {
            String res = helloService.hello(object);
            System.out.println(res);
        }
    }
}

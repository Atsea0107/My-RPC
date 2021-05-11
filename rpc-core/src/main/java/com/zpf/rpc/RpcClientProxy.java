package com.zpf.rpc;

import com.zpf.entity.RpcRequest;
import com.zpf.rpc.socket.client.SocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author zpf
 * @createTime 2021-05-10 21:40
 * com.zpf.rpc.RpcClient 动态代理
 * Client 端一侧没法直接生成实例对象（因为只有接口，没有具体的实现类）
 * 通过动态代理的方式生成实例
 * JDK动态代理，通过实现InvocationHandler接口，然后实现invoke方法
 */
public class RpcClientProxy implements InvocationHandler {
    private static final Logger logger = LoggerFactory.getLogger(RpcClientProxy.class);
    // 代理类需要去服务器获取服务
    private RpcClient rpcClient;

    public RpcClientProxy(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    /**
     * 获取代理类的实例对象
     * @param clazz
     * @param <T>
     * @return
     */
    // Unchecked cast: 'java.lang.Object' to 'T'
    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    /**
     * 方法PRC调用，返回server处理后的结果数据
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        logger.info("调用方法: {} # {}", method.getDeclaringClass().getName(), method.getName());
        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());
        return rpcClient.sendRequest(rpcRequest);
    }
}

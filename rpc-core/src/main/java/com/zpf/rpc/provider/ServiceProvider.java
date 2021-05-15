package com.zpf.rpc.provider;

/**
 * @author zpf
 * @createTime 2021-05-11 11:01
 * 保存和提供服务实例对象
 */
public interface ServiceProvider {
    // 注册服务
    // 方法前要声明是泛型
    <T> void addServiceProvider(T service, Class<T> serviceClass);
    // 获取服务
    Object getServiceProvider(String serviceName);
}

package com.zpf.rpc.registry;

/**
 * @author zpf
 * @createTime 2021-05-11 11:01
 * 服务注册中心
 * 应该是个容器
 */
public interface ServiceRegistry {
    // 注册服务
    // 方法前要声明是泛型
    <T> void registry(T service);
    // 获取服务
    Object getService(String serviceName);
}

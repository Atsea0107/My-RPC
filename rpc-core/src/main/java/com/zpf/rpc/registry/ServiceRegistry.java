package com.zpf.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author zpf
 * @createTime 2021-05-11 21:29
 * 服务注册中心通用接口
 */
public interface ServiceRegistry {
    /**
     * 将一个服务注册进注册表
     *
     * @param serviceName       服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    void register(String serviceName, InetSocketAddress inetSocketAddress);

}

package com.zpf.rpc.provider;

import com.zpf.enumeration.RpcError;
import com.zpf.exception.RpcException;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zpf
 * @createTime 2021-05-11 11:04
 * 默认的服务注册表
 * 容器，则HashMap，为了线程安全ConcurrentHashMap
 * key - value ：服务名(接口名，String)：提供服务的实例对象(Object)
 * set：保存已经注册的对象
 */
public class ServiceProviderImpl implements ServiceProvider {
    private static final Logger logger = LoggerFactory.getLogger(ServiceProviderImpl.class);

    private static final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    /*
     * 线程安全的HashSet，用来存储server要发布出去的服务的  接口实现类名
     * 保证只有一个对象。一个接口实现类只能有一个对象提供服务
     */
    private static final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void addServiceProvider(T service, Class<T> serviceClass) {
        String serviceName = serviceClass.getCanonicalName();
        if(registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        serviceMap.put(serviceName, service);
        logger.info("向接口：{} 注册服务{}", service.getClass().getInterfaces(), serviceName);
    }

    @Override
    public synchronized Object getServiceProvider(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}

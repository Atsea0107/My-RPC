package registry;

import com.zpf.enumeration.RpcError;
import com.zpf.exception.RpcException;
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
public class DefaultServiceRegistry implements ServiceRegistry {
    private static final Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    /*
     * 线程安全的HashSet，用来存储server要发布出去的服务的  接口实现类名
     * 保证只有一个对象。一个接口实现类只能有一个对象提供服务
     */
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    /**
     * @param service 服务器要注册服务的实例对象
     * @param <T>
     */
    @Override
    public synchronized <T> void registry(T service) {
        String serviceName = service.getClass().getName();
        if(registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);

        Class<?>[] interfaces = service.getClass().getInterfaces();
        if (interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLMENT_ANY_INTERFACE);
        }
        for (Class<?> i : interfaces){
            // 采用这个对象实现的接口的完整类名作为服务名
            // 一个接口对应一个对象 todo? why? 一个接口有多个实现类怎么办。就要求一个接口只能有一个实现类吗？
            serviceMap.put(i.getCanonicalName(), service);
        }
        logger.info("向接口：{} 注册服务{}", interfaces, serviceName);
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if (service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}

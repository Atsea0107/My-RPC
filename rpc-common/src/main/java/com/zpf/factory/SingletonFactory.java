package com.zpf.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zpf
 * @createTime 2021-05-15 14:41
 * 单例工厂
 */
public class SingletonFactory {
    /**
     * 因为是工厂，所以是容器
     */
    private static Map<Class, Object> objectMap = new HashMap<>();

    private SingletonFactory() {
    }

    public static <T> T getInstance(Class<T> clazz){
        /**
         * 不用ConcurrentHashMap的原因：
         * 用con 同样要使用synchronized来锁住Class对象，
         * 他只能保证对容器的put操作是同步的，
         * 现在的目标不是对容器进行同步操作
         */
        Object instance = objectMap.get(clazz);
        synchronized (clazz){
            if(instance == null){
                try {
                    instance = clazz.newInstance();
                    objectMap.put(clazz, instance);
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        }
        return clazz.cast(instance);
    }
}

package com.zpf.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author zpf
 * @createTime 2021-05-15 15:37
 * 轮询
 * 并发安全？ 并发安全是线程的问题
 * 启动多个服务端是多个进程，不存在线程安全问题
 */
public class RoundRobinLoadBalancer implements LoadBalancer {
    private int index = 0;
    @Override
    public Instance select(List<Instance> instances) {
        if(index >= instances.size()){
            index %= instances.size();
        }
        return instances.get(index++);
    }
}

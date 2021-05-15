package com.zpf.rpc.registry;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zpf.rpc.loadbalancer.LoadBalancer;
import com.zpf.rpc.loadbalancer.RandomLoadBalancer;
import com.zpf.util.NacosUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zpf
 * @createTime 2021-05-15 15:45
 */
public class NacosServiceDiscovery implements ServiceDiscovery {
    private static final Logger logger = LoggerFactory.getLogger(NacosServiceDiscovery.class);

    private final LoadBalancer loadBalancer;

    public NacosServiceDiscovery() {
        this(new RandomLoadBalancer());
    }

    public NacosServiceDiscovery(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
    }

    @Override
    public InetSocketAddress lookupService(String serviceName) {
        try {
            // 获取全部实例
            List<Instance> instances = NacosUtil.getAllInstance(serviceName);
            Instance instance = loadBalancer.select(instances);
            return new InetSocketAddress(instance.getIp(), instance.getPort());
        } catch (NacosException e) {
            logger.error("获取服务时有错误发生:", e);
        }
        return null;
    }
}

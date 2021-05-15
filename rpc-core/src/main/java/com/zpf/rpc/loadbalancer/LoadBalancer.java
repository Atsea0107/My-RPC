package com.zpf.rpc.loadbalancer;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * @author zpf
 * @createTime 2021-05-15 15:35
 */
public interface LoadBalancer {
    Instance select(List<Instance> instances);
}

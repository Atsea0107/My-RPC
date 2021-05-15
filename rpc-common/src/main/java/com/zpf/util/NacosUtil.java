package com.zpf.util;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zpf.enumeration.RpcError;
import com.zpf.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @author zpf
 * @createTime 2021-05-15 15:04
 * 管理Nacos连接等工具类
 */
public class NacosUtil {
    private static final Logger logger = LoggerFactory.getLogger(NacosUtil.class);

    private static final NamingService namingService;
    private static final Set<String> serviceNames = new HashSet<>();
    private static InetSocketAddress socketAddress;

    private static final String SERVER_ADDR = "127.0.0.1:8848";

    static {
        namingService = getNacosNamingService();
    }

    private static NamingService getNacosNamingService() {
        try {
            return NacosFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            logger.error("连接到Nacos时有错误发生: ", e);
            throw new RpcException(RpcError.FAILED_TO_CONNECT_TO_SERVICE_REGISTRY);
        }
    }

    public void registerService(String serviceName, InetSocketAddress inetSocketAddress) throws NacosException {
        // 注册一个实例到服务
        namingService.registerInstance(serviceName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        NacosUtil.socketAddress = inetSocketAddress;
        serviceNames.add(serviceName);
    }
    public static List<Instance> getAllInstance(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }
    public static void clearRegistry(){
        if (!serviceNames.isEmpty() && socketAddress != null){
            String hostName = socketAddress.getHostName();
            int port = socketAddress.getPort();
            Iterator<String> iterator = serviceNames.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                try {
                    namingService.deregisterInstance(next, hostName, port);
                } catch (NacosException e) {
                    logger.error("注销服务{} 失败", serviceNames, e);
                }
            }
        }
    }
}

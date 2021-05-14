package com.zpf.rpc.transport;

import com.zpf.rpc.serializer.CommonSerializer;

/**
 * @author zpf
 * @createTime 2021-05-11 14:21
 * 服务器类通用接口
 */
public interface RpcServer {
    void start();
    // 设置序列化工具
    void setSerializer(CommonSerializer serializer);
    // 发布service
    <T> void publishService(Object service, Class<T> serviceClass);
}

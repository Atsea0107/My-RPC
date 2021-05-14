package com.zpf.rpc.transport;

import com.zpf.entity.RpcRequest;
import com.zpf.rpc.serializer.CommonSerializer;

/**
 * @author zpf
 * @createTime 2021-05-11 14:21
 * 客户端类通用接口
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);

    void setSerializer(CommonSerializer serializer);
}

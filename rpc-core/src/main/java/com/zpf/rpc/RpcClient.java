package com.zpf.rpc;

import com.zpf.entity.RpcRequest;

/**
 * @author zpf
 * @createTime 2021-05-11 14:21
 * 客户端类通用接口
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}

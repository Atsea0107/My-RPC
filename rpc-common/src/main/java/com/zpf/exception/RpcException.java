package com.zpf.exception;

import com.zpf.enumeration.RpcError;

/**
 * @author zpf
 * @createTime 2021-05-11 11:
 * RPC调用的异常
 */
public class RpcException extends RuntimeException {

    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(RpcError rpcError) {
        super(rpcError.getMessage());
    }

    public RpcException(RpcError rpcError, String msg) {
        super(rpcError.getMessage() + ":" + msg);
    }
}

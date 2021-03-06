package com.zpf.entity;

import com.zpf.enumeration.ResponseCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zpf
 * @createTime 2021-05-10 21:20
 * 提供者执行完或者出错后，向消费者返回的结果对象
 */
@Data
public class RpcResponse<T> implements Serializable {
    // 响应状态码
    private Integer statusCode;

    // 响应状态补充信息
    private String message;

    // 响应数据
    private T data;

    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponseCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponseCode code){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}

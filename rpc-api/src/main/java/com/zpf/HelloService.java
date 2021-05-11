package com.zpf;

/**
 * @author zpf
 * @createTime 2021-05-10 20:57
 * 服务端和客户端的公共调用接口
 * 即服务端暴露在外的，
 * 客户端可以直接使用的
 */
public interface HelloService {
    String hello(HelloObject helloObject);
}

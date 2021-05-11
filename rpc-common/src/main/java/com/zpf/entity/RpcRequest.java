package com.zpf.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zpf
 * @createTime 2021-05-10 21:09
 * 消费者向提供者发送的请求对象
 * 即，client向服务器发起request请求——方法调用 需要的信息
 * server只向外提供接口，但具体的实现类是存在于server端的，
 * client要想调用，则需指明
 * 1、调用的接口是什么
 * 2、该接口的哪个方法
 * 3、方法形参 —— 形参具体参数、形参类型
 *
 * @Builder ： 建造者模式 —— 简单认为的 构造器？
 */
@Data
// TODO
@Builder
public class RpcRequest implements Serializable {
    // 接口名
    private String interfaceName;

    // 调用的方法名
    private String methodName;

    // 方法参数
    private Object[] parameters;

    // 方法的参数类型
    private Class<?>[] paramTypes;
}

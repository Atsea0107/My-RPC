package com.zpf.enumeration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * @author zpf
 * @createTime 2021-05-10 21:26
 * ResponseCode 枚举类
 * SUCCESS
 * FAIL
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(200, "调用方法成功"),
    FAIL(500, "调用方法失败"),
    NOT_FOUND_METHOD(500, "未找到指定方法"),
    NOT_FOUND_CLASS(500, "未找到指定类");

    /**
     * 因为枚举类的对象是有限个，所以不提供给外界set的方法
     * 同时属性应该是private final
     * 构造器也是私有的
     */
    private final int code;
    private final String message;
}

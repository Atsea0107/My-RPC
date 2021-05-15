package com.zpf.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zpf
 * @createTime 2021-05-11 15:33
 * 采用的序列化方式
 */
@Getter
@AllArgsConstructor
public enum SerializerCode {
    KRYO(0),
    JSON(1),
    HESSIAN(2),
    PROTOBUF(3);

    private final int code;
}

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
    JSON(1);

    private final int code;
}

package com.zpf.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author zpf
 * @createTime 2021-05-11 15:37
 */
@AllArgsConstructor
@Getter
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;
}

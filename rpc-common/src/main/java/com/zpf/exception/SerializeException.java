package com.zpf.exception;

/**
 * @author zpf
 * @createTime 2021-05-11 21:00
 * 序列化错误，其报错的内容由msg决定
 */
public class SerializeException extends RuntimeException{
    public SerializeException(String msg) {
        super(msg);
    }
}

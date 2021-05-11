package com.zpf.rpc.serializer;

/**
 * @author zpf
 * @createTime 2021-05-11 15:23
 * 通用的序列化 反序列化接口
 */
public interface CommonSerializer {
    // 序列化方法
    byte[] serialize(Object object);
    // 反序列化方法
    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code){
        switch (code){
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }
}

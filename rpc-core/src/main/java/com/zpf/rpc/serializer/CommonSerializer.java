package com.zpf.rpc.serializer;

/**
 * @author zpf
 * @createTime 2021-05-11 15:23
 * 通用的序列化 反序列化接口
 */
public interface CommonSerializer {
    int KRYO_SERIALIZER = 0;
    int JSON_SERIALIZER = 1;
    int HESSIAN_SERIALIZER = 2;
    int PROTOBUF_SERIALIZER = 3;

    int DEFAULT_SERIALIZER = KRYO_SERIALIZER;
    
    static CommonSerializer getByCode(int code){
        switch (code){
            case KRYO_SERIALIZER:
                return new KryoSerializer();
            case JSON_SERIALIZER:
                return new JsonSerializer();
            case HESSIAN_SERIALIZER:
                return new HessianSerializer();
            case PROTOBUF_SERIALIZER:
                return new ProtobufSerializer();
            default:
                return null;
        }
    }
    // 序列化方法
    byte[] serialize(Object object);
    // 反序列化方法
    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();
}

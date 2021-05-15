package com.zpf.rpc.serializer;

import com.zpf.enumeration.SerializerCode;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zpf
 * @createTime 2021-05-15 13:53
 * 使用ProtoBuf的序列化器
 */
public class ProtobufSerializer implements CommonSerializer {

    private LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    private Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    @Override
    public byte[] serialize(Object object) {
        Class<?> aClass = object.getClass();
        Schema schema = getSchema(aClass);
        byte[] data;
        try {
            data = ProtostuffIOUtil.toByteArray(object, schema, buffer);
        } finally {
            buffer.clear();
        }
        return data;
    }


    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        Schema schema = getSchema(clazz);
        Object object = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, object, schema);
        return object;
    }

    @Override
    public int getCode() {
        return SerializerCode.PROTOBUF.getCode();
    }

    private Schema getSchema(Class clazz) {
        Schema schema = schemaCache.get(clazz);
        // 等价于 obj == null
        if (Objects.isNull(schema)) {
            schema = RuntimeSchema.getSchema(clazz);
            if (Objects.nonNull(schema)) {
                schemaCache.put(clazz, schema);
            }
        }
        return schema;
    }
}

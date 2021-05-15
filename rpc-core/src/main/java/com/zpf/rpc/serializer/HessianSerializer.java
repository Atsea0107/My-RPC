package com.zpf.rpc.serializer;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.zpf.enumeration.SerializerCode;
import com.zpf.exception.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author zpf
 * @createTime 2021-05-15 13:30
 * 基于Hessian协议的序列化器
 */
public class HessianSerializer implements CommonSerializer {
    private static final Logger logger = LoggerFactory.getLogger(HessianSerializer.class);

    @Override
    public byte[] serialize(Object object) {
        /**
         * 使用try-with-resources 语法糖需要先实现AutoCloseable接口
         */
        HessianOutput hessianOutput = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessianOutput = new HessianOutput(byteArrayOutputStream);
            hessianOutput.writeObject(object);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            logger.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        } finally {
            if(hessianOutput != null){
                try {
                    hessianOutput.close();
                } catch (IOException e) {
                    logger.error("关闭流时有错误发生:", e);
                }
            }
        }
    }

    /**
     * TODO 反序列化不需要class信息？？
     * @param bytes
     * @param clazz
     * @return
     */
    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        HessianInput hessianInput = null;
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes)) {
            hessianInput = new HessianInput(byteArrayInputStream);
            return hessianInput.readObject();
        }catch (IOException e){
            logger.error("序列化时有错误发生:", e);
            throw new SerializeException("序列化时有错误发生");
        }finally {
            if(hessianInput != null){
                hessianInput.close();
            }
        }
    }

    @Override
    public int getCode() {
        return SerializerCode.HESSIAN.getCode();
    }
}

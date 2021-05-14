package com.zpf.rpc.transport.socket.util;

import com.zpf.entity.RpcRequest;
import com.zpf.enumeration.PackageType;
import com.zpf.rpc.serializer.CommonSerializer;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author zpf
 * @createTime 2021-05-12 10:15
 */
public class ObjectWriter {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    /**
     * 将对象以约定的协议封装，序列化
     */
    public static void writeObject(OutputStream outputStream, Object object, CommonSerializer serializer) throws IOException {
        outputStream.write(intToBytes(MAGIC_NUMBER));
        if (object instanceof RpcRequest) {
            outputStream.write(intToBytes(PackageType.REQUEST_PACK.getCode()));
        } else {
            outputStream.write(intToBytes(PackageType.RESPONSE_PACK.getCode()));
        }
        outputStream.write(intToBytes(serializer.getCode()));
        byte[] bytes = serializer.serialize(object);
        outputStream.write(intToBytes(bytes.length));
        outputStream.write(bytes);
        outputStream.flush();

    }

    private static byte[] intToBytes(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value>>24) & 0xFF);
        src[1] = (byte) ((value>>16)& 0xFF);
        src[2] = (byte) ((value>>8)&0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
}

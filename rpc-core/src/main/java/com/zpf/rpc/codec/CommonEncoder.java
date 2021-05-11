package com.zpf.rpc.codec;

import com.zpf.entity.RpcRequest;
import com.zpf.enumeration.PackageType;
import com.zpf.rpc.serializer.CommonSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author zpf
 * @createTime 2021-05-11 15:20
 * 通用的编码拦截器
 */
public class CommonEncoder extends MessageToByteEncoder {
    private static final int MAGIC_NUMBER = 0xCAFEBABE;

    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object msg, ByteBuf out) throws Exception {
        // 编码协议 4Bytes MAGIC_NUMBER
        out.writeInt(MAGIC_NUMBER);
        // 编码协议 4Bytes PackageType
        if(msg instanceof RpcRequest) {
            out.writeInt(PackageType.REQUEST_PACK.getCode());
        } else {
            out.writeInt(PackageType.RESPONSE_PACK.getCode());
        }
        // 编码协议 4Bytes Serializer Type
        out.writeInt(serializer.getCode());
        // 编码协议 4Bytes Bytes Length
        byte[] bytes = serializer.serialize(msg);
        out.writeInt(bytes.length);
        // 具体数据
        out.writeBytes(bytes);
    }
}

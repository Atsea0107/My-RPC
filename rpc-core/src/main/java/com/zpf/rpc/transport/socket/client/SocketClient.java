package com.zpf.rpc.transport.socket.client;


import com.zpf.entity.RpcRequest;
import com.zpf.entity.RpcResponse;
import com.zpf.enumeration.ResponseCode;
import com.zpf.enumeration.RpcError;
import com.zpf.exception.RpcException;
import com.zpf.rpc.registry.NacosServiceRegistry;
import com.zpf.rpc.registry.ServiceRegistry;
import com.zpf.rpc.serializer.CommonSerializer;
import com.zpf.rpc.transport.RpcClient;
import com.zpf.rpc.transport.socket.util.ObjectReader;
import com.zpf.rpc.transport.socket.util.ObjectWriter;
import com.zpf.util.RpcMessageChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author zpf
 * @createTime 2021-05-10 21:40
 * RPC的消费者 —— com.zpf.rpc.transport.socket.client
 */
public class SocketClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private final ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    public SocketClient() {
        this.serviceRegistry = new NacosServiceRegistry();
    }

    /**
     * client发送方法调用的请求
     *
     * @param rpcRequest 请求内容
     * @return
     */
    public Object sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        InetSocketAddress inetSocketAddress = serviceRegistry.lookupService(rpcRequest.getInterfaceName());
        try (Socket socket = new Socket()) {
            socket.connect(inetSocketAddress);
            OutputStream outputStream = socket.getOutputStream();
            InputStream inputStream = socket.getInputStream();
            ObjectWriter.writeObject(outputStream, rpcRequest, serializer);
            Object object = ObjectReader.readObject(inputStream);
            RpcResponse rpcResponse = (RpcResponse) object;
            if (rpcResponse == null) {
                logger.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()) {
                logger.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            // 检查响应和请求是否一致
            RpcMessageChecker.check(rpcRequest, rpcResponse);
            return rpcResponse.getData();
        } catch (IOException e) {
            logger.error("调用时发生错误：", e);
            throw new RpcException("服务调用失败: ", e);
        }

    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }
}

package com.zpf.rpc.transport.socket.server;

import com.zpf.entity.RpcRequest;
import com.zpf.entity.RpcResponse;
import com.zpf.rpc.handler.RequestHandler;
import com.zpf.rpc.registry.ServiceRegistry;
import com.zpf.rpc.serializer.CommonSerializer;
import com.zpf.rpc.transport.socket.util.ObjectReader;
import com.zpf.rpc.transport.socket.util.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zpf.rpc.provider.ServiceProvider;

import java.io.*;
import java.net.Socket;

/**
 * @author zpf
 * @createTime 2021-05-10 21:40
 * 处理RpcRequest的线程
 */
public class RequestHandlerThread implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerThread.class);
    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceRegistry serviceRegistry;
    private CommonSerializer serializer;

    /**
     * 处理request的线程
     * @param socket 从socket获取输入输出流
     * @param requestHandler 具体的处理器
     * @param serviceRegistry
     * @param serializer
     */
    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry, CommonSerializer serializer) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
        this.serializer = serializer;
    }

    @Override
    public void run() {
        try (OutputStream outputStream = socket.getOutputStream();
             InputStream inputStream = socket.getInputStream()) {
            // 读取请求
            RpcRequest rpcRequest = (RpcRequest) ObjectReader.readObject(inputStream);
            // 根据请求的信息（方法），以及对象，则可以得到处理结果
            Object result = requestHandler.handle(rpcRequest);
            // 返回结果
            RpcResponse<Object> response = RpcResponse.success(result, rpcRequest.getRequestId());
            ObjectWriter.writeObject(outputStream, response, serializer);
        } catch (IOException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}

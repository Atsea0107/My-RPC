package client;

import com.zpf.entity.RpcRequest;
import com.zpf.entity.RpcResponse;
import com.zpf.enumeration.ResponseCode;
import com.zpf.enumeration.RpcError;
import com.zpf.exception.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author zpf
 * @createTime 2021-05-10 21:40
 * RPC的消费者 —— client
 */
public class RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    /**
     * client发送方法调用的请求
     * @param rpcRequest 请求内容
     * @param host 请求的服务器ip
     * @param port 请求的服务器port
     * @return
     */
    public Object sendRequest(RpcRequest rpcRequest, String host, int port){
        /** try-with-resource语法糖  不用finally 关闭资源
         * Object In/OutputStream 对象流，执行序列化 或者反序列化操作
         */
        try (Socket socket = new Socket(host, port)) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();
            RpcResponse rpcResponse = (RpcResponse) objectInputStream.readObject();
            if (rpcResponse == null) {
                logger.error("服务调用失败，service：{}", rpcRequest.getInterfaceName());
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            if (rpcResponse.getStatusCode() == null || rpcResponse.getStatusCode() != ResponseCode.SUCCESS.getCode()){
                logger.error("调用服务失败, service: {}, response:{}", rpcRequest.getInterfaceName(), rpcResponse);
                throw new RpcException(RpcError.SERVICE_INVOCATION_FAILURE, " service:" + rpcRequest.getInterfaceName());
            }
            return rpcResponse.getData();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时发生错误：", e);
            throw new RpcException("服务调用失败: ", e);
        }

    }
}

package client;

import com.zpf.entity.RpcRequest;
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
            return objectInputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用时发生错误：", e);
            return null;
        }

    }
}

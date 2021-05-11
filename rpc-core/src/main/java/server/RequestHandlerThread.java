package server;

import com.zpf.entity.RpcRequest;
import com.zpf.entity.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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

    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            // 读取请求
            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            // 获取请求要调用的接口
            String interfaceName = rpcRequest.getInterfaceName();
            // 从注册中心获取服务接口的实例对象
            Object service = serviceRegistry.getService(interfaceName);
            // 根据请求的信息（方法），以及对象，则可以得到处理结果
            Object result = requestHandler.handle(rpcRequest, service);
            // 返回结果
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("调用或发送时有错误发生：", e);
        }
    }
}

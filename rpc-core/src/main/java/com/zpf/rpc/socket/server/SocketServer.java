package com.zpf.rpc.socket.server;

import com.zpf.rpc.RpcServer;
import com.zpf.rpc.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zpf.rpc.registry.ServiceRegistry;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author zpf
 * @createTime 2021-05-10 21:40
 * RPC的提供者（com.zpf.rpc.socket.server）
 * 使用一个ServerSocket监听端口，发来一个请求，则创建一个线程（BIO方式）
 * 用线程则是伪异步io
 */
public class SocketServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService theadPool;
    private final ServiceRegistry serviceRegistry;
    private RequestHandler requestHandler = new RequestHandler();

    public SocketServer(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
        ArrayBlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        theadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    /**
     * 注册服务，端口
     * @param port
     */
    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器正在启动...");
            Socket socket;
            // 服务器接收到连接请求后，开启一个线程去执行具体的服务
            while ((socket = serverSocket.accept()) != null){
                logger.info("客户端连接！{}:{}", socket.getInetAddress(), socket.getPort());
                theadPool.execute(new RequestHandlerThread(socket, requestHandler, serviceRegistry));
            }
            theadPool.shutdown();
        } catch (IOException e) {
            logger.error("服务器启动时有错误发生：", e);
        }
    }
}

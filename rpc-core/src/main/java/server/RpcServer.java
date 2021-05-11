package server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author zpf
 * @createTime 2021-05-10 21:40
 * RPC的提供者（server）
 * 使用一个ServerSocket监听端口，发来一个请求，则创建一个线程（NIO-acceptor）
 * 用线程则是伪异步io
 */
public class RpcServer {
    private final ExecutorService theadPool;
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    // todo 线程池的创建
    public RpcServer() {
        int corePoolSize = 5;
        int maxPoolSize = 50;
        long keepAliveTime = 60;
        ArrayBlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        theadPool = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime,
                TimeUnit.SECONDS, workingQueue, threadFactory);
    }

    /**
     * 注册服务，端口
     * @param service
     * @param port
     */
    public void register(Object service, int port){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("服务器正在启动...");
            Socket socket;
            while ((socket = serverSocket.accept()) != null){
                logger.info("客户端连接！ip为：" + socket.getInetAddress());
                theadPool.execute(new WorkerThread(socket, service));
            }
        } catch (IOException e) {
            logger.error("连接时有错误发生：", e);
        }
    }
}

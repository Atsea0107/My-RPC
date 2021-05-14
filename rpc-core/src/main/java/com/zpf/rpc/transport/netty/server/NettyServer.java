package com.zpf.rpc.transport.netty.server;

import com.zpf.enumeration.RpcError;
import com.zpf.exception.RpcException;
import com.zpf.rpc.provider.ServiceProvider;
import com.zpf.rpc.provider.ServiceProviderImpl;
import com.zpf.rpc.registry.NacosServiceRegistry;
import com.zpf.rpc.registry.ServiceRegistry;
import com.zpf.rpc.serializer.CommonSerializer;
import com.zpf.rpc.transport.RpcServer;
import com.zpf.rpc.codec.CommonDecoder;
import com.zpf.rpc.codec.CommonEncoder;
import com.zpf.rpc.serializer.KryoSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

/**
 * @author zpf
 * @createTime 2021-05-11 14:41
 */
public class NettyServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    private final String host;
    private final int port;

    private final ServiceRegistry serviceRegistry;
    private final ServiceProvider serviceProvider;

    private CommonSerializer serializer;

    public NettyServer(String host, int port) {
        this.host = host;
        this.port = port;
        this.serviceRegistry = new NacosServiceRegistry();
        this.serviceProvider = new ServiceProviderImpl();
    }

    @Override
    public void start() {
        // bossGroup 用来处理nio的 Accept 事件
        // workerGroup 用来处理nio的read write 事件
        /*
            默认线程数是可用的cpu数 * 2
         */
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // netty服务端应用开发的入口
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .option(ChannelOption.SO_BACKLOG, 256)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new CommonEncoder(serializer));
                            pipeline.addLast(new CommonDecoder());
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(host, port).sync();
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("启动服务器时有错误发生: ", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void setSerializer(CommonSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public <T> void publishService(Object service, Class<T> serviceClass) {
        if (serializer == null){
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        serviceProvider.addServiceProvider(service);
        serviceRegistry.register(serviceClass.getName(), new InetSocketAddress(host, port));
        start();
    }
}

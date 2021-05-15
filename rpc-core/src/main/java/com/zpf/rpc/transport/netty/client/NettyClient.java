package com.zpf.rpc.transport.netty.client;

import com.zpf.entity.RpcRequest;
import com.zpf.entity.RpcResponse;
import com.zpf.enumeration.RpcError;
import com.zpf.exception.RpcException;
import com.zpf.factory.SingletonFactory;
import com.zpf.rpc.loadbalancer.LoadBalancer;
import com.zpf.rpc.loadbalancer.RandomLoadBalancer;
import com.zpf.rpc.registry.NacosServiceDiscovery;
import com.zpf.rpc.registry.NacosServiceRegistry;
import com.zpf.rpc.registry.ServiceDiscovery;
import com.zpf.rpc.registry.ServiceRegistry;
import com.zpf.rpc.serializer.CommonSerializer;
import com.zpf.rpc.transport.RpcClient;
import com.zpf.rpc.codec.CommonDecoder;
import com.zpf.rpc.codec.CommonEncoder;
import com.zpf.rpc.serializer.KryoSerializer;
import com.zpf.rpc.transport.socket.client.SocketClient;
import com.zpf.util.RpcMessageChecker;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author zpf
 * @createTime 2021-05-11 14:41
 */
public class NettyClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private static final Bootstrap bootstrap;
    private static final EventLoopGroup group;
    private final ServiceDiscovery serviceDiscovery;
    private final CommonSerializer serializer;

    private final UnprocessedRequests unprocessedRequests;

    static {
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class);
    }

    public NettyClient() {
        this(CommonSerializer.DEFAULT_SERIALIZER, new RandomLoadBalancer());
    }
    public NettyClient(LoadBalancer loadBalancer) {
        this(CommonSerializer.DEFAULT_SERIALIZER, loadBalancer);
    }
    public NettyClient(Integer serializer) {
        this(serializer, new RandomLoadBalancer());
    }
    public NettyClient(int serializer, LoadBalancer loadBalancer) {
        this.serviceDiscovery = new NacosServiceDiscovery(loadBalancer);
        this.serializer = CommonSerializer.getByCode(serializer);
        this.unprocessedRequests = SingletonFactory.getInstance(UnprocessedRequests.class);
    }

    @Override
    public CompletableFuture<RpcResponse> sendRequest(RpcRequest rpcRequest) {
        if (serializer == null) {
            logger.error("未设置序列化器");
            throw new RpcException(RpcError.SERIALIZER_NOT_FOUND);
        }
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        try {
            InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest.getInterfaceName());
            Channel channel = ChannelProvider.get(inetSocketAddress, serializer);
            if (!channel.isActive()) {
                group.shutdownGracefully();
                return null;
            }
            unprocessedRequests.put(rpcRequest.getRequestId(), resultFuture);
            channel.writeAndFlush(rpcRequest).addListener((ChannelFutureListener) future1 -> {
                if (future1.isSuccess()) {
                    logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                } else {
                    future1.channel().close();
                    resultFuture.completeExceptionally(future1.cause());
                    logger.error("发送消息时有错误发生: ", future1.cause());
                }
            });
        } catch (InterruptedException e) {
            unprocessedRequests.remove(rpcRequest.getRequestId());
            logger.error(e.getMessage(), e);
            Thread.currentThread().interrupt();
        }
        return resultFuture;
    }
}

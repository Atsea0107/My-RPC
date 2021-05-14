# My-RPC

## rpc-api
> 相当于注册中心，对client端，是用于寻找服务；对server端，是用来暴露服务
## rpc-common
> 通信过程中的request 和 response
## rpc-core
> 框架核心
> Client：通过JDK动态代理获取服务的返回结果
> Server：监听端口，并开启线程(BIO)来处理服务，返回的是结果对象的序列化结果
## test-com.zpf.rpc.transport.socket.server
> 测试
## test-com.zpf.rpc.transport.socket.client
> 测试
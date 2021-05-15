package com.zpf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zpf
 * @createTime 2021-05-10 21:04
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);
    @Override
    public String hello(HelloObject helloObject) {
        logger.info("接收到消息：{}", helloObject.getMessage());
        return "本次处理来自Netty服务";
    }
}

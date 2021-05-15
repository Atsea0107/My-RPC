package com.zpf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zpf
 * @createTime 2021-05-10 21:04
 */
public class HelloServiceImpl2 implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl2.class);
    @Override
    public String hello(HelloObject helloObject) {
        logger.info("接收到消息：{}", helloObject.getMessage());
        return "本次处理来自Socket服务";
    }
}

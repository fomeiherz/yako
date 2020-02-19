package top.fomeiherz.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fomeiherz.api.HelloService;

/**
 * TODO
 *
 * @author fomeiherz
 * @date 2020/2/18 16:01
 */
public class HelloServiceImpl implements HelloService {
    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);

    @Override
    public String hello(String name) {
        return "我是" + name;
    }

    @Override
    public String hello(String name, String cl) {
        logger.info("HelloServiceImpl收到: {}.", name);
        String ret = "Hello, " + name + "," + cl;
        logger.info("HelloServiceImpl返回: {}.", ret);
        return ret;
    }

    @Override
    public Integer calc(Integer num1, Integer num2) {
        return num1 + num2;
    }
}

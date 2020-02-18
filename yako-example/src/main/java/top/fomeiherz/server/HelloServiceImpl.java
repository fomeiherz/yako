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
        logger.info("HelloServiceImpl收到: {}.", name);
        String ret = "Hello, " + name;
        logger.info("HelloServiceImpl返回: {}.", ret);
        return ret;
    }
}

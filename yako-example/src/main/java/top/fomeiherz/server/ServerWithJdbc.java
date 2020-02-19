package top.fomeiherz.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fomeiherz.RpcAccessPoint;
import top.fomeiherz.api.HelloService;
import top.fomeiherz.namesrv.NameService;
import top.fomeiherz.spi.ServiceSupport;

import java.io.Closeable;
import java.net.URI;

/**
 * 服务端
 *
 * @author fomeiherz
 * @date 2020/2/18 16:01
 */
public class ServerWithJdbc {
    private static final Logger logger = LoggerFactory.getLogger(ServerWithJdbc.class);

    public static void main(String[] args) throws Exception {
        String serviceName = HelloService.class.getCanonicalName();
        HelloService helloService = new HelloServiceImpl();
        logger.info("创建并启动RpcAccessPoint...");
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class);
             // 启动Netty服务
             Closeable ignored = rpcAccessPoint.startServer()
        ) {
            // 连接注册中心
            NameService nameService = rpcAccessPoint.getNameService(URI.create("jdbc:mysql"));
            assert nameService != null;
            logger.info("向RpcAccessPoint注册{}服务...", serviceName);
            // 添加一个服务提供者
            URI uri = rpcAccessPoint.addServiceProvider(helloService, HelloService.class);
            logger.info("服务名: {}, 向NameService注册...", serviceName);
            // 注册一个服务
            nameService.registerService(serviceName, uri);
            
            logger.info("开始提供服务，按任何键退出.");
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
            logger.info("Bye!");
        }
    }
}

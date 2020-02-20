package top.fomeiherz.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fomeiherz.RpcAccessPoint;
import top.fomeiherz.api.HelloService;
import top.fomeiherz.namesrv.NameService;
import top.fomeiherz.spi.ServiceSupport;

import java.io.File;
import java.io.IOException;
import java.net.URI;

/**
 * 客户端
 *
 * @author fomeiherz
 * @date 2020/2/18 15:59
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) throws IOException {
        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        String name = "Master MQ";
        // RpcAccessPoint继承Closeable，资源自动释放
        try (RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            // 根据文件的URI查找注册中心（其实就是找对应的文件）
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;
            // 根据服务名查找服务提供者地址uri
            URI uri = nameService.lookupService(serviceName);
            assert uri != null;
            logger.info("找到服务{}，提供者: {}.", serviceName, uri);
            // 启动Netty + 创建代理类
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
            logger.info("请求服务, name: {}...", name);
            String response1 = helloService.hello("李雷");
            logger.info("收到响应: {}.", response1);
            // 通过代理类调用服务
            String response = helloService.hello(name, "五<四>班");
            logger.info("收到响应: {}.", response);
            //Integer result = helloService.calc(10, 11);
            //logger.info("计算结果：" + result);
        }
    }
}

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
 * TODO
 *
 * @author fomeiherz
 * @date 2020/2/18 15:59
 */
public class Client {
    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    
    public static void main(String [] args) throws IOException {
        String serviceName = HelloService.class.getCanonicalName();
        File tmpDirFile = new File(System.getProperty("java.io.tmpdir"));
        File file = new File(tmpDirFile, "simple_rpc_name_service.data");
        String name = "Master MQ";
        try(RpcAccessPoint rpcAccessPoint = ServiceSupport.load(RpcAccessPoint.class)) {
            NameService nameService = rpcAccessPoint.getNameService(file.toURI());
            assert nameService != null;
            URI uri = nameService.lookupService(serviceName);
            assert uri != null;
            logger.info("找到服务{}，提供者: {}.", serviceName, uri);
            HelloService helloService = rpcAccessPoint.getRemoteService(uri, HelloService.class);
            logger.info("请求服务, name: {}...", name);
            String response = helloService.hello(name);
            logger.info("收到响应: {}.", response);
        }
    }
}

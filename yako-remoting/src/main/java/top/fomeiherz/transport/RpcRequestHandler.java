package top.fomeiherz.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fomeiherz.client.ServiceTypes;
import top.fomeiherz.model.RpcRequest;
import top.fomeiherz.namesrv.ServiceProviderRegistry;
import top.fomeiherz.serialize.SerializeSupport;
import top.fomeiherz.spi.Singleton;
import top.fomeiherz.transport.command.Code;
import top.fomeiherz.transport.command.Command;
import top.fomeiherz.transport.command.Header;
import top.fomeiherz.transport.command.ResponseHeader;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 接收客户端请求，反射调用对应的方法
 *
 * @author fomeiherz
 * @date 2020/2/18 16:10
 */
@Singleton
public class RpcRequestHandler implements RequestHandler, ServiceProviderRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);
    private Map<String/*service name*/, Object/*service provider*/> serviceProviders = new HashMap<>();

    @Override
    public Command handle(Command requestCommand) {
        Header header = requestCommand.getHeader();
        // 从payload中反序列化RpcRequest
        RpcRequest rpcRequest = SerializeSupport.parse(requestCommand.getPayload());
        try {
            // 查找所有已注册的服务提供方，寻找rpcRequest中需要的服务
            Object serviceProvider = serviceProviders.get(rpcRequest.getInterfaceName());
            if (serviceProvider != null) {
                // 找到服务提供者，利用Java反射机制调用服务的对应方法
                Object[] args = rpcRequest.getArguments();
                Class<?>[] types = rpcRequest.getArgumentTypes();
                Method method = serviceProvider.getClass().getMethod(rpcRequest.getMethodName(), types);
                // 返回类型
                Object result = method.invoke(serviceProvider, args);
                // 把结果封装成响应命令并返回
                return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId()), SerializeSupport.serialize(result));
            }
            // 如果没找到，返回NO_PROVIDER错误响应。
            logger.warn("No service Provider of {}#{}(String)!", rpcRequest.getInterfaceName(), rpcRequest.getMethodName());
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.NO_PROVIDER.getCode(), "No provider!"), new byte[0]);
        } catch (Throwable t) {
            // 发生异常，返回UNKNOWN_ERROR错误响应。
            logger.warn("Exception: ", t);
            return new Command(new ResponseHeader(type(), header.getVersion(), header.getRequestId(), Code.UNKNOWN_ERROR.getCode(), t.getMessage()), new byte[0]);
        }
    }

    @Override
    public int type() {
        return ServiceTypes.TYPE_RPC_REQUEST;
    }

    @Override
    public synchronized <T> void addServiceProvider(Class<? extends T> serviceClass, T serviceProvider) {
        serviceProviders.put(serviceClass.getCanonicalName(), serviceProvider);
        logger.info("Add service: {}, provider: {}.",
                serviceClass.getCanonicalName(),
                serviceProvider.getClass().getCanonicalName());
    }
}

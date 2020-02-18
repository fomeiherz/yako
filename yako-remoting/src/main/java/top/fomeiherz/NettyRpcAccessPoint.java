package top.fomeiherz;

import top.fomeiherz.client.StubFactory;
import top.fomeiherz.namesrv.ServiceProviderRegistry;
import top.fomeiherz.spi.ServiceSupport;
import top.fomeiherz.transport.RequestHandlerRegistry;
import top.fomeiherz.transport.Transport;
import top.fomeiherz.transport.TransportClient;
import top.fomeiherz.transport.TransportServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

/**
 * Netty实现类
 *
 * @author fomeiherz
 * @date 2020/2/18 14:29
 */
public class NettyRpcAccessPoint implements RpcAccessPoint {
    private final String host = "localhost";
    private final int port = 9999;
    private final URI uri = URI.create("rpc://" + host + ":" + port);
    private TransportServer server = null;
    private TransportClient client = ServiceSupport.load(TransportClient.class);
    private final Map<URI, Transport> clientMap = new ConcurrentHashMap<>();
    private final StubFactory stubFactory = ServiceSupport.load(StubFactory.class);
    private final ServiceProviderRegistry serviceProviderRegistry = ServiceSupport.load(ServiceProviderRegistry.class);

    @Override
    public <T> T getRemoteService(URI uri, Class<T> serviceClass) {
        Transport transport = clientMap.computeIfAbsent(uri, this::createTransport);
        // 创建“桩”
        return stubFactory.createStub(transport, serviceClass);
    }

    /**
     * 启动Netty客户端
     * @param uri Netty服务端地址
     * @return
     */
    private Transport createTransport(URI uri) {
        try {
            return client.createTransport(new InetSocketAddress(uri.getHost(), uri.getPort()),30000L);
        } catch (InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public synchronized <T> URI addServiceProvider(T service, Class<T> serviceClass) {
        serviceProviderRegistry.addServiceProvider(serviceClass, service);
        return uri;
    }

    @Override
    public synchronized Closeable startServer() throws Exception {
        if (null == server) {
            server = ServiceSupport.load(TransportServer.class);
            // 启动Netty服务
            server.start(RequestHandlerRegistry.INSTANCE, port);
        }
        return () -> {
            if(null != server) {
                server.stop();
            }
        };
    }

    @Override
    public void close() throws IOException {
        if(null != server) {
            server.stop();
        }
        client.close();
    }
}

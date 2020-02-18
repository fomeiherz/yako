package top.fomeiherz.transport;

/**
 * TODO
 *
 * @author fomeiherz
 * @date 2020/2/18 14:42
 */
public interface TransportServer {
    void start(RequestHandlerRegistry requestHandlerRegistry, int port) throws Exception;
    void stop();
}

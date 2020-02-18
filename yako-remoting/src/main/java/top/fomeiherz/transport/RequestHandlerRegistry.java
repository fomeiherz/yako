package top.fomeiherz.transport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.fomeiherz.spi.ServiceSupport;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO
 *
 * @author fomeiherz
 * @date 2020/2/18 14:43
 */
public class RequestHandlerRegistry {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerRegistry.class);
    private Map<Integer, RequestHandler> handlerMap = new HashMap<>();
    // 单例模式
    public final static RequestHandlerRegistry INSTANCE = new RequestHandlerRegistry();

    private RequestHandlerRegistry() {
        Collection<RequestHandler> requestHandlers = ServiceSupport.loadAll(RequestHandler.class);
        for (RequestHandler requestHandler : requestHandlers) {
            handlerMap.put(requestHandler.type(), requestHandler);
            logger.info("Load request handler, type: {}, class: {}.", requestHandler.type(), requestHandler.getClass().getCanonicalName());
        }
    }

    public RequestHandler get(int type) {
        return handlerMap.get(type);
    }
}

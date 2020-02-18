package top.fomeiherz.client;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * TODO
 *
 * @author fomeiherz
 * @date 2020/2/18 10:48
 */
public class RequestIdSupport {
    private final static AtomicInteger nextRequestId = new AtomicInteger(0);
    public static int next() {
        return nextRequestId.getAndIncrement();
    }
}

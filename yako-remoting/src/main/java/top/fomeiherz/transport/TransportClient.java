package top.fomeiherz.transport;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.concurrent.TimeoutException;

/**
 * Transport客户端
 *
 * @author fomeiherz
 * @date 2020/2/18 14:50
 */
public interface TransportClient extends Closeable {
    Transport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException;
}

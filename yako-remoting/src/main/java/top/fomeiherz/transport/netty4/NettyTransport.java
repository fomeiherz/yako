package top.fomeiherz.transport.netty4;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import top.fomeiherz.transport.InFlightRequests;
import top.fomeiherz.transport.ResponseFuture;
import top.fomeiherz.transport.Transport;
import top.fomeiherz.transport.command.Command;

import java.util.concurrent.CompletableFuture;

/**
 * 使用netty发送命令
 *
 * @author fomeiherz
 * @date 2020/2/18 9:47
 */
public class NettyTransport implements Transport {
    private final Channel channel;
    private final InFlightRequests inFlightRequests;

    NettyTransport(Channel channel, InFlightRequests inFlightRequests) {
        this.channel = channel;
        this.inFlightRequests = inFlightRequests;
    }

    @Override
    public  CompletableFuture<Command> send(Command request) {
        // 构建返回值
        CompletableFuture<Command> completableFuture = new CompletableFuture<>();
        try {
            // 将在途请求放到inFlightRequests中
            inFlightRequests.put(new ResponseFuture(request.getHeader().getRequestId(), completableFuture));
            // 发送命令
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                // 处理发送失败的情况
                if (!channelFuture.isSuccess()) {
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable t) {
            // 处理发送异常
            inFlightRequests.remove(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }
        return completableFuture;
    }
}

package top.fomeiherz.yako.transport.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.fomeiherz.yako.transport.model.Request;

@ChannelHandler.Sharable
public class MessageServerHandle extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        // 解析二进制报文
        if (msg instanceof Request) {
            System.out.println(msg);
        }
    }
}

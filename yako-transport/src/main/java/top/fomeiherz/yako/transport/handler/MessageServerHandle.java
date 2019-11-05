package top.fomeiherz.yako.transport.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import top.fomeiherz.yako.transport.model.Request;
import top.fomeiherz.yako.transport.service.UserService;

import java.lang.reflect.Method;

@ChannelHandler.Sharable
public class MessageServerHandle extends ChannelDuplexHandler {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
        // 解析二进制报文
        if (msg instanceof Request) {
            Request request = (Request) msg;
            Class<?> _class = Class.forName(request.getClassName());
            UserService userService = (UserService) _class.newInstance();
            Method pay = _class.getMethod("pay", String.class, String.class, Integer.class);
            pay.invoke(userService, request.getArgs().get(0), request.getArgs().get(1), request.getArgs().get(2));
        }
    }
}

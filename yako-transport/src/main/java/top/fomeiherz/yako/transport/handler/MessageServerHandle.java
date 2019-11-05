package top.fomeiherz.yako.transport.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
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
            Method pay = _class.getMethod(request.getMethodName(), request.getParameterTypes()[0], request.getParameterTypes()[1], request.getParameterTypes()[2]);
            pay.invoke(userService, request.getParameters()[0], request.getParameters()[1], request.getParameters()[2]);
        }
    }
}

package top.fomeiherz.yako.transport.transporter;


import io.netty.channel.Channel;
import top.fomeiherz.yako.transport.model.Request;

public class NettyClientTest {

    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient();
        client.doConnect();
        Channel channel = client.getChannel();
        Request request = new Request();
        request.setClassName("top.fomeiherz.yako.transport.service.UserService");
        request.setRequestId(1L);
        request.setMethodName("pay");

        Object[] parameters = new Object[3];
        parameters[0] = "小明";
        parameters[1] = "小红";
        parameters[2] = 10000;
        request.setParameters(parameters);

        Class<?>[] parameterTypes = new Class[3];
        parameterTypes[0] = String.class;
        parameterTypes[1] = String.class;
        parameterTypes[2] = Integer.class;
        request.setParameterTypes(parameterTypes);

        for (;;) {
            // 发送消息
            channel.writeAndFlush(request);
            Thread.sleep(3000);
        }
    }

}

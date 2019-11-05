package top.fomeiherz.yako.transport.transporter;


import io.netty.channel.Channel;
import top.fomeiherz.yako.transport.model.Request;

public class NettyClientTest {

    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient();
        client.doConnect();
        Channel channel = client.getChannel();
        Request request = new Request();
        request.setRequestId(1L);
        request.setMethodName("helloService");

        for (;;) {
            // 发送消息
            channel.writeAndFlush(request);
            Thread.sleep(3000);
        }
    }

}

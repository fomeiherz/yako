package top.fomeiherz.yako.transport.transporter;


import io.netty.channel.Channel;

public class NettyClientTest {

    public static void main(String[] args) throws Exception {
        NettyClient client = new NettyClient();
        client.doConnect();
        Channel channel = client.getChannel();
        for (;;) {
            // 发送消息
            channel.writeAndFlush("hello");
            Thread.sleep(3000);
        }
    }

}

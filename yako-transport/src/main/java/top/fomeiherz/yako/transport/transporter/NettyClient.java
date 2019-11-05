package top.fomeiherz.yako.transport.transporter;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import top.fomeiherz.yako.transport.codec.KyroMsgDecoder;
import top.fomeiherz.yako.transport.codec.KyroMsgEncoder;
import top.fomeiherz.yako.transport.handler.MessageClientHandler;

import java.util.concurrent.TimeUnit;

public class NettyClient {

    private Channel channel = null;

    /**
     * Connect netty client
     *
     * @throws Exception
     */
    public void doConnect() throws Exception {
        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new KyroMsgDecoder());
                            p.addLast(new KyroMsgEncoder());
                            p.addLast(new MessageClientHandler());
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect("127.0.0.1", 8181).sync();
            // Save this channel
            channel = f.channel();
            // Wait until the connection is closed.
            // f.channel().closeFuture().sync();
        } finally {
            // Shut down the event loop to terminate all threads.
            // group.shutdownGracefully();
        }
    }

    /**
     * Get ChannelFuture's channel
     *
     * @return
     */
    public Channel getChannel() {
        return channel;
    }

}

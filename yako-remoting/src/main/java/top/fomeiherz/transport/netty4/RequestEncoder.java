package top.fomeiherz.transport.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import top.fomeiherz.transport.command.Header;

/**
 * TODO
 *
 * @author fomeiherz
 * @date 2020/2/18 15:46
 */
public class RequestEncoder extends CommandEncoder {
    
    @Override
    protected void encodeHeader(ChannelHandlerContext channelHandlerContext, Header header, ByteBuf byteBuf) throws Exception {
        super.encodeHeader(channelHandlerContext, header, byteBuf);
    }
    
}

package top.fomeiherz.transport.netty4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import top.fomeiherz.transport.command.Header;
import top.fomeiherz.transport.command.ResponseHeader;

import java.nio.charset.StandardCharsets;

/**
 * TODO
 *
 * @author fomeiherz
 * @date 2020/2/18 15:53
 */
public class ResponseDecoder extends CommandDecoder {

    @Override
    protected Header decodeHeader(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) {
        int type = byteBuf.readInt();
        int version = byteBuf.readInt();
        int requestId = byteBuf.readInt();
        int code = byteBuf.readInt();
        int errorLength = byteBuf.readInt();
        byte [] errorBytes = new byte[errorLength];
        byteBuf.readBytes(errorBytes);
        String error = new String(errorBytes, StandardCharsets.UTF_8);
        return new ResponseHeader(
                type, version, requestId, code, error
        );
    }
    
}

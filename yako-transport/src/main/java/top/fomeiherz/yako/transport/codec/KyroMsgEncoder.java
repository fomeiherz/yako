package top.fomeiherz.yako.transport.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Output;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.apache.commons.io.IOUtils;
import top.fomeiherz.yako.transport.model.Request;

import java.io.ByteArrayOutputStream;

@ChannelHandler.Sharable
public class KyroMsgEncoder extends MessageToByteEncoder<Request> {

    protected void encode(ChannelHandlerContext ctx, Request msg, ByteBuf out) throws Exception {
        // 将对象转换为byte
        byte[] body = convertToBytes(msg);
        // 读取消息的长度
        int dataLength = body.length;
        // 先将消息长度写入，也就是消息头
        out.writeInt(dataLength);
        // 消息体中包含我们要发送的数据
        out.writeBytes(body);
    }

    private byte[] convertToBytes(Request request) {
        Kryo kryo = new Kryo();
        ByteArrayOutputStream bos = null;
        Output output = null;
        try {
            bos = new ByteArrayOutputStream();
            output = new Output(bos);
            kryo.writeObject(output, request);
            output.flush();
            return bos.toByteArray();
        } catch (KryoException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(output);
            IOUtils.closeQuietly(bos);
        }
        return null;
    }

}

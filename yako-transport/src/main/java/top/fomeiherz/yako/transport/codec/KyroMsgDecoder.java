package top.fomeiherz.yako.transport.codec;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.io.Input;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.commons.io.IOUtils;
import top.fomeiherz.yako.transport.model.Request;

import java.io.ByteArrayInputStream;
import java.util.List;

public class KyroMsgDecoder extends ByteToMessageDecoder {

    public static final int HEAD_LENGTH = 4;

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // 这个 HEAD_LENGTH 是我们用于表示头长度的字节数
        // 由于 Encoder 中长度使用int类型，一个int类型4个字节
        if (in.readableBytes() < HEAD_LENGTH) {
            return;
        }
        // 标记一下当前的readIndex的位置
        in.markReaderIndex();
        // 读取传送过来的消息的长度，ByteBuf的readInt()方法会让它的readIndex增加4
        int dataLength = in.readInt();
        // 消息体长度为0
        if (dataLength <= 0) {
            ctx.close();
        }
        // 读到的消息体长度如果小于我们传送过来的消息长度，则resetReaderIndex. 这个配合markReaderIndex使用的。把readIndex重置到mark的地方
        if (in.readableBytes() < dataLength) {
            in.resetReaderIndex();
            return;
        }

        // 传输正常
        byte[] body = new byte[dataLength];
        in.readBytes(body);
        // 将byte数据转化为bean对象
        Object o = convertToObject(body);
        out.add(o);
    }

    private Object convertToObject(byte[] body) {
        Kryo kryo = new Kryo();
        Input input = null;
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(body);
            input = new Input(bais);
            return kryo.readObject(input, Request.class);
        } catch (KryoException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(input);
            IOUtils.closeQuietly(bais);
        }
        return null;
    }

}

package top.fomeiherz.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import top.fomeiherz.serialize.Serializer;
import top.fomeiherz.serialize.Types;

import java.nio.charset.StandardCharsets;

/**
 * String类型的序列化工具
 *
 * @author fomeiherz
 * @date 2020/2/18 8:55
 */
public class StringSerializer implements Serializer<String> {

    public int size(String entry) {
        return JSON.toJSONBytes(entry).length;
    }

    public void serialize(String entry, byte[] bytes, int offset, int length) {
        byte[] strBytes = JSON.toJSONBytes(entry);
        System.arraycopy(strBytes, 0, bytes, offset, strBytes.length);
    }

    public String parse(byte[] bytes, int offset, int length) {
        return JSON.parseObject(bytes, offset, length, IOUtils.UTF8, String.class);
    }

    public byte type() {
        return Types.TYPE_STRING;
    }

    public Class<String> getSerializeClass() {
        return String.class;
    }
}

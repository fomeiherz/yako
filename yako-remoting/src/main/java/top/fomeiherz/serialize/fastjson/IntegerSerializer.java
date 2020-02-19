package top.fomeiherz.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import top.fomeiherz.serialize.Serializer;
import top.fomeiherz.serialize.Types;

/**
 * String类型的序列化工具
 *
 * @author fomeiherz
 * @date 2020/2/18 8:55
 */
public class IntegerSerializer implements Serializer<Integer> {

    public int size(Integer entry) {
        return JSON.toJSONBytes(entry).length;
    }

    public void serialize(Integer entry, byte[] bytes, int offset, int length) {
        byte[] strBytes = JSON.toJSONBytes(entry);
        System.arraycopy(strBytes, 0, bytes, offset, strBytes.length);
    }

    public Integer parse(byte[] bytes, int offset, int length) {
        return JSON.parseObject(bytes, offset, length, IOUtils.UTF8, Integer.class);
    }

    public byte type() {
        return Types.TYPE_INTEGER;
    }

    public Class<Integer> getSerializeClass() {
        return Integer.class;
    }
}

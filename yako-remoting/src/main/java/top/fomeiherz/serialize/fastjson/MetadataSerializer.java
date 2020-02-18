package top.fomeiherz.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.util.IOUtils;
import top.fomeiherz.model.Metadata;
import top.fomeiherz.serialize.Serializer;
import top.fomeiherz.serialize.Types;

import java.lang.reflect.Type;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MeteData数据序列化
 *
 * @author fomeiherz
 * @date 2020/2/18 9:07
 */
public class MetadataSerializer implements Serializer<Metadata> {

    @Override
    public int size(Metadata entry) {
        return JSON.toJSONBytes(entry).length;
    }

    @Override
    public void serialize(Metadata entry, byte[] bytes, int offset, int length) {
        byte[] strBytes = JSON.toJSONBytes(entry);
        System.arraycopy(strBytes, 0, bytes, offset, strBytes.length);
    }

    @Override
    public Metadata parse(byte[] bytes, int offset, int length) {
        Metadata data = new Metadata();
        Map<String, JSONArray> map = JSON.parseObject(bytes, offset, length, IOUtils.UTF8, Map.class);
        map.forEach((k, v) -> {
            List<URI> uris = new ArrayList<>();
            v.forEach(l -> uris.add(URI.create(l.toString())));
            data.put(k, uris);
        });
        return data;
    }

    @Override
    public byte type() {
        return Types.TYPE_METADATA;
    }

    @Override
    public Class<Metadata> getSerializeClass() {
        return Metadata.class;
    }

}

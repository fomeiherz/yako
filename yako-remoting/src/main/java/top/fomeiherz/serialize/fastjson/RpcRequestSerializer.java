/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package top.fomeiherz.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.util.IOUtils;
import top.fomeiherz.model.RpcRequest;
import top.fomeiherz.serialize.Serializer;
import top.fomeiherz.serialize.Types;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * RpcRequest序列化工具
 *
 * @author fomeiherz
 * @date 2020/2/18 9:05
 */
public class RpcRequestSerializer implements Serializer<RpcRequest> {
    @Override
    public int size(RpcRequest request) {
        return JSON.toJSONBytes(request).length;
    }

    @Override
    public void serialize(RpcRequest request, byte[] bytes, int offset, int length) {
        byte[] strBytes = JSON.toJSONBytes(request);
        System.arraycopy(strBytes, 0, bytes, offset, strBytes.length);
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        return JSON.parseObject(bytes, offset, length, IOUtils.UTF8, RpcRequest.class);
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }
}

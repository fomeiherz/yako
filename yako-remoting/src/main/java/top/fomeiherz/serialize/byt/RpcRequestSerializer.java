///**
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package top.fomeiherz.serialize.byt;
//
//import top.fomeiherz.model.RpcRequest;
//import top.fomeiherz.serialize.Serializer;
//import top.fomeiherz.serialize.Types;
//
//import java.nio.ByteBuffer;
//import java.nio.charset.StandardCharsets;
//
///**
// * RpcRequest序列化工具
// *
// * @author fomeiherz
// * @date 2020/2/18 9:05
// */
//public class RpcRequestSerializer implements Serializer<RpcRequest> {
//    @Override
//    public int size(RpcRequest request) {
//        return Integer.BYTES + request.getInterfaceName().getBytes(StandardCharsets.UTF_8).length +
//                Integer.BYTES + request.getMethodName().getBytes(StandardCharsets.UTF_8).length +
//                Integer.BYTES + request.getArguments().length;
//    }
//
//    @Override
//    public void serialize(RpcRequest request, byte[] bytes, int offset, int length) {
//        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
//        // 接口名
//        byte[] tmpBytes = request.getInterfaceName().getBytes(StandardCharsets.UTF_8);
//        buffer.putInt(tmpBytes.length);
//        buffer.put(tmpBytes);
//
//        // 方法名
//        tmpBytes = request.getMethodName().getBytes(StandardCharsets.UTF_8);
//        buffer.putInt(tmpBytes.length);
//        buffer.put(tmpBytes);
//
//        // 接口参数
//        tmpBytes = request.getArguments();
//        buffer.putInt(tmpBytes.length);
//        buffer.put(tmpBytes);
//    }
//
//    @Override
//    public RpcRequest parse(byte[] bytes, int offset, int length) {
//        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
//        int len = buffer.getInt();
//        byte[] tmpBytes = new byte[len];
//        buffer.get(tmpBytes);
//        String interfaceName = new String(tmpBytes, StandardCharsets.UTF_8);
//
//        len = buffer.getInt();
//        tmpBytes = new byte[len];
//        buffer.get(tmpBytes);
//        String methodName = new String(tmpBytes, StandardCharsets.UTF_8);
//
//        len = buffer.getInt();
//        tmpBytes = new byte[len];
//        buffer.get(tmpBytes);
//        byte[] serializedArgs = tmpBytes;
//
//        return new RpcRequest(interfaceName, methodName, serializedArgs, argumentTypes);
//    }
//
//    @Override
//    public byte type() {
//        return Types.TYPE_RPC_REQUEST;
//    }
//
//    @Override
//    public Class<RpcRequest> getSerializeClass() {
//        return RpcRequest.class;
//    }
//}

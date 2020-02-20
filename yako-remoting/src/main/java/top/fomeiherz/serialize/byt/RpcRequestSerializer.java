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
package top.fomeiherz.serialize.byt;

import top.fomeiherz.model.RpcRequest;
import top.fomeiherz.serialize.Serializer;
import top.fomeiherz.serialize.Types;

import java.io.*;
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
        return Integer.BYTES + request.getInterfaceName().getBytes(StandardCharsets.UTF_8).length +
                Integer.BYTES + request.getMethodName().getBytes(StandardCharsets.UTF_8).length +
                Short.BYTES + getArraySize(request.getArguments()) + Short.BYTES + getClassObjectSize(request.getArgumentTypes());
    }
    
    private int getArraySize(Object[] objects) {
        int total = 0;
        for (Object obj : objects) {
            byte[] argBytes = toByteArray(obj);
            total += Short.BYTES + argBytes.length;
        }
        return total;
    }

    private int getClassObjectSize(Class<?>[] objects) {
        int total = 0;
        for (Class<?> cl : objects) {
            byte[] argBytes = toByteArray(cl);
            total += Short.BYTES + argBytes.length;
        }
        return total;
    }
    
    @Override
    public void serialize(RpcRequest request, byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        // 接口名
        byte[] tmpBytes = request.getInterfaceName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        // 方法名
        tmpBytes = request.getMethodName().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(tmpBytes.length);
        buffer.put(tmpBytes);

        // 解析参数
        // 数组长度
        buffer.putShort(toShortSafely(request.getArguments().length));
        for (Object obj : request.getArguments()) {
            byte[] argBytes = toByteArray(obj);
            buffer.putShort(toShortSafely(argBytes.length));
            buffer.put(argBytes);
        }
        
        // 参数类型
        buffer.putShort(toShortSafely(request.getArgumentTypes().length));
        for (Class<?> cl : request.getArgumentTypes()) {
            byte[] artTypeBytes = toByteArray(cl);
            buffer.putShort(toShortSafely(artTypeBytes.length));
            buffer.put(artTypeBytes);
        }
    }

    /**
     * int转short
     * @param v
     * @return
     */
    private short toShortSafely(int v) {
        assert v < Short.MAX_VALUE;
        return (short) v;
    }

    @Override
    public RpcRequest parse(byte[] bytes, int offset, int length) {
        ByteBuffer buffer = ByteBuffer.wrap(bytes, offset, length);
        int len = buffer.getInt();
        byte[] tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        String interfaceName = new String(tmpBytes, StandardCharsets.UTF_8);

        len = buffer.getInt();
        tmpBytes = new byte[len];
        buffer.get(tmpBytes);
        String methodName = new String(tmpBytes, StandardCharsets.UTF_8);

        // 解析参数
        short argLen = buffer.getShort();
        Object[] arguments = new Object[argLen];
        for (int i = 0; i < argLen; i++) {
            len = buffer.getShort();
            byte[] argBytes = new byte[len];
            buffer.get(argBytes);
            arguments[i] = toObject(argBytes);
        }
        
        // 解析参数类型
        short argTypeLen = buffer.getShort();
        Class<?>[] argumentTypes = new Class<?>[argTypeLen];
        for (int i = 0; i < argTypeLen; i++) {
            len = buffer.getShort();
            byte[] argBytes = new byte[len];
            buffer.get(argBytes);
            argumentTypes[i] = (Class<?>) toObject(argBytes);
        }

        return new RpcRequest(interfaceName, methodName, arguments, argumentTypes);
    }

    @Override
    public byte type() {
        return Types.TYPE_RPC_REQUEST;
    }

    @Override
    public Class<RpcRequest> getSerializeClass() {
        return RpcRequest.class;
    }

    /**
     * Object转byte[]
     * @param obj
     * @return
     */
    public byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }
    
    /**
     * byte[]转Object
     * @param bytes
     * @return
     */
    public Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }
}

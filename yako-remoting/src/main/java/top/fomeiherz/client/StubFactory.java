package top.fomeiherz.client;

import top.fomeiherz.transport.Transport;

/**
 * 创建一个桩
 *
 * @author fomeiherz
 * @date 2020/2/18 10:16
 */
public interface StubFactory {

    /**
     * 为实例创建桩
     * 
     * @param transport 发送请求的类
     * @param clazz 被代理对象类型
     * @param <T> 被代理对象
     * @return Class<?>对象的桩
     */
    <T> T createStub(Transport transport, Class<T> clazz);
    
}

package top.fomeiherz.client;

import com.itranswarp.compiler.JavaStringCompiler;
import top.fomeiherz.transport.Transport;

import java.util.Map;

/**
 * StubFactory默认实现类
 *
 * @author fomeiherz
 * @date 2020/2/18 10:25
 */
public class DynamicStubFactory implements StubFactory {

    private final static String STUB_SOURCE_TEMPLATE =
            "package top.fomeiherz.client.stubs;\n" +
                    "import top.fomeiherz.serialize.SerializeSupport;\n" +
                    "\n" +
                    "public class %s extends AbstractStub implements %s {\n" +
                    "    @Override\n" +
                    "    public String %s(String arg) {\n" +
                    "        return SerializeSupport.parse(\n" +
                    "                invokeRemote(\n" +
                    "                        new top.fomeiherz.model.RpcRequest(\n" +
                    "                                \"%s\",\n" +
                    "                                \"%s\",\n" +
                    "                                SerializeSupport.serialize(arg)\n" +
                    "                        )\n" +
                    "                )\n" +
                    "        );\n" +
                    "    }\n" +
                    "}";

    @Override
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        try {
            // 填充模板
            String stubSimpleName = serviceClass.getSimpleName() + "Stub";
            String classFullName = serviceClass.getName();
            String stubFullName = "top.fomeiherz.client.stubs." + stubSimpleName;
            String methodName = serviceClass.getMethods()[0].getName();

            String source = String.format(STUB_SOURCE_TEMPLATE, stubSimpleName, classFullName, methodName, classFullName, methodName);

            // 编译源代码
            JavaStringCompiler compiler = new JavaStringCompiler();
            Map<String, byte[]> results = compiler.compile(stubSimpleName + ".java", source);

            // 加载源码
            Class<?> clazz = compiler.loadClass(stubFullName, results);
            ServiceStub stubInstance = (ServiceStub) clazz.newInstance();
            stubInstance.setTransport(transport);

            // 返回这个桩
            return (T) stubInstance;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}

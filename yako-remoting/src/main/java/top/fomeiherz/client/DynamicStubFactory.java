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


    // TODO 支持多个方法
    private final static String STUB_SOURCE_TEMPLATE =
            "package top.fomeiherz.client.stubs;\n" +
                    "import top.fomeiherz.serialize.SerializeSupport;\n" +
                    "import java.util.*;\n" +
                    "public class %s extends AbstractStub implements %s {\n" +
                    "    @Override\n" +
                    "    public String %s(String arg1, String arg2) {\n" +
                    "        List<Class<?>> cls = new ArrayList<>();\n" +
                    "        cls.add(arg1.getClass());\n" +
                    "        cls.add(arg2.getClass());\n" +
                    "        return SerializeSupport.parse(\n" +
                    "                invokeRemote(\n" +
                    "                        new top.fomeiherz.model.RpcRequest(\n" +
                    "                                \"%s\",\n" +
                    "                                \"%s\",\n" +
                    "                                new Object[]{arg1, arg2},\n" +
                    "                                cls\n" +
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
            // TODO 多个方法名和参数，有重载方法的情况
            String methodName = serviceClass.getMethods()[0].getName();
            // TODO 动态生成source
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

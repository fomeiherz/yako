package top.fomeiherz.client;

import com.itranswarp.compiler.JavaStringCompiler;
import top.fomeiherz.transport.Transport;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * StubFactory默认实现类
 *
 * @author fomeiherz
 * @date 2020/2/18 10:25
 */
public class DynamicStubFactory implements StubFactory {

    private static String prefix = "package top.fomeiherz.client.stubs;\n" +
            "import top.fomeiherz.serialize.SerializeSupport;\n" +
            "public class %s extends AbstractStub implements %s {\n";

    private final static String STRING = "}";

    private final static String ARG_PREFIX = "arg";

    @Override
    public <T> T createStub(Transport transport, Class<T> serviceClass) {
        try {
            // 填充模板
            String stubSimpleName = serviceClass.getSimpleName() + "Stub";
            String classFullName = serviceClass.getName();
            String stubFullName = "top.fomeiherz.client.stubs." + stubSimpleName;
            StringBuilder proxyMethods = new StringBuilder();
            // 多个方法名和参数，有重载方法的情况
            for (Method method : serviceClass.getMethods()) {
                StringBuilder args = new StringBuilder("new Object[]{");
                String methodName = method.getName();
                proxyMethods.append("@Override \n");
                proxyMethods.append("public")
                        .append(" ").append(method.getReturnType().getName())
                        .append(" ").append(methodName).append("(");
                for (int i = 0; i < method.getParameterCount(); i++) {
                    Class<?> parameterType = method.getParameterTypes()[i];
                    proxyMethods.append(parameterType.getName())
                            .append(" ")
                            .append(ARG_PREFIX)
                            .append(i);
                    args.append(ARG_PREFIX).append(i);
                    if (i != method.getParameterCount() - 1) {
                        proxyMethods.append(", ");
                        args.append(", ");
                    }
                }
                proxyMethods.append(") {\n");
                proxyMethods.append("Class<?>[] cls = new Class<?>[")
                        .append(method.getParameterCount()).append("];");
                for (int i = 0; i < method.getParameterCount(); i++) {
                    proxyMethods.append("cls[").append(i).append("]").append("=").append(ARG_PREFIX).append(i).append(".getClass();");
                }
                proxyMethods.append("return SerializeSupport.parse(");
                proxyMethods.append("invokeRemote(");
                proxyMethods.append("new top.fomeiherz.model.RpcRequest(");
                proxyMethods.append("\"").append(classFullName).append("\"").append(",");
                proxyMethods.append("\"").append(methodName).append("\"").append(",");
                // 参数
                args.append("},");
                proxyMethods.append(args);
                proxyMethods.append("cls,");
                proxyMethods.append(method.getReturnType().getName()).append(".class");
                proxyMethods.append(")").append(")").append(");");
                proxyMethods.append("}\n");
            }
            // 动态生成source
            prefix = String.format(prefix, stubSimpleName, classFullName);
            String source = prefix + proxyMethods + STRING;

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

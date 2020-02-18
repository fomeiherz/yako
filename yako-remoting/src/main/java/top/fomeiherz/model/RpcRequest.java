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
package top.fomeiherz.model;

/**
 * RpcRequest实体类
 *
 * @author fomeiherz
 * @date 2020/2/18 9:05
 */
public class RpcRequest {
    private final String interfaceName;
    private final String methodName;
    private final Object[] arguments;
    private final Class<?>[] argumentTypes;
    private final Class<?> returnType;

    public RpcRequest(String interfaceName, String methodName, Object[] arguments, Class<?>[] argumentTypes, Class<?> returnType) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.arguments = arguments;
        this.argumentTypes = argumentTypes;
        this.returnType = returnType;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getArguments() {
        return arguments;
    }

    public Class<?>[] getArgumentTypes() {
        return argumentTypes;
    }

    public Class<?> getReturnType() {
        return returnType;
    }
}

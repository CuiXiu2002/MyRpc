package body;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author Lenovo
 * @create 2023-03-22-18:53
 */
public class RpcRequest implements Serializable {

    static final long serialVersionUID = 654351342L;
    String interfaceName;
    String methodName;
    Object[] parameterValues;
    Class[] parameterTypes;

    public RpcRequest(String interfaceName, String methodName, Object[] parameterValues, Class[] parameterTypes) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.parameterValues = parameterValues;
        this.parameterTypes = parameterTypes;
    }

    public String getMethodName() {
        return methodName;
    }

    public Object[] getParameterValues() {
        return parameterValues;
    }

    public Class[] getParameterTypes() {
        return parameterTypes;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    @Override
    public String toString() {
        return "RpcRequest{" +
                "interfaceName='" + interfaceName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", parameterValues=" + Arrays.toString(parameterValues) +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }
}

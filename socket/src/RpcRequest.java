import java.io.Serializable;

/**
 * @author Lenovo
 * @create 2023-03-22-18:53
 */
public class RpcRequest implements Serializable {

    static final long serialVersionUID = 654351342L;
    String methodName;
    Object[] parameterValues;
    Class[] parameterTypes;

    public RpcRequest(String methodName, Object[] parameterValues, Class[] parameterTypes) {
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
}

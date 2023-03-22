import java.io.Serializable;

/**
 * @author Lenovo
 * @create 2023-03-22-18:53
 */
public class RpcRequest implements Serializable {

    static final long serialVersionUID = 654351342L;
    String methodName;

    public RpcRequest(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodName() {
        return methodName;
    }
}

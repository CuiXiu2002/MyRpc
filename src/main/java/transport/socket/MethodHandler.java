package transport.socket;

import body.RpcRequest;
import test.MethodProvider;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Lenovo
 * @create 2023-03-25-13:28
 */
public class MethodHandler {

    public void handle(ObjectInputStream objectInputStream,ObjectOutputStream objectOutputStream) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InterruptedException {
        RpcRequest rpcRequest=(RpcRequest) objectInputStream.readObject();
        String name=rpcRequest.getMethodName();
        Object[] paraArray= rpcRequest.getParameterValues();
        Class[] typeArray= rpcRequest.getParameterTypes();
        Object service=MethodProvider.getService(rpcRequest.getInterfaceName());
        Method method= service.getClass().getMethod(name, typeArray);
        Object res= method.invoke(service,paraArray);
        Thread.sleep(3000);
        objectOutputStream.writeObject(res);
    }
}

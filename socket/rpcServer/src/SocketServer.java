import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Provider;

/**
 * @author ${USER}
 * @create ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
public class SocketServer {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        Socket accept = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            serverSocket = new ServerSocket(50000);
            accept = serverSocket.accept();
            objectInputStream = new ObjectInputStream(accept.getInputStream());
            objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
            RpcRequest rpcRequest=(RpcRequest) objectInputStream.readObject();
            String name=rpcRequest.getMethodName();
            Object[] paraArray= rpcRequest.getParameterValues();
            Class[] typeArray= rpcRequest.getParameterTypes();
            Method method= MethodProvider.class.getMethod(name, typeArray);
            Object res= method.invoke(null,paraArray);
            objectOutputStream.writeObject(res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } finally {
            if(objectInputStream!=null) {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(objectOutputStream!=null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(accept!=null) {
                try {
                    accept.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(serverSocket!=null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
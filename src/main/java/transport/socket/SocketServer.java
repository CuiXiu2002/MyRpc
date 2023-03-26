package transport.socket;

import register.RegisterCenter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Provider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author ${USER}
 * @create ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
public class SocketServer {

    public static void accept() {
        ExecutorService service = Executors.newFixedThreadPool(3);
        ServerSocket serverSocket = null;
        Socket accept = null;
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            serverSocket = new ServerSocket(RegisterCenter.PORT);
            while((accept = serverSocket.accept())!=null){
                objectInputStream = new ObjectInputStream(accept.getInputStream());
                objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
                ObjectInputStream finalObjectInputStream = objectInputStream;
                ObjectOutputStream finalObjectOutputStream = objectOutputStream;
                service.execute(()->{
                    try {
                        new MethodHandler().handle(finalObjectInputStream, finalObjectOutputStream);
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
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
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
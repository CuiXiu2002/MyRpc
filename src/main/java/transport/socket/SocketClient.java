package transport.socket;

import body.RpcRequest;
import register.RegisterCenter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author ${USER}
 * @create ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
public class SocketClient {
    public Object send(RpcRequest rpcRequest) throws Exception {

        Socket socket = null;
        ObjectOutputStream outputStream = null;
        ObjectInputStream inputStream = null;
        String host=RegisterCenter.getService(rpcRequest.getInterfaceName());
        String ip=host.split(":")[0];
        int port= Integer.parseInt(host.split(":")[1]);
        InetAddress address = InetAddress.getByName(ip);
        try {
            socket = new Socket(address, port);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject(rpcRequest);
            inputStream = new ObjectInputStream(socket.getInputStream());
            Object o =  inputStream.readObject();
            return o;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if(outputStream!=null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

        }

    }
}
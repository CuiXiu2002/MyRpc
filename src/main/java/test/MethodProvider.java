package test;

import register.RegisterCenter;
import transport.socket.SocketServer;

import java.net.InetAddress;
import java.util.HashMap;

/**
 * @author cuixiu2002
 * @create ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
public class MethodProvider {
    private static HashMap<String,Object> serviceMap=new HashMap<>();

    public static void main(String[] args) throws Exception {
        Hello service=new HelloImpl();
        String ip=InetAddress.getLocalHost().toString();
        String serviceName=service.getClass().getInterfaces()[0].toString().split("interface ")[1];
        serviceMap.put(serviceName,service);
        RegisterCenter.registerService(serviceName, ip.substring(ip.lastIndexOf("/")+1));
        SocketServer.accept();
    }
    public static Object getService(String name){
        return serviceMap.get(name);
    }
}
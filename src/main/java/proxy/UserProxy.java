package proxy;

import body.RpcRequest;
import transport.netty.NettyClient;
import transport.socket.SocketClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author ${USER}
 * @create ${YEAR}-${MONTH}-${DAY}-${TIME}
 */
public class UserProxy implements InvocationHandler {
    private Class target; // 代理对象
    public <T> T getInstance(Class<T> target) {
        this.target = target;
        // 取得代理对象
        return (T) Proxy.newProxyInstance(target.getClassLoader(), new Class<?>[]{target}, this);
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getName(),method.getName(), args, method.getParameterTypes());
//        SocketClient socketClient = new SocketClient();
//        Object res=socketClient.send(rpcRequest);
        NettyClient nettyClient = new NettyClient();
        Object res=nettyClient.sendRequest(rpcRequest);
        return res;
    }
}
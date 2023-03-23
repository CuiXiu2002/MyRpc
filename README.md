# MyRpc
 简易RPC框架

### 第一次提交

利用反射机制实现了简易的远程方法调用，了解了远程方法调用的原理。

单线程且不分代理，耦合度极高。

![image-20230323140025821](https://cuicy-1314839020.cos.ap-shanghai.myqcloud.com/typora-user-images/image-20230323140025821.png)

- client

```java
socket = new Socket(InetAddress.getLocalHost(), 50000);
outputStream = new ObjectOutputStream(socket.getOutputStream());
outputStream.writeObject(new RpcRequest("method1"));
inputStream = new ObjectInputStream(socket.getInputStream());
Object o =  inputStream.readObject();
System.out.println(o);
```

- server

```java
serverSocket = new ServerSocket(50000);
accept = serverSocket.accept();
objectInputStream = new ObjectInputStream(accept.getInputStream());
objectOutputStream = new ObjectOutputStream(accept.getOutputStream());
RpcRequest message=(RpcRequest) objectInputStream.readObject();
String name=message.getMethodName();
Method method= Server.class.getMethod(name,null);
String res=(String) method.invoke(null);
objectOutputStream.writeObject(res);
```

### 第二次提交

利用动态代理模式拆分了框架和用户，这部分需要自学动态代理模式

仍然单线程且发送完即结束

![image-20230323140708257](https://cuicy-1314839020.cos.ap-shanghai.myqcloud.com/typora-user-images/image-20230323140708257.png)
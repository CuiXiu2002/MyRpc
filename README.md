# MyRpc
 简易RPC框架

### 第一层

利用反射机制实现了简易的远程方法调用，单线程且不分代理，耦合度极高

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


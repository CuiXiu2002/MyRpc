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
outputStream.writeObject(new body.RpcRequest("method1"));
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
body.RpcRequest message=(body.RpcRequest) objectInputStream.readObject();
String name=message.getMethodName();
Method method= Server.class.getMethod(name,null);
String res=(String) method.invoke(null);
objectOutputStream.writeObject(res);
```

### 第二次提交

利用动态代理模式拆分了框架和用户，这部分需要自学动态代理模式

动态代理的作用应该是屏蔽了实现细节，使用者调用起来更方便；除此之外，与静态代理的区别应该是，有了动态代理，增加方法/接口/服务时不必再更改代理类，都是统一的传方法名、方法参数（反射机制，不用改太多代码）

仍然单线程且发送完即结束

![image-20230323140708257](https://cuicy-1314839020.cos.ap-shanghai.myqcloud.com/typora-user-images/image-20230323140708257.png)

### 第三次提交

利用zookeeper实现了注册中心，对代码进行了重构，实现了多线程+socket传输，对代码结构有了新的理解

zookeeper给我的感觉就像是中转站、电话簿，a可以在上面查到b的电话号码然后就可以给b打电话了

![image-20230326145614226](https://cuicy-1314839020.cos.ap-shanghai.myqcloud.com/typora-user-images/image-20230326145614226.png)
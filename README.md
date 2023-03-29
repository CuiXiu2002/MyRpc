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

### 第四次提交

学习了黑马的netty教程，教的很不错

之前的三次提交都是比较简单的部分，发现netty才是最重要的，还包括协议设计、编码解码、序列化、粘包半包等等，下面层层剖析

总体来说目前完成情况不佳，包之间耦合严重、netty仅仅会使用很多bug还没有改。下一步解耦与熟悉netty、更新协议和序列化方式，提炼配置文件。

**Socket方法：**

服务端：主线程负责监听连接：while(socket.accept()!=null)，线程池负责处理每一个连接

**BIO和NIO的区别**

- accept：等待客户端建立连接
- BIO：
  - 阻塞在accept和read处，直到有结果；
  - 单线程时，各个客户端发送数据和建立连接不能同时进行，很难受；
  - 多线程，内存占用过多，线程上下文切换成本高；
  - 线程池，只适用于短连接，即每个线程只连接一下，长时间占用导致其他连接阻塞
- NIO：
  - ServerSocketChannel可以设置成非阻塞模式，accept不到则返回null（负责等待连接，一般只有一个）
  - SocketChannel可以设置成非阻塞模式，read不到返回0（负责与各个客户端连接，一般有多个）
  - 单线程依然可以让多个客户端同时工作
  - 缺点：一直空转，消耗cpu资源
  - 改进：selector机制（多路复用），可以监控多个channel读写事件。一个selector可以注册多个channel并绑定感兴趣的事件（read或accept），在select方法处阻塞，但是当有read或accept时都可以得到处理（结合代码）
  - 多线程：主线程负责accept，子线程通过selector监控多个channel

- 以上的IO都是同步IO：发起请求、获取结果由一个线程完成（自己买烟）
- 异步IO：发起请求由a完成，b线程得到结果后送给a线程（小弟买烟）

**Netty**

helloworld例子与流程分析

![0040](https://cuicy-1314839020.cos.ap-shanghai.myqcloud.com/typora-user-images/0040.png)

netty组件分析

- eventloop：一个eventloop会从始至终负责一个或多个channel的io事件,但是不一定是所有handler

<img src="https://cuicy-1314839020.cos.ap-shanghai.myqcloud.com/typora-user-images/0041.png" alt="0041" style="zoom:50%;" />

- future：java的future一般与异步有关
  - bootstrap的connect方法或serverbootstrap的bind方法会返回channelfuture对象，channel的closefuture方法返回closefuture对象
  - channelfuture对象的方法大多是异步方法，要么sync等着，要么addlinsner异步调用；
  - sync方法和lisener方法的执行线程不同，lisener的异步回调方法通常由负责关闭的线程调用
- channel：调用channelfuture的channel方法返回channel，可以用于write数据
- handler：所有handler都是channelinboundhandler和channeloutbondhandler的子类

**数据处理流程**

![image-20230329151319682](https://cuicy-1314839020.cos.ap-shanghai.myqcloud.com/typora-user-images/image-20230329151319682.png)


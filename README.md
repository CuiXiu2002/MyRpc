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

### 一些思考

前期仅仅实现了功能，对于为什么要实现，什么是rpc、网络的应用层相关知识、netty的原理等缺乏思考，被面试官拷打后重新学习

- 蚂蚁（记一下没答上来的点）
  - nio和bio多线程实现的区别
  - 为什么要设计一个rpc
    - 服务的解耦
    - 效率高（基于tcpip）
    - 可以去设计一些负载均衡、高可用的策略
  - selector的底层原理

#### http、rest和rpc（链接到网络八股）

- HTTP

  - 应用层协议

  - request

    - 请求行
    - 请求头
    - 空行
    - 请求体

    ![img](https://pic1.zhimg.com/80/v2-f16cb5e6227a43dae416a64d81bd4d88_720w.webp)

  - response

    - 状态行：HTTP版本号、状态码和状态描述

    - 响应头

    - 空行

    - 响应体

      ![img](https://pic2.zhimg.com/80/v2-3d5199c819af0c690b2beac551ad21ad_720w.webp)

![img](https://upload-images.jianshu.io/upload_images/16034279-1bb909dbe105103d.png?imageMogr2/auto-orient/strip|imageView2/2/w/1182/format/webp)

http协议不负责数据包的传输（那是下层的事），只是规定了传输的内容格式

- rest
  - 表述性状态转移
  - REST 基于HTTP协议定义的通用动词方法(GET、PUT、DELETE、POST)来实现对资源(URI)的4种CRUD
    - get：从服务器取出资源（参数在url里）
    - post：向服务器提交资源（安全）
  - 以状态码为核心表示结果
- rpc
  - 远程过程调用
  - 多用于解耦服务
  - soa时代的产物，服务间通信
- rpc和http rest的区别
  - rpc
    - 速度快（基于TCP/IP）
    - 可以去自定义一些负载均衡策略
    - 消息格式：二进制
    - 一般用于服务间通信(快而不规范的，只适用于内部)
    - 在http2.0问世之后，rpc已经没有优势了，rpc框架的主要优势在于主要是服务治理
  - http
    - http相对更规范，更标准，更通用，无论哪种语言都支持http协议。
    - 消息格式：xml、json（http1.0/http1.1）
    - 文本阅读友好，接口规范
    - 一般用于对外接口

#### BIO和NIO的比较

##### BIO多线程模式下，和NIO有什么区别？

- BIO

![img](https://dongzl.github.io/netty-handbook/_media/chapter02/chapter02_01.png)

- NIO

<img src="https://dongzl.github.io/netty-handbook/_media/chapter03/chapter03_01.png" alt="img" style="zoom:50%;" />

<img src="https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/9b475e421b71493483877d00b2da5e2e~tplv-k3u1fbpfcp-zoom-in-crop-mark:4536:0:0:0.awebp" alt="img" style="zoom:50%;" />

1. `BIO` 以流的方式处理数据（每次处理一个或多个字节），而 `NIO` 以块的方式处理数据，块 `I/O` 的效率比流 `I/O` 高很多。
   - 流：单向；channel：双向
   - channel可以实现异步I/O操作
2. `BIO` 是阻塞的，`NIO` 则是非阻塞的。
3. `Selector`（选择器）用于监听多个通道的事件（比如：连接请求，数据到达等），因此使用单个线程就可以监听多个客户端通道。

##### selector底层怎么实现的？（链接到操作系统相关八股，红黑树连接到数据结构相关八股）

- selector内部维护了三个selectionKeySet，分别是
  - keys：每一个channel绑定一个key，关注相应的事件（channel注册时会添加到该集合中）
  - selectedKeys：发生关注的事件的key
  - cancelledKeys：取消操作的key

- selector.open方法

  通过provider类创建selector对象，对于不同的操作系统有不同的类

- new channel

  构建socket的文件描述符

- channel.register方法
  - 构建对应的key并添加到keyset
  - 将channel对应的文件描述符注册到epoll中
- selector.select方法
  - 首先都有必要检查和更新状态，处理修改队列和取消注册队列；
  - 通过`EPoll.wait`方法来获取处于就绪状态的I/O文件描述符数量；
  - 最后更新状态，返回本次`doSelect`更新过的键的数量。（通过epollwait方法返回的文件描述符来更新selectedKeys）
- epoll
  - poll和select需要无差别**轮询**所有的fd，而epoll因为socket就绪时会调用**回调**函数在就绪链表中添加对应的fd，只需要检查就绪链表中有没有数据，复杂度为O(1)
  - epoll使用红黑树存储所监听的socket，插入和删除效率较高
  - 边缘触发模式：当被监控的 Socket 描述符上有可读事件发生时，**服务器端只会从 epoll_wait 中苏醒一次**，一般搭配非阻塞IO
  - 水平触发模式：当被监控的 Socket 上有可读事件发生时，**服务器端不断地从 epoll_wait 中苏醒，直到内核缓冲区数据被 read 函数读完才结束**

#### Netty原理（链接到线程池、集合八股）

##### Netty线程模型：Reactor模型

- 单reactor单线程

  <img src="https://dongzl.github.io/netty-handbook/_media/chapter05/chapter05_04.png" alt="img" style="zoom:50%;" />

- 单reactor多线程

  <img src="https://dongzl.github.io/netty-handbook/_media/chapter05/chapter05_05.png" alt="img" style="zoom:50%;" />

- 主从reactor多线程

  <img src="https://dongzl.github.io/netty-handbook/_media/chapter05/chapter05_06.png" alt="img" style="zoom:50%;" />

- netty线程模型

  <img src="https://dongzl.github.io/netty-handbook/_media/chapter05/chapter05_10.png" alt="img" style="zoom: 80%;" />

##### 源码解析

- eventloop的组成元素：selector、线程、任务队列
- 启动流程
  - 创建nioServerSocketChannel并绑定accept事件，初始化acceptor handler
  - 启动boss线程
- accept流程
  - selector阻塞直到接收到accept事件
  - 创建nioSocketChannel并绑定read事件
  - 从workerEventloopGroup中取一个来处理该channel
- read流程
  - selector阻塞直到接收到read事件
  - 读入buf，传到后续handler中
- 对于一个eventloop而言，如果任务队列中无任务，则select阻塞，如果队列中有任务就处理任务

<img src="https://cuicy-1314839020.cos.ap-shanghai.myqcloud.com/typora-user-images/image-20230413224958123.png" alt="image-20230413224958123" style="zoom:67%;" />

##### netty的优势

1. 设计优雅：适用于各种传输类型的统一 `API` 阻塞和非阻塞 `Socket`；基于灵活且可扩展的事件模型，可以清晰地分离关注点；高度可定制的线程模型-单线程，一个或多个线程池。
2. 使用方便：详细记录的 `Javadoc`，用户指南和示例；没有其他依赖项，`JDK5（Netty3.x）`或 `6（Netty4.x）`就足够了。
3. 高性能、吞吐量更高：延迟更低；减少资源消耗；最小化不必要的内存复制。
4. 安全：完整的 `SSL/TLS` 和 `StartTLS` 支持。
5. 社区活跃、不断更新：社区活跃，版本迭代周期短，发现的 `Bug` 可以被及时修复，同时，更多的新功能会被加入。

#### 编解码

- java原生序列化
  - 无法跨语言
  - 序列化后的体积太大，是二进制编码的5倍多。
  - 序列化性能太低
- kyro序列化
  - 无法跨语言
  - 先序列化类型，再序列化数据
  - 对于基本数据类型都有相应的序列化器
- 对比
  - 通过对long,int等数据类型，采用变长字节存储来代替java中使用固定字节(4,8)字节的模式，因为在软件开发中，对象的这些值基本上都是小值，能节省很多空间
  - 使用了类似缓存的机制，在一次序列化对象中，在整个递归序列化期间，相同的对象，只会序列化一次，后续的用一个局部int值来代替。

#### 协议

![img](https://ask.qcloudimg.com/http-save/yehe-5503232/et72be7xwc.jpeg?imageView2/2/w/2560/h/7000)

#### 粘包半包（链接到tcp可靠传输八股）

- 产生原因
  - 接收方bytebuf设置太大/太小
  - nagle算法：发送方将多个合并在一起
- 解决办法
  - 短链接
  - 分隔符
  - 固定长度
  - 协议里规定长度

#### 动态代理

##### 静态代理的缺点

- 重复性：每加一个方法，都要在代理类里重复定义一遍（实际每个方法的操作都是一样的，发送数据包）
- 脆弱性：接口改动，代理类跟着改
- 采用动态代理后，要增加方法只要在接口里增加就可以，代理类不用改

##### 动态代理源码分析

我们没有创建代理类的对象，为什么能运行代理类的方法，原理：

Proxy类动态生成一个proxyX子类（类似于静态代理的代理类），它会通过反射机制获得被代理类的method对象，然后调用handler的invoke方法

<img src="https://img-blog.csdnimg.cn/3e24c558432a474a9fa988d4cccc6e3a.png" alt="img" style="zoom:200%;" />

package transport.netty;

import body.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoop;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import register.RegisterCenter;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Lenovo
 * @create 2023-03-28-14:34
 */
public class NettyClient {
    Channel channel;
    Object result;
    public NettyClient() throws UnknownHostException, InterruptedException {
        ChannelFuture connect = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new RequestCodec());
                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                result=msg;
                            }
                        });
                    }
                })
                .connect(InetAddress.getLocalHost(),50000);
        channel=connect.sync().channel();
    }
    public Object sendRequest(RpcRequest rpcRequest) throws Exception {
//        String host= RegisterCenter.getService(rpcRequest.getInterfaceName());
//        String ip=host.split(":")[0];
//        int port= Integer.parseInt(host.split(":")[1]);
        channel.writeAndFlush(rpcRequest);
        //必须要等到channelfuture读完才能返回,目前还没有想到怎么做
        Thread.sleep(3000);
        return result;
    }
}

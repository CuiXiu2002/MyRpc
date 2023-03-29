package transport.netty;

import body.RpcRequest;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import test.MethodProvider;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

/**
 * @author Lenovo
 * @create 2023-03-28-14:36
 */
public class NettyServer {
    public NettyServer(){
        ChannelFuture bind = new ServerBootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioServerSocketChannel.class)
                .childHandler(
                        new ChannelInitializer<NioSocketChannel>() {
                            @Override
                            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                                nioSocketChannel.pipeline().addLast(new RequestCodec());
                                nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                    @Override
                                    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                        RpcRequest rpcRequest=(RpcRequest) msg;
                                        System.out.println(msg);
                                        String name=rpcRequest.getMethodName();
                                        Object[] paraArray= rpcRequest.getParameterValues();
                                        Class[] typeArray= rpcRequest.getParameterTypes();
                                        Object service= MethodProvider.getService(rpcRequest.getInterfaceName());
                                        Method method= service.getClass().getMethod(name, typeArray);
                                        Object res= method.invoke(service,paraArray);
                                        ctx.writeAndFlush(res);
                                    }
                                });
                            }
                        }
                )
                .bind(50000);
    }

}

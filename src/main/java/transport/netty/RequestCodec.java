package transport.netty;

import body.RpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * @author Lenovo
 * @create 2023-03-28-21:39
 */
/* 魔数、版本号、长度、内容 */
public class RequestCodec extends ByteToMessageCodec<Object> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object rpcRequest, ByteBuf byteBuf) throws Exception {
        //魔数
        byteBuf.writeInt(20020929);
        //版本号
        byteBuf.writeByte(1);
        /* jdk自带的序列化 */
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(rpcRequest);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
        System.out.println("变二进制");

    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int magic=byteBuf.readInt();
        byte version=byteBuf.readByte();
        int length=byteBuf.readInt();
        byte[] content=new byte[length];
        byteBuf.readBytes(content);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object o =  objectInputStream.readObject();
        list.add(o);
        System.out.println("变对象");

    }
}

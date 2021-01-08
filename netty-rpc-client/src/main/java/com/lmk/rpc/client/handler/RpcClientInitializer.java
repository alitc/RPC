package com.lmk.rpc.client.handler;

import com.netty.rpc.codec.*;
import com.netty.rpc.serializer.Serializer;
import com.netty.rpc.serializer.kryo.KryoPoolFactory;
import com.netty.rpc.serializer.kryo.KryoSerializer;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class RpcClientInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        Serializer serializer= KryoSerializer.class.newInstance();
        ChannelPipeline cp=socketChannel.pipeline();
        cp.addLast(new IdleStateHandler(0,0, Beat.BEAT_INTERVAL, TimeUnit.SECONDS));
        cp.addLast(new RpcEncoder(RpcRequest.class,serializer));
        cp.addLast(new LengthFieldBasedFrameDecoder(65536,0,4,0,0));
        cp.addLast(new RpcDecoder(RpcResponse.class,serializer));
        cp.addLast(new RpcClientHandler());
    }
}

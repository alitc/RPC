package com.lmk.rpc.client.handler;

import com.lmk.rpc.client.connect.ConnectionManager;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcFuture {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private EventLoopGroup eventLoopGroup=new NioEventLoopGroup(4);
}

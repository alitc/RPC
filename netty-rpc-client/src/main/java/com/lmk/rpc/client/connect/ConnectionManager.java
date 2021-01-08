package com.lmk.rpc.client.connect;

import com.lmk.rpc.client.handler.RpcClientHandler;
import com.lmk.rpc.client.route.RpcLoadBalance;
import com.lmk.rpc.client.route.RpcLoadBalanceRoundRobin;
import com.netty.rpc.protocol.RpcProtocol;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionManager {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private EventLoopGroup eventLoopGroup=new NioEventLoopGroup(4);
    private static ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(4,8,600L, TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(1000));

    private Map<RpcProtocol, RpcClientHandler> connectedServerNodes=new ConcurrentHashMap<>();
    private CopyOnWriteArraySet<RpcProtocol> rpcProtocolSet=new CopyOnWriteArraySet<>();
    private ReentrantLock lock=new ReentrantLock();
    private Condition connected=lock.newCondition();
    private long waitTimeOut=5000;
    private RpcLoadBalance loadBalance=new RpcLoadBalanceRoundRobin();
    private volatile boolean isRunning=true;

    private ConnectionManager() {
    }

    private static class SingletonHolder{
        private static final ConnectionManager instance=new ConnectionManager();
    }

    public static ConnectionManager getInstance(){
        return SingletonHolder.instance;
    }

    public void updateConnectedServer(List<RpcProtocol> serviceList){
        // Now using 2 collections to manage the service info and TCP connections because making the connection is async
        // Once service info is updated on ZK, will trigger this function
        // Actually client should only care about the service it is using
        if (serviceList!=null&&serviceList.size()>0){
            // Update local server nodes cache
            HashSet<RpcProtocol> serviceSet=new HashSet<>(serviceList.size());
            for (int i=0;i<serviceList.size();++i){
                RpcProtocol rpcProtocol=serviceList.get(i);
                serviceSet.add(rpcProtocol);
            }

            //Add new server info
            for (final RpcProtocol rpcProtocol:serviceSet){
                if (!rpcProtocolSet.contains(rpcProtocol)){
                    //connectServerNode(rpcProtocol);
                }
            }

            // Close and remove invalid server nodes
            for (RpcProtocol rpcProtocol : rpcProtocolSet) {
                if(!serviceSet.contains(rpcProtocol)){
                    logger.info("Remove invalid service: " + rpcProtocol.toJson());
                    RpcClientHandler handler=connectedServerNodes.get(rpcProtocol);
                    if (handler!=null){
                    }
                }
            }
        }
    }
}

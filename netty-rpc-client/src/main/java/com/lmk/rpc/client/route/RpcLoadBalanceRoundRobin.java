package com.lmk.rpc.client.route;

import com.lmk.rpc.client.handler.RpcClientHandler;
import com.netty.rpc.protocol.RpcProtocol;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Round robin load balance
 */
public class RpcLoadBalanceRoundRobin extends RpcLoadBalance{
    private AtomicInteger roundRobin=new AtomicInteger(0);

    public RpcProtocol doRoute(List<RpcProtocol> addressList){
        int size=addressList.size();
        //Round robin
        int index=(roundRobin.getAndAdd(1)+size)%size;
        return addressList.get(index);
    }

    @Override
    public RpcProtocol route(String serviceKey, Map<RpcProtocol, RpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RpcProtocol>> serviceMap = getServiceMap(connectedServerNodes);
        List<RpcProtocol> rpcProtocolList = serviceMap.get(serviceKey);
        if (rpcProtocolList!=null&&rpcProtocolList.size()>0){
            return doRoute(rpcProtocolList);
        }else {
            throw new Exception("Can not find connection for service: " + serviceKey);
        }
    }
}

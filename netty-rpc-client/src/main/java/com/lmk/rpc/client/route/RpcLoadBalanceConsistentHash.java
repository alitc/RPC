package com.lmk.rpc.client.route;

import com.google.common.hash.Hashing;
import com.lmk.rpc.client.handler.RpcClientHandler;
import com.netty.rpc.protocol.RpcProtocol;

import java.util.List;
import java.util.Map;

public class RpcLoadBalanceConsistentHash extends RpcLoadBalance{

    public RpcProtocol doRoute(String serviceKey, List<RpcProtocol> addressList){
        int index= Hashing.consistentHash(serviceKey.hashCode(),addressList.size());
        return addressList.get(index);
    }
    @Override
    public RpcProtocol route(String serviceKey, Map<RpcProtocol, RpcClientHandler> connectedServerNodes) throws Exception {
        Map<String,List<RpcProtocol>> serviceMap= getServiceMap(connectedServerNodes);
        List<RpcProtocol> addressList=serviceMap.get(serviceKey);
        if (addressList!=null&&addressList.size()>0){
            return doRoute(serviceKey,addressList);
        }else {
            throw new Exception("Can not find connection for service: " + serviceKey);
        }
    }
}

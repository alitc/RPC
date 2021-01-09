package com.lmk.rpc.client.route;

import com.lmk.rpc.client.handler.RpcClientHandler;
import com.netty.rpc.protocol.RpcProtocol;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class RpcLoadBalanceRandom extends RpcLoadBalance{
    private Random random=new Random();

    public RpcProtocol doRoute(List<RpcProtocol> addressList){
        int size=addressList.size();
        //random
        return addressList.get(random.nextInt(size));
    }

    @Override
    public RpcProtocol route(String serviceKey, Map<RpcProtocol, RpcClientHandler> connectedServerNodes) throws Exception {
        Map<String, List<RpcProtocol>> serviceMap = getServiceMap(connectedServerNodes);
        List<RpcProtocol> addressList = serviceMap.get(serviceKey);
        if (addressList!=null&addressList.size()>0){
            return doRoute(addressList);
        }else {
            throw new Exception("Can not find connection for service: " + serviceKey);
        }
    }
}

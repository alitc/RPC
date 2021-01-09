package com.lmk.rpc.client.route;

import com.lmk.rpc.client.handler.RpcClientHandler;
import com.netty.rpc.protocol.RpcProtocol;
import com.netty.rpc.protocol.RpcServiceInfo;
import com.netty.rpc.util.ServiceUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class RpcLoadBalance {
    // Service map: group by service name
    protected Map<String, List<RpcProtocol>> getServiceMap(Map<RpcProtocol, RpcClientHandler> connectedServerNodes){
        Map<String,List<RpcProtocol>> serviceMap=new HashMap<>();
        if (connectedServerNodes!=null&&connectedServerNodes.size()>0){
            for (RpcProtocol rpcProtocol:connectedServerNodes.keySet()){
                for (RpcServiceInfo serviceInfo:rpcProtocol.getServiceInfoList()){
                    String serviceKey= ServiceUtil.makeServiceKey(serviceInfo.getServiceName(),serviceInfo.getVersion());
                    List<RpcProtocol> rpcProtocolList=serviceMap.get(serviceKey);
                    if (rpcProtocolList==null){
                        rpcProtocolList=new ArrayList<>();
                    }
                    rpcProtocolList.add(rpcProtocol);
                    serviceMap.putIfAbsent(serviceKey,rpcProtocolList);
                }
            }
        }
        return serviceMap;
    }

    // Route the connection for service key
    public abstract RpcProtocol route(String serviceKey,Map<RpcProtocol,RpcClientHandler> connectedServerNodes) throws Exception;

}

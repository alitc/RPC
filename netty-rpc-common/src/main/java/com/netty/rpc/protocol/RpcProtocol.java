package com.netty.rpc.protocol;

import com.netty.rpc.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
@Getter
@Setter
public class RpcProtocol implements Serializable {
    private static final long serialVersionUID =1L;
    // service host
    private String host;
    // service port
    private int port;
    // service info list
    private List<RpcServiceInfo> serviceInfoList;

    public String toJson(){
        String json= JsonUtil.objectToJson(this);
        return json;
    }

    public static RpcProtocol fromJson(String json){
        return JsonUtil.jsonToObject(json,RpcProtocol.class);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RpcProtocol that= (RpcProtocol) obj;
        return port==that.port&&
                Objects.equals(host,that.host)&&
                isListEquals(serviceInfoList,that.getServiceInfoList());
    }

    private boolean isListEquals(List<RpcServiceInfo> thisList, List<RpcServiceInfo> thatList) {
        if (thisList == null && thatList == null) {
            return true;
        }
        if ((thisList == null && thatList != null)
                || (thisList != null && thatList == null)
                || (thisList.size() != thatList.size())) {
            return false;
        }
        return thisList.containsAll(thatList)&&thatList.containsAll(thisList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host,port,serviceInfoList.hashCode());
    }

    @Override
    public String toString() {
        return toJson();
    }
}

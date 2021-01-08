package com.netty.rpc.protocol;

import com.netty.rpc.util.JsonUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;
@Getter
@Setter
public class RpcServiceInfo implements Serializable {
    // interface name
    private String serviceName;
    // service version
    private String version;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RpcServiceInfo that = (RpcServiceInfo) o;
        return Objects.equals(serviceName, that.serviceName) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(serviceName, version);
    }

    public String toJson() {
        String json = JsonUtil.objectToJson(this);
        return json;
    }

    @Override
    public String toString() {
        return toJson();
    }
}

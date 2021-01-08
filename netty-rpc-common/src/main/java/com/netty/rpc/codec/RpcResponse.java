package com.netty.rpc.codec;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class RpcResponse implements Serializable {
    private static final long serialVersionUID =1L;

    private String requestId;
    private String error;
    private Object result;

    public boolean isError() {
        return error != null;
    }
}

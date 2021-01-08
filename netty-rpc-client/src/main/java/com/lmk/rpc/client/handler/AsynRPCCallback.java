package com.lmk.rpc.client.handler;

public interface AsynRPCCallback {

    void success(Object result);

    void fail(Exception e);
}


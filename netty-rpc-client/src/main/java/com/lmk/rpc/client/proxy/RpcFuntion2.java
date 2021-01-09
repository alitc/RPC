package com.lmk.rpc.client.proxy;
@FunctionalInterface
public interface RpcFuntion2 <T, P1, P2> extends SerializableFunction<T> {
    Object apply(T t, P1 p1, P2 p2);
}

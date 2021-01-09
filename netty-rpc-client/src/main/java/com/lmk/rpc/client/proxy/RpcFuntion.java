package com.lmk.rpc.client.proxy;
/**
 * lambda method reference
 * g-yu
 */
@FunctionalInterface
public interface RpcFuntion<T,P> extends SerializableFunction<T> {
    Object apply(T t,P p);
}

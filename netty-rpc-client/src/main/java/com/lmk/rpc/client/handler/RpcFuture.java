package com.lmk.rpc.client.handler;

import com.lmk.rpc.client.RpcClient;
import com.lmk.rpc.client.connect.ConnectionManager;
import com.netty.rpc.codec.RpcRequest;
import com.netty.rpc.codec.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * RPCFuture for async RPC call
 */
public class RpcFuture implements Future<Object> {
    private static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private Sync sync;
    private RpcRequest request;
    private RpcResponse response;
    private long startTime;
    private long responseTimeThreshold=5000;
    private List<AsynRPCCallback> pendingCallbacks=new ArrayList<>();
    private ReentrantLock lock=new ReentrantLock();

    public RpcFuture(RpcRequest request) {
        this.sync=new Sync();
        this.request=request;
        this.startTime=System.currentTimeMillis();
    }

    static class Sync extends AbstractQueuedSynchronizer{
        private static final long serialVersionUID = 1L;

        //future status
        private final int done=1;
        private final int pending=0;

        @Override
        protected boolean tryAcquire(int arg) {
            return getState()==done;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState()==pending){
                if (compareAndSetState(pending,done)){
                    return true;
                }else {
                    return false;
                }
            }else {
                return true;
            }
        }

        protected boolean isDone(){
            return getState()==done;
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        //获取锁，1是设置 锁状态 的参数，锁处于可获取状态是，值为0
        sync.acquire(1);
        if (this.response!=null){
            return this.response.getResult();
        }else{
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success=sync.tryAcquireNanos(1,unit.toNanos(timeout));
        if (success){
            if (this.response!=null){
                return this.response.getResult();
            }else{
                return null;
            }
        }else{
            throw new RuntimeException("Timeout exception. Request id: " + this.request.getRequestId()
                    + ". Request class name: " + this.request.getClassName()
                    + ". Request method: " + this.request.getMethodName());
        }
    }

    public void done(RpcResponse response){
        this.response=response;
        sync.release(1);
    }

    private void invokeCallbacks(){
        lock.lock();
        for (final AsynRPCCallback callback:pendingCallbacks){
            run
        }
    }

    public RpcFuture addCallBack(AsynRPCCallback callback){
        lock.lock();
        if (isDone()){
            run
        }
    }

    private void runCallback(final AsynRPCCallback callback){
        final RpcResponse response=this.response;
        RpcClient.submit(new Runnable(){
            @Override
            public void run() {
                if (!response.isError()){
                    callback.success(response.getResult());
                }else {
                    callback.fail(new RuntimeException("Response error", new Throwable(response.getError())));
                }
            }
        })
    }
}

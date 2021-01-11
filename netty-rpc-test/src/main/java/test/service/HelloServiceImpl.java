package test.service;

import com.netty.rpc.annotation.NettyRpcService;

@NettyRpcService(value = HelloService.class,version = "1.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return null;
    }

    @Override
    public String hello(Person person) {
        return null;
    }

    @Override
    public String hello(String name, Integer age) {
        return null;
    }
}

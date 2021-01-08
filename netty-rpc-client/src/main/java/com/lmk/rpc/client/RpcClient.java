package com.lmk.rpc.client;

import com.lmk.rpc.client.discovery.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class RpcClient implements ApplicationContextAware, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private ServiceDiscovery serviceDiscovery;

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }
}

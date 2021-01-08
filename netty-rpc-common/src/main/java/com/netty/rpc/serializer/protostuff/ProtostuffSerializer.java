package com.netty.rpc.serializer.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.netty.rpc.serializer.Serializer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtostuffSerializer extends Serializer {
    private Map<Class<?>, Schema<?>> cacheSchema=new ConcurrentHashMap<>();

    private Objenesis objenesis=new ObjenesisStd(true);

    private <T> Schema<T> getSchema(Class<T> cls){
        // for thread-safe
        return (Schema<T>) cacheSchema.computeIfAbsent(cls, RuntimeSchema::createFrom);
    }
    @Override
    public <T> byte[] serialize(T obj) {
        Class<T> cls= (Class<T>) obj.getClass();
        LinkedBuffer buffer=LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<T> schema=getSchema(cls);
            return ProtobufIOUtil.toByteArray(obj,schema,buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(),e);
        } finally {
            buffer.clear();
        }

    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        try {
            T message=objenesis.newInstance(clazz);
            Schema<T> schema=getSchema(clazz);
            ProtobufIOUtil.mergeFrom(bytes,message,schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}

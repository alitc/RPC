package com.netty.rpc.serializer.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.netty.rpc.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Hessian1Serializer extends Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        HessianOutput hessianOutput=new HessianOutput(byteArrayOutputStream);
        try {
            hessianOutput.writeObject(obj);
            hessianOutput.flush();
            byte[] result = byteArrayOutputStream.toByteArray();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                hessianOutput.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                byteArrayOutputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public <T> Object deserialize(byte[] bytes, Class<T> clazz) {
        ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(bytes);
        HessianInput hessianInput=new HessianInput(byteArrayInputStream);
        try {
            Object result = hessianInput.readObject();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                hessianInput.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                byteArrayInputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}

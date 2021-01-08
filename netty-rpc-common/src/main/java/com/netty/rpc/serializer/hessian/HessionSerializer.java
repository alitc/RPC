package com.netty.rpc.serializer.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.netty.rpc.serializer.Serializer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HessionSerializer extends Serializer {
    @Override
    public <T> byte[] serialize(T obj) {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        Hessian2Output hessian2Output=new Hessian2Output(byteArrayOutputStream);
        try {
            hessian2Output.writeObject(obj);
            hessian2Output.flush();
            byte[] bytes = byteArrayOutputStream.toByteArray();
            return bytes;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                hessian2Output.close();
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
        ByteArrayInputStream inputStream=new ByteArrayInputStream(bytes);
        Hessian2Input hessian2Input=new Hessian2Input(inputStream);
        try {
            Object result = hessian2Input.readObject();
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                hessian2Input.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

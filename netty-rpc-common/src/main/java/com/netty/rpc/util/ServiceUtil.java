package com.netty.rpc.util;

public class ServiceUtil {
    public static final String SERVICE_CONCAT_TOKEN = "#";

    public static String makeServiceKey(String interfaceName,String version){
        String servicekey=interfaceName;
        if (version!=null&&version.trim().length()>0){
            servicekey+=SERVICE_CONCAT_TOKEN.concat(version);
        }
        return servicekey;
    }
}

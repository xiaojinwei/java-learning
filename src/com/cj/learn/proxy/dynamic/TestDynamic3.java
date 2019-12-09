package com.cj.learn.proxy.dynamic;

import com.cj.learn.proxy.statics.IBank;
import sun.misc.ProxyGenerator;

import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * 生成IBank的代理类，保存到本地文件
 */
public class TestDynamic3 {
    public static void main(String[] args) throws Exception{
        byte[] proxyAnimals = ProxyGenerator.generateProxyClass("$Proxy0", new Class[]{IBank.class});
        OutputStream os = new FileOutputStream("$Proxy0.class");
        os.write(proxyAnimals);
        os.flush();
        os.close();
    }
}

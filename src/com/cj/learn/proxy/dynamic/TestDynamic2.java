package com.cj.learn.proxy.dynamic;

import com.cj.learn.proxy.statics.IBank;
import com.cj.learn.proxy.statics.Man;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 此方式相当于Proxy.newProxyInstance的内部实现
 */
public class TestDynamic2 {
    public static void main(String[] args) throws Exception {
        IBank bank = new Man();
        //创建代理类,是一个字节码文件
        Class<?> proxyClass = Proxy.getProxyClass(IBank.class.getClassLoader(), bank.getClass().getInterfaces());
        //通过 proxyClass 获得 一个带有InvocationHandler参数的构造器constructor
        Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
        constructor.setAccessible(true);
        //通过构造器创建一个  动态代理类 实例
        IBank proxy = (IBank) constructor.newInstance(new BankProxy(bank));
        proxy.applyBank();
    }

    public static class BankProxy implements InvocationHandler {

        private Object obj;//被代理类的引用

        public BankProxy(Object obj) {
            this.obj = obj;
        }

        /**
         * 执行被代理类的具体方法
         * @param proxy
         * @param method
         * @param args
         * @return
         * @throws Throwable
         */
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            System.out.println("执行前...");
            Object invoke = method.invoke(obj, args);
            System.out.println("执行后...");
            return invoke;
        }
    }

}

package com.cj.learn.proxy.dynamic;

import com.cj.learn.proxy.statics.IBank;
import com.cj.learn.proxy.statics.Man;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TestDynamic {
    public static void main(String[] args) {
        IBank bank = new Man();
        IBank proxy = (IBank) Proxy.newProxyInstance(IBank.class.getClassLoader(),
                //new Class[]{IBank.class},//或者
                bank.getClass().getInterfaces(),
                new BankProxy(bank));

        proxy.applyBank();
    }

    public static class BankProxy implements InvocationHandler{

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
            System.out.println("执行前");
            Object invoke = method.invoke(obj, args);
            System.out.println("执行后");
            return invoke;
        }
    }
}

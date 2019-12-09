package com.cj.learn.proxy.dynamic;

import com.cj.learn.proxy.statics.Man;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib 实现动态代理
 */
public class TestDynamic4 {
    public static void main(String[] args) {
        //创建增强类
        Enhancer enhancer = new Enhancer();
        //设置超类，因为cglib基于父类 生成代理子类
        enhancer.setSuperclass(Man.class);
        //设置回调，也就是我们的拦截处理
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("执行前...");
                Object result=methodProxy.invokeSuper(o, objects);
                System.out.println("执行后...");
                return result;
            }
        });
        //创建代理类
        Man man = (Man) enhancer.create();
        //执行方法
        man.applyBank();
    }
}

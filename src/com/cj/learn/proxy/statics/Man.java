package com.cj.learn.proxy.statics;

/**
 * 被代理类
 */
public class Man implements IBank{
    @Override
    public void applyBank() {
        System.out.println("自己做一些事情");
    }
}

package com.cj.learn.proxy.statics;

/**
 * 代理类
 */
public class Saleman implements IBank{
    private IBank bank; //持有一个具体被代理者的引用

    public Saleman(IBank bank) {
        this.bank = bank;
    }

    @Override
    public void applyBank() {
        System.out.println("代理执行前做一些事情");
        bank.applyBank();
        System.out.println("代理执行后做做一些事情");
    }
}

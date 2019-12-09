package com.cj.learn.patterns.singleton;

import java.io.Serializable;

/**
 * 静态内部类
 * 和懒汉式相比较创建对象更迟，是在Holder加载的时候创建，而Singleton03类加载的时候，内部类(Holder)是不会加载的，所以这种方式相比懒汉式更优
 */
public class Singleton03 implements Serializable {

    private Singleton03(){
    }

    private static class Holder{
        private static final Singleton03 instance = new Singleton03();
    }

    public static Singleton03 getInstance(){
        return Holder.instance;
    }

    /**
     * 保证序列化与反序列化后得到的仍是同一个对象
     * 单例模式虽然能保证线程安全，但在序列化和反序列化的情况下会出现生成多个对象的情况。
     * 当JVM从内存中反序列化地"组装"一个新对象时，就会自动调用这个 readResolve方法来返回我们指定好的对象了, 单例规则也就得到了保证。
     * readResolve()的出现允许程序员自行控制通过反序列化得到的对象。
     *
     * @return
     */
    protected Object readResolve() {
        return Holder.instance;
    }

}

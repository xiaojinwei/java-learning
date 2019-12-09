package com.cj.learn.patterns.singleton;

/**
 * 饿汉式
 * 类加载时创建对象
 */
public class Singleton01 {
    private static final Singleton01 instance = new Singleton01();

    private Singleton01(){}

    public static Singleton01 getInstance(){
        return instance;
    }
}

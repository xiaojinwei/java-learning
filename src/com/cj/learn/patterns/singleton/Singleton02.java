package com.cj.learn.patterns.singleton;

/**
 * 懒汉式
 */
public class Singleton02 {
    /**
     * volatile : 禁止指令重排序 (如不加此关键词，在多线程环境下可能会创建多个对象或空指针危险)
     */
    private volatile static Singleton02 instance = null;

    private Singleton02(){}

    /**
     * 双重锁 ： 多线程环境下保证安全的前提下也保证了性能
     * @return
     */
    public static Singleton02 getInstance(){
        if(instance == null){
            synchronized (Singleton02.class){
                if(instance == null){
                    instance = new Singleton02();
                }
            }
        }
        return instance;
    }
}

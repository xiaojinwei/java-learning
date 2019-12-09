package com.cj.learn.patterns.singleton;

/**
 * 枚举方式实现单例
 * 枚举类型实现的单例，无法通过反射创建出先的对象，因为jdk层面保证了枚举不能被反射
 * 其他方式实现的单例都可以通过反射创建出新的对象，如杜绝此类事件出现可以使用枚举实现单例
 */
public enum  Singleton04 {
    INSTANCE;

    public void doSomething(){

    }
}

enum  Singleton044 {
    INSTANCE("single");

    private String type;

    Singleton044(String type){
        this.type = type;
    }

    public void doSomething(){

    }
}

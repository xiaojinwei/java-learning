package com.cj.learn.patterns.factory;

/**
 * 抽象工厂模式
 * 符合开闭原则
 * 抽象工厂模式和工厂方法模式的区别就在于，工厂方法模式针对的是一个产品等级结构；而抽象工厂模式则是针对的多个产品等级结构。
 */
public class _AbstractFactory {
    public static void main(String[] args) {
        AbstractFactory3 factory3 = new Factory3();
        factory3.createProduct31().display();
        factory3.createProduct32().display();
    }
}

interface Product31{
    void display();
}

interface Product32{
    void display();
}

class Car3 implements Product31{

    @Override
    public void display() {
        System.out.println("car3");
    }
}

class Book3 implements Product32{

    @Override
    public void display() {
        System.out.println("book3");
    }
}

interface AbstractFactory3{
    Product31 createProduct31();
    Product32 createProduct32();
}

class Factory3 implements AbstractFactory3{

    @Override
    public Product31 createProduct31() {
        return new Car3();
    }

    @Override
    public Product32 createProduct32() {
        return new Book3();
    }
}


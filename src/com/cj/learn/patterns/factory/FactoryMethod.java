package com.cj.learn.patterns.factory;

/**
 * 工厂方法模式
 * 产品和工厂都是抽象的，要实现什么样的产品就要去添加可以实现该产品的工厂
 * 符合开闭原则，但是当产品类别过多时，代码量也很多
 * 抽象工厂模式和工厂方法模式的区别就在于，工厂方法模式针对的是一个产品等级结构；而抽象工厂模式则是针对的多个产品等级结构。
 */
public class FactoryMethod {
    public static void main(String[] args) {
        serviceFactory(new Car2Factory());
        serviceFactory(new Book2Factory());
    }

    public static void serviceFactory(AbstractFactory factory){
        Product2 product2 = factory.create();
        product2.display();
    }
}

interface Product2 {
    void display();
}

abstract class AbstractFactory{
    abstract Product2 create();
}

class Car2 implements Product2{
    @Override
    public void display() {
        System.out.println("Car2");
    }
}

class Car2Factory extends AbstractFactory {
    @Override
    Product2 create() {
        return new Car2();
    }
}

class Book2 implements Product2{
    @Override
    public void display() {
        System.out.println("Book2");
    }
}

class Book2Factory extends AbstractFactory {

    @Override
    Product2 create() {
        return new Book2();
    }
}

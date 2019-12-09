package com.cj.learn.patterns.factory;

/**
 * 简单工厂模式
 * 实现简单但是不符合开闭原则
 */
public class SimpleFactory {
    public static void main(String[] args) {
        Product car = Factory.create("car");
        car.display();
    }
}

interface Product{
    void display();
}

class Car implements Product{
    @Override
    public void display() {
        System.out.println("car");
    }
}

class Book implements Product{
    @Override
    public void display() {
        System.out.println("book");
    }
}

class Factory{
    public static Product create(String type){
        switch (type){
            case "car" :
                return new Car();
            case "book":
                return new Book();
        }
        throw new RuntimeException("not found type");
    }
}
package com.cj.learn.type;

import java.util.ArrayList;
import java.util.List;

/**
 * 协变&逆变
 *
 * 协变：<? extends T>  对应kotlin的 out
 * 逆变：<? super T>   对应kotlin的 in
 *
 * 从数据流来看，extends是限制数据来源的（生产者），而super是限制数据流入的（消费者）。
 *
 * <? super T>就是限制add方法传入的类型必须是T及其子类型。
 *
 * 如：Collections.copy(List<? super T> dest, List<? extends T> src)
 *
 * List<?>,List<T>,List<? extends T> 无法确定其指向类型，所以不能add
 * List<? super T>可以确定指向类型的下限，所以可以add
 */
public class CovariantContravariantTest {
    public static void main(String[] args) {
        covariantArray();
    }

    /**
     * <?>被称为无界通配符
     * 无法确定其类型，无法add
     */
    public static void captureConversion(){
        List<?> list = new ArrayList<Dog>();
        //list.add(new Dog());
        eat3(new ArrayList<Animal>());
        eat3(new ArrayList<Dog>());
        convert(new ArrayList<Dog>());
        convert(new ArrayList<Animal>());
    }

    public static <T> void convert(List<T> list) {
        //list.add(new Animal());
        T t = list.get(0);
    }

    public static void eat3(List<?> list){
        //list.add(new Object());//逆变可以添加，因为可以保证类型安全
        Dog object = (Dog) list.get(0);
    }

    /**
     * 逆变
     * 关键字super指出泛型的下界为Dog，<？ super T>称为超类通配符，代表一个具体类型，而这个类型是Dog的超类。
     * 这样编译器就知道向其中添加Dog或Dog的子类型是安全的了。
     */
    public static void superTypeWildcards(){
        //<? super Dog>只能指向Dog及Dog的父类，所以集合类型是Dog及Dog的父类，则向集合中添加Dog及Dog的子类是安全的
        List<? super Dog> list = new ArrayList<Animal>();
        //list.add(new Animal());//只能添加Dog或Dog的子类
        list.add(new Dog());
        list.add(new TY());

        eat2(new ArrayList<Animal>());
        eat2(new ArrayList<Dog>());
        //eat2(new ArrayList<TY>());
    }

    public static void eat2(List<? super Dog> list){
        list.add(new Dog());//逆变可以添加，因为可以保证类型安全
        Dog object = (Dog) list.get(0);
    }

    /**
     * 协变
     * <? extends Animal>，extends指出了泛型的上界为Animal，
     * <? extends T>称为子类通配符，意味着某个继承自Animal的具体类型
     *
     * List<? extends Animal>可以指向new ArrayList<Dog>()，new ArrayList<Dog>()等，
     * 编译器并不知道List<? extends Animal>指向的是哪一个，所以一旦执行这种类型的向上转型，就将丢失掉向其中传递任何对象的能力(不能add()了)。
     */
    public static void genericsAndCovariance(){
        //<? extends Animal>只能指向Animal及Animal的子类，所以集合类型是Animal及Animal的子类，可以是Animal,Dog,TY等，
        //则向集合中添加Dog及Dog的子类是不安全的，因为编译器不知道<? extends Animal>指向的是哪种类型，
        //泛型是将类型检测放在编译期，编译期不能确定<? extends Animal>具体是什么类型，直接不让其添加，保证其安全性
        List<? extends Animal> list = new ArrayList<Dog>();
        List<? extends Animal> list2 = new ArrayList<TY>();
        //list.add(new Animal());
        //list.add(new Dog());
        //list.add(new TY());

        //可以向上转型
        eat(new ArrayList<Animal>());
        eat(new ArrayList<Dog>());
        eat(new ArrayList<TY>());
    }

    public static void eat(List<? extends Animal> list){
        //list.add(new Animal());//协变不能添加，因为不可以保证类型安全
        Animal animal = list.get(0);
        Object animal2 = list.get(0);
        //Dog animal3 = list.get(0);
    }

    /**
     * 泛型与数组不同，泛型没有内建的协变类型。
     * 这是因为数组在语言中是完全定义的，因此内建了编译期和运行时的检查，
     * 但是在使用泛型时，类型信息在编译期被擦除了，运行时也就无从检查。
     * 因此，泛型将这种错误检测移入到编译期。
     *
     */
    public static void covariantList(){
        //List<Animal> list = new ArrayList<Dog>();
    }

    /**
     * 数组是协变的
     * Java中数组是协变的，可以向子类型的数组赋予基类型的数组引用
     */
    public static void covariantArray(){
        Animal[] animal = new Animal[3];
        animal[0] = new Dog();
        Animal[] animal2 = new Dog[3];
        animal2[0] = new TY();
        animal2[1] = new Animal();

        Dog[] dogs = new Dog[3];

        eat(dogs);
    }

    public static void eat(Animal[] animals){

    }


}

 class Animal{

}

 class Dog extends Animal{

}

 class TY extends Dog{

}

 class Cat extends Animal{

}

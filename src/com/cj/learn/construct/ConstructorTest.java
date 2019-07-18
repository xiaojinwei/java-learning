package com.cj.learn.construct;

import com.cj.learn.construct.internal.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 给定类型创建相应对象
 * 可用于ORM和json反序列化对象时，对象的创建
 */
public class ConstructorTest {
    public static void main(String[] args) throws Exception {
        //test1();
        //test2();
        //test3();
        test4();
    }

    private static void test4() {
        ConstructorFactory instance = ConstructorFactory.getInstance();
        ArrayList arrayList = instance.construct(ArrayList.class);
        List list = instance.construct(List.class);
        Map map = instance.construct(Map.class);
        System.out.println(arrayList);
        System.out.println(list.getClass());
        System.out.println(map.getClass());
    }

    public static void test3()throws Exception {
        ConstructorFactory instance = ConstructorFactory.getInstance();
        instance.addInstanceCreator(B.class, new InstanceCreator<B>() {
            @Override
            public B createInstance(Type type) {
                return new B(10);
            }
        });
        B b = instance.construct(B.class);
        A a = instance.construct(A.class);
        System.out.println(b);
        System.out.println(a);
    }

    public static void test2()throws Exception {
        ConstructorConstructor constructor = new ConstructorConstructor(Collections.<Type, InstanceCreator<?>>emptyMap());
        ObjectConstructor<A> objectObjectConstructor = constructor.get(TypeToken.get(A.class));
        A construct = objectObjectConstructor.construct();
        System.out.println(construct);
        ObjectConstructor<B> objectObjectConstructor2 = constructor.get(TypeToken.get(B.class));
        B construct2 = objectObjectConstructor2.construct();
        System.out.println(construct2);
    }

    /**
     * 核心代码
     * 当一个类没有提供默认构造，就会尝试使用JVM unsafe包来创建对象，失败再尝试使用dalvikvm ObjectStreamClass和ObjectInputStream来创建对象
     * @throws Exception
     */
    public static void test1()throws Exception {
        UnsafeAllocator unsafeAllocator = UnsafeAllocator.create();
        A a = unsafeAllocator.newInstance(A.class);
        System.out.println(a.x);
    }
}

class B {
    private int x;
    private B(){

    }

    public B(int x){
        this.x = x;
    }

    @Override
    public String toString() {
        return "B{" +
                "x=" + x +
                '}';
    }
}

class A{
    int x;
    private A(int x){
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    @Override
    public String toString() {
        return "A{" +
                "x=" + x +
                '}';
    }
}

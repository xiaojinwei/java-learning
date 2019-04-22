package com.cj.learn.concurrent;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.function.UnaryOperator;

public class AtomicReferenceFieldUpdaterTest {
    public static void main(String[] args) {
        SimpleValueHolder holder = new SimpleValueHolder();
        SimpleValueHolder.valueUpdater.compareAndSet(holder,"Hello","World");
        System.out.println(SimpleValueHolder.valueUpdater.get(holder));//HolleWorld
        System.out.println(holder.value);//HolleWorld
        String value = SimpleValueHolder.valueUpdater.getAndUpdate(holder, new UnaryOperator<String>() {
            @Override
            public String apply(String s) {
                return "HelloAtomic";
            }
        });
        System.out.println(value);//HolleWorld
        System.out.println(SimpleValueHolder.valueUpdater.get(holder));//HelloAtomic
        System.out.println(holder.value);//HelloAtomic
    }
}

class SimpleValueHolder{
    public static AtomicReferenceFieldUpdater<SimpleValueHolder,String> valueUpdater = AtomicReferenceFieldUpdater.newUpdater(SimpleValueHolder.class,String.class,"value");
    volatile String value = "HolleWorld";//必须是volatile修饰
}

package com.cj.learn.concurrent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

public class AtomicReferenceTest {
    public static void main(String[] args) {
        AtomicReferenceValueHolder holder = new AtomicReferenceValueHolder();
        holder.atomicValue.compareAndSet("Holle","World");
        System.out.println(holder.atomicValue.get());//HolleWorld
        String value = holder.atomicValue.getAndUpdate(new UnaryOperator<String>() {
            @Override
            public String apply(String s) {
                return "HolleAtomic";
            }
        });
        System.out.println(value);//HolleWorld
        System.out.println(holder.atomicValue.get());//HolleAtomic
    }
}

class AtomicReferenceValueHolder{
    AtomicReference<String> atomicValue = new AtomicReference<>("HolleWorld");//内部其实也有private volatile V value;
}
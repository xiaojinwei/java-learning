package com.cj.learn.construct.internal;

public interface ObjectConstructor<T> {

    /**
     * Returns a new instance.
     */
    public T construct();
}
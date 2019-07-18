package com.cj.learn.construct.internal;

import java.lang.reflect.Type;

public interface InstanceCreator<T> {

    /**
     * 创建指定的类型
     *
     * @param type the parameterized T represented as a {@link Type}.
     * @return a default object instance of type T.
     */
    public T createInstance(Type type);
}

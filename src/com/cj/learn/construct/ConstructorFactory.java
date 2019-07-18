package com.cj.learn.construct;

import com.cj.learn.construct.internal.ConstructorConstructor;
import com.cj.learn.construct.internal.InstanceCreator;
import com.cj.learn.construct.internal.ObjectConstructor;
import com.cj.learn.construct.internal.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * 类型对象创建工厂
 * 可用于ORM和json反序列化对象时，对象的创建
 * 不提供默认public构造也可以创建相应对象
 */
public class ConstructorFactory {
    private final Map<Type, InstanceCreator<?>> instanceCreators;
    private final ConstructorConstructor mConstructor;

    private ConstructorFactory() {
        this.instanceCreators = new HashMap<>();
        this.mConstructor = new ConstructorConstructor(this.instanceCreators);
    }

    private volatile static ConstructorFactory mInstance ;

    public static ConstructorFactory getInstance(){
        if(mInstance == null){
            synchronized (ConstructorFactory.class){
                if(mInstance == null){
                    mInstance = new ConstructorFactory();
                }
            }
        }
        return mInstance;
    }

    public void addInstanceCreator(Class<?> clazz, InstanceCreator<?> instanceCreator){
        this.instanceCreators.put(clazz,instanceCreator);
    }

    public <T> T construct(Class<T> clazz){
        ObjectConstructor<T> objectConstructor = this.mConstructor.get(TypeToken.get(clazz));
        return objectConstructor.construct();
    }

}

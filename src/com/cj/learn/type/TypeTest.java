package com.cj.learn.type;

import java.io.Serializable;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

/**
 * 泛型
 */
public class TypeTest {
}

/**
 * List<String>[]，T[]都是泛型数组，而String[]则不是，String[]属于Class普通类型
 * GenericArrayType的实现类是ParameterizedTypeImpl
 * @param <T>
 */
class GenericArrayTypeTest<T> {
    public List<String>[] testGenericArrayType(T[] args){
        return null;
    }

    public static void main(String[] args) throws Exception{
        Method testGenericArrayTypeMethod = GenericArrayTypeTest.class.getDeclaredMethod("testGenericArrayType", Object[].class);
        System.out.println(testGenericArrayTypeMethod);
        //参数类型
        Type[] genericParameterTypes = testGenericArrayTypeMethod.getGenericParameterTypes();
        GenericArrayType genericArrayType = (GenericArrayType) genericParameterTypes[0];//获取第0个参数
        System.out.println("genericArrayType : " + genericArrayType);//genericArrayType : T[]
        Type genericComponentType = genericArrayType.getGenericComponentType();
        System.out.println("genericComponentType : " + genericComponentType);//genericComponentType : T
        //返回值类型
        Type genericReturnType = testGenericArrayTypeMethod.getGenericReturnType();
        GenericArrayType genericArrayType2 = (GenericArrayType) genericReturnType;//获取返回值类型
        System.out.println("genericArrayType2 : " + genericArrayType2);//genericArrayType2 : java.util.List<java.lang.String>[]
        Type genericComponentType2 = genericArrayType2.getGenericComponentType();
        System.out.println("genericComponentType2 : " + genericComponentType2);//genericComponentType2 : java.util.List<java.lang.String>
    }
}

/**
 * List<T>、Map<K,V>带有泛型的类型就是参数化类型
 * List这种没有代泛型的类型就不是参数化类型，这个类型是Class
 * ParameterizedType的实现类是ParameterizedTypeImpl
 * 注意ParameterizedType和TypeVariable的区别，List<T>是ParameterizedType，T是TypeVariable
 * @param <T>
 */
class ParameterizedTypeTest<T> {

    public List<T> data;

    public List<String> testParameterizedType(Map<Integer,String> arg){
        return null;
    }

    public static void main(String[] args) throws Exception{
        Method testParameterizedTypeMethod = ParameterizedTypeTest.class.getDeclaredMethod("testParameterizedType", Map.class);
        System.out.println(testParameterizedTypeMethod);//public java.util.List ParameterizedTypeTest.testParameterizedType(java.util.Map)
        //参数类型
        Type[] genericParameterTypes = testParameterizedTypeMethod.getGenericParameterTypes();
        ParameterizedType parameterizedType = (ParameterizedType) genericParameterTypes[0];
        System.out.println("parameterizedType : " + parameterizedType);//parameterizedType : java.util.Map<java.lang.Integer, java.lang.String>
        //也就是泛型有几个，数组的个数就是几个，这里是2个
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        System.out.println("actualTypeArguments[0] : " + actualTypeArguments[0]);//actualTypeArguments[0] : class java.lang.Integer
        System.out.println("actualTypeArguments[1] : " + actualTypeArguments[1]);//actualTypeArguments[1] : class java.lang.String
        //原始类型，就是没有泛型的类型
        Type rawType = parameterizedType.getRawType();
        System.out.println("rawType : " + rawType);//rawType : interface java.util.Map
        Type ownerType = parameterizedType.getOwnerType();
        System.out.println("ownerType : " + ownerType);//ownerType : null
        //返回值类型
        Type genericReturnType = testParameterizedTypeMethod.getGenericReturnType();
        ParameterizedType parameterizedType2 = (ParameterizedType)genericReturnType;
        System.out.println("parameterizedType2 : " + parameterizedType2);//parameterizedType2 : java.util.List<java.lang.String>
        Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
        System.out.println("actualTypeArguments2[0] : " + actualTypeArguments2[0]);//actualTypeArguments2[0] : class java.lang.String
        Type rawType2 = parameterizedType2.getRawType();
        System.out.println("rawType2 : " + rawType2);//rawType2 : interface java.util.List
        Type ownerType2 = parameterizedType2.getOwnerType();
        System.out.println("ownerType2 : " + ownerType2);//ownerType2 : null

        //字段类型
        Field dataField = ParameterizedTypeTest.class.getDeclaredField("data");
        Type genericType = dataField.getGenericType();//获取字段的泛型参数类型
        ParameterizedType parameterizedType3 = (ParameterizedType)genericType;
        System.out.println("parameterizedType3 : " + parameterizedType3);//parameterizedType3 : java.util.List<T>
        Type[] actualTypeArguments3 = parameterizedType3.getActualTypeArguments();
        //actualTypeArguments3[0]是TypeVariable类型
        System.out.println("actualTypeArguments3[0] : " + actualTypeArguments3[0]);//actualTypeArguments3[0] : T
        Type rawType3 = parameterizedType3.getRawType();
        System.out.println("rawType3 : " + rawType3);//rawType3 : interface java.util.List
        Type ownerType3 = parameterizedType3.getOwnerType();
        System.out.println("ownerType3 : " + ownerType3);//ownerType3 : null
    }
}


/**
 * Map<? extends Object,? super String> 和 List<? extends T> 这样的通配符表达式是泛型表达式类型，
 * 不过需要通过ParameterizedType类型的getActualTypeArguments方法获取，因为List<? extends T>等仍然是ParameterizedType类型，只是里面的泛型 ? extends T 是WildcardType类型
 * WildcardType的实现类是WildcardTypeImpl
 * 类或者方法声明的泛型上可以使用&（并且）操作符
 * &不能用于?通配符上（因为通配符不能放在泛型的声明上），因为&只能放在泛型的声明上
 **/
class WildcardTypeTest {

    public Class<?> clazz; //默认<? extends Object>

    /**
     * 方法参数中的Type获取方法：method.getGenericParameterTypes()
     * 方法返回值的Type获取方法：method.getGenericReturnType()
     * 方法定义上的泛型的Type获取方法：method.getTypeParameters()
     * @param arg
     * @param <T>
     * @return
     */
    public <T extends Object & Serializable> List<? extends T> testWildcardType(Map<? extends Object,? super String> arg){
        return null;
    }

    public static void main(String[] args) throws Exception {
        Method testWildcardTypeMethod = WildcardTypeTest.class.getDeclaredMethod("testWildcardType", Map.class);
        //参数类型
        Type[] genericParameterTypes = testWildcardTypeMethod.getGenericParameterTypes();
        ParameterizedType parameterizedType = (ParameterizedType) genericParameterTypes[0];
        System.out.println("parameterizedType : " + parameterizedType);//parameterizedType : java.util.Map<?, ? super java.lang.String>
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        WildcardType wildcardType1 = (WildcardType) actualTypeArguments[0];//获取Map泛型中的第一个泛型参数
        Type[] upperBounds = wildcardType1.getUpperBounds();
        System.out.println("upperBounds[0] : " + upperBounds[0]);//upperBounds[0] : class java.lang.Object
        Type[] lowerBounds = wildcardType1.getLowerBounds();//空数组
        WildcardType wildcardType2 = (WildcardType) actualTypeArguments[1];
        Type[] upperBounds2 = wildcardType2.getUpperBounds();//空数组
        Type[] lowerBounds2 = wildcardType2.getLowerBounds();
        System.out.println("lowerBounds2[0] : " + lowerBounds2[0]);//lowerBounds2[0] : class java.lang.String
        //返回值类型
        Type genericReturnType = testWildcardTypeMethod.getGenericReturnType();
        ParameterizedType parameterizedType2 = (ParameterizedType) genericReturnType;
        System.out.println("parameterizedType2 : " + parameterizedType2);//parameterizedType2 : java.util.List<? extends T>
        Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
        WildcardType wildcardType3 = (WildcardType) actualTypeArguments2[0];
        Type[] upperBounds3 = wildcardType3.getUpperBounds();//upperBounds3[0]的是TypeVariable类型
        System.out.println("upperBounds3[0] : " + upperBounds3[0]);////upperBounds3[0] : T

        //方法定义上的泛型
        TypeVariable<Method>[] typeParameters = testWildcardTypeMethod.getTypeParameters();
        //<T extends Object & Serializable>是TypeVariable类型
        TypeVariable typeVariable = typeParameters[0];
        String name = typeVariable.getName();
        System.out.println("name : " + name);//name : T
        Type[] bounds = typeVariable.getBounds();
        //<T extends Object & Serializable>中T的上限是Object & Serializable
        //bounds[0]是Object的Type，也就是Class
        //bounds[1]是Serializable的Type，也就是Class
        System.out.println("bounds[0] : " + bounds[0]);//bounds[0] : class java.lang.Object
        System.out.println("bounds[1] : " + bounds[1]);//bounds[1] : interface java.io.Serializable

        //字段
        Field clazzField = WildcardTypeTest.class.getDeclaredField("clazz");
        Type genericType = clazzField.getGenericType();//获取字段的泛型参数类型
        ParameterizedType parameterizedType4 = (ParameterizedType)genericType;
        System.out.println("parameterizedType4 : " + parameterizedType4);//parameterizedType4 : java.lang.Class<?>
        Type[] actualTypeArguments4 = parameterizedType4.getActualTypeArguments();
        WildcardType wildcardType4 = (WildcardType)actualTypeArguments4[0];
        Type[] upperBounds4 = wildcardType4.getUpperBounds();
        System.out.println("upperBounds4[0] : " + upperBounds4[0]);//upperBounds4[0] : class java.lang.Object
        Type[] lowerBounds4 = wildcardType4.getLowerBounds();//空数组

    }
}


/**
 * List<T>、Map<K,V>中的T，K，V等值是TypeVariable
 * TypeVariable的实现类是TypeVariableImpl
 * 类上限定泛型类型变量的上限可以为多个，必须使用&符号相连接，& 后必须为接口
 * 类上限定泛型只能是上限extends，不能是下限super
 *
 * 类定义上的泛型类型获取方法：
 *  TypeVariableTest.class.getTypeParameters() 或
 *  genericDeclaration.getTypeParameters() //该方式需要通过类中任意方法参数、方法返回值、字段的泛型TypeVariable类型中的getGenericDeclaration()获取
 *
 * @param <T> 有类型上限
 * @param <V> 无类型上限，没有写上限，默认是Object
 */
class TypeVariableTest<T extends Number & Serializable,V> {

    public Map<T,V> map;

    public T[] array;

    public List<String> testTypeVariable(T arg){
        return null;
    }

    public static void main(String[] args) throws Exception{
        Method testParameterizedTypeMethod = TypeVariableTest.class.getDeclaredMethod("testTypeVariable", Number.class);
        System.out.println(testParameterizedTypeMethod);//public java.util.List TypeVariableTest.testTypeVariable(java.lang.Number)
        //参数类型
        Type[] genericParameterTypes = testParameterizedTypeMethod.getGenericParameterTypes();
        TypeVariable typeVariable = (TypeVariable) genericParameterTypes[0];
        String name = typeVariable.getName();
        System.out.println("name : " + name);//name : T
        Type[] bounds = typeVariable.getBounds();//获取对应的上限或下限
        //因为参数arg的类型是泛型T，而泛型T在类上定义有两个上限，Number和Serializable
        //bounds[0]中的类型是Number，而其Type的实现类是Class
        //bounds[1]中的类型是Serializable，而其Type的实现类是Class
        System.out.println("bounds[0] : " + bounds[0]);//bounds[0] : class java.lang.Number
        System.out.println("bounds[1] : " + bounds[1]);//bounds[1] : interface java.io.Serializable
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();//获取声明该类型变量实体，也就是TypeVariableTest
        //genericDeclaration是类TypeVariableTest，Class是GenericDeclaration的实现类
        System.out.println("genericDeclaration : " + genericDeclaration);//genericDeclaration : class TypeVariableTest
        TypeVariable<?>[] typeParameters = genericDeclaration.getTypeParameters();//获取TypeVariableTest类定义的泛型参数
        //typeParameters是指TypeVariableTest上的泛型类型参数，也就是T，V
        String name02 = typeParameters[0].getName();
        System.out.println("name02 : " + name02);//name02 : T
        String name12 = typeParameters[1].getName();
        System.out.println("name12 : " + name12);//name12 : V
        Type[] bounds02 = typeParameters[0].getBounds();//获取上下限
        //T的上限是Number & Serializable，所以有两个，都是原始类型Class类型，具体类Number 和 Serializable的Type是Class
        System.out.println("bounds02[0] : " + bounds02[0]);//bounds02[0] : class java.lang.Number
        System.out.println("bounds02[1] : " + bounds02[1]);//bounds02[1] : interface java.io.Serializable
        GenericDeclaration genericDeclaration02 = typeParameters[0].getGenericDeclaration();//这个获取的还是TypeVariableTest
        Type[] bounds12 = typeParameters[1].getBounds();//获取上下限
        //V的上限是Object（没有指定，默认是Object），所以只有一个，也是原始类型Class类型
        System.out.println("bounds12[0] : " + bounds12[0]);//bounds12[0] : class java.lang.Object
        AnnotatedType[] annotatedBounds = typeVariable.getAnnotatedBounds();//直接获取注解边界(上下限)
        //typeVariable是参数泛型T，而它的上下限定义在类TypeVariableTest上，也就是Number & Serializable
        //所以annotatedBounds中是两个上限Number 和 Serializable
        Type type02 = annotatedBounds[0].getType();//原始类型Class，因为Number是具体类
        System.out.println("type02 : " + type02);//type02 : class java.lang.Number
        Type type12 = annotatedBounds[1].getType();
        System.out.println("type12 : " + type12);//type12 : interface java.io.Serializable

        //返回值类型
        //List<String>没有TypeVariable类型，只有代泛型T的才有,如List<T>或List<V>
        Type genericReturnType = testParameterizedTypeMethod.getGenericReturnType();
        ParameterizedType parameterizedType = (ParameterizedType) genericReturnType;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        //actualTypeArguments[0]对应的是List<String>中的String，而String是具体类型，所以Type是Class
        System.out.println("actualTypeArguments[0] : " + actualTypeArguments[0]);//actualTypeArguments[0] : class java.lang.String

        //字段
        Field mapField = TypeVariableTest.class.getDeclaredField("map");
        Type genericType = mapField.getGenericType();
        ParameterizedType parameterizedType2 = (ParameterizedType) genericType;
        Type[] actualTypeArguments2 = parameterizedType2.getActualTypeArguments();
        //actualTypeArguments2是T,V，也就是TypeVariable
        TypeVariable typeVariable03 = (TypeVariable) actualTypeArguments2[0];//T
        String name03 = typeVariable03.getName();
        System.out.println("name03 : " + name03);//name03 : T
        Type[] bounds3 = typeVariable03.getBounds();
        //T的上限是Number & Serializable，也就是原始类型，所以是Class
        System.out.println("bounds3[0] : " + bounds3[0]);//bounds3[0] : class java.lang.Number
        System.out.println("bounds3[1] : " + bounds3[1]);//bounds3[1] : interface java.io.Serializable
        TypeVariable typeVariable13 = (TypeVariable) actualTypeArguments2[1];//V
        Type[] bounds13 = typeVariable13.getBounds();
        //V的上限是Object，也是原始类型，所以是Class
        System.out.println("bounds13[0] : " + bounds13[0]);//bounds13[0] : class java.lang.Object

        Field arrayField = TypeVariableTest.class.getDeclaredField("array");
        Type genericType2 = arrayField.getGenericType();
        GenericArrayType genericArrayType = (GenericArrayType) genericType2;
        Type genericComponentType = genericArrayType.getGenericComponentType();
        TypeVariable typeVariable2 = (TypeVariable) genericComponentType;//T
        String name4 = typeVariable2.getName();
        System.out.println("name4 : " + name4);//name4 : T
        Type[] bounds4 = typeVariable2.getBounds();
        //T的上下限是 Number & Serializable
        //bounds4[0]和bounds4[1]分别是Number和Serializable的Class
        System.out.println("bounds4[0] : " + bounds4[0]);//bounds4[0] : class java.lang.Number
        System.out.println("bounds4[1] : " + bounds4[1]);//bounds4[1] : interface java.io.Serializable
    }
}


/**
 * 原始/基本类型，也就是不带任何泛型信息的类型，如String,Integer[]的Class
 * Type的直接子类只有一个，也就是Class，代表着类型中的原始类型以及基本类型
 */
class ClassTest {

    public void testClass(String arg){

    }

    public static void main(String[] args) throws Exception{
        Method testClassMethod = ClassTest.class.getDeclaredMethod("testClass", String.class);
        //参数类型
        Type[] genericParameterTypes = testClassMethod.getGenericParameterTypes();
        Class clazz = (Class) genericParameterTypes[0];//Class
        System.out.println("clazz : " + clazz);//clazz : class java.lang.String

        TypeVariable<Class<B>>[] typeParameters = B.class.getTypeParameters();
        TypeVariable typeVariable = typeParameters[0];
        String name = typeVariable.getName();
        System.out.println("name : " + name);//name : T
        Type[] bounds = typeVariable.getBounds();
        //T的默认上限是Object,则Type是Class
        System.out.println("bounds[0] : " + bounds[0]);//bounds[0] : class java.lang.Object
        Class<? super B> superclass = B.class.getSuperclass();//返回直接继承的父类,不显示泛型参数
        System.out.println("superclass : " + superclass);//superclass : class ClassTest$A
        Type genericSuperclass = B.class.getGenericSuperclass();//返回直接继承的父类 显示泛型参数
        System.out.println("genericSuperclass : " + genericSuperclass);//genericSuperclass : ClassTest$A<T>
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type rawType = parameterizedType.getRawType();//原始类型，即A，也就是插除泛型信息的类型
        System.out.println("rawType : " + rawType);//rawType : class ClassTest$A
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        //类型参数是T
        System.out.println("actualTypeArguments[0] : " + actualTypeArguments[0]);//actualTypeArguments[0] : T
        TypeVariable typeVariable2 = (TypeVariable) actualTypeArguments[0];
        String name2 = typeVariable2.getName();
        System.out.println("name2 : " + name2);//name2 : T
        Type[] bounds2 = typeVariable2.getBounds();
        //T的上限默认是Object
        System.out.println("bounds2[0] : " + bounds2[0]);//bounds2[0] : class java.lang.Object
    }

    class A<V> {

    }

    class B<T> extends A<T>{

    }
}


class Test {

    public void test(List<String>[] list){

    }

    public static void main(String[] args) throws Exception{
        Method testMethod = Test.class.getDeclaredMethod("test", List[].class);
        System.out.println(getRawType(testMethod.getGenericParameterTypes()[0]));
    }

    /**
     * 获取原始类型，也就是不带泛型的，最后都会转成原始类型Class，前提是泛型中必要有原始类型
     * List<? super Object>或List<String> -> List -> Class
     * List<String>[] -> List[] -> Class
     * @param type
     * @return
     */
    static Class<?> getRawType(Type type) {

        if (type instanceof Class<?>) {
            // Type is a normal class.
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;

            // I'm not exactly sure why getRawType() returns Type instead of Class. Neal isn't either but
            // suspects some pathological case related to nested classes exists.
            Type rawType = parameterizedType.getRawType();
            if (!(rawType instanceof Class)) throw new IllegalArgumentException();
            return (Class<?>) rawType;
        }
        if (type instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) type).getGenericComponentType();
            return Array.newInstance(getRawType(componentType), 0).getClass();
        }
        if (type instanceof TypeVariable) {
            // We could use the variable's bounds, but that won't work if there are multiple. Having a raw
            // type that's more general than necessary is okay.
            return Object.class;
        }
        if (type instanceof WildcardType) {
            return getRawType(((WildcardType) type).getUpperBounds()[0]);
        }

        throw new IllegalArgumentException("Expected a Class, ParameterizedType, or "
                + "GenericArrayType, but <" + type + "> is of type " + type.getClass().getName());
    }

}


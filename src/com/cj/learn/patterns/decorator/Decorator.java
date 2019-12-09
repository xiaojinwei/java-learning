package com.cj.learn.patterns.decorator;

/**
 * 装饰者模式
 * 为接口增强性能
 * 通过包装一个装饰对象来扩展其功能，而又不改变其接口，是对原有实现的增强
 * Java IO就是使用的装饰者模式
 */
public class Decorator {
    public static void main(String[] args) {
        Reader reader = new FileReader();
        System.out.println(reader.read());
        reader = new BufferReader(new DatabaseReader());
        System.out.println(reader.read());
    }
}


interface Reader{
    String read();
}

class FileReader implements Reader{

    @Override
    public String read() {
        return "File data";
    }
}

class DatabaseReader implements Reader{

    @Override
    public String read() {
        return "Database data";
    }
}

/**
 * 装饰类
 */
class BufferReader implements Reader{

    protected volatile Reader reader;

    public BufferReader(Reader reader){
        this.reader = reader;
    }

    /**
     * 增强，在原有的基础上增强
     * @return
     */
    @Override
    public String read() {
        return reader.read() + " buffer";
    }

    /**
     * 也可以添加新的方法
     * @return
     */
    public String getBuffer(){
        return "buffer";
    }
}
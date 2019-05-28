package com.cj.learn.tree.rbt;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class RBTMapClient {
    static Random random = new Random();
    static int MAX = 20480;
    public static void main(String[] args) {
        //testPutExample();
        //testRBTRandom();
        //testTreeMapRandom();
        //testRBTIncrement();
        //testTreeMapIncrement();
        //testRemove();
        //testGet();
        //testPut();
        //testPutBlance();
        //testPutDeleteSimple();
        //testRBTIncrementAddRemove();
        //testTreeMapIncrementAddRemove();
        testSize();
    }

    private static void testSize() {
        RBTMap<Integer, String> sim = new RBTMap<>();
        sim.put(1,"1");
        System.out.println(sim.size());
        sim.put(2,"2");
        System.out.println(sim.size());
        sim.remove(1);
        System.out.println(sim.size());
        sim.remove(2);
        System.out.println(sim.size());
        System.out.println("-------------------------------------------");
        //MAX = 2;
        RBTMap<Integer,String> map = new RBTMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(i,random.nextInt(MAX) + "");
        }
        System.out.println(map.size());
        for (int i = 0; i < MAX; i++) {
            map.remove(i);
        }
        System.out.println(map.size());
        map.levelOrder();
        for (int i = 0; i < MAX; i++) {
            map.put(i,random.nextInt(MAX) + "");
        }
        System.out.println(map.size());
        for (int i = 0; i < MAX; i++) {
            map.remove(i);
        }
        System.out.println(map.size());
    }

    private static void testRBTIncrementAddRemove() {
        long start = System.currentTimeMillis();
        RBTMap<Integer,String> map = new RBTMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(i,random.nextInt(MAX) + "");
        }
        //boolean b = map.checkBalance();
        //System.out.println("testAVLRandom testAVLIncrement : " + b);
        for (int i = 0; i < MAX; i++) {
            map.get(random.nextInt(MAX));
        }
        for (int i = 0; i < MAX; i++) {
            map.remove(random.nextInt(MAX));
        }
        long end = System.currentTimeMillis();
        System.out.println("testRBTIncrementAddRemove use time : " + (end - start) + " ms");
    }

    private static void testTreeMapIncrementAddRemove() {
        long start = System.currentTimeMillis();
        TreeMap<Integer,String> map = new TreeMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(i,random.nextInt(MAX) + "");
        }
        for (int i = 0; i < MAX; i++) {
            map.get(random.nextInt(MAX));
        }
        for (int i = 0; i < MAX; i++) {
            map.remove(random.nextInt(MAX));
        }
        long end = System.currentTimeMillis();
        System.out.println("testTreeMapIncrementAddRemove use time : " + (end - start) + " ms");
    }

    private static void testPutDeleteSimple() {
        RBTMap<Integer, String> map1 = new RBTMap<>();
        TreeMap<Integer, String> map2 = new TreeMap<>();
        for (int i = 0; i < 65535; i++) {
            int key = random.nextInt(65535);
            String value = random.nextInt(65535) + "";
            map1.put(key,value);
            map2.put(key,value);
        }
        for (int i = 0;i < 10000;i ++){
            int key = random.nextInt(65535);
            map1.remove(key);
            map2.remove(key);
        }
        System.out.println(map1.size() == map2.size());
        System.out.println(map1.size());
        Iterator<RBTMap.RBNode<Integer, String>> iterator1 = map1.iterator();
        Iterator<Map.Entry<Integer, String>> iterator2 = map2.entrySet().iterator();
        boolean f = true;
        while (iterator1.hasNext() && iterator2.hasNext()) {
            RBTMap.RBNode<Integer, String> next = iterator1.next();
            Map.Entry<Integer, String> next1 = iterator2.next();
            boolean equals = next.getKey().equals(next1.getKey());
            boolean color = getColor(next1);
            boolean equals2 = next.isColor()==color;
            if (!equals || !equals2) {
                f = false;
            }
        }
        System.out.println(f);
        System.out.println(!iterator1.hasNext() && !iterator2.hasNext());
    }

    private static void testPutBlance(){
        RBTMap<Integer,String> map = new RBTMap<>();
        map.put(4,"4");
        map.put(6,"6");
        map.put(2,"2");
        map.put(7,"7");
        map.put(3,"3");
        map.put(33,"33");
        map.put(22,"22");
        map.put(1,"1");
        map.remove(33);
        map.remove(44);
        map.levelOrder();
    }

    private static void testPutExample(){
        RBTMap<Integer,String> map = new RBTMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(i,"abc" + i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(map.get(random.nextInt(100)));
        }
    }

    private static void testRBTRandom() {
        long start = System.currentTimeMillis();
        RBTMap<Integer,String> map = new RBTMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(random.nextInt(MAX),String.valueOf(random.nextInt(MAX)));
        }
        //boolean b = map.checkBalance();
        //System.out.println("testAVLRandom checkBalance : " + b);
        for (int i = 0; i < MAX; i++) {
            map.get(random.nextInt(MAX));
        }
        long end = System.currentTimeMillis();
        System.out.println("testRBTRandom use time : " + (end - start) + " ms");
    }

    private static void testTreeMapRandom() {
        long start = System.currentTimeMillis();
        TreeMap<Integer,String> map = new TreeMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(random.nextInt(MAX),random.nextInt(MAX) + "");
        }
        for (int i = 0; i < MAX; i++) {
            map.get(random.nextInt(MAX));
        }
        long end = System.currentTimeMillis();
        System.out.println("testTreeMapRandom use time : " + (end - start) + " ms");
    }

    private static void testRBTIncrement() {
        long start = System.currentTimeMillis();
        RBTMap<Integer,String> map = new RBTMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(i,random.nextInt(MAX) + "");
        }
        //boolean b = map.checkBalance();
        //System.out.println("testAVLRandom testAVLIncrement : " + b);
        for (int i = 0; i < MAX; i++) {
            map.get(random.nextInt(MAX));
        }
        long end = System.currentTimeMillis();
        System.out.println("testRBTIncrement use time : " + (end - start) + " ms");
    }

    private static void testTreeMapIncrement() {
        long start = System.currentTimeMillis();
        TreeMap<Integer,String> map = new TreeMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(i,random.nextInt(MAX) + "");
        }
        for (int i = 0; i < MAX; i++) {
            map.get(random.nextInt(MAX));
        }
        long end = System.currentTimeMillis();
        System.out.println("testTreeMapIncrement use time : " + (end - start) + " ms");
    }

    private static void testRemove() {
        RBTMap<Integer,String> map = new RBTMap<>();
        int[] array = {5,2,6,1,4,7,3};
        for (int i : array) {
            map.put(i,String.valueOf(i));
        }
        map.levelOrder();
        System.out.println(map.remove(5));
        map.levelOrder();
        Iterator<RBTMap.RBNode<Integer, String>> iterator = map.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next().getValue());
        }
        System.out.println();
    }

    public static void testGet() {
        RBTMap<Integer,String> map = new RBTMap<>();
        map.put(2,"a");
        map.put(1,"b");
        map.put(2,"c");
        map.put(4,"d");
        map.put(5,"e");
        System.out.println(map.get(2));
        System.out.println(map.get(5));
        System.out.println(map.get(9));
    }

    public static void testPut() {
        RBTMap<Integer, String> map1 = new RBTMap<>();
        TreeMap<Integer, String> map2 = new TreeMap<>();
        for (int i = 0; i < 65535; i++) {
            int key = random.nextInt(65535);
            String value = random.nextInt(65535) + "";
            map1.put(key,value);
            map2.put(key,value);
        }
        System.out.println(map1.size() == map2.size());
        System.out.println(map1.size());
        Iterator<RBTMap.RBNode<Integer, String>> iterator1 = map1.iterator();
        Iterator<Map.Entry<Integer, String>> iterator2 = map2.entrySet().iterator();
        boolean f = true;
        while (iterator1.hasNext() && iterator2.hasNext()) {
            RBTMap.RBNode<Integer, String> next = iterator1.next();
            Map.Entry<Integer, String> next1 = iterator2.next();
            boolean equals = next.getKey().equals(next1.getKey());
            boolean color = getColor(next1);
            boolean equals2 = next.isColor()==color;//和JDK TreeMap的结点颜色可能不一样
            if (!equals /*|| !equals2*/) {
                f = false;
            }
        }
        System.out.println(f);
        System.out.println(!iterator1.hasNext() && !iterator2.hasNext());
    }

    public static boolean getColor(Map.Entry<Integer, String> entry) {
        try {
            Class<? extends Map.Entry> entryClass = entry.getClass();
            Field colorField = entryClass.getDeclaredField("color");
            colorField.setAccessible(true);
            return (boolean) colorField.get(entry);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

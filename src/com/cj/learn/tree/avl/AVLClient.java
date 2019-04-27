package com.cj.learn.tree.avl;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class AVLClient {

    static Random random = new Random();
    static int MAX = 20480;
    public static void main(String[] args) {
        //testGet();
        //testRemove();
        //testPut();
        //testPutExample();
        testAVLRandom();
        testTreeMapRandom();
        testAVLIncrement();
        testTreeMapIncrement();
    }

    private static void testPutExample(){
        AVLMap<Integer,String> map = new AVLMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(i,"abc" + i);
        }
        for (int i = 0; i < 10; i++) {
            System.out.println(map.get(random.nextInt(100)));
        }
    }

    private static void testAVLRandom() {
        long start = System.currentTimeMillis();
        AVLMap<Integer,String> map = new AVLMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(random.nextInt(MAX),String.valueOf(random.nextInt(MAX)));
        }
        boolean b = map.checkBalance();
        System.out.println("testAVLRandom checkBalance : " + b);
        for (int i = 0; i < MAX; i++) {
            map.get(random.nextInt(MAX));
        }
        long end = System.currentTimeMillis();
        System.out.println("testAVLRandom use time : " + (end - start) + " ms");
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

    private static void testAVLIncrement() {
        long start = System.currentTimeMillis();
        AVLMap<Integer,String> map = new AVLMap<>();
        for (int i = 0; i < MAX; i++) {
            map.put(i,random.nextInt(MAX) + "");
        }
        boolean b = map.checkBalance();
        System.out.println("testAVLRandom testAVLIncrement : " + b);
        for (int i = 0; i < MAX; i++) {
            map.get(random.nextInt(MAX));
        }
        long end = System.currentTimeMillis();
        System.out.println("testAVLIncrement use time : " + (end - start) + " ms");
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
        AVLMap<Integer,String> map = new AVLMap<>();
        int[] array = {5,2,6,1,4,7,3};
        for (int i : array) {
            map.put(i,String.valueOf(i));
        }
        map.levelOrder();
        System.out.println(map.remove(5));
        map.levelOrder();
        Iterator<AVLMap.AVLEntry<Integer, String>> iterator = map.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next().getValue());
        }
        System.out.println();
    }

    public static void testGet() {
        AVLMap<Integer,String> map = new AVLMap<>();
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
        AVLMap<Integer, String> map1 = new AVLMap<>();
        TreeMap<Integer, String> map2 = new TreeMap<>();
        for (int i = 0; i < 65; i++) {
            int key = random.nextInt(65535);
            String value = random.nextInt(65535) + "";
            map1.put(key,value);
            map2.put(key,value);
        }
        System.out.println(map1.size() == map2.size());
        System.out.println(map1.size());
        Iterator<AVLMap.AVLEntry<Integer, String>> iterator1 = map1.iterator();
        Iterator<Map.Entry<Integer, String>> iterator2 = map2.entrySet().iterator();
        boolean f = true;
        while (iterator1.hasNext() && iterator2.hasNext()) {
            boolean equals = iterator1.next().getKey().equals(iterator2.next().getKey());
            if (!equals) {
                f = false;
            }
        }
        System.out.println(f);
        System.out.println(!iterator1.hasNext() && !iterator2.hasNext());
    }

}

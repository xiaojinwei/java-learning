package com.cj.learn.tree.avl;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class AVLClient {

    static Random random = new Random();
    public static void main(String[] args) {
        //testGet();
        testRemove();
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
        for (int i = 0; i < 65535; i++) {
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

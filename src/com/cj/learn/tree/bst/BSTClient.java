package com.cj.learn.tree.bst;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class BSTClient {
    public static void main(String[] args) {
        BSTMap<Integer,String> map = new BSTMap<>();
        map.put(4,"4");
        map.put(2,"2");
        map.put(1,"1");
        map.put(8,"8");
        Set<Map.Entry<Integer, String>> entries = map.entrySet();
        for (Map.Entry<Integer, String> entry : entries) {
            System.out.print(entry.getKey() + " ");
        }

        for (Map.Entry<Integer, String> entry : entries) {
            System.out.print(entry.getKey() + " ");
        }
        System.out.println();

        Iterator<Map.Entry<Integer, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
    }
}

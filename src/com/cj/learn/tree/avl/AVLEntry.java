package com.cj.learn.tree.avl;

import java.util.Map;

public class AVLEntry<K,V> implements Map.Entry<K,V> {
    private K key;
    private V value;
    public AVLEntry<K,V> left;
    public AVLEntry<K,V> right;

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public V setValue(V value) {
        this.value = value;
        return value;
    }

    public AVLEntry(K key) {
        this.key = key;
    }

    public AVLEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public AVLEntry(K key, V value, AVLEntry<K, V> left, AVLEntry<K, V> right) {
        this.key = key;
        this.value = value;
        this.left = left;
        this.right = right;
    }
}

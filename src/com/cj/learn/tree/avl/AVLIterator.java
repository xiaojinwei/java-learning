package com.cj.learn.tree.avl;

import com.cj.learn.tree.binary.TreeNode;

import java.util.Iterator;
import java.util.Stack;

/**
 * 中序遍历实现迭代器
 * 这里实现引入Stack，而非递归实现
 * @param <K>
 * @param <V>
 */
public class AVLIterator<K,V> implements Iterator<AVLEntry<K,V>> {
    private Stack<AVLEntry<K,V>> stack;
    public AVLIterator(AVLEntry<K, V> root) {
        stack = new Stack<>();
        addLeftPath(root);
    }

    private void addLeftPath(AVLEntry<K, V> p) {
        while (p != null) {
            stack.push(p);
            p = p.left;
        }
    }

    @Override
    public boolean hasNext() {
        return !stack.empty();
    }

    @Override
    public AVLEntry<K, V> next() {
        AVLEntry<K, V> entry = stack.pop();
        addLeftPath(entry.right);
        return entry;
    }
}

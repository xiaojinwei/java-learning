package com.cj.learn.tree.binary;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 递归实现iterator
 * 空间复杂度是O(n)
 */
public class BSTIterator {
    private Iterator<Integer> itr;

    public BSTIterator(TreeNode root) {
        //这个算法的空间复杂度是O(n)
        ArrayList<Integer> list = new ArrayList<>();
        inOrder(root,list);
        itr = list.iterator();
    }

    /**
     * 中序遍历将每个结点添加进集合
     * @param p
     * @param list
     */
    private void inOrder(TreeNode p,ArrayList<Integer> list) {
        if (p != null) {
            inOrder(p.left,list);
            list.add(p.val);
            inOrder(p.right,list);
        }
    }

    public boolean hasNext() {
        return itr.hasNext();
    }

    public int next() {
        return itr.next();
    }
}

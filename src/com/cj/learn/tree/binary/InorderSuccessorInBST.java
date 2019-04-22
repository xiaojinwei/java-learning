package com.cj.learn.tree.binary;

import sun.reflect.generics.tree.Tree;

public class InorderSuccessorInBST {
    /**
     * 根据root根结点，寻找p的后继结点
     * @param root
     * @param p
     * @return
     */
    public TreeNode inorderSuccessor(TreeNode root,TreeNode p) {
        if(root == null) return null;//1.如果root为null，直接返回null
        if(getLastEntry(root) == p) return null;//2.如果最大结点等于p结点，说明没有后继结点，直接返回null
        if(p.right != null) return getFirstEntry(p.right);//3.如果p有右孩子，直接返回以右孩子为根节点的最小结点
        //4.如果右孩子为空，可以有两种解法，一种是自顶向下，一种是自底向上
        //自顶向下：(需要用到while循环)找到的是最后一个父亲，也就是后续结点
        //自底向上：(需要用到栈Stack)找到的是第一个左孩子的父亲，
        //以下使用的是自顶向下的解法
        TreeNode parent = root;
        TreeNode tmp = root;
        while (parent != null) {
            if(parent == p) break;
            else if(p.val < parent.val){//小于说明在左孩子树上，说明parent有可能是p的后继节点，只要该parent是最后一个找到的(p.val < parent.val)的parent
                tmp = parent;
                parent = parent.left;
            }else //大于说明在右孩子树上
                parent = parent.right;
        }
        return tmp;
    }

    /**
     * 找到以p为根节点的最大结点
     * @param p
     * @return
     */
    private TreeNode getLastEntry(TreeNode p){
        while (p.right != null)
            p = p.right;
        return p;
    }

    /**
     * 找到以p为根节点的最小结点
     * @param p
     * @return
     */
    private TreeNode getFirstEntry(TreeNode p){
        while (p.left != null)
            p = p.left;
        return p;
    }
}

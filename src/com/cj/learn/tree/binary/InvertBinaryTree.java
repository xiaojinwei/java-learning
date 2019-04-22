package com.cj.learn.tree.binary;

import java.util.LinkedList;
import java.util.Queue;

public class InvertBinaryTree {
    /**
     *  Invert Binary Tree
     *  //先序遍历(根左右)
     *  先序遍历先从二叉树的根开始，然后到左子树，再到右子树
     * @param root
     * @return
     */
    public TreeNode invertTree1(TreeNode root){
        if (root != null) {
            TreeNode t = root.left;
            root.left = root.right;
            root.right = t;
            invertTree1(root.left);
            invertTree1(root.right);
            return root;
        }else {
            return null;
        }
    }

    /**
     *  Invert Binary Tree
     *  //后序遍历(左右根)
     *  后序遍历先从左子树开始，然后到右子树，再到根
     * @param root
     * @return
     */
    public TreeNode invertTree2(TreeNode root){
        if (root != null) {
            invertTree2(root.left);
            invertTree2(root.right);
            TreeNode t = root.left;
            root.left = root.right;
            root.right = t;
            return root;
        }else {
            return null;
        }
    }

    /**
     *  Invert Binary Tree
     *  //中序遍历(左根右)
     *  中序遍历先从左子树开始，然后到根，再到右子树
     * @param root
     * @return
     */
    public TreeNode invertTree3(TreeNode root){
        if (root != null) {
            invertTree3(root.left);
            TreeNode t = root.left;
            root.left = root.right;
            root.right = t;
            invertTree3(root.left);//root.left是原来的root.right
            return root;
        }else {
            return null;
        }
    }

    /**
     * Invert Binary Tree
     * 层序遍历 (一层一层遍历)
     *
     * @param root
     * @return
     */
    public TreeNode invertTree4(TreeNode root) {
        if (root == null) {
            return null;
        }else{
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            while (!queue.isEmpty()){
                TreeNode node = queue.poll();
                TreeNode t = node.left;
                node.left = node.right;
                node.right = t;
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            return root;
         }
    }
}

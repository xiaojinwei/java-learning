package com.cj.learn.tree.avl;

import com.cj.learn.tree.binary.TreeNode;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * 108(https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/)
 * 给定排序数组，将它转化为平衡二叉树
 * 要求左右子树高度差的绝对值不超过1(性质2)
 *
 * 实现方式1
 * AVLMap的put实现方式
 *
 * 实现方式2
 * 递归构建AVL + BST
 * 参考TreeMap中的buildFromSorted
 * 通过排序集合的迭代器it构建新的TreeMap,O(N)
 *
 */
public class ConvertSortedArrayToBinarySearchTree {

    //实现方式1
    //*****************************************************************************************************************************
    class LeetCodeAVL{
        private int size;
        private TreeNode root;
        private LinkedList<TreeNode> stack = new LinkedList<>();

        public LeetCodeAVL() {
        }

        public int size() {
            return this.size;
        }

        public boolean isEmpty() {
            return this.size == 0;
        }

        public void put(int key) {
            if (root == null) {
                root = new TreeNode(key);
                stack.push(root);//需要将put走的路径全部压栈，为了fixAfterInsertion实现平衡
                size ++;
            }else{
                TreeNode p = root;
                while (p != null) {
                    stack.push(p);//需要将put走的路径全部压栈，为了fixAfterInsertion实现平衡
                    int cmp = key - p.val;
                    if (cmp < 0) {
                        if (p.left == null) {
                            p.left = new TreeNode(key);
                            size ++;
                            stack.push(p.left);//需要将put走的路径全部压栈，为了fixAfterInsertion实现平衡
                            break;
                        }else{
                            p = p.left;//再次循环比较
                        }
                    } else if (cmp > 0) {
                        if (p.right == null) {
                            p.right = new TreeNode(key);
                            size ++;
                            stack.push(p.right);//需要将put走的路径全部压栈，为了fixAfterInsertion实现平衡
                            break;
                        }else{
                            p = p.right;
                        }
                    }else{
                        break;
                    }
                }
            }
            fixAfterInsertion(key);
        }

        private HashMap<TreeNode,Integer> heightMap = new HashMap<>();

        /**
         * 返回一个结点的高度
         */
        public int getHeight(TreeNode p) {
            return heightMap.containsKey(p) ? heightMap.get(p):0;
        }


        /**
         * 右旋(单旋转)
         * 该方法需要右返回值，因为AVLEntry中没有parent指针(JDK中是有parent指针的，所以不需要有返回值)，旋转之后它的新的根节点需要返回
         * @param p
         * @return
         */
        private TreeNode rotateRight(TreeNode p) {
            TreeNode left = p.left;
            p.left = left.right;
            left.right = p;
            heightMap.put(p,Math.max(getHeight(p.left),getHeight(p.right)) + 1);
            heightMap.put(left,Math.max(getHeight(left.left),getHeight(p)) + 1);
            return left;//新的根节点
        }

        /**
         * 左旋(单旋转)
         * @param p
         * @return
         */
        private TreeNode rotateLeft(TreeNode p) {
            TreeNode right = p.right;
            p.right = right.left;
            right.left = p;
            heightMap.put(p,Math.max(getHeight(p.left),getHeight(p.right)) + 1);
            heightMap.put(right,Math.max(getHeight(p),getHeight(right.right)) + 1);
            return right;//新的根节点
        }

        /**
         * 先左旋再右旋
         * 先将p.left进行左旋，再将p进行右旋
         * @param p
         * @return
         */
        private TreeNode firstLeftThenRight(TreeNode p) {
            p.left = rotateLeft(p.left);
            p = rotateRight(p);
            return p;
        }

        /**
         * 先右旋再左旋
         * 先将p.right进行右旋，再将p进行左旋
         * @param p
         * @return
         */
        private TreeNode firstRightThenLeft(TreeNode p) {
            p.right = rotateRight(p.right);
            p = rotateLeft(p);
            return p;
        }

        /**
         * 插入调整，使其二叉搜索树达到平衡
         * @param key
         */
        private void fixAfterInsertion(int key){
            TreeNode p = root;
            while (!stack.isEmpty()) {
                p = stack.pop();//插入所走的路径不断弹栈
                int newHeight = Math.max(getHeight(p.left),getHeight(p.right)) + 1;
                if (heightMap.containsKey(p) && getHeight(p) > 1 /*保证p不是叶子节点*/ && newHeight == getHeight(p)/*高度没有改变*/) {
                    stack.clear();
                    return;
                }
                heightMap.put(p,newHeight);//Math.max(getHeight(p.left),getHeight(p.right)) + 1;
                int d = getHeight(p.left) - getHeight(p.right);//计算平衡因子
                if (Math.abs(d) <= 1) { //改树平衡无需调整(旋转)
                    continue;
                }else{
                    if (d == 2) {
                        if (key - p.left.val < 0) { //插入到了左子树的左子树
                            p = rotateRight(p);//单旋转：右旋rotateRight
                        }else{//插入到了左子树的右子树
                            p = firstLeftThenRight(p); //双旋转：先左后右firstLeftThenRight
                        }
                    }else{ //d == -2
                        if (key - p.right.val > 0) { //插入到了右子树的右子树
                            p = rotateLeft(p);//单旋转：左旋rotateLeft
                        }else{//插入到了右子树的左子树
                            p = firstRightThenLeft(p);//双旋转：先右后左firstRightThenLeft
                        }
                    }
                    //旋转过后，需要判断走的是左子树还是右子树，也就是检测爷爷结点，也就是p.parent要设置左子树还是右子树
                    if (!stack.isEmpty()) {
                        if (key - stack.peek().val < 0) { //表明插入到了左子树
                            stack.peek().left = p;
                        }else{
                            stack.peek().right = p;
                        }
                    }
                }
            }
            root = p;//重新设置根节点
        }
    }

    public TreeNode sortedArrayToBST_useingAVL(int[] nums){
        if (nums == null || nums.length == 0) { //边界检测
            return null;
        }
        LeetCodeAVL avl = new LeetCodeAVL();
        for (int num : nums) {
            avl.put(num);
        }
        return avl.root;
    }


    //实现方式2
    //*****************************************************************************************************************
    /**
     * 模仿TreeMap中的buildFromSorted
     * 时间复杂度O(N)
     * 空间复杂度O(logN)
     * @param nums
     * @return
     */
    public TreeNode sortedArrayToBST(int[] nums){
        if (nums == null || nums.length == 0) {
            return null;
        }
        return buildFromSorted(0,nums.length - 1,nums);
    }

    private TreeNode buildFromSorted(int lo, int hi, int[] nums) {
        if (hi < lo) {
            return null;
        }
        int mid = (lo + hi) / 2;
        TreeNode left = null;
        if (lo < mid) {
            left = buildFromSorted(lo, mid - 1, nums);
        }
        TreeNode middle = new TreeNode(nums[mid]);
        if (left != null) {
            middle.left = left;
        }
        if (mid < hi) {
            TreeNode right = buildFromSorted(mid + 1, hi, nums);
            middle.right = right;
        }
        return middle;
    }
}

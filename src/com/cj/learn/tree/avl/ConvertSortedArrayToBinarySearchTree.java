package com.cj.learn.tree.avl;

import com.cj.learn.tree.binary.TreeNode;

/**
 * 108(https://leetcode.com/problems/convert-sorted-array-to-binary-search-tree/)
 * 给定排序数组，将它转化为平衡二叉树
 * 要求左右子树高度差的绝对值不超过1(性质2)
 *
 * 递归构建AVL + BST
 * 参考TreeMap中的buildFromSorted
 * 通过排序集合的迭代器it构建新的TreeMap,O(N)
 *
 */
public class ConvertSortedArrayToBinarySearchTree {
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

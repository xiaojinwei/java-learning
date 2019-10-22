package com.cj.learn.alg.sort;

/***
 * 快速排序
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] arr = {10,8,78,43,63,31,56,11,9,45,22};
        sort(arr,0,arr.length - 1);
        print(arr);
    }

    public static void sort(int[] arr,int left,int right){
        if(left <= right){
            int index = partition(arr, left, right);
            sort(arr,left,index - 1);
            sort(arr,index + 1,right);
        }
    }

    /**
     * 将数组以base基准值进行分区，小于基准值的在左边，大于等于基准值的在右边
     * @param arr 数组
     * @param left 数组排序的区域左边索引
     * @param right 数组排序的区域右边索引
     * @return 返回分区后的临界索引值
     */
    public static int partition(int[] arr,int left,int right){
        int base = arr[left];
        while (left < right){
            while (left < right && arr[right] > base){
                right --;
            }
            arr[left] = arr[right];
            while (left < right && arr[left] <= base){
                left ++;
            }
            arr[right] = arr[left];
        }
        arr[left] = base;
        return left;
    }

    private static void print(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}

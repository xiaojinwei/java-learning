package com.cj.learn.alg.sort;

/***
 * 插入排序
 */
public class InsertSort {
    public static void main(String[] args) {
        int[] arr = {10,8,78,43,63,31,56,11,9,45,22};
        sort(arr);
        print(arr);
    }

    public static void sort(int[] arr){
        int current;
        for (int i = 0; i < arr.length - 1; i++) {
            current = arr[i + 1];
            int index = i;
            while (index >= 0 && current < arr[index]) {
                arr[index + 1] = arr[index];
                index --;
            }
            arr[index + 1] = current;
        }
    }

    private static void print(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}

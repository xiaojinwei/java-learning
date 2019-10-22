package com.cj.learn.alg.sort;

/***
 * 选择排序
 */
public class SelectSort {
    public static void main(String[] args) {
        int[] arr = {10,8,78,43,63,31,56,11,9,45,22};
        sort(arr);
        print(arr);
    }

    public static void sort(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            int index = i;
            for (int j = i;j<arr.length;j++){
                if(arr[j] < arr[index]){
                    index = j;
                }
            }
            if(index != i){
                int t = arr[i];
                arr[i] = arr[index];
                arr[index] = t;
            }
        }
    }

    private static void print(int[] arr){
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }
    }
}

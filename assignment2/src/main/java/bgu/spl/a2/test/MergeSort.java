/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class MergeSort extends Task<int[]> {

    private final int[] array;

    public MergeSort(int[] array) {
        this.array = array;
    }

    @Override
    protected void start() {
        if (array.length == 1) {
            complete(array);
        } else if (array.length == 2) {
            int x = array[0];
            int y = array[1];
            if (x > y) {
                array[0] = y;
                array[1] = x;
            }
            complete(array);
        } else {
            int[] leftArr = new int[array.length / 2 + array.length % 2];
            int[] rightArr = new int[array.length / 2];
            for (int i = 0; i < rightArr.length; i++) {
                leftArr[i] = array[i];
                rightArr[i] = array[leftArr.length + i];
            }
            if (array.length % 2 == 1)
                leftArr[leftArr.length - 1] = array[array.length / 2];
            List<Task<int[]>> subTasks = new ArrayList<>();
            Task<int[]>[] spawnedTasks = new Task[2];
            spawnedTasks[0] = new MergeSort(leftArr);
            spawnedTasks[1] = new MergeSort(rightArr);
            subTasks.add(spawnedTasks[0]);
            subTasks.add(spawnedTasks[1]);
            spawn(spawnedTasks);
            whenResolved(subTasks, () -> {
                int i = 0;
                int j = 0;

                int[] left = subTasks.get(0).getResult().get();
                //System.out.println(Arrays.toString(subTasks.get(0).getResult().get()));
                int[] right = subTasks.get(1).getResult().get();
                //System.out.println(Arrays.toString(subTasks.get(1).getResult().get()));
                int[] merged = new int[left.length + right.length];
                while (i + j < merged.length) {
                    if (i == left.length) {
                        while (i + j < merged.length) {
                            merged[i + j] = right[j];
                            j++;
                        }
                        break;
                    }
                    if (j == right.length) {
                        while (i + j < merged.length) {
                            merged[i + j] = left[i];
                            i++;
                        }
                        break;
                    }
                    if (left[i] < right[j]) {
                        merged[i + j] = left[i];
                        i++;
                    } else {
                        merged[i + j] = right[j];
                        j++;
                    }
                }
                complete(merged);
        });
    }

}

    public static void main(String[] args) throws InterruptedException {
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        //int n = 500000; //you may check on different number of elements if you like
        //int[] array = new Random().ints(n).toArray();
        //int[] array = {10, 6, 4, -1, 7 , 14, 3, -4, 13, -8, 2, -9, -2, 20 ,24, 25};
        int[] array = {10, 6, 4, -1, 7, 14, 3, -4};
        //int[] array = {10, 6, 4, -1};
        MergeSort task = new MergeSort(array);
        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(task);
        //System.out.println(Arrays.toString(array));
        task.getResult().whenResolved(() -> {
            //warning - a large print!! - you can remove this line if you wish
            //System.out.println(Arrays.toString(task.getResult().get()));
            System.out.println("done");

            l.countDown();
        });
        l.await();
        pool.shutdown();
    }

}
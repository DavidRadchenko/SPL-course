package bgu.spl.a2.test;

import bgu.spl.a2.Task;
import bgu.spl.a2.WorkStealingThreadPool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class SumMatrix extends Task<int[]> {
    private int[][] array;

    public SumMatrix(int[][] array) {
        this.array = array;
    }

    protected void start() {
        int sum = 0;
        List<Task<Integer>> tasks = new ArrayList<>();
        int rows = array.length;
        for (int i = 0; i < rows; i++) {
            SumRow newTask = new SumRow(array, i);
            spawn(newTask);
            tasks.add(newTask);
        }
        whenResolved(tasks, () -> {
                    int[] res = new int[rows];
                    for (int j = 0; j < rows; j++) {
                        System.out.println(tasks.get(j).getResult().get());
                        res[j] = tasks.get(j).getResult().get();
                    }
                    complete(res);
                }
        );
    }

    public static void main(String[] args) throws InterruptedException {
        WorkStealingThreadPool pool = new WorkStealingThreadPool(4);
        int[][] array = new int[5][10];
        for (int i = 0; i < 5; i++)
            for (int j = 0; j < 10; j++) {
                array[i][j] = 2;
            }
        SumMatrix myTask = new SumMatrix(array);
        CountDownLatch l = new CountDownLatch(1);
        pool.start();
        pool.submit(myTask);
        myTask.getResult().whenResolved(() -> {
            //warning - a large print!! - you can remove this line if you wish
            System.out.println(Arrays.toString(myTask.getResult().get()));
            l.countDown();
        });
        l.await();
        pool.shutdown();
    }
}
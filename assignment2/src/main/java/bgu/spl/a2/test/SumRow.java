package bgu.spl.a2.test;

import bgu.spl.a2.Task;

/**
 * Created by david on 12/25/16.
 */
public class SumRow extends Task<Integer> {

    private int[][] array;
    private int r;
    public SumRow(int[][] array,int r) {
        this.array = array;
        this.r=r;
    }

    protected void start(){
        int sum=0;
        for(int j=0 ;j<array[0].length;j++)
            sum+=array[r][j];
        complete(sum);
    }
}
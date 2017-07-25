package bgu.spl.a2.sim.tools;
import java.util.Random;
import bgu.spl.a2.sim.Product;

public class RandomSumPliers implements Tool  {
    public String getType(){
    return "RandomSumPliers";
    }
    public RandomSumPliers(){}
    /** Tool use method
     * @param p - Product to use tool on
     * @return a long describing the result of tool use on Product package
     */
    public long useOn(Product p){
        long res = 0;
        for(Product part: p.getParts())
            res += Math.abs(func(part.getFinalId()));

        return res;
    }

    public long func(long id){
        Random r = new Random(id);
        long  sum = 0;
        for (long i = 0; i < id % 10000; i++) {
            sum += r.nextInt();
        }

        return sum;
    }
}

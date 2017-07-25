package bgu.spl.a2.sim.tools;

import bgu.spl.a2.sim.Product;

public class NextPrimeHammer implements Tool  {
    public String getType(){
        return "NextPrimeHammer";
    }


    public NextPrimeHammer(){}
    /** Tool use method
     * @param p - Product to use tool on
     * @return a long describing the result of tool use on Product package
     */
    public long useOn(Product p){
        long res = 0;
        for(Product part: p.getParts()) {
            res += Math.abs(func(part.getFinalId()));
            //part.setTemporeryId(func(part.getTemporeryId()));
           // System.out.println("product " + p.getName() + " has " + p.getParts().size() + " parts");
           // System.out.println("Using tool NextPrimeHammer on " + part.getName() + " with res " + res);
        }
        return res;
    }
    private boolean isPrime(long value) {
        if(value < 2) return false;
        if(value == 2) return true;
        long sq = (long) Math.sqrt(value);
        for (long i = 2; i <= sq; i++)
            if (value % i == 0)
                return false;
        return true;
    }

    public long func(long id) {

        long v =id + 1;
        while (!isPrime(v)) {
            v++;
        }
        return v;
    }

}

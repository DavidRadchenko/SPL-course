package bgu.spl.a2.sim.tools;
import java.math.BigInteger;
import bgu.spl.a2.sim.Product;


public class GcdScrewDriver implements Tool {
    public String getType(){
        return "GcdScrewDriver";
    }

    public GcdScrewDriver(){}

    /** Tool use method
     * @param p - Product to use tool on
     * @return a long describing the result of tool use on Product package
     */
    public long useOn(Product p){
        long res = 0;
        for(Product part: p.getParts()) {
            res += Math.abs(func(part.getFinalId()));
            //System.out.println("product " + p.getName() + " has " + p.getParts().size() + " parts");

            //part.setTemporeryId(func(part.getTemporeryId()));
            //System.out.println("Using tool GcdScrewDriver on " + part.getName() + " with res " + res);
        }
        return res;
    }

    public long func(long id){
        BigInteger b1 = BigInteger.valueOf(id);
        BigInteger b2 = BigInteger.valueOf(reverse(id));
        long value= (b1.gcd(b2)).longValue();
        return value;
    }
    public long reverse(long n){
        long reverse=0;
        while( n != 0 ){
            reverse = reverse * 10;
            reverse = reverse + n % 10;
            n = n/10;
        }
        return reverse;
    }
}

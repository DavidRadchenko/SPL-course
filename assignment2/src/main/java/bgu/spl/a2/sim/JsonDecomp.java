package bgu.spl.a2.sim;

import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;

/**
 * Created by eitan on 12/31/16.
 */
public class JsonDecomp {

    private int threads;
    private ToolsAmount[] tools;
    private ManufactoringPlan[] plans;
    private ProdDiscription[][] waves;

    public int getThreadsDec() {
        return threads;
    }

    public ToolsAmount[] getTools() {
        return tools;
    }

    public ManufactoringPlan[] getPlans() {
        return plans;
    }

    public ProdDiscription[][] getWaves() {
        return waves;
    }

    public Wave[] getArrayOfWaves() {
        Wave[] Manywaves = new Wave[waves.length];
        for (int i = 0; i < Manywaves.length; i++) {
            Manywaves[i] = new Wave();
            Manywaves[i].prodDiscr = waves[i];
        }
        return Manywaves;
    }


    class ProdDiscription {
        private String product;
        private int qty;
        private long startId;
    }

    public class Wave {
        private ProdDiscription[] prodDiscr;

        public int getWaveSize() {
            return prodDiscr.length;
        }

        public String getProductName(int t) {
            return prodDiscr[t].product;
        }

        public int getProductAmount(int t) {
            return prodDiscr[t].qty;
        }

        public long getStartId(int t) {
            return prodDiscr[t].startId;
        }

        public int getProductTypes() {
            return prodDiscr.length;
        }

    }


    class ToolsAmount {
        private String tool;
        private int qty;

        /*package*/ int getAmount() {
            return qty;
        }

        /*package*/ Tool getTool() {
            if (tool.equals("rs-pliers"))
                return new RandomSumPliers();
            if (tool.equals("gs-driver"))
                return new GcdScrewDriver();
            else
                return new NextPrimeHammer();

        }

    }

}

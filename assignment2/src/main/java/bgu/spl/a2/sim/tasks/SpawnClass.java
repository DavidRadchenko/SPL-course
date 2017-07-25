package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Task;
import bgu.spl.a2.sim.JsonDecomp;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by eitan on 1/1/17.
 */
public class SpawnClass extends Task<ConcurrentLinkedQueue<Product>> {

    private Warehouse warehouse;
    private JsonDecomp.Wave wave;
    private ConcurrentLinkedQueue<Product> res;


    public SpawnClass(JsonDecomp.Wave wave, Warehouse warehouse) {
        this.warehouse = warehouse;
        this.wave = wave;
        this.res = new ConcurrentLinkedQueue<Product>();
    }

    public void start() {
        int numOfProducts = wave.getWaveSize();
        List<Task<Product>> tasks = new ArrayList<>();

        // since a single wave can have multipulr products in it we must separate its execution
        // such that first we will complete each block before the next as requested in the assignment
        for (int i = 0; i < numOfProducts; i++) {
            for (int k = 0; k < wave.getProductAmount(i); k++) {

                ManufactoringPlan manufactoringPlan = warehouse.getPlan(wave.getProductName(i));
                long startId = wave.getStartId(i) + k;

                // we split our task of creating multiple products of the same type
                // into creating a single product many times..
                SingleProd singleP = new SingleProd(startId, warehouse, manufactoringPlan);
                tasks.add(singleP);

            }
            // we add to the pool all those new tasks we created
            spawn(tasks.toArray(new Task[tasks.size()]));
            whenResolved(tasks, () -> {
                for (Task<Product> p: tasks)
                    // we add the computed product to our queue
                    res.add(p.getResult().get());
                complete(res);
            });
        }
    }
}


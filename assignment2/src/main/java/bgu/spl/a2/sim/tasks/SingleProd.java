package bgu.spl.a2.sim.tasks;

import bgu.spl.a2.Deferred;
import bgu.spl.a2.Task;
import bgu.spl.a2.sim.Product;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tools.Tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by david on 1/1/17.
 */
public class SingleProd extends Task<Product> {

    private Warehouse warehouse;
    private ManufactoringPlan plan;
    private long res;

    public SingleProd(long startId, Warehouse warehouse, ManufactoringPlan plan) {
        this.warehouse = warehouse;
        this.plan = plan;
        this.res = startId;
    }

    public void start() {

        Product product = new Product(res, plan.getProductName());
        String[] parts = plan.getParts();

        List<Task<Product>> subTasks = new ArrayList<>();

        // since each product is composed from other products we must first
        // complete the the calculation of all sub products aka parts
        for (int i = 0; i < parts.length; i++) {
            Task<Product> tmp = new SingleProd(res + 1, warehouse, warehouse.getPlan(parts[i]));
            subTasks.add(tmp);
            spawn(tmp);
        }

        // when all subTasks are complete thus we have the calculated all finalIDs
        // of our product's parts we calculte the finalID of our product via useON.
        // We must take into consideration that our tools are of type Defferd,
        // meaning we can use them only if they are resolved, or send a callback which will be executed upon
        // when a time in which they are resolved
        whenResolved(subTasks, () -> {
            for(Task<Product> part: subTasks)
                if(part.getResult().isResolved())
                    product.addPart(part.getResult().get());

            String[] tools = plan.getTools();
            for (int i = 0; i < tools.length; i++) {
                final Deferred<Tool> tmp = warehouse.acquireTool(tools[i]);
                tmp.whenResolved(() -> {
                    res = res + tmp.get().useOn(product);
                    product.setFinalId(res);
                    warehouse.releaseTool(tmp.get());
                });
            }
            complete(product);
        });
    }
}





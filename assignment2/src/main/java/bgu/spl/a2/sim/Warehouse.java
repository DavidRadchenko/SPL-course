package bgu.spl.a2.sim;

import bgu.spl.a2.sim.tools.GcdScrewDriver;
import bgu.spl.a2.sim.tools.NextPrimeHammer;
import bgu.spl.a2.sim.tools.RandomSumPliers;
import bgu.spl.a2.sim.tools.Tool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.Deferred;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * A class representing the warehouse in your simulation
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 */
public class Warehouse {

    private ConcurrentLinkedQueue<ManufactoringPlan> manuPlans;
    private AtomicIntegerArray numOfTools;

    /**
     * Constructor
     */
    public Warehouse() {
        manuPlans = new ConcurrentLinkedQueue<ManufactoringPlan>();
        numOfTools = new AtomicIntegerArray(3);
    }

    /**
     * Tool acquisition procedure
     * Note that this procedure is non-blocking and should return immediatly
     *
     * @param type - string describing the required tool
     * @return a deferred promise for the  requested tool
     */
    public synchronized Deferred<Tool> acquireTool(String type) {
        //we decrement the corresponding cell in out tools array

        Tool tool = null;
        if (type.equals("gs-driver")) {
            numOfTools.decrementAndGet(0);
            if (numOfTools.get(0) >= 0) tool = new GcdScrewDriver();
        } else if (type.equals("np-hammer")) {
            numOfTools.decrementAndGet(1);
            if (numOfTools.get(1) >= 0) tool = new NextPrimeHammer();
        } else {
            numOfTools.decrementAndGet(2);
            if (numOfTools.get(2) >= 0) tool = new RandomSumPliers();
        }

        Deferred<Tool> getTool = new Deferred<Tool>();
        getTool.resolve(tool);
        return getTool;
    }

    /**
     * Tool return procedure - releases a tool which becomes available in the warehouse upon completion.
     *
     * @param tool - The tool to be returned
     */
    public synchronized void releaseTool(Tool tool) {
        //we increment the corresponding cell in out tools array
        if (tool instanceof GcdScrewDriver)
            numOfTools.incrementAndGet(0);

        else if (tool instanceof NextPrimeHammer)
            numOfTools.incrementAndGet(1);

        else
            numOfTools.incrementAndGet(2);
    }


    /**
     * Getter for ManufactoringPlans
     *
     * @param product - a string with the product name for which a ManufactoringPlan is desired
     * @return A ManufactoringPlan for product
     */
    public synchronized ManufactoringPlan getPlan(String product) {
        for (ManufactoringPlan manPlan : manuPlans) {
            if (manPlan.getProductName().equals(product))
                return manPlan;
        }
        return null;
    }

    /**
     * Store a ManufactoringPlan in the warehouse for later retrieval
     *
     * @param plan - a ManufactoringPlan to be stored
     */
    public void addPlan(ManufactoringPlan plan) {
        manuPlans.add(plan);
    }

    /**
     * Store a qty Amount of tools of type tool in the warehouse for later retrieval
     *
     * @param tool - type of tool to be stored
     * @param qty  - amount of tools of type tool to be stored
     */
    public void addTool(Tool tool, int qty) {
        if (tool instanceof GcdScrewDriver)
            numOfTools.addAndGet(0, qty);
        else if (tool instanceof NextPrimeHammer)
            numOfTools.addAndGet(1, qty);
        else
            numOfTools.addAndGet(2, qty);
    }

}

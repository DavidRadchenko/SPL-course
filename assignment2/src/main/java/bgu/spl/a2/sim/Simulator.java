/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;

import bgu.spl.a2.WorkStealingThreadPool;
import bgu.spl.a2.sim.conf.ManufactoringPlan;
import bgu.spl.a2.sim.tasks.SpawnClass;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;


/**
 * A class describing the simulator for part 2 of the assignment
 */
public class Simulator {

    static WorkStealingThreadPool myPool;
    private static Warehouse ware;
    private static JsonDecomp dec;


    /**
     * CreateWh creates an instance of the WareHouse class and adds plans to it according
     * to a given JSON file.
     * It also sets the quantity of each tool
     */
    public static void CreateWh() {
        ware = new Warehouse();

        for (ManufactoringPlan ma : dec.getPlans()) {
            ware.addPlan(ma);
        }
        for (int i = 0; i < dec.getTools().length; i++)
            ware.addTool(dec.getTools()[i].getTool(), dec.getTools()[i].getAmount());
    }

    /**
     * Begin the simulation
     * Should not be called before attachWorkStealingThreadPool()
     */

    public static ConcurrentLinkedQueue<Product> start() throws InterruptedException {
        //TODO: this,a new class for the spawning of manufacturing products,a class for manufacturing
        myPool.start();
        ConcurrentLinkedQueue<Product> result = new ConcurrentLinkedQueue<Product>();
        JsonDecomp.Wave[] wavesArray = dec.getArrayOfWaves();
        for (int i = 0; i < wavesArray.length; i++) {
            // We address every wave separately, and add it to our pool for execution
            CountDownLatch l = new CountDownLatch(1);
            SpawnClass next = new SpawnClass(wavesArray[i], ware);
            myPool.submit(next);
            next.getResult().whenResolved(() -> {
                for (Product pro : next.getResult().get())
                    result.add(pro);
                l.countDown();
            });
            l.await();
        }
        myPool.shutdown();
        return result;
    }

    /**
     * attach a WorkStealingThreadPool to the Simulator, this WorkStealingThreadPool will be used to run the simulation
     *
     * @param myWorkStealingThreadPool - the WorkStealingThreadPool which will be used by the simulator
     */
    public static void attachWorkStealingThreadPool(WorkStealingThreadPool myWorkStealingThreadPool) {
        myPool = myWorkStealingThreadPool;
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        // Parsing a JSON file using GSON
        Gson gson = new Gson();
        JsonParser parse = new JsonParser();

        JsonObject obj =(JsonObject) parse.parse(new FileReader(args[0]));

        dec = gson.fromJson(obj, JsonDecomp.class);
        attachWorkStealingThreadPool(new WorkStealingThreadPool(dec.getThreadsDec()));

        // setting the wareHouse
        CreateWh();

        ConcurrentLinkedQueue<Product> SimulationResult;
        // starting the simulation
        SimulationResult = start();

        // writing our input to result.ser
        FileOutputStream fout = new FileOutputStream("result.ser");
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(SimulationResult);
        for(Product p : SimulationResult){
            System.out.println("product = " +p.getName() +" "+p.getFinalId());
        }
    }
}

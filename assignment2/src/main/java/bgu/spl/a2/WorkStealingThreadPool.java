package bgu.spl.a2;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * represents a work stealing thread pool - to understand what this class does
 * please refer to your assignment.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
@SuppressWarnings("Since15")
public class WorkStealingThreadPool {

    private int nthreads;
    private ConcurrentLinkedDeque<Task>[] tasks;
    private Thread[] threads;
    private Processor[] processors;
    private VersionMonitor vm;

    /**
     * creates a {@link WorkStealingThreadPool} which has nthreads
     * {@link Processor}s. Note, threads should not get started until calling to
     * the {@link #start()} method.
     * <p>
     * Implementors note: you may not add other constructors to this class nor
     * you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param nthreads the number of threads that should be started by this
     *                 thread pool
     */
    public WorkStealingThreadPool(int nthreads) {
        this.nthreads = nthreads;
        processors = new Processor[nthreads];
        threads = new Thread[nthreads];
        tasks = new ConcurrentLinkedDeque[nthreads];
        vm = VersionMonitor.getInstance();
        for (int i = 0; i < nthreads; i++) {
            tasks[i] = new ConcurrentLinkedDeque<Task>();
            processors[i] = new Processor(i, this);
            threads[i] = new Thread(processors[i]);
        }
    }

    /**
     * submits a task to be executed by a processor belongs to this thread pool
     *
     * @param task the task to execute
     */
    public void submit(Task<?> task) {
        Random rand = new Random();
        int n = rand.nextInt(nthreads) + 0;
        tasks[n].add(task);
        vm.inc();
    }

    /**
     * closes the thread pool - this method interrupts all the threads and wait
     * for them to stop - it is returns *only* when there are no live threads in
     * the queue.
     * <p>
     * after calling this method - one should not use the queue anymore.
     *
     * @throws InterruptedException          if the thread that shut down the threads is
     *                                       interrupted
     * @throws UnsupportedOperationException if the thread that attempts to
     *                                       shutdown the queue is itself a processor of this queue
     */
    public void shutdown() throws InterruptedException {
        for (int i = 0; i < nthreads; i++) {
            processors[i].stop();
        }
        for (int i = 0; i < nthreads; i++) {
            threads[i].interrupt();
        }
        for (int i = 0; i < nthreads; i++) {
            threads[i].join();
        }
        Thread.currentThread().interrupt();
    }

    /**
     * start the threads belongs to this thread pool
     */
    public void start() {
        for (int i = 0; i < nthreads; i++) {
            processors[i].start();
            threads[i].start();
        }
    }

    /**
     * returns & removes the next task in line for execution in queue 'i'
     */
    /*package*/ Task<?> getTask(int i) {
        while (!tasks[i].isEmpty())
            return tasks[i].removeFirst();
        return null;
    }

    /**
     * returns the amount of tasks in a given queue
     */
    /*package*/ int tasksSize(int i) {
        return tasks[i].size();
    }

    /**
     * returns the size of the pool
     */
    /*package*/ int getNthreads() {
        return nthreads;
    }

    /**
     * returns the task queue of a given processor
     */
    /*package*/
    ConcurrentLinkedDeque<Task> getTasks(int id) {
        return tasks[id];
    }

}
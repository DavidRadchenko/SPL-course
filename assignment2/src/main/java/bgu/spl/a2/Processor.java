package bgu.spl.a2;


import java.util.NoSuchElementException;

/**
 * this class represents a single work stealing processor, it is
 * {@link Runnable} so it is suitable to be executed by threads.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 */
public class Processor implements Runnable {

    private final WorkStealingThreadPool pool;
    private final int id;
    private VersionMonitor vm;
    private boolean running;

    /**
     * constructor for this class
     * <p>
     * IMPORTANT:
     * 1) this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     * <p>
     * 2) you may not add other constructors to this class
     * nor you allowed to add any other parameter to this constructor - changing
     * this may cause automatic tests to fail..
     *
     * @param id   - the processor id (every processor need to have its own unique
     *             id inside its thread pool)
     * @param pool - the thread pool which owns this processor
     */
    /*package*/ Processor(int id, WorkStealingThreadPool pool) {
        this.id = id;
        this.pool = pool;
        vm = VersionMonitor.getInstance();
    }

    @Override
    public void run() {
        while (running) {

            // if you dont have any tasks yhe you shoul try to 'steal'
            if (pool.tasksSize(id) == 0)
                stealTasks();


            //as long as you have tasks/ or you can steal tasks you should keep running
            while (pool.tasksSize(id) > 0) {
                Task<?> t = pool.getTask(id);
                if (t != null)
                    t.handle(this);
                if (pool.tasksSize(id) == 0)
                    stealTasks();
            }

            // we set version to the current state of vm
            int version = vm.getVersion();
            try {
                vm.inc();
                vm.await(version + 1);
            } catch (InterruptedException e) {}
            finally {
                if(!running)
                    break;
            }
        }
    }

    /**
     * this function is responsible for the 'stealing' operation.
     * Here we take half of the tasks, from the next not empty queue.
     */
    protected synchronized void stealTasks() {
        int size = pool.getNthreads();
        for (int i = id; i < (size + id); i++) {
            int tmp = pool.tasksSize(i % size) / 2;
            for (int j = 0; j < tmp; j++) {
                Task<?> t;
                try {
                    t = pool.getTask(i % size);
                } catch (NoSuchElementException e) {
                    break;
                }
                if (t != null)
                    submit(t);

            }
        }
    }

    /**
     * sets running to be false so that run() won't start
     */
    /*package*/ void stop() {
        running = false;
    }

    /**
     * sets running to be true so that run() will start
     */
    /*package*/ void start() {
        running = true;
    }

    /**
     * submits new task/tasks to the processors queue
     */
    /*package*/ void submit(Task<?>... task) {
        for (Task<?> t : task)
            pool.getTasks(id).addLast(t);
        vm.inc();
    }

    /*package*/ int getId() {
        return id;
    }

  /* void putOnHold(Task<?> t) {
        onHold.add(t);
    }*/


    /*package*/ void resume(Task<?> t) {
        //onHold.remove(t);
        submit(t);
    }

    /*package*/ WorkStealingThreadPool getPool() {
        return pool;
    }
}
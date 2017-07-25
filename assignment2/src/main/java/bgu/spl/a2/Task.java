package bgu.spl.a2;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * an abstract class that represents a task that may be executed using the
 * {@link WorkStealingThreadPool}
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add to this class can
 * only be private!!!
 *
 * @param <R> the task result type
 */
public abstract class Task<R> {

    private Deferred<R> def = new Deferred<R>();
    private Processor processor;
    private Runnable callback;
    private ConcurrentLinkedDeque<Deferred>  depends = new ConcurrentLinkedDeque<Deferred>();

    /**
     * start handling the task - note that this method is protected, a handler
     * cannot call it directly but instead must use the
     * {@link #handle(Processor)} method
     */
    protected abstract void start();

    /**
     * start/continue handling the task
     * <p>
     * this method should be called by a processor in order to start this task
     * or continue its execution in the case where it has been already started,
     * any sub-tasks / child-tasks of this task should be submitted to the queue
     * of the handler that handles it currently
     * <p>
     * IMPORTANT: this method is package protected, i.e., only classes inside
     * the same package can access it - you should *not* change it to
     * public/private/protected
     *
     * @param handler the handler that wants to handle the task
     */
    /*package*/
    final synchronized void handle(Processor handler) {
        processor = handler;
        if (callback == null)
            start();
        else
            callback.run();
    }

    /**
     * This method schedules a new task (a child of the current task) to the
     * same processor which currently handles this task.
     *
     * @param task the task to execute
     */
    protected final void spawn(Task<?>... task) {
        processor.submit(task);
    }

    /**
     * add a callback to be executed once *all* the given tasks results are
     * resolved
     * <p>
     * Implementors note: make sure that the callback is running only once when
     * all the given tasks completed.
     *
     * @param tasks
     * @param callback the callback to execute once all the results are resolved

     * add a callback to be executed once all the given tasks are completed. Once you find
     * out that all the results has been resolved, re-add the task to the processor queue which will
     * cause it to eventually handle the task again, when this happen this task should be able to
    recognize that it was already been started and therefore execute the continuation instead of the start method.
     */

    protected final void whenResolved(Collection<? extends Task<?>> tasks, Runnable callback) {
        if(tasks.isEmpty())
            callback.run();
        this.callback = callback;

        for (Task<?> t : tasks)
            depends.add(t.getResult());

        for (Task<?> t : tasks) {
            t.getResult().whenResolved(() -> {
                boolean dependsResolved = true;
                for (Deferred<?> d : depends)
                    if (!d.isResolved())
                        dependsResolved = false;

                if (dependsResolved)
                    processor.resume(this);
            });
        }
    }


    /**
     * resolve the internal result - should be called by the task derivative
     * once it is done.
     *
     * @param result - the task calculated result
     */
    protected final void complete(R result) {
        if(!def.isResolved())
            def.resolve(result);
    }
    /**
     * @return this task deferred result
     * <p>
     * returns the task deferred result. In order to handle the rescheduling you can add a helper
     * method to Task which be called by a processor in order to start the task or continue its execution in the
     * case where it has been already started.
     */
    public final Deferred<R> getResult() {
        return def;
    }

}
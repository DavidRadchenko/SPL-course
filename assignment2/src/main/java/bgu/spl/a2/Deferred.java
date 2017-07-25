package bgu.spl.a2;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * this class represents a deferred result i.e., an object that eventually will
 * be resolved to hold a result of some operation, the class allows for getting
 * the result once it is available and registering a callback that will be
 * called once the resulkit is available.
 * <p>
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 * @param <T> the result type
 */
public class Deferred<T> {

    private T value;
    private ConcurrentLinkedDeque<Runnable> callbacks= new ConcurrentLinkedDeque<Runnable>();



    /**
     * @return the resolved value if such exists (i.e., if this object has been
     * {@link #resolve(Object)}ed yet
     * @throws IllegalStateException in the case where this method is called and
     *                               this object is not yet resolved
     */
    public T get() {
        if (value != null)
            return value;
        else throw new IllegalStateException("Not Resolved Yet.");
    }

    /**
     * @return true if this object has been resolved - i.e., if the method
     * {@link #resolve(Object)} has been called on this object before.
     */
    public boolean isResolved() {
        return (value != null);
    }

    /**
     * resolve this deferred object - from now on, any call to the method
     * {@link #get()} should return the given value
     * <p>
     * Any callbacks that were registered to be notified when this object is
     * resolved via the {@link #whenResolved(Runnable)} method should
     * be executed before this method returns
     *
     * @param value - the value to resolve this deferred object with
     * @throws IllegalStateException in the case where this object is already
     *                               resolved
     */
    public synchronized void resolve(T value) {
        if (isResolved())
            throw new IllegalStateException("resolving a resolved def");
        this.value = value;
        callbacks.forEach(Runnable::run);
    }

    /**
     * add a callback to be called when this object is resolved. if while
     * calling this method the object is already resolved - the callback should
     * be called immediately
     * <p>
     * Note that in any case, the given callback should never get called more
     * than once, in addition, in order to avoid memory leaks - once the
     * callback got called, this object should not hold its reference any
     * longer.
     *
     * @param callback the callback to be called when the deferred object is
     *                 resolved
     *                 <p>
     *                 add a callback to be called when this object is resolved if while calling this method the
     *                 object is already resolved - the callback should be called immediately.
     */
    public synchronized void whenResolved(Runnable callback) {

        if (value == null)
            callbacks.add(callback);
        else
            callback.run();
    }
}
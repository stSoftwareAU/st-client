package com.aspc.remote.rest.internal;

import com.aspc.remote.rest.Response;
import com.aspc.remote.util.misc.CLogger;
import com.aspc.remote.util.misc.ThreadPool;
import com.aspc.remote.util.misc.TimeUtil;
import com.aspc.remote.util.misc.VersionENV;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.apache.commons.logging.Log;

/**
 *
 *  @author      Lei Gao
 *  @since       20 March 2015
 */
public class ReSTTask 
{
    /**
     * The maximum number of concurrent requests to be run at one time. 
     */
    public static final int MAX_ASYNC_REST_CALLS;
    
    private final RestCall call;
    private Response response;
    private Exception exception;
    private static final Log LOGGER = CLogger.getLog( "com.aspc.remote.rest.internal.ReSTTask");//#LOGGER-NOPMD
    private boolean completed;
    private final Lock lock=new ReentrantLock();
    private final Condition taskChanged;
    private static final Condition COUNTER_CONDITION;
    private static final Lock COUNTER_LOCK=new ReentrantLock();
    private static final AtomicInteger CURRENT_CALL_COUNT=new AtomicInteger();
    private static final ThreadLocal<Boolean>ASYNC_REST_CALL=new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }        
    };
    private static final ThreadLocal<Boolean>SLEEPING=new ThreadLocal<Boolean>(){
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    };
    private ReSTTask(final @Nonnull RestCall call) {
        this.call=call;

        lock.lock();
        try
        {
            taskChanged=lock.newCondition();
        }
        finally
        {
            lock.unlock();
        }
    }

    private static final String PROPERTY_MAX_CONCURRENT_REST_CALLS="MAX_CONCURRENT_REST_CALLS";
    
    private static final BlockingQueue<RestCall> WAITING_CALLS=new LinkedBlockingQueue();
    @CheckReturnValue @Nonnull
    public static ReSTTask submit( final @Nonnull RestCall call, final @Nonnegative long timeout) throws InterruptedException,TimeoutException
    {
        if( timeout<=0) throw new IllegalArgumentException( "Timeout must be positive was: " + timeout);

        long start=0;
        ReSTTask t=new ReSTTask(call);

        if(ASYNC_REST_CALL.get())
        {
            t.process();
        }
        else
        {
            COUNTER_LOCK.lock();
            try
            {
                try{
                    while( CURRENT_CALL_COUNT.get() >MAX_ASYNC_REST_CALLS)
                    {
                        if( start==0)
                        {
                            start=System.currentTimeMillis();
                            WAITING_CALLS.add(call);
                            LOGGER.info("queuing ReST call: " + call);
                        }
                        else
                        {
                            LOGGER.info("polling: " + TimeUtil.getDiff(start) + " call: " + call);
                        }

                        Date deadline=new Date( start + timeout);
                        if( COUNTER_CONDITION.awaitUntil(deadline)==false)
                        {
                            throw new TimeoutException( "Timeout after: " + TimeUtil.getDiff(start) + " call: " + call);
                        }

                        RestCall peekCall = WAITING_CALLS.peek();

                        if( peekCall!=call)
                        {
                            /* Your call is Not the next one to run */
                            COUNTER_CONDITION.await(5000, TimeUnit.MILLISECONDS);
                        }
                    }
                }
                finally
                {
                    if( WAITING_CALLS.remove(call))
                    {
                        COUNTER_CONDITION.signalAll();
                    }
                }

                if( start!=0)
                {
                    LOGGER.info("blocked: " + TimeUtil.getDiff(start) + " call: " + call);
                }

                Runner r= new Runner(t);              
                ThreadPool.schedule(r);
                CURRENT_CALL_COUNT.incrementAndGet();
            }
            finally
            {
                COUNTER_LOCK.unlock();
            }
        }
        
        return t;
    }

    private static void increment()
    {
        assert ASYNC_REST_CALL.get():"should only be called in async mode";
        COUNTER_LOCK.lock();
        try
        {
            int count=CURRENT_CALL_COUNT.incrementAndGet();
            COUNTER_CONDITION.signalAll();
            assert count>=0: "Counter should never be negative was: " + count;
//            assert count <= MAX_ASYNC_REST_CALLS: "Counter should never be more than the max ASYNC rest calls was: " + count;
        }
        finally
        {
            COUNTER_LOCK.unlock();
        }
    }

    private static void decrement()
    {
        assert ASYNC_REST_CALL.get():"should only be called in async mode";
        COUNTER_LOCK.lock();
        try
        {
            int count=CURRENT_CALL_COUNT.decrementAndGet();
            COUNTER_CONDITION.signalAll();
            assert count>=0: "Counter should never be negative was: " + count;
        }
        finally
        {
            COUNTER_LOCK.unlock();
        }
    }

    public static void resume()
    {
        Boolean sleeping =SLEEPING.get();
        if(sleeping!=null && sleeping==true)
        {
            SLEEPING.set(Boolean.FALSE);

            if( ASYNC_REST_CALL.get())
            {
                increment();
            }
        }
    }

    public static void sleep()
    {
        Boolean sleeping =SLEEPING.get();
        if(sleeping!=null&& sleeping==false)
        {
            SLEEPING.set(Boolean.TRUE);
            if( ASYNC_REST_CALL.get())
            {
                decrement();
            }
        }
    }

    @CheckReturnValue @Nonnull
    public Response get( final @Nonnegative long ms)throws ExecutionException,TimeoutException,InterruptedException
    {
        if( ms<=0) throw new IllegalArgumentException("must be a positive number of milliseconds was: " + ms);
        long start=System.currentTimeMillis();
        long until=start + ms;
        while(completed==false)
        {
            long now=System.currentTimeMillis();
            long waitMS=Math.min(until-now, 1000);
            if( waitMS<=0)
            {
                throw new TimeoutException("timed out after " + TimeUtil.getDiff(start, now));
            }

            lock.lock();
            try
            {
                taskChanged.await(waitMS, TimeUnit.MILLISECONDS);
            }
            finally
            {
                lock.unlock();
            }
        }

        if( exception!=null)
        {
            throw new ExecutionException( exception);
        }
        return response;
    }
    
    private void process()
    {
        if( SLEEPING.get())
        {
            assert false: "Should not be 'sleeping' as we are actively making a call";
            SLEEPING.remove();
        }
        try{
           response=call.call();
        }
        catch( Exception e)
        {
            exception=e;
            LOGGER.warn(call, e);
        }
        finally
        {
            if( SLEEPING.get())
            {
                assert false: "Should not be 'sleeping' as we are actively making a call";
                SLEEPING.remove();
            }

            lock.lock();
            try
            {
                completed=true;
                taskChanged.signalAll();
            }
            finally
            {
                lock.unlock();
            }

        }
    }

    private static class Runner implements Runnable{
        private final ReSTTask t;
        Runner(final @Nonnull ReSTTask t)
        {
            this.t=t;
        }
        
        @Override
        public void run() {
            try{
                ASYNC_REST_CALL.set(true);
            
                t.process();           
            }
            finally
            {
                decrement();
                ASYNC_REST_CALL.set(false);
            }
        }
    }
    
    static{

        int cores=Runtime.getRuntime().availableProcessors();
        
        VersionENV tmp= new VersionENV( PROPERTY_MAX_CONCURRENT_REST_CALLS, cores*4, 2, 32072 );
        LOGGER.debug( tmp);

        MAX_ASYNC_REST_CALLS=tmp.calculateVersion();
        
        COUNTER_LOCK.lock();
        try
        {
            COUNTER_CONDITION = COUNTER_LOCK.newCondition();
        }
        finally
        {
            COUNTER_LOCK.unlock();
        }
    }
}

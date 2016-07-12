package org.ameet.rx;

import org.ameet.rx.RxProcessing;
import org.ameet.rx.ancillary.BasicStringSubscriberWithLatch;
import org.ameet.rx.ancillary.TaskUtility;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import rx.Observable;
import rx.observers.TestSubscriber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by achaub001c on 7/12/2016.
 */
public class RxProcessingTest {
    private static ExecutorService service;
    private final List<String> expectedList = new ArrayList<String>() {
        {
            add("1");
            add("2");
            add("3");
            add("4");
            add("5");
        }
    };
    private final String SINGLE_FUTURE_TASK = "dystopia";
    private RxProcessing rxProcessing = new RxProcessing();

    @BeforeClass
    public static void init() {
        service = Executors.newFixedThreadPool(4);
    }

    @Test
    public void testSingleBasic() {
        Observable<String> o = rxProcessing.getStringObservable();
        TestSubscriber<String> ts = new TestSubscriber<>();
        o.subscribe(ts);
        ts.awaitTerminalEvent();
        ts.assertNoErrors();
        ts.assertReceivedOnNext(Arrays.asList("1"));
    }

    /**
     * tests an observable which expects a list of values and emits them
     * the subscriber is constructed with a result List and a latch
     * the result is added to on onNext() and the latch is counted down on onComplete()
     */
    @Test
    public void testString() {
        Observable<String> o = rxProcessing.getSingleStringObservable(expectedList);
        List<String> actual = new ArrayList<>();
        BasicStringSubscriberWithLatch s = new BasicStringSubscriberWithLatch(actual, new CountDownLatch(1));
        o.subscribe(s);
        s.awaitTerminalEvent();
        System.out.println(Arrays.toString(actual.toArray()));
        Assert.assertEquals(expectedList, actual);
    }

    @Test
    public void testConcatObservable() {
        Observable<String> o = rxProcessing.concatObservables(expectedList);
        BasicStringSubscriberWithLatch s = rxProcessing.getSingleSubscriberWithBuiltInResultList();
        o.subscribe(s);
        s.awaitTerminalEvent();
        // get list
        List<String> actual = s.getResults();
        Assert.assertEquals(expectedList, actual);
        System.out.println(Arrays.toString(actual.toArray()));
    }

    /**
     * execute a task using thread pool / executor service and pass the future
     * to create an observable and get its result.
     */
    @Test
    public void testSingleFuture() {
        FutureTask<String> f = TaskUtility.getSingleStringFuture(SINGLE_FUTURE_TASK, 2000);
        service.submit(f);
        System.out.println("Task execution started at: " + System.currentTimeMillis());
        Observable<String> o = rxProcessing.getSingleFutureObservable(f);
        TestSubscriber<String> ts = new TestSubscriber<>();
        o.subscribe(ts);
        System.out.println("Subscription started at: " + System.currentTimeMillis());
        ts.awaitTerminalEvent();
        System.out.println("Termination at: " + System.currentTimeMillis());
        ts.assertNoErrors();
        ts.assertReceivedOnNext(Arrays.asList(SINGLE_FUTURE_TASK));
    }

    /**
     * collect a list of futures and pass it through observable, waiting for the
     * completion
     */
    @Test
    public void testListOfFutures() {
        long start = System.currentTimeMillis();
        List<String> expectedList = TaskUtility.getListOfNumbers(20);
        List<Future<String>> futures = TaskUtility.submitListOfTasks(expectedList, 2000);
        Observable<String> o = rxProcessing.getListOfFuturesObservable(futures, 2000);

        System.out.println("Observable created in: " + getMilliElapsed(start));
        // create subscriber
        BasicStringSubscriberWithLatch s = rxProcessing.getSingleSubscriberWithBuiltInResultList();
        // subscribe to the events
        o.subscribe(s);
        System.out.println("Subscription started in: " + getMilliElapsed(start));
        s.awaitTerminalEvent();
        System.out.println("Termination in: " + getMilliElapsed(start));

        List<String> actual = s.getResults();
        System.out.println(Arrays.toString(actual.toArray()));
    }

    private long getMilliElapsed(long start) {
        return System.currentTimeMillis() - start;
    }
}
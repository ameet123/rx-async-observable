package org.ameet.rx;

import org.ameet.rx.ancillary.GenericUtil;
import org.ameet.rx.ancillary.QuoteSubscriberWithLatch;
import org.ameet.rx.ancillary.TaskUtility;
import org.ameet.rx.model.QuoteResource;
import rx.Observable;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by achaub001c on 7/13/2016.
 * <p>
 * This is the main class which consolidates all the method calls into
 * just the specific method which can be consumer facing.
 * This is what should be displayed in the manual
 */
public class ObservableUtil {
    private RxProcessing rxProcessing;

    public ObservableUtil() {
        this.rxProcessing = new RxProcessing();
    }

    /**
     * collects all the relevant methods and packages them into a single method
     */
    public List<QuoteResource> processListOfQuotes() {
        long start = System.currentTimeMillis();
        System.out.println("1. creating callable task list");
        List<Callable<QuoteResource>> f = TaskUtility.getListQuoteCallable(false, 2);
        System.out.println("1.1 creating callable task completed");

        System.out.println("2. Creating Observable for list of callables");
        Observable<QuoteResource> o = rxProcessing.getFromCallableList(f);
        System.out.println("2.1 Observable created in: " + GenericUtil.getMilliElapsed(start));

        // create subscriber
        QuoteSubscriberWithLatch s = new QuoteSubscriberWithLatch();
        // subscribe to the events
        System.out.println("4 Starting Subscription ");
        o.subscribe(s);
        System.out.println("4.1Subscription started in: " + GenericUtil.getMilliElapsed(start));

        s.awaitTerminalEvent();
        System.out.println("Termination in: " + GenericUtil.getMilliElapsed(start));

        return s.getResults();
    }
}
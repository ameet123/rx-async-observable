package org.ameet.rx;

import org.ameet.rx.ancillary.BasicStringSubscriber;
import org.ameet.rx.ancillary.BasicStringSubscriberWithLatch;
import org.ameet.rx.model.QuoteResource;
import rx.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * Created by achaub001c on 7/12/2016.
 */
public class RxProcessing {

    public static void main(String[] args) {
        Observable<String> o = Observable.just("1", "2", "3");
        CountDownLatch latch = new CountDownLatch(1);
        List<String> results = new ArrayList<>();
        o.subscribe(new BasicStringSubscriber(results, latch));
        System.out.println("--------Print Result------------");
        System.out.println(Arrays.toString(results.toArray()));
        System.out.println("Latch=>" + latch.getCount());
    }

    /**
     * publish a list of values
     *
     * @return
     */
    public Observable<String> getSingleStringObservable(List<String> values) {
        return Observable.from(values.toArray(new String[values.size()]));
    }

    /**
     * this returns a simple subscriber with a built in newly created list and
     * a countdown latch with just 1 item. This is supposed to be a solitary subscriber
     * The list is used to collect and consolidate the onNext event values.
     *
     * @return
     */
    public BasicStringSubscriberWithLatch getSingleSubscriberWithBuiltInResultList() {
        return new BasicStringSubscriberWithLatch();
    }

    /**
     * concatenate observables
     *
     * @param values
     */
    public Observable<String> concatObservables(List<String> values) {
        Observable<String> o = Observable.just(values.get(0));
        for (int i = 1; i < values.size(); i++) {
            Observable<String> o1 = Observable.just(values.get(i));
            o = o.mergeWith(o1);
        }
        return o;
    }

    /**
     * publish a single value
     *
     * @return
     */
    public Observable<String> getStringObservable() {
        return Observable.just("1");
    }

    /**
     * observable based on a single future
     *
     * @param future
     * @return
     */
    public Observable<String> getSingleFutureObservable(Future<String> future) {
        return Observable.from(future);
    }

    /**
     * based on a list of futures, get an observable. I am using mergeWith method, since I don't
     * know how to concatenate multiple observables, especially with the signature for future which
     * provides for the timeout.
     *
     * @param futures
     * @param delayMilli
     * @return
     */
    public Observable<String> getListOfFuturesObservable(List<Future<String>> futures, long delayMilli) {
        Observable<String> o = Observable.from(futures.get(0), delayMilli, TimeUnit.MILLISECONDS);
        for (int i = 1; i < futures.size(); i++) {
            Observable<String> o1 = Observable.from(futures.get(i), delayMilli, TimeUnit.MILLISECONDS);
            o = o.mergeWith(o1);
        }
        return o;
    }

    public Observable<QuoteResource> getSingleQuoteFuture(FutureTask<QuoteResource> quoteResourceFutureTask) {
        return Observable.from(quoteResourceFutureTask);
    }
}

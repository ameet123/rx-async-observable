package org.ameet.rx.ancillary;

import rx.Subscriber;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by achaub001c on 7/12/2016.
 */
public class BasicStringSubscriberWithLatch extends Subscriber<String> {
    private List<String> results;
    private CountDownLatch countDownLatch;

    public BasicStringSubscriberWithLatch(List<String> result, CountDownLatch latch) {
        this.results = result;
        this.countDownLatch = latch;
    }

    public BasicStringSubscriberWithLatch() {
        this.results = new ArrayList<>();
        this.countDownLatch = new CountDownLatch(1);
    }

    public List<String> getResults() {
        return results;
    }

    public void setResults(List<String> results) {
        this.results = results;
    }

    @Override
    public void onCompleted() {
        countDownLatch.countDown();
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(String s) {
        results.add(s);
    }

    public void awaitTerminalEvent() {
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
    }
}

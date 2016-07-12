package org.ameet.rx.ancillary;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by achaub001c on 7/12/2016.
 * utility class to create simple callables returning futures
 */
public class TaskUtility {
    private static ExecutorService service = Executors.newFixedThreadPool(10);

    /**
     * generate of list of numbers converted to string
     * starting with 1 and inclusive of the provided count
     * @param count
     * @return
     */
    public static List<String> getListOfNumbers(int count) {
        return IntStream.rangeClosed(1, count).mapToObj(value -> String.valueOf(value)).collect
                (Collectors.toList());
    }

    /**
     * create a future based on the given value.
     *
     * @param v
     * @param delayMilli
     * @return
     */
    public static FutureTask<String> getSingleStringFuture(String v, long delayMilli) {
        FutureTask<String> f = new FutureTask<String>(() -> {
            Thread.sleep(delayMilli);
            return v;
        });
        return f;
    }

    /**
     * based on the list of values passed, submit callable tasks via an executor service
     * and return corresponding list of futures.
     *
     * @param values
     * @return
     */
    public static List<Future<String>> submitListOfTasks(List<String> values, long delayMilli) {
        List<Future<String>> futures = new ArrayList<>();
        for (String v : values) {
            futures.add(service.submit(() -> {
                Thread.sleep(delayMilli);
                return v;
            }));
        }
        return futures;
    }
}

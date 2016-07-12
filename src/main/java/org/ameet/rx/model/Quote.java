package org.ameet.rx.model;

/**
 * Created by achaub001c on 7/12/2016.
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * to store the quotes
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Quote {

    Long id;
    String quote;

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    String threadName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    @Override
    public String toString() {
        return "[ id-"+threadName+':'+ quote +']';
    }
}
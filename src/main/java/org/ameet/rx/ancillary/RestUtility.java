package org.ameet.rx.ancillary;

import org.ameet.rx.model.QuoteResource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Created by achaub001c on 7/12/2016.
 * a utility class to make http requests to a url and retrieve results
 */
public class RestUtility {
    private static final String QUOTE_URL = "http://gturnquist-quoters.cfapps.io/api/random";
    private static final int TIMEOUT_MS = 3000;
    private static final RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

    private static ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setReadTimeout(TIMEOUT_MS);
        factory.setConnectTimeout(TIMEOUT_MS);
        return factory;
    }

    /**
     * get a random quote from URL
     *
     * @return
     */
    public QuoteResource getRandomQuote() {
        return restTemplate.getForObject(QUOTE_URL, QuoteResource.class);
    }
}

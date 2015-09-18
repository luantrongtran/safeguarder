package ifn701.safeguarder.webservices.google_web_services;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;

/**
 * Created by lua on 16/09/2015.
 */
public class RequestFactorCreator {
    public static HttpRequestFactory createRequestFactory(final HttpTransport transport,
                                                          final JacksonFactory jacksonFactory) {
        return transport.createRequestFactory(new HttpRequestInitializer() {
            public void initialize(HttpRequest request) {
                JsonObjectParser parser = new JsonObjectParser(jacksonFactory);
                request.setParser(parser);
            }
        });
    }
}

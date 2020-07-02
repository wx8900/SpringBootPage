package com.demo.test.httpclient;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * @author Jack
 */
public class HttpClientPoolTest {
    public static void main(String[] args) {
        ConnectionKeepAliveStrategy keepAliveStrategy = new ConnectionKeepAliveStrategy() {

            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
                while (it.hasNext()) {
                    HeaderElement he = it.nextElement();
                    String param = he.getName();
                    String value = he.getValue();
                    if (value != null && param.equalsIgnoreCase("timeout")) {
                        try {
                            return Long.parseLong(value) * 1000;
                        } catch (NumberFormatException ignore) {

                        }
                    }
                }
                HttpHost target = (HttpHost) context.getAttribute(HttpClientContext.HTTP_TARGET_HOST);
                if ("bizapi.jd.com ".equalsIgnoreCase(target.getHostName())) {
                    return 60 * 1000;
                } else {
                    return 300 * 1000;
                }

//                CloseableHttpClient httpClient = httpClientBuilder.setConnectionManager(pollingConnectionManager)
//                        .setKeepAliveStrategy(keepAliveStrategy).setDefaultRequestConfig(defaultRequestConfig).build();
            }
        };
    }
}

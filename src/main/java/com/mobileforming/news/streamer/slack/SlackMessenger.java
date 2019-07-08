package com.mobileforming.news.streamer.slack;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.integration.handler.AbstractMessageHandler;
import org.springframework.messaging.Message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffreypthomas on 9/7/15.
 */
public class SlackMessenger extends AbstractMessageHandler {

    private static final String SLACK_URL =;//change code to supply this value during runtime
    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(SlackMessenger.class);
    private final static AtomicInteger newsCounter = new AtomicInteger();
    private final CloseableHttpClient httpClient = HttpClients.createDefault();


    @Override protected void handleMessageInternal(Message<?> message) throws Exception {
        final String payload = (String) message.getPayload();
        if (StringUtils.isNotEmpty(payload)) {
            LOG.info("Message object received [{}]", newsCounter.incrementAndGet());


            HttpPost httpPost = new HttpPost(SLACK_URL);

            httpPost.setEntity(new StringEntity(payload, ContentType.APPLICATION_JSON));
            CloseableHttpResponse response = httpClient.execute(httpPost);

            try {
                if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                    HttpEntity entity = response.getEntity();
                    String responseString = EntityUtils.toString(entity, "UTF-8");
                    LOG.error("#error during httpPost in SlackNotifier: " + responseString);
                }
            } finally {
                response.close();
            }


        }
    }
}

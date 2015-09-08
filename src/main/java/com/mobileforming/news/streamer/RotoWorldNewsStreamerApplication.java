package com.mobileforming.news.streamer;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobileforming.news.streamer.data.model.PlayerNews;
import com.mobileforming.news.streamer.rotoworld.RotoWorldNewsEnricher;
import com.mobileforming.news.streamer.slack.SlackMessageTransformer;
import com.mobileforming.news.streamer.slack.SlackMessenger;
import com.rometools.rome.feed.synd.SyndEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.channel.MessageChannels;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.json.ObjectToJsonTransformer;
import org.springframework.integration.metadata.MetadataStore;
import org.springframework.integration.metadata.PropertiesPersistingMetadataStore;
import org.springframework.integration.scheduling.PollerMetadata;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

import java.net.MalformedURLException;
import java.net.URL;


//TODO:


/**
 * extract payload
 * <p/>
 * GenericMessage [payload={"uri":"316529","link":"http://www.rotoworld.com/player/nfl/10358/duke-johnson","comments":null,"updatedDate":1441395000000,"title":"Duke Johnson still in NFL concussion protocol - Duke Johnson | CLE","description":null,"links":[{"href":"http://www.rotoworld.com/player/nfl/10358/duke-johnson","rel":"alternate","type":null,"hreflang":null,"title":null,"length":0}],"contents":[{"type":"text","value":"Duke Johnson remains in the NFL's concussion protocol.","mode":null,"interface":"com.rometools.rome.feed.synd.SyndContent"}],"modules":[{"uri":"http://purl.org/dc/elements/1.1/","title":null,"creator":null,"subject":null,"description":null,"publisher":null,"contributors":[],"date":null,"type":null,"format":null,"identifier":null,"source":null,"language":null,"relation":null,"coverage":null,"rights":null,"sources":[],"titles":[],"creators":[],"descriptions":[],"publishers":[],"contributor":null,"identifiers":[],"languages":[],"relations":[],"coverages":[],"rightsList":[],"types":[],"dates":[],"subjects":[],"formats":[],"interface":"com.rometools.rome.feed.module.DCModule"}],"enclosures":[],"authors":[],"contributors":[],"source":null,"foreignMarkup":[],"wireEntry":null,"categories":[],"titleEx":{"type":"text","value":"Duke Johnson still in NFL concussion protocol - Duke Johnson | CLE","mode":null,"interface":"com.rometools.rome.feed.synd.SyndContent"},"interface":"com.rometools.rome.feed.synd.SyndEntry","publishedDate":null,"author":""}, headers={id=76495d5d-6cc2-ae17-ea17-cb04a7637e02, json__TypeId__=class com.rometools.rome.feed.synd.SyndEntryImpl, contentType=application/json, timestamp=1441438129762}]
 * <p/>
 * convert payload to message for slack
 * send message to slack
 */
@Configuration @SpringBootApplication @IntegrationComponentScan
public class RotoWorldNewsStreamerApplication {

    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(RotoWorldNewsStreamerApplication.class);
    private final int rotoWorldPlayerNewsFeedDelay = 1000 * 60;

    public static void main(String[] args) {
        final ConfigurableApplicationContext applicationContext =
            SpringApplication.run(RotoWorldNewsStreamerApplication.class, args);

    }

    @Bean public MessageSource<SyndEntry> playerNewsFeedMessageSource()
        throws MalformedURLException {

        URL feedUrl =
            new URL("http://www.rotoworld.com/rss/feed.aspx?sport=nfl&ftype=news&format=atom");
        final FeedEntryMessageSource rotoplayernewsSource =
            new FeedEntryMessageSource(feedUrl, "rotoplayernews");
        rotoplayernewsSource.setMetadataStore(getMetaDataStore());
        return rotoplayernewsSource;

    }

    @Bean public MetadataStore getMetaDataStore() {
        return new PropertiesPersistingMetadataStore();
    }

    @Bean public IntegrationFlow playerNewsFlow() throws MalformedURLException {

        return IntegrationFlows.from(playerNewsFeedMessageSource(),
            c -> c.poller(Pollers.fixedRate(rotoWorldPlayerNewsFeedDelay).maxMessagesPerPoll(20))
                .autoStartup(true))
            .transform(new ObjectToJsonTransformer(ObjectToJsonTransformer.ResultType.NODE))
            .transform(getPlayerNewsEnricher()).transform(getSlackMessageTransformer())
            .channel(getSlackMessageQueue()).get();

    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER) public PollerMetadata poller() {
        return Pollers.fixedDelay(1000).get();
    }

    @Bean public IntegrationFlow sendMessageFlow() {

        return IntegrationFlows.from(getSlackMessageQueue()).handle(newsHandler()).get();

    }

    @Bean public QueueChannel getSlackMessageQueue() {
        return MessageChannels.queue("slack_messages", 20).get();
    }

    @Bean public MessageChannel loggingChannel() {
        return new DirectChannel();
    }

    @Bean public GenericTransformer<JsonNode, PlayerNews> getPlayerNewsEnricher() {
        return new RotoWorldNewsEnricher();
    }

    @Bean public GenericTransformer<PlayerNews, String> getSlackMessageTransformer() {
        return new SlackMessageTransformer(getJsonMapper());
    }

    @Bean public MessageHandler newsHandler() {
        return new SlackMessenger();
    }


    @Bean(name = "jsonMapper") ObjectMapper getJsonMapper() {

        return new ObjectMapper();

    }


/*
    static class MyMessageHandler extends AbstractMessageHandler {

        private final static AtomicInteger newsCounter = new AtomicInteger(0);
        private final static ObjectMapper MAPPER = new ObjectMapper();

        @Override protected void handleMessageInternal(Message<?> message) throws Exception {
            final Object payload = message.getPayload();
            if (payload != null) {
                System.out.println("Message object received []" + newsCounter.incrementAndGet());
                System.out
                    .println(String.format("Message -> %s", MAPPER.writeValueAsString(payload)));
            }
        }
    }*/



}

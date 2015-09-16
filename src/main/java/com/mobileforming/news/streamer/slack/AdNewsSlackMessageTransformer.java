package com.mobileforming.news.streamer.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobileforming.news.streamer.slack.model.SlackMessage;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.Message;
import org.springframework.util.Assert;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jeffreypthomas on 9/7/15.
 */
public class AdNewsSlackMessageTransformer implements GenericTransformer<String, Message<?>> {
    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(AdNewsSlackMessageTransformer.class);

    private static final String MESSAGE_TEXT_TEMPLATE =
        "*_Created By Jeffrey P. Thomas_* \n For requests or enhancements, create issue @<https://github.com/rabiddroid/rotoworld-slack-news-streamer/issues|here.>";
    private static final int flagMod = 10;
    private final ObjectMapper jsonMapper;
    private final String channelName;
    private final String botName;
    private final String iconName;
    private final AtomicInteger messageCounter = new AtomicInteger(0);


    public AdNewsSlackMessageTransformer(ObjectMapper jsonMapper,
        SlackMessageSettings slackMessageSettings) {

        Assert.notNull(jsonMapper);
        Assert.hasText(slackMessageSettings.getChannelName());
        Assert.hasText(slackMessageSettings.getBotName());
        Assert.hasText(slackMessageSettings.getImojiName());

        this.jsonMapper = jsonMapper;
        this.channelName = slackMessageSettings.getChannelName();
        this.botName = slackMessageSettings.getBotName();
        this.iconName = slackMessageSettings.getImojiName();

    }



    @Override public Message<?> transform(String playerNews) {

        if (playerNews != null) {

            final int currentMessageCount = messageCounter.incrementAndGet();

            if (currentMessageCount % flagMod == 0) {
                final SlackMessage slackMessage =
                    new SlackMessageBuilder().setChannel(channelName).setUsername(botName)
                        .setText(MESSAGE_TEXT_TEMPLATE).setIconEmoji(iconName).createSlackMessage();

                try {

                    final String message = jsonMapper.writeValueAsString(slackMessage);
                    return MessageBuilder.withPayload(message).setHeader("PUBLISH", true).build();

                } catch (JsonProcessingException e) {
                    LOG.error("Unable to transform message", e);
                    return getFailedMessage();
                }
            } else
                return getFailedMessage();
        } else
            return getFailedMessage();

    }

    private Message<?> getFailedMessage() {

        return MessageBuilder.withPayload("").setHeader("PUBLISH", false).build();
    }

}

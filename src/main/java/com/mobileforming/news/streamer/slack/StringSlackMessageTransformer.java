package com.mobileforming.news.streamer.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobileforming.news.streamer.slack.model.SlackMessage;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.util.Assert;

/**
 * Created by jeffreypthomas on 9/7/15.
 */
public class StringSlackMessageTransformer implements GenericTransformer<String, String> {
    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(StringSlackMessageTransformer.class);

    private final ObjectMapper jsonMapper;
    private final String channelName;
    private final String botName;
    private final String iconName;


    public StringSlackMessageTransformer(ObjectMapper jsonMapper,
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




    @Override public String transform(String message) {

        final SlackMessage slackMessage =
            new SlackMessageBuilder().setChannel(channelName).setUsername(botName)
                .setText(message).setIconEmoji(iconName)
                .createSlackMessage();

        try {
            return jsonMapper.writeValueAsString(slackMessage);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to transform message", e);
            return null;
        }

    }

}

package com.mobileforming.news.streamer.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobileforming.news.streamer.data.model.PlayerNews;
import com.mobileforming.news.streamer.slack.model.SlackMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.transformer.GenericTransformer;

/**
 * Created by jeffreypthomas on 9/7/15.
 */
public class SlackMessageTransformer implements GenericTransformer<PlayerNews, String> {
    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(SlackMessageTransformer.class);
    private static final String DEFAULT_CHANNEL = "@jeffrey";
    private static final String DEFAULT_SLACKBOT_NAME = "RotoBot";
    private static final String DEFAULT_ICON_NAME = ":football:";
    private static final String MESSAGE_TEXT_TEMPLATE = "*_%s_* \n %s \n <%s|Read more.>";

    private final ObjectMapper jsonMapper;

    @Autowired public SlackMessageTransformer(ObjectMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    @Override public String transform(PlayerNews playerNews) {

        final String channel = DEFAULT_CHANNEL;
        final String username = DEFAULT_SLACKBOT_NAME;
        final String text = getMessageBody(playerNews);
        final String iconEmoji = DEFAULT_ICON_NAME;

        final SlackMessage slackMessage =
            new SlackMessageBuilder().setChannel(channel).setUsername(username).setText(text)
                .setIconEmoji(iconEmoji).createSlackMessage();

        try {
            return jsonMapper.writeValueAsString(slackMessage);
        } catch (JsonProcessingException e) {
            LOG.error("Unable to transform message", e);
            return null;
        }

    }

    private String getMessageBody(PlayerNews playerNews) {
        return String.format(MESSAGE_TEXT_TEMPLATE, playerNews.getHeadline(), playerNews.getBody(),
            playerNews.getLink());
    }
}

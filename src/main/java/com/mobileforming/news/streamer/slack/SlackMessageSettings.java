package com.mobileforming.news.streamer.slack;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by jeffreypthomas on 9/13/15.
 */
@Component
@ConfigurationProperties(prefix="slack.message")
public class SlackMessageSettings {

    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(SlackMessageSettings.class);

    private String channelName;
    private String botName;
    private String imojiName;


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getBotName() {
        return botName;
    }

    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getImojiName() {
        return imojiName;
    }

    public void setImojiName(String imojiName) {
        this.imojiName = imojiName;
    }
}



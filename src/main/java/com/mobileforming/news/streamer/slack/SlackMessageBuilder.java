package com.mobileforming.news.streamer.slack;

import com.mobileforming.news.streamer.slack.model.SlackMessage;

public class SlackMessageBuilder {
    private String channel;
    private String username;
    private String text;
    private String iconEmoji;

    public SlackMessageBuilder setChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public SlackMessageBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public SlackMessageBuilder setText(String text) {
        this.text = text;
        return this;
    }

    public SlackMessageBuilder setIconEmoji(String iconEmoji) {
        this.iconEmoji = iconEmoji;
        return this;
    }

    public SlackMessage createSlackMessage() {
        return new SlackMessage(channel, username, text, iconEmoji);
    }
}

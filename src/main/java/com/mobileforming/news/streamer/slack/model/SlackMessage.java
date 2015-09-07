package com.mobileforming.news.streamer.slack.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by jeffreypthomas on 9/7/15.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "channel",
    "username",
    "text",
    "icon_emoji"
})
public class SlackMessage {

    private static final org.slf4j.Logger LOG =
        org.slf4j.LoggerFactory.getLogger(SlackMessage.class);

    @JsonProperty("channel")
    private String channel;
    @JsonProperty("username")
    private String username;
    @JsonProperty("text")
    private String text;
    @JsonProperty("icon_emoji")
    private String iconEmoji;

    public SlackMessage(String channel, String username, String text, String iconEmoji) {
        this.channel = channel;
        this.username = username;
        this.text = text;
        this.iconEmoji = iconEmoji;
    }

    /**
     *
     * @return
     * The channel
     */
    @JsonProperty("channel")
    public String getChannel() {
        return channel;
    }

    /**
     *
     * @param channel
     * The channel
     */
    @JsonProperty("channel")
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     *
     * @return
     * The username
     */
    @JsonProperty("username")
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     * The username
     */
    @JsonProperty("username")
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     * The text
     */
    @JsonProperty("text")
    public String getText() {
        return text;
    }

    /**
     *
     * @param text
     * The text
     */
    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    /**
     *
     * @return
     * The iconEmoji
     */
    @JsonProperty("icon_emoji")
    public String getIconEmoji() {
        return iconEmoji;
    }

    /**
     *
     * @param iconEmoji
     * The icon_emoji
     */
    @JsonProperty("icon_emoji")
    public void setIconEmoji(String iconEmoji) {
        this.iconEmoji = iconEmoji;
    }



}

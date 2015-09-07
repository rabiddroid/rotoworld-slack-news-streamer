package com.mobileforming.news.streamer.data.model;

import java.net.URL;

/**
 * Created by jeffreypthomas on 9/6/15.
 */
public class PlayerNews {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(PlayerNews.class);


    private Player player;
    private String headline;
    private String body;
    private URL link;

    public PlayerNews(Player player, String headline, String body, URL link) {
        this.player = player;
        this.headline = headline;
        this.body = body;
        this.link = link;
    }

    public static PlayerNews empty() {

        return null;
    }

    public Player getPlayer() {
        return player;
    }

    public String getHeadline() {
        return headline;
    }

    public String getBody() {
        return body;
    }

    public URL getLink() {
        return link;
    }
}

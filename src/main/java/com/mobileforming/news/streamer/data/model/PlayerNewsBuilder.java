package com.mobileforming.news.streamer.data.model;

import java.net.URL;

public class PlayerNewsBuilder {
    private Player player;
    private String headline;
    private String body;
    private URL link;

    public PlayerNewsBuilder setPlayer(Player player) {
        this.player = player;
        return this;
    }

    public PlayerNewsBuilder setHeadline(String headline) {
        this.headline = headline;
        return this;
    }

    public PlayerNewsBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public PlayerNewsBuilder setLink(URL link) {
        this.link = link;
        return this;
    }

    public PlayerNews createPlayerNews() {
        return new PlayerNews(player, headline, body, link);
    }
}

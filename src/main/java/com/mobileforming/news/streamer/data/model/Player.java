package com.mobileforming.news.streamer.data.model;

/**
 * Created by jeffreypthomas on 9/6/15.
 */
public class Player {

    private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Player.class);

    private String name;
    private String position;
    private String team;

    public Player(String name, String position, String team) {
        this.name = name;
        this.position = position;
        this.team = team;
    }

    public String getName() {
        return name;
    }

    public String getPosition() {
        return position;
    }

    public String getTeam() {
        return team;
    }
}

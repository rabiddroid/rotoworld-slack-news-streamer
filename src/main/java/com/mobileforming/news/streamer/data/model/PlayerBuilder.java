package com.mobileforming.news.streamer.data.model;

public class PlayerBuilder {
    private String name;
    private String position;
    private String team;

    public PlayerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PlayerBuilder setPosition(String position) {
        this.position = position;
        return this;
    }

    public PlayerBuilder setTeam(String team) {
        this.team = team;
        return this;
    }

    public Player createPlayer() {
        return new Player(name, position, team);
    }
}

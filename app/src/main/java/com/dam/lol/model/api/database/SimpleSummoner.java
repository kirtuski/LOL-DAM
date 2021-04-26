package com.dam.lol.model.api.database;

public class SimpleSummoner {
    private String name;
    private String server;

    public SimpleSummoner(String name, String server) {
        this.name = name;
        this.server = server;
    }

    public String getName() {
        return name;
    }

    public String getServer() {
        return server;
    }

}

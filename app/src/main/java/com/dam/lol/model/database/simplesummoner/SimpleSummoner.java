package com.dam.lol.model.database.simplesummoner;

public class SimpleSummoner {
    private final String name;
    private final String server;

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

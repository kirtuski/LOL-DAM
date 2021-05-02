package com.dam.lol.model.api;

import java.io.Serializable;

public class SummonerResponse implements Serializable {
    private final String id;
    private final String puuid;
    private final String name;
    private final String server;
    private final int profileIconId;
    private final int summonerLevel;
    private final String serverV5;

    public SummonerResponse(InvocadorResponseBuilder invocadorResponseBuilder) {
        this.id = invocadorResponseBuilder.id;
        this.puuid = invocadorResponseBuilder.puuid;
        this.name = invocadorResponseBuilder.name;
        this.profileIconId = invocadorResponseBuilder.profileIconId;
        this.summonerLevel = invocadorResponseBuilder.summonerLevel;
        this.server = invocadorResponseBuilder.server;
        this.serverV5 = convertServerToV5(this.server);
    }

    public String getId() {
        return id;
    }

    public String getPuuid() {
        return puuid;
    }

    public String getName() {
        return name;
    }

    public int getProfileIconId() {
        return profileIconId;
    }

    public int getSummonerLevel() {
        return summonerLevel;
    }

    public String getServer() {
        return server;
    }

    public String convertServerToV5(String server) {
        if (server.equals("na1") || server.equals("br1") || server.equals("la1") || server.equals("la2") || server.equals("oc1")) {
            return "americas";
        } else if (server.equals("jp1") || server.equals("kr")) {
            return "asia";
        } else {
            return "europe";
        }

    }

    public String getServerV5() {
        return serverV5;
    }

    public static class InvocadorResponseBuilder {
        private String id;
        private String puuid;
        private String name;
        private String server;
        private int profileIconId = 0;
        private int summonerLevel = 0;

        public SummonerResponse build() {
            return new SummonerResponse(this);
        }

        public InvocadorResponseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public InvocadorResponseBuilder puuid(String puuid) {
            this.puuid = puuid;
            return this;
        }

        public InvocadorResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public InvocadorResponseBuilder profileIconId(int profileIconId) {
            this.profileIconId = profileIconId;
            return this;
        }

        public InvocadorResponseBuilder summonerLevel(int summonerLevel) {
            this.summonerLevel = summonerLevel;
            return this;
        }

        public InvocadorResponseBuilder server(String server) {
            this.server = server;
            return this;
        }
    }
}

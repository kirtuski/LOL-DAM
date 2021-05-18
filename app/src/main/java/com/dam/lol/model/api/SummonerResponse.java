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

    public SummonerResponse(SummonerResponseBuilder summonerResponseBuilder) {
        this.id = summonerResponseBuilder.id;
        this.puuid = summonerResponseBuilder.puuid;
        this.name = summonerResponseBuilder.name;
        this.profileIconId = summonerResponseBuilder.profileIconId;
        this.summonerLevel = summonerResponseBuilder.summonerLevel;
        this.server = summonerResponseBuilder.server;
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

    public static class SummonerResponseBuilder {
        private String id;
        private String puuid;
        private String name;
        private String server;
        private int profileIconId = 0;
        private int summonerLevel = 0;

        public SummonerResponse build() {
            return new SummonerResponse(this);
        }

        public SummonerResponseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public SummonerResponseBuilder puuid(String puuid) {
            this.puuid = puuid;
            return this;
        }

        public SummonerResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public SummonerResponseBuilder profileIconId(int profileIconId) {
            this.profileIconId = profileIconId;
            return this;
        }

        public SummonerResponseBuilder summonerLevel(int summonerLevel) {
            this.summonerLevel = summonerLevel;
            return this;
        }

        public SummonerResponseBuilder server(String server) {
            this.server = server;
            return this;
        }
    }
}

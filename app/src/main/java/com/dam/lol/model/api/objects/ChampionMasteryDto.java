package com.dam.lol.model.api.objects;

public class ChampionMasteryDto {
    private long championPointsUtilNextLevel;
    private boolean chestGranted;
    private int championId;
    private long lastPlayTime;
    private int championLevel;
    private String summonerId;
    private int championPoints;
    private long championPointsSinceLastLevel;
    private int tokensEarned;

    public ChampionMasteryDto() {
    }

    public long getChampionPointsUtilNextLevel() {
        return championPointsUtilNextLevel;
    }

    public void setChampionPointsUtilNextLevel(long championPointsUtilNextLevel) {
        this.championPointsUtilNextLevel = championPointsUtilNextLevel;
    }

    public boolean isChestGranted() {
        return chestGranted;
    }

    public void setChestGranted(boolean chestGranted) {
        this.chestGranted = chestGranted;
    }

    public int getChampionId() {
        return championId;
    }

    public void setChampionId(int championId) {
        this.championId = championId;
    }

    public long getLastPlayTime() {
        return lastPlayTime;
    }

    public void setLastPlayTime(long lastPlayTime) {
        this.lastPlayTime = lastPlayTime;
    }

    public int getChampionLevel() {
        return championLevel;
    }

    public void setChampionLevel(int championLevel) {
        this.championLevel = championLevel;
    }

    public String getSummonerId() {
        return summonerId;
    }

    public void setSummonerId(String summonerId) {
        this.summonerId = summonerId;
    }

    public int getChampionPoints() {
        return championPoints;
    }

    public void setChampionPoints(int championPoints) {
        this.championPoints = championPoints;
    }

    public long getChampionPointsSinceLastLevel() {
        return championPointsSinceLastLevel;
    }

    public void setChampionPointsSinceLastLevel(long championPointsSinceLastLevel) {
        this.championPointsSinceLastLevel = championPointsSinceLastLevel;
    }

    public int getTokensEarned() {
        return tokensEarned;
    }

    public void setTokensEarned(int tokensEarned) {
        this.tokensEarned = tokensEarned;
    }




}

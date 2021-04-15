package com.dam.lol;

public class Invocador {
    private String id;
    private String puuid;
    private String name;
    private int profileIconId;
    private int summonerLevel;

    public String getId() {
        return id;
    }

    public void setId( String id){
        this.id = id;
    }

    public String getPuuid() {
        return puuid;
    }

    public void setPuuid( String puuid){
        this.puuid = puuid;
    }

    public String getName(){
        return name;
    }

    public void setName( String name){
        this.name = name;
    }

    public int getProfileIconId(){
        return profileIconId;
    }

    public void setProfileIconId( int profileIconId){
        this.profileIconId = profileIconId;
    }

    public int getSummonerLevel(){
        return summonerLevel;
    }

    public void setSummonerLevel(int summonerLevel){
        this.summonerLevel = summonerLevel;
    }
}

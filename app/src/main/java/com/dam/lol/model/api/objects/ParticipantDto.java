package com.dam.lol.model.api.objects;

public class ParticipantDto {
    private int assists;
    private int championLevel;
    private int championId;
    private int deaths;
    private int largestKillingSpree;
    private int largestMultiKill;
    private double longestTimeSpentLiving; //Tiempo sin morir
    private int participantId; //Id dentro de la partida
    private String puuid;
    private int summoner1Id;
    private int summoner2Id;
    private int summonerName;
    private int teamId; // 100 o 200
    private String teamPosition;
    private int totalMinionsKilled;
    private boolean win;

    //TODO gets, sets, constructor, builder?
}

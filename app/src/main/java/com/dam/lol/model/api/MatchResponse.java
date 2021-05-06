package com.dam.lol.model.api;

import com.dam.lol.model.api.dto.ParticipantDto;

import java.io.Serializable;
import java.util.ArrayList;

public class MatchResponse implements Serializable {
    private String matchId;
    private double gameCreation;
    private double gameDuration;
    private int queueId;
    private ArrayList<ParticipantDto> participants;

    public MatchResponse(String matchId, double gameCreation, double gameDuration, int queueId, ArrayList<ParticipantDto> participants) {
        this.matchId = matchId;
        this.gameCreation = gameCreation;
        this.gameDuration = gameDuration;
        this.queueId = queueId;
        this.participants = participants;
    }

    public String getMatchId() {
        return matchId;
    }

    public void setMatchId(String matchId) {
        this.matchId = matchId;
    }

    public double getGameCreation() {
        return gameCreation;
    }

    public void setGameCreation(double gameCreation) {
        this.gameCreation = gameCreation;
    }

    public double getGameDuration() {
        return gameDuration;
    }

    public void setGameDuration(double gameDuration) {
        this.gameDuration = gameDuration;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public ArrayList<ParticipantDto> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<ParticipantDto> participants) {
        this.participants = participants;
    }
}


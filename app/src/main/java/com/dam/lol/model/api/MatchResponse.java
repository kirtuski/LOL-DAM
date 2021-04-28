package com.dam.lol.model.api;

import com.dam.lol.model.api.objects.ParticipantDto;

import java.io.Serializable;
import java.util.ArrayList;

// TODO Clase con los campos minimos para mostrar, merece la pena ampliar para mostrar más info?
public class MatchResponse implements Serializable {
    //TODO Builder to wapo?
    private String matchId;
    private double gameCreation;
    private double gameDuration;
    // TODO Hace referencia al modo de juego, hay que parsear info de queues.json
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


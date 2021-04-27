package com.dam.lol.model.api;

import com.dam.lol.model.api.objects.ParticipantDto;

import java.io.Serializable;
import java.util.ArrayList;

// TODO Clase con los campos minimos para mostrar, merece la pena ampliar para mostrar más info?
public class MatchResponse implements Serializable {
    private String matchId;
    private double gameCreation;
    private double gameDuration;
    // TODO Hay que ver cual hace referencia al modo de juego y borrar el resto para simplificar?
    private String gameMode;
    private String gameType;
    private int mapId; //Creo que es este
    private ArrayList<ParticipantDto> participants;
    private int queueId; //400¿?

    //TODO gets, sets, constructor, builder?
}


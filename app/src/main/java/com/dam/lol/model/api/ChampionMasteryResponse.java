package com.dam.lol.model.api;

import com.dam.lol.model.api.dto.ChampionMasteryDto;

import java.io.Serializable;
import java.util.List;

public class ChampionMasteryResponse implements Serializable {
    List<ChampionMasteryDto> championMasteryDtoList;

    public ChampionMasteryResponse() {
    }

    public List<ChampionMasteryDto> getChampionMasteryDtoList() {
        return championMasteryDtoList;
    }

    public void setChampionMasteryDtoList(List<ChampionMasteryDto> championMasteryDtoList) {
        this.championMasteryDtoList = championMasteryDtoList;
    }

}

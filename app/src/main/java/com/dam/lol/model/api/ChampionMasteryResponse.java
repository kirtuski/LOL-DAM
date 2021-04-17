package com.dam.lol.model.api;

import com.dam.lol.model.api.objects.ChampionMasteryDto;

import java.util.List;

public class ChampionMasteryResponse {
    public List<ChampionMasteryDto> getChampionMasteryDtoList() {
        return championMasteryDtoList;
    }

    public ChampionMasteryResponse() {
    }

    public void setChampionMasteryDtoList(List<ChampionMasteryDto> championMasteryDtoList) {
        this.championMasteryDtoList = championMasteryDtoList;
    }

    List<ChampionMasteryDto> championMasteryDtoList;

}

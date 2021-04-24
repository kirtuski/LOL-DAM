package com.dam.lol.model.api;

import com.dam.lol.model.api.objects.ChampionMasteryDto;
import com.dam.lol.model.api.objects.LeagueDto;

import java.io.Serializable;
import java.util.List;

public class LeagueResponse implements Serializable {
    List<LeagueDto> leagueDtoList;

    public LeagueResponse() {
    }

    public LeagueResponse(List<LeagueDto> leagueDtoList){
        this.leagueDtoList = leagueDtoList;
    }

    public List<LeagueDto> getLeagueDtoList() {
        return leagueDtoList;
    }

    public void setleagueDtoList(List<LeagueDto> leagueDtoList) {
        this.leagueDtoList = leagueDtoList;
    }

}

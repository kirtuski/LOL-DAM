package com.dam.lol.model.api;

import java.util.ArrayList;

public class MatchListResponse {
    private ArrayList<String> matchList;

    public ArrayList<String> getMatchList() {
        return matchList;
    }

    public void setMatchList(ArrayList<String> matchList) {
        this.matchList = matchList;
    }

    public MatchListResponse(ArrayList<String> matchList) {
        this.matchList = matchList;
    }
}

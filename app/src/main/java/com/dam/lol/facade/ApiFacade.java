package com.dam.lol.facade;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.activities.ChampionRotationActivity;
import com.dam.lol.activities.SummonerActivity;
import com.dam.lol.model.api.ChampionMasteryResponse;
import com.dam.lol.model.api.ChampionRotationResponse;
import com.dam.lol.model.api.LeagueResponse;
import com.dam.lol.model.api.MatchListResponse;
import com.dam.lol.model.api.MatchResponse;
import com.dam.lol.model.api.SummonerResponse;
import com.dam.lol.model.api.dto.ChampionMasteryDto;
import com.dam.lol.model.api.dto.LeagueDto;
import com.dam.lol.model.api.dto.ParticipantDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ApiFacade {
    private String apiKey;
    public void setApiKey(String  apiKey){
        this.apiKey = apiKey;
    }

    public ApiFacade(String api_key) {
        this.apiKey = api_key;
    }

    public void getIdFromSummoner(String name, String server, Activity activity) {

        final String URL = "https://" + server + ".api.riotgames.com/lol/summoner/v4/summoners/by-name/" + name + "?api_key=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, response -> {
                    Log.d("Volley", response.toString());
                    try {
                        SummonerResponse summonerResponse = new SummonerResponse.InvocadorResponseBuilder()
                                .id(response.getString("id"))
                                .puuid(response.getString("puuid"))
                                .name(response.getString("name"))
                                .profileIconId(response.getInt("profileIconId"))
                                .summonerLevel(response.getInt("summonerLevel"))
                                .server(server)
                                .build();

                        Intent intent = new Intent(activity, SummonerActivity.class);
                        intent.putExtra("datos", summonerResponse);
                        activity.startActivity(intent);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    VolleyLog.e("Error", error.getMessage());
                    if (error.networkResponse.statusCode == 403)
                        Toast.makeText(activity, activity.getString(R.string.error_key), Toast.LENGTH_SHORT).show();
                    if (error.networkResponse.statusCode == 404)
                        Toast.makeText(activity, activity.getString(R.string.error_no_summoner), Toast.LENGTH_SHORT).show();
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getChampionsRotation(String server, Activity activity) {

        final String URL = "https://" + server + ".api.riotgames.com/lol/platform/v3/champion-rotations?api_key=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, response -> {
                    Log.d("Volley", response.toString());
                    ChampionRotationResponse championRotationResponse = new ChampionRotationResponse();
                    try {
                        championRotationResponse.setMaxNewPlayerLevel(response.getInt("maxNewPlayerLevel"));

                        JSONArray freeChampionsIdsJSON = response.getJSONArray("freeChampionIds");
                        List<Integer> freeChampionsIds = new ArrayList<>();
                        for (int i = 0; i < freeChampionsIdsJSON.length(); ++i) {
                            freeChampionsIds.add(freeChampionsIdsJSON.getInt(i));
                        }
                        championRotationResponse.setFreeChampionIds(freeChampionsIds);

                        JSONArray freeChampionIdsForNewPlayersJSON = response.getJSONArray("freeChampionIdsForNewPlayers");
                        List<Integer> freeChampionIdsForNewPlayers = new ArrayList<>();
                        for (int i = 0; i < freeChampionIdsForNewPlayersJSON.length(); ++i) {
                            freeChampionIdsForNewPlayers.add(freeChampionIdsForNewPlayersJSON.getInt(i));
                        }
                        championRotationResponse.setFreeChampionIdsForNewPlayers(freeChampionIdsForNewPlayers);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(activity, ChampionRotationActivity.class);
                    intent.putExtra("ChampionRotationResponse", championRotationResponse);
                    activity.startActivity(intent);

                }, error -> {
                    VolleyLog.e("Error", error.getMessage());
                    if (error.networkResponse.statusCode == 403)
                        Toast.makeText(activity, activity.getString(R.string.error_key), Toast.LENGTH_SHORT).show();
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getChampionsMastery(String encryptedSummonerId, String server, SummonerActivity activity) {

        final String URL = "https://" + server + ".api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/" + encryptedSummonerId + "?api_key=" + apiKey;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, response -> {
                    Log.d("Volley", response.toString());
                    try {
                        List<ChampionMasteryDto> championMasteryDtos = new ArrayList<>();
                        for (int i = 0; i < response.length(); ++i) {
                            ChampionMasteryDto championMasteryDto = new ChampionMasteryDto();

                            JSONObject championMasteryDtoJSON = response.getJSONObject(i);
                            championMasteryDto.setChampionPointsUtilNextLevel(championMasteryDtoJSON.getLong("championPointsUntilNextLevel"));
                            championMasteryDto.setChestGranted(championMasteryDtoJSON.getBoolean("chestGranted"));
                            championMasteryDto.setChampionId(championMasteryDtoJSON.getInt("championId"));
                            championMasteryDto.setLastPlayTime(championMasteryDtoJSON.getLong("lastPlayTime"));
                            championMasteryDto.setChampionLevel(championMasteryDtoJSON.getInt("championLevel"));
                            championMasteryDto.setSummonerId(championMasteryDtoJSON.getString("summonerId"));

                            championMasteryDtos.add(championMasteryDto);
                        }
                        ChampionMasteryResponse championMasteryResponse = new ChampionMasteryResponse();
                        championMasteryResponse.setChampionMasteryDtoList(championMasteryDtos);

                        activity.loadChampionMastery(championMasteryResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    VolleyLog.e("Error", error.getMessage());
                    if (error.networkResponse.statusCode == 403)
                        Toast.makeText(activity, activity.getString(R.string.error_key), Toast.LENGTH_SHORT).show();
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getSummonerLeague(String encryptedSummonerId, String server, SummonerActivity activity) {
        final String URL = "https://" + server + ".api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=" + apiKey;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, response -> {
                    Log.d("Volley", response.toString());
                    try {
                        List<LeagueDto> leagueDtos = new ArrayList<>();
                        for (int i = 0; i < response.length(); ++i) {
                            LeagueDto leagueDto = new LeagueDto();
                            JSONObject leaguesDtosDtoJSON = response.getJSONObject(i);

                            leagueDto.setLeagueId(leaguesDtosDtoJSON.getString("leagueId"));
                            leagueDto.setQueueType(leaguesDtosDtoJSON.getString("queueType"));
                            leagueDto.setTier(leaguesDtosDtoJSON.getString("tier"));
                            leagueDto.setRank(leaguesDtosDtoJSON.getString("rank"));
                            leagueDto.setSummonerId(leaguesDtosDtoJSON.getString("summonerId"));
                            leagueDto.setSummonerName(leaguesDtosDtoJSON.getString("summonerName"));
                            leagueDto.setLeaguePoints(leaguesDtosDtoJSON.getInt("leaguePoints"));
                            leagueDto.setWins(leaguesDtosDtoJSON.getInt("wins"));
                            leagueDto.setLosses(leaguesDtosDtoJSON.getInt("losses"));
                            leagueDto.setVeteran(leaguesDtosDtoJSON.getBoolean("veteran"));
                            leagueDto.setInactive(leaguesDtosDtoJSON.getBoolean("inactive"));
                            leagueDto.setFreshBlood(leaguesDtosDtoJSON.getBoolean("freshBlood"));
                            leagueDto.setHotStreak(leaguesDtosDtoJSON.getBoolean("hotStreak"));
                            leagueDtos.add(leagueDto);
                        }

                        LeagueResponse leagueResponse = new LeagueResponse(leagueDtos);

                        activity.loadLeagueInfo(leagueResponse);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    VolleyLog.e("Error", error.getMessage());
                    if (error.networkResponse.statusCode == 403)
                        Toast.makeText(activity, activity.getString(R.string.error_key), Toast.LENGTH_SHORT).show();
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getMatchById(String matchId, String serverV5, SummonerActivity activity) {
        final String URL = "https://" + serverV5 + ".api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + apiKey;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, response -> {
                    Log.d("Volley", response.toString());
                    try {
                        String matchId1 = response.getJSONObject("metadata").getString("matchId");
                        double gameCreation = response.getJSONObject("info").getDouble("gameCreation");
                        double gameDuration = response.getJSONObject("info").getDouble("gameDuration");
                        int queueId = response.getJSONObject("info").getInt("queueId");
                        ArrayList<ParticipantDto> participants = new ArrayList<>();

                        for (int i = 0; i < response.getJSONObject("info").getJSONArray("participants").length(); i++) {
                            JSONObject oneParticipant = response.getJSONObject("info").getJSONArray("participants").getJSONObject(i);
                            ParticipantDto participantDto = new ParticipantDto();
                            participantDto.setSummonerName(oneParticipant.getString("summonerName"));
                            participantDto.setPuuid(oneParticipant.getString("puuid"));
                            participantDto.setChampionId(oneParticipant.getInt("championId"));
                            participantDto.setChampionLevel(oneParticipant.getInt("champLevel"));
                            participantDto.setTeamPosition(oneParticipant.getString("teamPosition"));
                            participantDto.setSummoner1Id(oneParticipant.getInt("summoner1Id"));
                            participantDto.setSummoner2Id(oneParticipant.getInt("summoner2Id"));
                            participantDto.setKills(oneParticipant.getInt("kills"));
                            participantDto.setDeaths(oneParticipant.getInt("deaths"));
                            participantDto.setAssists(oneParticipant.getInt("assists"));
                            participantDto.setTotalMinionsKilled(oneParticipant.getInt("totalMinionsKilled"));
                            participantDto.setLargestKillingSpree(oneParticipant.getInt("largestKillingSpree"));
                            participantDto.setLargestMultiKill(oneParticipant.getInt("largestMultiKill"));
                            participantDto.setLongestTimeSpentLiving(oneParticipant.getInt("longestTimeSpentLiving"));
                            participantDto.setTeamId(oneParticipant.getInt("teamId"));
                            participantDto.setParticipantId(oneParticipant.getInt("participantId"));
                            participantDto.setWin(oneParticipant.getBoolean("win"));
                            participantDto.setItem0(oneParticipant.getInt("item0"));
                            participantDto.setItem0(oneParticipant.getInt("item1"));
                            participantDto.setItem0(oneParticipant.getInt("item2"));
                            participantDto.setItem0(oneParticipant.getInt("item3"));
                            participantDto.setItem0(oneParticipant.getInt("item4"));
                            participantDto.setItem0(oneParticipant.getInt("item5"));
                            participantDto.setItem0(oneParticipant.getInt("item6"));
                            participants.add(participantDto);
                        }
                        activity.loadMatchInActivity(new MatchResponse(matchId1, gameCreation, gameDuration, queueId, participants));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    VolleyLog.e("Error", error.getMessage());
                    if (error.networkResponse.statusCode == 403)
                        Toast.makeText(activity, activity.getString(R.string.error_key), Toast.LENGTH_SHORT).show();
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getMatchListByPuuid(String summonerPuuid, String serverV5, int start, int count, SummonerActivity activity) {
        final String URL = "https://" + serverV5 + ".api.riotgames.com/lol/match/v5/matches/by-puuid/" + summonerPuuid + "/ids?start=" + start + "&count=" + count + "&api_key=" + apiKey;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, response -> {
                    Log.d("Volley", response.toString());
                    try {
                        ArrayList<String> matchList = new ArrayList<>();
                        for (int i = 0; i < response.length(); ++i)
                            matchList.add(response.getString(i));

                        activity.findMatches(new MatchListResponse(matchList));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> {
                    VolleyLog.e("Error", error.getMessage());
                    if (error.networkResponse.statusCode == 403)
                        Toast.makeText(activity, activity.getString(R.string.error_key), Toast.LENGTH_SHORT).show();
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

}
package com.dam.lol.facade;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dam.lol.activities.InvocadorActivity;
import com.dam.lol.LolApplication;
import com.dam.lol.activities.ChampionRotationActivity;
import com.dam.lol.model.api.ChampionMasteryResponse;
import com.dam.lol.model.api.ChampionRotationResponse;
import com.dam.lol.model.api.LeagueResponse;
import com.dam.lol.model.api.MatchListResponse;
import com.dam.lol.model.api.MatchResponse;
import com.dam.lol.model.api.SummonerResponse;
import com.dam.lol.model.api.objects.ChampionMasteryDto;
import com.dam.lol.model.api.objects.LeagueDto;
import com.dam.lol.model.api.objects.ParticipantDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ApiFacade {
    final private String api_key;

    public ApiFacade(String api_key) {
        this.api_key = api_key;
    }

    public void getIdFromSummoner(String nombre, String servidor, Activity activity) {

        final String URL = "https://" + servidor + ".api.riotgames.com/lol/summoner/v4/summoners/by-name/" + nombre + "?api_key=" + api_key;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", response.toString());
                        try {
                            SummonerResponse invocador  = new SummonerResponse.InvocadorResponseBuilder()
                                    .id(response.getString("id"))
                                    .puuid(response.getString("puuid"))
                                    .name(response.getString("name"))
                                    .profileIconId(response.getInt("profileIconId"))
                                    .summonerLevel(response.getInt("summonerLevel"))
                                    .server(servidor)
                                    .build();

                            Intent intent = new Intent(activity, InvocadorActivity.class);
                            intent.putExtra("datos", invocador);
                            activity.startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
                        if( error.networkResponse.statusCode == 403)
                            Toast.makeText(activity, "La api key no es correcta", Toast.LENGTH_SHORT).show();
                        if( error.networkResponse.statusCode == 404)
                            Toast.makeText(activity, "No existe el nombre de invocador", Toast.LENGTH_SHORT).show();
                    }
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getChampionsRotation(String servidor, Activity activity) {

        final String URL = "https://" + servidor + ".api.riotgames.com/lol/platform/v3/champion-rotations?api_key=" + api_key;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
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

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
                        if( error.networkResponse.statusCode == 403)
                            Toast.makeText(activity, "La api key no es correcta", Toast.LENGTH_SHORT).show();
                    }
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getChampionsMastery(String encryptedSummonerId, String servidor, InvocadorActivity activity) {

        final String URL = "https://" + servidor + ".api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/" + encryptedSummonerId + "?api_key=" + api_key;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", response.toString());
                        try {
                            List<ChampionMasteryDto> championMasteryDtos = new ArrayList<ChampionMasteryDto>();
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

                            activity.ponChampionMastery(championMasteryResponse);


                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
                        if( error.networkResponse.statusCode == 403)
                            Toast.makeText(activity, "La api key no es correcta", Toast.LENGTH_SHORT).show();
                    }
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getSummonerLeague(String encryptedSummonerId, String servidor, InvocadorActivity activity) {
        final String URL = "https://" + servidor + ".api.riotgames.com/lol/league/v4/entries/by-summoner/" + encryptedSummonerId + "?api_key=" + api_key;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
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

                            activity.ponLeagueInfo(leagueResponse);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
                        if( error.networkResponse.statusCode == 403)
                            Toast.makeText(activity, "La api key no es correcta", Toast.LENGTH_SHORT).show();
                    }
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getMatchById(String matchId, String servidor, InvocadorActivity activity) {
        //TODO servidor ha cambiado, mirar siguietne función para + info
        servidor = "europe";
        final String URL ="https://" + servidor + ".api.riotgames.com/lol/match/v5/matches/" + matchId + "?api_key=" + api_key;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", response.toString());
                        try {
                            String matchId = response.getJSONObject("metadata").getString("matchId");
                            double gameCreation = response.getJSONObject("info").getDouble("gameCreation");
                            double gameDuration = response.getJSONObject("info").getDouble("gameDuration");
                            int queueId  = response.getJSONObject("info").getInt("queueId");
                            ArrayList<ParticipantDto> participants = new ArrayList<>();

                            for( int i = 0 ; i < response.getJSONObject("info").getJSONArray("participants").length() ; i++){
                                JSONObject oneParticipant = response.getJSONObject("info").getJSONArray("participants").getJSONObject(i);
                                ParticipantDto participantDto = new ParticipantDto();
                                participantDto.setSummonerName(oneParticipant.getString("summonerName") );
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
                                participants.add(participantDto);
                            }

                            activity.ponPartidaEnActivity(new MatchResponse(matchId, gameCreation, gameDuration, queueId, participants));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
                        if( error.networkResponse.statusCode == 403)
                            Toast.makeText(activity, "La api key no es correcta", Toast.LENGTH_SHORT).show();
                    }
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }


    public void getMatchListByPuuid(String summonerPuuid, String servidor, InvocadorActivity activity) {
        getMatchListByPuuid(summonerPuuid,  servidor, 0, 2, activity);
    }

    public void getMatchListByPuuid(String summonerPuuid, String servidor, int start, int count,InvocadorActivity activity) {
        //TODO servidor ha cambiado
        // The AMERICAS routing value serves NA, BR, LAN, LAS, and OCE. The ASIA routing value serves KR and JP. The EUROPE routing value serves EUNE, EUW, TR, and RU.
        //  Hacer apaño
        servidor = "europe";

        final String URL = "https://" + servidor + ".api.riotgames.com/lol/match/v5/matches/by-puuid/"+ summonerPuuid +"/ids?start="+ start +"&count=" + count + "&api_key=" + api_key;

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Volley", response.toString());
                        try {
                            ArrayList<String> matchList = new ArrayList<>();
                            for (int i = 0; i < response.length(); ++i)
                                matchList.add(response.getString(i));

                            activity.buscaPartidas(new MatchListResponse(matchList));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
                        if( error.networkResponse.statusCode == 403)
                            Toast.makeText(activity, "La api key no es correcta", Toast.LENGTH_SHORT).show();
                    }
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

}
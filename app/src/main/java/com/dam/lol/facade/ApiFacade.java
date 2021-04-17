package com.dam.lol.facade;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.dam.lol.LolApplication;
import com.dam.lol.activities.SettingsActivity;
import com.dam.lol.model.api.ChampionMasteryResponse;
import com.dam.lol.model.api.ChampionRotationResponse;
import com.dam.lol.model.api.SummonerResponse;
import com.dam.lol.model.api.objects.ChampionMasteryDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                                    .build();

                            Intent intent = new Intent(activity, SettingsActivity.class);
                            //Podemos pasar informacion entre actividades con el intent
                            intent.putExtra("parametro", 2);
                            activity.startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
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
                        try {
                            ChampionRotationResponse championRotationResponse = new ChampionRotationResponse();
                            championRotationResponse.setMaxNewPlayerLevel(response.getInt("maxNewPlayerLevel"));

                            JSONArray freeChampionsIdsJSON = response.getJSONArray("freeChampionsIds");
                            List<Integer> freeChampionsIds = new ArrayList<Integer>();
                            for (int i = 0; i < freeChampionsIdsJSON.length(); ++i) {
                                freeChampionsIds.add(freeChampionsIdsJSON.getInt(i));
                            }
                            championRotationResponse.setFreeChampionIds(freeChampionsIds);

                            JSONArray freeChampionIdsForNewPlayersJSON = response.getJSONArray("freeChampionIdsForNewPlayers");
                            List<Integer> freeChampionIdsForNewPlayers = new ArrayList<Integer>();
                            for (int i = 0; i < freeChampionIdsForNewPlayersJSON.length(); ++i) {
                                freeChampionIdsForNewPlayers.add(freeChampionIdsForNewPlayersJSON.getInt(i));
                            }
                            championRotationResponse.setFreeChampionIdsForNewPlayers(freeChampionIdsForNewPlayers);

                           // Intent intent = new Intent(activity, SettingsActivity.class);
                            //Podemos pasar informacion entre actividades con el intent
                            //intent.putExtra("parametro", 2);
                           // activity.startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
                        // Toast.makeText(activity, "No existe el nombre de invocador", Toast.LENGTH_SHORT).show();
                    }
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }

    public void getChampionsMastery(String encryptedSummonerId, String servidor, Activity activity) {

        final String URL = "https://" + servidor + ".api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-summoner/" + encryptedSummonerId + "?api_key=" + api_key;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Volley", response.toString());
                        try {
                            JSONArray championMasteryDtosJSON = response.getJSONArray("ChampionMasteryDto");
                            List<ChampionMasteryDto> championMasteryDtos = new ArrayList<ChampionMasteryDto>();
                            for (int i = 0; i < championMasteryDtosJSON.length(); ++i) {
                                ChampionMasteryDto championMasteryDto = new ChampionMasteryDto();

                                JSONObject championMasteryDtoJSON = championMasteryDtosJSON.getJSONObject(i);
                                championMasteryDto.setChampionPointsUtilNextLevel(championMasteryDtoJSON.getLong("championPointsUtilNextLevel"));
                                championMasteryDto.setChestGranted(championMasteryDtoJSON.getBoolean("chestGranted"));
                                championMasteryDto.setChampionId(championMasteryDtoJSON.getLong("championId"));
                                championMasteryDto.setLastPlayTime(championMasteryDtoJSON.getLong("lastPlayTime"));
                                championMasteryDto.setChampionLevel(championMasteryDtoJSON.getInt("championLevel"));
                                championMasteryDto.setSummonerId(championMasteryDtoJSON.getString("summonerId"));

                                championMasteryDtos.add(championMasteryDto);
                            }

                            ChampionMasteryResponse championMasteryResponse = new ChampionMasteryResponse();
                            championMasteryResponse.setChampionMasteryDtoList(championMasteryDtos);


                            // Intent intent = new Intent(activity, SettingsActivity.class);
                            //Podemos pasar informacion entre actividades con el intent
                            //intent.putExtra("parametro", 2);
                            // activity.startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error", error.getMessage());
                        // Toast.makeText(activity, "No existe el nombre de invocador", Toast.LENGTH_SHORT).show();
                    }
                });

        LolApplication.getInstance().getRequestQueue().add(jsonObjectRequest);
    }


}
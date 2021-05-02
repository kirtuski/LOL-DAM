package com.dam.lol.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.ChampionFacade;
import com.dam.lol.facade.DatabaseFacade;
import com.dam.lol.facade.ImageFacade;
import com.dam.lol.model.api.ChampionMasteryResponse;
import com.dam.lol.model.api.LeagueResponse;
import com.dam.lol.model.api.MatchListResponse;
import com.dam.lol.model.api.MatchResponse;
import com.dam.lol.model.api.SummonerResponse;
import com.dam.lol.model.api.objects.LeagueDto;
import com.dam.lol.model.api.objects.ParticipantDto;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

//TODO cambiar el color de arriba, la barra, por el que más hay en el splash art

public class InvocadorActivity extends AppCompatActivity {
    private final int COUNT = 5; //Numero de partidas que se muestran
    private SummonerResponse summoner;
    private ApiFacade apiFacade;
    private ImageFacade imageFacade;
    private ChampionFacade championFacade;
    private DatabaseFacade databaseFacade;
    private int matchNumber;
    private LinearLayoutCompat listaPartidas;
    private ArrayList<String> matchListOnView;

    private void initializeFacades() {
        this.apiFacade = LolApplication.getInstance().getApiFacade();
        this.imageFacade = LolApplication.getInstance().getImageFacade();
        this.championFacade = LolApplication.getInstance().getChampionFacade();
        this.databaseFacade = LolApplication.getInstance().getDatabaseFacade();
    }

    public void ponLeagueInfo(LeagueResponse leagueResponse) {
        List<LeagueDto> leagueDtos = leagueResponse.getLeagueDtoList();

        for (int i = 0; i < leagueDtos.size(); i++) {
            LeagueDto leagueDto = leagueDtos.get(i);
            if (leagueDto.getQueueType().equals("RANKED_FLEX_SR")) {
                TextView winFlex = findViewById(R.id.wins_flex);
                winFlex.setText("Wins: " + leagueDto.getWins());

                TextView loseFlex = findViewById(R.id.loses_flex);
                loseFlex.setText("Loses: " + leagueDto.getLosses());

                TextView winRatio = findViewById(R.id.win_ratio_flex);
                float winRatiof = (float) 100 * leagueDto.getWins() / (leagueDto.getWins() + leagueDto.getLosses());
                winRatio.setText("Winrate: " + (int) (winRatiof + 0.5) + "%");

                ImageView rankFlexIcon = findViewById(R.id.rankIcon_flex);

                int identifier = this.getResources().getIdentifier("emblem_" + leagueDto.getTier().toLowerCase(), "drawable", this.getPackageName());
                rankFlexIcon.setImageDrawable(this.getResources().getDrawable(identifier));

                TextView rankFlexText = findViewById(R.id.rank_flex);
                rankFlexText.setText(leagueDto.getTier() + " " + leagueDto.getRank());

                TextView rankFlexLp = findViewById(R.id.lp_flex);
                rankFlexLp.setText("Lp: " + leagueDto.getLeaguePoints());
            }

            if (leagueDto.getQueueType().equals("RANKED_SOLO_5x5")) {
                TextView winSolo = findViewById(R.id.wins_solo);
                winSolo.setText("Wins: " + leagueDto.getWins());

                TextView losesSolo = findViewById(R.id.loses_solo);
                losesSolo.setText("Loses: " + leagueDto.getLosses());

                TextView winRatio = findViewById(R.id.win_ratio_solo);
                float winRatiof = (float) 100 * leagueDto.getWins() / (leagueDto.getWins() + leagueDto.getLosses());
                winRatio.setText("Winrate: " + (int) (winRatiof + 0.5) + "%");

                ImageView rankSoloIcon = findViewById(R.id.rankIcon_solo);

                int identifier = this.getResources().getIdentifier("emblem_" + leagueDto.getTier().toLowerCase(), "drawable", this.getPackageName());
                rankSoloIcon.setImageDrawable(this.getResources().getDrawable(identifier));

                TextView rankSoloText = findViewById(R.id.rank_solo);
                rankSoloText.setText(leagueDto.getTier() + " " + leagueDto.getRank());

                TextView rankSoloLp = findViewById(R.id.lp_solo);
                rankSoloLp.setText("Lp: " + leagueDto.getLeaguePoints());
            }
        }
    }

    public void ponChampionMastery(ChampionMasteryResponse championMasteryResponse) throws JSONException, IOException {
        //TODO check != 0
        int campeon = championMasteryResponse.getChampionMasteryDtoList().get(0).getChampionId();
        String campeonName = championFacade.getChampionNameById(campeon);

        //TODO  se distorsiona, hay que arreglar la forma que se muestra la imagen
        ImageView fondo = findViewById(R.id.fondo);
        fondo.setImageDrawable(imageFacade.getSplashByChampionName(campeonName));

    }

    private int getIndicePartida(String matchId) {
        matchListOnView.add(matchId);
        Collections.sort(matchListOnView, Collections.reverseOrder());
        return matchListOnView.indexOf(matchId);
    }

    public void ponPartidaEnActivity(MatchResponse partidaResponse) {

        ConstraintLayout oneMatch = (ConstraintLayout) this.getLayoutInflater().inflate(R.layout.one_match, listaPartidas, false);
        listaPartidas.addView(oneMatch, 0);

        for (ParticipantDto participant : partidaResponse.getParticipants()) {
            if (participant.getPuuid().equals(summoner.getPuuid())) {
                //Imagenes

                //Champion
                ImageView imageChamp = findViewById(R.id.championImage);
                imageChamp.setImageDrawable(imageFacade.getChampionImageByName(championFacade.getChampionNameById(participant.getChampionId())));
                //Summoner1
                ImageView imageSummoner1 = findViewById(R.id.summoner1Image);
                imageSummoner1.setImageDrawable(imageFacade.getSummonerSpellImageByName(championFacade.getSummonerSpellNameById(participant.getSummoner1Id(), this)));
                //Summoner2
                ImageView imageSummoner2 = findViewById(R.id.summoner2Image);
                imageSummoner2.setImageDrawable(imageFacade.getSummonerSpellImageByName(championFacade.getSummonerSpellNameById(participant.getSummoner2Id(), this)));

                //Datos del jugador
                //kda
                String kdaFormat = participant.getKills() + "/" + participant.getDeaths() + "/" + participant.getAssists();
                TextView kda = findViewById(R.id.KDAText);
                kda.setText(kdaFormat);
                //CS
                double cM = participant.getTotalMinionsKilled() / (partidaResponse.getGameDuration() / 60000);
                double csMin = Math.round(cM * 100.0) / 100.0; // Para coger solo dos decimales
                String csFormat = participant.getTotalMinionsKilled() + "(" + csMin + ")CS";
                TextView cs = findViewById(R.id.CSText);
                cs.setText(csFormat);

                //LargestKillingSpree
                int lks = participant.getLargestKillingSpree();
                TextView largestKillingSpreeText = findViewById(R.id.largestKillingSpreeText);
                largestKillingSpreeText.setText(String.valueOf(lks));
                //largestMultiKill
                int lms = participant.getLargestMultiKill();
                TextView largestMultiKillText = findViewById(R.id.largestMultiKillText);
                largestMultiKillText.setText(String.valueOf(lms));
                //longestTimeSpentLiving
                double ltsl = participant.getLongestTimeSpentLiving();
                long minu = ((long) ltsl / 60);
                int segu = (int) (ltsl - (minu * 60));
                String format = minu + "m" + segu + "s";
                TextView longestTimeSpentLivingText = findViewById(R.id.longestTimeSpentLivingText);
                longestTimeSpentLivingText.setText(format);

                //Datos de la partida
                //matchType
                TextView matchType = findViewById(R.id.matchTypeText);
                matchType.setText(championFacade.getQueueNameById(partidaResponse.getQueueId(), this));
                //howLongAgo
                Date diaP = new Date((long) partidaResponse.getGameCreation());
                Date diaA = new Date();
                int dias = (int) ((diaA.getTime() - diaP.getTime()) / 86400000);
                String date = "";
                if (dias != 0) {
                    date = "Hace " + dias + " dias";
                } else {
                    long diff = (diaA.getTime() - diaP.getTime());
                    long horas = diff / (60 * 60 * 1000);
                    date = "Hace " + horas + " h";
                }
                TextView howLongAgoText = findViewById(R.id.howLongAgoText);
                howLongAgoText.setText(date);
                //isWin
                TextView isWin = findViewById(R.id.isWinText);
                if (participant.isWin()) {
                    isWin.setText("Victoria");
                } else {
                    isWin.setText("Derrota");
                }
                //matchDuration
                double duration = partidaResponse.getGameDuration();
                long min = ((long) duration / 1000) / 60;
                int seg = (int) ((duration / 1000) % 60);
                String durationFormat = min + "min " + seg + "s";
                TextView matchDurationText = findViewById(R.id.matchDurationText);
                matchDurationText.setText(durationFormat);
            }
            int rIdImagen = this.getResources().getIdentifier("participant" + participant.getParticipantId() + "Image", "id", this.getPackageName());
            ImageView imageChampMin = findViewById(rIdImagen);
            imageChampMin.setImageDrawable(imageFacade.getChampionImageByName(championFacade.getChampionNameById(participant.getChampionId())));
            int rIdText = this.getResources().getIdentifier("participant" + participant.getParticipantId() + "Name", "id", this.getPackageName());
            TextView participantNameMin = findViewById(rIdText);
            participantNameMin.setText(participant.getSummonerName());
        }

        int index = getIndicePartida(partidaResponse.getMatchId());
        View tempView = listaPartidas.getChildAt(0);
        listaPartidas.removeViewAt(0);
        listaPartidas.addView(tempView, index);
    }

    public void buscaPartidas(MatchListResponse matchListResponse) {
        this.matchListOnView = new ArrayList<>();
        for (String matchId : matchListResponse.getMatchList())
            apiFacade.getMatchById(matchId, summoner.getServerV5(), this);
    }

    public void cargaAntiguas(View view) {
        while (listaPartidas.getChildCount() != 1)
            listaPartidas.removeViewAt(0);
        matchNumber += COUNT;
        buscaListaPartidas();
    }

    public void cargaNuevas(View view) {
        while (listaPartidas.getChildCount() != 1)
            listaPartidas.removeViewAt(0);
        matchNumber -= COUNT;
        if (matchNumber < 0)
            matchNumber = 0;
        buscaListaPartidas();
    }

    private void buscaListaPartidas() {
        apiFacade.getMatchListByPuuid(summoner.getPuuid(), summoner.getServerV5(), matchNumber, COUNT, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invocador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        listaPartidas = findViewById(R.id.listaPartidas);

        initializeFacades();
        summoner = (SummonerResponse) this.getIntent().getSerializableExtra("datos");
        apiFacade.getChampionsMastery(summoner.getId(), summoner.getServer(), this);
        apiFacade.getSummonerLeague(summoner.getId(), summoner.getServer(), this);

        buscaListaPartidas();

        TextView summonerName = findViewById(R.id.summonerName);
        summonerName.setText(summoner.getName());

        ImageView summonerIcon = findViewById(R.id.summonerIcon);
        summonerIcon.setImageDrawable(imageFacade.getProfileIconById(summoner.getProfileIconId()));

        TextView summonerLevel = findViewById(R.id.summonerLevel);
        summonerLevel.setText("Level: " + summoner.getSummonerLevel());

        CollapsingToolbarLayout collapse = findViewById(R.id.toolbar_layout);
        AppBarLayout appBar = findViewById(R.id.app_bar);
        appBar.addOnOffsetChangedListener(new AppBarLayout.BaseOnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapse.setTitle(summoner.getName());
                    isShow = true;
                } else if (isShow) {
                    collapse.setTitle(" ");
                    isShow = false;
                }
            }
        });

        //TODO si el invocador esta en fav o se ha pulsado el boton se podría poner la estrella en dorada
        // Si pulsas otra vez se quita de fav, usar un toogleButton?
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!databaseFacade.checkSummonerExists(summoner.getName(), summoner.getServer())) {
                    if (databaseFacade.insertSummoner(summoner.getName(), summoner.getServer()) != -1) {
                        Snackbar.make(view, "Invocador añadido satisfactoriamente", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        Snackbar.make(view, "Error añadiendo el invocador", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                } else {
                    Snackbar.make(view, "El invocador ya se encuentra en favoritos", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

    }
}
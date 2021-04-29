package com.dam.lol.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import java.util.Date;
import java.util.List;

//TODO cambiar el color de arriba, la barra, por el que más hay en el splash art

public class InvocadorActivity extends AppCompatActivity {
    private SummonerResponse summoner;

    private ApiFacade apiFacade;
    private ImageFacade imageFacade;
    private ChampionFacade championFacade;
    private DatabaseFacade databaseFacade;

    private void initializeFacades() {
        this.apiFacade = LolApplication.getInstance().getApiFacade();
        this.imageFacade = LolApplication.getInstance().getImageFacade();
        this.championFacade = LolApplication.getInstance().getChampionFacade();
        this.databaseFacade = LolApplication.getInstance().getDatabaseFacade();
    }

    public void ponLeagueInfo(LeagueResponse leagueResponse){
        List<LeagueDto> leagueDtos = leagueResponse.getLeagueDtoList();

       for(int i = 0; i < leagueDtos.size() ; i++){
           LeagueDto leagueDto = leagueDtos.get(i);
           if(leagueDto.getQueueType().equals("RANKED_FLEX_SR")){
               TextView winFlex = findViewById(R.id.wins_flex);
               winFlex.setText("Wins: " + leagueDto.getWins());

               TextView loseFlex = findViewById(R.id.loses_flex);
               loseFlex.setText("Loses: " + leagueDto.getLosses());

               TextView winRatio = findViewById(R.id.win_ratio_flex);
               float winRatiof = (float)100*leagueDto.getWins()/(leagueDto.getWins() + leagueDto.getLosses()) ;
               winRatio.setText("Winrate: " + (int) (winRatiof+0.5) + "%");

               ImageView rankFlexIcon = findViewById(R.id.rankIcon_flex);

               int identifier = this.getResources().getIdentifier("emblem_" + leagueDto.getTier().toLowerCase(), "drawable", this.getPackageName());
               rankFlexIcon.setImageDrawable( this.getResources().getDrawable(identifier) );

               TextView rankFlexText = findViewById(R.id.rank_flex);
               rankFlexText.setText(leagueDto.getTier() + " " + leagueDto.getRank());

               TextView rankFlexLp = findViewById(R.id.lp_flex);
               rankFlexLp.setText("Lp: " + leagueDto.getLeaguePoints());
           }

           if(leagueDto.getQueueType().equals("RANKED_SOLO_5x5")){
               TextView winSolo = findViewById(R.id.wins_solo);
               winSolo.setText("Wins: " + leagueDto.getWins());

               TextView losesSolo = findViewById(R.id.loses_solo);
               losesSolo.setText("Loses: " + leagueDto.getLosses());

               TextView winRatio = findViewById(R.id.win_ratio_solo);
               float winRatiof = (float)100*leagueDto.getWins()/(leagueDto.getWins() + leagueDto.getLosses()) ;
               winRatio.setText("Winrate: " + (int) (winRatiof+0.5) + "%");

               ImageView rankSoloIcon = findViewById(R.id.rankIcon_solo);

               int identifier = this.getResources().getIdentifier("emblem_" + leagueDto.getTier().toLowerCase(), "drawable", this.getPackageName());
               rankSoloIcon.setImageDrawable( this.getResources().getDrawable(identifier) );

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
        String campeonName = championFacade.getChampionNameById(campeon, this);

        //TODO  se distorsiona, hay que arreglar la forma que se muestra la imagen
        ImageView fondo = findViewById(R.id.fondo);
        fondo.setImageDrawable(imageFacade.getSplashByChampionName(campeonName));

    }

    public void ponPartidaEnActivity(MatchResponse partidaResponse){
        //TODO usar linealLayout.addView(partida, index)
        // cual debe ser el indice? Existe getNChilds?
        //se debe insertar penultimo para dejar el boton siempre al final
        for(ParticipantDto participant : partidaResponse.getParticipants()){
            if(participant.getPuuid() == summoner.getPuuid()) {
                //Imagenes
                //Champion
                ImageView imageChamp = findViewById(R.id.championImage);
                imageChamp.setImageDrawable(imageFacade.getChampionImageByName(championFacade.getChampionNameById(participant.getChampionId(), this)));
                //Summoner1
                ImageView imageSummoner1 = findViewById(R.id.summoner1Image);
                imageSummoner1.setImageDrawable(imageFacade.getSummonerSpellImageByName(championFacade.getSummonerSpellNameById(participant.getSummoner1Id(), this)));
                //Summoner2
                ImageView imageSummoner2 = findViewById(R.id.summoner1Image);
                imageSummoner2.setImageDrawable(imageFacade.getSummonerSpellImageByName(championFacade.getSummonerSpellNameById(participant.getSummoner2Id(), this)));

                //Datos del jugador
                //kda
                String kdaFormat = participant.getKills() + "/" + participant.getDeaths() + "/" + participant.getAssists();
                TextView kda = findViewById(R.id.KDAText);
                kda.setText(kdaFormat);
                //CS
                double cM = participant.getTotalMinionsKilled()/(partidaResponse.getGameDuration()/60000);
                double csMin = Math.round(cM*100.0)/100.0; // Para coger solo dos decimales
                String csFormat = participant.getTotalMinionsKilled() + "(" + csMin + ")CS";
                TextView cs = findViewById(R.id.CSText);
                cs.setText(csFormat);

                //Datos de la partida
                //matchType
                TextView matchType = findViewById(R.id.matchTypeText);
                matchType.setText(championFacade.getQueueNameById(partidaResponse.getQueueId(), this);
                //howLongAgo
                Date diaP = new Date((long) partidaResponse.getGameCreation());
                Date diaA = new Date();
                int dias = (int) ((diaA.getTime()-diaP.getTime())/86400000);
                String date = "Hace " + dias + "dias";
                TextView howLongAgoText = findViewById(R.id.howLongAgoText);
                howLongAgoText.setText(date);
                //isWin
                TextView isWin = findViewById(R.id.isWinText);
                if(participant.isWin()) {
                    isWin.setText("Victoria");
                }
                else{
                    isWin.setText("Derrota");
                }
                //matchDuration
                double duration = Math.round((partidaResponse.getGameDuration()/60000)*100.0)/100.0;
                double seg = duration % 1;
                double min = duration - seg;
                String durationFormat = min + "min " + seg + "s";
                TextView matchDurationText = findViewById(R.id.matchDurationText);
                matchDurationText.setText(durationFormat);
            }
            int rIdImagen = this.getResources().getIdentifier("participant"+participant.getParticipantId()+"Imagen", "string", this.getPackageName());
            ImageView imageChampMin = findViewById(rIdImagen);
            imageChampMin.setImageDrawable(imageFacade.getChampionImageByName(championFacade.getChampionNameById(participant.getChampionId(), this)));

            int rIdText = this.getResources().getIdentifier("participant"+participant.getParticipantId()+"Name", "string", this.getPackageName());
            TextView participantNameMin = findViewById(rIdText);
            participantNameMin.setText(participant.getSummonerName());
        }
    }

    public void buscaPartidas(MatchListResponse matchListResponse){
        //TODO arreglar lo del servidor, explicado en ApiFacade
        for( String matchId : matchListResponse.getMatchList())
            apiFacade.getMatchById(matchId, summoner.getServer(), this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invocador);
        //TODO hace referencia a la barra de arriba? Util si queremos botones
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeFacades();
        summoner = (SummonerResponse) this.getIntent().getSerializableExtra("datos");

        apiFacade.getChampionsMastery(summoner.getId(), summoner.getServer(), this);
        apiFacade.getSummonerLeague(summoner.getId(), summoner.getServer(), this);

        //TODO organizar el proceso de mostrar partidas
        // Mostrar 5 partidas( parametro por defecto), vista resumen, si pulsar sobre una, activity con información expandida?
        // Aporta algo nuevo al proyecto? No -> No mostrar info expandida Si -> ya sabes
        // Boton para mostrar más partidas al final de la lista
        // Usar layout inflater con layout predefinido para mostrar los datos
        // xml -> tableLayout (al que añadir partidas, lineas) + boton para buscar más partidas (usar la otra llamada con start)
        // parametro para contar cuantas veces se ha llamado ?
        //apiFacade.getMatchListByPuuid(summoner.getPuuid(), summoner.getServer(),this);

        TextView summonerName = findViewById(R.id.summonerName);
        summonerName.setText(summoner.getName());

        ImageView summonerIcon = findViewById(R.id.summonerIcon);
        try {
            summonerIcon.setImageDrawable(imageFacade.getProfileIconById(summoner.getProfileIconId()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        TextView summonerLevel = findViewById(R.id.summonerLevel);
        summonerLevel.setText("Level: " + summoner.getSummonerLevel() );

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
                } else if(isShow) {
                    collapse.setTitle(" ");
                    isShow = false;
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(databaseFacade.insertSummoner(summoner.getName(), summoner.getServer()) != -1)
                    Snackbar.make(view, "Invocador añadido satisfactoriamente", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                else
                    Snackbar.make(view, "Error añadiendo el invocador", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
            }
        });

    }
}
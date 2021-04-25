package com.dam.lol.activities;

import android.media.Image;
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
import com.dam.lol.facade.ImageFacade;
import com.dam.lol.model.api.ChampionMasteryResponse;
import com.dam.lol.model.api.LeagueResponse;
import com.dam.lol.model.api.SummonerResponse;
import com.dam.lol.model.api.objects.LeagueDto;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

//TODO cambiar el color de arriba, la barra, por el que más hay en el splash art

public class InvocadorActivity extends AppCompatActivity {
    private SummonerResponse summoner;

    private ApiFacade apiFacade;
    private ImageFacade imageFacade;
    private ChampionFacade championFacade;

    private void initializeFacades() {
        this.apiFacade = LolApplication.getInstance().getApiFacade();
        this.imageFacade = LolApplication.getInstance().getImageFacade();
        this.championFacade = LolApplication.getInstance().getChampionFacade();
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

               TextView loseSolo = findViewById(R.id.loses_solo);
               loseSolo.setText("Loses: " + leagueDto.getLosses());

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invocador);
        //TODO hace referencia a la barra de arriba? Util si queremos botones
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initializeFacades();

        summoner = (SummonerResponse) this.getIntent().getSerializableExtra("datos");
        // Icono nombre y nivel
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

        //TODO poner bien el servidor en la llamada, sacar del intent
        apiFacade.getSummonerLeague(summoner.getId(), "euw1", this);
        apiFacade.getChampionsMastery(summoner.getId(), "euw1", this);


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
                    collapse.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO esto es como un Toast pero más gordo, a lo mejor es conveniente?
                // el toast no se ve muy bien cuando esta el traclado
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }
}
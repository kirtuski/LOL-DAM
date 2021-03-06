package com.dam.lol.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.android.volley.toolbox.NetworkImageView;
import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.DatabaseFacade;
import com.dam.lol.facade.ImageFacade;
import com.dam.lol.facade.ResourcesFacade;
import com.dam.lol.model.api.ChampionMasteryResponse;
import com.dam.lol.model.api.LeagueResponse;
import com.dam.lol.model.api.MatchListResponse;
import com.dam.lol.model.api.MatchResponse;
import com.dam.lol.model.api.SummonerResponse;
import com.dam.lol.model.api.dto.LeagueDto;
import com.dam.lol.model.api.dto.ParticipantDto;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class SummonerActivity extends AppCompatActivity {
    private final int COUNT = 5; //Numero de partidas que se muestran
    private SummonerResponse summoner;
    private ApiFacade apiFacade;
    private ImageFacade imageFacade;
    private ResourcesFacade resourcesFacade;
    private DatabaseFacade databaseFacade;
    private int matchNumber;
    private LinearLayoutCompat matchesList;
    private ArrayList<String> matchListOnView;

    private void initializeFacades() {
        this.apiFacade = LolApplication.getInstance().getApiFacade();
        this.imageFacade = LolApplication.getInstance().getImageFacade();
        this.resourcesFacade = LolApplication.getInstance().getResourcesFacade();
        this.databaseFacade = LolApplication.getInstance().getDatabaseFacade();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summoner_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        matchesList = findViewById(R.id.listaPartidas);

        initializeFacades();
        summoner = (SummonerResponse) this.getIntent().getSerializableExtra("datos");
        apiFacade.getChampionsMastery(summoner.getId(), summoner.getServer(), this);
        apiFacade.getSummonerLeague(summoner.getId(), summoner.getServer(), this);

        findMatchesList();

        TextView summonerName = findViewById(R.id.summoner_name);
        summonerName.setText(summoner.getName());

        NetworkImageView summonerIcon = findViewById(R.id.summoner_icon);
        imageFacade.setProfileIconById(summoner.getProfileIconId(), summonerIcon);

        TextView summonerLevel = findViewById(R.id.summonerLevel);
        summonerLevel.setText(getString(R.string.summoner_level, summoner.getSummonerLevel()));

        CollapsingToolbarLayout collapse = findViewById(R.id.toolbar_layout);
        TypedValue typedValue = new TypedValue();
        this.getTheme().resolveAttribute(R.attr.colorOnPrimary, typedValue, true);
        collapse.setCollapsedTitleTextColor(typedValue.data);
        collapse.setExpandedTitleColor(Color.argb(0, 0, 0, 0)); //Transparent
        this.setTitle(summoner.getName());

        FloatingActionButton fab = findViewById(R.id.fab);
        if (databaseFacade.checkSummonerExists(summoner.getName(), summoner.getServer())) {
            fab.setImageResource(R.drawable.star);
        } else {
            fab.setImageResource(R.drawable.star_border);
        }

        fab.setOnClickListener(view -> {
            if (!databaseFacade.checkSummonerExists(summoner.getName(), summoner.getServer())) {
                if (databaseFacade.insertSummoner(summoner.getName(), summoner.getServer()) != -1) {
                    Snackbar.make(view, getString(R.string.adding_summoner_success), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.star);
                } else {
                    Snackbar.make(view, getString(R.string.adding_summoner_error), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                if (databaseFacade.deleteSummoner(summoner.getName(), summoner.getServer()) == 0)
                    Snackbar.make(view, getString(R.string.deleting_summoner_error), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                else {
                    Snackbar.make(view, getString(R.string.deleting_summoner_success), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.star_border);
                }
            }
        });

    }

    public void loadLeagueInfo(LeagueResponse leagueResponse) {
        List<LeagueDto> leagueDtos = leagueResponse.getLeagueDtoList();

        for (int i = 0; i < leagueDtos.size(); i++) {
            LeagueDto leagueDto = leagueDtos.get(i);

            if (leagueDto.getQueueType().equals("RANKED_FLEX_SR")) {
                TextView winRatio = findViewById(R.id.win_ratio_flex);
                float winRatioOf = (float) 100 * leagueDto.getWins() / (leagueDto.getWins() +
                        leagueDto.getLosses());
                winRatio.setText(getString(R.string.win_rate, (int) (winRatioOf + 0.5)).concat(getString(R.string.percent)));
                ProgressBar bar = findViewById(R.id.stats_bar_flex);
                bar.setProgress((int) (winRatioOf + 0.5));

                ImageView rankFlexIcon = findViewById(R.id.rank_icon_flex);

                int identifier = this.getResources().getIdentifier("emblem_" +
                        leagueDto.getTier().toLowerCase(), "drawable", this.getPackageName());
                rankFlexIcon.setImageDrawable(ContextCompat.getDrawable(this, identifier));

                TextView rankFlexText = findViewById(R.id.rank_flex);
                rankFlexText.setText(getString(R.string.tier, leagueDto.getTier(), leagueDto.getRank()));

                TextView rankFlexLp = findViewById(R.id.lp_flex);
                rankFlexLp.setText(getString(R.string.lp, leagueDto.getLeaguePoints()));
            }

            if (leagueDto.getQueueType().equals("RANKED_SOLO_5x5")) {

                TextView winRatio = findViewById(R.id.win_ratio_solo);
                float winRatiof = (float) 100 * leagueDto.getWins() / (leagueDto.getWins() + leagueDto.getLosses());
                winRatio.setText(getString(R.string.win_rate, (int) (winRatiof + 0.5)).concat(getString(R.string.percent)));
                ProgressBar bar = findViewById(R.id.stats_bar_solo);
                bar.setProgress((int) (winRatiof + 0.5));

                ImageView rankSoloIcon = findViewById(R.id.rank_icon_solo);

                int identifier = this.getResources().getIdentifier("emblem_" +
                        leagueDto.getTier().toLowerCase(), "drawable", this.getPackageName());
                rankSoloIcon.setImageDrawable(ContextCompat.getDrawable(this, identifier));

                TextView rankSoloText = findViewById(R.id.rank_solo);
                rankSoloText.setText(getString(R.string.tier, leagueDto.getTier(), leagueDto.getRank()));

                TextView rankSoloLp = findViewById(R.id.lp_solo);
                rankSoloLp.setText(getString(R.string.lp, leagueDto.getLeaguePoints()));
            }
        }
    }

    public void loadChampionMastery(ChampionMasteryResponse championMasteryResponse) {
        if (championMasteryResponse.getChampionMasteryDtoList().size() != 0) {
            int championId = championMasteryResponse.getChampionMasteryDtoList().get(0).getChampionId();
            String champName = resourcesFacade.getChampionNameById(championId);
            NetworkImageView background = findViewById(R.id.background_mastery);
            imageFacade.setSplashByChampionName(champName, background);
        }
    }

    private int getMatchIndex(String matchId) {
        matchListOnView.add(matchId);
        matchListOnView.sort(Collections.reverseOrder());
        return matchListOnView.indexOf(matchId);
    }

    public void loadMatchInActivity(MatchResponse matchResponse) {
        ConstraintLayout oneMatch = (ConstraintLayout)
                this.getLayoutInflater().inflate(R.layout.one_match, matchesList, false);
        matchesList.addView(oneMatch, 0);

        for (ParticipantDto participant : matchResponse.getParticipants()) {
            if (participant.getPuuid().equals(summoner.getPuuid())) {
                //Champion
                NetworkImageView imageChamp = findViewById(R.id.championImage);
                imageFacade.setChampionImageByName(resourcesFacade.getChampionNameById(
                        participant.getChampionId()), imageChamp);
                //Summoner1
                NetworkImageView imageSummoner1 = findViewById(R.id.summoner1Image);
                imageFacade.setSummonerSpellImageByName(resourcesFacade.getSummonerSpellNameById(
                        participant.getSummoner1Id()), imageSummoner1);
                //Summoner2
                NetworkImageView imageSummoner2 = findViewById(R.id.summoner2Image);
                imageFacade.setSummonerSpellImageByName(resourcesFacade.getSummonerSpellNameById(
                        participant.getSummoner2Id()), imageSummoner2);

                //Participant
                //KDA
                TextView kda = findViewById(R.id.KDAText);
                kda.setText(getString(R.string.kda_value, participant.getKills(), participant.getDeaths(), participant.getAssists()));
                //CS
                double cM = participant.getTotalMinionsKilled() / (matchResponse.getGameDuration() / 60000);
                double csMin = Math.round(cM * 100.0) / 100.0;
                TextView cs = findViewById(R.id.CSText);
                cs.setText(getString(R.string.cs_value, participant.getTotalMinionsKilled(), csMin));

                //LargestKillingSpree
                TextView largestKillingSpreeText = findViewById(R.id.largestKillingSpree);
                largestKillingSpreeText.setText(getString(R.string.largest_spree, participant.getLargestKillingSpree()));
                //largestMultiKill
                TextView largestMultiKillText = findViewById(R.id.largestMultiKill);
                largestMultiKillText.setText(getString(R.string.largest_multikill, participant.getLargestMultiKill()));
                //longestTimeSpentLiving
                double ltsl = participant.getLongestTimeSpentLiving();
                long min = ((long) ltsl / 60);
                int seg = (int) (ltsl - (min * 60));
                TextView longestTimeSpentLivingText = findViewById(R.id.longestTimeSpentLiving);
                longestTimeSpentLivingText.setText(getString(R.string.longest_time_alive).concat(getString(R.string.min_seg_time, min, seg)));

                //Match
                //matchType
                TextView matchType = findViewById(R.id.matchTypeText);
                matchType.setText(resourcesFacade.getQueueNameById(matchResponse.getQueueId()));
                //howLongAgo
                Date diaP = new Date((long) matchResponse.getGameCreation());
                Date diaA = new Date();
                int days = (int) ((diaA.getTime() - diaP.getTime()) / 86400000);
                TextView howLongAgoText = findViewById(R.id.howLongAgoText);
                if (days != 0) {
                    howLongAgoText.setText(getResources().getQuantityString(R.plurals.days_ago, days, days));
                } else {
                    long diff = (diaA.getTime() - diaP.getTime());
                    int hours = (int) (diff / (60 * 60 * 1000));
                    howLongAgoText.setText(getResources().getQuantityString(R.plurals.hours_ago, hours, hours));
                }

                //isWin
                TextView isWin = findViewById(R.id.isWinText);
                ConstraintLayout bgElement = findViewById(R.id.box_match);
                if (participant.isWin()) {
                    isWin.setText(getString(R.string.win));
                    bgElement.setBackgroundColor(this.getColor(R.color.blueFill));
                } else {
                    isWin.setText(getString(R.string.lose));
                    bgElement.setBackgroundColor(this.getColor(R.color.redFill));
                }
                //matchDuration
                double duration = matchResponse.getGameDuration();
                long m_min = ((long) duration / 1000) / 60;
                int m_seg = (int) ((duration / 1000) % 60);
                TextView matchDurationText = findViewById(R.id.matchDurationText);
                matchDurationText.setText(getString(R.string.min_seg_time, m_min, m_seg));
            }

            int rIdImagen = this.getResources().getIdentifier("participant" +
                    participant.getParticipantId() + "Image", "id", this.getPackageName());
            NetworkImageView imageChampMin = findViewById(rIdImagen);
            imageFacade.setChampionImageByName(resourcesFacade.getChampionNameById(participant.getChampionId()),
                    imageChampMin);

            int rIdText = this.getResources().getIdentifier("participant" +
                    participant.getParticipantId() + "Name", "id", this.getPackageName());
            TextView participantNameMin = findViewById(rIdText);
            participantNameMin.setText(participant.getSummonerName());
        }

        int index = getMatchIndex(matchResponse.getMatchId());
        View tempView = matchesList.getChildAt(0);
        matchesList.removeViewAt(0);
        matchesList.addView(tempView, index);
    }

    public void findMatches(MatchListResponse matchListResponse) {
        this.matchListOnView = new ArrayList<>();
        for (String matchId : matchListResponse.getMatchList())
            apiFacade.getMatchById(matchId, summoner.getServerV5(), this);
    }

    public void loadOlder(View view) {
        while (matchesList.getChildCount() != 1)
            matchesList.removeViewAt(0);
        matchNumber += COUNT;
        findMatchesList();
    }

    public void loadNewer(View view) {
        while (matchesList.getChildCount() != 1)
            matchesList.removeViewAt(0);
        matchNumber -= COUNT;
        if (matchNumber < 0)
            matchNumber = 0;
        findMatchesList();
    }

    private void findMatchesList() {
        apiFacade.getMatchListByPuuid(summoner.getPuuid(), summoner.getServerV5(), matchNumber, COUNT, this);
    }
}
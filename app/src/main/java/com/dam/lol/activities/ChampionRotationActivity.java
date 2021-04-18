package com.dam.lol.activities;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.ChampionFacade;
import com.dam.lol.facade.ImageFacade;
import com.dam.lol.model.api.ChampionRotationResponse;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChampionRotationActivity extends AppCompatActivity {
    private ApiFacade apiFacade;
    private ImageFacade imageFacade;
    private ChampionFacade championFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeFacades();

        super.onCreate(savedInstanceState);
        //TODO hacer layout decente
        setContentView(R.layout.champion_rotation_activity);

        try {
            fillChampionRotationTable((ChampionRotationResponse) getIntent().getSerializableExtra("ChampionRotationResponse"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void initializeFacades() {
        this.apiFacade = LolApplication.getInstance().getApiFacade();
        this.imageFacade = LolApplication.getInstance().getImageFacade();
        this.championFacade = LolApplication.getInstance().getChampionFacade();
    }


    public void fillChampionRotationTable(ChampionRotationResponse championRotationResponse) throws IOException, JSONException {
        List<ImageView> imageViewList = new ArrayList<>();

        imageViewList.add(findViewById(R.id.imageView1));
        imageViewList.add(findViewById(R.id.imageView2));
        imageViewList.add(findViewById(R.id.imageView3));
        imageViewList.add(findViewById(R.id.imageView4));
        imageViewList.add(findViewById(R.id.imageView5));
        imageViewList.add(findViewById(R.id.imageView6));
        imageViewList.add(findViewById(R.id.imageView7));
        imageViewList.add(findViewById(R.id.imageView8));
        imageViewList.add(findViewById(R.id.imageView9));
        imageViewList.add(findViewById(R.id.imageView10));
        imageViewList.add(findViewById(R.id.imageView11));
        imageViewList.add(findViewById(R.id.imageView12));
        imageViewList.add(findViewById(R.id.imageView13));
        imageViewList.add(findViewById(R.id.imageView14));
        imageViewList.add(findViewById(R.id.imageView15));

        //Consumo de red de las 16 descargas aprox = 290kBytes, merece la pena guardar estas cosas en cache?
        // Si sobra tiempo pues siempre se puede comprobar que android studio tiene abajo el profiler para mirar el rendimiento de la app, se puede hacer la comparación de las 2 formas
        for(int i = 0 ; i < 15; i++){
            imageViewList.get(i).setImageDrawable(
                    imageFacade.getChampionImageByName(championFacade.getChampionNameById(championRotationResponse.getFreeChampionIds().get(i), this)));
        }
    }
}

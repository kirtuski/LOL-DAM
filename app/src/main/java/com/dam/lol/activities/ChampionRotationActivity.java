package com.dam.lol.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.NetworkImageView;
import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.ChampionFacade;
import com.dam.lol.facade.ImageFacade;
import com.dam.lol.model.api.ChampionRotationResponse;

import java.util.ArrayList;
import java.util.List;

public class ChampionRotationActivity extends AppCompatActivity {
    private ChampionFacade championFacade;
    private ImageFacade imageFacade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.champion_rotation_activity);

        initializeFacades();
        this.setTitle("CAMPEONES EN ROTACION");
        fillChampionRotationTable((ChampionRotationResponse) getIntent().getSerializableExtra("ChampionRotationResponse"));
    }

    private void initializeFacades() {
        this.championFacade = LolApplication.getInstance().getChampionFacade();
        this.imageFacade = LolApplication.getInstance().getImageFacade();
    }

    public void fillChampionRotationTable(ChampionRotationResponse championRotationResponse) {
        TableLayout tabla = findViewById(R.id.tabla_rotaciones);
        List<ImageView> imageViewList = new ArrayList<>();

        // TODO eliminar altura sobrante de cada fila, son esas propiedades que hacen rellenar el layout
        //Se usa el Params de quien lo contiene, filas estan contenidas en tabla, imagenes en filas
        TableLayout.LayoutParams filaProperties = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams imageProperties = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

        for (int i = 0; i < championRotationResponse.getFreeChampionIds().size(); i++) {
            NetworkImageView nv = new NetworkImageView(this);
            nv.setDefaultImageResId(R.drawable.default_champion);
            String champName = championFacade.getChampionNameById(championRotationResponse.getFreeChampionIds().get(i));
            imageFacade.setChampionImageByName(champName, nv);
            imageViewList.add(nv);
        }

        //Crea tabla
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //De pie 3*5
            for (int i = 0; i < 3; i++) {
                TableRow fila = new TableRow(this);
                fila.setLayoutParams(filaProperties);

                for (int j = 0; j < 5; j++) {
                    fila.addView(imageViewList.get(i * 5 + j));
                }
                tabla.addView(fila);
            }
        } else { //Apaisado 5*3
            for (int i = 0; i < 5; i++) {
                TableRow fila = new TableRow(this);
                fila.setLayoutParams(filaProperties);
                for (int j = 0; j < 3; j++) {
                    fila.addView(imageViewList.get(i * 3 + j));
                }
                tabla.addView(fila);
            }
        }

        //Ajusta¿?
        for (int i = 0; i < championRotationResponse.getFreeChampionIds().size(); i++) {
            ImageView imageViewTemp = imageViewList.get(i);
            imageViewTemp.setLayoutParams(imageProperties);
        }

    }

}

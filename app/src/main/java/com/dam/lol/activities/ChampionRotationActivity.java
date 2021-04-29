package com.dam.lol.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.appcompat.app.AppCompatActivity;

import com.dam.lol.LolApplication;
import com.dam.lol.R;
import com.dam.lol.customviews.SquareImageView;
import com.dam.lol.facade.ApiFacade;
import com.dam.lol.facade.ChampionFacade;
import com.dam.lol.facade.ImageFacade;
import com.dam.lol.model.api.ChampionRotationResponse;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO Consumo de red de las 16 descargas aprox = 290kBytes, merece la pena guardar estas cosas en cache?
// Si sobra tiempo pues siempre se puede comprobar que android studio tiene abajo el profiler para mirar el rendimiento de la app, se puede hacer la comparación de las 2 formas
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

        this.setTitle("CAMPEONES GRATUITOS EN ROTACION");

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

    // TODO eliminar altura sobrante de cada fila
    public void fillChampionRotationTable(ChampionRotationResponse championRotationResponse) throws IOException, JSONException {
        TableLayout tabla = findViewById(R.id.tabla_rotaciones);
        List<ImageView> imageViewList = new ArrayList<>();

        //Se usa el Params de quien lo contiene, filas estan contenidas en tabla, imagenes en filas
        TableLayout.LayoutParams filaProperties = new TableLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f);
        TableRow.LayoutParams imageProperties = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);

        for( int i = 0 ; i < championRotationResponse.getFreeChampionIds().size() ; i++){
            ImageView imageView = new SquareImageView(this);
            imageView.setImageDrawable(imageFacade.getChampionImageByName(championFacade.getChampionNameById(championRotationResponse.getFreeChampionIds().get(i), this)));

            imageViewList.add(imageView);
        }

        //Crea tabla
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            //De pie 3*5
            for (int i = 0; i < 3 ; i ++){
                TableRow fila = new TableRow(this);
                fila.setLayoutParams( filaProperties );

                for (int j = 0; j  < 5 ; j ++){

                    fila.addView(imageViewList.get(i*5+j));

                }
                tabla.addView(fila);
            }
        }
        else{ //Apaisado 5*3
            for (int i = 0; i < 5 ; i ++){
                TableRow fila = new TableRow(this);
                fila.setLayoutParams(filaProperties );
                for (int j = 0; j  < 3 ; j ++){
                    fila.addView(imageViewList.get(i*3+j));
                }
                tabla.addView(fila);
            }
        }

        //Ajusta¿?
        for( int i = 0 ; i < championRotationResponse.getFreeChampionIds().size() ; i++){
            ImageView imageViewTemp = imageViewList.get(i);
            imageViewTemp.setLayoutParams(imageProperties);
        }

    }

}

package com.dam.lol.facade;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

//TODO En AndroidManifest se carga network_security_config para permitir la descarga desde http, se puede permitir directamente desde el manifiesto para evitar el warning de que es de versiones mayores a la api minima.
public class ImageFacade {
    public Drawable getChampionImageByName(String name) throws IOException {
        //esto casi mejor que este en resources todos los iconos, ¿hacer un script para descargarlos todos?
            //Respuesta a lo de arriba, creo que todos los recursos son 700mb, demasiado para descargarlo de golpe al abir la app? Aunque fueran 200mb puede tardar bastante, poco funcional
        //esta mierda fuerza que sea sincrono, si el internet va mal hay riesgo de que pete la app... pero paso de hacerlo en asincrono
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        InputStream is = (InputStream) new URL("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/champion/" + name + ".png").getContent();
        return Drawable.createFromStream(is, "src name");
    }
}

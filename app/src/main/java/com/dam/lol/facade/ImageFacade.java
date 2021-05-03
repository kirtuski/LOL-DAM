package com.dam.lol.facade;

import com.android.volley.toolbox.NetworkImageView;
import com.dam.lol.LolApplication;

//TODO encapsular strings para facil cambio entre versiones.
public class ImageFacade {
    public void setChampionImageByName(String name, NetworkImageView nv) {
        nv.setImageUrl("http://ddragon.leagueoflegends.com/cdn/11.9.1/img/champion/" + name + ".png", LolApplication.getInstance().getImageLoader());
    }

    public void setSummonerSpellImageByName(String name, NetworkImageView nv) {
        nv.setImageUrl("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/spell/" + name + ".png", LolApplication.getInstance().getImageLoader());
    }

    public void setProfileIconById(int id, NetworkImageView nv) {
        nv.setImageUrl("http://ddragon.leagueoflegends.com/cdn/11.8.1/img/profileicon/" + id + ".png", LolApplication.getInstance().getImageLoader());
    }


    public void setSplashByChampionName(String name, NetworkImageView nv) {
        nv.setImageUrl("http://ddragon.leagueoflegends.com/cdn/img/champion/splash/" + name + "_0.jpg", LolApplication.getInstance().getImageLoader());
    }
}

package com.dam.lol.facade;

import com.android.volley.toolbox.NetworkImageView;
import com.dam.lol.LolApplication;
import com.dam.lol.R;

public class ImageFacade {
    public void setChampionImageByName(String name, NetworkImageView nv) {
        nv.setImageUrl(nv.getContext().getString(R.string.champion_image_url, name), LolApplication.getInstance().getImageLoader());
    }

    public void setSummonerSpellImageByName(String name, NetworkImageView nv) {
        nv.setImageUrl(nv.getContext().getString(R.string.spell_image_url, name), LolApplication.getInstance().getImageLoader());
    }

    public void setProfileIconById(int id, NetworkImageView nv) {
        nv.setImageUrl(nv.getContext().getString(R.string.profile_icon_url, id), LolApplication.getInstance().getImageLoader());
    }

    public void setSplashByChampionName(String name, NetworkImageView nv) {
        nv.setImageUrl(nv.getContext().getString(R.string.splash_url, name), LolApplication.getInstance().getImageLoader());
    }
}

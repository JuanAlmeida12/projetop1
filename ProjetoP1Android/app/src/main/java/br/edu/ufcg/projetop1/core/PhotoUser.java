package br.edu.ufcg.projetop1.core;

import android.graphics.Bitmap;

/**
 * Created by root on 15/08/16.
 */
public class PhotoUser {

    private Bitmap photo;
    private String descript;

    public PhotoUser(Bitmap photo, String descript) {
        this.photo = photo;
        this.descript = descript;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}

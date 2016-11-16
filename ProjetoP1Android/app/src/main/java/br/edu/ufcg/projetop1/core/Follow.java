package br.edu.ufcg.projetop1.core;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by root on 30/08/16.
 */
@IgnoreExtraProperties
public class Follow {
    public String token;
    public String following;
    public String follower;

    public Follow() {
    }

    public Follow(String token, String following, String follower) {
        this.token = token;
        this.following = following;
        this.follower = follower;
    }
}

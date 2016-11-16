package br.edu.ufcg.projetop1.core;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by root on 21/08/16.
 */
@IgnoreExtraProperties
public class UserInfo {

    public String picture;
    public String name;
    public String email;
    public String uuid;
    public String token;

    public UserInfo() {
    }
}

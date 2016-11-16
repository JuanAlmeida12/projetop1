package br.edu.ufcg.projetop1.core;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by root on 30/08/16.
 */
@IgnoreExtraProperties
public class UserAction {
    public String user;
    public String type;
    public String content;

    public UserAction() {
    }

    public UserAction(String user, String type, String content) {
        this.user = user;
        this.type = type;
        this.content = content;
    }
}

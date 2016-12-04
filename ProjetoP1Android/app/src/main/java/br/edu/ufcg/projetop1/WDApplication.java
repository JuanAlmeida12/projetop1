package br.edu.ufcg.projetop1;

import android.app.Application;

import com.google.firebase.FirebaseApp;

/**
 * Created by root on 25/07/16.
 */
public class WDApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(myContext)
                .applicationId("YOUR_APP_ID")
                .server("http://YOUR_PARSE_SERVER:1337/parse")

        ...

        .build()
        );
    }
}

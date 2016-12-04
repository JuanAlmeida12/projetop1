package p1.edu.ufcg.worlddiscovery;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

import p1.edu.ufcg.worlddiscovery.utils.FollowUtils;

/**
 * Created by root on 20/09/16.
 */
public class WDApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.parser_app_id))
                .clientKey(getString(R.string.parser_client_key))
                .server(getString(R.string.parse_server_url))
                .build()
        );
        FacebookSdk.sdkInitialize(this);
        ParseFacebookUtils.initialize(this);
    }
}

package es.cmartincha.cloudypics.lib;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Carlos on 07/06/2015.
 */
public class LoginToken {
    protected static final String LOGIN_TOKEN = "login_token";

    protected SharedPreferences mSharedPreferences;

    public LoginToken(Activity activity) {
        mSharedPreferences = activity.getPreferences(Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(LOGIN_TOKEN, token);
        editor.apply();
    }

    public String getToken() {
        return mSharedPreferences.getString(LOGIN_TOKEN, null);
    }

    public boolean hasToken() {
        return mSharedPreferences.contains(LOGIN_TOKEN);
    }
}

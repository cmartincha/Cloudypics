package es.cmartincha.cloudypics.lib;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Carlos on 07/06/2015.
 */
public class UserCredentials {
    protected static final String PREFERENCES_FILENAME = "cloudy_preferences";
    protected static final String LOGIN_TOKEN = "login_token";
    protected static final String LOGIN_USERNAME = "login_username";
    protected static final String LOGIN_PASSWORD = "login_password";

    protected SharedPreferences mSharedPreferences;

    public UserCredentials(Activity activity) {
        mSharedPreferences = activity.getSharedPreferences(PREFERENCES_FILENAME, Context.MODE_PRIVATE);
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(LOGIN_TOKEN, token);
        editor.apply();
    }

    public void removeToken() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.remove(LOGIN_TOKEN);
        editor.apply();
    }

    public String getToken() {
        return mSharedPreferences.getString(LOGIN_TOKEN, null);
    }

    public boolean hasToken() {
        return mSharedPreferences.contains(LOGIN_TOKEN);
    }

    public String getPassword() {
        return mSharedPreferences.getString(LOGIN_PASSWORD, null);
    }

    public void setPassword(String password) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(LOGIN_PASSWORD, password);
        editor.apply();
    }

    public String getUserName() {
        return mSharedPreferences.getString(LOGIN_USERNAME, null);
    }

    public void setUserName(String userName) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();

        editor.putString(LOGIN_USERNAME, userName);
        editor.apply();
    }
}

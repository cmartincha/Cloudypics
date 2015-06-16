package es.cmartincha.cloudypics.task;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import es.cmartincha.cloudypics.activity.PicturesActivity;
import es.cmartincha.cloudypics.lib.Server;
import es.cmartincha.cloudypics.lib.UserCredentials;

/**
 * Created by Carlos on 16/06/2015.
 */
public class SignInTask extends AsyncTask<Void, Void, JSONObject> {
    private String mUsername;
    private String mPassword;
    private String mToken;

    private Fragment mSignInFragment;

    public SignInTask(Fragment fragment, String username, String password, String token) {
        mUsername = username;
        mPassword = password;
        mToken = token;
        mSignInFragment = fragment;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject response = null;

        try {
            response = Server.login(mUsername,
                    mPassword,
                    mToken);
        } catch (Exception e) {
        }

        return response;
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        try {
            if (response.getBoolean("success")) {
                UserCredentials userCredentials = new UserCredentials(mSignInFragment.getActivity());

                userCredentials.setToken(response.getString("token"));
                userCredentials.setUserName(mUsername);
                userCredentials.setPassword(mPassword);

                Intent intent = new Intent(mSignInFragment.getActivity(), PicturesActivity.class);
                mSignInFragment.startActivity(intent);
            } else {
                Toast.makeText(mSignInFragment.getActivity(), "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (JSONException e) {
            Toast.makeText(mSignInFragment.getActivity(), "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}

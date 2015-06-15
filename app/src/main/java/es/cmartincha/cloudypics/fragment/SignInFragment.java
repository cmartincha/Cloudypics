package es.cmartincha.cloudypics.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.activity.LoginListener;
import es.cmartincha.cloudypics.lib.Server;
import es.cmartincha.cloudypics.lib.UserCredentials;

public class SignInFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    protected Button btnSignInBack;
    protected Button btnSignInEnter;
    protected EditText txtSignInUserName;
    protected EditText txtSignInPassword;
    protected EditText txtSignInKey;

    protected LoginListener mLoginListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        btnSignInBack = (Button) view.findViewById(R.id.btnSignInBack);
        btnSignInBack.setOnClickListener(this);

        btnSignInEnter = (Button) view.findViewById(R.id.btnSignInEnter);
        btnSignInEnter.setOnClickListener(this);

        txtSignInPassword = (EditText) view.findViewById(R.id.txtSignInPassword);
        txtSignInUserName = (EditText) view.findViewById(R.id.txtSignInUserName);
        txtSignInKey = (EditText) view.findViewById(R.id.txtSignInKey);

        txtSignInKey.setOnEditorActionListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mLoginListener = (LoginListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginListener");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignInBack:
                mLoginListener.changeToLoginFragment();
                break;
            case R.id.btnSignInEnter:
                new SignInTask().execute();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            new SignInTask()
                    .execute();

            return true;
        }

        return false;
    }

    private class SignInTask extends AsyncTask<Void, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(Void... params) {
            JSONObject response = null;

            try {
                response = Server.login(txtSignInUserName.getText().toString(),
                        txtSignInPassword.getText().toString(),
                        txtSignInKey.getText().toString());
            } catch (Exception e) {
            }

            return response;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            try {
                if (response.getBoolean("success")) {
                    UserCredentials userCredentials = new UserCredentials(getActivity());

                    userCredentials.setToken(response.getString("token"));
                    userCredentials.setUserName(txtSignInUserName.getText().toString());
                    userCredentials.setPassword(txtSignInPassword.getText().toString());

                    mLoginListener.goToPicturesActivity();
                } else {
                    Toast.makeText(getActivity(), "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Ooops, algo no ha ido bien", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}

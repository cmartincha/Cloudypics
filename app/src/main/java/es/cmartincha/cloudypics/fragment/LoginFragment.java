package es.cmartincha.cloudypics.fragment;

import android.app.Activity;
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

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.activity.LoginListener;
import es.cmartincha.cloudypics.task.SignInTask;

public class LoginFragment extends Fragment implements View.OnClickListener, TextView.OnEditorActionListener {

    protected Button btnLoginRegister;
    protected Button btnLoginEnter;
    protected EditText txtLoginUsername;
    protected EditText txtLoginPassword;
    protected LoginListener mLoginListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btnLoginRegister = (Button) view.findViewById(R.id.btnLoginRegister);
        btnLoginRegister.setOnClickListener(this);

        btnLoginEnter = (Button) view.findViewById(R.id.btnLoginEnter);
        btnLoginEnter.setOnClickListener(this);

        txtLoginUsername = (EditText) view.findViewById(R.id.txtLoginUserName);

        txtLoginPassword = (EditText) view.findViewById(R.id.txtLoginPassword);

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
            case R.id.btnLoginRegister:
                mLoginListener.changeToSignInFragment();
                break;
            case R.id.btnLoginEnter:
                new SignInTask(this, txtLoginUsername.getText().toString(), txtLoginPassword.getText().toString(), "").execute();
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            new SignInTask(this, txtLoginUsername.getText().toString(), txtLoginPassword.getText().toString(), "").execute();

            return true;
        }

        return false;
    }
}

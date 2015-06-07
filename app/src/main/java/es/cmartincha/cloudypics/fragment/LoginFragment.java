package es.cmartincha.cloudypics.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.activity.LoginListener;

public class LoginFragment extends Fragment implements View.OnClickListener {

    protected Button btnLoginRegister;
    protected LoginListener mLoginListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        btnLoginRegister = (Button) view.findViewById(R.id.btnLoginRegister);
        btnLoginRegister.setOnClickListener(this);

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
        }
    }
}

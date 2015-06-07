package es.cmartincha.cloudypics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.lib.LoginToken;


public class LoginActivity extends AppCompatActivity implements LoginListener {
    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSavedInstanceState = savedInstanceState;
        LoginToken loginToken = new LoginToken(this);

        if (loginToken.hasToken()) {
            goToPicturesActivity();
        } else {
            changeToLoginFragment();
        }
    }

    @Override
    public void changeToLoginFragment() {
        changeToFragment(new LoginFragment());
    }

    @Override
    public void changeToSignInFragment() {
        changeToFragment(new SignInFragment());
    }

    @Override
    public void goToPicturesActivity() {
        Intent intent = new Intent(this, PicturesActivity.class);
        startActivity(intent);
    }

    protected void changeToFragment(Fragment fragment) {
        if (findViewById(R.id.fragment_container) != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
                if (mSavedInstanceState != null) {
                    return;
                }

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
            }
        }
    }
}

package es.cmartincha.cloudypics.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import es.cmartincha.cloudypics.R;
import es.cmartincha.cloudypics.lib.UserLogin;


public class LoginActivity extends AppCompatActivity implements LoginListener {
    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSavedInstanceState = savedInstanceState;
        UserLogin userLogin = new UserLogin(this);

        if (userLogin.hasToken()) {
            goToPicturesActivity();
        } else {
            changeToLoginFragment();
        }
    }

    @Override
    public void goToPicturesActivity() {
        Intent intent = new Intent(this, PicturesActivity.class);

        startActivity(intent);
    }

    @Override
    public void changeToLoginFragment() {
        changeToFragment(new LoginFragment());
    }

    @Override
    public void changeToSignInFragment() {
        changeToFragment(new SignInFragment());
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

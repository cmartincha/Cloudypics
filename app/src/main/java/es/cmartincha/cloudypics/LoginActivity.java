package es.cmartincha.cloudypics;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;


public class LoginActivity extends FragmentActivity implements LoginListener {
    Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSavedInstanceState = savedInstanceState;
        changeToLoginFragment();
    }

    @Override
    public void changeToLoginFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            LoginFragment loginFragment = new LoginFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
                if (mSavedInstanceState != null) {
                    return;
                }

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, loginFragment).commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, loginFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }

    @Override
    public void changeToSignInFragment() {
        if (findViewById(R.id.fragment_container) != null) {
            SignInFragment signInFragment = new SignInFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            if (fragmentManager.findFragmentById(R.id.fragment_container) == null) {
                if (mSavedInstanceState != null) {
                    return;
                }

                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, signInFragment).commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, signInFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
    }
}

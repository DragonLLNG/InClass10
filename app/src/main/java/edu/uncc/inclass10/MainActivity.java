package edu.uncc.inclass10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import edu.uncc.inclass10.models.AuthResponse;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener,
        SignUpFragment.SignUpListener, PostsFragment.PostsListener, CreatePostFragment.CreatePostListener {

    AuthResponse mAuthResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);

        if(sharedPref.contains("token")) {
            mAuthResponse = new AuthResponse();
            mAuthResponse.setToken(sharedPref.getString("token", ""));
            mAuthResponse.setUser_fullname(sharedPref.getString("name", ""));
            mAuthResponse.setUser_id(sharedPref.getString("uid", ""));

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.containerView, PostsFragment.newInstance(mAuthResponse))
                    .commit();

        } else {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new LoginFragment())
                    .commit();
        }
    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new SignUpFragment())
                .commit();
    }

    @Override
    public void authCompleted(AuthResponse authResponse) {
        SharedPreferences sharedPref = this.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", authResponse.getUser_fullname());
        editor.putString("token", authResponse.getToken());
        editor.putString("uid", authResponse.getUser_id());
        editor.apply();

        this.mAuthResponse = authResponse;
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, PostsFragment.newInstance(authResponse))
                .commit();
    }

    @Override
    public void login() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();
    }

    @Override
    public void logout() {

    }

    @Override
    public void createPost() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new CreatePostFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToPosts() {
        getSupportFragmentManager().popBackStack();
    }
}
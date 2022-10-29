package edu.uncc.inclass10;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import edu.uncc.inclass10.models.Post;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener,
        SignUpFragment.SignUpListener, PostsFragment.PostsListener, CreatePostFragment.CreatePostListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                    .add(R.id.containerView, new LoginFragment())
                    .commit();

    }

    @Override
    public void createNewAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new SignUpFragment())
                .commit();
    }

    @Override
    public void goToPostFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new PostsFragment(), "postFragment")
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new LoginFragment())
                .commit();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void createPost() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new CreatePostFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goBackToPosts() {
        PostsFragment postsFragment = (PostsFragment) getSupportFragmentManager().findFragmentByTag("postFragment");
        if(postsFragment != null) {
            getSupportFragmentManager().popBackStack();
        }
    }

   // @Override
    public void updatePosts(Post post) {
         PostsFragment postsFragment = (PostsFragment) getSupportFragmentManager().findFragmentByTag("postFragment");
        if(postsFragment != null) {
            postsFragment.addPost(post);
            getSupportFragmentManager().popBackStack();
        }
    }
}
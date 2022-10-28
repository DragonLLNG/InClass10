package edu.uncc.inclass10;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.inclass10.databinding.FragmentPostsBinding;
import edu.uncc.inclass10.databinding.PostRowItemBinding;
import edu.uncc.inclass10.models.AuthResponse;
import edu.uncc.inclass10.models.Post;

public class PostsFragment extends Fragment {

    private final String TAG="demo";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_AUTH = "ARG_PARAM_AUTH";
    private AuthResponse mAuth;


    public PostsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PostsFragment newInstance(AuthResponse authResponse) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_AUTH, authResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuth = (AuthResponse) getArguments().getSerializable(ARG_PARAM_AUTH);
        }
    }

    FragmentPostsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.createPost();
            }
        });

        binding.buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.logout();
            }
        });

        binding.recyclerViewPosts.setLayoutManager(new LinearLayoutManager(getContext()));
        postsAdapter = new PostsAdapter();
        binding.recyclerViewPosts.setAdapter(postsAdapter);

        getActivity().setTitle(R.string.posts_label);

        getPosts();
    }

    void getPosts(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("posts").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        Log.d(TAG, "onSuccess: ");
                        for (QueryDocumentSnapshot document: queryDocumentSnapshots){
                            Log.d(TAG, "onComplete " + document.getId());
                            Log.d(TAG, "onComplete " + document.getData());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });
    }

    PostsAdapter postsAdapter;
    ArrayList<Post> mPosts = new ArrayList<>();

    class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {
        @NonNull
        @Override
        public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            PostRowItemBinding binding = PostRowItemBinding.inflate(getLayoutInflater(), parent, false);
            return new PostsViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
            Post post = mPosts.get(position);
            holder.setupUI(post);
        }

        @Override
        public int getItemCount() {
            return mPosts.size();
        }

        class PostsViewHolder extends RecyclerView.ViewHolder {
            PostRowItemBinding mBinding;
            Post mPost;
            public PostsViewHolder(PostRowItemBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
            }

            public void setupUI(Post post){
                mPost = post;
                mBinding.textViewPost.setText(post.getPost_text());
                mBinding.textViewCreatedBy.setText(post.getCreated_by_name());
                mBinding.textViewCreatedAt.setText(post.getCreated_at());
                mBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        }

    }

    PostsListener mListener;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (PostsListener) context;
    }

    interface PostsListener{
        void logout();
        void createPost();
    }
}
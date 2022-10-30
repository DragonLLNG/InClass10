package edu.uncc.inclass10;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

import edu.uncc.inclass10.databinding.FragmentCreatePostBinding;
import edu.uncc.inclass10.models.Post;


@RequiresApi(api = Build.VERSION_CODES.O)
public class CreatePostFragment extends Fragment {
    private final String TAG="demo";
    FragmentCreatePostBinding binding;
    LocalDateTime date;
    String pattern = "MM/dd/yyyy HH:mma";
    DateTimeFormatter dateTime = DateTimeFormatter.ofPattern(pattern);
    Post post;


    public CreatePostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreatePostBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBackToPosts();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postText = binding.editTextPostText.getText().toString();

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                if(postText.isEmpty()){
                    alertBuilder.setTitle(R.string.error)
                            .setMessage(R.string.toast_desc)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Log.d(TAG, "onClick: ");
                                }
                            });
                    alertBuilder.create().show();
                } else {

                    post = new Post();
                    post.setPost_text(postText);

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user != null) {
                        post.setCreated_by_name(user.getDisplayName());
                    } else {
                        Log.d(TAG, "onClick: Error: No User Logged In");
                    }

                    date = LocalDateTime.now();
                    post.setCreated_at(dateTime.format(date));

                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("posts")
                            .add(post)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    post.setPost_id(documentReference.getId());

                                    HashMap<String, Object> postId = new HashMap<>();
                                    postId.put("post_id", post.post_id);

                                    db.collection("posts")
                                            .document(post.post_id)
                                            .update(postId);

                                    mListener.updatePosts(post);

                                }
                            });
//
                }
            }
        });

        getActivity().setTitle(R.string.create_post_label);
    }

    CreatePostListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreatePostListener) context;
    }

    interface CreatePostListener {
        void goBackToPosts();
        void updatePosts(Post post);
    }
}
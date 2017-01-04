package com.example.path.dribble;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.path.dribble.api.APIClient;
import com.example.path.dribble.api.ServiceGenerator;
import com.example.path.dribble.api.objects.AccessToken;
import com.example.path.dribble.api.objects.Comment;
import com.example.path.dribble.api.objects.Dribble;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTID;
import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTSECRET;

/**
 * Created by Котиш on 01.01.2017.
 */

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    List<Comment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment);
        final Dribble post = (Dribble) getIntent().getSerializableExtra("post");
        final APIClient client;

        final Realm realm = Realm.getDefaultInstance();
        String accessToken = realm.where(AccessToken.class).findFirst().getAccessToken();
        String token_type = realm.where(AccessToken.class).findFirst().getTokenType();

        AccessToken token = new AccessToken();
        token.setAccessToken(accessToken);
        token.setTokenType(token_type);
        token.setClientID(API_OAUTH_CLIENTID);
        token.setClientSecret(API_OAUTH_CLIENTSECRET);
        client = ServiceGenerator.createService(APIClient.class, token, this);

        comments = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.comment_recyclerview);
        CommentAdapter adapter = new CommentAdapter(comments, getApplicationContext());

        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        Call<List<Comment>> call = client.getShotComments(post.getId(), 1, 20);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                comments.addAll(response.body());
                final int positionStart = comments.size() + 1;
                recyclerView.getAdapter().notifyItemRangeInserted(positionStart, response.body().size());
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {

            }
        });
        final EditText comment = (EditText) findViewById(R.id.commenttext);

        ImageView send = (ImageView) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = comment.getText().toString();
                Call<Comment> callComment=client.createComment(post.getId(),message);
                callComment.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        int responseCode=response.code();
                        if (responseCode==201){
                            moveTaskToBack(true);
                        }

                     if (responseCode==403){
                         Toast.makeText(CommentActivity.this,"Error. You have to be a member of team!",Toast.LENGTH_SHORT).show();
                     }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Toast.makeText(CommentActivity.this,"Network problems",Toast.LENGTH_SHORT).show();

                    }
                });

            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}

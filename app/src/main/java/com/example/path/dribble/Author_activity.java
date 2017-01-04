package com.example.path.dribble;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.path.dribble.api.APIClient;
import com.example.path.dribble.api.ServiceGenerator;
import com.example.path.dribble.api.objects.AccessToken;
import com.example.path.dribble.api.objects.Dribble;

import com.example.path.dribble.api.objects.Follower;
import com.example.path.dribble.api.objects.Followers;
import com.example.path.dribble.api.objects.Likes;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTID;
import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTSECRET;
import static com.example.path.dribble.R.id.whois;

public class Author_activity extends Activity {

    TextView userName;
    TextView city;
    TextView whoIs;
    TextView likes;
    TextView followers;
    TextView likesCount;
    TextView followersCount;
    ImageView avatar;
    RecyclerView recyclerViewLikes;
    RecyclerView recyclerViewFollowers;
    List<Likes> likesList;
    List<Followers> followersList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.author);

        Dribble post = (Dribble) getIntent().getSerializableExtra("post");

        userName = (TextView) findViewById(R.id.author_name);
        city = (TextView) findViewById(R.id.city);
        whoIs = (TextView) findViewById(whois);
        likes = (TextView) findViewById(R.id.likesName);
        followers = (TextView) findViewById((R.id.followersName));
        likesCount = (TextView) findViewById(R.id.likesCount);
        followersCount = (TextView) findViewById(R.id.followersCount);
        avatar = (ImageView) findViewById(R.id.avatar);


        userName.setText(post.getUser().getUsername());
        city.setText(post.getUser().getLocation());
        whoIs.setText(android.text.Html.fromHtml(post.getUser().getBio()));
        likesCount.setText(String.valueOf(post.getUser().getLikesReceivedCount()));
        followersCount.setText(String.valueOf(post.getUser().getFollowersCount()));
        Glide.with(getApplicationContext())
                .load(post.getUser().getAvatarUrl()).centerCrop().into(avatar);


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

        likesList = new ArrayList<>();
        recyclerViewLikes = (RecyclerView) findViewById(R.id.likes_recycler_view);
        LikesAdapter adapter = new LikesAdapter(likesList, getApplicationContext());
        recyclerViewLikes.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewLikes.setLayoutManager(linearLayoutManager);

        Call<List<Likes>> likes = client.getUsersLikes(post.getUser().getId(), 1, 20);
        likes.enqueue(new Callback<List<Likes>>() {
            @Override
            public void onResponse(Call<List<Likes>> call, Response<List<Likes>> response) {
                likesList.addAll(response.body());

                final int positionStart = likesList.size() + 1;
                recyclerViewLikes.getAdapter().notifyItemRangeInserted(positionStart, response.body().size());
            }

            @Override
            public void onFailure(Call<List<Likes>> call, Throwable t) {

            }
        });

        followersList = new ArrayList<>();
        recyclerViewFollowers = (RecyclerView) findViewById(R.id.followers_recycler_view);
        FollowersAdapter adapterFollorwers = new FollowersAdapter(followersList, getApplicationContext());
        recyclerViewFollowers.setAdapter(adapterFollorwers);
        LinearLayoutManager linearLayoutManagerFollowers = new LinearLayoutManager(this);
        recyclerViewFollowers.setLayoutManager(linearLayoutManagerFollowers);

        Call<List<Followers>> followers = client.getUsersFollowers(post.getUser().getId(), 1, 20);
        followers.enqueue(new Callback<List<Followers>>() {
            @Override
            public void onResponse(Call<List<Followers>> call, Response<List<Followers>> response) {
                followersList.addAll(response.body());

                final int positionStart = followersList.size() + 1;
                recyclerViewLikes.getAdapter().notifyItemRangeInserted(positionStart, response.body().size());
            }

            @Override
            public void onFailure(Call<List<Followers>> call, Throwable t) {

            }
        });


        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("tag1");
        tabSpec.setContent(R.id.Likes);
        tabSpec.setIndicator("Likes");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("tag2");
        tabSpec.setContent(R.id.Followers);
        tabSpec.setIndicator("Followers");
        tabHost.addTab(tabSpec);

        tabHost.setCurrentTab(0);

    }
}

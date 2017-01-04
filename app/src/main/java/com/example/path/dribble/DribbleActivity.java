package com.example.path.dribble;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;

import com.example.path.dribble.api.APIClient;
import com.example.path.dribble.api.ServiceGenerator;
import com.example.path.dribble.api.objects.AccessToken;
import com.example.path.dribble.api.objects.Dribble;
import com.example.path.dribble.api.objects.User;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTID;
import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTSECRET;

public class DribbleActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().removeAllCookies(null);
        } else
            CookieSyncManager.createInstance(getApplicationContext());
        CookieManager.getInstance().removeAllCookie();
        Intent out = new Intent(DribbleActivity.this, LoginActivity.class);
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();
        startActivity(out);
        finish();

        int id = item.getItemId();
        if (id == R.id.logout_action) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    RecyclerView recyclerView;
    List<Dribble> posts;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dribble_main);

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
        posts = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.posts_dribble);

        DribbleAdapter adapter = new DribbleAdapter(posts, getApplicationContext());
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter.clear();

        final Call<List<Dribble>> call = client.getShots(1, 30);
        call.enqueue(new Callback<List<Dribble>>() {
            @Override
            public void onResponse(Call<List<Dribble>> call, Response<List<Dribble>> response) {
                int responseCode = response.code();
                if (responseCode == 200) {
                    posts.addAll(response.body());

                    final int positionStart = posts.size() + 1;
                    recyclerView.getAdapter().notifyItemRangeInserted(positionStart, response.body().size());

                }
            }


            @Override
            public void onFailure(Call<List<Dribble>> call, Throwable t) {
                Toast.makeText(DribbleActivity.this, "Something wrong with server", Toast.LENGTH_LONG).show();

            }
        });
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final Call<List<Dribble>> call = client.getShots( 1, 30);
                call.enqueue(new Callback<List<Dribble>>() {
                    @Override
                    public void onResponse(Call<List<Dribble>> call, Response<List<Dribble>> response) {
                        int responseCode = response.code();
                        if (responseCode == 200) {
                            posts.clear();
                            posts.addAll(response.body());

                            final int positionStart = posts.size() + 1;
                            recyclerView.getAdapter().notifyItemRangeInserted(positionStart, response.body().size());
                            swipeContainer.setRefreshing(false);

                        }
                    }


                    @Override
                    public void onFailure(Call<List<Dribble>> call, Throwable t) {
                        Toast.makeText(DribbleActivity.this, "Something wrong with server", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }


}



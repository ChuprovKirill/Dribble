package com.example.path.dribble;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.path.dribble.api.APIClient;
import com.example.path.dribble.api.ServiceGenerator;
import com.example.path.dribble.api.objects.AccessToken;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class LoginActivity extends AppCompatActivity {

    Button loginButton;

    public static final String API_LOGIN_URL_FOR_FIRST_TIME = "https://dribbble.com/oauth/authorize?client_id=2cb7ea42b8b56d8148a14ddb489c5325b85777f54c4612dd9559cfbc2a6b1ca7&scope=public+write";
    public static final String API_OAUTH_CLIENTID = "2cb7ea42b8b56d8148a14ddb489c5325b85777f54c4612dd9559cfbc2a6b1ca7";
    public static final String API_OAUTH_CLIENTSECRET = "e09689b74615291ffc95a21cb4e274ce8798aaed22c6327dbc2be7a31130bb36";
    public static final String API_OAUTH_REDIRECT = "com.example.path.dribble://oauth";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(getApplicationContext());
        Realm realm = Realm.getDefaultInstance();

        if (realm.isEmpty()) {
            setContentView(R.layout.login_activity);

            loginButton = (Button) findViewById(R.id.buttonLogin);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(API_LOGIN_URL_FOR_FIRST_TIME));
                    // This flag is set to prevent the browser with the login form from showing in the history stack
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Intent dribble = new Intent(LoginActivity.this, DribbleActivity.class);
            startActivity(dribble);


        }
    }

    @Override
    protected void onResume() {
        super.onResume();


        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(API_OAUTH_REDIRECT)) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                // Добавить прогресс бар

                final SharedPreferences prefs = getApplicationContext().getSharedPreferences(
                        BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE);


                APIClient client = ServiceGenerator.createService(APIClient.class);
                Call<AccessToken> call = client.getNewAccessToken(code, API_OAUTH_CLIENTID,
                        API_OAUTH_CLIENTSECRET, API_OAUTH_REDIRECT,
                        "authorization_code");
                call.enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        int statusCode = response.code();
                        if (statusCode == 200) {


                            Intent intent = new Intent(LoginActivity.this, DribbleActivity.class);

                            Realm.init(getApplicationContext());
                            Realm realm = Realm.getDefaultInstance();
                            realm.beginTransaction();
                            realm.deleteAll();
                            AccessToken token = realm.createObject(AccessToken.class);
                            token.setAccessToken(response.body().getAccessToken());
                            token.setRefreshToken(response.body().getRefreshToken());
                            token.setTokenType(response.body().getTokenType());
                            realm.commitTransaction();
                            realm.close();
                            startActivity(intent);

                        } else {
                            Toast.makeText(LoginActivity.this, "Something wrong with server response. Check your id's.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Toast.makeText(LoginActivity.this, "Something wrong with server", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }
}









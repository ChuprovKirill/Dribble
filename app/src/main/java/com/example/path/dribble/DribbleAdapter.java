package com.example.path.dribble;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.path.dribble.api.APIClient;
import com.example.path.dribble.api.ServiceGenerator;
import com.example.path.dribble.api.objects.AccessToken;
import com.example.path.dribble.api.objects.Dribble;

import java.io.Serializable;
import java.util.List;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTID;
import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTSECRET;


public class DribbleAdapter extends RecyclerView.Adapter<DribbleAdapter.ViewHolder> implements CompoundButton.OnCheckedChangeListener, View.OnClickListener,Serializable {
    List<Dribble> posts;

    Context context;

    Realm realm = Realm.getDefaultInstance();
    String accessToken = realm.where(AccessToken.class).findFirst().getAccessToken();
    String token_type = realm.where(AccessToken.class).findFirst().getTokenType();
    AccessToken token = new AccessToken();


    public DribbleAdapter(List<Dribble> posts, Context context) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dribble_item, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Dribble post = posts.get(position);
        holder.user_name.setText(post.getUser().getName());
        String post_picture = post.getImages().getNormal();
        String avatar = post.getUser().getAvatarUrl();
        holder.description.setText(post.getDescription());
        holder.picture_name.setText(post.getTitle());
        Glide.with(context)
                .load(avatar).
                centerCrop().
                into(holder.avatar);
        Glide.with(context)
                .load(post_picture)
                .centerCrop()
                .into(holder.picture);

        holder.like_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                token.setAccessToken(accessToken);
                token.setTokenType(token_type);
                token.setClientID(API_OAUTH_CLIENTID);
                token.setClientSecret(API_OAUTH_CLIENTSECRET);
                APIClient client;
                client = ServiceGenerator.createService(APIClient.class, token, context);
                Call<List<Dribble>> call;

                if (isChecked) {
                    call = client.setLike(post.getId());
                    call.enqueue(new Callback<List<Dribble>>() {
                        @Override
                        public void onResponse(Call<List<Dribble>> call, Response<List<Dribble>> response) {

                        }

                        @Override
                        public void onFailure(Call<List<Dribble>> call, Throwable t) {

                        }
                    });
                } else client.deleteLike(post.getId());
            }
        });

        holder.user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent();
                    intent.putExtra("post",post);
                    intent.setClass(context,Author_activity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);


                }
            });
        holder.comment_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent= new Intent();
                intent.putExtra("post",post);
                intent.setClass(context,CommentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);


            }
        });

        }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {


    }
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        TextView description;
        TextView picture_name;
        ImageView picture;
        ImageView avatar;
        ToggleButton like_button;
        Button comment_button;


        public ViewHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.user_name);
            description = (TextView) itemView.findViewById(R.id.description);
            picture_name = (TextView) itemView.findViewById(R.id.picture_name);
            picture = (ImageView) itemView.findViewById(R.id.picture);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            like_button = (ToggleButton) itemView.findViewById(R.id.like_button);
            comment_button=(Button) itemView.findViewById(R.id.comment_button);



        }
    }
}


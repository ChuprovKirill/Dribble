package com.example.path.dribble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.path.dribble.api.APIClient;
import com.example.path.dribble.api.ServiceGenerator;
import com.example.path.dribble.api.objects.AccessToken;
import com.example.path.dribble.api.objects.Dribble;

import com.example.path.dribble.api.objects.Likes;
import com.example.path.dribble.api.objects.Shot;
import com.example.path.dribble.api.objects.User;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTID;
import static com.example.path.dribble.LoginActivity.API_OAUTH_CLIENTSECRET;


public class LikesAdapter extends RecyclerView.Adapter<LikesAdapter.LikesHolder> {
    List<Likes> likesList;

    public LikesAdapter(List<Likes> users,Context context) {
        this.context=context;
        this.likesList = users;
    }

    Context context;

    @Override
    public LikesAdapter.LikesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.likes_item, parent, false);
        return new LikesAdapter.LikesHolder(v);

    }
    class LikesHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView user_name;
        TextView liked;
        TextView shot_name;
        TextView date;
        public LikesHolder (View itemView){
            super(itemView);
            user_name=(TextView) itemView.findViewById(R.id.liker_name);
            avatar=(ImageView) itemView.findViewById(R.id.liker_avatar);
            liked=(TextView) itemView.findViewById(R.id.liked);
            shot_name=(TextView) itemView.findViewById(R.id.shot_name);

            date=(TextView) itemView.findViewById(R.id.date);
        }

    }


    @Override
    public void onBindViewHolder(final LikesHolder holder, int position) {
        final Likes user=likesList.get(position);
        Date getDate= new DateTime(user.getCreatedAt()).toDate();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(getDate);
        holder.date.setText(date);
        holder.shot_name.setText(user.getShot().getTitle());
        holder.user_name.setText(user.getShot().getUser().getName());
        Glide.with(context).load(user.getShot().getUser().getAvatarUrl()).centerCrop().into(holder.avatar);


    }

    @Override
    public int getItemCount() {
        return likesList.size();
    }



}

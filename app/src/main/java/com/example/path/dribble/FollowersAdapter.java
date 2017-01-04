package com.example.path.dribble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.path.dribble.api.objects.Dribble;
import com.example.path.dribble.api.objects.Follower;
import com.example.path.dribble.api.objects.Followers;

import java.util.List;


public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.FollowerHolder> {
    List<Followers> users;
    Context context;

    public FollowersAdapter(List<Followers> users, Context context) {
        this.users = users;
        this.context=context;
    }

    @Override
    public FollowerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.followers_item, parent, false);
        return new FollowersAdapter.FollowerHolder(v);
    }

    @Override
    public void onBindViewHolder(FollowerHolder holder, int position) {
        final Followers user=users.get(position);
        holder.username.setText(user.getFollower().getName());
        Glide.with(context).load(user.getFollower().getAvatarUrl()).centerCrop().into(holder.avatar);
        holder.likes_count.setText(String.valueOf(user.getFollower().getLikesReceivedCount()));


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class FollowerHolder extends RecyclerView.ViewHolder{
        ImageView avatar;
        TextView username;
        TextView left_likes;
        TextView likes_count;
        public FollowerHolder (View itemView){
            super(itemView);
            avatar=(ImageView) itemView.findViewById(R.id.follower_avatar);
            username=(TextView) itemView.findViewById(R.id.follower_name);
            left_likes=(TextView) itemView.findViewById(R.id.left_likes);
            likes_count=(TextView) itemView.findViewById(R.id.likes_left_count);
        }


    }
}

package com.example.path.dribble;

import android.content.Context;
import android.icu.text.RelativeDateTimeFormatter;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.example.path.dribble.api.objects.AccessToken;
import com.example.path.dribble.api.objects.Comment;
import com.example.path.dribble.api.objects.Dribble;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.realm.Realm;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {
    List<Comment> comments;
    Context context;

    public CommentAdapter(List<Comment> comments, Context context) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentAdapter.CommentHolder(v);

    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        final Comment comment=comments.get(position);
        Date getDate= new DateTime(comment.getCreatedAt()).toDate();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(getDate);
        holder.date.setText(date);


        holder.user_name.setText(comment.getUser().getName());
        Glide.with(context).load(comment.getUser().getAvatarUrl()).centerCrop().into(holder.avatar);
        holder.comment.setText(Html.fromHtml(comment.getBody()));

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    class CommentHolder extends RecyclerView.ViewHolder {
        TextView user_name;
        TextView comment;
        TextView date;
        ImageView avatar;

        public CommentHolder(View itemView) {
            super(itemView);
            user_name = (TextView) itemView.findViewById(R.id.username);
            avatar = (ImageView) itemView.findViewById(R.id.avatar);
            comment=(TextView) itemView.findViewById(R.id.comment);
            date=(TextView) itemView.findViewById(R.id.date);

        }
    }
}

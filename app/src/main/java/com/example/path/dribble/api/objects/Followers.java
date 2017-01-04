package com.example.path.dribble.api.objects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class Followers {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("follower")
    @Expose
    private Follower follower;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Follower getFollower() {
        return follower;
    }

    public void setFollower(Follower follower) {
        this.follower = follower;
    }

}


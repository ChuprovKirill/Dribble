package com.example.path.dribble.api;

import com.example.path.dribble.api.objects.AccessToken;
import com.example.path.dribble.api.objects.Comment;
import com.example.path.dribble.api.objects.Dribble;
import com.example.path.dribble.api.objects.Follower;
import com.example.path.dribble.api.objects.Followers;
import com.example.path.dribble.api.objects.Likes;
import com.example.path.dribble.api.objects.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIClient {

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getNewAccessToken(
            @Field("code") String code,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri,
            @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("/oauth/token")
    Call<AccessToken> getRefreshAccessToken(
            @Field("refresh_token") String refreshToken,
            @Field("client_id") String clientId,
            @Field("client_secret") String clientSecret,
            @Field("redirect_uri") String redirectUri);

    @GET("shots")
    Call<List<Dribble>> getShots(@Query("page") int page, @Query("per_page") int perPage);

    @POST("shots/{id}/like")
    Call<List<Dribble>> setLike(
            @Path("id") int id);

    @GET("shots/{id}/like")
    Call<List<Dribble>> isLiked(
            @Path("id") int id);


    @DELETE("shots/{id}/like")
    Call<List<Dribble>> deleteLike(
            @Path("id") int id);

    @GET("users/{user}")
    Call<User> getSingleUser(
            @Path("user") long userId);

    @GET("users/{user}/likes")
    Call<List<Likes>> getUsersLikes(@Path("user") int userId, @Query("page") int page, @Query("per_page") int perPage);

    @GET("users/{user}/followers")
    Call<List<Followers>> getUsersFollowers(@Path("user") int userId, @Query("page") int page, @Query("per_page") int perPage);

    @GET("shots/{shot}/comments")
    Call<List<Comment>> getShotComments(@Path("shot") int id, @Query("page") int page, @Query("per_page") int perPage);

    @POST("shots/{shot}/comments")
    Call<Comment> createComment(@Path("shot") int shotId, @Body String body);


}




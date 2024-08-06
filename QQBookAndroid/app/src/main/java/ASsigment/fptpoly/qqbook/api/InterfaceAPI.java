package ASsigment.fptpoly.qqbook.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.List;

import ASsigment.fptpoly.qqbook.Fragment.ChangePasswordRequest;
import ASsigment.fptpoly.qqbook.InforActivity;
import ASsigment.fptpoly.qqbook.Model.Comic;
import ASsigment.fptpoly.qqbook.Model.Comment;
import ASsigment.fptpoly.qqbook.Model.Favorite;
import ASsigment.fptpoly.qqbook.Model.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface InterfaceAPI {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .setLenient()
            .create();
    InterfaceAPI apiservice = new Retrofit.Builder()
            .baseUrl(InforActivity.Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(InterfaceAPI.class);

    @POST("/login")
    Call<User> Login(@Body User user);

    @GET("/api/username/{username}")
    Call<User> getUserWithName(@Path("username") String username);

    @GET("/user/getname/{userId}")
    Call<User> getUsernameWithUserId(@Path("userId") String userId);

    @GET("/user/favorite/{userId}")
    Call<List<Comic>> getFavorite(@Path("userId") String userId);

    @DELETE("/user/delete/favorite/{userId}/{comicId}")
    Call<User> deleteFavorite(@Path("userId") String userId, @Path("comicId") String comicId);

    @GET("/api/favorite/comicId/{userId}")
    Call<JsonObject> getComicIdWithUser(@Path("userId") String userId);

    @PUT("/user/buyComic/{userId}")
    Call<User> buyComic(@Path("userId") String userId, @Body Favorite favorite);

    @PUT("/user/update/password/{userId}")
    Call<User> changePassword(@Path("userId") String userId, @Body ChangePasswordRequest newpass);

    @POST("/user/post")
    Call<User> Register(@Body User user);

    ///
    @GET("/api/comics")
    Call<List<Comic>> getComic();

    @GET("/comics/search/{name}")
    Call<List<Comic>> searchComicByName(@Path("name") String name);

    ///
    @GET("/api/comments/{comicId}")
    Call<List<Comment>> getCommentWithComcId(@Path("comicId") String comicId);

    @DELETE("/comments/delete/{commentId}")
    Call<Comment> deleteComment(@Path("commentId") String commentId);



    @PUT("/comments/update/{commentId}")
    Call<Comment> updateComment(@Path("commentId") String commentId, @Body Comment comment);

    @POST("/comments")
    Call<Comment> postComment(@Body Comment comment);
}

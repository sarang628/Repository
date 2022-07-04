package com.sarang.api;

import com.example.torang_core.data.model.Alarm;
import com.example.torang_core.data.model.Comment;
import com.example.torang_core.data.model.Feed;
import com.example.torang_core.data.model.Filter;
import com.example.torang_core.data.model.HoursOfOperation;
import com.example.torang_core.data.model.Like;
import com.example.torang_core.data.model.Menu;
import com.example.torang_core.data.model.MenuReview;
import com.example.torang_core.data.model.Picture;
import com.example.torang_core.data.model.Response;
import com.example.torang_core.data.model.Restaurant;
import com.example.torang_core.data.model.Review;
import com.example.torang_core.data.model.User;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface RestaurantService {

    @FormUrlEncoded
    @POST("getAllRestaurant")
    Call<ArrayList<Restaurant>> getAllRestaurant(@FieldMap Map<String, String> params);

    @POST("getFilterRestaurant")
    Call<ArrayList<Restaurant>> getFilterRestaurant(@Body Filter filter);

    @FormUrlEncoded
    @POST("getRestaurant")
    Call<Restaurant> getRestaurant(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getOpenHours")
    Call<ArrayList<HoursOfOperation>> getHoursOfOperation(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getMenus")
    Call<ArrayList<Menu>> getMenus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getPictures")
    Call<ArrayList<Picture>> getPictures(@FieldMap Map<String, String> params);

    @POST("addReview")
    Call<Review> addReview(@Body com.example.torang_core.data.model.Review reviewBody);

    @POST("updateReview")
    Call<Review> updateReview(@Body com.example.torang_core.data.model.Review reviewBody);

    @POST("deleteReview")
    Call<Review> deleteReview(@Body Review review);

    @FormUrlEncoded
    @POST("getReviews")
    Call<ArrayList<Review>> getReviews(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getMyReview")
    Call<Review> getMyReview(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("getMenuReviews")
    Call<ArrayList<MenuReview>> getMenuReviews(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("facebook_login")
    Call<Response<User>> facebook_login(@FieldMap Map<String, String> params);

    @Multipart
    @POST("fileUpload")
    Call<Review> fileUpload(@PartMap() Map<String, RequestBody> params, @Part MultipartBody.Part... pictures);

    @FormUrlEncoded
    @POST("deletePicture")
    Call<Void> deletePicture(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getTimelines")
    Call<ArrayList<Review>> getTimelines(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("saveMenu")
    Call<Void> saveMenu(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getFeeds")
    Call<ArrayList<Feed>> getFeeds(@FieldMap Map<String, String> params);

    @POST("addMenuReview")
    Call<MenuReview> addMenuReview(@Body MenuReview menuReview);

    @FormUrlEncoded
    @POST("getMyReviews")
    Call<ArrayList<Review>> getMyReviews(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getMyReviewsByUserId")
    Call<ArrayList<Review>> getMyReviewsByUserId(@FieldMap Map<String, String> params);

    @POST("getMyMenuReviews")
    Call<ArrayList<MenuReview>> getMyMenuReviews(@Body Review review);

    @POST("addComment")
    Call<Comment> addComment(@Body Comment comment);

    @POST("modifyComment")
    Call<Comment> modifyComment(@Body Comment comment);

    @POST("deleteComment")
    Call<Comment> deleteComment(@Body Comment comment);

    @FormUrlEncoded
    @POST("getComments")
    Call<ArrayList<Comment>> getComments(@FieldMap Map<String, String> params);

    @POST("addLike")
    Call<Like> addLike(@Body Like like);

    @POST("deleteLike")
    Call<Like> deleteLike(@Body Like like);

    @FormUrlEncoded
    @POST("getAlarms")
    Call<ArrayList<Alarm>> getAlarms(@FieldMap Map<String, String> params);
}

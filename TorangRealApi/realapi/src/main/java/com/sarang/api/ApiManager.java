package com.sarang.api;

import android.app.Activity;
import android.app.Application;

import com.example.torang_core.data.model.Alarm;
import com.example.torang_core.data.model.Comment;
import com.example.torang_core.data.model.FeedData;
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
import com.example.torang_core.api.BaseApiManager;
import com.example.torang_core.api.BaseCallbackListener;
import com.example.torang_core.util.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiManager implements BaseApiManager {

    private static ApiManager apiManager;
    private static Application application;

    public static ApiManager getInstance() {
        if (apiManager == null) {
            apiManager = new ApiManager();
        }
        return apiManager;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    private static String URL = Constants.API_URL;

    private RestaurantService getService() {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
        logger.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        logger.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(logger);
        httpClient.writeTimeout(10, TimeUnit.SECONDS);
        httpClient.connectTimeout(10, TimeUnit.SECONDS);
        httpClient.writeTimeout(10, TimeUnit.SECONDS);
        httpClient.readTimeout(10, TimeUnit.SECONDS);

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .header("User-Agent", "android")
                        .header("accessToken", /*BananaPreference.getInstance(application).getAccessToken()*/"")
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });


        OkHttpClient client = httpClient.build();


        //레트로핏 초기화 BASE URL 설정하는 곳
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //통신인터페이스 기반 서비스 생성
        RestaurantService service = retrofit.create(RestaurantService.class);
        return service;
    }

    public void getAllRestaurant(BaseCallbackListener<ArrayList<Restaurant>> callbackListener) {
        callbackListener.apiStart();
        getService().getAllRestaurant(new HashMap<String, String>()).enqueue(((CallbackListener) callbackListener).callback);
    }

    public void getFilterRestaurant(Filter filter, BaseCallbackListener<ArrayList<Restaurant>> callbackListener) {
        callbackListener.apiStart();
        getService().getFilterRestaurant(filter).enqueue(new CallbackListener<ArrayList<Restaurant>>(callbackListener).callback);
    }

    @Override
    public void getRestaurant(int restaurantId, BaseCallbackListener<Restaurant> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("restaurant_id", "" + restaurantId);
        callbackListener.apiStart();
        getService().getRestaurant(param).enqueue(new CallbackListener<Restaurant>(callbackListener).callback);
    }

    @Override
    public void getHoursOfOperation(int restaurantId, BaseCallbackListener<ArrayList<HoursOfOperation>> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("restaurant_id", "" + restaurantId);
        callbackListener.apiStart();
        getService().getHoursOfOperation(param).enqueue((new CallbackListener<ArrayList<HoursOfOperation>>(callbackListener).callback));
    }

    @Override
    public void getMenus(int restaurantId, BaseCallbackListener<ArrayList<Menu>> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("restaurant_id", "" + restaurantId);
        callbackListener.apiStart();
        getService().getMenus(param).enqueue((new CallbackListener<ArrayList<Menu>>(callbackListener).callback));
    }


    @Override
    public void getPictures(int restaurantId, BaseCallbackListener<ArrayList<Picture>> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("restaurant_id", "" + restaurantId);
        callbackListener.apiStart();
        getService().getPictures(param).enqueue((new CallbackListener<ArrayList<Picture>>(callbackListener).callback));
    }

    public void addReview(Review reviewBody, BaseCallbackListener<Review> callbackListener) {
        Logger.d(reviewBody != null ? reviewBody.toString() : "reviewBody is null");
        callbackListener.apiStart();
        getService().addReview(reviewBody).enqueue((new CallbackListener<Review>(callbackListener).callback));
    }

    public void updateReview(Review reviewBody, BaseCallbackListener<Review> callbackListener) {
        callbackListener.apiStart();
        getService().updateReview(reviewBody).enqueue((new CallbackListener<Review>(callbackListener).callback));
    }

    @Override
    public void follow(int userId, int userId1, BaseCallbackListener<User> userBaseCallbackListener) {

    }

    @Override
    public void unfollow(int userId, int userId1, BaseCallbackListener<User> userBaseCallbackListener) {

    }

    @Override
    public void getReviews(int restaurant_id, BaseCallbackListener<ArrayList<Review>> callbackListener) {
        Logger.d(restaurant_id);
        Map<String, String> param = new HashMap<>();
        param.put("restaurant_id", "" + restaurant_id);
        callbackListener.apiStart();
        getService().getReviews(param).enqueue((new CallbackListener<ArrayList<Review>>(callbackListener).callback));
    }

    public void getMenuReviews(int menu_id, BaseCallbackListener<ArrayList<MenuReview>> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("menu_id", "" + menu_id);
        callbackListener.apiStart();
        getService().getMenuReviews(param).enqueue((new CallbackListener<ArrayList<MenuReview>>(callbackListener).callback));
    }

    public void getMyReview(int restaurant_id, int user_id, BaseCallbackListener<Review> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("restaurant_id", "" + restaurant_id);
        param.put("userId", "" + user_id);
        callbackListener.apiStart();
        getService().getMyReview(param).enqueue((new CallbackListener<Review>(callbackListener).callback));
    }

    public void facebookLogin(String accessToken, BaseCallbackListener<Response<User>> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("accessToken", "" + accessToken);
        callbackListener.apiStart();
        getService().facebook_login(param).enqueue(new CallbackListener<Response<User>>(callbackListener).callback);
    }

    @Override
    public void deleteReview(Review review, BaseCallbackListener<Review> callbackListener) {
        callbackListener.apiStart();
        getService().deleteReview(review).enqueue((new CallbackListener<Review>(callbackListener).callback));
    }

    @Override
    public void addComment(Comment comment, BaseCallbackListener<Comment> callbackListener) {
        callbackListener.apiStart();
        getService().addComment(comment).enqueue((new CallbackListener<Comment>(callbackListener).callback));
    }

    @Override
    public void modifyComment(Comment comment, BaseCallbackListener<Comment> callbackListener) {
        callbackListener.apiStart();
        getService().modifyComment(comment).enqueue((new CallbackListener<Comment>(callbackListener).callback));
    }

    @Override
    public void deleteComment(Comment comment, BaseCallbackListener<Comment> callbackListener) {
        callbackListener.apiStart();
        getService().deleteComment(comment).enqueue((new CallbackListener<Comment>(callbackListener).callback));
    }

    @Override
    public void getComments(FeedData feed, BaseCallbackListener<ArrayList<Comment>> callbackListener) {

    }

    public void fileUpload(Activity activity, Review reviewBody,
                           BaseCallbackListener<Review> callbackListener, ArrayList<File> fileList
            , OnFileUploadProgressListener onFileUploadProgressListener) {
        callbackListener.apiStart();
        MultipartBody.Part[] pictureList = new MultipartBody.Part[fileList.size()];

        Logger.d(fileList.size());

        for (int i = 0; i < fileList.size(); i++) {
            File file = fileList.get(i);
            CountingFileRequestBody requestFile = new CountingFileRequestBody(file, "image/*"
                    , num -> activity.runOnUiThread(() -> {
                float percentage = ((float) num / (float) file.length()) * 100;
                if (onFileUploadProgressListener != null) {
                    onFileUploadProgressListener.OnProgress(file, percentage);
                }
                //uploadProgressDialog.setProcessProgress((int) percentage);
            }));
            pictureList[i] = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        }
        Logger.d(pictureList.length);

        getService().fileUpload(reviewBody.toMap(), pictureList).enqueue((new CallbackListener<Review>(callbackListener).callback));
    }

    public void deletePicture(int picture_id, BaseCallbackListener<Void> callbackListener) {
        Logger.d("call deletePicture api");
        Map<String, String> param = new HashMap<>();
        param.put("picture_id", "" + picture_id);
        callbackListener.apiStart();
        getService().deletePicture(param).enqueue((new CallbackListener<Void>(callbackListener).callback));
    }

    @Override
    public void getTimelines(BaseCallbackListener<ArrayList<Review>> callbackListener) {
        Map<String, String> param = new HashMap<>();
        callbackListener.apiStart();
        getService().getTimelines(param).enqueue(new CallbackListener<ArrayList<Review>>(callbackListener).callback);
    }


    @Override
    public void saveMenu(Picture picture, MenuReview menuReview, BaseCallbackListener<Void> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("picture_id", "" + picture.picture_id);
        param.put("menu_id", "" + menuReview.menu_id);
        param.put("menu_review_id", "" + menuReview.menu_review_id);
        callbackListener.apiStart();
        getService().saveMenu(param).enqueue((new CallbackListener<Void>(callbackListener).callback));
    }

    @Override
    public void getFeeds(int user_id, BaseCallbackListener<ArrayList<FeedData>> callbackListener) {

    }

    @Override
    public void addMenuReview(MenuReview menuReview, BaseCallbackListener<MenuReview> callbackListener) {
        callbackListener.apiStart();
        getService().addMenuReview(menuReview).enqueue((new CallbackListener<MenuReview>(callbackListener).callback));
    }

    @Override
    public void getMyReviews(int restaurant_id, int user_id, BaseCallbackListener<ArrayList<Review>> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("restaurant_id", "" + restaurant_id);
        param.put("user_id", "" + user_id);
        callbackListener.apiStart();
        getService().getMyReviews(param).enqueue((new CallbackListener<ArrayList<Review>>(callbackListener).callback));
    }

    @Override
    public void getMyReviewsByUserId(int user_id, BaseCallbackListener<ArrayList<Review>> callbackListener) {
        Map<String, String> param = new HashMap<>();
        param.put("user_id", "" + user_id);
        callbackListener.apiStart();
        getService().getMyReviewsByUserId(param).enqueue((new CallbackListener<ArrayList<Review>>(callbackListener).callback));
    }

    @Override
    public void getMyMenuReviews(Review review, BaseCallbackListener<ArrayList<MenuReview>> callbackListener) {
        callbackListener.apiStart();
        getService().getMyMenuReviews(review).enqueue((new CallbackListener<ArrayList<MenuReview>>(callbackListener).callback));
    }

    @Override
    public void addLike(Like like, BaseCallbackListener<Like> callbackListener) {
        callbackListener.apiStart();
        getService().addLike(like).enqueue((new CallbackListener<Like>(callbackListener).callback));
    }

    @Override
    public void deleteLike(Like like, BaseCallbackListener<Like> callbackListener) {
        callbackListener.apiStart();
        getService().deleteLike(like).enqueue((new CallbackListener<Like>(callbackListener).callback));
    }

    @Override
    public void getMyAlarms(User user, BaseCallbackListener<ArrayList<Alarm>> callbackListener) {
        callbackListener.apiStart();
        Map<String, String> param = new HashMap<>();
        param.put("user_id", "" + user.userId);
        getService().getAlarms(param).enqueue((new CallbackListener<ArrayList<Alarm>>(callbackListener).callback));
    }

    @Override
    public void getFollowers(int user_id, BaseCallbackListener<ArrayList<User>> callbackListener) {
        callbackListener.callback(User.listDummy());
    }
}

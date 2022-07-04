package com.sarang.api;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GlobalApplication extends Application {

    ArrayList<AppCompatActivity> activityArrayList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        ApiManager.getInstance().setApplication(this);
    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //MultiDex.install(this);
    }

    public void addActivity(AppCompatActivity appCompatActivity) {
        activityArrayList.add(appCompatActivity);
    }

    public void finishActivity() {
        for (AppCompatActivity appCompatActivity : activityArrayList) {
            if (appCompatActivity != null && !appCompatActivity.isDestroyed()) {
                appCompatActivity.finish();
            }
        }
    }
}

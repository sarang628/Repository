package com.sarang.api;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.torang_core.api.BaseCallbackListener;
import com.example.torang_core.util.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallbackListener<V> extends BaseCallbackListener<V> {
    Context context;
    ProgressDialog progressDialog;
    BaseCallbackListener baseCallbackListener;

    public void setBaseCallbackListener(BaseCallbackListener baseCallbackListener) {
        this.baseCallbackListener = baseCallbackListener;
    }

    public CallbackListener(BaseCallbackListener baseCallbackListener) {
        this.baseCallbackListener = baseCallbackListener;
    }

    public CallbackListener(Context context) {
        this.context = context;
    }

    public CallbackListener() {
    }

    Callback<V> callback = new Callback<V>() {
        @Override
        public void onResponse(Call<V> call, Response<V> response) {
            apiEnd();
            try {
                V model = response.body();
                callback(model);
            } catch (Exception e) {
                e.printStackTrace();
                failed(e.toString());
            }
        }


        @Override
        public void onFailure(Call<V> call, Throwable t) {
            apiEnd();
            callback(null);
            Logger.d(t.toString());
            failed(t.toString());
        }
    };


    @Override
    public void callback(V model) {
        if (baseCallbackListener != null)
            baseCallbackListener.callback(model);
    }

    @Override
    public void failed(String msg) {
        Logger.d(msg);
    }

    public void apiStart() {
        if (context != null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    public void apiEnd() {
        if (context != null && progressDialog != null)
            progressDialog.dismiss();
    }


}

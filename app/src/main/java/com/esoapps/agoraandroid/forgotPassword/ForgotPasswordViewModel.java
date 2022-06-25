package com.esoapps.agoraandroid.forgotPassword;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.esoapps.agoraandroid.remote.APIService;
import com.esoapps.agoraandroid.remote.RetrofitClient;

import net.steamcrafted.loadtoast.LoadToast;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends AndroidViewModel {
private LoadToast loadToast;
private Context context;
    ForgotPasswordViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context=context;
    }

    void sendForgotPassLink(String userName) {
        loadToast = new LoadToast(context);
        loadToast.setText("Processing");
        loadToast.show();

        APIService apiService = RetrofitClient.getAPIService();
        Call<String> forgotPasswordResponse = apiService.sendForgotPassword(userName);
        forgotPasswordResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                loadToast.success();
                Toast.makeText(getApplication(), "Link Sent, Please Check Your Emails", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadToast.error();
                Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });

    }
}

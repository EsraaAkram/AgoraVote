package com.esoapps.agoraandroid.home;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.esoapps.agoraandroid.main.MainActivity;
import com.esoapps.agoraandroid.remote.APIService;
import com.esoapps.agoraandroid.remote.RetrofitClient;

import net.steamcrafted.loadtoast.LoadToast;

import com.esoapps.agoraandroid.utilities.SharedPrefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class HomeViewModel extends AndroidViewModel {
    private final Context context;
    private LoadToast loadToast;
    private final SharedPrefs sharedPrefs = new SharedPrefs(getApplication());


    public HomeViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
    }

    void doLogout(String token) {
        loadToast = new LoadToast(context);
        loadToast.setText("Logging out");
        loadToast.show();
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> logoutResponse = apiService.logout(token);
        logoutResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    loadToast.success();
                    Toast.makeText(getApplication(), "Logged Out Successfully", Toast.LENGTH_SHORT).show();
                    sharedPrefs.clearLogin();
                    context.startActivity(new Intent(getApplication(), MainActivity.class));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadToast.error();
                Toast.makeText(getApplication(), "Something Went Wrong Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });

    }


}

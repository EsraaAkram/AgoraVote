package com.esoapps.agoraandroid.logIn;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.esoapps.agoraandroid.remote.APIService;
import com.esoapps.agoraandroid.remote.RetrofitClient;

import net.steamcrafted.loadtoast.LoadToast;

import com.esoapps.agoraandroid.utilities.SharedPrefs;
import com.esoapps.agoraandroid.home.HomeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {
    private final Context context;
    private LoadToast loadToast;
    private final SharedPrefs sharedPrefs = new SharedPrefs(getApplication());


    public LoginViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;
    }

    public void logInRequest(final String userName, final String userPassword) {

        loadToast = new LoadToast(context);
        loadToast.setText("Logging in");
        loadToast.show();
        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("identifier", userName);
            jsonObject.put("password", userPassword);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> logInResponse = apiService.logIn(jsonObject.toString());
        logInResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    try {
                        JSONObject jsonObjects = new JSONObject(response.body());

                        JSONObject token = jsonObjects.getJSONObject("token");
                        String expiresOn = token.getString("expiresOn");
                        String key = token.getString("token");
                        String sUserName = jsonObjects.getString("username");
                        String email = jsonObjects.getString("email");
                        String firstName = jsonObjects.getString("firstName");
                        String lastName = jsonObjects.getString("lastName");

                        sharedPrefs.saveUserName(sUserName);
                        sharedPrefs.saveEmail(email);
                        sharedPrefs.saveFullName(firstName, lastName);
                        sharedPrefs.saveToken(key);
                        sharedPrefs.savePass(userPassword);
                        sharedPrefs.saveTokenExpiresOn(expiresOn);
                        loadToast.success();

                        context.startActivity(new Intent(context, HomeActivity.class));


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    loadToast.error();
                    Toast.makeText(getApplication(), "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadToast.error();
                Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void facebookLogInRequest(String accessToken) {
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> facebookLogInResponse = apiService.facebookLogin(accessToken);
        facebookLogInResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());
                        String expiresOn = jsonObject.getString("expiresOn");
                        String authToken = jsonObject.getString("token");
                        sharedPrefs.saveToken(authToken);
                        sharedPrefs.saveTokenExpiresOn(expiresOn);
                        getUserData(authToken);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplication(), "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserData(String authToken) {
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> getDataResponse = apiService.getUserData(authToken);
        getDataResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body());

                        String UserName = jsonObject.getString("username");
                        String email = jsonObject.getString("email");
                        String firstName = jsonObject.getString("firstName");
                        String lastName = jsonObject.getString("lastName");

                        sharedPrefs.saveUserName(UserName);
                        sharedPrefs.saveEmail(email);
                        sharedPrefs.saveFullName(firstName, lastName);
                        context.startActivity(new Intent(context, HomeActivity.class));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplication(), "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();

            }
        });

    }

}

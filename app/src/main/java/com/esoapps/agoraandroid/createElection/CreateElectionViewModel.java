package com.esoapps.agoraandroid.createElection;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;

import com.esoapps.agoraandroid.home.HomeActivity;
import com.esoapps.agoraandroid.remote.APIService;
import com.esoapps.agoraandroid.remote.RetrofitClient;
import com.esoapps.agoraandroid.utilities.SharedPrefs;
import com.esoapps.agoraandroid.utilities.TinyDB;

import net.steamcrafted.loadtoast.LoadToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


class CreateElectionViewModel extends AndroidViewModel {
    private final Context context;
    private final ElectionDetailsSharedPrefs electionDetailsSharedPrefs = new ElectionDetailsSharedPrefs(getApplication());
    private final SharedPrefs sharedPrefs = new SharedPrefs(getApplication());
    private final TinyDB tinydb = new TinyDB(getApplication());
    private LoadToast loadToast;


    CreateElectionViewModel(@NonNull Application application, Context context) {
        super(application);
        this.context = context;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createElection() {
        loadToast = new LoadToast(context);
        loadToast.setText("Creating Election");
        loadToast.show();
        ArrayList<String> candidates = tinydb.getListString("Candidates");
        JSONArray jsArray = new JSONArray(candidates);
        String token = sharedPrefs.getToken();
        final JSONObject jsonObject = new JSONObject();
        try {
            Map<String, String> map = new HashMap<>();
            map.put("voteBallot", "");
            map.put("voterEmail", "");
            JSONArray ballot = new JSONArray(new Map[]{map});
            jsonObject.put("ballot", ballot);  //Append the other JSONObject to the parent one
            jsonObject.put("name", electionDetailsSharedPrefs.getElectionName());
            jsonObject.put("description", electionDetailsSharedPrefs.getElectionDesc());
            jsonObject.put("voterListVisibility", electionDetailsSharedPrefs.getVoterListVisibility());
            jsonObject.put("startingDate", electionDetailsSharedPrefs.getStartTime());
            jsonObject.put("endingDate", electionDetailsSharedPrefs.getEndTime());
            jsonObject.put("isInvite", electionDetailsSharedPrefs.getIsInvite());
            jsonObject.put("ballotVisibility", electionDetailsSharedPrefs.getBallotVisibility());
            jsonObject.put("isRealTime", electionDetailsSharedPrefs.getIsRealTime());
            jsonObject.put("votingAlgo", electionDetailsSharedPrefs.getVotingAlgo());
            jsonObject.put("candidates", jsArray);
            jsonObject.put("noVacancies", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> electionResponse = apiService.createElection(jsonObject.toString(), token);
        electionResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.message().equals("OK")) {
                    loadToast.success();
                    Toast.makeText(getApplication(), "Created Successfully", Toast.LENGTH_SHORT).show();
                    electionDetailsSharedPrefs.clearElectionData();
                    context.startActivity(new Intent(context, HomeActivity.class));
                } else {
                    loadToast.error();
                    Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadToast.error();
                Toast.makeText(getApplication(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
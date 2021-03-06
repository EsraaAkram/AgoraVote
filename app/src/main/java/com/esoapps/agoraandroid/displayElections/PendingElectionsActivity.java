package com.esoapps.agoraandroid.displayElections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.esoapps.agoraandroid.adapters.ElectionsRecyclerAdapter;

import com.esoapps.agoraandroid.R;

import com.esoapps.agoraandroid.createElection.ElectionDetailsSharedPrefs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PendingElectionsActivity extends AppCompatActivity {
    private final ArrayList<String> mElectionNameList = new ArrayList<>();
    private final ArrayList<String> mElectionDescriptionList = new ArrayList<>();
    private final ArrayList<String> mElectionStartDateList = new ArrayList<>();
    private final ArrayList<String> mElectionEndDateList = new ArrayList<>();
    private final ArrayList<String> mElectionStatusList = new ArrayList<>();
    private final ArrayList<String> mCandidatesList = new ArrayList<>();
    private final ArrayList<String> mIDList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_elections);
        ElectionDetailsSharedPrefs electionDetailsSharedPrefs = new ElectionDetailsSharedPrefs(getApplicationContext());
        RecyclerView rvElectionDetails = findViewById(R.id.rv_pending_elections);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvElectionDetails.setLayoutManager(mLayoutManager);
        try {
            JSONObject jsonObject = new JSONObject(electionDetailsSharedPrefs.getElectionDetails());
            JSONArray electionsJsonArray = jsonObject.getJSONArray("elections");

            for (int i = 0; i < electionsJsonArray.length(); i++) {
                StringBuilder mCandidateName = new StringBuilder();
                JSONObject singleElectionJsonObject = electionsJsonArray.getJSONObject(i);

                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                Date formattedStartingDate = formatter.parse(singleElectionJsonObject.getString("start"));
                Date formattedEndingDate = formatter.parse(singleElectionJsonObject.getString("end"));
                Date currentDate = Calendar.getInstance().getTime();

                if (currentDate.before(formattedStartingDate)) {
                    mElectionStatusList.add("Pending");
                    mElectionStartDateList.add(formattedStartingDate.toString());
                    mElectionEndDateList.add(formattedEndingDate.toString());
                    mElectionNameList.add(singleElectionJsonObject.getString("name"));
                    mIDList.add(singleElectionJsonObject.getString("_id"));
                    mElectionDescriptionList.add(singleElectionJsonObject.getString("description"));
                }
                JSONArray candidatesJsonArray = singleElectionJsonObject.getJSONArray("candidates");
                for (int j = 0; j < candidatesJsonArray.length(); j++) {
                    mCandidateName.append(candidatesJsonArray.getString(j)).append("\n");
                }
                mCandidatesList.add(mCandidateName.toString().trim());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ElectionsRecyclerAdapter electionsRecyclerAdapter = new ElectionsRecyclerAdapter(mIDList,this,mElectionNameList, mElectionDescriptionList, mElectionStartDateList, mElectionEndDateList, mElectionStatusList, mCandidatesList, "pending");
        rvElectionDetails.setAdapter(electionsRecyclerAdapter);
    }
}

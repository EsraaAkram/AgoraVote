package com.esoapps.agoraandroid.displayElections;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.esoapps.agoraandroid.adapters.VotersRecyclerAdapter;

import com.esoapps.agoraandroid.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VotersActivity extends AppCompatActivity {
    private String voterResponse;
    private final ArrayList<String> mVoterEmailList = new ArrayList<>();
    private final ArrayList<String> mVoterNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voters);
        RecyclerView rvVotersDetails = findViewById(R.id.recycler_view_voters);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvVotersDetails.setLayoutManager(mLayoutManager);


        if (getIntent().hasExtra("voters_response")) {
            voterResponse = getIntent().getStringExtra("voters_response");
        }
        try {
            JSONObject jsonObject = new JSONObject(voterResponse);
            JSONArray ballotJsonArray = jsonObject.getJSONArray("voters");
            for (int i = 0; i < ballotJsonArray.length(); i++) {
                JSONObject ballotJsonObject = ballotJsonArray.getJSONObject(i);

                mVoterEmailList.add(ballotJsonObject.getString("email"));
                mVoterNameList.add(ballotJsonObject.getString("name"));

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        VotersRecyclerAdapter votersRecyclerAdapter = new VotersRecyclerAdapter(mVoterNameList, mVoterEmailList);
        rvVotersDetails.setAdapter(votersRecyclerAdapter);
    }
}

package com.esoapps.agoraandroid.createElection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.esoapps.agoraandroid.R;

public class CreateElectionThree extends AppCompatActivity {
    private ElectionDetailsSharedPrefs electionDetailsSharedPrefs;
    private String votingAlgorithm;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_election_three);
        electionDetailsSharedPrefs =new ElectionDetailsSharedPrefs(getApplication());
        Button mFinalStepButton = findViewById(R.id.button_to_final_step);
        radioGroup = findViewById(R.id.radioGroup);
        mFinalStepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                votingAlgorithm = radioButton.getText().toString();
                electionDetailsSharedPrefs.saveVotingAlgo(votingAlgorithm);
                startActivity(new Intent(CreateElectionThree.this, CreateElectionFour.class));
            }
        });
    }
}

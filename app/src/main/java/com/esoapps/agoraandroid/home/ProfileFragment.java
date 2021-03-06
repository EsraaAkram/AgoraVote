package com.esoapps.agoraandroid.home;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.esoapps.agoraandroid.remote.APIService;
import com.esoapps.agoraandroid.remote.RetrofitClient;
import com.google.android.material.textfield.TextInputLayout;

import net.steamcrafted.loadtoast.LoadToast;

import com.esoapps.agoraandroid.R;
import com.esoapps.agoraandroid.utilities.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private TextInputLayout mNewPAss, mConfirmPass;
    private LoadToast loadToast;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPrefs sharedPrefs = new SharedPrefs(getActivity());
        View view = inflater.inflate(R.layout.fragment_profile, null);
        TextView userName = view.findViewById(R.id.text_user_name);
        TextView emailId = view.findViewById(R.id.text_email_id);
        TextView fullName = view.findViewById(R.id.text_full_name);
        final String token = sharedPrefs.getToken();
        Button mChangePassButton = view.findViewById(R.id.button_change_password);
        mNewPAss = view.findViewById(R.id.textInputLayout_new_password);
        mConfirmPass = view.findViewById(R.id.textInputLayout_confirm_password);
        mChangePassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPass = mNewPAss.getEditText().getText().toString().trim();
                String confirmPass = mConfirmPass.getEditText().getText().toString().trim();
                if (newPass.equals(confirmPass))
                    doChangePasswordRequest(confirmPass, token);
                else mConfirmPass.setError("Password Does Not Matches");
            }
        });
        String username = sharedPrefs.getUserName();
        String email = sharedPrefs.getEmail();
        String userFullName = sharedPrefs.getfullName();

        userName.setText(username);
        emailId.setText(email);
        fullName.setText(userFullName);
        return view;
    }

    @Override

    public void onViewCreated(@NonNull View view, @Nullable Bundle
            savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void doChangePasswordRequest(String password, String token) {
        loadToast = new LoadToast(getActivity());
        loadToast.setText("Changing Password");
        loadToast.show();
        final JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("password", password);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        APIService apiService = RetrofitClient.getAPIService();
        Call<String> changePassResponse = apiService.changePassword(jsonObject.toString(), token);
        changePassResponse.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    loadToast.success();
                    Toast.makeText(getActivity(), "Password Changed Successfully", Toast.LENGTH_SHORT).show();


                } else {
                    Log.d("TAG", "onResponse:" + response.body());
                    loadToast.error();
                    Toast.makeText(getActivity(), "Wrong User Name or Password", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                loadToast.error();
                Toast.makeText(getActivity(), "Something went wrong please try again", Toast.LENGTH_SHORT).show();

            }
        });

    }
}
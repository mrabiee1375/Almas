package com.example.almas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.SignInModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Models.UserModel;
import com.example.almas.Utilities.Utility;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInLayout extends AppCompatActivity {

    private ApiService _ApiService;

    EditText fName;
    EditText lName;
    EditText nationalCode;
    EditText phoneNumber;
    EditText password;
    EditText confPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_layout);
        fName = (EditText) findViewById(R.id.signIn_fName);
        lName = (EditText) findViewById(R.id.signIn_lName);
        nationalCode = (EditText) findViewById(R.id.signIn_nationalCode);
        phoneNumber = (EditText) findViewById(R.id.signIn_phoneNumber);
        password = (EditText) findViewById(R.id.signIn_pass);
        confPassword = (EditText) findViewById(R.id.signIn_confPass);


        SharedPreferences sharedPreferences = getSharedPreferences("userDetailsShEditor", MODE_PRIVATE);
        String userDetailJson = sharedPreferences.getString("userDetails", "");

        if (!userDetailJson.isEmpty()) {
            Gson gson = new Gson();
            UserModel userModel = gson.fromJson(userDetailJson, UserModel.class);
            fName.setText(userModel.getFirstName());
            lName.setText(userModel.getLastName());
            nationalCode.setText(userModel.getNationalcode());
            phoneNumber.setText(userModel.getPhoneNumber());

            Utility.DisableEditText(fName);
            Utility.DisableEditText(lName);
            Utility.DisableEditText(nationalCode);
            Utility.DisableEditText(phoneNumber);

            Button signIn=(Button)findViewById(R.id.signIn_btn);
            signIn.setVisibility(View.GONE);
            password.setVisibility(View.GONE);
            confPassword.setVisibility(View.GONE);
        }



    }

    public void CreateUser(View view) {


        SignInModel signInModel = new SignInModel();
        signInModel.setFirstName(fName.getText().toString());
        signInModel.setLastName(lName.getText().toString());
        signInModel.setConfirmPassword(confPassword.getText().toString());
        signInModel.setPassword(password.getText().toString());
        signInModel.setNationalNumber(nationalCode.getText().toString());
        signInModel.setPhoneNumber(phoneNumber.getText().toString());
        signInModel.setUserName(phoneNumber.getText().toString());

        _ApiService= RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.SignIn(signInModel).enqueue(new Callback<ResponseModel<String>>() {
            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                if(response.body().getIsSuccess()) {
                    Intent intent = new Intent(SignInLayout.this, ActivationCodeLayout.class);
                    intent.putExtra("userId",response.body().getData());
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Utility.oprnCustomToast(response.body().getDetailMessages(),SignInLayout.this);
                }
            }
            @Override
            public void onFailure(Call<ResponseModel<String>> call, Throwable t) {
                int x=9;
            }
        });
    }



}

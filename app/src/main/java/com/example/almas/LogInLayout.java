package com.example.almas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.LogInModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Models.UserModel;
import com.example.almas.Utilities.Utility;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogInLayout extends AppCompatActivity {
    private ApiService _ApiService;

    EditText phone;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_layout);
        phone = (EditText) findViewById(R.id.login_phone);
        password = (EditText) findViewById(R.id.login_password);

    }

    public void login_click(View view) {
        LogInModel model = new LogInModel();
        model.setPassword(password.getText().toString());
        model.setUserName(phone.getText().toString());
        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.LogIn(model).enqueue(new Callback<ResponseModel<UserModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<UserModel>> call, Response<ResponseModel<UserModel>> response) {

                if(response.body().getIsSuccess()) {
                    Gson gson = new Gson();
                    String jsonObj = gson.toJson(response.body().getData());
                    SharedPreferences.Editor sharedEditor = getSharedPreferences("userDetailsShEditor", MODE_PRIVATE).edit();
                    sharedEditor.putString("userDetails", jsonObj);
                    sharedEditor.commit();
                    Intent intent = new Intent(LogInLayout.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Utility.oprnCustomToast(response.body().getDetailMessages(),LogInLayout.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<UserModel>> call, Throwable t) {

            }
        });
    }
}

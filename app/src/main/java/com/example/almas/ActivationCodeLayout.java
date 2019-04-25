package com.example.almas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.ActivationAccountModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.SmsListener;
import com.example.almas.Utilities.SmsReciver;
import com.example.almas.Utilities.Utility;

import java.security.PrivateKey;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivationCodeLayout extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;


    private ApiService _ApiService;
    EditText codeEditText;
    String userId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation_code_layout);
        codeEditText = (EditText) findViewById(R.id.activation_code);

        userId=getIntent().getExtras().getString("userId");

        String action = "START";
        final Intent intent = new Intent(this, SmsReciver.class);
        intent.setAction(action);
        startService(intent);
       SmsReciver.bindListener(new SmsListener() {
           @Override
           public void onMessageReceived(String messageText) {
               String code="";
               code = messageText.replaceAll("[^0-9]+", "");
               codeEditText.setText(code);

               ActivationAccountModel model=new ActivationAccountModel();
               model.setActivationCode(code);
               model.setUserId(userId);
               ActivateAccount(model);

           }
       });

       if(!checkPermission()) {
           requestPermissionReciveSms();
       }
    }

    public void ActivateClick(View view) {
        String code=codeEditText.getText().toString();

        ActivationAccountModel model=new ActivationAccountModel();
        model.setActivationCode(code);
        model.setUserId(userId);
        ActivateAccount(model);
    }
    private void ActivateAccount(ActivationAccountModel model)
    {
        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.ActivationAccount(model).enqueue(new Callback<ResponseModel<Boolean>>() {
            @Override
            public void onResponse(Call<ResponseModel<Boolean>> call, Response<ResponseModel<Boolean>> response) {
                if(response.body().getIsSuccess()) {
                    Intent intent = new Intent(ActivationCodeLayout.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Utility.oprnCustomToast(response.body().getDetailMessages(),ActivationCodeLayout.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<Boolean>> call, Throwable t) {

            }
        });
    }

    private void requestPermissionReciveSms(){
        ActivityCompat.requestPermissions(ActivationCodeLayout.this,new String[]{
                Manifest.permission.RECEIVE_SMS},PERMISSION_REQUEST_CODE);
    }
    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(ActivationCodeLayout.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

}

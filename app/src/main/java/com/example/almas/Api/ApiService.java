package com.example.almas.Api;

import com.example.almas.Models.ActivationAccountModel;
import com.example.almas.Models.LogInModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.SignInModel;
import com.example.almas.Models.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("Account/SignIn/")
    Call<ResponseModel<String>> SignIn(@Body SignInModel model);

    @POST("Account/ActivationAccount/")
    Call<ResponseModel<Boolean>> ActivationAccount(@Body ActivationAccountModel model);

    @POST("Account/Login/")
    Call<ResponseModel<UserModel>> LogIn(@Body LogInModel model);


}

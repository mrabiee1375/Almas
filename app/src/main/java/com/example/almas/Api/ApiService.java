package com.example.almas.Api;

import com.example.almas.Models.ActivationAccountModel;
import com.example.almas.Models.AdminMessageModel;
import com.example.almas.Models.BillModel;
import com.example.almas.Models.ChargeModel;
import com.example.almas.Models.CreateAndEditAdminMessageRequest;
import com.example.almas.Models.CreateAndEditBillRequestModel;
import com.example.almas.Models.GetAdminMessgesRequestModel;
import com.example.almas.Models.GetBillListRequestModel;
import com.example.almas.Models.GetChargesRequestModel;
import com.example.almas.Models.LogInModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.SignInModel;
import com.example.almas.Models.UserModel;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    @POST("Account/SignIn/")
    Call<ResponseModel<String>> SignIn(@Body SignInModel model);

    @POST("Account/ActivationAccount/")
    Call<ResponseModel<Boolean>> ActivationAccount(@Body ActivationAccountModel model);

    @POST("Account/Login/")
    Call<ResponseModel<UserModel>> LogIn(@Body LogInModel model);

    //bills region
    @POST("Admin/GetBills/")
    Call<ResponseModel<ArrayList<BillModel>>> GetBills(@Body GetBillListRequestModel model);

    @POST("Admin/CreateBill/")
    Call<ResponseModel<BillModel>> CreateBill(@Body CreateAndEditBillRequestModel model);

    @POST("Admin/UpdateBill/")
    Call<ResponseModel<BillModel>> UpdateBill(@Body CreateAndEditBillRequestModel model);

    @GET("Admin/GetBillDetails/")
    Call<ResponseModel<BillModel>> GetBillDetail(@Query("billId")int billId);

    //admin messages region
    @POST("Admin/GetAdminMessages/")
    Call<ResponseModel<ArrayList<AdminMessageModel>>> GetAdminMessages(@Body GetAdminMessgesRequestModel model);

    @POST("Admin/CreateAdminMessage/")
    Call<ResponseModel<AdminMessageModel>> CreateAdminMessage(@Body CreateAndEditAdminMessageRequest model);

    @POST("Admin/UpdateAdminMessage/")
    Call<ResponseModel<AdminMessageModel>> UpdateAdminMessage(@Body CreateAndEditAdminMessageRequest model);

    @GET("Admin/GetAdminMessageDetails/")
    Call<ResponseModel<AdminMessageModel>> GetAdminMessageDetails(@Query("messageId")int messageId);

    //admin charge region
    @POST("Admin/GetChargeRecords/")
    Call<ResponseModel<ArrayList<ChargeModel>>> GetChargeRecords(@Body GetChargesRequestModel model);

    @POST("Admin/CreateCharge/")
    Call<ResponseModel<ChargeModel>> CreateCharge(@Body ChargeModel model);

    @POST("Admin/UpdateCharge/")
    Call<ResponseModel<ChargeModel>> UpdateCharge(@Body ChargeModel model);

    @GET("Admin/GetCharge/")
    Call<ResponseModel<ChargeModel>> GetCharge(@Query("chargeId")int chargeId);

}

package com.example.almas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.BillModel;
import com.example.almas.Models.CreateAndEditBillRequestModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.Utility;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLandPage extends Fragment {

    private ImageView userBills;
    private ImageView userCharges;
    private ImageView userMessages;
    private ImageView adminBills;
    private ImageView adminCharges;
    private ImageView adminMessages;
    private ImageView adminCreateBills;
    private ImageView adminCreateCharges;
    private ImageView adminCreateMessages;
    private ImageView notUserLogin;
    private ImageView notUserSignIn;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_land_page, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        userBills=(ImageView)getView().findViewById(R.id.user_bills);
        userCharges=(ImageView)getView().findViewById(R.id.user_charges);
        userMessages=(ImageView)getView().findViewById(R.id.user_messages);
        adminBills=(ImageView)getView().findViewById(R.id.admin_bills);
        adminCreateBills=(ImageView)getView().findViewById(R.id.admin_create_bills);
        adminCharges=(ImageView)getView().findViewById(R.id.admin_charges);
        adminCreateCharges=(ImageView)getView().findViewById(R.id.admin_create_charges);
        adminMessages=(ImageView)getView().findViewById(R.id.admin_message);
        adminCreateMessages=(ImageView)getView().findViewById(R.id.admin_create_message);
        notUserLogin=(ImageView)getView().findViewById(R.id.not_user_login);
        notUserSignIn=(ImageView)getView().findViewById(R.id.not_user_sign_in);
        ButtonsStatus();
        ImagesEvents();
    }

    public  void  ButtonsStatus()
    {
        LinearLayout notUserLayout=(LinearLayout)getView().findViewById(R.id.not_login_view);
        LinearLayout userLayout=(LinearLayout)getView().findViewById(R.id.user_view);
        LinearLayout adminLayout=(LinearLayout)getView().findViewById(R.id.admin_view);

        if(StaticVars.UserModel==null)
        {
            notUserLayout.setVisibility(View.VISIBLE);
            userLayout.setVisibility(View.GONE);
            adminLayout.setVisibility(View.GONE);
        }
        else  if(StaticVars.IsAdmin)
        {
            notUserLayout.setVisibility(View.GONE);
            userLayout.setVisibility(View.GONE);
            adminLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            notUserLayout.setVisibility(View.GONE);
            userLayout.setVisibility(View.VISIBLE);
            adminLayout.setVisibility(View.GONE);
        }
    }
    public  void  ImagesEvents()
    {
        userBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentBillsList());
            }
        });
        userCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentChargesList());
            }
        });
        userMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentAdminMessagesList());
            }
        });
        adminBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentBillsList());
            }
        });
        adminCreateBills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentCreateBill());
            }
        });
        adminMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentAdminMessagesList());
            }
        });
        adminCreateMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentCreateAdminMessage());
            }
        });
        adminCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentChargesList());
            }
        });
        adminCreateCharges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadFragment(new FragmentCreateCharge());
            }
        });

        notUserSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),SignInLayout.class);
                startActivity(intent);
            }
        });

        notUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),LogInLayout.class);
                startActivity(intent);
            }
        });
    }

    public void LoadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentFrame, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
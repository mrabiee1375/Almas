package com.example.almas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.almas.Adapters.BillListAdapter;
import com.example.almas.Adapters.ChargeDetailListAdapter;
import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.ChargeDetailModel;
import com.example.almas.Models.ChargeModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCreateCharge extends Fragment {

    ApiService _ApiService;
    ArrayList<ChargeDetailModel> chargeDetailsList;
    ChargeDetailListAdapter adapter;
    ListView detailsListView;
    Button addDetail;
    Button submitDetails;
    EditText text;
    EditText title;
    boolean updateCharge = false;
    Integer chargeId = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            updateCharge = getArguments().getBoolean("update_flag");
            chargeId = getArguments().getInt("charge_id");
        } catch (Exception ex) {
        }
        return inflater.inflate(R.layout.fragment_create_charge, container, false);
    }

    @Override
    public void onResume() {



        chargeDetailsList = new ArrayList<ChargeDetailModel>();
        chargeDetailsList.add(new ChargeDetailModel());
        addDetail = (Button) getView().findViewById(R.id.add_charge_detail_btn);
        submitDetails = (Button) getView().findViewById(R.id.submit_charge_btn);
        text = (EditText) getView().findViewById(R.id.create_charge_text);
        title = (EditText) getView().findViewById(R.id.create_charge_title);

        if(StaticVars.IsAdmin)
        {
            AdminOperations();
        }
        else{
            NotAdminOperations();
        }
        if (updateCharge) {
            FillForm();
        } else {
            adapter = new ChargeDetailListAdapter(chargeDetailsList, getActivity());
            detailsListView = (ListView) getView().findViewById(R.id.charge_details_list_view);
            detailsListView.setAdapter(adapter);
        }


        addDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                chargeDetailsList.add(new ChargeDetailModel());
                adapter.notifyDataSetChanged();
                ArrayList<ChargeDetailModel> list = chargeDetailsList;

                Utility.SetListViewHeightBasedOnItems(detailsListView);
            }
        });
        submitDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.notifyDataSetChanged();
                ChargeModel model = new ChargeModel();
                model.setChargeDetails(chargeDetailsList);
                model.setText(text.getText().toString());
                model.setTitle(title.getText().toString());
                model.setId(chargeId);
                if (updateCharge) {
                    UpdateCharge(model);
                } else {
                    CreateCharge(model);
                }

            }
        });
        super.onResume();
    }

    public  void  FillForm()
    {
        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.GetCharge(chargeId).enqueue(new Callback<ResponseModel<ChargeModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<ChargeModel>> call, Response<ResponseModel<ChargeModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        chargeDetailsList=response.body().getData().getChargeDetails();
                        text.setText(response.body().getData().getText());
                        title.setText(response.body().getData().getTitle());
                        adapter = new ChargeDetailListAdapter(chargeDetailsList, getActivity());
                        detailsListView = (ListView) getView().findViewById(R.id.charge_details_list_view);
                        detailsListView.setAdapter(adapter);
                        Utility.SetListViewHeightBasedOnItems(detailsListView);
                    } else {
                        Utility.oprnCustomToast(response.body().getDetailMessages(), getActivity());
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ChargeModel>> call, Throwable t) {

            }
        });
    }
    public void UpdateCharge(ChargeModel model) {
        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.UpdateCharge(model).enqueue(new Callback<ResponseModel<ChargeModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<ChargeModel>> call, Response<ResponseModel<ChargeModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        Fragment newFragment = new FragmentChargesList();

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(R.id.fragmentFrame, newFragment);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();
                    } else {
                        Utility.oprnCustomToast(response.body().getDetailMessages(), getActivity());
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ChargeModel>> call, Throwable t) {

            }
        });
    }

    public void CreateCharge(ChargeModel model) {
        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.CreateCharge(model).enqueue(new Callback<ResponseModel<ChargeModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<ChargeModel>> call, Response<ResponseModel<ChargeModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        Fragment newFragment = new FragmentChargesList();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(R.id.fragmentFrame, newFragment);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();
                    } else {
                        Utility.oprnCustomToast(response.body().getDetailMessages(), getActivity());
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ChargeModel>> call, Throwable t) {

            }
        });

    }
    public void AdminOperations()
    {
        Utility.EnableEditText(title);
        Utility.EnableEditText(text);
        addDetail.setVisibility(View.VISIBLE);
        submitDetails.setVisibility(View.VISIBLE);

    }
    public void NotAdminOperations()
    {
        addDetail.setVisibility(View.INVISIBLE);
        submitDetails.setVisibility(View.INVISIBLE);
        Utility.DisableEditText(title);
        Utility.DisableEditText(text);

    }
}

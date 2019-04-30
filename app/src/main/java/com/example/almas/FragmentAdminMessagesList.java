package com.example.almas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.almas.Adapters.AdminMessagesListAdapter;
import com.example.almas.Adapters.BillListAdapter;
import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.AdminMessageModel;
import com.example.almas.Models.BillModel;
import com.example.almas.Models.GetAdminMessgesRequestModel;
import com.example.almas.Models.GetBillListRequestModel;
import com.example.almas.Models.ListAdapterModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentAdminMessagesList extends Fragment {

    private ApiService _ApiService;
    private Spinner billTypesSpinner;

    TextView FilterStartDate;
    TextView FilterEndDate;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_admin_messages_list, container, false);


    }

    @Override
    public void onResume() {
        super.onResume();

        FilterStartDate=(TextView) getView().findViewById(R.id.messages_from_date);
        FilterEndDate=(TextView) getView().findViewById(R.id.messages_to_date);
        //Api Request Model
        GetAdminMessgesRequestModel model = new GetAdminMessgesRequestModel();
        model.setCount(StaticVars.ListItemsCount);
        model.setPage(1);
        GetAdminMessagesList(model);


        Button button = (Button) getView().findViewById(R.id.set_filter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAdminMessgesRequestModel model = new GetAdminMessgesRequestModel();
                model.setCount(StaticVars.ListItemsCount);
                model.setPage(1);
                model.setFromDateFa(FilterStartDate.getText().toString());
                model.setToDateFa(FilterEndDate.getText().toString());
                GetAdminMessagesList(model);
            }
        });

        FilterEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.ShowCalendar(getActivity(),FilterEndDate);
            }
        });
        FilterStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.ShowCalendar(getActivity(),FilterStartDate);
            }
        });
    }

    public void GetAdminMessagesList(GetAdminMessgesRequestModel model) {
        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.GetAdminMessages(model).enqueue(new Callback<ResponseModel<ArrayList<AdminMessageModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<AdminMessageModel>>> call, Response<ResponseModel<ArrayList<AdminMessageModel>>> response) {
                try {

                    if (response.body().getIsSuccess()) {
                        //Adapter List
                        ArrayList<ListAdapterModel> adapterModelsList = new ArrayList<ListAdapterModel>();

                        //Response Data = List of bills
                        ArrayList<AdminMessageModel> responseData = response.body().getData();
                        for (int i = 0; i < responseData.size(); i++) {
                            ListAdapterModel item = new ListAdapterModel();
                            item.setId(responseData.get(i).getId());
                            item.setText(responseData.get(i).getTitle());
                            adapterModelsList.add(item);
                        }

                        AdminMessagesListAdapter adapter = new AdminMessagesListAdapter(adapterModelsList, getActivity());
                        ListView lView = (ListView) getView().findViewById(R.id.bills_list_view);
                        lView.setAdapter(adapter);
                    }
                }
                catch (Exception ex)
                {}
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<AdminMessageModel>>> call, Throwable t) {

            }

        });

    }



}


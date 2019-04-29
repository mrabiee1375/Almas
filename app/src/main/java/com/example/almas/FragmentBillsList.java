package com.example.almas;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.almas.Adapters.BillListAdapter;
import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.BillModel;
import com.example.almas.Models.GetBillListRequestModel;
import com.example.almas.Models.ListAdapterModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentBillsList extends Fragment {

    private ApiService _ApiService;
    private Spinner billTypesSpinner;

    TextView FilterStartDate;
    TextView FilterEndDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_bills_list, container, false);


    }

    @Override
    public void onResume() {
        super.onResume();
        //Api Request Model
        GetBillListRequestModel model = new GetBillListRequestModel();
        model.setCount(StaticVars.ListItemsCount);
        model.setPage(1);
        GetBillsList(model);

        FilterEndDate=(TextView) getView().findViewById(R.id.bills_end_date);
        FilterStartDate=(TextView) getView().findViewById(R.id.bills_start_date);

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

        //prepare spinning
        billTypesSpinner = (Spinner) getView().findViewById(R.id.bill_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, StaticVars.BillTypesStrList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billTypesSpinner.setAdapter(adapter);

        Button button = (Button) getView().findViewById(R.id.set_filter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer id = StaticVars.BillTypes.get(billTypesSpinner.getSelectedItemPosition());
                GetBillListRequestModel model = new GetBillListRequestModel();
                model.setCount(StaticVars.ListItemsCount);
                model.setToDateFa(FilterEndDate.getText().toString());
                model.setFromDateFa(FilterStartDate.getText().toString());
                model.setPage(1);
                model.setBillType(id);
                GetBillsList(model);
            }
        });


    }

    public void GetBillsList(GetBillListRequestModel model) {


        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.GetBills(model).enqueue(new Callback<ResponseModel<ArrayList<BillModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<BillModel>>> call, Response<ResponseModel<ArrayList<BillModel>>> response) {
                if (response.body().getIsSuccess()) {
                    //Adapter List
                    ArrayList<ListAdapterModel> adapterModelsList = new ArrayList<ListAdapterModel>();

                    //Response Data = List of bills
                    ArrayList<BillModel> responseData = response.body().getData();
                    for (int i = 0; i < responseData.size(); i++) {
                        ListAdapterModel item = new ListAdapterModel();
                        item.setId(responseData.get(i).getId());
                        item.setText(responseData.get(i).getTitle());
                        adapterModelsList.add(item);
                    }

                    BillListAdapter adapter = new BillListAdapter(adapterModelsList, getActivity());
                    ListView lView = (ListView) getView().findViewById(R.id.bills_list_view);
                    lView.setAdapter(adapter);
                }

            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<BillModel>>> call, Throwable t) {

            }
        });
    }


}


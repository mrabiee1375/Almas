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

import com.example.almas.Adapters.BillListAdapter;
import com.example.almas.Adapters.ChargeListAdapter;
import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.BillModel;
import com.example.almas.Models.ChargeModel;
import com.example.almas.Models.GetBillListRequestModel;
import com.example.almas.Models.GetChargesRequestModel;
import com.example.almas.Models.ListAdapterModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.Utility;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentChargesList extends Fragment {

    private ApiService _ApiService;

    TextView FilterStartDate;
    TextView FilterEndDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_charges_list, container, false);


    }

    @Override
    public void onResume() {
        super.onResume();
        FilterEndDate=(TextView) getView().findViewById(R.id.charges_end_date);
        FilterStartDate=(TextView) getView().findViewById(R.id.charges_start_date);

        //Api Request Model
        GetChargesRequestModel model = new GetChargesRequestModel();
        model.setCount(StaticVars.ListItemsCount);
        model.setPage(1);
        GetChargesList(model);

        Button button = (Button) getView().findViewById(R.id.set_filter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetChargesRequestModel model = new GetChargesRequestModel();
                model.setFromDateFa(FilterStartDate.getText().toString());
                model.setToDateFa(FilterEndDate.getText().toString());
                model.setCount(StaticVars.ListItemsCount);
                model.setPage(1);
                GetChargesList(model);
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

    public void GetChargesList(GetChargesRequestModel model) {


        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        boolean justEnables=(StaticVars.IsAdmin?false:true);
        _ApiService.GetChargeRecords(model,justEnables).enqueue(new Callback<ResponseModel<ArrayList<ChargeModel>>>() {
            @Override
            public void onResponse(Call<ResponseModel<ArrayList<ChargeModel>>> call, Response<ResponseModel<ArrayList<ChargeModel>>> response) {
                if (response.body().getIsSuccess()) {
                    //Adapter List
                    ArrayList<ListAdapterModel> adapterModelsList = new ArrayList<ListAdapterModel>();

                    //Response Data = List of bills
                    ArrayList<ChargeModel> responseData = response.body().getData();
                    for (int i = 0; i < responseData.size(); i++) {
                        ListAdapterModel item = new ListAdapterModel();
                        item.setId(responseData.get(i).getId());
                        item.setStatus(responseData.get(i).getIsEnable()==true?"فعال":"غیرفعال");
                        item.setText(responseData.get(i).getTitle());
                        adapterModelsList.add(item);
                    }

                    ChargeListAdapter adapter = new ChargeListAdapter(adapterModelsList, getActivity());
                    ListView lView = (ListView) getView().findViewById(R.id.charges_list_view);
                    lView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ResponseModel<ArrayList<ChargeModel>>> call, Throwable t) {

            }
        });

    }


}


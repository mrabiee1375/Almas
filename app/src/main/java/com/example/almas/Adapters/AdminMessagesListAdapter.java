package com.example.almas.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.FragmentCreateAdminMessage;
import com.example.almas.FragmentCreateBill;
import com.example.almas.Models.ListAdapterModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminMessagesListAdapter extends ListAdapter {
    public AdminMessagesListAdapter(ArrayList<ListAdapterModel> list, Context context) {
        super(list, context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getText());

        TextView listItemStatus = (TextView) view.findViewById(R.id.list_item_status);
        listItemStatus.setText(list.get(position).getStatus());

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button) view.findViewById(R.id.delete_btn);
        Button updateBtn = (Button) view.findViewById(R.id.update_btn);
        deleteBtn.setText("تغییر وضعیت");
        if (!StaticVars.IsAdmin) {
            deleteBtn.setVisibility(View.GONE);
            listItemStatus.setVisibility(View.GONE);
            updateBtn.setText("مشاهده");
        } else {
            deleteBtn.setVisibility(View.VISIBLE);
            listItemStatus.setVisibility(View.VISIBLE);
            updateBtn.setText("مشاهده و ویرایش");
        }
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiService apiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
                apiService.ChangeAdminMessageStatus(list.get(position).getId()).enqueue(new Callback<ResponseModel<Boolean>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<Boolean>> call, Response<ResponseModel<Boolean>> response) {
                        if (response.body().getIsSuccess()) {
                            if (response.body().getData()) {
                                list.get(position).setStatus("فعال");
                                notifyDataSetChanged();
                            } else {
                                list.get(position).setStatus("غیرفعال");
                                notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<Boolean>> call, Throwable t) {

                    }
                });
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment newFragment = new FragmentCreateAdminMessage();
                Bundle bundle = new Bundle();
                bundle.putBoolean("update_flag", true);
                bundle.putInt("message_id", list.get(position).getId());
                newFragment.setArguments(bundle);
                FragmentTransaction transaction = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.fragmentFrame, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

            }
        });

        return view;
    }
}

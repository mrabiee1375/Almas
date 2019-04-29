package com.example.almas.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almas.FragmentCreateAdminMessage;
import com.example.almas.Models.ChargeDetailModel;
import com.example.almas.Models.ListAdapterModel;
import com.example.almas.R;

import java.util.ArrayList;

public class ChargeDetailListAdapter extends BaseAdapter implements android.widget.ListAdapter {
    protected ArrayList<ChargeDetailModel> list = new ArrayList<ChargeDetailModel>();
    protected Context context;

    public ChargeDetailListAdapter(ArrayList<ChargeDetailModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);

    }

    @Override
    public long getItemId(int pos) {
        return 0;
        //just return 0 if your list items do not have an Id variable.
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.charge_detail_item, null);
        }

        //Handle TextView and display string from your list
       final TextView chargeItemTitle = (EditText)view.findViewById(R.id.charge_detail_title);
        chargeItemTitle.setText(list.get(position).getTitle());

        final TextView chargeItemPrice= (EditText)view.findViewById(R.id.charge_detail_price);
        chargeItemPrice.setText(list.get(position).getPrice());


        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button) view.findViewById(R.id.charge_detail_delete);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();

            }
        });

        chargeItemTitle.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") ) {
                    //do your work here
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
                list.get(position).setTitle(chargeItemTitle.getText().toString());
            }
        });

        chargeItemPrice.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if(!s.equals("") ) {
                    //do your work here
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void afterTextChanged(Editable s) {
                list.get(position).setPrice(chargeItemPrice.getText().toString());
            }
        });


        return view;
    }
}
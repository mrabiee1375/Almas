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

import com.example.almas.FragmentCreateBill;
import com.example.almas.FragmentCreateCharge;
import com.example.almas.Models.ListAdapterModel;
import com.example.almas.R;

import java.util.ArrayList;

public class ChargeListAdapter extends ListAdapter {
    public ChargeListAdapter(ArrayList<ListAdapterModel> list, Context context)
    {
        super(list,context);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position).getText());

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button) view.findViewById(R.id.delete_btn);
        Button updateBtn = (Button)view.findViewById(R.id.update_btn);

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                list.remove(position);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Fragment newFragment = new FragmentCreateCharge();
                Bundle bundle = new Bundle();
                bundle.putBoolean("update_flag", true);
                bundle.putInt("charge_id", list.get(position).getId());
                newFragment.setArguments(bundle);
                FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();

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

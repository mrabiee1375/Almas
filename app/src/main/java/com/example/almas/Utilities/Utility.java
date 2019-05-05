package com.example.almas.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.StaticVars;
import com.example.almas.R;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Utility {
    public static void oprnCustomToast1(List<String> list, Context dialogContext) {
        AlertDialog.Builder dialogBuilder=new AlertDialog.Builder(dialogContext);
        dialogBuilder.setView(R.layout.custom_alert);
        final AlertDialog dialog=dialogBuilder.create();
        dialog.show();

        ListView listView = (ListView) dialog.findViewById(R.id.message_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        Button dialogOk = (Button) dialog.findViewById(R.id.dialog_ok);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height=list.size()*100;
        if(list.size()*100>500)
        {
            params.height=500;
        }
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
    public static void oprnCustomToast(List<String> list, Context dialogContext) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(dialogContext, android.R.layout.simple_list_item_1, list);
        final DialogPlus dialog = DialogPlus.newDialog(dialogContext)
                .setContentHolder(new ViewHolder(R.layout.custom_alert))
                .setExpanded(false)  // This will enable the expand feature, (similar to android L share dialog)
                .setGravity(Gravity.CENTER)
               . setInAnimation(R.anim.open_dialog_anim)
                .create();
        dialog.show();
        ListView listView = (ListView) dialog.findViewById(R.id.message_list_view);
        listView.setAdapter(adapter);
        Button dialogOk = (Button) dialog.findViewById(R.id.dialog_ok);
        dialogOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public static String EncodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public static boolean SetListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get padding
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //setDynamicHeight(listView);
            return true;

        } else {
            return false;
        }

    }
    //function that  prepare and show calendar view
    public static void ShowCalendar(Context context,final TextView textView) {
        PersianCalendar initDate = new PersianCalendar();
        initDate.setPersianDate(1370, 3, 13);
        PersianDatePickerDialog picker;
        picker = new PersianDatePickerDialog(context)
                .setPositiveButtonString("ثبت")
                .setNegativeButton("انصراف")
                .setTodayButton("امروز")
                .setTodayButtonVisible(true)
                .setInitDate(initDate)
                .setMaxYear(PersianDatePickerDialog.THIS_YEAR)
                .setMinYear(1300)
                .setActionTextColor(Color.GRAY)
                .setListener(new Listener() {
                    @Override
                    public void onDateSelected(PersianCalendar persianCalendar) {
                        textView.setText(persianCalendar.getPersianYear() + "/" + persianCalendar.getPersianMonth() + "/" + persianCalendar.getPersianDay());
                    }

                    @Override
                    public void onDismissed() {

                    }
                });

        picker.show();
    }
    public static void DisableEditText(EditText view)
    {
        view.setFocusable(false);
        view.setFocusableInTouchMode(false);
        view.setClickable(false);
    }
    public static void EnableEditText(EditText view)
    {
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setClickable(true);
    }

    public static void CallTextApi(ArrayList<String> text,String userId) {
        ApiService apiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        apiService.ReadText(text,userId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}

package com.example.almas.Utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import java.util.List;

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



}

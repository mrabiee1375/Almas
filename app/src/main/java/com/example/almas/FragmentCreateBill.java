package com.example.almas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.Utility;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.internal.Util;

public class FragmentCreateBill extends Fragment {
    private Spinner billTypesSpinner;
    private Button chooseImage;
    private ImageView imageView;
    private String Base64Image;

    private int IMAGE_REQUEST = 5;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_bill, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        imageView = (ImageView) getView().findViewById(R.id.bill_image);

        //prepare spinning
        billTypesSpinner = (Spinner) getView().findViewById(R.id.create_bill_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, StaticVars.BillTypesStrList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billTypesSpinner.setAdapter(adapter);

        chooseImage = (Button) getView().findViewById(R.id.choose_image);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImageFunc();
            }
        });
    }

    public void ChooseImageFunc() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // Log.d(TAG, String.valueOf(bitmap));
                imageView.setImageURI(uri);
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Base64Image = Utility.EncodeImage(selectedImage);
            } catch (Exception ex) {

            }
        }
    }
}

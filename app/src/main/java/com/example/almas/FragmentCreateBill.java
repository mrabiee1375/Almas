package com.example.almas;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.BillModel;
import com.example.almas.Models.CreateAndEditBillRequestModel;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.Utility;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

import ir.hamsaa.persiandatepicker.Listener;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.util.PersianCalendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCreateBill extends Fragment {
    private ApiService _ApiService;
    private Spinner billTypesSpinner;
    private Button chooseImage;
    private Button datePicker;
    private Button createBill;
    private ImageView imageView;
    private String base64Image;
    private TextView endDateView;
    private EditText title;
    private TextView createDateLable;
    private TextView billTypeLable;
    private TextView billTypeValue;
    private EditText billNumber;
    private EditText price;
    private int billId = 0;
    private int IMAGE_REQUEST = 5;
    private PersianCalendar initDate = new PersianCalendar();
    boolean updateBill = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            updateBill = getArguments().getBoolean("update_flag");
            billId = getArguments().getInt("bill_id");
        } catch (Exception ex) {
        }
        return inflater.inflate(R.layout.fragment_create_bill, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();


        imageView = (ImageView) getView().findViewById(R.id.bill_image);
        endDateView = (TextView) getView().findViewById(R.id.create_end_date);
        title = (EditText) getView().findViewById(R.id.create_bill_title);
        billNumber = (EditText) getView().findViewById(R.id.create_bill_number);
        price = (EditText) getView().findViewById(R.id.create_bill_price);
        billTypesSpinner = (Spinner) getView().findViewById(R.id.create_bill_types);
        chooseImage = (Button) getView().findViewById(R.id.choose_image);
        datePicker = (Button) getView().findViewById(R.id.create_date_picker);
        createBill = (Button) getView().findViewById(R.id.submit_create_bill);

        createDateLable = (TextView) getView().findViewById(R.id.create_date_lable);
        billTypeLable = (TextView) getView().findViewById(R.id.bill_type_lable);
        billTypeValue = (TextView) getView().findViewById(R.id.bill_type_value);


        endDateView.setText(initDate.getPersianYear() + "/" + initDate.getPersianMonth() + "/" + initDate.getPersianDay());


        if (StaticVars.IsAdmin) {
            AdminOperations();
        } else {
            NotAdminOperations();
        }
        if (updateBill && !StaticVars.ChooseImageFlag) {
            fillForm();
        }
        StaticVars.ChooseImageFlag=false;
        //prepare spinning
        billTypesSpinner = (Spinner) getView().findViewById(R.id.create_bill_types);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, StaticVars.BillTypesStrList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        billTypesSpinner.setAdapter(adapter);

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImageFunc();
            }
        });

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.ShowCalendar(getActivity(), endDateView);
            }
        });

        createBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateBill) {
                    SubmitUpdate();
                } else {
                    SubmitCreate();
                }
            }
        });
    }

    //create intent to choose image
    public void ChooseImageFunc() {
        StaticVars.ChooseImageFlag = true;
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_REQUEST);
    }


    //submit create bill model to api
    public void SubmitCreate() {

        CreateAndEditBillRequestModel model = prepareCreateModel();

        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.CreateBill(model).enqueue(new Callback<ResponseModel<BillModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<BillModel>> call, Response<ResponseModel<BillModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        Fragment newFragment = new FragmentBillsList();

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(R.id.fragmentFrame, newFragment);
                        //transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();
                    } else {
                        Utility.oprnCustomToast(response.body().getDetailMessages(), getActivity());
                    }
                } catch (Exception ex) {

                }

            }

            @Override
            public void onFailure(Call<ResponseModel<BillModel>> call, Throwable t) {

            }
        });

    }

    //Fill Create Or Edit Bill Model
    public CreateAndEditBillRequestModel prepareCreateModel() {
        CreateAndEditBillRequestModel model = new CreateAndEditBillRequestModel();
        model.setBase64Image(base64Image);
        model.setBillId(billId);
        model.setTitle(title.getText().toString());
        Integer id = StaticVars.BillTypes.get(billTypesSpinner.getSelectedItemPosition());
        model.setBillType(id);
        model.setPrice(!price.getText().toString().isEmpty() ? Integer.parseInt(price.getText().toString()) : null);
        model.setNumber(billNumber.getText().toString());
        model.setEndDateFa(endDateView.getText().toString());
        return model;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_REQUEST && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                imageView.setImageURI(uri);
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                base64Image = Utility.EncodeImage(selectedImage);

            } catch (Exception ex) {

            }
        }
    }

    //Update Region
    public void fillForm() {
        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.GetBillDetail(billId).enqueue(new Callback<ResponseModel<BillModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<BillModel>> call, Response<ResponseModel<BillModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        billNumber.setText(response.body().getData().getNumber());
                        billTypeValue.setText(StaticVars.BillKeyValueTypes.get(
                                response.body().getData().getBillType()
                        ));
                        title.setText(response.body().getData().getTitle());
                        price.setText(response.body().getData().getPrice().toString());
                        endDateView.setText(response.body().getData().getEndDateFa());
                        if (!response.body().getData().getImageAddress().isEmpty()) {
                            String imageUrl = StaticVars.BaseUrl.substring(0, StaticVars.BaseUrl.length() - 1) + response.body().getData().getImageAddress();
                            Picasso.with(getActivity()).load(imageUrl).into(imageView);

                        }
                    } else {
                        Utility.oprnCustomToast(response.body().getDetailMessages(), getActivity());
                    }
                } catch (Exception ex) {

                }

            }

            @Override
            public void onFailure(Call<ResponseModel<BillModel>> call, Throwable t) {

            }
        });
    }

    public void SubmitUpdate() {

        CreateAndEditBillRequestModel model = prepareCreateModel();

        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.UpdateBill(model).enqueue(new Callback<ResponseModel<BillModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<BillModel>> call, Response<ResponseModel<BillModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        Fragment newFragment = new FragmentBillsList();

                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack
                        transaction.replace(R.id.fragmentFrame, newFragment);
                        //transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();
                    } else {
                        Utility.oprnCustomToast(response.body().getDetailMessages(), getActivity());
                    }
                } catch (Exception ex) {

                }

            }

            @Override
            public void onFailure(Call<ResponseModel<BillModel>> call, Throwable t) {

            }
        });

    }

    public void NotAdminOperations() {
        chooseImage.setVisibility(View.GONE);
        datePicker.setVisibility(View.GONE);
        createBill.setVisibility(View.GONE);
        billTypesSpinner.setVisibility(View.GONE);
        billTypeLable.setVisibility(View.VISIBLE);
        billTypeValue.setVisibility(View.VISIBLE);
        createDateLable.setVisibility(View.VISIBLE);
        Utility.DisableEditText(title);
        Utility.DisableEditText(billNumber);
        Utility.DisableEditText(price);
    }

    public void AdminOperations() {
        createDateLable.setVisibility(View.GONE);
        billTypeLable.setVisibility(View.GONE);
        billTypeValue.setVisibility(View.GONE);
        chooseImage.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);
        createBill.setVisibility(View.VISIBLE);
        Utility.EnableEditText(title);
        Utility.EnableEditText(billNumber);
        Utility.EnableEditText(price);
    }


}

package com.example.almas;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.AdminMessageModel;
import com.example.almas.Models.CreateAndEditAdminMessageRequest;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Utilities.Utility;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentCreateAdminMessage extends Fragment {
    private ApiService _ApiService;
    private Button createAdminMessage;
    private EditText title;
    private EditText text;
    private int messageId;
    boolean updateMessage = false;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        try {
            updateMessage = getArguments().getBoolean("update_flag");
            messageId = getArguments().getInt("message_id");
        } catch (Exception ex) {

        }
        return inflater.inflate(R.layout.fragment_create_admin_message, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();


        text = (EditText) getView().findViewById(R.id.create_admin_message_Text);
        title = (EditText) getView().findViewById(R.id.create_admin_message_title);
        createAdminMessage = (Button) getView().findViewById(R.id.submit_create_admin_message);

        if(StaticVars.IsAdmin)
        {
            AdminOperations();
        }
        else{
            NotAdminOperations();
        }
        if (updateMessage) {
            fillForm();
        }

        createAdminMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (updateMessage) {
                    SubmitUpdate();
                } else {
                    SubmitCreate();
                }
            }
        });
    }

    //submit create bill model to api
    public void SubmitCreate() {

        CreateAndEditAdminMessageRequest model = prepareCreateModel();

        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.CreateAdminMessage(model).enqueue(new Callback<ResponseModel<AdminMessageModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<AdminMessageModel>> call, Response<ResponseModel<AdminMessageModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        Fragment newFragment = new FragmentAdminMessagesList();
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
            public void onFailure(Call<ResponseModel<AdminMessageModel>> call, Throwable t) {

            }
        });
    }

    //Fill Create Or Edit Bill Model
    public CreateAndEditAdminMessageRequest prepareCreateModel() {
        CreateAndEditAdminMessageRequest model = new CreateAndEditAdminMessageRequest();
        model.setTitle(title.getText().toString());
        model.setText(text.getText().toString());
        model.setId(messageId);
        return model;
    }

    //Update Region
    public void fillForm() {
        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);

        _ApiService.GetAdminMessageDetails(messageId).enqueue(new Callback<ResponseModel<AdminMessageModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<AdminMessageModel>> call, Response<ResponseModel<AdminMessageModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        text.setText(response.body().getData().getText());
                        title.setText(response.body().getData().getTitle());
                    } else {
                        Utility.oprnCustomToast(response.body().getDetailMessages(), getActivity());
                    }
                } catch (Exception ex) {

                }
            }

            @Override
            public void onFailure(Call<ResponseModel<AdminMessageModel>> call, Throwable t) {
            }
        });
    }


    public void SubmitUpdate() {

        CreateAndEditAdminMessageRequest model = prepareCreateModel();

        _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
        _ApiService.UpdateAdminMessage(model).enqueue(new Callback<ResponseModel<AdminMessageModel>>() {
            @Override
            public void onResponse(Call<ResponseModel<AdminMessageModel>> call, Response<ResponseModel<AdminMessageModel>> response) {
                try {
                    if (response.body().getIsSuccess()) {
                        Fragment newFragment = new FragmentAdminMessagesList();

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
            public void onFailure(Call<ResponseModel<AdminMessageModel>> call, Throwable t) {

            }
        });
    }

    public void NotAdminOperations()
    {
        createAdminMessage.setVisibility(View.INVISIBLE);
        Utility.DisableEditText(title);
        Utility.DisableEditText(text);

    }
    public void AdminOperations()
    {
        createAdminMessage.setVisibility(View.VISIBLE);
        Utility.EnableEditText(title);
        Utility.EnableEditText(text);

    }
}

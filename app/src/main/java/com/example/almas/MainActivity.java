package com.example.almas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.almas.Api.ApiService;
import com.example.almas.Api.RetrofitClient;
import com.example.almas.Models.ResponseModel;
import com.example.almas.Models.StaticVars;
import com.example.almas.Models.UploadProfileImageModel;
import com.example.almas.Models.UserModel;
import com.example.almas.Utilities.Utility;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ApiService _ApiService;
    ImageView menu_icon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private int IMAGE_REQUEST = 5;
    private int Profile_IMAGE_REQUEST = 6;
    ImageView imageProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);

        menu_icon = (ImageView) findViewById(R.id.open_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer);
        navigationView = (NavigationView) findViewById(R.id.menu_navigation);
        imageProfile = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.profile_image);
        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.signIn_mItem:
                        GoToSignIn();
                        break;
                    case R.id.logIn_mItem:
                        GoToLogIn();
                        break;
                    case R.id.signOut_mItem:
                        LogOut();
                        break;
                    case R.id.messages_mItem:
                        LoadFragment(new FragmentAdminMessagesList());
                        break;
                    case R.id.create_message_mItem:
                        LoadFragment(new FragmentCreateAdminMessage());
                        break;
                    case R.id.charges_mItem:
                        LoadFragment(new FragmentChargesList());
                        break;
                    case R.id.create_charge_mItem:
                        LoadFragment(new FragmentCreateCharge());
                        break;
                    case R.id.bills_mItem:
                        LoadFragment(new FragmentBillsList());
                        break;
                    case R.id.create_bill_mItem:
                        LoadFragment(new FragmentCreateBill());
                        break;

                }
                return false;
            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences("userDetailsShEditor", MODE_PRIVATE);
        String userDetailJson = sharedPreferences.getString("userDetails", "");

        if (!userDetailJson.isEmpty()) {
            Gson gson = new Gson();
            UserModel userModel = gson.fromJson(userDetailJson, UserModel.class);
            TextView fullNameTtextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.fullName_textView);
            String fullName = userModel.getFirstName() + " " + userModel.getLastName();
            fullNameTtextView.setText(fullName);
            AfterLoginOperations(userModel);
            StaticVars.UserModel = userModel;
            if(StaticVars.UserModel.getImagePath()!=null && !StaticVars.UserModel.getImagePath().isEmpty())
            {
                Picasso.with(MainActivity.this).load(StaticVars.UserModel.getImagePath()).into(imageProfile);
            }
        } else {
            BeforeLoginOperations();
        }

        //List<String> a=new ArrayList<String>();
        //a.add("A");
        //Utility.oprnCustomToast(a,MainActivity.this);


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            drawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            super.onBackPressed();
        }
    }

    public void GoToSignIn() {
        Intent intent = new Intent(MainActivity.this, SignInLayout.class);
        startActivity(intent);
        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    public void GoToLogIn() {
        Intent intent = new Intent(MainActivity.this, LogInLayout.class);
        startActivity(intent);
    }

    public void LogOut() {

        SharedPreferences.Editor sharedEditor = getSharedPreferences("userDetailsShEditor", MODE_PRIVATE).edit();
        sharedEditor.remove("userDetails");
        sharedEditor.commit();
        TextView fullNameTtextView = (TextView) navigationView.getHeaderView(0).findViewById(R.id.fullName_textView);
        fullNameTtextView.setText("");

        MenuItem logout_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.signOut_mItem);
        logout_mItem.setVisible(false);

        MenuItem signIn_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.signIn_mItem);
        MenuItem logIn_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.logIn_mItem);
        logIn_mItem.setVisible(true);
        signIn_mItem.setTitle("عضویت");

        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void LoadFragment(Fragment fragment) {
        // Create new fragment and transaction

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragmentFrame, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

        drawerLayout.closeDrawer(Gravity.RIGHT);
    }

    public void AfterLoginOperations(UserModel model) {
        StaticVars.IsAdmin = model.getIsAdmin();
        MenuItem signIn_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.signIn_mItem);
        signIn_mItem.setTitle("مشاهده مشخصات");
        MenuItem logIn_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.logIn_mItem);
        logIn_mItem.setVisible(false);

        MenuItem createMessage_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.create_message_mItem);
        MenuItem createCharge_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.create_charge_mItem);
        MenuItem createBill_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.create_bill_mItem);
        MenuItem bills_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.bills_mItem);
        MenuItem charges_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.charges_mItem);
        MenuItem message_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.messages_mItem);
        bills_mItem.setVisible(true);
        charges_mItem.setVisible(true);
        message_mItem.setVisible(true);

        if (!model.getIsAdmin()) {
            createMessage_mItem.setVisible(false);
            createCharge_mItem.setVisible(false);
            createBill_mItem.setVisible(false);
        } else {
            createMessage_mItem.setVisible(true);
            createCharge_mItem.setVisible(true);
            createBill_mItem.setVisible(true);
        }

        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseImageFunc();
            }
        });
    }

    public void BeforeLoginOperations() {
        MenuItem logout_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.signOut_mItem);
        MenuItem createMessage_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.create_message_mItem);
        MenuItem createCharge_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.create_charge_mItem);
        MenuItem createBill_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.create_bill_mItem);
        MenuItem bills_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.bills_mItem);
        MenuItem charges_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.charges_mItem);
        MenuItem message_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.messages_mItem);
        bills_mItem.setVisible(false);
        charges_mItem.setVisible(false);
        message_mItem.setVisible(false);
        logout_mItem.setVisible(false);
        createMessage_mItem.setVisible(false);
        createCharge_mItem.setVisible(false);
        createBill_mItem.setVisible(false);
    }

    //create intent to choose image
    public void ChooseImageFunc() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), Profile_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Profile_IMAGE_REQUEST && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // Log.d(TAG, String.valueOf(bitmap));
                final InputStream imageStream = this.getContentResolver().openInputStream(uri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String base64Image = Utility.EncodeImage(selectedImage);

                UploadProfileImageModel model=new UploadProfileImageModel();
                model.setBase64Image(base64Image);
                model.setUserName(StaticVars.UserModel.getUserName());
                _ApiService = RetrofitClient.getAPIService(StaticVars.BaseUrl);
                _ApiService.UploadImage(model).enqueue(new Callback<ResponseModel<String>>() {
                    @Override
                    public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> response) {
                        if (response.body().getIsSuccess()) {
                            String imageUrl = StaticVars.BaseUrl.substring(0, StaticVars.BaseUrl.length() - 1) + response.body().getData();
                            Picasso.with(MainActivity.this).load(imageUrl).into(imageProfile);
                            StaticVars.UserModel.setImagePath(imageUrl);

                            Gson gson = new Gson();
                            String jsonObj = gson.toJson(StaticVars.UserModel);
                            SharedPreferences.Editor sharedEditor = getSharedPreferences("userDetailsShEditor", MODE_PRIVATE).edit();
                            sharedEditor.putString("userDetails", jsonObj);
                            sharedEditor.commit();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseModel<String>> call, Throwable t) {

                    }
                });

            } catch (Exception ex) {

            }
        }
    }

}

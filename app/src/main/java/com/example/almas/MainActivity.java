package com.example.almas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.almas.Models.UserModel;
import com.example.almas.Utilities.Utility;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ImageView menu_icon;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_layout);


        menu_icon = (ImageView) findViewById(R.id.open_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.menu_drawer);
        navigationView = (NavigationView) findViewById(R.id.menu_navigation);
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
            MenuItem signIn_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.signIn_mItem);
            MenuItem logIn_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.logIn_mItem);
            logIn_mItem.setVisible(false);
            signIn_mItem.setTitle("مشاهده مشخصات");
        } else {
            MenuItem logout_mItem = (MenuItem) navigationView.getMenu().findItem(R.id.signOut_mItem);
            logout_mItem.setVisible(false);

        }

        //List<String> a=new ArrayList<String>();
        //a.add("A");
        //Utility.oprnCustomToast(a,MainActivity.this);

        // Create new fragment and transaction
        Fragment newFragment = new FragmentCreateAdminMessage();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

         // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.fragmentFrame, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
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

    public void GoToActivatinLayout() {
        Intent intent = new Intent(MainActivity.this, ActivationCodeLayout.class);
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
    }




}

package com.mobile.midterm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.gson.Gson;
import com.mobile.midterm.dao.AuthenticationDAO;
import com.mobile.midterm.databinding.ActivityMainBinding;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.Utils;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AuthenticationDAO auth = AuthenticationDAO.getInstance();
    private SharedPreferences sharedPreferences;
    private ArrayList<User> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FirebaseApp.initializeApp(this);
        sharedPreferences = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        User signInUser = new Gson().fromJson(sharedPreferences.getString(getString(R.string.user_key), null), User.class);
        if(signInUser != null){
            if(signInUser.getRole().equalsIgnoreCase("manager")){
                navigateToManager();
            }
            else {
                navigateToHome();
            }
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();


        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.emailTxt.getText().toString();
                String password = binding.passwordTxt.getText().toString();
                if(email.isEmpty() || password.isEmpty()){
                    Utils.showAlertDialog( Gravity.CENTER, getString(R.string.please_fill_info), MainActivity.this);
                }
                else{
                    binding.loadingIndicator.setVisibility(View.VISIBLE);
                     auth.login(email, password).thenAcceptAsync(
                        user->{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.loadingIndicator.setVisibility(View.GONE);
                                    String userJson = new Gson().toJson(user);
                                    editor.putString(getString(R.string.user_key), userJson);
                                    editor.apply();
                                    if(user.getRole().equalsIgnoreCase("manager")){
                                        navigateToManager();
                                    }
                                    else{
                                        navigateToHome();
                                    }
                                }
                            });
                        }
                    ).exceptionally(
                        e ->{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    binding.loadingIndicator.setVisibility(View.GONE);
                                    Utils.showAlertDialog(Gravity.CENTER, getString(R.string.wrong_credentials), MainActivity.this);
                                }
                            });
                                return null;
                            }
                     );
                }
            }
        });
    }

    private void navigateToHome(){
        Intent homeRoute = new Intent(this, HomeActivity.class);
        homeRoute.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(homeRoute);
    }

    private void navigateToManager(){
        Intent managerRoute = new Intent(this, ManagerHomeActivity.class);
        managerRoute.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(managerRoute);
    }
}
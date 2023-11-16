package com.mobile.midterm;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.mobile.midterm.dao.UserDAO;
import com.mobile.midterm.databinding.ActivityUpdateProfileBinding;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.Utils;
import com.squareup.picasso.Picasso;

public class UpdateProfileActivity extends AppCompatActivity {
    private ActivityUpdateProfileBinding binding;
    private SharedPreferences sharedPreferences;
    private UserDAO userDAO = UserDAO.getInstance();
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        user = getIntent().getParcelableExtra("user");
        if(user != null){
            Picasso.get().load(user.getImage()).placeholder(R.drawable.cupertino_activity_indicator).error(R.drawable.close).fit().into(binding.profileImage);
            binding.fullNameEdt.setText(user.getFullName());
            binding.usernameEdt.setText(user.getUsername());
            binding.emailEdt.setText(user.getEmail());
            binding.ageEdt.setText(String.valueOf(user.getAge()));
            binding.phoneNumTxt.setText(user.getPhoneNum());
        }
        else{
            finish();
        }
        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.usernameTextInput.setError("");
                binding.usernameTextInput.setErrorEnabled(false);
                binding.ageTextInput.setError("");
                binding.ageTextInput.setErrorEnabled(false);
                binding.fullNameTextInput.setError("");
                binding.fullNameTextInput.setErrorEnabled(false);
                binding.phoneNumTextInput.setError("");
                binding.phoneNumTextInput.setErrorEnabled(false);
                binding.emailTextInput.setError("");
                binding.emailTextInput.setErrorEnabled(false);
                String username = binding.usernameEdt.getText().toString();
                String age = binding.ageEdt.getText().toString();
                String fullName = binding.fullNameEdt.getText().toString();
                String phoneNum = binding.phoneNumTxt.getText().toString();
                String email = binding.emailEdt.getText().toString();
                if(username.isEmpty()){
                    binding.usernameTextInput.setErrorEnabled(true);
                    binding.usernameTextInput.setError(getString(R.string.fill_username));
                    return;
                }
                if(age.isEmpty()){
                    binding.ageTextInput.setErrorEnabled(true);
                    binding.ageTextInput.setError(getString(R.string.fill_age));
                    return;
                }else if(Integer.parseInt(age) <= 0){
                    binding.ageTextInput.setErrorEnabled(true);
                    binding.ageTextInput.setError(getString(R.string.valid_age));
                    return;
                }

                if(fullName.isEmpty()){
                    binding.fullNameTextInput.setErrorEnabled(true);
                    binding.fullNameTextInput.setError(getString(R.string.fill_name));
                    return;
                }

                if(phoneNum.isEmpty()){
                    binding.phoneNumTextInput.setErrorEnabled(true);
                    binding.phoneNumTextInput.setError(getString(R.string.fill_phone));
                    return;
                }

                if(email.isEmpty()){
                    binding.emailTextInput.setErrorEnabled(true);
                    binding.emailTextInput.setError(getString(R.string.fill_email));
                    return;
                }

                binding.loadingIndicator.setVisibility(View.VISIBLE);
                binding.blurredBackground.setVisibility(View.VISIBLE);

                userDAO.updateProfile(user.getId(), username, email, Integer.parseInt(age), fullName, phoneNum).thenAcceptAsync(
                        it->{
                            if(it != null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.blurredBackground.setVisibility(View.GONE);
                                        binding.loadingIndicator.setVisibility(View.GONE);
                                        Intent returnIntent = new Intent();
                                        returnIntent.putExtra("user", it);
                                        setResult(Activity.RESULT_OK, returnIntent);
                                        finish();
                                    }
                                });
                            }
                        }
                ).exceptionally(
                        e->{
                            binding.blurredBackground.setVisibility(View.GONE);
                            binding.loadingIndicator.setVisibility(View.GONE);
                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.update_user_failed), UpdateProfileActivity.this);
                            return null;
                        }
                );

            }
        });
    }
}
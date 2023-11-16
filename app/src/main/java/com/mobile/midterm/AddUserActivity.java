package com.mobile.midterm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.mobile.midterm.dao.UserDAO;
import com.mobile.midterm.databinding.ActivityAddUserBinding;
import com.mobile.midterm.utils.Utils;

public class AddUserActivity extends AppCompatActivity {
    private ActivityAddUserBinding binding;
    private UserDAO userDAO = UserDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                binding.passwordTextInput.setError("");
                binding.passwordTextInput.setErrorEnabled(false);
                String username = binding.usernameEdt.getText().toString();
                String age = binding.ageEdt.getText().toString();
                String fullName = binding.fullNameEdt.getText().toString();
                String phoneNum = binding.phoneNumTxt.getText().toString();
                String password = binding.passwordEdt.getText().toString();
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

                if(password.isEmpty()){
                    binding.passwordTextInput.setErrorEnabled(true);
                    binding.passwordTextInput.setError(getString(R.string.fill_password));
                    return;
                }

                if(email.isEmpty()){
                    binding.emailTextInput.setErrorEnabled(true);
                    binding.emailTextInput.setError(getString(R.string.fill_email));
                    return;
                }

                if(binding.activateRadio.getCheckedRadioButtonId() == -1){
                    Utils.showSnackBar(binding.getRoot(), getString(R.string.fill_activate));
                    return;
                }
                boolean activated = binding.activatedCheck.isChecked();
                binding.loadingIndicator.setVisibility(View.VISIBLE);

                userDAO.addManager(username,phoneNum, email, activated, Integer.parseInt(age), fullName, password).thenAcceptAsync(
                    (it) -> {
                        runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    binding.loadingIndicator.setVisibility(View.GONE);
                                    Utils.showAlertDialog(Gravity.CENTER, getString(R.string.add_user_success), AddUserActivity.this);
                                }
                            }
                        );
                    }
                ).exceptionally(
                    throwable -> {
                        binding.loadingIndicator.setVisibility(View.GONE);
                        Utils.showAlertDialog(Gravity.CENTER, getString(R.string.add_user_unsuccessfully), AddUserActivity.this);
                        return null;
                    }
                );
            }
        });
    }
}
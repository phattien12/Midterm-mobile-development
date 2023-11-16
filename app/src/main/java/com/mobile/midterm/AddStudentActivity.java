package com.mobile.midterm;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.midterm.dao.UserDAO;
import com.mobile.midterm.databinding.ActivityAddStudentBinding;
import com.mobile.midterm.utils.Utils;

public class AddStudentActivity extends AppCompatActivity {
    private ActivityAddStudentBinding binding;
    private UserDAO userDAO = UserDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddStudentBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        binding.returnButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.submitBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.usernameUserTextInput.setError("");
                binding.usernameUserTextInput.setErrorEnabled(false);
                binding.ageUserTextInput.setError("");
                binding.ageUserTextInput.setErrorEnabled(false);
                binding.fullNameUserTextInput.setError("");
                binding.fullNameUserTextInput.setErrorEnabled(false);
                binding.phoneNumUserTextInput.setError("");
                binding.phoneNumUserTextInput.setErrorEnabled(false);
                binding.emailUserTextInput.setError("");
                binding.emailUserTextInput.setErrorEnabled(false);
                binding.passwordUserTextInput.setError("");
                binding.passwordUserTextInput.setErrorEnabled(false);
                String username = binding.usernameUserEdt.getText().toString();
                String age = binding.ageUserEdt.getText().toString();
                String fullName = binding.fullNameUserEdt.getText().toString();
                String phoneNum = binding.phoneNumUserTxt.getText().toString();
                String password = binding.passwordUserEdt.getText().toString();
                String email = binding.emailUserEdt.getText().toString();
                if (username.isEmpty()) {
                    binding.usernameUserTextInput.setErrorEnabled(true);
                    binding.usernameUserTextInput.setError(getString(R.string.fill_username));
                    return;
                }
                if (age.isEmpty()) {
                    binding.ageUserTextInput.setErrorEnabled(true);
                    binding.ageUserTextInput.setError(getString(R.string.fill_age));
                    return;
                } else if (Integer.parseInt(age) <= 0) {
                    binding.ageUserTextInput.setErrorEnabled(true);
                    binding.ageUserTextInput.setError(getString(R.string.valid_age));
                    return;
                }

                if (fullName.isEmpty()) {
                    binding.fullNameUserTextInput.setErrorEnabled(true);
                    binding.fullNameUserTextInput.setError(getString(R.string.fill_name));
                    return;
                }

                if (phoneNum.isEmpty()) {
                    binding.phoneNumUserTextInput.setErrorEnabled(true);
                    binding.phoneNumUserTextInput.setError(getString(R.string.fill_phone));
                    return;
                }

                if (password.isEmpty()) {
                    binding.passwordUserTextInput.setErrorEnabled(true);
                    binding.passwordUserTextInput.setError(getString(R.string.fill_password));
                    return;
                }

                if (email.isEmpty()) {
                    binding.emailUserTextInput.setErrorEnabled(true);
                    binding.emailUserTextInput.setError(getString(R.string.fill_email));
                    return;
                }

                if (binding.activateRadioUser.getCheckedRadioButtonId() == -1) {
                    Utils.showSnackBar(binding.getRoot(), getString(R.string.fill_activate));
                    return;
                }
                boolean activated = binding.activatedCheckUser.isChecked();
                binding.loadingIndicatorUser.setVisibility(View.VISIBLE);

                userDAO.addStudent(username, phoneNum, email, activated, Integer.parseInt(age), fullName, password).thenAcceptAsync(
                        (it) -> {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.loadingIndicatorUser.setVisibility(View.GONE);
                                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.add_user_success), AddStudentActivity.this);
                                        }
                                    }
                            );
                        }
                ).exceptionally(
                        throwable -> {
                            binding.loadingIndicatorUser.setVisibility(View.GONE);
                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.add_user_fail), AddStudentActivity.this);
                            return null;
                        }
                );
            }
        });
    }
}
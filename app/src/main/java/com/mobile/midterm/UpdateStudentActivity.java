package com.mobile.midterm;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobile.midterm.dao.UserDAO;
import com.mobile.midterm.databinding.ActivityUpdateStudentBinding;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.Utils;
import com.squareup.picasso.Picasso;

public class UpdateStudentActivity extends AppCompatActivity {

    private ActivityUpdateStudentBinding binding;
    private UserDAO userDAO = UserDAO.getInstance();
    private User currentUser;
    private static final int PICK_IMAGE_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateStudentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        User data = getIntent().getParcelableExtra("student");
        if (data != null) {
            currentUser = data;
            if (data.getImage() != null) {
                Picasso.get().load(data.getImage())
                        .placeholder(R.drawable.cupertino_activity_indicator)
                        .error(R.drawable.close).fit().into(binding.profileImage);
            }
            binding.fullNameEdt.setText(data.getFullName());
            binding.emailEdt.setText(data.getEmail());
            binding.ageEdt.setText(data.getAge() + "");
            binding.usernameEdt.setText(data.getUsername());
            binding.phoneNumTxt.setText(data.getPhoneNum());
            binding.passwordEdt.setText(data.getPassword());
            binding.activatedCheck.setChecked(data.isActivated());
            binding.deactivatedCheck.setChecked(!data.isActivated());
        }

        binding.pickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickingImage = new Intent(Intent.ACTION_PICK);
                pickingImage.setType("image/*");
                pickingImage.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                startActivityForResult(pickingImage, PICK_IMAGE_REQUEST);
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
                if (username.isEmpty()) {
                    binding.usernameTextInput.setErrorEnabled(true);
                    binding.usernameTextInput.setError(getString(R.string.fill_username));
                    return;
                }
                if (age.isEmpty()) {
                    binding.ageTextInput.setErrorEnabled(true);
                    binding.ageTextInput.setError(getString(R.string.fill_age));
                    return;
                } else if (Integer.parseInt(age) <= 0) {
                    binding.ageTextInput.setErrorEnabled(true);
                    binding.ageTextInput.setError(getString(R.string.valid_age));
                    return;
                }

                if (fullName.isEmpty()) {
                    binding.fullNameTextInput.setErrorEnabled(true);
                    binding.fullNameTextInput.setError(getString(R.string.fill_name));
                    return;
                }

                if (phoneNum.isEmpty()) {
                    binding.phoneNumTextInput.setErrorEnabled(true);
                    binding.phoneNumTextInput.setError(getString(R.string.fill_phone));
                    return;
                }

                if (password.isEmpty()) {
                    binding.passwordTextInput.setErrorEnabled(true);
                    binding.passwordTextInput.setError(getString(R.string.fill_password));
                    return;
                }

                if (email.isEmpty()) {
                    binding.emailTextInput.setErrorEnabled(true);
                    binding.emailTextInput.setError(getString(R.string.fill_email));
                    return;
                }

                if (binding.activateRadio.getCheckedRadioButtonId() == -1) {
                    Utils.showSnackBar(binding.getRoot(), getString(R.string.fill_activate));
                    return;
                }
                boolean activated = binding.activatedCheck.isChecked();
                binding.loadingIndicator.setVisibility(View.VISIBLE);
                binding.blurredBackground.setVisibility(View.VISIBLE);

                userDAO.updateStudent(currentUser.getId(), username, phoneNum, email, activated, Integer.parseInt(age), fullName, password).thenAcceptAsync(
                        it -> {
                            if (it != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.blurredBackground.setVisibility(View.GONE);
                                        binding.loadingIndicator.setVisibility(View.GONE);
                                        Utils.showAlertDialog(Gravity.CENTER, getString(R.string.update_user_success), UpdateStudentActivity.this);
                                    }
                                });
                            }
                        }
                ).exceptionally(
                        e -> {
                            binding.blurredBackground.setVisibility(View.GONE);
                            binding.loadingIndicator.setVisibility(View.GONE);
                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.update_user_failed), UpdateStudentActivity.this);
                            return null;
                        }
                );

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            if (selectedImageUri == null) {
                return;
            }
            binding.loadingIndicator.setVisibility(View.VISIBLE);
            binding.blurredBackground.setVisibility(View.VISIBLE);
            userDAO.updateUserImage(selectedImageUri, currentUser.getId()).thenAcceptAsync(it -> {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.blurredBackground.setVisibility(View.GONE);
                        binding.loadingIndicator.setVisibility(View.GONE);
                        Picasso.get().load(it).error(R.drawable.close).fit().into(binding.profileImage);
                        Utils.showAlertDialog(Gravity.CENTER, getString(R.string.upload_image_success), UpdateStudentActivity.this);
                    }
                });
            }).exceptionally(
                    e -> {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                binding.blurredBackground.setVisibility(View.GONE);
                                binding.loadingIndicator.setVisibility(View.GONE);
                                Utils.showAlertDialog(Gravity.CENTER, getString(R.string.upload_image_failed), UpdateStudentActivity.this);
                            }
                        });
                        return null;
                    }
            );
        }
    }
}
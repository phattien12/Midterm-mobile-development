package com.mobile.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobile.midterm.dao.CertificationDAO;
import com.mobile.midterm.dao.UserCertificationDAO;
import com.mobile.midterm.databinding.ActivityUpdateUserCertificationBinding;
import com.mobile.midterm.model.Certification;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.Utils;
import com.mobile.midterm.viewmodel.HomeDataViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class UpdateUserCertificationActivity extends AppCompatActivity {

    private ActivityUpdateUserCertificationBinding binding;
    private CertificationDAO certificationDAO = CertificationDAO.getInstance();
    private UserCertificationDAO userCertificationDAO = UserCertificationDAO.getInstance();
    private Certification currentCertification;
    private String certification;
    private String userCertification;
    private User currentUser;
    private HomeDataViewModel homeDataViewModel;

    private void getVM() {
        homeDataViewModel = new ViewModelProvider(this).get(HomeDataViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateUserCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getCertificationCurrentActive();
        getVM();
        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Certification data = getIntent().getParcelableExtra("certification");
        String[] ids = data.getId().split("-");
        if (data != null) {
            userCertification = ids[1];
            data.setId(ids[0]);
            currentCertification = data;
            binding.certificateDetailEdt.setText(data.getDetail());
            binding.activatedCheck.setChecked(data.isActivated());
            binding.deactivatedCheck.setChecked(!data.isActivated());
        }
        User data2 = getIntent().getParcelableExtra("user");
        if (data2 != null) {
            currentUser = data2;
        }
        binding.userIdEdt.setText(currentUser.getFullName() + " - " + currentUser.getId());

        Spinner spinner = binding.certification;
        homeDataViewModel.getCertificationList().observe(UpdateUserCertificationActivity.this, list -> {
            String[] options = list.stream().filter(x -> x.isActivated()).map(o -> o.getName() + " - " + o.getId()).collect(Collectors.toList()).toArray(new String[list.size()]);
            ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(UpdateUserCertificationActivity.this, android.R.layout.simple_spinner_item, options);
            adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpin);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    certification = options[position].split(" - ")[1];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    List<String> value = Arrays.stream(options).filter(o -> o.split(" - ")[1] == currentCertification.getId()).collect(Collectors.toList());
                    certification = value.size() == 0 ? options[0] : value.get(0).split(" - ")[1];
                }
            });
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String detail = binding.certificateDetailEdt.getText().toString();
                if (binding.activateRadio.getCheckedRadioButtonId() == -1) {
                    Utils.showSnackBar(binding.getRoot(), getString(R.string.fill_activate));
                    return;
                }
                boolean activated = binding.activatedCheck.isChecked();
                binding.loadingIndicator.setVisibility(View.VISIBLE);
                binding.blurredBackground.setVisibility(View.VISIBLE);
                userCertificationDAO.updateUserCertification(userCertification, certification, currentUser.getId(), detail, activated).thenAcceptAsync(
                        it -> {
                            if (it != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.blurredBackground.setVisibility(View.GONE);
                                        binding.loadingIndicator.setVisibility(View.GONE);
                                        Utils.showAlertDialog(Gravity.CENTER, getString(R.string.update_user_certification_success), UpdateUserCertificationActivity.this);
                                    }
                                });
                            }
                        }
                ).exceptionally(
                        e -> {
                            binding.blurredBackground.setVisibility(View.GONE);
                            binding.loadingIndicator.setVisibility(View.GONE);
                            HashMap<String, Object> updates = new HashMap<>();
                            updates.put("id", userCertification);
                            updates.put("certificationId", certification);
                            updates.put("userId", currentUser.getId());
                            updates.put("detail", detail);
                            updates.put("activated", activated);
                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.update_user_certification_failed) + "\n" + ids[0] + "-" + ids[1], UpdateUserCertificationActivity.this);
                            return null;
                        }
                );

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getCertificationCurrentActive() {
        certificationDAO.getAllCertification().thenAcceptAsync(t -> {
            homeDataViewModel.setCertificationList(new ArrayList<>(t));
        }).exceptionally((t) -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showSnackBar(binding.getRoot(), t.getMessage());
                }
            });
            return null;
        });
    }
}
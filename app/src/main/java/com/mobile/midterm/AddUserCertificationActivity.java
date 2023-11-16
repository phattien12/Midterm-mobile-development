package com.mobile.midterm;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mobile.midterm.dao.CertificationDAO;
import com.mobile.midterm.dao.UserCertificationDAO;
import com.mobile.midterm.databinding.ActivityAddUserCertificationBinding;
import com.mobile.midterm.model.Certification;
import com.mobile.midterm.model.User;
import com.mobile.midterm.model.UserCertification;
import com.mobile.midterm.utils.Utils;
import com.mobile.midterm.viewmodel.HomeDataViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AddUserCertificationActivity extends AppCompatActivity {
    private ActivityAddUserCertificationBinding binding;
    private HomeDataViewModel homeDataViewModel;
    private UserCertificationDAO userCertificationDAO = UserCertificationDAO.getInstance();
    private String certification;
    private CertificationDAO certificationDAO = CertificationDAO.getInstance();

    private void getVM() {
        homeDataViewModel = new ViewModelProvider(this).get(HomeDataViewModel.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddUserCertificationBinding.inflate(getLayoutInflater());
        getVM();

        setContentView(binding.getRoot());
        getCertificationCurrentActive();
        User user = getIntent().getParcelableExtra("user");
        if (user != null)
            binding.userIdEdt.setText(user.getFullName() + " - " + user.getId());
        else
            finish();
        binding.returnButtonUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Spinner spinner = binding.certification;
        homeDataViewModel.getCertificationList().observe(AddUserCertificationActivity.this, list -> {
            String[] options = list.stream().filter(x -> x.isActivated()).map(o -> o.getName() + " - " + o.getId()).collect(Collectors.toList()).toArray(new String[list.size()]);
            ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(AddUserCertificationActivity.this, android.R.layout.simple_spinner_item, options);
            adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapterSpin);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    certification = options[position].split(" - ")[1];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    certification = options[0].split(" - ")[1];
                }
            });
        });


        binding.submitBtnUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCertificationDAO.addUserCertification(certification, user.getId(), binding.certificateDetailEdt.getText().toString()).thenAcceptAsync(
                        (it) -> {
                            getCertificationCurrent(user.getId());
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.loadingIndicatorUser.setVisibility(View.GONE);
                                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.add_user_certification_success), AddUserCertificationActivity.this);
                                        }
                                    }
                            );
                        }
                ).exceptionally(
                        throwable -> {
                            binding.loadingIndicatorUser.setVisibility(View.GONE);
                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.add_user_certification_fail), AddUserCertificationActivity.this);
                            return null;
                        }
                );
            }
        });
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

    private void getCertificationCurrent(String userId) {
        try {
            userCertificationDAO
                    .getUserCertificationByUserId(userId)
                    .thenAcceptAsync(t -> {
                        homeDataViewModel.setUserCertificationListCurrent(t);
                        certificationDAO.getCertificationByIds(t.stream().map(u -> u.getCertificationId()).collect(Collectors.toList()))
                                .thenAcceptAsync(t2 -> {
                                    List<Certification> certifications = t2.stream().map(c -> {
                                        List<UserCertification> ucs = t.stream().filter(uc -> c.getId().equals(uc.getCertificationId())).collect(Collectors.toList());
                                        c.setId(c.getId() + "-" + ucs.get(0).getId());
                                        return c;
                                    }).collect(Collectors.toList());
                                    homeDataViewModel.setCertificationListCurrent(new ArrayList<>(certifications));
                                }).exceptionally((t2) -> {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Utils.showSnackBar(binding.getRoot(), t2.getMessage());
                                        }
                                    });
                                    return null;
                                });
                    }).exceptionally((t) -> {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Utils.showSnackBar(binding.getRoot(), t.getMessage());
                            }
                        });
                        return null;
                    });
        } catch (Exception e) {
            Utils.showAlertDialog(Gravity.CENTER, e.getMessage(), AddUserCertificationActivity.this);
        }
    }

}
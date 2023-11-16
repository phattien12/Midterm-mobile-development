package com.mobile.midterm;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.mobile.midterm.dao.CertificationDAO;
import com.mobile.midterm.databinding.ActivityAddCertificationBinding;
import com.mobile.midterm.utils.Utils;

public class AddCertificationActivity extends AppCompatActivity {
    private ActivityAddCertificationBinding binding;
    private CertificationDAO certificationDAO = CertificationDAO.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCertificationBinding.inflate(getLayoutInflater());

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

                binding.certificationNameTextInput.setError("");
                binding.certificationNameTextInput.setErrorEnabled(false);
                String name = binding.certificationNameEdt.getText().toString();
                String detail = binding.certificateDetailEdt.getText().toString();

                if (name.isEmpty()) {
                    binding.certificationNameTextInput.setErrorEnabled(true);
                    binding.certificationNameTextInput.setError(getString(R.string.fill_certificationName));
                    return;
                }

                if (binding.activateRadioUser.getCheckedRadioButtonId() == -1) {
                    Utils.showSnackBar(binding.getRoot(), getString(R.string.fill_activate));
                    return;
                }
                boolean activated = binding.activatedCheckUser.isChecked();
                binding.loadingIndicatorUser.setVisibility(View.VISIBLE);

                certificationDAO.addCertification(name, detail, activated).thenAcceptAsync(
                        (it) -> {
                            runOnUiThread(
                                    new Runnable() {
                                        @Override
                                        public void run() {
                                            binding.loadingIndicatorUser.setVisibility(View.GONE);
                                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.add_certification_success), AddCertificationActivity.this);
                                        }
                                    }
                            );
                        }
                ).exceptionally(
                        throwable -> {
                            binding.loadingIndicatorUser.setVisibility(View.GONE);
                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.add_certification_fail), AddCertificationActivity.this);
                            return null;
                        }
                );
            }
        });
    }
}
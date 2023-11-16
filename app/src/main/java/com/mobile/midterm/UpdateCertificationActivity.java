package com.mobile.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.mobile.midterm.dao.CertificationDAO;
import com.mobile.midterm.databinding.ActivityUpdateCertificationBinding;
import com.mobile.midterm.model.Certification;
import com.mobile.midterm.utils.Utils;

public class UpdateCertificationActivity extends AppCompatActivity {

    private ActivityUpdateCertificationBinding binding;
    private CertificationDAO certificationDAO = CertificationDAO.getInstance();
    private Certification currentCertification;
    private static final int PICK_IMAGE_REQUEST = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateCertificationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Certification data = getIntent().getParcelableExtra("certification");
        if (data != null) {
            currentCertification = data;
            binding.certificateNameEdt.setText(data.getName());
            binding.certificateDetailEdt.setText(data.getDetail());
            binding.activatedCheck.setChecked(data.isActivated());
            binding.deactivatedCheck.setChecked(!data.isActivated());
        }


        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.certificationNameTextInput.setError("");
                binding.certificationNameTextInput.setErrorEnabled(false);

                String name = binding.certificateNameEdt.getText().toString();
                String detail = binding.certificateDetailEdt.getText().toString();

                if (name.isEmpty()) {
                    binding.certificationNameTextInput.setErrorEnabled(true);
                    binding.certificationNameTextInput.setError(getString(R.string.fill_certificationName));
                    return;
                }

                if (binding.activateRadio.getCheckedRadioButtonId() == -1) {
                    Utils.showSnackBar(binding.getRoot(), getString(R.string.fill_activate));
                    return;
                }
                boolean activated = binding.activatedCheck.isChecked();
                binding.loadingIndicator.setVisibility(View.VISIBLE);
                binding.blurredBackground.setVisibility(View.VISIBLE);

                certificationDAO.updateCertification(currentCertification.getId(), name, detail, activated).thenAcceptAsync(
                        it -> {
                            if (it != null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.blurredBackground.setVisibility(View.GONE);
                                        binding.loadingIndicator.setVisibility(View.GONE);
                                        Utils.showAlertDialog(Gravity.CENTER, getString(R.string.update_certification_success), UpdateCertificationActivity.this);
                                    }
                                });
                            }
                        }
                ).exceptionally(
                        e -> {
                            binding.blurredBackground.setVisibility(View.GONE);
                            binding.loadingIndicator.setVisibility(View.GONE);
                            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.update_certification_failed), UpdateCertificationActivity.this);
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
}
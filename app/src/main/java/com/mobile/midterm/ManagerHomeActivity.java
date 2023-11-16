package com.mobile.midterm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mobile.midterm.dao.CertificationDAO;
import com.mobile.midterm.dao.UserDAO;
import com.mobile.midterm.databinding.ActivityManagerHomeBinding;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.OnDataChangeListener;
import com.mobile.midterm.utils.OnOpenDrawerListener;
import com.mobile.midterm.utils.OnSelectedBottomNavbarItem;
import com.mobile.midterm.utils.Utils;
import com.mobile.midterm.viewmodel.HomeDataViewModel;

import java.util.ArrayList;
import java.util.List;

public class ManagerHomeActivity extends AppCompatActivity implements OnSelectedBottomNavbarItem, OnOpenDrawerListener, OnDataChangeListener {
    private ActivityManagerHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private UserDAO userDAO = UserDAO.getInstance();
    private CertificationDAO certificationDAO = CertificationDAO.getInstance();
    private Fragment managerHomeFragment = new ManagerHomeFragment();
    private Fragment systemStudentFragment = new SystemStudentsFragment();
    private Fragment systemCertificationsFragment = new SystemCertificationsFragment();
    private Fragment profileFragment = new ProfileFragment();
    private Fragment activeFragment = managerHomeFragment;
    private HomeDataViewModel homeDataViewModel;

    private FragmentManager fragmentManager = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);

        homeDataViewModel = new ViewModelProvider(this).get(HomeDataViewModel.class);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.tempContainer, managerHomeFragment)
                .add(R.id.tempContainer, systemCertificationsFragment)
                .hide(systemCertificationsFragment)
                .add(R.id.tempContainer, systemStudentFragment)
                .hide(systemStudentFragment)
                .add(R.id.tempContainer, profileFragment)
                .hide(profileFragment)
                .commit();
        getUserJson();
        binding.navigationDrawerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navLogoutItem) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.user_key), null);
                    editor.apply();
                    Intent loginScreen = new Intent(ManagerHomeActivity.this, MainActivity.class);
                    loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginScreen);
                }
                binding.managerHomeLayout.closeDrawers();
                return true;
            }
        });
        binding.bottomNavbar.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homeTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(managerHomeFragment).commit();
                    activeFragment = managerHomeFragment;
                    return true;
                } else if (item.getItemId() == R.id.certificateTab) {
                    try {
                        fragmentManager.beginTransaction().hide(activeFragment).show(systemCertificationsFragment).commit();
                        activeFragment = systemCertificationsFragment;
                    } catch (Exception e) {
                        String t = e.getMessage();
                        e.printStackTrace();
                    }

                    return true;
                } else if (item.getItemId() == R.id.studentsListTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(systemStudentFragment).commit();
                    activeFragment = systemStudentFragment;
                    return true;
                } else if (item.getItemId() == R.id.profileTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit();
                    activeFragment = profileFragment;
                    return true;
                }
                return false;
            }
        });

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getUserJson() {
        String userJson = sharedPreferences.getString(getString(R.string.user_key), null);
        if (userJson == null) finish();
        else {
            homeDataViewModel.setUser(new Gson().fromJson(userJson, User.class));
        }
        userDAO.getSystemStudents().thenAcceptAsync(t -> {
            homeDataViewModel.setStudentsList(new ArrayList<>(t));
        }).exceptionally((t) -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showSnackBar(binding.getRoot(), t.getMessage());
                }
            });
            return null;
        });
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

    @Override
    public void onSelectedNavbarItem(int item) {
        if (item == R.id.homeTab) {
            binding.bottomNavbar.setSelectedItemId(R.id.homeTab);
        } else if (item == R.id.certificateTab) {
            binding.bottomNavbar.setSelectedItemId(R.id.certificateTab);
        } else if (item == R.id.studentsListTab) {
            binding.bottomNavbar.setSelectedItemId(R.id.studentsListTab);
        } else if (item == R.id.profileTab) {
            binding.bottomNavbar.setSelectedItemId(R.id.userProfileTab);
        } else {
            binding.bottomNavbar.setSelectedItemId(R.id.studentsListTab);
        }
    }

    @Override
    public void onDataChanged(List<User> users) {
        homeDataViewModel.setStudentsList(new ArrayList<User>(users));
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDAO.getSystemStudents().thenAcceptAsync(t -> {
            homeDataViewModel.setStudentsList(new ArrayList<>(t));
        }).exceptionally((t) -> {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Utils.showSnackBar(binding.getRoot(), t.getMessage());
                }
            });
            return null;
        });
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

    @Override
    public void openDrawer() {
        binding.managerHomeLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
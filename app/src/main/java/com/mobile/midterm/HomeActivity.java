package com.mobile.midterm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.mobile.midterm.dao.UserDAO;
import com.mobile.midterm.databinding.ActivityHomeBinding;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.OnDataChangeListener;
import com.mobile.midterm.utils.OnOpenDrawerListener;
import com.mobile.midterm.utils.OnSelectedBottomNavbarItem;
import com.mobile.midterm.utils.Utils;
import com.mobile.midterm.viewmodel.HomeDataViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements OnSelectedBottomNavbarItem, OnOpenDrawerListener, OnDataChangeListener {
    private ActivityHomeBinding binding;
    private SharedPreferences sharedPreferences;
    private UserDAO userDAO = UserDAO.getInstance();
    private Fragment homeFragment = new HomeFragment();
    private Fragment activeFragment = homeFragment;
    private Fragment systemUserFragment = new SystemUsersFragment();
    private Fragment profileFragment = new ProfileFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private HomeDataViewModel homeDataViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.tempContainer, homeFragment)
                .add(R.id.tempContainer, systemUserFragment)
                .hide(systemUserFragment)
                .add(R.id.tempContainer, profileFragment)
                .hide(profileFragment)
                .commit();

        homeDataViewModel = new ViewModelProvider(this).get(HomeDataViewModel.class);
        getUserJson();

        binding.navigationDrawerView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navLogoutItem) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.user_key), null);
                    editor.apply();
                    Intent loginScreen = new Intent(HomeActivity.this, MainActivity.class);
                    loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(loginScreen);
                }
                binding.homeActivityLayout.closeDrawers();
                return true;
            }
        });

        binding.bottomNavbar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.homeTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(homeFragment).commit();
                    activeFragment = homeFragment;
                    return true;
                } else if (item.getItemId() == R.id.systemUsersTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(systemUserFragment).commit();
                    activeFragment = systemUserFragment;
                    return true;
                } else if (item.getItemId() == R.id.userProfileTab) {
                    fragmentManager.beginTransaction().hide(activeFragment).show(profileFragment).commit();
                    activeFragment = profileFragment;
                    return true;
                }
                return false;
            }
        });
    }

    private void getUserJson() {
        String userJson = sharedPreferences.getString(getString(R.string.user_key), null);
        if (userJson == null) finish();
        else {
            homeDataViewModel.setUser(new Gson().fromJson(userJson, User.class));
        }
        userDAO.getSystemUsers().thenAcceptAsync(t -> {
            homeDataViewModel.setSystemUsersList(new ArrayList<>(t));
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
        } else if (item == R.id.systemUsersTab) {
            binding.bottomNavbar.setSelectedItemId(R.id.systemUsersTab);
        } else if (item == R.id.userProfileTab) {
            binding.bottomNavbar.setSelectedItemId(R.id.userProfileTab);
        } else {
            binding.bottomNavbar.setSelectedItemId(R.id.systemUsersTab);
        }
    }

    @Override
    public void onDataChanged(List<User> users) {
        homeDataViewModel.setSystemUsersList(new ArrayList<User>(users));
    }

    @Override
    protected void onResume() {
        super.onResume();
        userDAO.getSystemUsers().thenAcceptAsync(t -> {
            homeDataViewModel.setSystemUsersList(new ArrayList<>(t));
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
        binding.homeActivityLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
package com.mobile.midterm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.mobile.midterm.databinding.FragmentProfileBinding;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.Utils;
import com.mobile.midterm.viewmodel.HomeDataViewModel;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentProfileBinding binding;
    private HomeDataViewModel homeDataViewModel;
    private SharedPreferences sharedPreferences;
    private static final int UPDATE_PROFILE_REQUEST = 50;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private void getUserVM(){
        homeDataViewModel = new ViewModelProvider(requireActivity()).get(HomeDataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        getUserVM();
        homeDataViewModel.getUser().observe(getViewLifecycleOwner(), user -> {
            Picasso.get().load(user.getImage()).placeholder(R.drawable.cupertino_activity_indicator).error(R.drawable.close).fit().into(binding.profileImage);
            binding.fullNameTxt.setText(user.getFullName());
        });
        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_key), Context.MODE_PRIVATE);
        binding.logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(getString(R.string.user_key), null);
                editor.apply();
                Intent loginScreen = new Intent(getActivity(), MainActivity.class);
                loginScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(loginScreen);
            }
        });
        binding.profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToUpdateProfile();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == UPDATE_PROFILE_REQUEST && resultCode == Activity.RESULT_OK && data != null){
            User user = data.getParcelableExtra("user");
            homeDataViewModel.setUser(user);
            sharedPreferences.edit().putString(getString(R.string.user_key), new Gson().toJson(user)).apply();
            Utils.showAlertDialog(Gravity.CENTER, getString(R.string.update_profile_success), getActivity());
        }
    }

    private void navigateToUpdateProfile(){
        Intent updateProfile = new Intent(getActivity(),UpdateProfileActivity.class);
        updateProfile.putExtra("user", homeDataViewModel.getUser().getValue());
        startActivityForResult(updateProfile, UPDATE_PROFILE_REQUEST);
    }
}
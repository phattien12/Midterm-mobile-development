package com.mobile.midterm;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobile.midterm.databinding.FragmentManagerHomeBinding;
import com.mobile.midterm.utils.OnOpenDrawerListener;
import com.mobile.midterm.utils.OnSelectedBottomNavbarItem;
import com.mobile.midterm.viewmodel.HomeDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ManagerHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManagerHomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentManagerHomeBinding binding;
    private OnOpenDrawerListener onOpenDrawerListener;
    private OnSelectedBottomNavbarItem onSelectedBottomNavbarItem;
    private HomeDataViewModel homeDataViewModel;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnOpenDrawerListener && context instanceof OnSelectedBottomNavbarItem) {
            onOpenDrawerListener = (OnOpenDrawerListener) context;
            onSelectedBottomNavbarItem = (OnSelectedBottomNavbarItem) context;
        }
    }

    public ManagerHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManagerHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManagerHomeFragment newInstance(String param1, String param2) {
        ManagerHomeFragment fragment = new ManagerHomeFragment();
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

    private void getUserVM() {
        homeDataViewModel = new ViewModelProvider(requireActivity()).get(HomeDataViewModel.class);
        binding.helloText.setText(getString(R.string.hello) + ", " + homeDataViewModel.getUser().getValue().getFullName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentManagerHomeBinding.inflate(inflater, container, false);
        getUserVM();
        binding.drawerToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOpenDrawerListener.openDrawer();
            }
        });
        binding.viewStudentListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectedBottomNavbarItem.onSelectedNavbarItem(R.id.studentsListTab);
            }
        });
        binding.viewCertificationListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectedBottomNavbarItem.onSelectedNavbarItem(R.id.certificateTab);

            }
        });
        binding.addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addStudentScreen = new Intent(requireActivity(), AddStudentActivity.class);
                startActivity(addStudentScreen);
            }
        });
        binding.addCertificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addCertificationBtnScreen = new Intent(requireActivity(), AddCertificationActivity.class);
                startActivity(addCertificationBtnScreen);
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
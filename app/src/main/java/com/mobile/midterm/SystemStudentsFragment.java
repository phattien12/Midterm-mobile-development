package com.mobile.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mobile.midterm.adapter.SystemUserAdapter;
import com.mobile.midterm.databinding.FragmentSystemStudentsBinding;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.OnRecyclerViewItemClickListener;
import com.mobile.midterm.viewmodel.HomeDataViewModel;

import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SystemStudentsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SystemStudentsFragment extends Fragment implements OnRecyclerViewItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSystemStudentsBinding binding;
    private HomeDataViewModel homeDataViewModel;
    private SystemUserAdapter adapter;
    private String sortType = "";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SystemStudentsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SystemUsersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SystemStudentsFragment newInstance(String param1, String param2) {
        SystemStudentsFragment fragment = new SystemStudentsFragment();
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

    private void getVM() {
        homeDataViewModel = new ViewModelProvider(requireActivity()).get(HomeDataViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSystemStudentsBinding.inflate(inflater, container, false);
        getVM();
        adapter = new SystemUserAdapter(
                requireContext(),
                homeDataViewModel.getStudentsList().getValue(),
                R.layout.system_user_item, this);
        binding.systemUsersList.setHasFixedSize(true);
        binding.systemUsersList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        binding.systemUsersList.setAdapter(adapter);

        binding.buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textSearch = binding.searchText.getText().toString();
                homeDataViewModel.getStudentsList().observe(getViewLifecycleOwner(), list -> {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    adapter.setUsers(list.stream().filter(
                            user -> user.getId().contains(textSearch) ||
                                    user.getFullName().contains(textSearch) ||
                                    user.getEmail().contains(textSearch) ||
                                    user.getUsername().contains(textSearch) ||
                                    user.getPhoneNum().contains(textSearch)
                    ).sorted(new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            if (sortType == "Name")
                                return o1.getFullName().compareTo(o2.getFullName());
                            else if (sortType == "Phone")
                                return o1.getPhoneNum().compareTo(o2.getPhoneNum());
                            else if (sortType == "CreatedTime")
                                return new Date(o1.getCreatedDate()).compareTo(new Date(o2.getCreatedDate()));
                            else if (sortType == "Id")
                                return o1.getId().compareTo(o2.getId());
                            else return 1;
                        }
                    }).collect(Collectors.toList()));
                    binding.progressBar.setVisibility(View.GONE);
                });
            }
        });
        String[] options = {"ID", "Name", "Phone", "CreatedTime"};
        Spinner spinner = binding.sortType;
        ArrayAdapter<String> adapterSpin = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, options);
        adapterSpin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpin);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                sortType = options[position];
                String textSearch = binding.searchText.getText().toString();
                homeDataViewModel.getStudentsList().observe(getViewLifecycleOwner(), list -> {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    adapter.setUsers(list.stream().filter(
                            user -> user.getId().contains(textSearch) ||
                                    user.getFullName().contains(textSearch) ||
                                    user.getEmail().contains(textSearch) ||
                                    user.getUsername().contains(textSearch) ||
                                    user.getPhoneNum().contains(textSearch)
                    ).sorted(new Comparator<User>() {
                        @Override
                        public int compare(User o1, User o2) {
                            if (sortType == "Name")
                                return o1.getFullName().compareTo(o2.getFullName());
                            else if (sortType == "Phone")
                                return o1.getPhoneNum().compareTo(o2.getPhoneNum());
                            else if (sortType == "CreatedTime")
                                return new Date(o1.getCreatedDate()).compareTo(new Date(o2.getCreatedDate()));
                            else if (sortType == "Id")
                                return o1.getId().compareTo(o2.getId());
                            else return 1;
                        }
                    }).collect(Collectors.toList()));
                    binding.progressBar.setVisibility(View.GONE);
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                String textSearch = binding.searchText.getText().toString();
                homeDataViewModel.getStudentsList().observe(getViewLifecycleOwner(), list -> {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    adapter.setUsers(list.stream().filter(
                            user -> user.getId().contains(textSearch) ||
                                    user.getFullName().contains(textSearch) ||
                                    user.getEmail().contains(textSearch) ||
                                    user.getUsername().contains(textSearch) ||
                                    user.getPhoneNum().contains(textSearch)
                    ).collect(Collectors.toList()));
                    binding.progressBar.setVisibility(View.GONE);
                });
            }
        });

        homeDataViewModel.getStudentsList().observe(getViewLifecycleOwner(), list -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            adapter.setUsers(list);
            binding.progressBar.setVisibility(View.GONE);
        });
        return binding.getRoot();
    }

    @Override
    public void onItemClickListener(User position) {
        Intent intent = new Intent(requireActivity(), SystemUserCertificationsActivity.class);
        intent.putExtra("student", position);
        startActivity(intent);
    }
    
}
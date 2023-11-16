package com.mobile.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mobile.midterm.adapter.SystemUserAdapter;
import com.mobile.midterm.databinding.FragmentSystemUsersBinding;
import com.mobile.midterm.model.User;
import com.mobile.midterm.utils.OnRecyclerViewItemClickListener;
import com.mobile.midterm.viewmodel.HomeDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SystemUsersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SystemUsersFragment extends Fragment implements OnRecyclerViewItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSystemUsersBinding binding;
    private HomeDataViewModel homeDataViewModel;
    private SystemUserAdapter adapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SystemUsersFragment() {
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
    public static SystemUsersFragment newInstance(String param1, String param2) {
        SystemUsersFragment fragment = new SystemUsersFragment();
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
        binding = FragmentSystemUsersBinding.inflate(inflater, container, false);
        getVM();
        adapter = new SystemUserAdapter(
                requireContext(),
                homeDataViewModel.getSystemUsersList().getValue(),
                R.layout.system_user_item, this);
        binding.systemUsersList.setHasFixedSize(true);
        binding.systemUsersList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        binding.systemUsersList.setAdapter(adapter);

        homeDataViewModel.getSystemUsersList().observe(getViewLifecycleOwner(), list -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            adapter.setUsers(list);
            binding.progressBar.setVisibility(View.GONE);
        });

        return binding.getRoot();
    }

    @Override
    public void onItemClickListener(User position) {
        Intent intent = new Intent(requireActivity(), UpdateUserActivity.class);
        intent.putExtra("user", position);
        startActivity(intent);
    }
}
package com.mobile.midterm;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.mobile.midterm.adapter.SystemUserCertificationAdapter;
import com.mobile.midterm.databinding.FragmentSystemUserCertificationsBinding;
import com.mobile.midterm.model.Certification;
import com.mobile.midterm.utils.OnRecyclerViewItemCertificationClickListener;
import com.mobile.midterm.viewmodel.HomeDataViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SystemUserCertificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SystemUserCertificationsFragment extends Fragment implements OnRecyclerViewItemCertificationClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentSystemUserCertificationsBinding binding;
    private HomeDataViewModel homeDataViewModel;
    private SystemUserCertificationAdapter adapter;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SystemUserCertificationsFragment() {
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
    public static SystemUserCertificationsFragment newInstance(String param1, String param2) {
        SystemUserCertificationsFragment fragment = new SystemUserCertificationsFragment();
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

        binding = FragmentSystemUserCertificationsBinding.inflate(inflater, container, false);
        getVM();
        adapter = new SystemUserCertificationAdapter(
                requireContext(),
                homeDataViewModel.getUserSelect().getValue(),
                homeDataViewModel.getCertificationList().getValue(),
                R.layout.system_certification_item, this);
        binding.systemCertificationsList.setHasFixedSize(true);
        binding.systemCertificationsList.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        binding.systemCertificationsList.setAdapter(adapter);

        homeDataViewModel.getCertificationList().observe(getViewLifecycleOwner(), list -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            adapter.setCertifications(list);
            binding.progressBar.setVisibility(View.GONE);
        });

        return binding.getRoot();
    }

    @Override
    public void onItemClickListener(Certification position) {
        Intent intent = new Intent(requireActivity(), UpdateCertificationActivity.class);
        intent.putExtra("certification", position);
        startActivity(intent);
    }
}
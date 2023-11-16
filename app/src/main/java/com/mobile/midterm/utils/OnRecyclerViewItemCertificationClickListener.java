package com.mobile.midterm.utils;

import com.mobile.midterm.model.Certification;
import com.mobile.midterm.model.User;

public interface OnRecyclerViewItemCertificationClickListener {
    public void onItemClickListenerImage(User user);

    public void onItemClickListener(Certification position);


}

package com.mobile.midterm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.midterm.model.Certification;
import com.mobile.midterm.model.User;
import com.mobile.midterm.model.UserCertification;

import java.util.List;

public class HomeDataViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<User>();
    private final MutableLiveData<List<User>> systemUsersList = new MutableLiveData<List<User>>();
    private final MutableLiveData<List<User>> studentsList = new MutableLiveData<List<User>>();
    private final MutableLiveData<List<Certification>> certificationList = new MutableLiveData<List<Certification>>();
    private final MutableLiveData<List<Certification>> certificationListCurrent = new MutableLiveData<List<Certification>>();
    private final MutableLiveData<List<UserCertification>> userCertificationListCurrent = new MutableLiveData<List<UserCertification>>();
    private final MutableLiveData<List<UserCertification>> userCertificationList = new MutableLiveData<List<UserCertification>>();

    public LiveData<List<User>> getSystemUsersList() {
        return systemUsersList;
    }

    public void setSystemUsersList(List<User> systemUsersList) {
        this.systemUsersList.postValue(systemUsersList);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user.setValue(user);
    }


    public LiveData<List<User>> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(List<User> studentsList) {
        this.studentsList.postValue(studentsList);
    }

    public LiveData<List<Certification>> getCertificationList() {
        return certificationList;
    }

    public void setCertificationList(List<Certification> certificationsList) {
        this.certificationList.postValue(certificationsList);
    }

    public LiveData<List<Certification>> getCertificationListCurrent() {
        return certificationListCurrent;
    }

    public void setCertificationListCurrent(List<Certification> certificationsList) {
        this.certificationListCurrent.postValue(certificationsList);
    }

    public LiveData<List<UserCertification>> getUserCertificationListCurrent() {
        return userCertificationListCurrent;
    }

    public void setUserCertificationListCurrent(List<UserCertification> userCertificationsList) {
        this.userCertificationListCurrent.postValue(userCertificationsList);
    }

    public LiveData<List<UserCertification>> getUserCertificationList() {
        return userCertificationList;
    }

    public void setUserCertificationList(List<UserCertification> userCertificationsList) {
        this.userCertificationList.postValue(userCertificationsList);
    }
}

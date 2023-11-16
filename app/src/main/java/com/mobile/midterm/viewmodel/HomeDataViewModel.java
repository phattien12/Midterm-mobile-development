package com.mobile.midterm.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mobile.midterm.model.Certification;
import com.mobile.midterm.model.User;

import java.util.List;

public class HomeDataViewModel extends ViewModel {
    private final MutableLiveData<User> user = new MutableLiveData<User>();
    private final MutableLiveData<List<User>> systemUsersList = new MutableLiveData<List<User>>();
    private final MutableLiveData<List<User>> studentsList = new MutableLiveData<List<User>>();
    private final MutableLiveData<List<Certification>> certificationList = new MutableLiveData<List<Certification>>();

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

    public LiveData<User> getUserSelect() {
        return user;
    }

    public void setUserSelect(User user) {
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
}

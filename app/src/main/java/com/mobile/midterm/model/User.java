package com.mobile.midterm.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class User implements Parcelable {
    private String id;
    private String email;
    private String username;
    private int age;
    private String phoneNum;
    private String fullName;
    private String createdDate;
    private String password;
    private String role;
    private boolean activated;
    private String image;

    public User() {

    }

    public User(String email, int age, String phoneNum, String fullName, String createdDate, String password, String role, boolean activated, String image, String username) {
        this.email = email;
        this.age = age;
        this.phoneNum = phoneNum;
        this.fullName = fullName;
        this.createdDate = createdDate;
        this.password = password;
        this.role = role;
        this.activated = activated;
        this.image = image;
        this.username = username;
    }

    protected User(Parcel in) {
        id = in.readString();
        email = in.readString();
        age = in.readInt();
        phoneNum = in.readString();
        fullName = in.readString();
        createdDate = in.readString();
        password = in.readString();
        role = in.readString();
        activated = in.readByte() != 0;
        image = in.readString();
        username = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                ", phoneNum='" + phoneNum + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' +
                ", activated=" + activated +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(email);
        parcel.writeInt(age);
        parcel.writeString(phoneNum);
        parcel.writeString(fullName);
        parcel.writeString(createdDate);
        parcel.writeString(password);
        parcel.writeString(role);
        parcel.writeByte((byte) (activated ? 1 : 0));
        parcel.writeString(image);
        parcel.writeString(username);
    }


}

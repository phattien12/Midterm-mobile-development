package com.mobile.midterm.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class UserCertification implements Parcelable {
    private String id;
    private String certificationId;
    private String UserId;
    private String detail;
    private String createdDate;
    private boolean activated;

    public UserCertification() {
    }

    public UserCertification(String certificationId, String userId, String detail, String createdDate, boolean activated) {
        this.certificationId = certificationId;
        UserId = userId;
        this.detail = detail;
        this.createdDate = createdDate;
        this.activated = activated;
    }


    protected UserCertification(Parcel in) {
        id = in.readString();
        certificationId = in.readString();
        UserId = in.readString();
        detail = in.readString();
        createdDate = in.readString();
        activated = in.readByte() != 0;
    }

    public static final Creator<UserCertification> CREATOR = new Creator<UserCertification>() {
        @Override
        public UserCertification createFromParcel(Parcel in) {
            return new UserCertification(in);
        }

        @Override
        public UserCertification[] newArray(int size) {
            return new UserCertification[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(String certificationId) {
        this.certificationId = certificationId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public String toString() {
        return "UserCertification{" +
                "id='" + id + '\'' +
                ", certificationId='" + certificationId + '\'' +
                ", UserId='" + UserId + '\'' +
                ", detail='" + detail + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", activated=" + activated +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(certificationId);
        dest.writeString(UserId);
        dest.writeString(detail);
        dest.writeString(createdDate);
        dest.writeByte((byte) (activated ? 1 : 0));
    }
}

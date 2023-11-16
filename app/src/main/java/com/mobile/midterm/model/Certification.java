package com.mobile.midterm.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Certification implements Parcelable {
    private String id;
    private String name;
    private String detail;
    private String createdDate;
    private boolean activated;

    public Certification() {
    }

    public Certification(String name, String detail, String createdDate, boolean activated) {
        this.name = name;
        this.detail = detail;
        this.createdDate = createdDate;
        this.activated = activated;
    }

    protected Certification(Parcel in) {
        id = in.readString();
        name = in.readString();
        detail = in.readString();
        createdDate = in.readString();
        activated = in.readByte() != 0;
    }

    public static final Creator<Certification> CREATOR = new Creator<Certification>() {
        @Override
        public Certification createFromParcel(Parcel in) {
            return new Certification(in);
        }

        @Override
        public Certification[] newArray(int size) {
            return new Certification[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        return "Certification{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
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
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(detail);
        parcel.writeString(createdDate);
        parcel.writeByte((byte) (activated ? 1 : 0));
    }
}

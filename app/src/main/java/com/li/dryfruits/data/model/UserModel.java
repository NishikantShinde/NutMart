package com.li.dryfruits.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserModel implements Parcelable {

    private String userId;
    private String fname;
    private String lname;
    private String mobile;
    private String email;
    private String password;
    private String address;

    public UserModel() {
    }

    public UserModel(String userId,String fname, String lname, String mobile, String email, String password, String address) {
        this.userId=userId;
        this.fname = fname;
        this.lname = lname;
        this.mobile = mobile;
        this.email = email;
        this.password = password;
        this.address = address;
    }

    protected UserModel(Parcel in) {
        userId = in.readString();
        fname = in.readString();
        lname = in.readString();
        mobile = in.readString();
        email = in.readString();
        password = in.readString();
        address = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(fname);
        parcel.writeString(lname);
        parcel.writeString(mobile);
        parcel.writeString(email);
        parcel.writeString(password);
        parcel.writeString(address);
    }
}

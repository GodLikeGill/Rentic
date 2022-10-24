package com.group9.project.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.UUID;

public class Listing implements Parcelable {

    private String id;
    private String user;
    private String userName;
    private String phone;
    private String title;
    private String desc;
    private String address;
    private String price;
    private String image;

    public Listing() {
    }

    public Listing(String id, String user, String userName, String phone, String title, String desc, String address, String price, String image) {
        this.id = id;
        this.user = user;
        this.userName = userName;
        this.phone = phone;
        this.title = title;
        this.desc = desc;
        this.address = address;
        this.price = price;
        this.image = image;
    }

    protected Listing(Parcel in) {
        id = in.readString();
        user = in.readString();
        userName = in.readString();
        phone = in.readString();
        title = in.readString();
        desc = in.readString();
        address = in.readString();
        price = in.readString();
        image = in.readString();
    }

    public static final Creator<Listing> CREATOR = new Creator<Listing>() {
        @Override
        public Listing createFromParcel(Parcel in) {
            return new Listing(in);
        }

        @Override
        public Listing[] newArray(int size) {
            return new Listing[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(user);
        dest.writeString(userName);
        dest.writeString(phone);
        dest.writeString(title);
        dest.writeString(desc);
        dest.writeString(address);
        dest.writeString(price);
        dest.writeString(image);
    }
}

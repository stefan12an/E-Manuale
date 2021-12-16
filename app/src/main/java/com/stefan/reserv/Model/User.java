package com.stefan.reserv.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private String id;
    private String username;
    private String email;
    private String password;
    private String role;
    private byte[] profile_pic;

    public User(String id, String username, String email, String password, String role, byte[] profile_pic) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.profile_pic = profile_pic;
    }

    public User(String id, String username, String email, String password, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public byte[] getProfile_pic() {
        return profile_pic;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(role);
        dest.writeByteArray(profile_pic);
    }
    public static final Parcelable.Creator<User> CREATOR
            = new Parcelable.Creator<User>() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    private User(Parcel in) {
        id = in.readString();
        username = in.readString();
        email = in.readString();
        password = in.readString();
        role = in.readString();
        profile_pic = in.createByteArray();
    }
}

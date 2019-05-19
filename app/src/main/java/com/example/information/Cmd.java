package com.example.information;

import android.os.Parcel;
import android.os.Parcelable;

public class Cmd implements Parcelable {

    private String description;
    private String name;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected Cmd(Parcel in) {
    }

    public static final Creator<Cmd> CREATOR = new Creator<Cmd>() {
        @Override
        public Cmd createFromParcel(Parcel in) {
            return new Cmd(in);
        }

        @Override
        public Cmd[] newArray(int size) {
            return new Cmd[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}

package com.example.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fares on 17.10.15.
 */
public class Trailer implements Parcelable {

    @SerializedName("id")   private String mId;
    @SerializedName("iso_639_1") private String mLanguage;
    @SerializedName("key")  private String mKey;
    @SerializedName("name") private String mName;
    @SerializedName("site") private String mSite;
    @SerializedName("size") private int mSize;
    @SerializedName("type") private String mType;

    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public Trailer(Parcel source) {
        this.mId   = source.readString();
        this.mLanguage  = source.readString();
        this.mKey  = source.readString();
        this.mName = source.readString();
        this.mSite = source.readString();
        this.mSize = source.readInt();
        this.mType = source.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mLanguage);
        parcel.writeString(mKey);
        parcel.writeString(mName);
        parcel.writeString(mSite);
        parcel.writeInt(mSize);
        parcel.writeString(mType);
    }

    @Override
    public String toString() {
        return "Trailer{" +
                "mId='" + mId + '\'' +
                ", mLanguage='" + mLanguage + '\'' +
                ", mKey='" + mKey + '\'' +
                ", mName='" + mName + '\'' +
                ", mSite='" + mSite + '\'' +
                ", mSize=" + mSize +
                ", mType='" + mType + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public String getID() {
        return mId;
    }
    public void setID(String mId) {
        this.mId = mId;
    }

    public String getLanguage() {
        return mLanguage;
    }
    public void setLanguage(String mLanguage) {
        this.mLanguage = mLanguage;
    }

    public String getKey() {
        return mKey;
    }
    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public String getName() {
        return mName;
    }
    public void setName(String mName) {
        this.mName = mName;
    }

    public String getSite() {
        return mSite;
    }
    public void setSite(String mSite) {
        this.mSite = mSite;
    }

    public int getSize() {
        return mSize;
    }
    public void setSize(int mSize) {
        this.mSize = mSize;
    }

    public String getType() {
        return mType;
    }
    public void setType(String mType) {
        this.mType = mType;
    }
}

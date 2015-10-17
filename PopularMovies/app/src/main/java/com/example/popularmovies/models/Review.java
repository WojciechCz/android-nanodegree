package com.example.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by fares on 17.10.15.
 */
public class Review implements Parcelable {

    @SerializedName("id")       private String mId;
    @SerializedName("author")   private String mAuthor;
    @SerializedName("content")  private String mContent;
    @SerializedName("url")      private String mUrl;

    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    public Review(Parcel source) {
        this.mId   = source.readString();
        this.mAuthor  = source.readString();
        this.mContent  = source.readString();
        this.mUrl = source.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
        parcel.writeString(mUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Review{" +
                "mId='" + mId + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mContent='" + mContent + '\'' +
                ", mUrl='" + mUrl + '\'' +
                '}';
    }

    
    public String getId() {
        return mId;
    }
    public void setId(String mId) {
        this.mId = mId;
    }

    public String getAuthor() {
        return mAuthor;
    }
    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getContent() {
        return mContent;
    }
    public void setContent(String mContent) {
        this.mContent = mContent;
    }

    public String getUrl() {
        return mUrl;
    }
    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }
}

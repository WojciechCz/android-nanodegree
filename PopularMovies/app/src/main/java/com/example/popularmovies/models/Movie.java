package com.example.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

/**
 * Created by fares on 7/27/15.
 */
public class Movie  implements Parcelable{
    @SerializedName("adult")             private boolean mAdult;
    @SerializedName("backdrop_path")     private String mBackdropPath;
    @SerializedName("genre_ids")         private int[] mGenreIds;
    @SerializedName("id")                private int mId;
    @SerializedName("original_language") private String mOriginLanguage;
    @SerializedName("original_title")    private String mOriginTitle;
    @SerializedName("overview")          private String mOverview;
    @SerializedName("release_date")      private String mReleaseDate;
    @SerializedName("poster_path")       private String mPosterPath;
    @SerializedName("popularity")        private float mPopularity;
    @SerializedName("title")             private String mTitle;
    @SerializedName("video")             private boolean mVideo;
    @SerializedName("vote_average")      private int mVoteAverage;
    @SerializedName("vote_count")        private int mVoteCount;


    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public Movie(Parcel source) {
        this.mAdult         = source.readByte() != 0;
        this.mBackdropPath  = source.readString();
        this.mGenreIds      = source.createIntArray();
        this.mId            = source.readInt();
        this.mOriginLanguage= source.readString();
        this.mOriginTitle   = source.readString();
        this.mOverview      = source.readString();
        this.mReleaseDate   = source.readString();
        this.mPosterPath    = source.readString();
        this.mPopularity    = source.readFloat();
        this.mTitle         = source.readString();
        this.mVideo         = source.readByte() != 0;
        this.mVoteAverage   = source.readInt();
        this.mVoteCount     = source.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (mAdult ? 1 : 0));
        parcel.writeString(mBackdropPath);
        parcel.writeIntArray(mGenreIds);
        parcel.writeInt(mId);
        parcel.writeString(mOriginLanguage);
        parcel.writeString(mOriginTitle);
        parcel.writeString(mOverview);
        parcel.writeString(mReleaseDate);
        parcel.writeString(mPosterPath);
        parcel.writeFloat(mPopularity);
        parcel.writeString(mTitle);
        parcel.writeByte((byte) (mVideo ? 1 : 0));
        parcel.writeInt(mVoteAverage);
        parcel.writeInt(mVoteCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mAdult=" + mAdult +
                ", mBackdropPath='" + mBackdropPath + '\'' +
                ", mGenreIds=" + Arrays.toString(mGenreIds) +
                ", mId=" + mId +
                ", mOriginLanguage='" + mOriginLanguage + '\'' +
                ", mOriginTitle='" + mOriginTitle + '\'' +
                ", mOverview='" + mOverview + '\'' +
                ", mReleaseDate='" + mReleaseDate + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mPopularity=" + mPopularity +
                ", mTitle='" + mTitle + '\'' +
                ", mVideo=" + mVideo +
                ", mVoteAverage=" + mVoteAverage +
                ", mVoteCount=" + mVoteCount +
                '}';
    }

    public boolean ismAdult() {
        return mAdult;
    }

    public void setmAdult(boolean mAdult) {
        this.mAdult = mAdult;
    }

    public String getmBackdropPath() {
        return mBackdropPath;
    }

    public void setmBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public int[] getmGenreIds() {
        return mGenreIds;
    }

    public void setmGenreIds(int[] mGenreIds) {
        this.mGenreIds = mGenreIds;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmOriginLanguage() {
        return mOriginLanguage;
    }

    public void setmOriginLanguage(String mOriginLanguage) {
        this.mOriginLanguage = mOriginLanguage;
    }

    public String getmOriginTitle() {
        return mOriginTitle;
    }

    public void setmOriginTitle(String mOriginTitle) {
        this.mOriginTitle = mOriginTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public float getmPopularity() {
        return mPopularity;
    }

    public void setmPopularity(float mPopularity) {
        this.mPopularity = mPopularity;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean ismVideo() {
        return mVideo;
    }

    public void setmVideo(boolean mVideo) {
        this.mVideo = mVideo;
    }

    public int getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(int mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public int getmVoteCount() {
        return mVoteCount;
    }

    public void setmVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }


}

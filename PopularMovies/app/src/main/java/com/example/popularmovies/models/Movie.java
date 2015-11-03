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

    public static class Builder {
        private boolean mAdult;
        private String mBackdropPath;
        private int[] mGenreIds;
        private int mId;
        private String mOriginLanguage;
        private String mOriginTitle;
        private String mOverview;
        private String mReleaseDate;
        private String mPosterPath;
        private float mPopularity;
        private String mTitle;
        private boolean mVideo;
        private int mVoteAverage;
        private int mVoteCount;

        public Builder (){
        }

        public Builder title (String title){
            this.mTitle = title;
            return this;
        }
        public Builder posterPath (String posterPath){
            this.mPosterPath = posterPath;
            return this;
        }
        public Builder id (int id){
            this.mId = id;
            return this;
        }

        public Movie build(){
            return new Movie(this);
        }
    }

    public Movie(Builder builder){
        this.mAdult = builder.mAdult;
        this.mBackdropPath = builder.mBackdropPath;
        this.mGenreIds = builder.mGenreIds;
        this.mId = builder.mId;
        this.mOriginLanguage = builder.mOriginLanguage;
        this.mOriginTitle = builder.mOriginTitle;
        this.mOverview = builder.mOverview;
        this.mReleaseDate = builder.mReleaseDate;
        this.mPosterPath = builder.mPosterPath;
        this.mPopularity = builder.mPopularity;
        this.mTitle = builder.mTitle;
        this.mVideo = builder.mVideo;
        this.mVoteAverage = builder.mVoteAverage;
        this.mVoteCount = builder.mVoteCount;
    }

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



    public boolean isAdult() {
        return mAdult;
    }
    public void setAdult(boolean mAdult) {
        this.mAdult = mAdult;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }
    public void setBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public int[] getGenreIds() {
        return mGenreIds;
    }
    public void setGenreIds(int[] mGenreIds) {
        this.mGenreIds = mGenreIds;
    }

    public int getId() {
        return mId;
    }
    public void setId(int mId) {
        this.mId = mId;
    }

    public String getOriginLanguage() {
        return mOriginLanguage;
    }
    public void setOriginLanguage(String mOriginLanguage) {
        this.mOriginLanguage = mOriginLanguage;
    }

    public String getOriginTitle() {
        return mOriginTitle;
    }
    public void setOriginTitle(String mOriginTitle) {
        this.mOriginTitle = mOriginTitle;
    }

    public String getOverview() {
        return mOverview;
    }
    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }
    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }
    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public float getPopularity() {
        return mPopularity;
    }
    public void setPopularity(float mPopularity) {
        this.mPopularity = mPopularity;
    }

    public String getTitle() {
        return mTitle;
    }
    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean isVideo() {
        return mVideo;
    }
    public void setVideo(boolean mVideo) {
        this.mVideo = mVideo;
    }

    public int getVoteAverage() {
        return mVoteAverage;
    }
    public void setVoteAverage(int mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public int getVoteCount() {
        return mVoteCount;
    }
    public void setVoteCount(int mVoteCount) {
        this.mVoteCount = mVoteCount;
    }


}

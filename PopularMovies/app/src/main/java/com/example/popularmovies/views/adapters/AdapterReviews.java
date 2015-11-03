package com.example.popularmovies.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.Review;

import java.util.List;

/**
 * Created by fares on 18.10.15.
 */
public class AdapterReviews extends RecyclerView.Adapter<AdapterReviews.Holder> {

    private List<Review> mListReviews;

    public AdapterReviews(List<Review> mListReviews) {
        this.mListReviews = mListReviews;
    }

    @Override
    public AdapterReviews.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterReviews.Holder holder, int position) {
        holder.bindData(mListReviews.get(position));
    }

    @Override
    public int getItemCount() {
        return mListReviews.size();
    }

    public void updateList(List<Review> reviews){
        if (mListReviews != null){
            mListReviews.clear();
            mListReviews.addAll(reviews);
            notifyDataSetChanged();
        }
    }

    protected class Holder extends RecyclerView.ViewHolder {

        private final TextView mAuthor;
        private final TextView mContent;

        public Holder(View itemView) {
            super(itemView);
            mAuthor  = (TextView) itemView.findViewById(R.id.listItemReviewAuthor);
            mContent = (TextView) itemView.findViewById(R.id.listItemReviewContent);
        }

        public void bindData(Review review){
            if (mAuthor != null && !review.getAuthor().isEmpty())
                mAuthor.setText(review.getAuthor());
            if (mContent != null && !review.getContent().isEmpty())
                mContent.setText(review.getContent());
        }
    }
}

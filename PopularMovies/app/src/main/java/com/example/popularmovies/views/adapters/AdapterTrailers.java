package com.example.popularmovies.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.popularmovies.R;
import com.example.popularmovies.models.Trailer;

import java.util.List;

/**
 * Created by fares on 18.10.15.
 */
public class AdapterTrailers extends RecyclerView.Adapter<AdapterTrailers.Holder> {

    private List<Trailer> mListTrailers;

    public AdapterTrailers(List<Trailer> mListTrailers) {
        this.mListTrailers = mListTrailers;
    }

    @Override
    public AdapterTrailers.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false);
        return new Holder(itemView);
    }

    @Override
    public void onBindViewHolder(AdapterTrailers.Holder holder, int position) {
        holder.bindData(mListTrailers.get(position));
    }

    @Override
    public int getItemCount() {
        return mListTrailers.size();
    }

    public void updateList(List<Trailer> trailers){
        if (mListTrailers != null){
            mListTrailers.clear();
            mListTrailers.addAll(trailers);
        }
    }

    protected class Holder extends RecyclerView.ViewHolder {

        private final TextView mName;

        public Holder(View itemView) {
            super(itemView);
            mName  = (TextView) itemView.findViewById(R.id.listItemTrailerName);
        }

        public void bindData(Trailer trailer){
            if (mName != null && !trailer.getName().isEmpty())
                mName.setText(trailer.getName());
        }
    }
}

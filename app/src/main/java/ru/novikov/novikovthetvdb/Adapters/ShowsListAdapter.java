package ru.novikov.novikovthetvdb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.ShowItemList;
import ru.novikov.novikovthetvdb.R;


/**
 * adapter for tv shows list
 */
public class ShowsListAdapter
        extends RecyclerView.Adapter<TvShowViewHolder> {

    private final List<ShowItemList> mValues;
    private ShowListClickListener showListClickListener;

    public ShowsListAdapter(List<ShowItemList> items, ShowListClickListener listener) {
        mValues = items;
        showListClickListener = listener;
    }

    @Override
    public TvShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_list_content, parent, false);
        return new TvShowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final TvShowViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).seriesName);
        holder.mContentView.setText(mValues.get(position).overview);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListClickListener.OnShowClick(holder.mItem.id, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public interface ShowListClickListener{
        void OnShowClick(int id, View v);
    }
}

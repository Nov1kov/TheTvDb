package ru.novikov.novikovthetvdb.Adapters;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;
import ru.novikov.novikovthetvdb.R;


/**
 * adapter for tv shows list
 */
public class SeriesListAdapter extends RecyclerView.Adapter<SeriesViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    private List<SeriesData> mValues;
    private ShowListClickListener showListClickListener;

    public SeriesListAdapter(List<SeriesData> items, ShowListClickListener listener) {
        mValues = items;
        showListClickListener = listener;


    }

    public void updateList(List<SeriesData> items){
        mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public SeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.show_list_content, parent, false);
        return new SeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SeriesViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.SeriesNameTextView.setText(String.valueOf(mValues.get(position).lastUpdated));
        //holder.SeriesOverviewTextView.setText(mValues.get(position).overview);

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
        void OnShowClick(long id, View v);
    }
}

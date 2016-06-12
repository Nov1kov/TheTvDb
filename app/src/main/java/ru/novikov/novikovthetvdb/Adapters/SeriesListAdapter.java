package ru.novikov.novikovthetvdb.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.R;


/**
 * adapter for tv shows list
 */
public class SeriesListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private boolean isLoading;
    private static final int VISIBLE_THRESHOLD = 5;

    private List<Series> mValues;
    private ShowListClickListener showListClickListener;

    public SeriesListAdapter(List<Series> items, ShowListClickListener listener) {
        mValues = items;
        showListClickListener = listener;
    }

    public void updateList(List<Series> items){
        mValues = items;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return mValues.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.show_list_content, parent, false);
            return new SeriesViewHolder(view);

        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.progresbar_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof SeriesViewHolder) {
            Series item = mValues.get(position);
            final SeriesViewHolder seriesViewHolder = (SeriesViewHolder) holder;
            seriesViewHolder.mItem = item;
            seriesViewHolder.SeriesNameTextView.setText(String.valueOf(item.lastUpdated));

            seriesViewHolder.SeriesNameTextView.setText(item.seriesName);
            seriesViewHolder.SeriesOverviewTextView.setText(item.overview);

            seriesViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showListClickListener.OnShowClick(seriesViewHolder.mItem.id, v);
                }
            });
        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }

    public boolean needLoadedMore(int totalItemCount, int lastVisibleItem) {
        return !isLoading && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD);
    }

    public void showProgressBar() {
        isLoading = true;
        mValues.add(null);
        notifyItemInserted(mValues.size() - 1);
    }

    public interface ShowListClickListener{
        void OnShowClick(long id, View v);
    }

    public void setLoaded() {
        isLoading = false;
    }
}

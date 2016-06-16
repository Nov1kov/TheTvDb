package ru.novikov.novikovthetvdb.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.novikov.novikovthetvdb.Model.Rest.ApiClient;
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
    private ItemListClickListener itemListClickListener;

    private Picasso picasso;
    private long selectedId;

    public SeriesListAdapter(List<Series> items, ItemListClickListener listener) {
        mValues = items;
        itemListClickListener = listener;
    }

    public void setPicasso(Picasso picasso){
        this.picasso = picasso;
        picasso.setLoggingEnabled(true);
    }

    public void updateList(List<Series> items){
        mValues.addAll(items);
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
                    .inflate(R.layout.series_list_item, parent, false);
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
            seriesViewHolder.SeriesNameTextView.setText(item.seriesName);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.ROOT);
            seriesViewHolder.firstAiredTextView.setText(sdf.format(new Date(item.lastUpdated * 1000)));
            seriesViewHolder.mView.setSelected(selectedId == seriesViewHolder.mItem.id);

            seriesViewHolder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedId = seriesViewHolder.mItem.id;
                    itemListClickListener.OnSeriesClick(seriesViewHolder.mItem.id, v);
                }
            });
            if (picasso != null)
                picasso.
                    load(ApiClient.TVDB_IMAGES_URL + item.banner).
                    fit().
                    centerCrop().
                    into(seriesViewHolder.SeriesImageView);

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

    public void setLoaded() {
        if (getItemViewType(mValues.size() - 1) == VIEW_TYPE_LOADING){
            mValues.remove(mValues.size() - 1);
            notifyItemRemoved(mValues.size());
        }
        isLoading = false;
    }
}

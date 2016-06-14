package ru.novikov.novikovthetvdb.Adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.DataBase.GreenDao.FavoriteItem;
import ru.novikov.novikovthetvdb.R;


/**
 * adapter for favorites series
 */
public class FavoritesListAdapter extends RecyclerView.Adapter<FavoriteViewHolder> {

    private List<FavoriteItem> mValues;
    private ItemListClickListener itemListClickListener;

    public FavoritesListAdapter(List<FavoriteItem> items, @Nullable ItemListClickListener listener) {
        mValues = items;
        itemListClickListener = listener;
    }

    public void updateList(List<FavoriteItem> items){
        if (items != null){
            mValues.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_list_item, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FavoriteViewHolder holder, int position) {
        FavoriteItem item = mValues.get(position);
        holder.mItem = item;
        holder.SeriesNameTextView.setText(item.getSeriesName());
        holder.SeriesGenreTextView.setText(item.getGenre());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListClickListener != null)
                    itemListClickListener.OnSeriesClick(holder.mItem.getSeriesId(), v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }
}

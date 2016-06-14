package ru.novikov.novikovthetvdb.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.novikov.novikovthetvdb.Model.DataBase.GreenDao.FavoriteItem;
import ru.novikov.novikovthetvdb.R;

/**
 *
 */
public class FavoriteViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView SeriesNameTextView;
    public final TextView SeriesGenreTextView;
    public FavoriteItem mItem;

    public FavoriteViewHolder(View view) {
        super(view);
        mView = view;
        SeriesNameTextView = (TextView) view.findViewById(R.id.seriesName);
        SeriesGenreTextView = (TextView) view.findViewById(R.id.seriesGenre);
    }
}
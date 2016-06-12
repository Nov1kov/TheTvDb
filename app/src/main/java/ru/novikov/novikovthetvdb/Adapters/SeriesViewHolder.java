package ru.novikov.novikovthetvdb.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;
import ru.novikov.novikovthetvdb.R;

/**
 *
 */
public class SeriesViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final ImageView SeriesImageView;
    public final TextView SeriesNameTextView;
    public final TextView SeriesOverviewTextView;
    public Series mItem;

    public SeriesViewHolder(View view) {
        super(view);
        mView = view;
        SeriesImageView = (ImageView) view.findViewById(R.id.picture);
        SeriesNameTextView = (TextView) view.findViewById(R.id.seriesName);
        SeriesOverviewTextView = (TextView) view.findViewById(R.id.overview);
    }
}
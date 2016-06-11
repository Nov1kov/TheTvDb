package ru.novikov.novikovthetvdb.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.novikov.novikovthetvdb.Model.ShowItemList;
import ru.novikov.novikovthetvdb.R;
import ru.novikov.novikovthetvdb.dummy.DummyContent;

/**
 * Created by Ivan on 10.06.2016.
 */
public class TvShowViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView mIdView;
    public final TextView mContentView;
    public ShowItemList mItem;

    public TvShowViewHolder(View view) {
        super(view);
        mView = view;
        mIdView = (TextView) view.findViewById(R.id.id);
        mContentView = (TextView) view.findViewById(R.id.content);
    }
}
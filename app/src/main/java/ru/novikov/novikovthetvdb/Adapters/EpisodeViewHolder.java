package ru.novikov.novikovthetvdb.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.R;

/**
 *
 */
public class EpisodeViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final TextView EpisodeNameTextView;
    public final TextView EpisodeNumberTextView;
    public Episode item;

    public EpisodeViewHolder(View view) {
        super(view);
        mView = view;
        EpisodeNameTextView = (TextView) view.findViewById(R.id.episodeName);
        EpisodeNumberTextView = (TextView) view.findViewById(R.id.episodeNumber);
    }
}
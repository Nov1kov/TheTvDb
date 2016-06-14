package ru.novikov.novikovthetvdb.Adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.R;


/**
 * adapter for episodes about purpose series
 */
public class EpisodesListAdapter extends RecyclerView.Adapter<EpisodeViewHolder> {

    private List<Episode> mValues;
    private ItemListClickListener itemListClickListener;

    public EpisodesListAdapter(List<Episode> items, @Nullable ItemListClickListener listener) {
        mValues = items;
        itemListClickListener = listener;
    }

    public void updateList(List<Episode> items){
        if (items != null){
            mValues.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.episode_list_item, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final EpisodeViewHolder holder, int position) {
        Episode item = mValues.get(position);
        holder.item = item;
        holder.EpisodeNameTextView.setText(item.episodeName);
        holder.EpisodeNumberTextView.setText(
                String.format(holder.mView.getContext().getString(R.string.number_of_episode),
                        item.airedSeason, item.airedEpisodeNumber));
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListClickListener != null)
                    itemListClickListener.OnSeriesClick(holder.item.id, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }
}

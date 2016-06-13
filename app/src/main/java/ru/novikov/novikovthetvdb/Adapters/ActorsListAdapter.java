package ru.novikov.novikovthetvdb.Adapters;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.R;


/**
 * adapter for tv shows list
 */
public class ActorsListAdapter extends RecyclerView.Adapter<ActorViewHolder> {

    private List<Actor> mValues;
    private ItemListClickListener itemListClickListener;

    public ActorsListAdapter(List<Actor> items, @Nullable ItemListClickListener listener) {
        mValues = items;
        itemListClickListener = listener;
    }

    public void updateList(List<Actor> items){
        if (items != null){
            mValues.addAll(items);
            notifyDataSetChanged();
        }
    }

    @Override
    public ActorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.actor_list_item, parent, false);
        return new ActorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ActorViewHolder holder, int position) {
        Actor item = mValues.get(position);
        holder.item = item;
        holder.ActorNameTextView.setText(item.name);
        holder.ActorRoleTextView.setText(item.role);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemListClickListener != null)
                    itemListClickListener.OnShowClick(holder.item.id, v);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues == null ? 0 : mValues.size();
    }
}

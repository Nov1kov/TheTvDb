package ru.novikov.novikovthetvdb.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.R;

/**
 *
 */
public class ActorViewHolder extends RecyclerView.ViewHolder {
    public final View mView;
    public final ImageView ActorImageView;
    public final TextView ActorNameTextView;
    public final TextView ActorRoleTextView;
    public Actor item;

    public ActorViewHolder(View view) {
        super(view);
        mView = view;
        ActorImageView = (ImageView) view.findViewById(R.id.actorImage);
        ActorNameTextView = (TextView) view.findViewById(R.id.actorName);
        ActorRoleTextView = (TextView) view.findViewById(R.id.actorRole);
    }
}
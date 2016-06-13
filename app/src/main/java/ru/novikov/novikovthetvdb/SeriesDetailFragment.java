package ru.novikov.novikovthetvdb;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.novikovthetvdb.Adapters.ActorsListAdapter;
import ru.novikov.novikovthetvdb.Adapters.EpisodesListAdapter;
import ru.novikov.novikovthetvdb.Model.DataProviderSubscriber;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;

/**
 * A fragment representing a single Show detail screen.
 * This fragment is either contained in a {@link SeriesListActivity}
 * in two-pane mode (on tablets) or a {@link SeriesDetailActivity}
 * on handsets.
 */
public class SeriesDetailFragment extends Fragment implements DataProviderSubscriber {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    private Series currentSeries;
    private ActorsListAdapter actorsListAdapter;
    private EpisodesListAdapter episodesListAdapter;

    private TextView overviewTextView;
    private RecyclerView actorsRecyclerView;
    private RecyclerView episodesRecyclerView;

    public SeriesDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            getApp().getDataProvider().setSubscriber(this);
            getApp().getDataProvider().getSeriesDetail(getArguments().getLong(ARG_ITEM_ID, 0));
        }
    }

    private SeriesApp getApp(){
        return (SeriesApp) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.series_detail_fragment, container, false);

        overviewTextView = (TextView) rootView.findViewById(R.id.overview);

        actorsRecyclerView = (RecyclerView) rootView.findViewById(R.id.actorsRecyclerView);
        actorsListAdapter = new ActorsListAdapter(new ArrayList<Actor>(), null);
        actorsRecyclerView.setAdapter(actorsListAdapter);
        actorsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        episodesRecyclerView = (RecyclerView) rootView.findViewById(R.id.episodesRecyclerView);
        episodesListAdapter = new EpisodesListAdapter(new ArrayList<Episode>(), null);
        episodesRecyclerView.setAdapter(episodesListAdapter);
        episodesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getApp().getDataProvider().setSubscriber(this);

        return rootView;
    }

    @Override
    public void onDestroyView() {
        getApp().getDataProvider().deleteSubscriber(this);
        super.onDestroyView();
    }

    @Override
    public void receivedSeriesList(List<Series> seriesDataList) {

    }

    @Override
    public void receivedSeries(Series series) {
        currentSeries = series;
        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(currentSeries.seriesName);
        }
        if (overviewTextView != null){
            if (currentSeries.overview == null || currentSeries.overview.equals(""))
                overviewTextView.setText(R.string.empty_overview);
            else{
                overviewTextView.setText(currentSeries.overview);
            }
        }


        getApp().getDataProvider().getActors(currentSeries.id);
        getApp().getDataProvider().getEpisodes(currentSeries.id, "1");
    }

    @Override
    public void receiveActors(List<Actor> actorList) {
        actorsListAdapter.updateList(actorList);
    }

    @Override
    public void receiveEpisodes(List<Episode> episodesList) {
        episodesListAdapter.updateList(episodesList);
    }

    @Override
    public void receivedFail(String msg) {

    }
}

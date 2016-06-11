package ru.novikov.novikovthetvdb;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesResponse;

/**
 * A fragment representing a single Show detail screen.
 * This fragment is either contained in a {@link ShowListActivity}
 * in two-pane mode (on tablets) or a {@link ShowDetailActivity}
 * on handsets.
 */
public class ShowDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private SeriesResponse.Series currentSeries;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ShowDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            currentSeries = app().getDataProvider().getSeries(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(currentSeries.seriesName);
            }
        }
    }

    private ShowsApp app(){
        return (ShowsApp) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.show_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (currentSeries != null) {
            ((TextView) rootView.findViewById(R.id.show_detail)).setText(currentSeries.overview);
        }

        return rootView;
    }
}
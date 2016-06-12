package ru.novikov.novikovthetvdb;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;


import ru.novikov.novikovthetvdb.Adapters.SeriesListAdapter;
import ru.novikov.novikovthetvdb.Model.DataProviderSubscriber;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.SeriesData;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Shows. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ShowDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SeriesListActivity extends AppCompatActivity implements DataProviderSubscriber {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SeriesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



        View recyclerView = findViewById(R.id.show_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        getApp().getDataProvider().setSubscriber(this);
        getApp().getDataProvider().getSeriesList();

        if (findViewById(R.id.show_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new SeriesListAdapter(new ArrayList<SeriesData>(),
                new SeriesListAdapter.ShowListClickListener() {
                    @Override
                    public void OnShowClick(long id, View v) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putLong(ShowDetailFragment.ARG_ITEM_ID, id);
                            ShowDetailFragment fragment = new ShowDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.show_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, ShowDetailActivity.class);
                            intent.putExtra(ShowDetailFragment.ARG_ITEM_ID, id);

                            context.startActivity(intent);
                        }
                    }
                });

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
/*
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }*/
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void receivedSeriesList(List<SeriesData> seriesDataList) {
        adapter.updateList(seriesDataList);
    }

    @Override
    public void receivedSeries(Series series) {

    }

    @Override
    protected void onPause() {
        getApp().getDataProvider().deleteSubscriber();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getApp().getDataProvider().setSubscriber(this);
    }

    public ShowsApp getApp() {
        return (ShowsApp) getApplication();
    }
}

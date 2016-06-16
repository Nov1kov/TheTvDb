package ru.novikov.novikovthetvdb.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import com.squareup.picasso.Picasso;

import ru.novikov.novikovthetvdb.Adapters.FavoritesListAdapter;
import ru.novikov.novikovthetvdb.Adapters.ItemListClickListener;
import ru.novikov.novikovthetvdb.Adapters.SeriesListAdapter;
import ru.novikov.novikovthetvdb.Login.AuthOnGoogle;
import ru.novikov.novikovthetvdb.Model.DataBase.GreenDao.FavoriteItem;
import ru.novikov.novikovthetvdb.Model.DataProviderSubscriber;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;
import ru.novikov.novikovthetvdb.R;
import ru.novikov.novikovthetvdb.SeriesApp;
import ru.novikov.novikovthetvdb.View.Animation.CustomAnimation;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Series. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SeriesDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class SeriesListActivity extends AppCompatActivity
                                implements DataProviderSubscriber,
                                            CompoundButton.OnCheckedChangeListener,
                                            ItemListClickListener {

    private SwitchCompat listSwitchCompat;
    private ViewSwitcher viewSwitcher;
    private FloatingActionButton fab;
    private RecyclerView seriesRecyclerView;

    private boolean mTwoPane;
    private long currentSeriesId;
    private FavoritesListAdapter favoritesAdapter;
    private SeriesListAdapter seriesAdapter;

    private AuthOnGoogle authOnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        viewSwitcher = (ViewSwitcher) findViewById(R.id.listSwitcher);

        listSwitchCompat = (SwitchCompat) findViewById(R.id.listSwitchCompat);
        onCheckedChanged(null, listSwitchCompat.isChecked());
        listSwitchCompat.setOnCheckedChangeListener(this);

        authOnGoogle = new AuthOnGoogle(this);
        if (savedInstanceState == null)
            authOnGoogle.getGoogleToken();

        seriesRecyclerView = (RecyclerView) findViewById(R.id.series_list);
        setupSeriesRecyclerView(seriesRecyclerView);
        View favoritesRecyclerView = findViewById(R.id.favorites_list);
        setupFavoritesRecyclerView((RecyclerView) favoritesRecyclerView);

        getApp().getDataProvider().setSubscriber(this);
        getApp().getDataProvider().getFullSeriesList(0);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        if (findViewById(R.id.show_detail_container) != null) {
            // large-screen layouts (res/values-w900dp).
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SeriesDetailFragment fragment =
                            (SeriesDetailFragment) getSupportFragmentManager().findFragmentById(R.id.show_detail_container);

                    if (fragment != null){
                        view.startAnimation(CustomAnimation.createFabAnimation(SeriesListActivity.this, view));
                        fragment.addToFavoriteButtonClick();
                    }

                }
            });
            mTwoPane = true;
        }else{
            fab.setVisibility(View.INVISIBLE);
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.show_detail_container);
            if (fragment != null)
                fragment.onDestroy();
        }
    }

    @Override
    protected void onStart() {
        favoritesAdapter.updateList(getApp().getDataProvider().getFavoritesList());
        super.onStart();
    }

    private void setupFavoritesRecyclerView(@NonNull RecyclerView recyclerView) {
        favoritesAdapter = new FavoritesListAdapter(new ArrayList<FavoriteItem>(), this);
        recyclerView.setAdapter(favoritesAdapter);
    }

    private void setupSeriesRecyclerView(@NonNull RecyclerView recyclerView) {
        seriesAdapter = new SeriesListAdapter(new ArrayList<Series>(), this);
        seriesAdapter.setPicasso(new Picasso.Builder(this).build());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (seriesAdapter.needLoadedMore(totalItemCount, lastVisibleItem)){
                    getApp().getDataProvider().getFullSeriesList(seriesAdapter.getItemCount());
                    seriesAdapter.showProgressBar();
                }
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(seriesAdapter);
        seriesAdapter.showProgressBar();
    }

    /*
        for highlight selected row
     */
    private void selectedView(View view){
        View child;
        for (int i = 0; i < seriesRecyclerView.getChildCount(); i++) {
            child = seriesRecyclerView.getChildAt(i);
            child.setSelected(view == child);
        }
    }

    /*
    method calls from both recyclerViews favorites and series lists
     */
    @Override
    public void OnSeriesClick(long id, View v) {
        selectedView(v);
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putLong(SeriesDetailFragment.ARG_ITEM_ID, id);
            SeriesDetailFragment fragment = new SeriesDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                    .replace(R.id.show_detail_container, fragment)
                    .commit();
            fab.show();
        } else {
            Context context = v.getContext();
            Intent intent = new Intent(context, SeriesDetailActivity.class);
            intent.putExtra(SeriesDetailFragment.ARG_ITEM_ID, id);
            context.startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.login:
                authOnGoogle.getGoogleToken();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void receivedSeriesList(List<Series> seriesDataList) {
        seriesAdapter.setLoaded();
        seriesAdapter.updateList(seriesDataList);
    }

    @Override
    public void receivedSeries(Series series) {

    }

    @Override
    public void receiveActors(List<Actor> seriesDataList) {

    }

    @Override
    public void receiveEpisodes(List<Episode> episodesList) {

    }

    @Override
    public void receivedFail(String msg) {
        seriesAdapter.setLoaded();
        Toast.makeText(this, R.string.loading_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        getApp().getDataProvider().deleteSubscriber(this);
        super.onDestroy();
    }

    public SeriesApp getApp() {
        return (SeriesApp) getApplication();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            listSwitchCompat.setText(R.string.favorites_list);
        }else {
            listSwitchCompat.setText(R.string.series_list);
        }
        viewSwitcher.showNext();
    }
}

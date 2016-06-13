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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import ru.novikov.novikovthetvdb.Adapters.ItemListClickListener;
import ru.novikov.novikovthetvdb.Adapters.SeriesListAdapter;
import ru.novikov.novikovthetvdb.Login.AuthOnGoogle;
import ru.novikov.novikovthetvdb.Model.DataProviderSubscriber;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Actor;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Episode;
import ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Shows. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link SeriesDetailActivity} representing
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

    private AuthOnGoogle authOnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authOnGoogle.getGoogleToken();
            }
        });

        authOnGoogle = new AuthOnGoogle(this);

        View recyclerView = findViewById(R.id.show_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        getApp().getDataProvider().setSubscriber(this);
        getApp().getDataProvider().getFullSeriesList(0);

        if (findViewById(R.id.show_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        adapter = new SeriesListAdapter(new ArrayList<Series>(),
                new ItemListClickListener() {
                    @Override
                    public void OnShowClick(long id, View v) {
                        if (mTwoPane) {
                            Bundle arguments = new Bundle();
                            arguments.putLong(SeriesDetailFragment.ARG_ITEM_ID, id);
                            SeriesDetailFragment fragment = new SeriesDetailFragment();
                            fragment.setArguments(arguments);
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.show_detail_container, fragment)
                                    .commit();
                        } else {
                            Context context = v.getContext();
                            Intent intent = new Intent(context, SeriesDetailActivity.class);
                            intent.putExtra(SeriesDetailFragment.ARG_ITEM_ID, id);
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

                if (adapter.needLoadedMore(totalItemCount, lastVisibleItem)){
                    getApp().getDataProvider().getFullSeriesList(adapter.getItemCount());
                    adapter.showProgressBar();
                }
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.showProgressBar();
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
            case R.id.favorites:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*
        if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            // Receiving a result from the AccountPicker
            if (resultCode == RESULT_OK) {
                mEmail = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                // With the account name acquired, go get the auth token
                getUsername();
            } else if (resultCode == RESULT_CANCELED) {
                // The account picker dialog closed without selecting an account.
                // Notify users that they must pick an account to proceed.
                Toast.makeText(this, R.string.pick_account, Toast.LENGTH_SHORT).show();
            }
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void receivedSeriesList(List<Series> seriesDataList) {
        adapter.setLoaded();
        adapter.updateList(seriesDataList);
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
        adapter.setLoaded();
        Toast.makeText(this, R.string.loading_fail, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        getApp().getDataProvider().deleteSubscriber(this);
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        getApp().getDataProvider().setSubscriber(this);
    }

    public SeriesApp getApp() {
        return (SeriesApp) getApplication();
    }
}

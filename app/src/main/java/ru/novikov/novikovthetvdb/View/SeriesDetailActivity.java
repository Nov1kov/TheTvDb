package ru.novikov.novikovthetvdb.View;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.animation.Animation;

import ru.novikov.novikovthetvdb.R;
import ru.novikov.novikovthetvdb.View.Animation.CustomAnimation;

/**
 * An activity representing a single Show detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link SeriesListActivity}.
 */
public class SeriesDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);



        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        if (fab != null){

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SeriesDetailFragment fragment =
                            (SeriesDetailFragment) getSupportFragmentManager().findFragmentById(R.id.show_detail_container);
                    if (fragment != null){
                        Animation animation = CustomAnimation.createFabAnimation(SeriesDetailActivity.this, view);
                        view.setAnimation(animation);
                        animation.start();
                        //fab.hide();
                        fragment.addToFavoriteButtonClick();
                    }
                }
            });
        }

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putLong(SeriesDetailFragment.ARG_ITEM_ID,
                    getIntent().getLongExtra(SeriesDetailFragment.ARG_ITEM_ID, 0));
            SeriesDetailFragment fragment = new SeriesDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.show_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, SeriesListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

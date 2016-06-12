package ru.novikov.novikovthetvdb.Presenter;

import ru.novikov.novikovthetvdb.View.SeriesListView;

/**
 *
 */
public class SeriesListPresenter {

    private SeriesListView seriesListView;

    public SeriesListPresenter(SeriesListView seriesListView){

        this.seriesListView = seriesListView;
    }

    public void loadSeriesList(){
        seriesListView.getApp().getDataProvider().getSeriesList();
    }



}

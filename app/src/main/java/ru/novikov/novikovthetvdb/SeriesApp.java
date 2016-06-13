package ru.novikov.novikovthetvdb;

import android.app.Application;

import ru.novikov.novikovthetvdb.Model.DataProvider;
import ru.novikov.novikovthetvdb.Model.RestRepository;

public class SeriesApp extends Application {

    private DataProvider dataProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        RestRepository repository = new RestRepository();
        dataProvider = new DataProvider(repository);
    }

    public DataProvider getDataProvider(){
        return dataProvider;
    }

}

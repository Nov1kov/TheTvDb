package ru.novikov.novikovthetvdb;

import android.app.Application;

import ru.novikov.novikovthetvdb.Model.DataProvider;
import ru.novikov.novikovthetvdb.Model.IRepository;
import ru.novikov.novikovthetvdb.Model.RestRepository;

public class ShowsApp extends Application {

    private DataProvider dataProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        IRepository repository = new RestRepository();
        dataProvider = new DataProvider(repository);
    }

    public DataProvider getDataProvider(){
        return dataProvider;
    }

}

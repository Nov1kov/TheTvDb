package ru.novikov.novikovthetvdb;

import android.app.Application;

import ru.novikov.novikovthetvdb.Model.DataBase.DataBaseRepository;
import ru.novikov.novikovthetvdb.Model.DataProvider;
import ru.novikov.novikovthetvdb.Model.SharedPreferences.PreferencesRepository;
import ru.novikov.novikovthetvdb.Model.Rest.RestRepository;

public class SeriesApp extends Application {

    private DataProvider dataProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesRepository preferencesRepository = new PreferencesRepository(getBaseContext());
        DataBaseRepository dataBaseRepository = new DataBaseRepository(getBaseContext());
        RestRepository repository = new RestRepository();
        dataProvider = new DataProvider(repository, preferencesRepository, dataBaseRepository);
    }

    public DataProvider getDataProvider(){
        return dataProvider;
    }

}

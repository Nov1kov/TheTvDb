package ru.novikov.novikovthetvdb.Model.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.novikovthetvdb.Model.DataBase.GreenDao.DaoMaster;
import ru.novikov.novikovthetvdb.Model.DataBase.GreenDao.DaoSession;
import ru.novikov.novikovthetvdb.Model.DataBase.GreenDao.FavoriteItem;
import ru.novikov.novikovthetvdb.Model.DataBase.GreenDao.FavoriteItemDao;


public class DataBaseRepository {

    private Context context;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private FavoriteItem favoriteItem;

    public DataBaseRepository(Context context) {
        this.context = context;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "NovikovTvDb", null);
        db = helper.getWritableDatabase();
        daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public List<FavoriteItem> getAllFavoritesItems(String owner){
        if (owner != null){
            return daoSession.queryBuilder(FavoriteItem.class)
                    .where(FavoriteItemDao.Properties.Owner.eq(owner))
                    .list();
        }
            return new ArrayList<>();
    }

    public void addFavoriteItem(FavoriteItem favoriteItem){
        daoSession.insert(favoriteItem);
    }
}

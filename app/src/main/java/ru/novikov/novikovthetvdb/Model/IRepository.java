package ru.novikov.novikovthetvdb.Model;

import java.util.List;

/**
 * Created by Ivan on 10.06.2016.
 */
public interface IRepository {

    List<ShowItemList> getShowsLastWeek();

}

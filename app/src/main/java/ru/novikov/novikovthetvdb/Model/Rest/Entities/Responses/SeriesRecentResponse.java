package ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses;

import java.util.List;

public class SeriesRecentResponse {

    public class SeriesData {
        public long id;
        public long lastUpdated;
    }

    public List<SeriesData> data;
    public Errors errors;
}

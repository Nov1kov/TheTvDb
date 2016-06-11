package ru.novikov.novikovthetvdb.Model.Rest.Entities.Responses;


import java.util.List;


public class SeriesResponse {

    public Series data;

    public class Series {
        public long id;
        public String seriesName;
        public List<String> aliases;
        public String banner;
        public String seriesId;
        public String status;
        public String firstAired;
        public String network;
        public String networkId;
        public String runtime;
        public List<String> genre;
        public String overview;
        public long lastUpdated;
        public String airsDayOfWeek;
        public String airsTime;
        public String rating;
        public String imdbId;
        public String zap2itId;
        public String added;
        public double siteRating;
        public long siteRatingCount;
    }

    public Errors errors;
}



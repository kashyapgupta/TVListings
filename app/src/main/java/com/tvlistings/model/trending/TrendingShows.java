package com.tvlistings.model.trending;

import com.tvlistings.model.Show;

/**
 * Created by Rohit on 3/17/2016.
 */
public class TrendingShows {
    private String watchers;
    private Show show;

    public TrendingShows(String watchers, Show show) {
        this.watchers = watchers;
        this.show = show;
    }

    public String getWatchers() {
        return watchers;
    }

    public Show getShow() {
        return show;
    }
}

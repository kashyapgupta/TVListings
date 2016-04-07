package com.tvlistings.constants;

/**
 * Created by Rohit on 3/21/2016.
 */
public class UrlConstants {
    public static final String SEARCH_URL = "http://api.themoviedb.org/3/search/tv?api_key=c23fe079e72d437208ac3638b04f7c80&query=%s&page=%d";
    public static final String IMAGE_URLW_185 = "http://image.tmdb.org/t/p/w185%s";
    public static final String IMAGE_URLW_300 = "http://image.tmdb.org/t/p/w300%s";
    public static final String IMAGE_URLW_500 = "http://image.tmdb.org/t/p/w500%s";
    public static final String SHOW_URL = "http://api.themoviedb.org/3/tv/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String SEASON_URL = "http://api.themoviedb.org/3/tv/%d/season/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String PEOPLE_URL = "http://api.themoviedb.org/3/tv/%d/credits?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String PEOPLE_DETAILS = "http://api.themoviedb.org/3/person/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String PEOPLE_CASTING = "http://api.themoviedb.org/3/person/%d/combined_credits?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String POPULAR_SHOWS = "http://api.themoviedb.org/3/tv/popular?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String AIRING_TODAY_SHOWS = "http://api.themoviedb.org/3/tv/airing_today?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String TOP_RATED_SHOWS = "http://api.themoviedb.org/3/tv/top_rated?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String SIMILAR_SHOWS = "http://api.themoviedb.org/3/tv/%d/similar?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String EPISODE_DETAILS = "http://api.themoviedb.org/3/tv/%d/season/%d/episode/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static int notificationID = 0;
}

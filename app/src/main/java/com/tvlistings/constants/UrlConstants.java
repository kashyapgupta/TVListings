package com.tvlistings.constants;

/**
 * Created by Rohit on 3/21/2016.
 */
public class UrlConstants {
    public static final String SEARCH_URL = "http://api.themoviedb.org/3/search/multi?api_key=c23fe079e72d437208ac3638b04f7c80&query=%s&page=%d";
    public static final String IMAGE_URLW_185 = "http://image.tmdb.org/t/p/w185%s";
    public static final String IMAGE_URLW_300 = "http://image.tmdb.org/t/p/w300%s";
    public static final String IMAGE_URLW_500 = "http://image.tmdb.org/t/p/w500%s";
    public static final String SHOW_URL = "http://api.themoviedb.org/3/tv/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String MOVIE_URL = "http://api.themoviedb.org/3/movie/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String MOVIE_COLLECTION_URL = "http://api.themoviedb.org/3/collection/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String SEASON_URL = "http://api.themoviedb.org/3/tv/%d/season/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String SHOW_PEOPLE_URL = "http://api.themoviedb.org/3/tv/%d/credits?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String MOVIE_PEOPLE_URL = "http://api.themoviedb.org/3/movie/%d/credits?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String PEOPLE_DETAILS = "http://api.themoviedb.org/3/person/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String PEOPLE_CASTING = "http://api.themoviedb.org/3/person/%d/combined_credits?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String POPULAR_SHOWS = "http://api.themoviedb.org/3/tv/popular?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String AIRING_TODAY_SHOWS = "http://api.themoviedb.org/3/tv/airing_today?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String TOP_RATED_SHOWS = "http://api.themoviedb.org/3/tv/top_rated?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String SIMILAR_MOVIES = "http://api.themoviedb.org/3/movie/%d/similar?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String SIMILAR_SHOWS = "http://api.themoviedb.org/3/tv/%d/similar?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String EPISODE_DETAILS = "http://api.themoviedb.org/3/tv/%d/season/%d/episode/%d?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static int notificationID = 0;
    public static final String POPULAR_MOVIES = "http://api.themoviedb.org/3/movie/popular?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String TOP_RATED_MOVIES = "http://api.themoviedb.org/3/movie/top_rated?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String UPCOMING_MOVIES = "http://api.themoviedb.org/3/movie/upcoming?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String NOW_PLAYING_MOVIES = "http://api.themoviedb.org/3/movie/now_playing?api_key=c23fe079e72d437208ac3638b04f7c80";
    public static final String POPULAR_PEOPLE = "http://api.themoviedb.org/3/person/popular?api_key=c23fe079e72d437208ac3638b04f7c80&page=%d";
    public static final String DISCOVER_MOVIES = "http://api.themoviedb.org/3/discover/movie?api_key=c23fe079e72d437208ac3638b04f7c80&sort_by=%s&primary_release_year=%d&page=%d";
    public static final String NETWORK_SHOWS = "http://api.themoviedb.org/3/discover/tv?api_key=c23fe079e72d437208ac3638b04f7c80&with_networks=%d&page=%d";
    public static final String PRODUCTION_COMPANY_MOVIES = "http://api.themoviedb.org/3/company/%d/movies?api_key=c23fe079e72d437208ac3638b04f7c80&page=%d";
}

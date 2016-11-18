package com.mvreview.www.mvr.APIProvider;

import android.graphics.Bitmap;

/**
 * Created by Faizi on 21-11-2015.
 */
public class MovieIDProvider {


    private boolean adult;
    public static final String name_adult="adult";
    private String backdrop_path;
    public static final String name_backdrop_path="backdrop_path";
    private String budget;
    public static final String name_budget="budget";
    private String[][] genres;
    public static final String name_genres="genres";
    private String homepage;
    public static final String name_homepage="homepage";
    private String id;
    public static final String name_id="id";
    private String imdb_id;
    public static final String name_imdb_id="imdb_id";
    private String original_language;
    public static final String name_original_language="original_language";
    private String original_title;
    public static final String name_original_title="original_title";
    private String overview;
    public static final String name_overview="overview";
    private double popularity;
    public static final String name_popularity="popularity";
    private String poster_path;
    public static final String name_poster_pth="poster_path";
    private String[][] production_companies;
    public static final String name_production_companies="production_companies";
    private String[][] production_countries;
    public static final String name_production_countries="production_countries";
    private String release_date;
    public static final String name_release_date="release_date";
    private long revenue;
    public static final String name_revenue="revenue";
    private int runtime;
    public static final String name_runtime="runtime";
    private String[][] spoken_languages;
    public static final String name_spoken_language="spoken_language";
    private String status;
    public static final String name_status="status";
    private String tagline;
    public static final String name_tagline="tagline";
    private String title;
    public static final String name_title="title";
    private Bitmap bitmap_background;



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getBitmap_background() {
        return bitmap_background;
    }

    public void setBitmap_background(Bitmap bitmap_background) {
        this.bitmap_background = bitmap_background;
    }




    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String[][] getProduction_companies() {
        return production_companies;
    }

    public void setProduction_companies(String[][] production_companies) {
        this.production_companies = production_companies;
    }

    public String[][] getProduction_countries() {
        return production_countries;
    }

    public void setProduction_countries(String[][] production_countries) {
        this.production_countries = production_countries;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public long getRevenue() {
        return revenue;
    }

    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String[][] getSpoken_languages() {
        return spoken_languages;
    }

    public void setSpoken_languages(String[][] spoken_languages) {
        this.spoken_languages = spoken_languages;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getImdb_id() {
        return imdb_id;
    }

    public void setImdb_id(String imdb_id) {
        this.imdb_id = imdb_id;
    }


    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String[][] getGenres() {
        return genres;
    }

    public void setGenres(String[][] genres) {
        this.genres = genres;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult=true;
        if(adult.compareToIgnoreCase("false")==0)
            this.adult = false;
    }

    public MovieIDProvider(String id) {
        this.id = id;
    }
}

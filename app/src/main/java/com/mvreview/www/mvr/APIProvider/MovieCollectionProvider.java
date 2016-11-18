package com.mvreview.www.mvr.APIProvider;

import android.graphics.Bitmap;

/**
 * Created by Gurleen on 16-11-2015.
 */
public class MovieCollectionProvider {

    private String backdrop_path;
    //private int[] genre_ids;
    private String id;
    private String title;
    private String overview;
    private String poster_path;
    private Bitmap bitmap_poster;


    public static final String name_backdrop_path="backdrop_path";
    public static final String name_id="id";
    public static final String name_title="title";
    public static final String name_overview="overview";
    public static final String name_poster_path="poster_path";


    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Bitmap getBitmap_poster() {
        return bitmap_poster;
    }

    public void setBitmap_poster(Bitmap bitmap_poster) {
        this.bitmap_poster = bitmap_poster;
    }
}

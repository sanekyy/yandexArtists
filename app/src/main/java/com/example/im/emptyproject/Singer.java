package com.example.im.emptyproject;

import android.graphics.Bitmap;
import android.widget.TextView;

/**
 * Created by Im on 15.04.2016.
 */
public class Singer {

    public static final String NAME = "name";
    public static final String BIG_COVER = "bigCover";
    public static final String GENRES = "genres";
    public static final String TRACKS = "tracks";
    public static final String ALBUMS = "albums";
    public static final String DESCRIPTION = "description";
    public static final String LINK = "link";


    int id;
    String name;
    String genres[];
    int tracks;
    int albums;
    String link;
    String description;
    String bigCover;
    String smallCover;
    Bitmap smallCoverImg;

    Singer(int _id, String _name, String _genres[], int _tracks, int _albums, String _link,
           String _description, String _bigCover, String _smallCover){
        id=_id;
        name=_name;
        genres=_genres;
        tracks=_tracks;
        albums=_albums;
        link=_link;
        description=_description;
        bigCover=_bigCover;
        smallCover=_smallCover;
        smallCoverImg=null;
    }

}

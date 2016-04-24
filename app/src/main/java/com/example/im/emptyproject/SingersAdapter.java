package com.example.im.emptyproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

/**
 * Created by Im on 15.04.2016.
 */
public class SingersAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<Singer> singers;
    ImageLoader mImageLoader;

    SingersAdapter(Context _context, ArrayList<Singer> _singers){
        context =_context;
        singers =_singers;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mImageLoader = MySingleton.getInstance(context).getImageLoader();
    }

    @Override
    public int getCount() {
        return singers.size();
    }

    @Override
    public Object getItem(int position) {
        return singers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) { // заполняем вьюху ( старую/новую ) данными

        View view = convertView;
        if(view==null){ // Проверяем, заполняем старую вьюху, или нужно создать новую
            view = layoutInflater.inflate(R.layout.my_list_item,parent,false);
        }

        // Заполняем вьюху данными
        Singer singer = getSinger(position);

        ((TextView) view.findViewById(R.id.name)).setText(singer.name);

        String genres="";
        for(int i=0;i<singer.genres.length-1;i++){
            genres+=singer.genres[i];
            genres+=", ";
        }
        if(singer.genres.length!=0)
            genres += singer.genres[singer.genres.length-1];
        ((TextView) view.findViewById(R.id.genres)).setText(genres);

        String albumTrack=String.valueOf(singer.albums)+" альбомов, " + String.valueOf(singer.tracks) + " песен";
        ((TextView) view.findViewById(R.id.albumTrack)).setText(String.valueOf(albumTrack));

        ((NetworkImageView) view.findViewById(R.id.networkImageView)).setDefaultImageResId(R.drawable.ic_account_box_black_112dp);
        ((NetworkImageView) view.findViewById(R.id.networkImageView)).setImageUrl(singer.smallCover, mImageLoader);

        return view;
    }

    private Singer getSinger(int position) {
        return (Singer) getItem(position);
    }
}

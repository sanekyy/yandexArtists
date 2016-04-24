package com.example.im.emptyproject;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.io.UnsupportedEncodingException;

/**
 * Created by Im on 16.04.2016.
 */
public class SecondFragment extends Fragment implements View.OnClickListener{

    ActionBar actionBar;
    ImageView imageView;
    ProgressBar progressBar;
    Button repeatButton;
    TextView repeatText;

   SecondFragment(ActionBar _actionBar){
       actionBar=_actionBar;
   }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.second_fragment,null);
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getArguments().getString(Singer.NAME));
    }

    @Override
    public void onStop() {
        super.onStop();
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Инициализируем переменные
        imageView = (ImageView) getView().findViewById(R.id.imageView);
        repeatButton = (Button) getView().findViewById(R.id.repeatButton);
        repeatText = (TextView) getView().findViewById(R.id.repeatText);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);

        // Выставляем высоту изображения в зависимости от размера экрана
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        imageView.getLayoutParams().height=displayMetrics.widthPixels;
        imageView.requestLayout();

        getView().findViewById(R.id.space).getLayoutParams().height=displayMetrics.widthPixels*3/4;
        getView().findViewById(R.id.space).requestLayout();

        // Устанавливаем слушателей кнопкам, если ссылки на офф страницу нет, удаляем кнопку.
        ((Button) getView().findViewById(R.id.repeatButton)).setOnClickListener(this);
        if (getArguments().getString(Singer.LINK) != "")
            ((Button) getView().findViewById(R.id.linkButton)).setOnClickListener(this);
        else
            ((LinearLayout) getView().findViewById(R.id.dataLayout)).removeView(getView().findViewById(R.id.linkButton));
        fillData();
    }

    private void fillData() { // Заполняем вьюхи данными, переданными активити
        String genres = getArguments().getString(Singer.GENRES);
        int albums = getArguments().getInt(Singer.ALBUMS);
        int tracks = getArguments().getInt(Singer.TRACKS);
        String description = getArguments().getString(Singer.DESCRIPTION);

        loadImage();
        String dot = "\u2022";
        byte[] utf8Bytes;
        String dot2;
        String albumTrack="%2d альбомов   %2s   %2d песен";
        try {
            utf8Bytes = dot.getBytes("UTF-16BE");
            dot2 = new String(utf8Bytes,"UTF-16BE");
            ((TextView) getView().findViewById(R.id.albumTrack)).setText(String.format(albumTrack,albums,dot2,tracks));
        } catch (UnsupportedEncodingException e) {
            ((TextView) getView().findViewById(R.id.albumTrack)).setText(String.format(albumTrack,albums,"",tracks));
        }
        ((TextView) getView().findViewById(R.id.genres)).setText(genres);
        ((TextView) getView().findViewById(R.id.description)).setText(description);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.linkButton:
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(getArguments().getString(Singer.LINK))));
                break;
            case R.id.repeatButton:
                loadImage();
                break;
        }
    }

    private void loadImage() {
        getView().findViewById(R.id.repeatText).setVisibility(View.INVISIBLE);
        getView().findViewById(R.id.repeatButton).setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        ImageLoader imageLoader = MySingleton.getInstance(getActivity()).getImageLoader();
        imageLoader.get(getArguments().getString(Singer.BIG_COVER), new ImageLoader.ImageListener() {
            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if(response.getBitmap()!=null)
                    progressBar.setVisibility(View.INVISIBLE);
                    imageView.setImageBitmap(response.getBitmap());
            }

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.INVISIBLE);
                getView().findViewById(R.id.repeatText).setVisibility(View.VISIBLE);
                getView().findViewById(R.id.repeatButton).setVisibility(View.VISIBLE);
            }
        });
    }
}

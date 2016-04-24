package com.example.im.emptyproject;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements FirstFragment.OnItemSelectedListener{

    RequestQueue queue;
    FirstFragment firstFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState==null) {
            // Создаём первый экран/фрагмент, добавляем его в контейнер.
            firstFragment = new FirstFragment(getSupportActionBar());

            getFragmentManager().
                    beginTransaction().
                    add(R.id.conteiner, firstFragment).
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                    commit();

            // Создаём очередь, создаём запрос, добавляем запрос в очередь.
            queue = Volley.newRequestQueue(getApplicationContext());
            makeRequest();
        }
    }

    private void makeRequest() {
        JsonArrayRequest request = new JsonArrayRequest(getResources().getString(R.string.jsonUri),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        parseResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                makeRequest();
                            }
                        }, 1000);
                        Log.d("MyErr", "REQUEST Error");
                    }
                }
        );
        queue.add(request);
    }

    public void onItemPicked(int position) {

        // Создаём второй экран/фрагмент, передаём в него данные с помощью Bundle
        SecondFragment secondFragment = new SecondFragment(getSupportActionBar());
        secondFragment.setArguments(prepareBundle(position));

        getFragmentManager(). // Заменяем первый фрагмент вторым, добавляем транзакцию в backStack
                beginTransaction().
                replace(R.id.conteiner, secondFragment).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).
                commit();



    }

    private Bundle prepareBundle(int position){ // Подготавливаем Bundle для передачи данных второму фрагменту
        // Достаём выбранный элемент из адапрета
        Singer singer = (Singer) firstFragment.getListAdapter().getItem(position);

        // Создаём Bundle и кладём туда необходимые данные
        Bundle bundle = new Bundle();
        bundle.putString(Singer.NAME,singer.name);
        bundle.putString(Singer.BIG_COVER, singer.bigCover);

        String genres="";
        for(int i=0;i<singer.genres.length-1;i++){
            genres+=singer.genres[i];
            genres+=", ";
        }
        if(singer.genres.length!=0)
            genres += singer.genres[singer.genres.length-1];

        bundle.putString(Singer.GENRES,genres);
        bundle.putInt(Singer.ALBUMS,singer.albums);
        bundle.putInt(Singer.TRACKS,singer.tracks);
        bundle.putString(Singer.DESCRIPTION, singer.description);
        bundle.putString(Singer.LINK, singer.link);
        return bundle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // При нажатии на кнопку домой, возвращаемся к первому к первому экрану.
        if(item.getItemId()==android.R.id.home){
            getFragmentManager().popBackStack();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Аналогично, при нажатии на кнопку back, возвращаемся к первому к первому экрану,
        // либо закрываем приложение
        if (getFragmentManager().getBackStackEntryCount() > 0)
            getFragmentManager().popBackStack();
        else
            super.onBackPressed();
    }

    private void parseResponse(JSONArray response) { // Парсим полученный JSON, передаём данные адаптеру
        try {
            ArrayList<Singer> singers = new ArrayList<Singer>(response.length());

            for(int i=0;i<response.length();i++){
                JSONObject singer = response.getJSONObject(i);

                int id = singer.getInt("id");
                String name = singer.getString(Singer.NAME);

                String genres[] = new String[singer.getJSONArray(Singer.GENRES).length()];
                for (int j = 0; j < singer.getJSONArray(Singer.GENRES).length(); j++) {
                    genres[j] = singer.getJSONArray(Singer.GENRES).getString(j);
                }

                int tracks = singer.getInt(Singer.TRACKS);
                int albums = singer.getInt(Singer.ALBUMS);

                String link = "";
                if(!singer.isNull(Singer.LINK))
                    link = singer.getString(Singer.LINK);

                String description = "Nothing";
                if(!singer.isNull(Singer.DESCRIPTION))
                    description = singer.getString(Singer.DESCRIPTION);

                String bigCover = singer.getJSONObject("cover").getString("big");
                String smallCover = singer.getJSONObject("cover").getString("small");

                singers.add(new Singer(id, name, genres, tracks, albums, link,
                        description, bigCover, smallCover));
            }

            // Передаём распарсенные данные в адапрер и устанавливаем его для первого фрагмента
            firstFragment.setListAdapter(new SingersAdapter(this, singers));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
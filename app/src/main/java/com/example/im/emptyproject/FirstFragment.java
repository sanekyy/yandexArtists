package com.example.im.emptyproject;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Im on 15.04.2016.
 */
public class FirstFragment extends ListFragment{

    ActionBar actionBar;

    FirstFragment(ActionBar _actionBar){
        actionBar=_actionBar;
    }

    @Override
    public void onResume() {
        super.onResume();
        actionBar.setTitle(getResources().getString(R.string.firstFragmentTitle));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ((OnItemSelectedListener) getActivity()).onItemPicked(position); // Сообщаем активити, что нажали на элемент списка
    }

    public interface OnItemSelectedListener{
        public void onItemPicked(int position);
    }
}

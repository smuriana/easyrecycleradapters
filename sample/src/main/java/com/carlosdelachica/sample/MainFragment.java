package com.carlosdelachica.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.carlosdelachica.easyrecycleradapters.BaseRecyclerFragment;
import com.carlosdelachica.easyrecycleradapters.CommonRecyclerAdapter;

import java.util.ArrayList;

import carlosdelachica.com.myapplication.R;

public class MainFragment extends BaseRecyclerFragment<ImageData> {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLongClickable(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        generateRandomData();
    }

    private void generateRandomData() {
        ArrayList<ImageData> data = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            data.add(new ImageData("http://placeimg.com/400/200/nature/" + i, "Image " + i));
        }
        updateItems(data);
    }

    @Override
    protected CommonRecyclerAdapter<ImageData> createAdapter() {
        return new ImageAdapter(getActivity());
    }

    @Override
    protected GridLayoutManager createLayoutManager() {
        return new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_columns));
    }

    @Override
    public boolean onLongItemClicked(int position, View view) {
        Toast.makeText(getActivity(), "painting long selected " + position, Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public void onItemClick(int position, View view) {
        Toast.makeText(getActivity(), "painting selected " + position, Toast.LENGTH_LONG).show();
    }

}
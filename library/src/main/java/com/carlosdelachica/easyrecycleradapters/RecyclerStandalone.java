package com.carlosdelachica.easyrecycleradapters;

import android.content.Context;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.carlosdelachica.easyrecycleradapters.adapter.CommonRecyclerAdapter;
import com.carlosdelachica.easyrecycleradapters.decorations.DividerItemDecoration;

import java.util.List;

public class RecyclerStandalone<T> implements CommonRecyclerAdapter.OnItemClickListener,
        CommonRecyclerAdapter.OnItemLongClickListener {

    private RecyclerView recyclerView;
    private TextView auxTextView;
    private CommonRecyclerAdapter<T> adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Context context;
    private RecyclerStandaloneCallback callback;
    private DividerItemDecoration dividerItemDecoration;
    private int loadingTextColor;
    private String loadingText;
    private String emplyListText;
    private int emptyListTextColor;

    public void attachToRecyclerView(RecyclerView recyclerView, CommonRecyclerAdapter<T> adapter) {
        this.attachToRecyclerView(recyclerView, adapter, new LinearLayoutManager(recyclerView.getContext()));
    }

    public void attachToRecyclerView(RecyclerView recyclerView, CommonRecyclerAdapter<T> adapter, RecyclerView.LayoutManager layoutManager) {
        this.recyclerView = recyclerView;
        this.adapter = adapter;
        this.layoutManager = layoutManager;
        this.context = recyclerView.getContext();
        emptyListTextColor = context.getResources().getColor(R.color.empty_list_text_color);
        loadingTextColor = context.getResources().getColor(R.color.empty_list_text_color);
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        initRecyclerViewPadding();
        initAdapter();
        initLayoutManager();
        initItemDecorations();
    }

    private void initRecyclerViewPadding() {
        //Apply padding as described in Material Design specs
        int topBottomPadding = context.getResources().getDimensionPixelSize(R.dimen.material_design_list_top_bottom_padding);
        setRecyclerViewPadding(0, topBottomPadding, 0, topBottomPadding);
    }

    private void initAdapter() {
        recyclerView.setAdapter(adapter);
        initListeners();
    }

    private void initListeners() {
        adapter.setOnItemClickListener(this);
        adapter.setOnItemLongClickListener(this);
    }

    private void initLayoutManager() {
        recyclerView.setLayoutManager(layoutManager);
    }

    private void initItemDecorations() {
        initDivider();
    }

    private void initDivider() {
        int dividerRes;
        if (layoutManager instanceof GridLayoutManager) {
            dividerRes = R.drawable.grid_divider;
        } else {
            dividerRes = R.drawable.list_divider;
        }
        this.dividerItemDecoration = new DividerItemDecoration(context, context.getResources().getDrawable(dividerRes));
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    public void setDivider(@DrawableRes int dividerDrawableRes) {
        dividerItemDecoration.setDivider(dividerDrawableRes);
    }

    public void setLoadingTextColor(@ColorRes int colorRes) {
        loadingTextColor = context.getResources().getColor(colorRes);
    }

    public void setLoadingText(@StringRes int messageStringRes) {
        loadingText = context.getString(messageStringRes);
    }

    public void setEmptyListTextColor(@ColorRes int colorRes) {
        emptyListTextColor = context.getResources().getColor(colorRes);
    }

    public void setEmptyListText(@StringRes int messageStringRes) {
        emplyListText = context.getString(messageStringRes);
    }

    public void attachToAuxTextView(TextView auxTextView) {
        this.auxTextView = auxTextView;
    }

    public void setAuxTextViewEnabled(boolean enabled) {
        if (!enabled) return;
        initAuxTextView();
    }

    private void initAuxTextView() {
        updateAuxTextView(AuxTextViewStates.LOADING);
    }

    public void setCallback(RecyclerStandaloneCallback callback) {
        this.callback = callback;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerViewPadding(int left, int top, int right, int bottom) {
        recyclerView.setPadding(left, top, right, bottom);
    }

    public T getItem(int position) {
        return adapter.getItem(position);
    }

    public void updateItems(List<T> data) {
        adapter.updateItems(data);
        updateAuxTextView(data.size() > 0 ? AuxTextViewStates.HIDEN : AuxTextViewStates.EMPTY);
    }

    public void addItem(T data, int position) {
        adapter.add(data, position);
        updateAuxTextView(AuxTextViewStates.HIDEN);
    }

    public void addItem(T data) {
        adapter.add(data);
        updateAuxTextView(AuxTextViewStates.HIDEN);
    }

    public void removeItem (T data) {
        adapter.remove(data);
        updateAuxTextView(adapter.getItemCount() > 0 ? AuxTextViewStates.HIDEN : AuxTextViewStates.EMPTY);
    }

    public void removeItem (int position) {
        adapter.remove(position);
        updateAuxTextView(adapter.getItemCount() > 0 ? AuxTextViewStates.HIDEN : AuxTextViewStates.EMPTY);
    }

    public void onRefresh() {
        adapter.clearItems();
        updateAuxTextView(AuxTextViewStates.LOADING);
    }

    private void updateAuxTextView(AuxTextViewStates loading) {
        switch (loading) {
            case HIDEN:
                setAuxTextViewVisible(false);
                break;
            case EMPTY:
                setAuxTextViewVisible(true);
                setAuxTextViewText(emplyListText);
                setAuxTextViewTextColor(emptyListTextColor);
                break;
            case LOADING:
                setAuxTextViewVisible(true);
                setAuxTextViewText(loadingText);
                setAuxTextViewTextColor(loadingTextColor);
                break;
        }
    }

    private void setAuxTextViewText(String text) {
        if (auxTextView == null) return;
        auxTextView.setText(text);
    }

    private void setAuxTextViewTextColor(int color) {
        if (auxTextView == null) return;
        auxTextView.setTextColor(color);
    }

    private void setAuxTextViewVisible(boolean visible) {
        if (auxTextView == null) return;
        auxTextView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onItemClick(int position, View view) {
        if (callback == null) return;
        callback.onItemClick(position, view);
    }

    @Override
    public boolean onLongItemClicked(int position, View view) {
        if (callback == null) return false;
        return callback.onLongItemClicked(position, view);
    }

    public interface RecyclerStandaloneCallback {
        void onItemClick(int position, View view);
        boolean onLongItemClicked(int position, View view);
    }

}

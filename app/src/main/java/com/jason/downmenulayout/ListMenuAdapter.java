package com.jason.downmenulayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ListMenuAdapter extends BaseMenuAdapter {

    private String[] mItems = {"类型","品牌","价格","更多"};
    private Context mContext;

    public ListMenuAdapter (Context context){
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public View getTabView(int position, ViewGroup parent) {
        TextView tabView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.tab_item_layout, parent,
                false);
        tabView.setText(mItems[position]);
        return tabView;
    }

    @Override
    public View getMenuView(int position, ViewGroup parent) {
        TextView menuView = (TextView) LayoutInflater.from(mContext).inflate(R.layout.menu_item_layout, parent,
                false);
        menuView.setText(mItems[position]);
        return menuView;
    }
}

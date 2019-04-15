package com.jason.downmenulayout;

import android.view.View;
import android.view.ViewGroup;

public abstract class BaseMenuAdapter {

    public abstract int getCount();

    public abstract View getTabView(int position, ViewGroup parent);

    public abstract View getMenuView(int position,ViewGroup parent);

}

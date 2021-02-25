package com.xw.aschwitkey.FloorCountDownLib;

import java.util.Observer;

import androidx.recyclerview.widget.RecyclerView;

public interface ICountDownCenter {
    void addObserver(Observer observer);
    void deleteObservers();
    void startCountdown();
    void stopCountdown();
    boolean containHolder(Observer observer);
    void notifyAdapter();
    void bindRecyclerView(RecyclerView recyclerView);
}
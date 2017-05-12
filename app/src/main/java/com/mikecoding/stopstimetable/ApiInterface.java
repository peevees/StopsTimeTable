package com.mikecoding.stopstimetable;

import java.util.ArrayList;

public interface ApiInterface {
    void onTaskComplete(ArrayList<Station> stations);
    void showProgressBar();
    void hideProgressBar();
    void displayToast(int msg);
}
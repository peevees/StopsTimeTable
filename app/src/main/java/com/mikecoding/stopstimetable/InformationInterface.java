package com.mikecoding.stopstimetable;

import java.util.ArrayList;

/**
 * Created by micste on 2017-05-08.
 */

public interface InformationInterface {
    void onTaskComplete(ArrayList<Information> subwayInfo, ArrayList<Information> trainInfo, ArrayList<Information> busInfo);
    void showProgressBar();
    void hideProgressBar();
    void displayToast(int msg);
    void lastUpdate(String time);
}

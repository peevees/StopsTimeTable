package com.mikecoding.stopstimetable;

import java.util.ArrayList;

/**
 * Created by micste on 2017-05-08.
 */

public interface InformationInterface {
    void onTaskComplete(ArrayList<Information> informations);
    void hideProgressBar();
    void displayToast(String msg);
}

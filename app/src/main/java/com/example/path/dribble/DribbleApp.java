package com.example.path.dribble;

import android.app.Application;
import android.content.Context;

/**
 * Created by Кот on 26.12.2016.
 */

public class DribbleApp extends Application {


    private static Context context;

    public void onCreate() {
        super.onCreate();
        DribbleApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return DribbleApp.context;
    }
}


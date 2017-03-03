package com.example.elabelle.cp282final;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.StrictMode;
import android.support.annotation.NonNull;

import com.example.elabelle.cp282final.utils.Constants;

/**
 * Created by elabelle on 2/26/17.
 */

public class CP282Final extends Application {

    private static Context mContext;

    static SharedPreferences prefs;


    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        prefs = getSharedPreferences(Constants.PREFS_NAME, MODE_MULTI_PROCESS);

        if (isDebugBuild()) {
            StrictMode.enableDefaults();
        }
    }

    @NonNull
    public static boolean isDebugBuild() {
        return BuildConfig.BUILD_TYPE.equals("debug");
    }


    @Override
    // Used to restore user selected locale when configuration changes
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


    public static Context getAppContext() {
        return CP282Final.mContext;
    }


    /**
     * Statically returns app's default SharedPreferences instance
     *
     * @return SharedPreferences object instance
     */
    public static SharedPreferences getSharedPreferences() {
        return getAppContext().getSharedPreferences(Constants.PREFS_NAME, MODE_MULTI_PROCESS);
    }
}

package com.example.elabelle.cp282final.helpers;

import android.content.ContentValues;
import android.util.Log;

import com.example.elabelle.cp282final.models.Note;
import com.example.elabelle.cp282final.utils.Constants;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by elabelle on 3/2/17.
 */


public class UpgradeProcessor {

    private final static String METHODS_PREFIX = "onUpgradeTo";

    private static UpgradeProcessor instance;


    private UpgradeProcessor() {
    }


    private static UpgradeProcessor getInstance() {
        if (instance == null) {
            instance = new UpgradeProcessor();
        }
        return instance;
    }


    public static void process(int dbOldVersion, int dbNewVersion) {
        try {
            List<Method> methodsToLaunch = getInstance().getMethodsToLaunch(dbOldVersion, dbNewVersion);
            for (Method methodToLaunch : methodsToLaunch) {
                methodToLaunch.invoke(getInstance());
            }
        } catch (SecurityException | IllegalAccessException | InvocationTargetException e) {
            Log.d(Constants.TAG, "Explosion processing upgrade!", e);
        }
    }


    private List<Method> getMethodsToLaunch(int dbOldVersion, int dbNewVersion) {
        List<Method> methodsToLaunch = new ArrayList<>();
        Method[] declaredMethods = getInstance().getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getName().contains(METHODS_PREFIX)) {
                int methodVersionPostfix = Integer.parseInt(declaredMethod.getName().replace(METHODS_PREFIX, ""));
                if (dbOldVersion <= methodVersionPostfix && methodVersionPostfix <= dbNewVersion) {
                    methodsToLaunch.add(declaredMethod);
                }
            }
        }
        return methodsToLaunch;
    }

    /**
     * Ensures that no duplicates will be found during the creation-to-id transition
     */
    private void onUpgradeTo501() {
        List<Long> creations = new ArrayList<>();
        for (Note note : DbHelper.getInstance().getAllNotes(false)) {
            if (creations.contains(note.getCreation())) {

                ContentValues values = new ContentValues();
                values.put(DbHelper.KEY_CREATION, note.getCreation() + (long) (Math.random() * 999));
                DbHelper.getInstance().getDatabase().update(DbHelper.TABLE_NOTES, values, DbHelper.KEY_TITLE +
                        " = ? AND " + DbHelper.KEY_CREATION + " = ? AND " + DbHelper.KEY_CONTENT + " = ?", new String[]{note
                        .getTitle(), String.valueOf(note.getCreation()), note.getContent()});
            }
            creations.add(note.getCreation());
        }
    }

}
package com.example.elabelle.cp282final.utils;

import android.content.Context;

import com.example.elabelle.cp282final.CP282Final;
import com.example.elabelle.cp282final.R;
import com.example.elabelle.cp282final.models.Category;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by elabelle on 2/27/17.
 */

public class Navigation {
    public static final int HOME = 0;
    public static final int ARCHIVE = 1;
    public static final int TRASH = 2;
    public static final int UNCATEGORIZED = 3;
    public static final int CATEGORY = 4;
    public static final int NOTEBOOK = 5;
    public static final int TAG = 6;


    /**
     * Returns actual navigation status
     */
    public static int getNavigation() {
        String[] navigationListCodes = CP282Final.getAppContext().getResources().getStringArray(R.array.nav_item_list);
        String navigation = getNavigationText();

        if (navigationListCodes[HOME].equals(navigation)) {
            return HOME;
        } else if (navigationListCodes[ARCHIVE].equals(navigation)) {
            return ARCHIVE;
        } else if (navigationListCodes[TRASH].equals(navigation)) {
            return TRASH;
        } else if (navigationListCodes[UNCATEGORIZED].equals(navigation)) {
            return UNCATEGORIZED;
        } else {
            return CATEGORY;
        }
    }


    public static String getNavigationText() {
        Context mContext = CP282Final.getAppContext();
        String[] navigationListCodes = mContext.getResources().getStringArray(R.array.nav_item_list);
        @SuppressWarnings("static-access")
        String navigation = mContext.getSharedPreferences(Constants.PREFS_NAME,
                mContext.MODE_MULTI_PROCESS).getString(Constants.PREF_NAVIGATION, navigationListCodes[0]);
        return navigation;
    }


    /**
     * Retrieves category currently shown
     *
     * @return id of category or null if current navigation is not a category
     */
    public static Long getCategory() {
        if (getNavigation() == CATEGORY) {
            return Long.valueOf(CP282Final.getAppContext().getSharedPreferences(Constants.PREFS_NAME, Context
                    .MODE_MULTI_PROCESS).getString(Constants.PREF_NAVIGATION, ""));
        } else {
            return null;
        }
    }


    /**
     * Checks if passed parameters is the actual navigation status
     */
    public static boolean checkNavigation(int navigationToCheck) {
        return checkNavigation(new Integer[]{navigationToCheck});
    }


    public static boolean checkNavigation(Integer[] navigationsToCheck) {
        boolean res = false;
        int navigation = getNavigation();
        for (int navigationToCheck : new ArrayList<>(Arrays.asList(navigationsToCheck))) {
            if (navigation == navigationToCheck) {
                res = true;
                break;
            }
        }
        return res;
    }


    /**
     * Checks if passed parameters is the category user is actually navigating in
     */
    public static boolean checkNavigationCategory(Category categoryToCheck) {
        Context mContext = CP282Final.getAppContext();
        String[] navigationListCodes = mContext.getResources().getStringArray(R.array.nav_item_list);
        String navigation = mContext.getSharedPreferences(Constants.PREFS_NAME, Context.MODE_MULTI_PROCESS).getString(Constants.PREF_NAVIGATION, navigationListCodes[0]);
        return (categoryToCheck != null && navigation.equals(String.valueOf(categoryToCheck.getId())));
    }
}

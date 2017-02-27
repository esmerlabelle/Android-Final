package com.example.elabelle.cp282final.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.elabelle.cp282final.R;
import com.example.elabelle.cp282final.fragments.CategoriesFragment;
import com.example.elabelle.cp282final.fragments.HomeFragment;
import com.example.elabelle.cp282final.fragments.NoteFragment;
import com.example.elabelle.cp282final.fragments.NotebookFragment;
import com.example.elabelle.cp282final.fragments.TagsFragment;

public class MainActivity extends AppCompatActivity
        implements HomeFragment.OnFragmentInteractionListener,
        CategoriesFragment.OnFragmentInteractionListener,
        NoteFragment.OnFragmentInteractionListener,
        NotebookFragment.OnFragmentInteractionListener,
        TagsFragment.OnFragmentInteractionListener {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_NOTE = "note";
    private static final String TAG_NOTEBOOKS = "notebooks";
    private static final String TAG_CATEGORIES = "categories";
    private static final String TAG_TAGS = "tags";
    public static String CURRENT_TAG = TAG_HOME;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        handler = new Handler();

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //createFABMenu();

        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }
    // select fragment from menu
    private void loadHomeFragment() {
        // select nav menu item
        selectNavMenu();

        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawerLayout.closeDrawers();

            //toggleFab();
            return;
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // replace fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        handler.post(runnable);

        drawerLayout.closeDrawers();
    }
    private Fragment getHomeFragment() {
        switch (CURRENT_TAG) {
            case TAG_HOME:
                // home
                return new HomeFragment();
            case TAG_NOTEBOOKS:
                // notebook
                return new NotebookFragment();
            case TAG_CATEGORIES:
                // categories
                return new CategoriesFragment();
            case TAG_TAGS:
                // tags
                return new TagsFragment();
            case TAG_NOTE:
                return new NoteFragment();
            default:
                return new HomeFragment();
        }
    }
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    public void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();

                        // Check which item is clicked and perform the action
                        switch (menuItem.getItemId()) {
                            case R.id.nav_home:
                                navItemIndex = 0;
                                CURRENT_TAG = TAG_HOME;
                                break;
                            case R.id.nav_notebooks:
                                navItemIndex = 1;
                                CURRENT_TAG = TAG_NOTEBOOKS;
                                break;
                            case R.id.nav_categories:
                                navItemIndex = 2;
                                CURRENT_TAG = TAG_CATEGORIES;
                                break;
                            case R.id.nav_tags:
                                navItemIndex = 3;
                                CURRENT_TAG = TAG_TAGS;
                                break;
                            case R.id.nav_settings:
                                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                drawerLayout.closeDrawers();
                                menuItem.setCheckable(false);
                                menuItem.setChecked(false);
                                return true;
                            default:
                                navItemIndex = 0;
                        }

                        if (menuItem.isChecked()) {
                            menuItem.setChecked(false);
                        } else {
                            menuItem.setChecked(true);
                        }
                        menuItem.setChecked(true);

                        loadHomeFragment();

                        return true;
                    }
                });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
            return;
        }

        if (!(CURRENT_TAG.equals(TAG_HOME))) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
            return;
        }
        super.onBackPressed();
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

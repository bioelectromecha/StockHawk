package com.udacity.stockhawk.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.fragments.DetailFragment;

public class StockDetailActivity extends AppCompatActivity {

    private static final String KEY_DETAIL_FRAGMENT = "DETAIL_FRAGMENT";
    private DetailFragment mDetailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // get the fragment if it already exists
        mDetailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(KEY_DETAIL_FRAGMENT);

        if (mDetailFragment == null) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            DetailFragment detailFragment = new DetailFragment();
            fragmentTransaction.replace(R.id.activity_detail_fragment_container, detailFragment, KEY_DETAIL_FRAGMENT);
            fragmentTransaction.commit();
        }
    }
}

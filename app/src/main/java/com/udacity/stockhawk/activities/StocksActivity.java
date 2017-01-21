package com.udacity.stockhawk.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.fragments.StocksFragment;

/**
 * this activity is mainly a container fro the stocks fragment
 */
public class StocksActivity extends AppCompatActivity {
    private static final String KEY_STOCKS_FRAGMENT = "STOCKS_FRAGMENT";
    private StocksFragment mStocksFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);


    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // get the fragment if it already exists
        mStocksFragment = (StocksFragment) getSupportFragmentManager().findFragmentByTag(KEY_STOCKS_FRAGMENT);

        if (mStocksFragment == null) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            StocksFragment stockListFragment = new StocksFragment();
            fragmentTransaction.replace(R.id.activity_stocks_fragment_container, stockListFragment, KEY_STOCKS_FRAGMENT);
            fragmentTransaction.commit();
        }
    }
}

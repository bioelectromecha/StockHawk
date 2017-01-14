package com.udacity.stockhawk.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.fragments.StocksListFragment;

public class StockListActivity extends AppCompatActivity {
    private static final String KEY_STOCKS_FRAGMENT = "STOCKS_FRAGMENT";
    private StocksListFragment mStocksFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // get the fragment if it already exists
        mStocksFragment = (StocksListFragment) getSupportFragmentManager().findFragmentByTag(KEY_STOCKS_FRAGMENT);
        
        if (mStocksFragment != null) {
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            StocksListFragment moviesFragment = new StocksListFragment();
            fragmentTransaction.replace(R.id.activity_stock_list_fragment_container, moviesFragment, KEY_STOCKS_FRAGMENT);
            fragmentTransaction.commit();
        }
    }
}

package com.udacity.stockhawk.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

public class StockWidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    int mWidgetId;
    private Cursor mCursor;
    private Context mContext;

    public StockWidgetFactory(Context context, Intent intent) {
        mContext = context;
        mWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        // Nothing to do
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_stocks_list_item);
        if (mCursor.moveToPosition(position)) {
            remoteViews.setTextViewText(R.id.widget_stock_symbol,
                    mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_SYMBOL)));
            remoteViews.setTextViewText(R.id.widget_price,
                    mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_PRICE)));
            remoteViews.setTextViewText(R.id.widget_change,
                    mCursor.getString(mCursor.getColumnIndex(Contract.Quote.COLUMN_PERCENTAGE_CHANGE)));
        }
        return remoteViews;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                null, null, Contract.Quote.COLUMN_SYMBOL);
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

}
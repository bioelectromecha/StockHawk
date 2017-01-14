package com.udacity.stockhawk.fragments;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.stockhawk.Constants;
import com.udacity.stockhawk.R;
import com.udacity.stockhawk.data.Contract;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 500;
    LineChartView mChart;
    TextView mTitleTextView;
    private String mSymbol;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSymbol = getActivity().getIntent().getStringExtra(Constants.KEY_SYMBOL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        mChart = (LineChartView) view.findViewById(R.id.fragment_detail_chart);
        mTitleTextView = (TextView) view.findViewById(R.id.fragment_detail_title);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().getSupportLoaderManager().initLoader(DETAIL_LOADER, null, this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getActivity().onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String selection = Contract.Quote.COLUMN_SYMBOL + "=?";
        String[] selectionArgs = {mSymbol};

        return new CursorLoader(getActivity(),
                Contract.Quote.URI,
                Contract.Quote.QUOTE_COLUMNS.toArray(new String[]{}),
                selection, selectionArgs, Contract.Quote.COLUMN_SYMBOL);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<Pair<Float, Float>> historyList = new ArrayList<>(0);
        if (data != null) {
            data.moveToFirst();
            do {
                String chartPointsCsvString = data.getString(Contract.Quote.POSITION_HISTORY);
                List<String> items = Arrays.asList(chartPointsCsvString.split("\\n"));
                for (String item : items) {
                    List<String> subItems = Arrays.asList(item.split("\\s*, \\s*"));
                    Pair<Float, Float> pair = new Pair<>(Float.valueOf(subItems.get(0)), Float.valueOf(subItems.get(1)));
                    historyList.add(pair);
                }
                mTitleTextView.setText(data.getString(Contract.Quote.POSITION_SYMBOL));
            } while (data.moveToNext());
        }
        updateChart(historyList);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void updateChart(List<Pair<Float,Float>> historyList) {
        DateFormat dateFormat = SimpleDateFormat.getDateInstance();

        List<AxisValue> axisValuesX = new ArrayList<>();
        List<PointValue> pointValues = new ArrayList<>();

        for (int i = historyList.size() - 1; i >= 0; i--) {
            pointValues.add(new PointValue(historyList.get(i).first, historyList.get(i).second));

            if (i >= 0 && i % (historyList.size() / 2) == 0) {
                AxisValue axisValueX = new AxisValue(historyList.get(i).first);
                axisValueX.setLabel(dateFormat.format(new Date(historyList.get(i).first.longValue())));
                axisValuesX.add(axisValueX);
            }
        }

        // Prepare data for chart
        Line line = new Line(pointValues).setColor(Color.WHITE).setCubic(false);
        List<Line> lines = new ArrayList<>();
        lines.add(line);
        LineChartData lineChartData = new LineChartData();
        lineChartData.setLines(lines);

        // Init x-axis
        Axis axisX = new Axis(axisValuesX);
        axisX.setHasLines(true);
        axisX.setMaxLabelChars(4);
        lineChartData.setAxisXBottom(axisX);

        // Init y-axis
        Axis axisY = new Axis();
        axisY.setAutoGenerated(true);
        axisY.setHasLines(true);
        axisY.setMaxLabelChars(4);
        lineChartData.setAxisYLeft(axisY);

        // Update chart with new data.
        mChart.setInteractive(false);
        mChart.setContainerScrollEnabled(true, ContainerScrollType.VERTICAL);
        mChart.setLineChartData(lineChartData);

        // Show chart
        mChart.setVisibility(View.VISIBLE);
    }
}

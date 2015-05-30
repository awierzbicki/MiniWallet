package com.github.miniwallet.graphs;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.ChartData;
import com.github.miniwallet.R;

public class BarChartItem extends ChartItem {

    public BarChartItem(ChartData<?> cd, Context c) {
        super(cd);
    }

    @Override
    public int getItemType() {
        return TYPE_BARCHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_barchart, null);
            holder.chart = (BarChart) convertView.findViewById(R.id.chart);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.setDescription("");
        holder.chart.setDrawGridBackground(false);
        holder.chart.setDrawBarShadow(false);
        holder.chart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        holder.chart.getLegend().setTextSize(16f);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setXOffset(20f);

        YAxis leftAxis = holder.chart.getAxisLeft();
        leftAxis.setLabelCount(5);

        YAxis rigthAxis = holder.chart.getAxisRight();
        rigthAxis.setLabelCount(5);

        // set data
        holder.chart.setData((BarData) mChartData);

        // do not forget to refresh the chart
//        holder.chart.invalidate();
        holder.chart.animateY(700);
        holder.chart.setVisibleXRange(3);

        holder.chart.setScaleYEnabled(false);
        holder.chart.setHighlightEnabled(false);

        return convertView;
    }

    private static class ViewHolder {
        BarChart chart;
    }
}
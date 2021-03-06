package com.github.miniwallet.graphs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.github.miniwallet.R;

public class PieChartItem extends ChartItem {


    public PieChartItem(ChartData<?> cd, Context c) {
        super(cd);
    }

    @Override
    public int getItemType() {
        return TYPE_PIECHART;
    }

    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();

            convertView = LayoutInflater.from(c).inflate(
                    R.layout.list_item_piechart, null);
            holder.chart = (PieChart) convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // apply styling
        holder.chart.setHoleRadius(40f);
        holder.chart.setTransparentCircleRadius(50f);
        holder.chart.setCenterText("Expenses\nby categories");

        holder.chart.setCenterTextSize(18f);
        holder.chart.setUsePercentValues(true);

        mChartData.setValueFormatter(new PercentFormatter());

        mChartData.setValueTextSize(15f);
        mChartData.setValueTextColor(Color.BLACK);
        // set data
        holder.chart.setData((PieData) mChartData);

        holder.chart.setDescription("");
        holder.chart.getLegend().setEnabled(false);
        //Legend l = holder.chart.getLegend();
        //l.setPosition(LegendPosition.RIGHT_OF_CHART);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateXY(1500, 1500);

        return convertView;
    }

    private static class ViewHolder {
        PieChart chart;
    }
}

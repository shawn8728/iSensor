package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class bot1_Activity extends AppCompatActivity {
    LineChart lineChart;
    DatabaseReference mref;
    YAxis leftYAxis;
    YAxis rightYAxis;
    XAxis xAxis;
    int s = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot1_);

        lineChart = findViewById(R.id.linechart1);

        lineChart.getDescription().setEnabled(false);
        lineChart.setDrawBorders(true);

        xAxis = lineChart.getXAxis();
        leftYAxis = lineChart.getAxisLeft();
        rightYAxis = lineChart.getAxisRight();
        leftYAxis.setAxisMinimum(0f);
        leftYAxis.setAxisMaximum(120f);
        rightYAxis.setEnabled(false);

        MarkerView mv = new markerview1(this,R.layout.markerview);
        lineChart.setMarkerView(mv);

        lineChart.setTouchEnabled(true);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.animateX(2000);

        Legend legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextSize(11f);

        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.my_color));
        getSupportActionBar().setTitle("Humidity");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseDatabase firebasedatabase = FirebaseDatabase.getInstance();
        mref = firebasedatabase.getReference("DHT11").child("Data");
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Entry> datavalues = new ArrayList<>();
                ArrayList<String>dates = new ArrayList<>();
                float i = 0;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String SV =ds.child("humidity").getValue().toString();
                    String date = ds.child("date").getValue().toString();
                    String time = ds.child("time").getValue().toString();
                    String x0 = time;
                    String x1 = date +"\n"+time ;
                    String change = "00:00";
                    String change1 = "12:00";
                    float a = Float.parseFloat(SV);
                    int flag = x0.compareTo(change);
                    int flag1 = x0.compareTo(change1);
                    if(i==0 || i%500==0 || flag==0 || flag1==0) {
                        dates.add(x1);
                    }
                    else {
                        dates.add(x0);
                    }
                    datavalues.add(new Entry(i, a));
                    i = i + 1;
                }
                if(s == 0 || s % 3 == 0) {
                    lineChart.moveViewToX(i);
                }
                s++;
                final LineDataSet lineDataSet = new LineDataSet(datavalues, "Humidity");
                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                dataSets.add(lineDataSet);
                LineData data = new LineData(lineDataSet);
                lineDataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return ""+value;    //
                    }
                });
                lineChart.setData(data);

                lineChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IndexAxisValueFormatter(dates));
                lineChart.getXAxis().setLabelCount(4);
                lineChart.setVisibleXRangeMaximum(300);

                lineDataSet.setHighlightEnabled(true);
                lineDataSet.setDrawValues(false);
                lineDataSet.setDrawCircles(false);
                lineDataSet.setDrawHighlightIndicators(true);
                lineDataSet.setHighLightColor(Color.BLACK);
                lineDataSet.setColor(Color.BLACK);
                lineDataSet.setValueTextSize(11f);
                lineDataSet.setValueTextColor(Color.BLACK);
                lineDataSet.setCircleColor(Color.BLACK);
                lineDataSet.setLineWidth(2.0f);

                lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                lineDataSet.setCubicIntensity(0.2f);
                lineDataSet.setDrawFilled(true);
                lineDataSet.setFillAlpha(128);
                lineDataSet.setFillColor(Color.GRAY);

                lineChart.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i("Posts", "Listener was canceled");
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Animatoo.animateSlideRight(this);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
            Animatoo.animateSlideRight(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

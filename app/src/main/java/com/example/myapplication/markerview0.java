package com.example.myapplication;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

public class markerview0 extends MarkerView {
    private TextView mContentTv;
    public String value;

    public markerview0(Context context, int layoutResource) {
        super(context, layoutResource);
        mContentTv = findViewById(R.id.tv_content_marker_view);
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        value =  (Float)e.getY() + "°C" ;
        mContentTv.setText(value);
        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        // Log.e("ddd", "width:" + (-(getWidth() / 2)) + "height:" + (-getHeight()));
        return new MPPointF(-(getWidth() / 2), -(getHeight()  + 50));
    }


}




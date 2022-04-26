package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    List<notifications> item;
    private Context context;


    public CustomAdapter(Context context, List<notifications> arrayList) {
        this.context = context;
        this.item = arrayList;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // R.layout.custom_layout 是你自己創的自訂layout( 可參考下面)
        View v = View.inflate(context, R.layout.customlist, null);
        TextView title = v.findViewById(R.id.textView1);
        TextView context = v.findViewById(R.id.textView2);
        title.setText(item.get(position).getText());
        context.setText(item.get(position).getTime());

        return v;
    }
}

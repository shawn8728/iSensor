package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity {

    DatabaseReference databaseReference;
    SearchView searchView;
    ListView listview;
    ArrayList<String>arrayList = new ArrayList<String>();
    ArrayAdapter<String> Adapter;

    CustomAdapter CustomAdapter;
    ArrayList<String> rawData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchfragment);


        listview = findViewById(R.id.Listview);
        searchView = findViewById(R.id.Searchview);
        searchView.setFocusable(false);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(true);
        searchView.requestFocusFromTouch();
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("輸入");
        listview.setTextFilterEnabled(true);

        rawData = new ArrayList<String>();


        Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);



        CustomAdapter = new CustomAdapter(this,rawData);
        listview.setAdapter(Adapter);



        databaseReference = FirebaseDatabase.getInstance().getReference("DHT11").child("Data");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String value0= dataSnapshot.child("date").getValue().toString();
                String value1= dataSnapshot.child("time").getValue().toString();
                String value2 = dataSnapshot.child("temperature").getValue().toString();
                String value3 = dataSnapshot.child("humidity").getValue().toString();
                String value4= dataSnapshot.child("dustdensity").getValue().toString();
                arrayList.add(value0+" "+value1+ "\n\nTemperature : " + value2 + "°C\nHumidity : "+value3 + " %\nDustdensity : "+value4+" μg/m^3\n" );
                rawData.add("\nDate : "+value0+"\nTime : "+value1+ "\nTemperature : " + value2 + "°C\nHumidity : "+value3 + " %\nDustdensity : "+value4+" μg/m^3\n" );
                Adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Adapter.getFilter().filter(newText);

                return true;
            }

        });


    }

    private class CustomAdapter extends BaseAdapter {
        LayoutInflater inflater;
        ArrayList<String> item;
        List<String> list = null;

        public CustomAdapter(Context context, ArrayList<String> list) {
            inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.list = list;
            this.item = new ArrayList<String>();
            this.item.addAll(list);
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
       //     TextView text = findViewById(R.id.textView);
        //    text.setText(list.get(position));

            return convertView;
        }


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

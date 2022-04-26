package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FragmentSearch extends Fragment {

    DatabaseReference databaseReference;
    SearchView searchView;
    ListView listview;
    Context context;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> Adapter;
    ArrayList<String> rawData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.searchfragment,container,false);
        context = getActivity();
        listview = view.findViewById(R.id.Listview);
        searchView = view.findViewById(R.id.Searchview);
        searchView.setFocusable(false);
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(true);
        searchView.requestFocusFromTouch();
        searchView.setSubmitButtonEnabled(false);
        searchView.setQueryHint("輸入");
        listview.setTextFilterEnabled(true);

        rawData = new ArrayList<String>();


        Adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayList);



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

        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle(" ");
    }


}

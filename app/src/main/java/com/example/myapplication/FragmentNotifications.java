package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentNotifications extends Fragment {
    private TextView mTextMessage;
    private ListView listview;
    CustomAdapter customadapter;
    Context context;
    List<notifications> arrayList;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public String temp0;
    public String hum0;
    public String dust0;
    public String temp1;
    public String hum1;
    public String dust1;
    public float temp2;
    public float hum2;
    public float dust2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notificationsfragment,container,false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
        context = getActivity();

        listview = view.findViewById(R.id.notificationlist);
        arrayList = new ArrayList<>();
        customadapter = new CustomAdapter(context,arrayList);
        listview.setAdapter(customadapter);


        databaseReference = FirebaseDatabase.getInstance().getReference().child("DHT11");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                temp0 = dataSnapshot.child("realtime").child("Temperature").getValue().toString();
                hum0 = dataSnapshot.child("realtime").child("Humidity").getValue().toString();
                dust0 = dataSnapshot.child("realtime").child("DustDensity").getValue().toString();
                temp1 = temp0.substring(0,2);
                hum1 = hum0.substring(0,2);
                dust1 = dust0.substring(0,4);
                temp2 = Float.parseFloat(temp1);
                hum2 = Float.parseFloat(hum1);
                dust2 = Float.parseFloat(dust1);
                if (temp2>=30)
                    arrayList.add(new notifications("Temperature sensor","溫度過高 高於30°C"));
                if (hum2>=80)
                    arrayList.add(new notifications("Humidity sensor", "濕度過高 高於80%"));
                if (dust2>=35 && dust2<=53)
                    arrayList.add(new notifications("Dust density sensor", "環境對敏感族群不健康"));
                if (dust2>=54 && dust2<=149)
                    arrayList.add(new notifications("Dust density sensor", "環境對所有族群不健康"));
                if (dust2>=150 && dust2<=249)
                    arrayList.add(new notifications( "Dust density sensor", "非常不健康"));
                if (dust2>=250)
                    arrayList.add(new notifications("Dust density sensor", "危害嚴重"));
                Log.i("temp", "dsd");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });







        return  view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("Notifications");
    }

}


package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class FragmentHome extends Fragment {
    private Button btn0;
    private Button btn1;
    private Button btn2;
    Context context;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //firebase
    private TextView temp;
    private TextView hum;
    private TextView dust;
    private TextView date;
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
            View view = inflater.inflate(R.layout.homefragment,container,false);
             ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
            context = getActivity();
            temp = view.findViewById(R.id.msg1);
            hum = view.findViewById(R.id.msg2);
            dust = view.findViewById(R.id.msg3);
            date = view.findViewById(R.id.date);

            databaseReference = FirebaseDatabase.getInstance().getReference().child("DHT11");



            //button
            btn0 =  view.findViewById(R.id.button1);
            btn0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), bot0_Activity.class);
                    startActivity(intent);
                    Animatoo.animateSlideLeft(getActivity());
                }
            });
            btn1 =  view.findViewById(R.id.button2);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), bot1_Activity.class);
                    startActivity(intent);
                    Animatoo.animateSlideLeft(getActivity());
                }
            });
            btn2 =  view.findViewById(R.id.button3);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), bot2_Activity.class);
                    startActivity(intent);
                    Animatoo.animateSlideLeft(getActivity());
                }
            });


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

                    temp.setText(temp0);
                    hum.setText(hum0);
                    dust.setText(dust0);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


        SimpleDateFormat   formatter   =   new SimpleDateFormat("yyyy年MM月dd日  ");
        Date curDate =  new Date(System.currentTimeMillis());
        String   str   =   formatter.format(curDate);
        date.setText(str);




      return  view;
    }

    @Override
    public void onResume(){
        super.onResume();
        ((MainActivity) getActivity()).setActionBarTitle("iSensor");
    }


}

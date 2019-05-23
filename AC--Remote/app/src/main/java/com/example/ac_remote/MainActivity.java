package com.example.ac_remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private Button power, plus, minus, swing;
    private String powervalue = "", swingvalue = "", tempvalue = "";
    TextView temperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        temperature = findViewById( R.id.temp );
        swing = findViewById( R.id.swing );
        power = findViewById( R.id.power );
        plus = findViewById( R.id.plus_button );
        minus = findViewById( R.id.minus_button );
        if(isNetworkAvailable()) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference swingd = database.getReference( "swing" );
            final DatabaseReference tempd = database.getReference( "temp" );
            final DatabaseReference powerd = database.getReference( "power" );
            swingd.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    swingvalue = (String) dataSnapshot.getValue();
                    if (swingvalue.equals( "1" )) {
                        swing.setTextColor( getResources().getColor( android.R.color.holo_green_light ) );
                    } else {
                        swing.setTextColor( getResources().getColor( android.R.color.black ) );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

            swing.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (swingvalue.equals( "1" )) {
                        swingd.setValue( "0" );
                        swing.setTextColor( getResources().getColor( android.R.color.black ) );
                    } else {
                        swingd.setValue( "1" );
                        swing.setTextColor( getResources().getColor( android.R.color.holo_green_light ) );
                    }
                }
            } );
            powerd.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    powervalue = (String) dataSnapshot.getValue();
                    if (powervalue.equals( "1" )) {
                        power.setTextColor( getResources().getColor( android.R.color.holo_green_light ) );

                    } else {
                        power.setTextColor( getResources().getColor( android.R.color.black ) );
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );

            power.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (powervalue.equals( "1" )) {
                        power.setTextColor( getResources().getColor( android.R.color.black ) );
                        powerd.setValue( "0" );
                    } else {
                        powerd.setValue( "1" );
                        power.setTextColor( getResources().getColor( android.R.color.holo_green_light ) );

                    }
                }
            } );
            tempd.addValueEventListener( new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tempvalue = (String) dataSnapshot.getValue();
                    temperature.setText( tempvalue + " °C" );
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            } );
            plus.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt( tempvalue );
                    if (temp < 40)
                        ++temp;
                    tempd.setValue( "" + temp );
                }
            } );
            minus.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int temp = Integer.parseInt( tempvalue );
                    if (temp > 16)
                        --temp;
                    tempd.setValue( "" + temp );
                }
            } );
        }
        else
            Toast.makeText( this, "Check your internet connection", Toast.LENGTH_LONG ).show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService( Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    @Override
    protected void onResume() {
        temperature = findViewById( R.id.temp );
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference swingd = database.getReference( "swing" );
        swingd.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                swingvalue = (String) dataSnapshot.getValue();
                if (swingvalue.equals( "1" )) {
                    swing.setTextColor( getResources().getColor( android.R.color.holo_green_light ) );
                } else {
                    swing.setTextColor( getResources().getColor( android.R.color.black ) );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        DatabaseReference powerd = database.getReference( "power" );
        powerd.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                powervalue = (String) dataSnapshot.getValue();
                if (powervalue.equals( "1" )) {
                    power.setTextColor( getResources().getColor( android.R.color.holo_green_light ) );

                } else {
                    power.setTextColor( getResources().getColor( android.R.color.black ) );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        DatabaseReference tempd = database.getReference( "temp" );
        tempd.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tempvalue = (String) dataSnapshot.getValue();
                temperature.setText( tempvalue + " °C" );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        super.onResume();
    }
}

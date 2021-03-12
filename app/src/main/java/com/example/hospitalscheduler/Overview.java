package com.example.hospitalscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import static com.example.hospitalscheduler.Constants.*;

public class Overview extends AppCompatActivity {

    List<OperatingTheatre> lstOTv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        ArrayList<Operation> schedule1 = new ArrayList<>();
        schedule1.add(new Operation("Head Deflation", NEURO, 0,
                "Dr. John Zoidberg", "Annie",
                "JD", "Rev. Nichols",
                null, null,
                0, 0, null));

        ArrayList<Operation> schedule2 = new ArrayList<>();
        schedule2.add(new Operation("Vascularitis", VASCULAR, 0,
                "Dr. Nick Riviera", "Annie",
                "Turk", "Archbishop Tutu",
                null, null,
                0, 0, null));

        ArrayList<Operation> schedule3 = new ArrayList<>();
        schedule3.add(new Operation("Blocked Nose", HEAD_AND_NECK, 0,
                "Dr. Perry Cox", "Tess",
                "Todd", "Francis",
                null, null,
                0, 0, null));

        ArrayList<Operation> schedule4 = new ArrayList<>();
        schedule4.add(new Operation("Spare Ribs", ORTHO, 0,
                "Dr. Greg House", "Dave",
                "Elliot", "Tom Cruise",
                null, null,
                0, 0, null));

        ArrayList<Operation> schedule5 = new ArrayList<>();
        schedule5.add(new Operation("Broken Heart", CARDIOTHOR, 0,
                "Dr. Who", "Greg",
                "Amelia", "Derrick Alek",
                null, null,
                0, 0, null));

        lstOTv2 = new ArrayList<>();
        lstOTv2.add(new OperatingTheatre(1, 0, schedule1));
        lstOTv2.add(new OperatingTheatre(2, 0, schedule2));
        lstOTv2.add(new OperatingTheatre(3, 0, schedule3));
        lstOTv2.add(new OperatingTheatre(4, 0, schedule4));
        lstOTv2.add(new OperatingTheatre(5, 0, schedule5));

        RecyclerView my_rv = (RecyclerView) findViewById(R.id.recylerview_id);
        OTRecyclerViewAdapter myAdapter = new OTRecyclerViewAdapter(this, lstOTv2);
        my_rv.setLayoutManager(new GridLayoutManager(this, 1));
        my_rv.setAdapter(myAdapter);






    }
}
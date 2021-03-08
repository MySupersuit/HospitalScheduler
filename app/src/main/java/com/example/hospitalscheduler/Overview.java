package com.example.hospitalscheduler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Overview extends AppCompatActivity {

    List<OperatingTheatre> lstOT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        lstOT = new ArrayList<>();
        lstOT.add(new OperatingTheatre(1, false, "10:00",
                "Dr. Nick Riviera", R.drawable.humanbrain, "Head Deflation",2));
        lstOT.add(new OperatingTheatre(2, false, "11:00",
                "Dr. John Zoidberg", R.drawable.vascularicon, "Vascularitis",1));
        lstOT.add(new OperatingTheatre(3, false, "9:30",
                "Dr. Perry Cox", R.drawable.ent_icon, "Blocked Nose",2));
        lstOT.add(new OperatingTheatre(4, false, "9:50",
                "Dr. Greg House", R.drawable.intestine_icon, "Spare Ribs",3));
        lstOT.add(new OperatingTheatre(5, false, "10:15",
                "Dr. Elliot Reid", R.drawable.ortho_icon, "Broken Heart",1));
        lstOT.add(new OperatingTheatre(6, false, "10:00",
                "Dr. Nick Riviera", R.drawable.humanbrain, "Head Deflation",3));
        lstOT.add(new OperatingTheatre(7, false, "11:00",
                "Dr. John Zoidberg", R.drawable.vascularicon, "Vascularitis",1));
        lstOT.add(new OperatingTheatre(8, false, "9:30",
                "Dr. Perry Cox", R.drawable.ent_icon, "Blocked Nose",2));
        lstOT.add(new OperatingTheatre(9, false, "9:50",
                "Dr. Greg House", R.drawable.intestine_icon, "Spare Ribs",3));

        RecyclerView my_rv = (RecyclerView) findViewById(R.id.recylerview_id);
        OTRecyclerViewAdapter myAdapter = new OTRecyclerViewAdapter(this, lstOT);
        my_rv.setLayoutManager(new GridLayoutManager(this, 2));
        my_rv.setAdapter(myAdapter);

    }
}
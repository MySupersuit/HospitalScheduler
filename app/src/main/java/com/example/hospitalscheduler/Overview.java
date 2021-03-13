package com.example.hospitalscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static com.example.hospitalscheduler.Utilites.createSampleOTList;

public class Overview extends AppCompatActivity {

    LinearLayout linearLayout;
    boolean showOnlyNotified;
    ArrayList<OperatingTheatre> lstOTv2;
    ArrayList<OperatingTheatre> allOTs;
    RecyclerView my_rv;
    OTRecyclerViewAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        this.showOnlyNotified = false;


//        this.showOnlyNotified = false;
        linearLayout = (LinearLayout) findViewById(R.id.overview_linear_layout);

        lstOTv2 = createSampleOTList();
        allOTs = new ArrayList<>(lstOTv2);

        Log.d("NOTIFY", String.valueOf(lstOTv2.size()));
        my_rv = (RecyclerView) findViewById(R.id.recylerview_id);
        myAdapter = new OTRecyclerViewAdapter(this, lstOTv2);
        my_rv.setLayoutManager(new GridLayoutManager(this, 1));
        my_rv.setAdapter(myAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview_menu, menu);
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_notified:
                toggleShowNotified();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void toggleShowNotified() {
//        if currently showing only notified then show all
        if (this.showOnlyNotified) {
            lstOTv2 = this.allOTs;
            myAdapter = new OTRecyclerViewAdapter(this, lstOTv2);
            my_rv.setAdapter(myAdapter);
            this.showOnlyNotified = false;

        } else {
            // else show only the theatres notified
            int[] notifiedOTs = getNotifiedOTs();
            ArrayList<OperatingTheatre> ots = new ArrayList<>();
            for (int i = 0; i < notifiedOTs.length; i++) {
                if (notifiedOTs[i] == 1) {
                    ots.add(lstOTv2.get(i));
                }
            }
            lstOTv2 = ots;

            // Not ideal but makes and sets a new Adapter with the new list of theatres to show
            myAdapter = new OTRecyclerViewAdapter(this, lstOTv2);
            my_rv.setAdapter(myAdapter);
            this.showOnlyNotified = true;
            Log.d("NOTIFIED", String.valueOf(this.showOnlyNotified));

        }

    }

    private int[] getNotifiedOTs() {
        int[] otNotifiedStatus = new int[this.lstOTv2.size()];
        for (int i = 0; i < this.lstOTv2.size(); i++) {
            OperatingTheatre ot = lstOTv2.get(i);
            if (ot.getIsNotified() == 1) {
                otNotifiedStatus[i] = 1;
            }
        }
        return otNotifiedStatus;
    }
}
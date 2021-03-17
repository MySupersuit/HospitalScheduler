package com.example.hospitalscheduler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static com.example.hospitalscheduler.Utilites.*;

public class Overview extends AppCompatActivity {

    boolean showOnlyNotified;
    ArrayList<OperatingTheatreV2> lstOTv3;
    ArrayList<OperatingTheatreV2> allOTsv3;
    ArrayList<ArrayList<OperationV2>> allOps;
    RecyclerView my_rv;
    OTRecyclerViewAdapter myAdapter;
    SharedPreferences sharedPref;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;

    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        this.showOnlyNotified = false;
        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        spinnerOn();

        my_rv = (RecyclerView) findViewById(R.id.recylerview_id);
        my_rv.setLayoutManager(new GridLayoutManager(this, 1));
        mContext = this;

        allOps = new ArrayList<>();
        initDBReaders(new SimpleCallback<ArrayList<OperatingTheatreV2>>() {
            @Override
            public void callback(ArrayList<OperatingTheatreV2> data) {
                Log.d("DATA", "Data callback called");
                allOTsv3 = data;
                lstOTv3 = data;
                myAdapter = new OTRecyclerViewAdapter(mContext, allOTsv3);
                my_rv.setAdapter(myAdapter);
                spinnerOff();

            }
        });

        // Shared preferences necessary?
        sharedPref = getPreferences(Context.MODE_PRIVATE);
//        if (sharedPref.contains(getString(R.string.show_notified_key))) {
//            this.showOnlyNotified = sharedPref.getBoolean(getString(R.string.show_notified_key), false);
//            Log.d("PREF", String.valueOf(this.showOnlyNotified));
//        } else {
//            Log.d("NOPREF", "nopref");
//        }
    }

    // load initial data in
    public static void initDBReaders(@NonNull SimpleCallback<ArrayList<OperatingTheatreV2>> finishedCallback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://hospitalscheduler-41566-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = db.getReference("operations");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
//        ref.add
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> ot_nums = new ArrayList<>();
                ArrayList<OperatingTheatreV2> lstOTs = new ArrayList<>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ot_nums.add(ds.getKey());
                }

                for (String ot_num : ot_nums) { // 1,2,3,4,5
                    DataSnapshot ot_num_ds = snapshot.child(ot_num);

                    ArrayList<String> ops_inOT = new ArrayList<>();
                    for (DataSnapshot ds : ot_num_ds.getChildren()) {
                        ops_inOT.add(ds.getKey());
                    }

                    ArrayList<OperationV2> operationsInOT = new ArrayList<>();
                    for (String op_key : ops_inOT) {
                        DataSnapshot ds = ot_num_ds.child(op_key);
                        OperationV2 op = ds.getValue(OperationV2.class);
                        operationsInOT.add(op);
                    }
                    OperatingTheatreV2 ot = new OperatingTheatreV2(Integer.parseInt(ot_num), 0, operationsInOT);
                    lstOTs.add(ot);
                }
                finishedCallback.callback(lstOTs);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void spinnerOn() {
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressBarHolder.setAnimation(inAnimation);
        progressBarHolder.setVisibility(View.VISIBLE);
    }

    private void spinnerOff() {
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressBarHolder.setAnimation(outAnimation);
        progressBarHolder.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.overview_menu, menu);
        return true;
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
        Log.d("TOGSTATE", String.valueOf(this.showOnlyNotified));
//        if currently showing only notified then show all
        if (this.showOnlyNotified) {
            lstOTv3 = this.allOTsv3;
            myAdapter = new OTRecyclerViewAdapter(this, lstOTv3);
            my_rv.setAdapter(myAdapter);
            this.showOnlyNotified = false;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.show_notified_key), this.showOnlyNotified);
            editor.apply();
        } else {
            // else show only the theatres notified
            int [] notifiedOTsArray = getNotifiedOTsArray();
            lstOTv3 = getNotifiedOTs(notifiedOTsArray);

            // Not ideal but makes and sets a new Adapter with the new list of theatres to show
            myAdapter = new OTRecyclerViewAdapter(this, lstOTv3);
            my_rv.setAdapter(myAdapter);
            this.showOnlyNotified = true;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.show_notified_key), this.showOnlyNotified);
            editor.apply();
        }

    }

    private int[] getNotifiedOTsArray() {
        int[] otNotifiedStatus = new int[this.allOTsv3.size()];
        for (int i = 0; i < this.allOTsv3.size(); i++) {
            OperatingTheatreV2 ot = allOTsv3.get(i);
            if (ot.getIsNotified() == 1) {
                otNotifiedStatus[i] = 1;
            }
        }
        return otNotifiedStatus;
    }

    private ArrayList<OperatingTheatreV2> getNotifiedOTs(int[] notifiedOTs) {
        ArrayList<OperatingTheatreV2> ots = new ArrayList<>();
        for (int i = 0; i < notifiedOTs.length; i++) {
            if (notifiedOTs[i] == 1) {
                ots.add(lstOTv3.get(i));
            }
        }
        return ots;
    }
}
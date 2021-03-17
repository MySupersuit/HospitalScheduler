package com.example.hospitalscheduler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

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
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // Keeps a most recent list of ops to check against when updates come in
    private HashMap<String, OperationV2> opsDiffCheck;
    private HashMap<DatabaseReference, ChildEventListener> mListenerMap;

    Context mContext;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        this.showOnlyNotified = false;
        this.mListenerMap = new HashMap<>();
        this.opsDiffCheck = new HashMap<>();
        this.allOps = new ArrayList<>();
        my_rv = (RecyclerView) findViewById(R.id.recylerview_id);
        my_rv.setLayoutManager(new GridLayoutManager(this, 1));
        mContext = this;
        mView = (LinearLayout) findViewById(R.id.overview_linear_layout);

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);
        spinnerOn();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_overview);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        // Initial read of data
        singleDBRead(
                new SimpleCallback<ArrayList<ArrayList<OperationV2>>>() {
                    @Override
                    public void callback(ArrayList<ArrayList<OperationV2>> data) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.d("DATA", "Data callback called");
                        ArrayList<OperatingTheatreV2> new_ots = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            new_ots.add(new OperatingTheatreV2(i + 1, 0, data.get(i)));
                        }
                        lstOTv3 = new_ots;
                        allOTsv3 = new_ots;
                        myAdapter = new OTRecyclerViewAdapter(mContext, allOTsv3);
                        my_rv.setAdapter(myAdapter);
                        spinnerOff();
                    }
                },
                // most recent list of ops to check against when updates come in
                new SimpleCallback<HashMap<String, OperationV2>>() {
                    @Override
                    public void callback(HashMap<String, OperationV2> data) {
                        opsDiffCheck = data;
                    }
                });

        // Start the listeners
        initDBListeners(
                new SimpleCallback<HashMap<DatabaseReference, ChildEventListener>>() {
                    @Override
                    public void callback(HashMap<DatabaseReference, ChildEventListener> data) {
//                        Log.d("HASHCALL", data.toString());
                        mListenerMap = data;
                    }
                },
                // Called on child change
                new SimpleCallback<DataSnapshot>() {
                    @Override
                    public void callback(DataSnapshot ds) {
                        handleDataChange(ds);
                    }
                });

        // TODO Is notified not persisting on reload
        sharedPref = getPreferences(Context.MODE_PRIVATE);
    }

    private void handleDataChange(DataSnapshot ds) {
        // ds is the child node that has changed
        // ie. the OperationV2 object that has changed
        // Still need to find what has changed and act accordingly

        OperationV2 op = ds.getValue(OperationV2.class);
        String updateKey = op.getId();
        OperationV2 oldOp = this.opsDiffCheck.get(updateKey);
        // opsDiffCheck is a Key-Value Map
        // {op_id: OperationV2}

//        // Outline OT in Yellow?
//        Log.d("OT", String.valueOf(op.getTheatre_number()));

        Log.d("OPCHANGE", op.getTheatre_number() + " changed");
        Log.d("NEWOP", String.valueOf(op.getCurrent_stage()));
        Log.d("OLDOP", String.valueOf(oldOp.getCurrent_stage()));
        this.opsDiffCheck.put(updateKey, op);

        makeSnackbar("Refresh for new data!", mView, Snackbar.LENGTH_LONG);
    }

    // Called on refresh of data
    // Doesn't create new OperatingTheatre Objects
    // Only replaces the schedules inside the OTs
    private void refresh() {
        Log.d("REFRSH", "refreshing");
        singleDBRead(
                new SimpleCallback<ArrayList<ArrayList<OperationV2>>>() {
                    @Override
                    public void callback(ArrayList<ArrayList<OperationV2>> data) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Log.d("DATA", "Data callback called");
                        ArrayList<OperatingTheatreV2> new_ots = new ArrayList<>();
                        for (int i = 0; i < data.size(); i++) {
                            allOTsv3.get(i).setSchedule(data.get(i));
                            Log.d("isNotified", String.valueOf(allOTsv3.get(i).getIsNotified()));
                        }

                        if (showOnlyNotified) {
                            lstOTv3 = getNotifiedOTs();
                            Log.d("SZE", String.valueOf(lstOTv3.size()));
                            if (lstOTv3.size() > 0) {
                                myAdapter = new OTRecyclerViewAdapter(mContext, lstOTv3);
                            } else {
                                myAdapter = new OTRecyclerViewAdapter(mContext, allOTsv3);
                            }
                        } else {
                            myAdapter = new OTRecyclerViewAdapter(mContext, allOTsv3);
                        }

                        my_rv.setAdapter(myAdapter);
                        spinnerOff();
                    }
                },
                new SimpleCallback<HashMap<String, OperationV2>>() {
                    @Override
                    public void callback(HashMap<String, OperationV2> data) {
                        opsDiffCheck = data;
                    }
                });
    }

    // initialise the listeners on each theatre child in database
    public static void initDBListeners(@NonNull SimpleCallback<HashMap<DatabaseReference, ChildEventListener>> finishedCallback,
                                       @NonNull SimpleCallback<DataSnapshot> listenerCallback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://hospitalscheduler-41566-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = db.getReference("operations");


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> ot_nums = new ArrayList<>();
                HashMap<DatabaseReference, ChildEventListener> listenerMap = new HashMap<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    ot_nums.add(key);
                }

                for (String ot_num : ot_nums) { // 1,2,3,4,5
                    DataSnapshot ot_num_ds = snapshot.child(ot_num);

                    ChildEventListener listener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Log.d("CHILD", String.valueOf(ot_num) + " changed");
                            listenerCallback.callback(snapshot);

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    };

                    DatabaseReference dref = ot_num_ds.getRef();
                    listenerMap.put(dref, listener);

                    dref.addChildEventListener(listener);

                }
                finishedCallback.callback(listenerMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    // for initial read and refreshing
    public static void singleDBRead(@NonNull SimpleCallback<ArrayList<ArrayList<OperationV2>>> finishedCallback,
                                    @NonNull SimpleCallback<HashMap<String, OperationV2>> diffCheckCallback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance("https://hospitalscheduler-41566-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = db.getReference("operations");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> ot_nums = new ArrayList<>();
                ArrayList<OperatingTheatreV2> lstOTs = new ArrayList<>();
                ArrayList<ArrayList<OperationV2>> new_schedules = new ArrayList<>();

                HashMap<String, OperationV2> opsDiffCheck = new HashMap<>(); // id - operationV2
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
                        opsDiffCheck.put(op_key, op);
                    }

                    new_schedules.add(operationsInOT);
                }
                finishedCallback.callback(new_schedules);
                diffCheckCallback.callback(opsDiffCheck);
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
            case R.id.menu_refresh:
                mSwipeRefreshLayout.setRefreshing(true);
                refresh();
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
//            int[] notifiedOTsArray = getNotifiedOTsArray();
            lstOTv3 = getNotifiedOTs();
            if (lstOTv3.size() == 0) {
                makeSnackbar("No notified theatres", mView, Snackbar.LENGTH_SHORT);
                return;
            }

            // Not ideal but makes and sets a new Adapter with the new list of theatres to show
            myAdapter = new OTRecyclerViewAdapter(this, lstOTv3);
            my_rv.setAdapter(myAdapter);
            this.showOnlyNotified = true;

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.show_notified_key), this.showOnlyNotified);
            editor.apply();
        }

    }

//    private int[] getNotifiedOTsArray() {
//        int[] otNotifiedStatus = new int[this.allOTsv3.size()];
//        for (int i = 0; i < this.allOTsv3.size(); i++) {
//            OperatingTheatreV2 ot = allOTsv3.get(i);
//            if (ot.getIsNotified() == 1) {
//                otNotifiedStatus[i] = 1;
//            }
//        }
//        return otNotifiedStatus;
//    }

    private ArrayList<OperatingTheatreV2> getNotifiedOTs() {
        ArrayList<OperatingTheatreV2> ots = new ArrayList<>();
        for (int i = 0; i < allOTsv3.size(); i++) {
            if (allOTsv3.get(i).getIsNotified() == 1) {
                ots.add(allOTsv3.get(i));
            }
        }
        return ots;
    }
}
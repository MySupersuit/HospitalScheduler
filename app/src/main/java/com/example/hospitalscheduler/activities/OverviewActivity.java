package com.example.hospitalscheduler.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.example.hospitalscheduler.objects.OperatingTheatreV2;
import com.example.hospitalscheduler.objects.OperationV2;
import com.example.hospitalscheduler.R;
import com.example.hospitalscheduler.interfaces.SimpleCallback;
import com.example.hospitalscheduler.adapters.OTRecyclerViewAdapter;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.hospitalscheduler.utilities.Utilites.*;

public class OverviewActivity extends AppCompatActivity {

    boolean showOnlyNotified;

    ArrayList<OperatingTheatreV2> rv_OTlist; // list to go into RecyclerView
    ArrayList<OperatingTheatreV2> allOTs; // always has all OTs

    RecyclerView my_rv;
    OTRecyclerViewAdapter myAdapter;
    SharedPreferences sharedPref;

    AlphaAnimation inAnimation;
    AlphaAnimation outAnimation;
    FrameLayout progressBarHolder;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    ArrayList<ObjectAnimator> animators; // animator objs for each cardview

    // Keeps a most recent list of ops to check against when updates come in
    private HashMap<String, OperationV2> opsDiffCheck;
    private HashMap<DatabaseReference, ChildEventListener> mListenerMap;

    private HashMap<String, Integer> isNotifiedMap;

    Context mContext;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

//        writeInitDataDB();
//        return;

        createNotificationChannel(); // DO AS SOON AS APP STARTS
        mContext = this;

        sharedPref = mContext.getSharedPreferences(
                getString(R.string.is_notified_pref), Context.MODE_PRIVATE);
        isNotifiedMap = new HashMap<>();

        this.showOnlyNotified = false;
        this.mListenerMap = new HashMap<>();
        this.opsDiffCheck = new HashMap<>();
        this.allOTs = new ArrayList<>();
        this.rv_OTlist = new ArrayList<>();
        this.animators = new ArrayList<>();

        my_rv = (RecyclerView) findViewById(R.id.recylerview_id);
        my_rv.setLayoutManager(new GridLayoutManager(this, 1));
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

                        // get shared prefs here
                        isNotifiedMap = (HashMap<String, Integer>) loadMap();
                        Log.d("SIZE", String.valueOf(isNotifiedMap.size()));


                        // if in shared prefs then use that value as isNotified
                        for (int i = 0; i < data.size(); i++) {
                            if (isNotifiedMap.containsKey(String.valueOf(i + 1))) {
                                new_ots.add(new OperatingTheatreV2(i + 1, isNotifiedMap.get(String.valueOf(i + 1)), data.get(i)));
//                                rv_OTlist.add(new OperatingTheatreV2(i+1, isNotifiedMap.get(String.valueOf(i+1)), data.get(i)));
                            } else {
                                new_ots.add(new OperatingTheatreV2(i + 1, 0, data.get(i)));
//                                rv_OTlist.add(new OperatingTheatreV2(i+1, 0, data.get(i)));
                            }
//                            allOTs.add(new OperatingTheatreV2(i+1, 0, data.get(i)));
                            animators.add(new ObjectAnimator());
                        }
                        // Separate copies of lists
                        allOTs = new ArrayList<>(new_ots);
                        rv_OTlist = new ArrayList<>(new_ots);
                        Log.d("ChildCount", String.valueOf(my_rv.getChildCount()));
                        myAdapter = new OTRecyclerViewAdapter(mContext, rv_OTlist, (ot_num) -> {
                            ObjectAnimator animator = animators.get(ot_num - 1);
                            stopAnimation(animator);

                        });
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
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test_channel";
            String description = "test_description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(
                    "TEST_CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // TODO Notifications when app is closed = FCM
    // Can do with FCM - when click notify, app send registration id to server with number and add to db
    // Shouldn't happen crazy often
    // Someone wants to be notified - generally they want be flipping it the whole time
    // Someone removes notification - can also send to server to remove them
    //
    // DB LAYOUT
    // operations : {...},
    // notifications: {
    //      "1": {
    //          "id number of app notified of OT 1",
    //          "another id number"
    //      },
    //      "2": {...},
    //      "3": {...}
    // }
    //
    // Then when updating on web app query which apps should be sent push notification
    // and send it through. Eg. OT 1 gets updated, query notification["1"] and send

    private void handleDataChange(DataSnapshot ds) {
        // ds is the child node that has changed
        // ie. the OperationV2 object that has changed
        // Still need to find what has changed and act accordingly

        // Get new Operation
        OperationV2 op = ds.getValue(OperationV2.class);
        // Get updated theatre
        int updatedTheatre = op.getTheatre_number();
        String updateKey = op.getId();
        // Get old Operation by key
        OperationV2 oldOp = this.opsDiffCheck.get(updateKey);
        // opsDiffCheck is a Key-Value Map
        // {op_id: OperationV2}

        // Update the old operation
        this.opsDiffCheck.put(updateKey, op);

        // Get the schedule of operations from theatre

        ArrayList<OperationV2> sched = allOTs.get(updatedTheatre - 1).getSchedule();
        // index of old operation in schedule
        int old_op_index = sched.indexOf(oldOp);
        // replace it with the new operation
        sched.set(old_op_index, op);
        OperatingTheatreV2 op1 = allOTs.get(updatedTheatre - 1);

        op1.setSchedule(sched);
        allOTs.set(updatedTheatre - 1, op1);

        if (showOnlyNotified) {
            ArrayList<OperatingTheatreV2> to_show = getNotifiedOTs();
            rv_OTlist.clear();
            rv_OTlist.addAll(to_show);
        } else {
            ArrayList<OperatingTheatreV2> to_show = new ArrayList<>(allOTs);
            rv_OTlist.clear();
            rv_OTlist.addAll(to_show);
        }

        myAdapter.notifyDataSetChanged();

        // if haven't asked for updates then don't send notification
        if (allOTs.get(updatedTheatre - 1).getIsNotified() == 0) {
            return;
        }

        // Generating notifications

        ArrayList<String> updates = generateMessages(op, oldOp);
        String updateString = stringArrayToString(updates);
        String titleString = "OT " + op.getTheatre_number();

        // SEND notification if notified of changed OT
        Notification.Builder builder = new Notification.Builder(
                this, "TEST_CHANNEL_ID")
                .setSmallIcon(R.drawable.clock_icon)
                .setContentTitle(titleString)
                .setContentText(updateString)
                .setAutoCancel(true);

        // click notification brings to overview screen
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0,
                new Intent(mContext, OverviewActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
        // Outline OT in Yellow?
        // find changed OT in lstOTv3

        for (int i = 0; i < rv_OTlist.size(); i++) {
            if (oldOp.getTheatre_number() == rv_OTlist.get(i).getNumber()) {
                Log.d("TET", String.valueOf(op.getTheatre_number()));
                MaterialCardView cv = (MaterialCardView) my_rv.getChildAt(i);
//                cv.setStrokeWidth(30);
                animate(cv, op.getTheatre_number()); // figure out animation
                break;

            }
        }
    }

    private OperatingTheatreV2 getTheatreWithNumber(ArrayList<OperatingTheatreV2> ots, int number) {
        for (int i = 0; i < ots.size(); i++) {
            if (ots.get(i).getNumber() == number) {
                return ots.get(i);
            }
        }
        return null;
    }

    private ArrayList<String> generateMessages(OperationV2 newOp, OperationV2 oldOp) {
        ArrayList<String> messages = new ArrayList<>();
        if (!(newOp.getCurrent_stage() == oldOp.getCurrent_stage())) {
            messages.add("Moved from stage " + oldOp.getCurrent_stage() + " to " + newOp.getCurrent_stage());
        }
        if (!(newOp.getIsCovid() == oldOp.getIsCovid())) {
            messages.add((newOp.getIsCovid() == 1) ? "Has COVID" : "No longer has COVID");
        }
        if (!(newOp.getIsDelayed() == oldOp.getIsDelayed())) {
            messages.add((newOp.getIsDelayed() == 1) ? "Operation Delayed" : "Operation no longer delayed");
        }
        return messages;
    }

    private void animate(MaterialCardView cv, int ot_num) {

        stopAnimation(animators.get(ot_num - 1));

        ObjectAnimator animator = ObjectAnimator.ofArgb(
                cv, "strokeColor",
                Color.parseColor("#00FFEA00"),
                Color.parseColor("#FFFFEA00")
        );
        animator.setDuration(1000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        animator.start();
        animators.set(ot_num - 1, animator);
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
                            allOTs.get(i).setSchedule(data.get(i));
                        }

                        if (showOnlyNotified) {
                            ArrayList<OperatingTheatreV2> to_show = getNotifiedOTs();

                            if (to_show.size() > 0) {
                                rv_OTlist.clear();
                                rv_OTlist.addAll(to_show);
                                myAdapter.notifyDataSetChanged();
                            }

                        } else {
                            ArrayList<OperatingTheatreV2> to_show = new ArrayList<>(allOTs);

                            rv_OTlist.clear();
                            rv_OTlist.addAll(to_show);
                            myAdapter.notifyDataSetChanged();
                        }

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

                    // TODO handle more than just child changed
                    ChildEventListener listener = new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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
                        Log.d("LONG", ds.toString());
                        OperationV2 op = null;
//                        try {
                            op = ds.getValue(OperationV2.class);
//                            Log.d("ADD", "Add");
//                        } catch (DatabaseException e) {
//                            Log.e("DB", e.toString());
//                        }
//                        if (op != null) {
                            operationsInOT.add(op);
                            opsDiffCheck.put(op_key, op);
//                        }
//
//                        operationsInOT.add(op);
//                        opsDiffCheck.put(op_key, op);
                    }
                    Log.d("OPs", operationsInOT.toString());
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
            this.showOnlyNotified = false;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.show_notified_key), this.showOnlyNotified);
            editor.apply();

            ArrayList<OperatingTheatreV2> to_show = new ArrayList<>(allOTs);

            for (int i = 0; i < to_show.size(); i++) {
                Log.d("I", String.valueOf(to_show.get(i).getIsNotified()));
            }

            rv_OTlist.clear();
            rv_OTlist.addAll(to_show);
            myAdapter.notifyDataSetChanged();

        } else {
            // else show only the theatres notified

            // get from shared prefs
            ArrayList<OperatingTheatreV2> to_show = getNotifiedOTs();

            if (to_show.size() == 0) {
                makeSnackbar("No notified theatres", mView, Snackbar.LENGTH_SHORT);
                return;
            }
            this.showOnlyNotified = true;
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.show_notified_key), this.showOnlyNotified);
            editor.apply();

            rv_OTlist.clear();
            rv_OTlist.addAll(to_show);
            myAdapter.notifyDataSetChanged();
        }
    }

    // returns ots to be notified of
    // gets from sharedPrefs and updates allOTs?
    private ArrayList<OperatingTheatreV2> getNotifiedOTs() {
        ArrayList<OperatingTheatreV2> ots = new ArrayList<>();
        HashMap<String, Integer> map = (HashMap<String, Integer>) loadMap();
        Log.d("GETNOT", map.toString());

        for (int i = 0; i < allOTs.size(); i++) {
            String ot_num = String.valueOf(allOTs.get(i).getNumber());
            if (map.containsKey(ot_num)) {
                allOTs.get(i).setIsNotified(map.get(ot_num));
            }
            if (allOTs.get(i).getIsNotified() == 1) {
                ots.add(allOTs.get(i));
            }
        }
        return ots;

    }

    private void stopAnimation(ObjectAnimator animator) {
        if (animator.getValues() != null && animator.getValues().length > 0) {
            animator.cancel();
            animator.setDuration(0);
            animator.reverse();
        }
    }

    private void saveMap(Map<String, Integer> inputMap) {
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.is_notified_pref), Context.MODE_PRIVATE);
        if (pSharedPref != null) {
            JSONObject jsonObject = new JSONObject(inputMap);
            String jsonString = jsonObject.toString();
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove("My_map").apply();
            editor.putString("My_map", jsonString);
            editor.apply();
        }
    }

    private Map<String, Integer> loadMap() {
        Map<String, Integer> outputMap = new HashMap<>();
        SharedPreferences pSharedPref = getApplicationContext().getSharedPreferences(
                getString(R.string.is_notified_pref), Context.MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString("My_map", (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Iterator<String> keysItr = jsonObject.keys();
                while (keysItr.hasNext()) {
                    String key = keysItr.next();
                    int value = (Integer) jsonObject.get(key);
                    outputMap.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputMap;
    }

}
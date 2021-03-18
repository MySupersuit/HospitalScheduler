package com.example.hospitalscheduler;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.hospitalscheduler.Constants.*;

public final class Utilites {

    // TODO fix with remaining icons
    public static int categoryToDrawable(String category) {
        switch (category) {
            case NEURO:
                return R.drawable.humanbrain;
            case VASCULAR:
                return R.drawable.vascularicon;
            case HEAD_AND_NECK:
                return R.drawable.ent_icon;
            case ORTHO:
                return R.drawable.ortho_icon;
            case CARDIOTHOR:
                return R.drawable.intestine_icon;
            case UROLOGY:
//                return R.drawable.urology;
                return R.drawable.ic_urology_icon;
            default:
                return -1;
        }
    }

    // TODO update with all new icons
    public static String categoryToColour(String category) {
        switch (category) {
            case NEURO:
                return "#ADD8E6";
            case HEAD_AND_NECK:
                return "#FFC0CB";
            case CARDIOTHOR:
                return "#FFFFAA";
            case VASCULAR:
                return "#E6E6FA";
            case ORTHO:
                return "#FFFFFF";
            case UROLOGY:
                return "#98FB98";
            default:
                return "#FFFFFF";
//                return "errorInUtilities";
        }
    }

    public static void makeSnackbar(String message, View view, int length) {
        Snackbar snackbar = Snackbar.make(view, message, length);
        snackbar.show();
    }

    public static String stringArrayToString(ArrayList<String> strings) {
        StringBuilder sb = new StringBuilder();
        for (String s : strings) {
            sb.append(s);
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void writeInitDataDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hospitalscheduler-41566-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference("operations");

        // OT 1
        DatabaseReference ot1ref = ref.child("1");

        String op11Key = ot1ref.push().getKey();
        OperationV2 op11 = new OperationV2("Annie", NEURO,
                    op11Key,
                    0,0, "Tim Key",
                "", "Head Deflation",
                "Reggie", "JD",
                "Dr. John Zoidberg", 1);

        ot1ref.child(op11Key).setValue(op11);

        String op12Key = ot1ref.push().getKey();
        OperationV2 op12 = new OperationV2("Greg", CARDIOTHOR,
                op12Key,
                0,0, "Terence Malik",
                op11Key,  "Broken Heart",
                "Reggie", "JD",
                "Dr. John Zoidberg", 1);

        ot1ref.child(op12Key).setValue(op12);

        // OT 2
        DatabaseReference ot2ref = ref.child("2");

        String op21Key = ot2ref.push().getKey();
        OperationV2 op21 = new OperationV2("Annie", VASCULAR,
                op21Key,
                0,0, "Wes Anderson",
                "",  "Vascularitis",
                "Reginald", "Turk",
                "Dr. Nick Riviera", 2);

        ot2ref.child(op21Key).setValue(op21);

        String op22Key = ot2ref.push().getKey();
        OperationV2 op22 = new OperationV2("Annie", NEURO,
                op22Key,
                0,0, "Sofia Coppola",
                op21Key,  "Head Deflation",
                "Reggie", "JD",
                "Dr. John Zoidberg", 1);

        ot2ref.child(op22Key).setValue(op22);

        // OT 3
        DatabaseReference ot3ref = ref.child("3");

        String op31Key = ot3ref.push().getKey();
        OperationV2 op31 = new OperationV2("Tess", HEAD_AND_NECK,
                op31Key,
                0,0, "Bong Joon Ho",
                "",  "Blocked Nose",
                "Francis", "Todd",
                "Dr. Perry Cox", 3);

        ot3ref.child(op31Key).setValue(op31);

        String op32Key = ot3ref.push().getKey();
        OperationV2 op32 = new OperationV2("Dave", VASCULAR,
                op32Key,
                0,0, "Jim Jarmusch",
                op31Key,  "Vascularitis",
                "John", "Elliot",
                "Dr. Greg House", 3);

        ot3ref.child(op32Key).setValue(op32);

        // OT 4
        DatabaseReference ot4ref = ref.child("4");

        String op41Key = ot4ref.push().getKey();
        OperationV2 op41 = new OperationV2("Dave", ORTHO,
                op41Key,
                0,0, "Jim Jarmusch",
                "",  "Spare Ribs",
                "John", "Elliot",
                "Dr. Greg House", 4);

        ot4ref.child(op41Key).setValue(op41);

        String op42Key = ot4ref.push().getKey();
        OperationV2 op42 = new OperationV2("Tess", HEAD_AND_NECK,
                op42Key,
                0,0, "Bong Joon Ho",
                op41Key,  "Blocked Nose",
                "Francis", "Todd",
                "Dr. Perry Cox", 4);

        ot4ref.child(op42Key).setValue(op42);

        // OT 5
        DatabaseReference ot5ref = ref.child("5");

        String op51Key = ot5ref.push().getKey();
        OperationV2 op51 = new OperationV2("Greg", UROLOGY,
                op51Key,
                0,0, "Christopher Nolan",
                "",  "Broken Heart",
                "Mert", "Amelia",
                "Dr. Who", 5);

        ot5ref.child(op51Key).setValue(op51);

        String op52Key = ot5ref.push().getKey();
        OperationV2 op52 = new OperationV2("Dave", ORTHO,
                op52Key,
                0,0, "Jim Jarmusch",
                op51Key,  "Spare Ribs",
                "John", "Elliot",
                "Dr. Greg House", 5);

        ot5ref.child(op52Key).setValue(op52);

    }

//    public static void testDBWrite() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hospitalscheduler-41566-default-rtdb.europe-west1.firebasedatabase.app/");
//        DatabaseReference mRef = database.getReference("operations");
//
//        String key = mRef.push().getKey();
//
//        OperationDB op1 = new OperationDB(1,"Spare Ribs",
//                ORTHO, "Dr. Greg House", "Dave","Elliot",
//                "Tom Cruise", 0,0);
//
//        String key1 = mRef.push().getKey();
//        OperationDB op2 = new OperationDB(1, "Head Deflation", NEURO,
//                "Dr. John Zoidberg", "Annie", "JD", "Tom Cruise", 0, 0, key);
//
//        mRef.child(key).setValue(op1);
//        mRef.child(key1).setValue(op2);
//    }

//    public static void testDBRead() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hospitalscheduler-41566-default-rtdb.europe-west1.firebasedatabase.app/");
//        DatabaseReference mRef = database.getReference("operations");
//
//        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // gets all keys in 'operations' ref
//                // TODO change to per theatre
//                ArrayList<String> keys = new ArrayList<>();
//                for (DataSnapshot datas : snapshot.getChildren()) {
//                    keys.add(datas.getKey());
//                }
////
//                long value = snapshot.getChildrenCount();
//                Log.d("SPZ", "no of children: "+ value);
//                DataSnapshot ds = snapshot.child(keys.get(1));
//                Log.d("SPZ", ds.toString());
//                OperationDB op = ds.getValue(OperationDB.class);
//                Log.d("SPZ", op.getCategory());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w("TAG", "loadPost:onCancelled", error.toException());
//            }
//        });
//    }

    //    public static ArrayList<OperatingTheatre> createSampleOTList() {
//        ArrayList<Operation> schedule1 = new ArrayList<>();
//        schedule1.add(new Operation("Head Deflation", NEURO, 1,
//                "Dr. John Zoidberg", "Annie",
//                "JD", "Rev. Nichols", null,
//                0, 0, "20", "Diane", 1));
//        schedule1.add(new Operation("Broken Heart", CARDIOTHOR, 0,
//                "Dr. Who", "Greg",
//                "Amelia", "Derrick Alek",
//                null,
//                0, 0, null, "Roger", 1));
//
//        ArrayList<Operation> schedule2 = new ArrayList<>();
//        schedule2.add(new Operation("Vascularitis", VASCULAR, 3,
//                "Dr. Nick Riviera", "Annie",
//                "Turk", "Archbishop Tutu",
//                null, 0, 0, "6",
//                "Jane", 2));
//        schedule2.add(new Operation("Head Deflation", NEURO, 0,
//                "Dr. John Zoidberg", "Annie",
//                "JD", "Rev. Nichols", null,
//                0, 0, null,
//                "Dave", 2));
//
//        ArrayList<Operation> schedule3 = new ArrayList<>();
//        schedule3.add(new Operation("Blocked Nose", HEAD_AND_NECK, 0,
//                "Dr. Perry Cox", "Tess",
//                "Todd", "Francis",
//                null,
//                0, 0, null,
//                "Chad", 3));
//        schedule3.add(new Operation("Vascularitis", VASCULAR, 0,
//                "Dr. Nick Riviera", "Annie",
//                "Turk", "Archbishop Tutu",
//                null,
//                0, 0, null,
//                "Ted", 3));
//
//        ArrayList<Operation> schedule4 = new ArrayList<>();
//        schedule4.add(new Operation("Spare Ribs", ORTHO, 0,
//                "Dr. Greg House", "Dave",
//                "Elliot", "Tom Cruise",
//                null,
//                0, 0, null,
//                "John", 4));
//        schedule4.add(new Operation("Blocked Nose", HEAD_AND_NECK, 0,
//                "Dr. Perry Cox", "Tess",
//                "Todd", "Francis",
//                null,
//                0, 0, null,
//                "Sarah", 4));
//
//        ArrayList<Operation> schedule5 = new ArrayList<>();
//        schedule5.add(new Operation("Broken Heart", UROLOGY, 0,
//                "Dr. Who", "Greg",
//                "Amelia", "Derrick Alek",
//                null,
//                0, 0, null,
//                "Mert", 5));
//        schedule5.add(new Operation("Spare Ribs", ORTHO, 0,
//                "Dr. Greg House", "Dave",
//                "Elliot", "Tom Cruise",
//                null,
//                0, 0, null,
//                "Jolene", 5));
//
//        ArrayList<OperatingTheatre> lstOTv2 = new ArrayList<>();
//        lstOTv2.add(new OperatingTheatre(1, 0, schedule1));
//        lstOTv2.add(new OperatingTheatre(2, 0, schedule2));
//        lstOTv2.add(new OperatingTheatre(3, 0, schedule3));
//        lstOTv2.add(new OperatingTheatre(4, 0, schedule4));
//        lstOTv2.add(new OperatingTheatre(5, 0, schedule5));
//
//        return lstOTv2;
//    }
}

package com.example.hospitalscheduler.utilities;

import android.util.Log;
import android.view.View;

import com.example.hospitalscheduler.R;
import com.example.hospitalscheduler.objects.Comment;
import com.example.hospitalscheduler.objects.OperationV2;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.hospitalscheduler.utilities.Constants.*;

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
                return R.drawable.no_data_icon;
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
                return "#FFCC80";
            case UROLOGY:
                return "#98FB98";
            case NO_OP:
                return "#EAEAEA";
            default:
                return "#FFFFFF";
        }
    }

    public static String categoryToSecColour(String category) {
        switch (category) {
            case NEURO:
                return "#7898a1";
            case HEAD_AND_NECK:
                return "#b3878e";
            case CARDIOTHOR:
                return "#b3b377";
            case VASCULAR:
                return "#a1a1af";
            case ORTHO:
                return "#FF9800";
            case UROLOGY:
                return "#6bb06b";
            default:
                return "#FFFFFF";
//                return "errorInUtilities";
        }
    }

    public static String categoryToTextColour(String category) {
        switch (category) {
            case NEURO:
                return "#4e636a";
            case HEAD_AND_NECK:
                return "#75595d";
            case CARDIOTHOR:
                return "#75754e";
            case VASCULAR:
                return "#6a6a73";
            case ORTHO:
                return "#757575";
            case UROLOGY:
                return "#467346";
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

    public static String epochTimeToHourMin(long time) {
        Date d = new Date(time*1000);
        return new SimpleDateFormat("HH:mm", Locale.getDefault()).format(d);
    }

    public static String epochTimeToDateHourMin(long time) {
        Date d = new Date(time*1000);
        return new SimpleDateFormat("dd/MM HH:mm", Locale.getDefault()).format(d);
    }

    // Assume more than 999 minutes is error
    public static String getMinutesSince(long timestamp) {
        long curr_time = System.currentTimeMillis() / 1000L;
        long difference = curr_time - timestamp;
        int mins = (int) Math.ceil((double) difference / 60);
        return (mins > 999) ? "0" : String.valueOf(mins);
    }

    public static String timeSince(long timestamp) {
        long curr_time = System.currentTimeMillis() / 1000L;
        long sec_difference = curr_time - timestamp;
        if (sec_difference < SECONDS_IN_MIN) return sec_difference + " seconds ";
        if (sec_difference < SECONDS_IN_HOUR) {
            int mins = (int) Math.round((double) sec_difference / SECONDS_IN_MIN);
            return mins + ((mins > 1) ? " minutes" : " minute");
        }
        if (sec_difference < SECONDS_IN_DAY) {
            int hours = (int) Math.round((double) sec_difference / SECONDS_IN_HOUR);
            return hours + ((hours > 1) ? " hours" : " hour");
        }
        if (sec_difference < SECONDS_IN_30DAYS) {
            int days = (int) Math.round((double) sec_difference / SECONDS_IN_DAY);
            return days + ((days > 1) ? " days" : " day");
        }
        if (sec_difference < SECONDS_IN_YEAR) {
            int months = (int) Math.round((double) sec_difference / SECONDS_IN_30DAYS);
            return months + ((months > 1) ? " months" : " month");
        }
        return "Years";

    }

    public static OperationV2 getCurrentOperation(ArrayList<OperationV2> schedule) {
        if (schedule.size() <= 0) {
            return null;
        }
        for (OperationV2 op : schedule) {
            if (op.getCurrent_stage() <= NUM_STAGES) {
                return op;
            }
        }
        return null;
    }

    // could be bad, just takes operation after Current Operation
    public static OperationV2 getNextOperation(ArrayList<OperationV2> schedule, OperationV2 curr) {
        int curr_index = schedule.indexOf(curr);
        if (curr_index+1 >= schedule.size()) {
            return null;
        } else {
            return schedule.get(curr_index+1);
        }
    }

    public static boolean isCurrentOperation(ArrayList<OperationV2> schedule, OperationV2 op) {
        OperationV2 curr = getCurrentOperation(schedule);
        return curr.getId().equals(op.getId());
    }

    public static boolean isNextOperation(ArrayList<OperationV2> schedule, OperationV2 op) {
        OperationV2 curr = getCurrentOperation(schedule);
        OperationV2 next = getNextOperation(schedule, curr);
        return next.getId().equals(op.getId());
    }

    public static Comment getMostRecentComment(List<Comment> comments) { ;
        if (comments == null) return null;
        if (comments.size() == 0) return null;
        Collections.sort(comments);
        return comments.get(0);
    }

    // in form YYYY/MM/DD
    public static String getAgeFromDOB(String dob) {
        String[] splits = dob.split("/");
        int year = Integer.parseInt(splits[0]);
        int month = Integer.parseInt(splits[1]);
        int day = Integer.parseInt(splits[2]);
        LocalDate birthdate = LocalDate.of(year, month, day);
        LocalDate now = LocalDate.now();
        return String.valueOf(Period.between(birthdate, now).getYears());
    }

    public static void writeInitDataDB() {
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://hospitalscheduler-41566-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference ref = database.getReference("operations");

        // OT 1
        DatabaseReference ot1ref = ref.child("1");

        ArrayList<Comment> commentsTest = new ArrayList<>();
//        commentsTest.add(new Comment("test", -1));

        String op11Key = ot1ref.push().getKey();

        OperationV2 op11 = new OperationV2("Annie", NEURO,
                    op11Key,
                    0,0, "Tim Key",
                "", "Head Deflation",
                "Reggie", "JD",
                "Dr. John Zoidberg", 1, commentsTest,
                "M", "1976/09/02");

        ot1ref.child(op11Key).setValue(op11);

        String op12Key = ot1ref.push().getKey();
        OperationV2 op12 = new OperationV2("Greg", CARDIOTHOR,
                op12Key,
                0,0, "Terence Malik",
                op11Key,  "Broken Heart",
                "Reggie", "JD",
                "Dr. John Zoidberg", 1, commentsTest,
                "M", "1943/11/03");

        ot1ref.child(op12Key).setValue(op12);

        String op13Key = ot1ref.push().getKey();
        OperationV2 op13 = new OperationV2("Suri", HEAD_AND_NECK,
                op13Key,
                0,0,"Sam Shephard",
                op12Key, "Lower impacted wisdom tooth extraction",
                "Greg", "Ted",
                "Dr. Julius Hibbert", 1, commentsTest,
                "M", "1943/11/05");

        ot1ref.child(op13Key).setValue(op13);

        String op14Key = ot1ref.push().getKey();
        OperationV2 op14 = new OperationV2("Jack", ORTHO,
                op14Key,
                1,0,"Doc Torrance",
                op13Key, "Visions of Tony",
                "Matt", "Carla",
                "Dr. Strangelove", 1, commentsTest,
                "M", "1971/04/22");
        ot1ref.child(op14Key).setValue(op14);

        // OT 2
        DatabaseReference ot2ref = ref.child("2");

        String op21Key = ot2ref.push().getKey();
        OperationV2 op21 = new OperationV2("Annie", VASCULAR,
                op21Key,
                0,0, "Wes Anderson",
                "",  "Vascularitis",
                "Reginald", "Turk",
                "Dr. Nick Riviera", 2, commentsTest,
                "M", "1969/05/01");

        ot2ref.child(op21Key).setValue(op21);

        String op22Key = ot2ref.push().getKey();
        OperationV2 op22 = new OperationV2("Annie", NEURO,
                op22Key,
                0,0, "Sofia Coppola",
                op21Key,  "Head Deflation",
                "Reggie", "JD",
                "Dr. John Zoidberg", 2, commentsTest,
                "F", "1971/05/14");

        ot2ref.child(op22Key).setValue(op22);

        // OT 3
        DatabaseReference ot3ref = ref.child("3");

        String op31Key = ot3ref.push().getKey();
        OperationV2 op31 = new OperationV2("Tess", HEAD_AND_NECK,
                op31Key,
                0,0, "Bong Joon Ho",
                "",  "Blocked Nose",
                "Francis", "Todd",
                "Dr. Perry Cox", 3, commentsTest,
                "M", "1969/09/14");

        ot3ref.child(op31Key).setValue(op31);

        String op32Key = ot3ref.push().getKey();
        OperationV2 op32 = new OperationV2("Dave", VASCULAR,
                op32Key,
                0,0, "Jim Jarmusch",
                op31Key,  "Vascularitis",
                "John", "Elliot",
                "Dr. Greg House", 3, commentsTest,
                "M", "1953/01/22");

        ot3ref.child(op32Key).setValue(op32);

        // OT 4
        DatabaseReference ot4ref = ref.child("4");

        String op41Key = ot4ref.push().getKey();
        OperationV2 op41 = new OperationV2("Dave", ORTHO,
                op41Key,
                0,0, "Jim Jarmusch",
                "",  "Spare Ribs",
                "John", "Elliot",
                "Dr. Greg House", 4, commentsTest,
                "M", "1953/01/22");

        ot4ref.child(op41Key).setValue(op41);

        String op42Key = ot4ref.push().getKey();
        OperationV2 op42 = new OperationV2("Tess", HEAD_AND_NECK,
                op42Key,
                0,0, "Bong Joon Ho",
                op41Key,  "Blocked Nose",
                "Francis", "Todd",
                "Dr. Perry Cox", 4, commentsTest,
                "M", "1953/01/22");

        ot4ref.child(op42Key).setValue(op42);

        // OT 5
        DatabaseReference ot5ref = ref.child("5");

        String op51Key = ot5ref.push().getKey();
        OperationV2 op51 = new OperationV2("Greg", UROLOGY,
                op51Key,
                0,0, "Christopher Nolan",
                "",  "Broken Heart",
                "Mert", "Amelia",
                "Dr. Who", 5, commentsTest,
                "M", "1970/07/30");

        ot5ref.child(op51Key).setValue(op51);

        String op52Key = ot5ref.push().getKey();
        OperationV2 op52 = new OperationV2("Dave", ORTHO,
                op52Key,
                0,0, "Jim Jarmusch",
                op51Key,  "Spare Ribs",
                "John", "Elliot",
                "Dr. Greg House", 5, commentsTest,
                "M", "1953/01/22");

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
